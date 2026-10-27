# Claim: SOLR-17968 HTTP binary compatibility investigation handoff, Windows round 1

- Host: windows. Tags: review, draft, records.
- Started: 2026-10-27 20:50:31 UTC.
- Authority: Nick's direct request to post an assignment investigating the earlier change and determine whether a ticket is warranted.
- Scope: prepare and post the investigation assignment; not the Linux investigation, a compatibility repair, or a public filing.
- Board snapshot: `c6a9f7ae938499085b3db3e910a35b65f7bb0d2a`, refreshed to `f93c225d16119e8d29a240d57d2efa70bc9ad453`; intervening assignment/claim/report changes concern unrelated tickets.
- Existing records: SOLR-18325 matched binary attribution, evidence-closeout receipt/report, HTTP prerequisite design, and impact conclusion. No separate HTTP ABI investigation assignment or live claim was found at the reviewed snapshot.
- Preliminary reads: live SOLR-17968 and duplicate-search results via Apache Jira connector; Apache Solr PR 3829 metadata/body; refactor commit `e5340a954b6700627b8826431a243c2e2759b0f3`; released HTTP class declarations and the refactor's upgrade-note diff.
- Decision ladder: rungs 1 and 3 establish that the renaming was deliberate and already tracked. They do not establish that the binary compatibility consequence was intended or accepted. Assign bounded policy/history/released-artifact and consumer probes before choosing production restoration or filing a duplicate.
- Deliverable: `assignments/solr-17968-http-abi-investigation-round-1.md`.
- No Windows Gradle, public post, production change, gate waiver or schedule change.
