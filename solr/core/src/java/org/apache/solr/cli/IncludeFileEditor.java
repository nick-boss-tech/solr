/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.solr.cli;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.AclEntry;
import java.nio.file.attribute.AclFileAttributeView;
import java.nio.file.attribute.GroupPrincipal;
import java.nio.file.attribute.PosixFileAttributeView;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.nio.file.attribute.UserPrincipal;
import java.util.EnumSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

/** Edits Solr shell and Windows include files without changing unrelated settings. */
final class IncludeFileEditor {
  enum Format {
    SHELL(Pattern.compile("\\s*(?:export\\s+)?([A-Za-z_][A-Za-z0-9_]*)\\s*=.*"), "# "),
    WINDOWS(Pattern.compile("\\s*(?i:set)\\s+\"?([A-Za-z_][A-Za-z0-9_]*)\\s*=.*"), "REM ");

    private final Pattern assignment;
    private final String comment;

    Format(Pattern assignment, String comment) {
      this.assignment = assignment;
      this.comment = comment;
    }

    private String variable(String line) {
      var matcher = assignment.matcher(line);
      if (!matcher.matches()) return null;
      return this == WINDOWS ? matcher.group(1).toUpperCase(Locale.ROOT) : matcher.group(1);
    }
  }

  private IncludeFileEditor() {}

  static String update(
      String original, Map<String, String> settings, Set<String> replaced, Format format) {
    String newline = newline(original, format);
    StringBuilder output = new StringBuilder();
    for (String line : original.split("\\R")) {
      String variable = format.variable(line);
      if (variable == null || !replaced.contains(variable)) output.append(line).append(newline);
    }
    settings.forEach(
        (key, value) -> {
          if (!key.matches("[A-Za-z_][A-Za-z0-9_]*")
              || value.indexOf('\n') >= 0
              || value.indexOf('\r') >= 0
              || value.indexOf('\0') >= 0) {
            throw new IllegalArgumentException(
                "Include-file settings must be single-line variable assignments.");
          }
          if (format == Format.WINDOWS) {
            output.append("set ").append(key).append('=').append(value);
          } else {
            output.append(key).append("='").append(value.replace("'", "'\"'\"'")).append("'");
          }
          output.append(newline);
        });
    return output.toString();
  }

  static String disable(String original, Set<String> settings, Format format) {
    String newline = newline(original, format);
    StringBuilder output = new StringBuilder();
    boolean changed = false;
    for (String line : original.split("\\R")) {
      String variable = format.variable(line);
      if (variable != null && settings.contains(variable)) {
        output.append(format.comment);
        changed = true;
      }
      output.append(line).append(newline);
    }
    return changed ? output.toString() : original;
  }

  private static String newline(String original, Format format) {
    return original.contains("\r\n") || (original.isEmpty() && format == Format.WINDOWS)
        ? "\r\n"
        : "\n";
  }

  static Path backup(Path path) throws IOException {
    Path target = path.toRealPath();
    Path backup = Files.createTempFile(target.getParent(), target.getFileName() + ".", ".bak");
    try {
      FileAccess.read(target).privateAccess().apply(backup);
      Files.write(backup, Files.readAllBytes(target));
      return backup;
    } catch (IOException failure) {
      Files.deleteIfExists(backup);
      throw failure;
    }
  }

  static Path save(Path path, String content) throws IOException {
    Path backup = backup(path);
    writePrivate(path, content.getBytes(StandardCharsets.UTF_8));
    return backup;
  }

  static void writePrivate(Path path, byte[] data) throws IOException {
    writePrivate(path, data, null);
  }

  /** New POSIX files use the include file's owner and private service-group access. */
  static void writePrivate(Path path, byte[] data, Path reference) throws IOException {
    write(path, data, accessFor(path, reference));
  }

  static void checkWritable(Path path, Path reference) throws IOException {
    Path target = target(path);
    if (!Files.isDirectory(target.getParent())
        || !Files.isWritable(target.getParent())
        || (Files.exists(target)
            && (!Files.isRegularFile(target)
                || !Files.isReadable(target)
                || !Files.isWritable(target)))) {
      throw new IOException(
          "Configuration file and its parent directory must be writable: " + path);
    }
    Path probe = Files.createTempFile(target.getParent(), ".solr-config-", ".tmp");
    try {
      accessFor(path, reference).apply(probe);
    } finally {
      Files.deleteIfExists(probe);
    }
  }

