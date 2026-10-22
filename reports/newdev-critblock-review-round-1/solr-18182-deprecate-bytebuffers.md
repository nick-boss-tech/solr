# Review: SOLR-18182, branch solr-18182-deprecate-bytebuffers (unit 36)

Head reviewed: 0abcca2f4b99b92136e5a5650fc7262bbe24ab17. Base: c8ab71430f. Ticket: SOLR-18182, "Deprecate ByteBuffersDirectoryFactory".

## Pass 1

- Ticket fit. The ticket asks to deprecate `ByteBuffersDirectoryFactory` in favour of `RAMDirectoryFactory`. The branch adds `@Deprecated(since = "11.0")` and a javadoc `@deprecated` note. The note says the two factories create the same directory with the same lock handling. Checked in code: `RAMDirectoryFactory.createLockFactory` and `create` are the same as the deprecated factory's (`new ByteBuffersDirectory(lockFactory)`, `SingleInstanceLockFactory` / `NoLockFactory`). That matches the note.
- Scope. Annotation and javadoc in one class, one test method, and a changelog file.
- Finding, minor: the rejected-lock error message on the deprecated factory still names `ByteBuffersDirectory`, while the replacement names `RAMDirectory`. The note's "same lock handling" holds for accepted values; the error text differs. Recorded, not repaired.
- Finding, minor: the new test method is `testDeprecated`, and it checks only the annotation's presence, not the javadoc text or the `since` value. Recorded, not repaired.
- Proof leg: the test fails on base as expected (annotation absent).
- Repairs: none.

Regate: `gates/SOLR-18182-review-pass-1.md`, GATE GREEN at 0abcca2f4b on vm4.

## Pass 2

Review (2026-10-22, vm4), same head 0abcca2f4b. - Pass 1 minor findings, unchanged and confirmed in the file: the rejected-lock message still names `ByteBuffersDirectory` while the replacement names `RAMDirectory`; the new test checks only the annotation.
- Repairs: none.

Regate: `gates/SOLR-18182-review-pass-2.md`, GATE GREEN at 0abcca2f4b on vm4.
