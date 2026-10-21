Draft for Nick to apply by hand. Prepared 2026-10-21 by vm3 for head `1b14ce9556`. Not applied; a live body edit needs Nick's approval.

Suggested title: `SOLR-18010: Standalone security.json edits are serialized, and written through a temporary file where that is safe`

---

🤖 *AI text below* 🤖 *(posted on behalf of Nick Shanin)*

https://issues.apache.org/jira/browse/SOLR-18010

## What happens today

**Two edits to a standalone security.json that overlap in time can lose one change, and two overlapping writes can leave a file that is not valid JSON.**

The Admin UI sends one request per granted permission without waiting for each response ([security.js](https://github.com/nick-boss-tech/solr/blob/14c7aac0d151402b00259e2fb9bf5eed7049ec5d/solr/webapp/web/js/angular/controllers/security.js#L1248-L1283)). Each edit reads security.json, applies its commands, and writes the whole file back ([SecurityConfHandler.java](https://github.com/nick-boss-tech/solr/blob/14c7aac0d151402b00259e2fb9bf5eed7049ec5d/solr/core/src/java/org/apache/solr/handler/admin/SecurityConfHandler.java#L124), [line 151](https://github.com/nick-boss-tech/solr/blob/14c7aac0d151402b00259e2fb9bf5eed7049ec5d/solr/core/src/java/org/apache/solr/handler/admin/SecurityConfHandler.java#L151)). If two edits start from the same content, the later write drops the other's change.

The write itself is not atomic. The file is opened for writing, which empties it, and the new content is written after that ([SecurityConfHandlerLocal.java](https://github.com/nick-boss-tech/solr/blob/14c7aac0d151402b00259e2fb9bf5eed7049ec5d/solr/core/src/java/org/apache/solr/handler/admin/SecurityConfHandlerLocal.java#L86-L87)). Two overlapping writes can leave the start of one document followed by the tail of another. The corrupted file in the ticket also carries an empty-string key with `{"v":0}` in both sections. That is Solr's internal version marker, which standalone mode always writes as 0. These links point to merge-base commit 14c7aac0d15.

## What this change does

**Standalone edits run one at a time on each node, and the new content replaces security.json through a temporary file when that is safe.**

- Edits to a node's security handler run one at a time. The lock that serializes them was added by #5065, which is on main; this PR keeps it unchanged. Reads do not wait, and responses do not change.
- The new content is written to a temporary file in the same folder and moved over security.json. The move is used only when the file keeps its owner and its POSIX permission bits. In every other case the content is written in place, as the code did before this PR: a symbolic link, a directory that cannot take a new file (for example a read-only SOLR_HOME), a file whose owner or permissions cannot carry over, and a move that the file system or another program refuses. A warning is logged when the write falls back.
- The internal version marker is removed before the file is written. Standalone mode never reads that marker back, and the reload check in `CoreContainer.readVersion` only acts on a positive version, which standalone edits never write. A `GET` on a local file no longer shows the empty-string entry after an edit. An existing file that still has the marker loses it on its next edit.

## Choice

**An edit keeps the owner and the POSIX permission bits of the existing security.json.** If the temporary file cannot take them over, the edit is written in place, which keeps them as before. An edit does not fail because of this.

## Proof

- `SecurityConfHandlerTest` 5 of 5: `testEdit`, `testConcurrentEditsToLocalSecurityJson`, `testConcurrentPersistConfLeavesOneWholeDocument`, `testEditKeepsExistingSecurityJsonPermissions`, `testEditInPlaceWhenSolrHomeIsReadOnly`.
- `BasicAuthStandaloneTest` 2 of 2, including the check that the saved file has no internal marker.
- `testEditInPlaceWhenSolrHomeIsReadOnly` fails on the previous head with `AccessDeniedException` on the temporary file, and passes with this change.
- Module check (`:solr:core:check -x test`) and Error Prone compile pass at head `1b14ce9556`.
- Not covered by a test: a symbolic link (see the question below), an owner change (needs root), a single-file bind mount, and Windows.

## Limits

**Standalone mode only, and the lock covers one Solr process.**

- Cloud mode keeps security.json in ZooKeeper and uses SecurityConfHandlerZk. This change does not touch that path.
- Two Solr processes sharing one SOLR_HOME are not coordinated, as with #5065.
- When the write falls back in place, it is not atomic. A process crash during that write can leave a partly written file, as the code did before this PR.
- The file is not flushed to disk before it is replaced. A process crash leaves the previous file in place; a power failure can still leave an empty or stale file.
- The move does not carry over ACLs or extended attributes, and the check does not look at them. When the move is used on a file that has an ACL, the ACL is lost.
- A file created by an edit gets owner-only permissions (0600). That happens only if the file is removed between the read and the write, because an edit needs the existing file to find the key it changes.
- On Windows, a move over a file another program holds open is refused, and the write falls back in place.

## Question for the maintainers

The suite's test policy (`gradle/testing/randomization/policies/solr-tests.policy`) grants hard links but not symbolic links, so a test cannot create a symbolic link. Should the policy allow `LinkPermission "symbolic"` so the link case can be tested? This PR does not change the policy.

Changelog: [changelog/unreleased/SOLR-18010-security-json-atomic-write.yml](https://github.com/nick-boss-tech/solr/blob/1b14ce9556fa1357f41ca0aefebb435a97ef1504/changelog/unreleased/SOLR-18010-security-json-atomic-write.yml)

### AI assistance

AI agents assisted with research, implementation, review, and drafting. Nick Shanin directed the work and takes responsibility for this contribution.
