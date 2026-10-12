# Investigation job: SOLR-15615 fallback path trace for the receipt

- Branch: solr-15615-submit. Head: be77267bf5549ae6bdc91bc4122b5bf5256f41e1 (live tip, verified by ls-remote at posting, 2026-10-11; GATE GREEN at this head per receipts/SOLR-15615.md).
- Intended host: any capable Linux host, under the WORKFLOW.md claim rules. Windows never claims build work.
- Status: OPEN, unclaimed.
- Capability tags: investigation, record.
- Job type: code investigation, recorded in receipts/SOLR-15615.md. No code change, no branch commit, no push, no gate run.
- Why: the draft's Limits (pr-drafts/query-parsing/SOLR-15615.md) owes a sentence on the fallback's authorization, and it cannot be written until the fallback path is traced end to end (reports/draft-fidelity-fixes-application-response.md, item F23: that is code work, not a record). This job is that trace.
- What to trace, in the branch code at the head above, with file and line citations at that head for every step:
  1. The entry: where the more-like-this query parser decides the document id is not in the local collection and enters the fallback (the draft describes the fallback as asking the collections one at a time; verify that shape against the code).
  2. The per-collection requests: how each fallback request is constructed, which collection it targets, in what order collections are tried, and what stops the sequence (a hit, exhaustion, an error).
  3. The client and the credentials: which HTTP client or internal dispatch the fallback requests use, whose authentication they carry (the original request's user context, or the internal inter-node credentials), and whether any shard or PKI authentication is involved.
  4. The authorization question the owed sentence turns on: when a fallback request reaches another collection, does that collection's authorization check run against the original user, against an internal identity, or not at all? Name the code that establishes the answer; a step that cannot be established from the code is recorded as a gap, not filled by inference.
  5. The exit: where the final error (the draft says a 400) is produced when the document exists in no collection, and what a mid-sequence error does to the sequence.
- The record: write the trace into receipts/SOLR-15615.md as a dated section, ending with a verdict in one of two forms: the exact Limits sentence on fallback authorization that the trace supports, quoted in full for the main side to place in the draft; or a statement that the sentence still cannot be written, naming the specific step that could not be established and what would settle it. The verdict is recorded either way; a trace that ends in a gap is a completed job.
- On completion: receipts/SOLR-15615.md carries the trace and the verdict, this job is marked DONE, and the claim is marked DONE. The draft is not edited by this job.

## Step checklist

- [ ] fallback entry point and per-collection sequence traced at be77267bf554, with file and line citations
- [ ] client, credentials and the authorization path for fallback requests established, or the gap named
- [ ] the final-error exit and mid-sequence error behavior traced
- [ ] trace and verdict written to receipts/SOLR-15615.md (the exact Limits sentence the trace supports, or the named gap)
- [ ] job and claim marked DONE
