# Gate job: SOLR-16437 changelog re-gate

- Branch: solr-16437-submit. Head: `a23f770e4b9587acbd14fcdbaae2047bd5c727c4` (fork tip after the fast-forward push). Previous gated head: `f72a9edd0a8b1aa1dded6af6c0c792fbb6179aa2`.
- Status: OPEN, unclaimed.
- Capability tags: gate.
- Intended host: any capable Linux host (vm1 or vm2), under the WORKFLOW.md claim rules. Windows never claims build work.
- Job type: changelog check at the new head. No Java changed between the two heads: the only commit is a changelog title.
- Why: the head moved, so the receipt names the old one.

## Steps

1. Confirm the head: `ls-remote origin refs/heads/solr-16437-submit` must equal `a23f770e4b9587acbd14fcdbaae2047bd5c727c4`. If it does not, stop and record the new tip.
2. Confirm that the only file that differs from `f72a9edd0a8b` is `changelog/unreleased/SOLR-16437-addreplicaprop-validate-inputs.yml`: `git diff --stat f72a9edd0a8b a23f770e4b9`.
3. Changelog YAML parse for that file.
4. Full module check only if step 2 shows a Java change. Otherwise skip it and say so in the receipt.
5. Refresh `receipts/SOLR-16437.md`: the new head, the changelog parse result, and the note that the code is identical to the gated head. Mark this job DONE.

## Checklist

- [ ] head confirmed by ls-remote
- [ ] diff scope confirmed (changelog only)
- [ ] changelog parse
- [ ] receipt refreshed at the new head; job DONE
