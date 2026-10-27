# Receipt: browser check of the patched RC3 admin UI (vm5, 2026-10-27, 03:12 to 03:18 UTC)

Build under test: release assembled from `23053d08816` (RC3 plus copy fallback and js-client install fix). Not the official RC3 tarball. The control assignment against the official artifacts (`assignments/rc3-admin-screen-test-vm5-round-1.md`) is still open and not run.

Environment: Ubuntu 22.04.5 LTS, aarch64, OpenJDK 21.0.12.1 (`/workspace/jdk/jdk-21.0.12.1+1`), headless Chromium from `/opt/playwright-browsers/chromium-current` via Playwright (`uv run --with playwright`). Script: `/workspace/admin-screen-check/scripts/admin_check.py`.

Start: `bin/solr start -p 8983`, stock configuration, no auth. Solr started in SolrCloud mode, which is the 10.1 default (`bin/solr:684`), with embedded ZooKeeper on 9983. Solr stopped at the end: `bin/solr stop` reported a kill error, the JVM became a zombie, and port 8983 was free.

| View (URL) | Role message seen | Console errors | Failed requests | HTTP statuses |
|---|---|---|---|---|
| Dashboard `/solr/` | no | 0 | 0 | 200 only |
| Security `/solr/#/~security` | no | 0 | 0 | 200 only |
| Cloud `/solr/#/~cloud` | no | 0 | 0 | 200 only |
| Collections `/solr/#/~collections` | no | 0 | 0 | 200 only |
| Logging `/solr/#/~logging` | no | 0 | 0 | 200 only |

`solr.log` after the checks: 0 ERROR or Exception lines.
The dashboard shows `solr-impl 10.1.0-SNAPSHOT 23053d08816`. Security shows "Security is not enabled for this server", which is expected for this configuration.

Files (local): `/workspace/admin-screen-check/patched-*.json` and `patched-admin.png`.

Not tested: authentication setups (Joel's report may depend on one); the official RC3 artifacts; 10.0.0 control; any admin view beyond the five above.
