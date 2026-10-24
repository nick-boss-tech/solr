"""Prepare pinned Java-only compatibility proof inputs; never invokes a build tool."""
from pathlib import Path
from concurrent.futures import ThreadPoolExecutor
import difflib
import hashlib
import json
import shutil
import urllib.request
import zipfile

ROOT = Path(__file__).resolve().parent
MAVEN = "https://repo.maven.apache.org/maven2/"


def download(spec):
    group, artifact, version, suffix = spec
    name = f"{artifact}-{version}{suffix}"
    url = MAVEN + group.replace(".", "/") + f"/{artifact}/{version}/{name}"
    target = ROOT / "jars" / name
    target.parent.mkdir(exist_ok=True)
    if not target.exists():
        with urllib.request.urlopen(url) as response:
            target.write_bytes(response.read())
    # Maven's published digest guards against truncated/corrupted cached downloads.
    with urllib.request.urlopen(url + ".sha1") as response:
        expected = response.read().decode().strip().split()[0]
    assert hashlib.sha1(target.read_bytes()).hexdigest() == expected, name
    return {"file": name, "url": url, "sha256": hashlib.sha256(target.read_bytes()).hexdigest()}


def main():
    metadata = json.loads((ROOT / "release-metadata.json").read_text())
    for relative in metadata["files"]:
        source = ROOT / "release" / relative
        if not source.exists():
            url = f"https://raw.githubusercontent.com/{metadata['repo']}/{metadata['base']}/{relative}"
            with urllib.request.urlopen(url) as response:
                data = response.read()
            source.parent.mkdir(parents=True, exist_ok=True)
            source.write_bytes(data)
        data = source.read_bytes()
        blob = hashlib.sha1(b"blob " + str(len(data)).encode() + b"\0" + data).hexdigest()
        assert blob == metadata["blobs"][relative], relative
    toolchain = ROOT / "toolchain"
    if not (toolchain / "jdk-22.0.2+9/bin/javac.exe").exists():
        archive_path = ROOT / "jdk22.zip"
        if not archive_path.exists():
            url = ("https://github.com/adoptium/temurin22-binaries/releases/download/"
                   "jdk-22.0.2%2B9/OpenJDK22U-jdk_x64_windows_hotspot_22.0.2_9.zip")
            with urllib.request.urlopen(url) as response:
                archive_path.write_bytes(response.read())
        assert hashlib.sha256(archive_path.read_bytes()).hexdigest() == (
            "bf61e74c3dbd2da27d6415a804813c5ade87f9c838acca1edfa08ce9155bd82b")
        with zipfile.ZipFile(ROOT / "jdk22.zip") as archive:
            for item in archive.infolist():
                dest = (toolchain / item.filename).resolve()
                assert dest.is_relative_to(toolchain.resolve())
            archive.extractall(toolchain)
    specs = []
    for version in ("10.2.0", "10.4.0"):
        for artifact in ("lucene-core", "lucene-backward-codecs", "lucene-codecs"):
            specs.append(("org.apache.lucene", artifact, version, ".jar"))
        specs.append(("org.apache.lucene", "lucene-core", version, "-sources.jar"))
    for version in ("25.12.0", "26.10.0"):
        for suffix in (".jar", "-sources.jar"):
            specs.append(("com.nvidia.cuvs.lucene", "cuvs-lucene", version, suffix))
        specs.append(("com.nvidia.cuvs", "cuvs-java", version, ".jar"))
    with ThreadPoolExecutor(max_workers=6) as pool:
        manifest = list(pool.map(download, specs))
    (ROOT / "artifact-manifest.json").write_text(json.dumps(manifest, indent=2) + "\n")
    java_path = "java/cuvs-lucene/src/main/java/com/nvidia/cuvs/lucene/AcceleratedHNSWUtils.java"
    before = (ROOT / "release" / java_path).read_text(encoding="utf-8")
    helper = """  private static int[] getSortedNodes(NodesIterator nodesOnLevel) {
    int[] sortedNodes = new int[nodesOnLevel.size()];
    for (int n = 0; nodesOnLevel.hasNext(); n++) {
      sortedNodes[n] = nodesOnLevel.nextInt();
    }
    Arrays.sort(sortedNodes);
    return sortedNodes;
  }

"""
    # Put the helper immediately before writeGraph's existing Javadoc.
    method_pos = before.index("  public static int[][] writeGraph(")
    doc_pos = before.rfind("  /**", 0, method_pos)
    after = before[:doc_pos] + helper + before[doc_pos:]
    assert after.count("NodesIterator.getSortedNodes(") == 1
    after = after.replace("NodesIterator.getSortedNodes(", "getSortedNodes(")
    patched = ROOT / "patched" / java_path
    patched.parent.mkdir(parents=True, exist_ok=True)
    patched.write_text(after, encoding="utf-8", newline="\n")
    diff = "diff --git a/{0} b/{0}\n".format(java_path)
    diff += "".join(difflib.unified_diff(before.splitlines(True), after.splitlines(True),
                                       fromfile="a/" + java_path, tofile="b/" + java_path))
    (ROOT / "cuvs-lucene-lucene104-fix.patch").write_text(diff, encoding="utf-8", newline="\n")
    # This exact source delta should also apply to current main (checked independently later).
    print(json.dumps({"artifacts": len(manifest), "jdk": str(toolchain / "jdk-22.0.2+9"),
                      "patch": str(ROOT / "cuvs-lucene-lucene104-fix.patch")}, indent=2))


if __name__ == "__main__":
    main()
