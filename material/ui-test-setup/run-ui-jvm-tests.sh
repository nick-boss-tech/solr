#!/bin/sh
# Runs the new UI JVM tests on a host without a GL driver.
# Usage: REPO=<checkout> run-ui-jvm-tests.sh <test filter, e.g. org.apache.solr.ui.views.files.FileContentViewerTest>
set -eu
here=$(cd "$(dirname "$0")" && pwd)
root=${REPO:-$(pwd)}
stub_dir=${STUB_DIR:-/tmp/ui-egl-stub}
filter=${1:?test filter required}
mkdir -p "$stub_dir"
gcc -shared -fPIC -Wl,-soname,libEGL.so.1 -o "$stub_dir/libEGL.so.1" "$here/egl-stub/stub.c"
LD_LIBRARY_PATH="$stub_dir" JAVA_TOOL_OPTIONS="-Dskiko.renderApi=SOFTWARE_FAST" \
  "$root/gradlew" -p "$root/ui" :shared:jvmTest --tests "$filter" --no-daemon --console=plain
