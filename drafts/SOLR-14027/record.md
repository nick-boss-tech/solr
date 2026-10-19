# Skip record: SOLR-14027

- Ticket: SOLR-14027, Zookeeper Status Page does not work with SSL Enabled Zookeeper. JIRA read 2026-10-19 (this pass, evidence tool): status Open, resolution None, created 2019-12-06, updated 2023-10-19, duplicate of SOLR-16056 (Resolved), referenced SOLR-16115 (Open) and SOLR-13502 (Reopened); six comments, the first from janhoy (2019-12-08) pointing to SOLR-13502.
- Verdict: keep-open.
- Draft branch: solr-14027-draft-open (the existing keep-open branch; this record replaces its draft files).
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942. Code read and grep run at this head (detached worktree).
- Source: remediation redo of batch 05, item 10, under assignments/pool-aaa-skip-verdict-remediation-05.md. The earlier record (round audit-1, 2026-10-04, confirmed-skip) cited the redirect from audit evidence; the code is read here.

## Ticket reading

The ZooKeeper status page sends four-letter commands (ruok, mntr, conf) over a plain socket, so it fails when ZooKeeper requires SSL. A maintainer (janhoy, 2019-12-08) redirects the fix to ZooKeeper AdminServer support in SOLR-13502. A 2022 comment suspects a socket leak; another maintainer says the code is closed by try-with-resources.

## Code reading and grep at the pinned head

- solr/core/src/java/org/apache/solr/handler/admin/ZookeeperStatusHandler.java:328, `getZkRawResponse(String, String)`: the raw four-letter command path. The socket is opened with try-with-resources at :336 (`try (Socket socket = new Socket(host, port); ...)`), which closes it, so the 2022 leak concern does not hold for this method.
- The callers are at :278 (`ruok`), :282 (`mntr`) and :299 (`conf`). The path is still plain-socket and has no SSL branch.
- `git grep -n "AdminServer" 498a09aea97aac86c2cc7ac114113b946d249942 -- solr/core/src/java/org/apache/solr/handler/admin/ZookeeperStatusHandler.java | wc -l` gives 0. The status handler does not use the ZooKeeper AdminServer.
- `git log --format='%h %ad %s' --grep='SOLR-16056' 498a09aea97aac86c2cc7ac114113b946d249942` and the same for SOLR-13502 return no commits.

## History

- No commit on the pin names SOLR-16056 (the resolved duplicate) or SOLR-13502 (the redirect target). The ticket's fix is not on the pin.

## Reproduction attempts

Not run. A run needs a ZooKeeper with SSL and the status page; this pass did not set one up.

## Gap analysis

The raw-socket problem is in the code at the pin. The maintainer's direction (AdminServer support via SOLR-13502) is open and not on the pin. The resource-leak concern is answered by the code (try-with-resources at :336), so that part is closed.

## Verdict justification

keep-open: the attempts made are the code reads and greps above at the pinned head, the Jira read, and the history check. The missing piece is the AdminServer design (SOLR-13502, reopened) or another SSL-capable status path, which is a maintainer decision. close-fixed and close-obsolete are not supported: the plain-socket path is current, and SOLR-16056 names no fix on the pin. pr-needed is not supported without that direction.

Missing for a closing verdict: the SOLR-13502 outcome or another decided status path.

## Limits

This record does not run the status page against an SSL ZooKeeper. It does not post anything to JIRA or GitHub. The draft comment is a draft until someone posts it by hand.
