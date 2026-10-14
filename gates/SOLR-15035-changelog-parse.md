# Gate job: SOLR-15035 changelog YAML parse at the live tip

- Branch: solr-15035-submit. Head: 12d7491e82cd660ad55063cb85633b7e05e1918c (live tip, verified by `git ls-remote` on 2026-10-13; receipt `receipts/SOLR-15035.md`, GATE GREEN (hardened) at this head).
- Intended host: any capable Linux host, under the WORKFLOW.md claim rules. Windows never claims build work.
- Status: DONE 2026-10-14T12:49ZZ by vm2. Parse OK at 12d7491e82; see Result below. Posted 2026-10-13 by the main side (openings census, `reports/openings-census-round-1.md`). The round 2 SolrCloud slate (`reports/solrcloud-openings-slate-round-2.md`) names this job as owed and not yet filed; this file files it.
- Capability tags: gate.
- Job type: a parse check only. No test run, no branch change, no push.
- Why owed: the branch adds `changelog/unreleased/SOLR-15035.yml`. An unquoted title containing ": " is invalid plain YAML and fails the Validate Changelog workflow while looking fine to a reader, so the fragment gets a real YAML parse before the ticket can leave its hold. The ticket itself stays HOLD: its blocking step is the owner's settlement line from HOLD to READY, which is not this job's to write.
- Steps:
  1. Confirm the head with `git ls-remote`; record it here.
  2. Read `changelog/unreleased/SOLR-15035.yml` at the head (a scratch checkout or `git show` is fine) and parse it with a real YAML parser (the main side's `check-changelog-yaml.sh` shape: python3 with pyyaml, pure parse). Record the parse result and the parsed title here.
  3. Also run `./gradlew --no-daemon logchangeLint` in a scratch worktree at the head if the host can; record the exit code. If Gradle cannot run on the claiming host, the YAML parse alone discharges this job and the lint is recorded as not run.
- On completion: this file gets the result and a DONE mark. The claiming host adds a line to `receipts/SOLR-15035.md` naming the head, the parse result and the date. Nothing about the ticket's HOLD changes in this job.

## Step checklist

- [ ] head confirmed by ls-remote and recorded here
- [ ] changelog fragment parsed with a real YAML parser; result and title recorded here
- [ ] logchangeLint run and recorded, or recorded as not run with the reason
- [ ] receipt line added to receipts/SOLR-15035.md; job DONE

- 2026-10-14T04:03ZZ vm2 gate backlog coordinator: DONE. Head 12d7491e82cd confirmed by ls-remote. Changelog YAML parsed with pyyaml (safe_load) OK; title: "Replicas created with ADDREPLICA now record numShards in core.properties, matching replicas created with the collection CREATE command." logchangeLint not run (parse only, as the job allows).
