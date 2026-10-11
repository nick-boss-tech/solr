# Host: vm3 (tracking label assigned by Nick)

- Environment: current shared Sandbox computer; this is a tracking name, not a separate purchased or dedicated VM.
- Capabilities demonstrated: Linux, JDK 21.0.12.1, Gradle 9.7.0, Node 24.19.0, npm 11.17.0; changelog validation and Solr reference-guide build.
- Build protocol: run one gate at a time; check `df -i /` before every build and again after; record available space and inode usage. Preserve shared source worktrees, `.git` metadata, caches, and logs. Remove only generated build output or this task's isolated clean checkout after recording.
- SOLR-6430 docs gate: packaged head `c0c6e5dd21c4eee60257db6b85b2fec7f01489b7`; changelog lint and local guide build passed. The gate and claim are attributed to vm3. Logs remain under `/workspace/gates/logs/vm1/`, the literal directory used during that run.
- SOLR-7119 docs gate: prior completed build peak was recorded as +35,345 inodes from its pre-build snapshot, with post-cleanup net +13,636.
- SOLR-11700 docs gate: completed GREEN at packaged head `8a945757e6687253f61f7dc1e46130fa722c3f14`. Changelog lint and local reference-guide build passed; the rendered filters page was checked. No Java tests were run. The gate and receipt disclose the unintended build-infrastructure compilation during an earlier supposed dry-run and the mismatch between Lucene API Javadoc wording and implementation/tests.
- SOLR-11700 resource snapshot before the required site build, 2026-10-11T05:10:41Z: 54 GB total, 27 GB available; 620,243 of 837,408 inodes used (74.07%; 217,165 free). After site build, 2026-10-11T05:12:25Z: 26 GB available; 655,586 inodes used (78.29%; 181,822 free). After final lint, 2026-10-11T05:13:24Z: 26 GB available; 655,589 inodes used (78.29%; 181,819 free). The build peak was not continuously sampled.
- SOLR-11700 logs are retained under `/workspace/gates/logs/vm3/`.
