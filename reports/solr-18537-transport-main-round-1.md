# Report: independent node transport follow-ups based on main

Status: Implemented, Proof Pending.

- Assignment `assignments/solr-12613-adjacent-implementation-round-1.md`; claim `claims/solr-18537-transport-main-windows-round-1.md`.
- Scope correction `gates/solr-12613-adjacent-scope-standdown-round-1.md` withdraws the old picker-based gate inputs. This report supplies two clean replacements; UI and combined gates remain withdrawn.
- Main baseline `07ccc73cd24e26e59da169ca35b3fd5f64c35986`, refreshed from upstream before authoring. Each new head is a single ordinary commit directly on main. The old picker head is not an ancestor. Old branches remain preserved.

| Slice | Fork branch | Pushed and independently read-back head | Linux gate |
| --- | --- | --- | --- |
| Encoded targeting query keys | `solr-18537-encoded-key-followup` | `7b83425fe7ce704ee781f273cd099b4425b795af` | `gates/solr-18537-encoded-key-main-round-1.md` |
| Encoded path through the replica hop | `solr-18537-path-coverage-followup` | `59ad950e1ac14d628e711e82746ee37c489cf151` | `gates/solr-18537-path-coverage-main-round-1.md` |

## Correctness and scope

Main and the old merged baseline have identical versions of NodeTargetProxyFilter, its two test classes and HttpSolrCall. Each new branch carries exactly the prior scoped patch, with unchanged source/test blobs. This removes the 17 inherited backend API files and unrelated frontend changes from the proposed main-relative diffs without changing the transport behavior under review.

The encoded-key slice parses escaped GET parameter names, retains the header/query conflict check, consumes the targeting directive before forwarding, and preserves query parse errors as 400 through the authentication wrapper. Files are `solr/core/src/java/org/apache/solr/servlet/NodeTargetProxyFilter.java`, `solr/core/src/test/org/apache/solr/servlet/NodeTargetProxyFilterTest.java`, and `solr/core/src/test/org/apache/solr/servlet/NodeTargetProxyFilterAuthTest.java`. It adds five unauthenticated and two authenticated test methods, including raw encoded v1/v2 requests, unrelated encoded values, POST non-routing and malformed UTF-8.

The path slice changes only `solr/core/src/test/org/apache/solr/servlet/NodeTargetProxyFilterTest.java`, adding two methods for header and query targeting across an actual collection-replica hop. It asserts encoded URI, forwarding count, query and foreign-header preservation, and consumed target directives. Production remains unchanged; this is coverage-only work.

## Architectural fit, reuse and security limits

Both changes belong to the generic transport introduced by merged SOLR-18537. They add no endpoint parameter, response model, aggregation contract or v2 API. They reuse the existing two-node cluster, cloud-minimal configset, echo handler, and authenticated fixture. Tests exercise existing v1 and v2 consumers of the same transport, plus collection forwarding. No new fixture or dependency is introduced.

Authentication remains in the existing filter chain; the fix handles parse-error status locally. The auth tests provide regression coverage for BasicAuth only, not client certificates or a complete security assessment. No new trust boundary or public security claim is asserted.

## Verification and remaining work

Windows reviewed the exact file-scoped patches and checked main-only ancestry, clean worktrees, matching remote refs, file lists, original-patch blob equality, whitespace and Nick Shanin commit identity. Static method counts are 19+6 for encoded keys and 16+4 for path coverage; they are expectations for execution, not passing results. No Gradle, compiler, browser or tests ran on Windows.

New Linux jobs retain focused and neighboring suites, fixed seeds, tidy, root check and fresh logs/XML. The encoded-key job requires four intended runtime fail-before assertions on main plus only the new tests; the inherited-only NodeAdminProxySolrCloudTest neighbor is removed. The path job requires passing original/candidate suites and a compiling, isolated second-hop double-encoding mutation producing the intended URI assertion failures. No old receipt transfers to these new heads.

Receipts must be written by the execution host at `receipts/SOLR-18537-encoded-key-main-round-1.md` and `receipts/SOLR-18537-path-coverage-main-round-1.md`. A posted job is not an execution claim or a green result. A repair moves the head and requires a superseding gate. Public issue identity/changelog packaging for the encoded-key bug remains undecided; SOLR-18537 names provenance. No PR, GitHub/Jira comment, contributor-branch write or upstream push was performed.

The remaining UI lifecycle/request migration and combined integration work require a frontend-only picker baseline and regenerated exact-head jobs. Backend v2 additions must be assessed through Nick's separate tickets, avoiding duplication of OPEN SOLR-15823 / PR 5031. Those tasks are not declared complete by these two transport ports.
