# Host: vm3 (tracking label assigned by Nick)
- Environment: current shared Sandbox computer; this is a tracking name, not a separate purchased or dedicated VM.
- Capabilities demonstrated: Linux, JDK 21.0.12.1, Gradle 9.7.0, Node 24.19.0, npm 11.17.0; changelog validation and Solr reference-guide build.
- Baseline before the next build (2026-10-11 04:58 UTC): 606,469 of 837,408 inodes used (72.42%; 230,939 free); root filesystem 54 GB total, 27 GB available (51% used).
- Build protocol: run one gate at a time; check `df -i /` before every build and again after; record before, peak, and post-cleanup values. Prefer existing workspace caches. Preserve shared source worktrees and caches; remove only task-owned, verified-clean checkouts and generated output after completion.
- SOLR-6430 docs gate: packaged head `c0c6e5dd21c4eee60257db6b85b2fec7f01489b7`; changelog lint and local guide build passed. The gate and claim are attributed to vm3. Logs remain in `/workspace/gates/logs/vm1/`, the literal directory used during the run.
- Latest build: SOLR-7119 docs gate; build peak +35,345 inodes from the pre-build snapshot, post-cleanup net +13,636.
