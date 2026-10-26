# Gate: interrupted-close fix, final validation round 1

Status: OPEN until its receipt. Immutable job.

- Fork: nick-boss-tech/solr, branch solr-teardown-interrupt-close-fix.
- Exact head F: 5ec461c4012036faf3fd14363202e82c4686fb07 (parent H 8787c6605ad5a5d322e9bca253235cd73142f101, commit "Import HttpClient in the new interrupt-close tests").
- Base M: d298cd0b42602247bee7bf9e2bcf302e725f6690.
- Assignment: assignments/teardown-interrupt-close-fix-final-validation-round-1.md. Review: reports/teardown-interrupt-close-fix-review-round-1.md.
- Host: vm2, Linux aarch64, JDK 21.0.12.1+1 at /workspace/jdk/jdk-21.0.12.1+1, GRADLE_USER_HOME /workspace/gradle-home.
- Worktree: /workspace/worktrees/teardown-final-r1 (clean, at F).
- Runner and lock: pool/bin/sweep-run.sh under /workspace/tools/.gradle-vm2.lock. Driver: /workspace/gates/gates-vm2/run-final-gate-r1.sh (one driver, nonblocking driver lock /workspace/tools/.teardown-final-gate.lock).
- Log root: /workspace/gates/gates-vm2/logs/final-gate-r1/ (each leg has its own name; no reuse).
- Seed FFF5A3C3AAC69084. -Ptests.jvms=1 -Ptests.useSecurityManager=true -Ptests.file.encoding=ISO-8859-1.

## Legs (sequential, one JVM each, one module-suite invocation allowed)

1. tidy: ./gradlew --no-daemon --max-workers=2 tidy (preflight at F; tree must stay clean).
2. compile with Error Prone: ./gradlew --no-daemon --max-workers=2 --no-build-cache :solr:solrj-jetty:clean :solr:solrj-jetty:compileJava :solr:solrj-jetty:compileTestJava -Pvalidation.errorprone=true
3. module check: ./gradlew --no-daemon --max-workers=2 :solr:solrj-jetty:check -x test -Pvalidation.errorprone=true
4. whole module suite, once: ./gradlew --no-daemon --max-workers=2 :solr:solrj-jetty:cleanTest :solr:solrj-jetty:test -Ptests.seed=FFF5A3C3AAC69084 -Ptests.jvms=1 -Ptests.useSecurityManager=true -Ptests.file.encoding=ISO-8859-1

Watchdog: 30 min per leg, 90 min total. No source or test edits while legs run. Any failed leg is recorded and not retried.

Published before execution: 2026-10-26T05:17:28Z (see claims/teardown-interrupt-final-gate-round-1-vm2.md).
