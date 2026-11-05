# Gate: SOLR-18623 replacement-session recovery and ordering coverage, round 2

Status: OPEN. Supersedes `gates/pr-5301-reconnect-liveness-round-1.md` on the exact head only.
Repair report: `reports/pr-5301-reconnect-repair-round-1.md`.

Everything in the round 1 job stands unchanged (scope, positive legs 1 to 6, negative legs A and B, repair and retry rules, finish conditions), with these substitutions:

- Exact head: H2 `27a9cf105b25f1bf3d604534cf8bdd8674c470fc` on fork branch `solr-18623-reconnect-liveness`, replacing H1 `19579b2ed2397929c0cc903a6c682331cc865586`. H2 is H1 plus one formatting-only tidy commit (two files, verified line by line in the repair report).
- Contributor H0 is unchanged: `01e820f0539cddc4697d26176d216614a776a99a` (apache/solr PR 5301). Negative leg A still restores `ConnectionManager.java` from the exact H0 blob; negative leg B still mutates only `acceptStateEvent(long)`.
- Reserved execution receipt unchanged: `receipts/PR-5301-reconnect-liveness-round-1.md`, written only by the gate host, recording H2 as the tested head.
