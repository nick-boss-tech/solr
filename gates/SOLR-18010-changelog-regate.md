# Gate job: SOLR-18010 changelog title re-gate

- Branch: solr-18010-submit. Head: `9e3ccafe39098bdc23cc8ea3313918071150e148` (fork tip after the fast-forward push). Previous gated head: `c3685bb37d9d74e4ed538d6d1b65bb1b60970677`.
- Status: OPEN, unclaimed.
- Capability tags: gate.
- Intended host: any capable Linux host (vm1 or vm2), under the WORKFLOW.md claim rules. Windows never claims build work.
- Job type: changelog check at the new head. No code change.
- Why: the head moved by one commit that changes only the changelog title in `changelog/unreleased/SOLR-18010-security-json-concurrent-edits.yml`. The PR description's changelog link and its verification line name the old head.

## Steps

1. Confirm the head: `ls-remote origin refs/heads/solr-18010-submit` must equal `9e3ccafe39098bdc23cc8ea3313918071150e148`. If it does not, stop and record the new tip.
2. Confirm the diff scope: `git diff --stat c3685bb37d9d 9e3ccafe390` must list only the changelog file.
3. Changelog YAML parse for `changelog/unreleased/SOLR-18010-security-json-concurrent-edits.yml`, with the repository's validator (`dev-tools/scripts/validate-changelog-yaml.py`). The title is a plain scalar, and it must parse.
4. Module check is not needed for a changelog-only change. Record that in the receipt.
5. Refresh `receipts/SOLR-18010.md`: the new head, the diff scope, and the YAML result. State that the code is identical to the gated head. Mark this job DONE.

## Checklist

- [ ] head confirmed by ls-remote
- [ ] diff scope confirmed (changelog only)
- [ ] changelog YAML parse
- [ ] receipt refreshed at the new head; job DONE
