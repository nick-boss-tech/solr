# PR drafts for the round 3 newly gated branches

Assignment: `assignments/pool-drafts-newly-gated-round-3.md`. Claim: `claims/pool-drafts-newly-gated-round-3.md`. Lead: the windows draft agent. Part reports, each with the receipt numbers used, the head check, the citation checks and the proof shape:

- `reports/drafts-newly-gated-round-3-a.md`: SOLR-5011 (core-admin), SOLR-11678 (security), SOLR-12161 (security).
- `reports/drafts-newly-gated-round-3-b.md`: SOLR-16322 (build-docs), SOLR-10667 (cli), SOLR-12347 (cli).

Six draft files, text only. Nothing was committed, pushed, posted or built, and no GitHub or Jira write was made. Each Proof states the proof shape the receipt records, including where a test does not fail on base.

## Per draft

| Draft | Head drafted | Choice section | Status |
|---|---|---|---|
| SOLR-5011 (core-admin) | `3fd85bedef64` (fork tip matches) | no; the shared-schema question is in Limits and undecided | ready for owner review |
| SOLR-11678 (security) | packaged `611eae246453` (vm2 local; fork tip `55d8cd189d15`) | no | **hold** (see below) |
| SOLR-12161 (security) | `cdd265168a2a` (fork tip `1725cbd8`) | no; a test-only pin, no changelog fragment | ready for owner review |
| SOLR-16322 (build-docs) | `68dd5e2b154e` (fork tip matches) | no; only the printed reproduce line changes | ready, with the decision below |
| SOLR-10667 (cli) | packaged `1e3f022552be` (fork tip matches; no packaging delta) | no | ready for owner review |
| SOLR-12347 (cli) | packaged `56d38389c75a` (fork tip matches; no packaging delta) | yes: default 600 seconds versus keeping 180, or 900 or 1200 | ready for owner review |

## Holds and decisions for the lead

1. **SOLR-11678, hold.** The security audit's blocking item is still open at `55d8cd18`: `solr-solrj`'s `EnvToSyspropMappings.properties` has no `SOLR_SSL_KEY_MANAGER_PASSWORD` line. The receipt does not mention it. The lead decides whether this blocks the opening. The draft does not carry the item.
2. **SOLR-11678 and SOLR-12161, links.** Both drafts link packaged heads that are not on the fork until the vm2 packaging commits are pushed. The links return 404 until then.
3. **SOLR-16322, the word "seed".** The draft uses "seed" as the product term for `-Ptests.seed`. It states no seed values. The brief lists "seeds" as process vocabulary. The lead decides whether the flag name stays or the draft says "random seed" without the flag. Nothing was changed.
4. **SOLR-12347, the merge-base.** The branch's true base is `b6b2b8f10e`, not `cabedd1d968`. The draft says the run base `cabedd1d968` has identical script and test files. The lead confirms that wording.

## Receipt gaps

- **SOLR-16322.** The receipt has no date, so the Proof omits it. The receipt records only beast runs; non-beast, passing build and user-seed runs are named as not run in Limits.
- **SOLR-5011 and SOLR-11678.** The focused-run dates are not in the receipts. The SOLR-5011 draft gives the gate-run date; the SOLR-11678 draft gives only the premise date.
- **SOLR-12347.** The Windows effective wait of 570 seconds comes from code and was not run. The systemd `TimeoutSec=180`, ref guide line 400 and the start-default test gap are named in Limits as unchanged.

## Not done

- No build, test, gate or test-queue run. No GitHub or Jira write.
- The gate and premise logs named in the receipts are not on disk. Counts and dates are the receipts' recorded values.
- Packaged-head objects were fetched by SHA into `FETCH_HEAD` only. No refs were moved.
