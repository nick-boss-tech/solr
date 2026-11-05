# SOLR-12613: core-menu correctness patch on the actual PR head

Status: IMPLEMENTED, PROOF PENDING. Direct owner instruction implemented; fork push separately verified. Windows Codex desktop, 2026-11-05. Claim: `claims/solr-12613-pr-head-lifecycle-windows-round-1.md`.

## Source and delivery

| Item | Exact value |
| --- | --- |
| Contributor PR | https://github.com/apache/solr/pull/5110, OPEN at parent P |
| Contributor P | `6b5d830132b941faa85856347099ade8b190be0e` |
| Fork branch | `nick-boss-tech/solr:pr-5110-core-menu-lifecycle` |
| Pushed F | `f02bb7bb06decce976c3ed2fdfbf5907764c2183` |
| Ancestry | F is one ordinary commit with P as its only parent; preserves contributor history |
| Commit identity | Nick Shanin, nick.boss.us@gmail.com, author and committer; no trailers |
| Source worktree | `C:/Users/shaninna/dev/Solr-issues/wt/PR-5110-core-menu-lifecycle`, task-specific sparse checkout |
| Outbound files | `solr/webapp/web/js/angular/app.js`; `solr/webapp/src/test/org/apache/solr/webapp/AdminUiNodePickerTest.java` |
| Diff | 2 files, +261/-11; no API/core/generated/dependency/changelog delta |

[Pinned author-head-to-fix diff](https://github.com/nick-boss-tech/solr/compare/6b5d830132b941faa85856347099ade8b190be0e...f02bb7bb06decce976c3ed2fdfbf5907764c2183).

The task used a dedicated fetched ref to avoid another task changing shared FETCH_HEAD. The helper verifies the literal fork remote, task branch, contributor ancestry, exact outbound file list and clean tree before committing/pushing. The plain push created only the new fork branch. A separate GitHub refs API read returned F in full after the push completed. An earlier lookup while push was still running returned 404; it was not treated as a completed push or the final read-back. The contributor PR still reports P; neither its approved head nor its body/review state was changed.

## Implementation and reuse

The two affected files at P are byte-identical to the previous synthetic baseline `4e8aa913fce4a40b529fe89d8b5dd7930c29a86e`. The reviewed two-file lifecycle delta from `7f8d251d6c27200f3804f8b4e1298b9646f6cab4` therefore applies cleanly without importing that branch's synthetic ancestry. The production app.js delta is unchanged. One test cast expression was hand-reflowed before commit; fresh tidy is still required on Linux.

The controller now waits for mode/local-node/selection discovery before the initial core request; stale callbacks check both captured selected node and request generation before touching parent state. A shared clear function keeps parent UI selection coherent and a real local core refresh follows standalone detection or controlled metadata failure. Existing node-discovery failure fallback is retained. These are the separate remaining correctness concerns, not a reimplementation of Nick's three already addressed P2 findings.

Four added tests extend AdminUiNodePickerTest, giving 8 methods total. Two hold only delivery of real generated core callbacks and then assert reversed-completion inventories in parent state and rendered options. The CDP startup observer captures request URL/target header before Angular initialization, without setting a target. The metadata-failure control injects only the metadata error, delegates the core read to the generated API and requires its success. Existing ChromeDriver, cluster, chosen-select and wait helpers are reused; no new fixture, browser framework or dependency is added.

The actual PR helper differs from the synthetic baseline by lacking an extra JdkHttpClient-prefixed thread filter, and has no synthetic AdminUiNodeContractTest. Neither was copied into the author offer. Gate must investigate any actual fixture/thread failure rather than importing old proof or broad suppression. Existing contributor feature changelog covers the unreleased picker; no duplicate bug fragment or unrelated nick-field cleanup was included.

## Verification and gate

Windows inspected the full scoped source/test diff, existing fixtures, expected test boundaries, ancestry/identity/outbound files and clean status. `git diff --check` passed. No Gradle, compiler, formatter, Selenium, browser or retired local test queue ran. The four new tests and all passing counts remain unexecuted expectations here.

Gate: `gates/solr-12613-pr-head-lifecycle-round-1.md`, receipt target `receipts/SOLR-12613-pr-head-lifecycle-round-1.md`. It requires baseline picker 4 and candidate picker 8; a full 17-method neighboring selection on both heads at seed 5110C0AE; the same full selection on both heads at the earlier failure seed 12613F00D with actual AddCore investigation; four intended assertion failures with original P production and unchanged F tests; root tidy, browser-test Error Prone compilation, exact-source JS client and root check. Fresh logs/XML are required for every leg. A source/format/fixture repair moves the head and needs a superseding exact-head job with fresh proof.

The old synthetic selection job is superseded for this author-offer slice. New proof does not automatically satisfy the old combined integration job at a different head. No old partial receipt is a prerequisite or substitute for this actual-head run. Posted handoff is not execution or a green result.

## Comment and assignment reconciliation

Conditional draft: `reports/solr-12613-pr-head-lifecycle-comment-draft-round-1.md`. It includes the pinned diff and fast-forward commands, corrects the three old findings, and distinguishes the small offered patch from planned SOLR-18619/transport/deprecation work. Its success wording is explicitly hypothetical until the actual receipt matches this head and acceptance bar. No public comment was posted.

Claim wording correction: the claim's last sentence about a comment review hold was overrestrictive. This task prepares a comment and does not authorize immediate publication, but no new owner hold is invented here. Any actual owner draft-review hold still governs; otherwise the existing exact-head clean-gate standing authorization controls a later follow-up once all of its conditions are met. Do not claim Nick has reviewed wording before he does.

Update the submission matrix's lifecycle row to this current-head author offer. Preserve the earlier synthetic branch as reference; do not open a competing lifecycle PR by default. The six prepared packages and newly planned backend deprecation candidate otherwise keep their existing scopes/dependencies. No log migration, legacy parameter deprecation, API addition, branch deletion, force push or contributor write was performed.
