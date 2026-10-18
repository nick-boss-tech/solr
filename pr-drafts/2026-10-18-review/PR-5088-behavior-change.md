# PR 5088 draft: behavior change and maintainer choices (for Nick to apply)

Status: DRAFT. Not applied. The live PR body changes only when Nick approves.
Where: replace the "## A choice to check" section of the PR 5088 body with the two sections below. The "## Limits" and "### AI assistance" sections stay as they are.
Decision: one PR. The production commits stay with the test repair (no split, no new ticket). Recorded in claims/pool-vm3-solr-13696-split.md.

---

## Behavior change

**Two requests that fail on main now succeed. Both paths are reached only by dimensional aliases.**

- **Create alias through the V2 API, dimensional alias.** On main, the request reaches the overseer without the top-level router name and field, and the Dimensional tests fail at CREATEALIAS on the base commit. After this change, the message carries a `Dimensional[TIME,CATEGORY]` style router name and a comma-joined router field, the same shape SolrJ already sends. Every dimensional alias created through the V2 create-alias API takes this path. Single-router aliases are unchanged.
- **Time route with a string timestamp.** On main, `formattedRouteValues` casts the field to `Date`, so a String value throws ClassCastException. After this change, the value goes through `parseRouteKey`: `Instant` and `Date` values behave as before, and a `CharSequence` is parsed as ISO-8601 with `Instant.parse`. A string that is not ISO-8601 now fails in the parser instead of with ClassCastException. Only dimensional aliases call this method.

## Choices for maintainers

**Two decisions need a maintainer's call. Each one sets the behavior in this PR against a stricter alternative.**

**1. Should the V2 create-alias API accept dimensional aliases?**

- This PR: yes. The V2 request sends the same router name and field shape that SolrJ already sends, so both create paths agree.
- Alternative: no. The V2 API keeps rejecting dimensional aliases without the top-level fields, and the Dimensional test fixture creates its alias another way. No change to the API.

**2. Should a time-routed document with a string timestamp be parsed or rejected?**

- This PR: parse. A string is read as ISO-8601. The same class already parses string route values on another path (base line 379, `parseRouteKey`).
- Alternative: reject. Return a 400 error that names the field, instead of parsing the value.

**Our recommendation:** the choices in this PR for both. Choice 1 removes a difference between the V2 and SolrJ create paths. Choice 2 uses the parser the class already has. The alternatives are reasonable if a maintainer prefers a narrower API surface.
