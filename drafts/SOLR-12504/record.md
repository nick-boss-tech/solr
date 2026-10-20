# Skip record: SOLR-12504

- Ticket: SOLR-12504, Leader should compute its fingerprint and retrieve recent updates in an atomic way. JIRA read in this pass: Open, resolution none, fix versions none, 0 comments.
- Verdict: keep-open.
- Draft branch: solr-12504-draft-open.
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942 (checked at this head for this record).
- Source: the queue entry (round audit-1, 2026-10-04, audit verdict confirmed-skip) is background only; the reading below is fresh at the pin.

## Ticket reading

The ticket: PeerSync can fail on a fingerprint mismatch because the leader computes its fingerprint and then retrieves its recent versions in two steps, so an update that lands between them appears in the versions but not the fingerprint (or the reverse). The reporter says blocking leader updates for getVersions is not acceptable for indexing performance.

The skip note from the queue says a maintainer-side design question remains and that no deterministic test was found.

## Code reading

- solr/core/src/java/org/apache/solr/handler/component/RealTimeGetComponent.java, processGetVersions (about lines 1229-1265): when doFingerprint is set, the fingerprint is computed first (IndexFingerprint.getFingerprint at about line 1257, with the comment "get fingerprint first as it will cause a soft commit"), and then, in a separate step, recentUpdates.getVersions(nVersions) is read (about line 1262). No lock or shared snapshot covers both reads, so an update can land in between. The ordering is the ticket's scenario order, so the window is present at the pin.
- solr/core/src/java/org/apache/solr/update/PeerSync.java, compareFingerprint (about lines 506-516): the requesting replica computes its own fingerprint only up to sreq.fingerprint.getMaxVersionSpecified(), so updates above the leader's reported max version are excluded from the comparison. This bounds the mismatch from a later update, and it is the mitigation the code relies on; it does not make the leader's two reads atomic.
- solr/core/src/java/org/apache/solr/update/PeerSyncWithLeader.java (about lines 224-234, 374-393): the same leader-side getVersions and getFingerprint pair is requested together from the leader.

## History

- No commit at the pin names SOLR-12504 (git log --grep at the pin: none).
- The file history of RealTimeGetComponent.java at the pin ends with 812408433c (SOLR-18350), 1af33ac699 (SOLR-18164), and 090f5b995c (dependency update). None of them is a fix for SOLR-12504 and none changes the ordering of the two reads; the ordering was not traced through their diffs in this pass.

## Reproduction attempts

Not run. A deterministic test would need a concurrent update injected between the fingerprint and the versions read on the leader. No such injection point was found in this pass, so no reproduction was attempted.

## Gap analysis

The window the ticket describes is still present in the leader's handler at the pin: the fingerprint and the recent versions are read in two steps without a shared snapshot. The PeerSync bound reduces the effect but is not a fix for the atomicity the ticket asks for. A fix needs either an atomic snapshot of the two values or a documented decision to accept the bounded mismatch, and the ticket's reporter and skip note both leave that as a design question.

## Verdict justification

keep-open: the window is present at the pin, so close-fixed and close-obsolete are not supported. close-cannot-reproduce needs a run attempt, and none was made. The open question is a design decision, not a finished fix.

Missing for a closing verdict: a maintainer decision on accepting the bounded mismatch with the PeerSync comparison, or an atomic snapshot change that closes the window, plus a deterministic test or run showing the mismatch is gone.

## Limits

This record does not run a concurrency test, does not trace the history diffs of the file beyond the three commits listed, and does not post anything to JIRA or GitHub.
