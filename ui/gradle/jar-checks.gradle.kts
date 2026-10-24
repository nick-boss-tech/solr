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

import java.security.MessageDigest
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.api.artifacts.component.ProjectComponentIdentifier

// Desktop artifacts are resolved for every target, not only the host OS, so the result
// does not depend on the machine running the build.
val licensesDir = layout.projectDirectory.dir("licenses")
val composeVersion = extensions.getByType<VersionCatalogsExtension>().named("libs").findVersion("compose").get().requiredVersion
val desktopTargets = listOf("linux-x64", "linux-arm64", "macos-x64", "macos-arm64", "windows-x64")
val uiModules = listOf("shared", "desktopApp", "webApp")
val shippedConfigurations = listOf("jvmRuntimeClasspath", "wasmJsRuntimeClasspath", "runtimeClasspath")
val checksumOnlyConfigurations = listOf(
    "jvmCompileClasspath", "jvmTestCompileClasspath", "jvmTestRuntimeClasspath",
    "wasmJsCompileClasspath", "wasmJsTestCompileClasspath", "wasmJsTestRuntimeClasspath",
    "compileClasspath", "testCompileClasspath", "testRuntimeClasspath",
)
// Mirrors the noticeOptional flags of gradle/validation/jar-checks.gradle.
val licenseTypes = mapOf("ASL" to false, "BSD" to false, "EPL" to true, "MIT" to true)

val desktopAllOs by lazy {
  configurations.detachedConfiguration(
    *desktopTargets.map { dependencies.create("org.jetbrains.compose.desktop:desktop-jvm-$it:$composeVersion") }.toTypedArray()
  )
}

fun uiArtifacts(shipped: Boolean): Map<String, ResolvedArtifact> {
  val result = sortedMapOf<String, ResolvedArtifact>()
  val configurationNames = if (shipped) shippedConfigurations else shippedConfigurations + checksumOnlyConfigurations
  uiModules.forEach { module ->
    val p = project(":$module")
    configurationNames.forEach { name ->
      p.configurations.findByName(name)?.resolvedConfiguration?.resolvedArtifacts
        ?.filter { it.id.componentIdentifier !is ProjectComponentIdentifier }
        ?.forEach { result.putIfAbsent(it.file.name, it) }
    }
  }
  desktopAllOs.resolvedConfiguration.resolvedArtifacts.forEach { result.putIfAbsent(it.file.name, it) }
  return result
}

fun sha1(file: File): String =
  MessageDigest.getInstance("SHA-1").digest(file.readBytes()).joinToString("") { "%02x".format(it) }

val updateLicenses = tasks.register("updateLicenses") {
  group = "Dependency validation"
  description = "Write SHA-1 checksums of the UI dependencies"
  outputs.upToDateWhen { false }
  doLast {
    val dir = licensesDir.asFile
    dir.mkdirs()
    dir.listFiles { f -> f.name.endsWith(".sha1") }?.forEach { it.delete() }
    uiArtifacts(shipped = false).forEach { (fileName, artifact) ->
      dir.resolve("$fileName.sha1").writeText(sha1(artifact.file) + "\n")
    }
  }
}

val validateJarChecksums = tasks.register("validateJarChecksums") {
  group = "Dependency validation"
  description = "Validate SHA-1 checksums of the UI dependencies"
  outputs.upToDateWhen { false }
  doLast {
    val errors = mutableListOf<String>()
    uiArtifacts(shipped = false).forEach { (fileName, artifact) ->
      val expectedFile = licensesDir.file("$fileName.sha1").asFile
      if (!expectedFile.exists()) {
        errors += "Dependency checksum missing ('${artifact.moduleVersion.id}'), expected it at: $expectedFile"
      } else {
        val expected = expectedFile.readText().trim()
        val actual = sha1(artifact.file)
        if (!expected.equals(actual, ignoreCase = true)) {
          errors += "Dependency checksum mismatch ('${artifact.moduleVersion.id}'), expected it to be: $expected, but was: $actual"
        }
      }
    }
    if (errors.isNotEmpty()) {
      throw GradleException(
        "Dependency checksum validation failed:\n  - " + errors.joinToString("\n  - ") +
          "\n\nThese missing checksums can be generated using:\n  ./gradlew -p ui updateLicenses"
      )
    }
  }
}

val validateJarLicenses = tasks.register("validateJarLicenses") {
  group = "Dependency validation"
  description = "Validate license and notice files of the shipped UI dependencies"
  outputs.upToDateWhen { false }
  doLast {
    val errors = mutableListOf<String>()
    uiArtifacts(shipped = true).values.forEach { artifact ->
      var baseName = artifact.name
      var found: List<File>
      while (true) {
        found = licensesDir.asFile.listFiles { f ->
          f.name.startsWith("$baseName-LICENSE-") && f.name.endsWith(".txt")
        }?.toList() ?: emptyList()
        val prefix = baseName.replace(Regex("-[^-]+$"), "")
        if (found.isNotEmpty() || prefix == baseName) break
        baseName = prefix
      }

      when {
        found.isEmpty() -> errors +=
          "License file missing ('${artifact.moduleVersion.id}'), expected it at: ${licensesDir.asFile}/$baseName-LICENSE-[type].txt, " +
            "where [type] can be any of ${licenseTypes.keys}."
        found.size > 1 -> errors += "Multiple license files matching for ('${artifact.moduleVersion.id}'): ${found.joinToString(", ")}"
        else -> {
          val licenseFile = found.single()
          val licenseName = licenseFile.name.substringAfter("-LICENSE-").removeSuffix(".txt")
          val noticeOptional = licenseTypes[licenseName]
          if (noticeOptional == null) {
            errors += "Unknown license type suffix for ('${artifact.moduleVersion.id}'): $licenseFile (must be one of ${licenseTypes.keys})"
          } else {
            val noticeFile = File(licenseFile.path.replace(Regex("-LICENSE-.+"), "-NOTICE.txt"))
            if (!noticeFile.exists() && !noticeOptional) {
              errors += "Notice file missing for ('${artifact.moduleVersion.id}'), expected it at: $noticeFile"
            }
          }
        }
      }
    }
    if (errors.isNotEmpty()) {
      throw GradleException("Certain license/ notice files are missing:\n  - " + errors.joinToString("\n  - "))
    }
  }
}

val licenses = tasks.register("licenses") {
  group = "Dependency validation"
  description = "Validate checksums, license and notice files of the UI dependencies"
  dependsOn(validateJarChecksums, validateJarLicenses)
}

tasks.named("check") {
  dependsOn(licenses)
}
