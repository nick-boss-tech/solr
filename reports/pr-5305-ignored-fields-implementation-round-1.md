# SOLR-18627 / PR 5305: ignored-field sibling repair

Status: Implemented, Proof Pending.

- Owner authorized implementation and gate handoff after the deeper source review.
- Implementation claim: claims/pr-5305-ignored-fields-windows-round-1.md, posted at baea8c7f01fffd41df85543949f6e80babf12b03.
- Contributor parent O: 00292cd3729126194235e939afe38e351d850c70, apache/solr PR 5305, linslee75/solr:SOLR-18627-ignored-field-npe.
- Fork: nick-boss-tech/solr; branch pr-5305-solr-18627-ignored-fields.
- Pushed head F: 219fee78bf7cae5039e50fe885108a1d4937ccd3. The separate GitHub fork-ref read matches the commit printed by commit/push.
- Tree: 0f979d6c070da09f889b829f51c915743df77239.
- Commit identity: Nick Shanin <nick.boss.us@gmail.com>, author and committer; no attribution trailers.
- Compare: https://github.com/nick-boss-tech/solr/compare/00292cd3729126194235e939afe38e351d850c70...219fee78bf7cae5039e50fe885108a1d4937ccd3
- Gate: gates/pr-5305-ignored-fields-round-1.md.
- Required execution-host receipt: receipts/PR-5305-ignored-fields-round-1.md.

## Changes

The original SOLR-18164 singleton-list modernization changed nullable field results into List.of(null) failures. The contributor already repairs StrField; this commit repairs the six production siblings and the matching test-framework fallback without changing contributor ancestry or modifying the public PR head.

BoolField and BinaryField now return an empty list only after docValues assembly when their final ordinary/docValues result is null. SortableTextField guards its no-docValues result. CollationField, ICUCollationField and TrieField reuse the null-safe inherited createFields method in their no-docValues branches. SortableBinaryField delegates its no-docValues fallback to the repaired BinaryField implementation. DocValues-only processing remains reachable.

PrimitiveFieldTypeTest reuses its existing SolrConfig and schema15.xml setup. Seven new ignored-field methods cover StrField as an existing-fix control, the five affected core production classes, and the binary test utility. Each checks both single and multivalued ignored definitions; the Trie method checks all five concrete numeric/date types. A separate preservation method checks single/multivalued docValues for string, boolean, sortable text and JDK collation, plus supported single-valued binary docValues, including exact bytes for string/boolean/binary.

TestICUCollationField reuses its existing initialized ICU schema and fixture. It gains ignored single/multivalue cases and a docValues-only preservation method. No new schema/configset, test class, dependency, hook or public API is added. The contributor's BasicFunctionalityTest indexing/commit/query regression is retained unchanged.

The changelog names the broader affected field family, retains Li Enlai, adds Nick Shanin by name only, and removes the nick property in accordance with the workspace's outbound-fragment convention. No Reference Guide or upgrade note is required for restored behavior.

## Exact additional outbound files (O to F)

1. changelog/unreleased/SOLR-18627-ignored-field-npe.yml
2. solr/core/src/java/org/apache/solr/schema/BinaryField.java
3. solr/core/src/java/org/apache/solr/schema/BoolField.java
4. solr/core/src/java/org/apache/solr/schema/CollationField.java
5. solr/core/src/java/org/apache/solr/schema/SortableTextField.java
6. solr/core/src/java/org/apache/solr/schema/TrieField.java
7. solr/core/src/test/org/apache/solr/schema/PrimitiveFieldTypeTest.java
8. solr/modules/analysis-extras/src/java/org/apache/solr/schema/ICUCollationField.java
9. solr/modules/analysis-extras/src/test/org/apache/solr/schema/TestICUCollationField.java
10. solr/test-framework/src/java/org/apache/solr/schema/SortableBinaryField.java

These are the only ten files in the added commit, 176 additions and 9 deletions. The four contributor PR files remain in ancestry. The scoped commit helper stages only this exact list and refuses extra changed or untracked files.

## Static assessment and proof boundary

- Correctness: checked null-result reachability, superclass dispatch, schema property normalization, preservation of docValues branches, and compatibility with DocumentBuilder's null filtering and copyField flow. No blanket field skip or parser/configuration contract change is introduced.
- Architecture and reuse: uses the existing FieldType empty-result behavior and existing test fixtures; no new extension surface or shared infrastructure.
- Security: restores ordinary field omission; no new trust boundary, permissions, external access, or persisted format.
- Verification performed on Windows: reviewed the complete scoped diff, git diff --check passed, exact-file status was checked, author/committer and clean worktree were verified, and the pushed fork ref was independently read back.
- Not executed on Windows: Gradle, Java compilation, tests, formatter or Linux gate. Existing upstream green checks cover O only, not F. Runtime fail-before results, focused/neighbor passes, tidy and root check at F are pending.

The gate requires the new tests retained over O's seven affected production/test-framework files, with six core methods and one ICU method failing at the expected List.of null boundary. It separately requires all positive methods and controls at exact F. Preservation assertions are coverage pins unless additional discriminating evidence is recorded; no blanket claim that every assertion has fail-before proof is made.

## Public state

The final implementation-time live read at 2026-11-06 16:54 UTC still showed public PR head O, open, base 65a33d03ce4fe6ddad8968b3d0de8c1aeab77165, updated at 12:19:21 UTC. The contributor's branch was not changed. No comment, PR-body edit or JIRA write was made.

The current PR body still names the removed StrFieldTest.testIgnoredFieldCanBeWritten. A future authorized description should instead name BasicFunctionalityTest.testIgnoredFields and the added PrimitiveFieldTypeTest / TestICUCollationField methods, and describe the sibling production guards. Do not publish unexecuted results. Any public follow-up requires the complete matching receipt and fresh live-state reconciliation under the root authorization rules.

The gate is an open execution handoff, not evidence that a Linux host has claimed it or that F is built or validated.