  record Snapshot(byte[] data, FileAccess access) {}

  static Snapshot snapshot(Path path) throws IOException {
    return Files.exists(path)
        ? new Snapshot(Files.readAllBytes(path), FileAccess.read(path))
        : null;
  }

  static void restore(Path path, Snapshot snapshot) throws IOException {
    if (snapshot == null) Files.deleteIfExists(path);
    else write(path, snapshot.data(), snapshot.access());
  }

  private static Path target(Path path) throws IOException {
    return Files.exists(path) ? path.toRealPath() : path.toAbsolutePath().normalize();
  }

  private static FileAccess accessFor(Path path, Path reference) throws IOException {
    if (Files.exists(path)) return FileAccess.read(path).privateAccess();
    if (reference != null
        && reference.getFileSystem().supportedFileAttributeViews().contains("posix")) {
      return FileAccess.read(reference).privateAccess();
    }
    return new FileAccess(null, null, PosixFilePermissions.fromString("rw-------"), null);
  }

  private static void write(Path path, byte[] data, FileAccess access) throws IOException {
    Path target = target(path);
    Path staged = Files.createTempFile(target.getParent(), ".solr-config-", ".tmp");
    try {
      access.apply(staged);
      Files.write(staged, data);
      Files.move(
          staged, target, StandardCopyOption.ATOMIC_MOVE, StandardCopyOption.REPLACE_EXISTING);
    } finally {
      Files.deleteIfExists(staged);
    }
  }

  private record FileAccess(
      UserPrincipal owner,
      GroupPrincipal group,
      Set<PosixFilePermission> permissions,
      List<AclEntry> acl) {
    static FileAccess read(Path path) throws IOException {
      var posix = Files.getFileAttributeView(path, PosixFileAttributeView.class);
      if (posix != null) {
        var attributes = posix.readAttributes();
        return new FileAccess(
            attributes.owner(), attributes.group(), Set.copyOf(attributes.permissions()), null);
      }
      var view = Files.getFileAttributeView(path, AclFileAttributeView.class);
      return view == null
          ? new FileAccess(null, null, null, null)
          : new FileAccess(view.getOwner(), null, null, List.copyOf(view.getAcl()));
    }

    FileAccess privateAccess() {
      if (permissions == null) return this;
      var restricted = EnumSet.of(PosixFilePermission.OWNER_READ, PosixFilePermission.OWNER_WRITE);
      // Retain an explicitly private service group, but never share secrets from a public include.
      if (!permissions.contains(PosixFilePermission.OTHERS_READ)
          && !permissions.contains(PosixFilePermission.OTHERS_WRITE)
          && !permissions.contains(PosixFilePermission.OTHERS_EXECUTE)) {
        if (permissions.contains(PosixFilePermission.GROUP_READ)) {
          restricted.add(PosixFilePermission.GROUP_READ);
        }
        if (permissions.contains(PosixFilePermission.GROUP_WRITE)) {
          restricted.add(PosixFilePermission.GROUP_WRITE);
        }
      }
      return new FileAccess(owner, group, restricted, acl);
    }

    void apply(Path path) throws IOException {
      var posix = Files.getFileAttributeView(path, PosixFileAttributeView.class);
      if (posix != null) {
        var current = posix.readAttributes();
        if (owner != null && !owner.equals(current.owner())) posix.setOwner(owner);
        if (group != null && !group.equals(current.group())) posix.setGroup(group);
        if (permissions != null) posix.setPermissions(permissions);
      } else if (acl != null) {
        var view = Files.getFileAttributeView(path, AclFileAttributeView.class);
        if (view == null) throw new IOException("Cannot preserve file ACL: " + path);
        if (!owner.equals(view.getOwner())) view.setOwner(owner);
        view.setAcl(acl);
      }
    }
  }
}
