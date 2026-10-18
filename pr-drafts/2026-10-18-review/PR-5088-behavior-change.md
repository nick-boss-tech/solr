# PR 5088 draft: behavior change section (for Nick to apply)

Status: DRAFT. Not applied. The live PR body changes only when Nick approves.
Where: insert before "## A choice to check" in the PR 5088 body.
Decision: the two production commits stay in PR 5088 (no new ticket, no new PR, no regate of a test-only branch). Recorded in claims/pool-vm3-solr-13696-split.md.

---

## Behavior change

**Two requests that fail on main now succeed. Both paths are reached only by dimensional aliases.**

- **Create alias through the V2 API, dimensional alias.** On main, the request reaches the overseer without the top-level router name and field, and the Dimensional tests fail at CREATEALIAS on the base commit. After this change, the message carries `Dimensional[TIME,CATEGORY]` style router name and a comma-joined router field, the same shape SolrJ already sends. Every dimensional alias created through the V2 create-alias API takes this path. Single-router aliases are unchanged.
- **Time route with a string timestamp.** On main, `formattedRouteValues` casts the field to `Date`, so a String value throws ClassCastException. After this change, the value goes through `parseRouteKey`: `Instant` and `Date` values behave as before, and a `CharSequence` is parsed as ISO-8601 with `Instant.parse`. A string that is not ISO-8601 now fails in the parser instead of with ClassCastException. Only dimensional aliases call this method.

**Asked of reviewers:** please confirm you accept both changes in this PR. If you would rather see the create-alias fix on its own ticket, say so, and the test repair will wait for that change to land first.
