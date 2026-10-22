🤖 *AI text below* 🤖 *(posted on behalf of Nick Shanin)*

https://issues.apache.org/jira/browse/SOLR-18010

## What happens today

**Standalone security.json is written in place, so a crash mid-write can leave a partly written file, and the file keeps an internal version marker.**

The Admin UI sends one request per granted permission without waiting for responses ([security.js L1248-L1285](https://github.com/nick-boss-tech/solr/blob/4422d26ac7/solr/webapp/web/js/angular/controllers/security.js#L1248-L1285)). #5065 on main already serializes standalone edits within one Solr process, reads included ([SecurityConfHandlerLocal.java L52-L56](https://github.com/nick-boss-tech/solr/blob/4422d26ac7/solr/core/src/java/org/apache/solr/handler/admin/SecurityConfHandlerLocal.java#L52-L56)). Two things remain. The write truncates the file before it writes ([L99-L100](https://github.com/nick-boss-tech/solr/blob/4422d26ac7/solr/core/src/java/org/apache/solr/handler/admin/SecurityConfHandlerLocal.java#L99-L100)), and each edit stores an empty-string `{"v":0}` key in every section ([SecurityConfHandler.java L147-L148](https://github.com/nick-boss-tech/solr/blob/4422d26ac7/solr/core/src/java/org/apache/solr/handler/admin/SecurityConfHandler.java#L147-L148)). Links point to the merge base `4422d26ac7`.

## What this change does

**The new content replaces security.json through a temporary file in the same folder, and the marker is no longer saved.**

- The move is used only when the new file can keep the owner and POSIX permission bits. Otherwise the content is written in place, as on main, with a warning logged. That covers a symbolic link, a directory that cannot take a new file (for example a read-only SOLR_HOME), an owner change, and a refused move ([`replaceSecurityJson` at head, L128-L141](https://github.com/nick-boss-tech/solr/blob/1b14ce9556fa1357f41ca0aefebb435a97ef1504/solr/core/src/java/org/apache/solr/handler/admin/SecurityConfHandlerLocal.java#L128-L141)).
- An edit keeps the existing owner and permission bits. A failed permission copy falls back to the in-place write and does not fail the edit.
- The marker is removed before the write. `CoreContainer.readVersion` ([L611-L617](https://github.com/nick-boss-tech/solr/blob/4422d26ac7/solr/core/src/java/org/apache/solr/core/CoreContainer.java#L611-L617)) skips a reload only for a positive version, and a standalone edit writes 0, so no reload changes. The ZooKeeper handler is unchanged. Its conditional writes use the znode version, not the stored marker, so the two storage forms now differ.

## Choice for maintainers

**Keep the atomic move, or write in place and remove only the marker.** The move gives crash safety but replaces the file. A symbolic link needs the fallback, and ACLs and extended attributes are lost. Is crash safety worth the change of file identity? This PR makes the move the default.

## Proof

**On main, 2 of the 7 tests in these two classes fail with this PR's tests. On this head, all 7 pass.**

- Main (`7892348272`, whose `SecurityConfHandlerLocal.java` matches `4422d26ac7`): `SecurityConfHandlerTest.testConcurrentPersistConfLeavesOneWholeDocument` fails ("not a single JSON document"), and `BasicAuthStandaloneTest.testBasicAuth` fails (internal metadata in the file). The other five pass. The #5065 lock covers `testConcurrentEditsToLocalSecurityJson`. Main already keeps permissions and writes in place, so `testEdit`, `testEditKeepsExistingSecurityJsonPermissions`, `testEditInPlaceWhenSolrHomeIsReadOnly` and `BasicAuthStandaloneTest.testConcurrentEdits` pin behavior only.
- This head `1b14ce9556`: focused tests 5 and 2, 0 failures. Error Prone compile, tidy and `:solr:core:check -x test` pass.
- `testEditInPlaceWhenSolrHomeIsReadOnly` fails at round 2 head `dfcf50aec0` with `AccessDeniedException`.
- Symbolic link: not in the suite (see the question below). With `LinkPermission "symbolic"` added locally, all 6 tests pass at this head. At `dfcf50aec0`, `testEditThroughSymbolicLinkUpdatesTarget` fails because the move replaced the link.

## Limits

**One process only, and nothing is fsynced.**

- Two Solr processes sharing one SOLR_HOME are not coordinated, the same limit as #5065.
- Nothing is fsynced before the move, so a power failure can leave an empty or stale file.
- A file created by an edit gets 0600, which happens only if the file is removed between the read and the write.
- On Windows, a move over a file another program holds open is refused and the write falls back in place. Not run here.
- A crash between creating and moving the temporary file leaves a `security.json*.tmp` file in SOLR_HOME with the new content. On POSIX it is created 0600. Solr does not remove it.
- Not tested: an owner change (needs root) and a single-file bind mount. Cloud mode is not touched.

## Question for maintainers: the test policy

The suite policy grants `LinkPermission "hard"` only ([solr-tests.policy L38](https://github.com/nick-boss-tech/solr/blob/4422d26ac7/gradle/testing/randomization/policies/solr-tests.policy#L38)). The symbolic link test passes once `"symbolic"` is added and fails without the fix. This PR does not change the repo-wide policy. Should `"symbolic"` be allowed so the test runs in CI, or should the link case stay untested?

Changelog: [changelog/unreleased/SOLR-18010-security-json-atomic-write.yml](https://github.com/nick-boss-tech/solr/blob/1b14ce9556fa1357f41ca0aefebb435a97ef1504/changelog/unreleased/SOLR-18010-security-json-atomic-write.yml)

*AI-generated description. Nick Shanin directed the work and takes responsibility for this contribution.*
