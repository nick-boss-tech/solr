# Host: vm3 (tracking label assigned by Nick to Ares)
- This is a tracking label for the current Sandbox worker, not a separate computer assignment.
- Capabilities demonstrated on SOLR-6430: Linux, JDK 21, Gradle 9.7.0, Node 24/npm 11.17.0; changelog lint and reference-guide build.
- Inode rule: check `df -i /` before every build. The SOLR-6430 docs build peaked at +84,649 inodes vs. its baseline; removing generated `build` outputs reduced the residual increase to +41,982. Preserve source, `.git`, npm caches, and other active work.
- SOLR-6430 log files are retained at `/workspace/gates/logs/vm1/` because that was the literal directory used during the run; the gate/claim attribution is vm3.
