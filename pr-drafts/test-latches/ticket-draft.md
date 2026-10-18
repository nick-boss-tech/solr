# Ticket draft: replace sleeps in Solr tests with latches and wait helpers (not filed)

Status: DRAFT. Not filed. Filing is a public write and needs Nick's approval. Replace nothing else in this text.

Issue type: Task. Component: Tests. Related: SOLR-16630 (the same change in one test class, `TestCoordinatorRole`).

Summary: Replace Thread.sleep calls in Solr tests with latches or existing wait helpers

Description:

Solr's test sources have 374 sleep calls in 155 files on main (c8ab71430f, 2026-10-18). Most sleep while waiting for a condition, and that makes the tests slow and sometimes flaky on slow machines. SOLR-16630 converted every sleep in one test class to latches and waits, and the focused test passed on six seeds. This ticket applies the same approach to the rest.

Scope: test sources only. No production change.

Method: each sleep is converted file by file. The design for each file is attempted and recorded. A sleep that cannot be replaced is kept with a stated reason. Poll loops are replaced with the wait helper for the condition they check, or with a latch counted down by the code under test.

Phases:
1. Sleeps inside polling loops.
2. Sleeps next to an existing wait helper, removed where the wait alone is enough.
3. The rest, read one by one.

Inventory: attached as a list of file, line and code.

---

### AI assistance

AI agents assisted with the inventory, research, implementation, review, and drafting. Nick Shanin directed the work and takes responsibility for this contribution.
