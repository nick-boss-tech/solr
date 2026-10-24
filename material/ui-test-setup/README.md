# New UI JVM tests on a host without a GL driver

The new Admin UI (`ui/`) tests run on the JVM through Skiko. Skiko loads a native library that links against EGL, and pool hosts have no GL driver. Without a stand-in, the test JVM fails at library load before any test runs.

## What this does

- `egl-stub/stub.c` exports the three EGL symbols Skiko loads (`eglGetCurrentDisplay`, `eglGetProcAddress`, `eglQueryString`) and returns NULL from each. It is built into `libEGL.so.1` under `STUB_DIR`, outside the repo.
- `JAVA_TOOL_OPTIONS=-Dskiko.renderApi=SOFTWARE_FAST` makes Skiko use its software raster path, so no GL call is made.
- No repo file changes. The workaround applies only to the test JVM's environment.

## Usage

Needs `JAVA_HOME` and `GRADLE_USER_HOME` set as in AGENTS.md (`/workspace/jdk/jdk-21.0.12.1+1` and `/workspace/gradle-home`).

    REPO=/path/to/checkout material/ui-test-setup/run-ui-jvm-tests.sh org.apache.solr.ui.views.files.FileContentViewerTest

Run it from the checkout root. Build with `-p ui`, so the root Gradle wrapper must exist.

## Limits

- Tests use the software renderer. A bug that only shows with GL will not be caught here. A GL host or CI is still needed to gate a change that touches rendering.
- Checked on vm2 at `ui` commit 8047b99ef09 (base apache/main c8ab71430f): `FileContentViewerTest` 3 of 3 pass, and `:shared:spotlessCheck` passes.
- `/tmp` is not persistent across container restarts. The script rebuilds the stub each run, so that is harmless.
