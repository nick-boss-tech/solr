# SOLR-18347: Admin UI bugs from Selenium, checked against main, round 1

Claim: `claims/warm-ticket-evidence-units-round-1-unit-4-SOLR-18347-vm2.md`. Job: `assignments/warm-ticket-evidence-units-round-1.md`, unit 4.
Base: apache/solr main `13c693d805f6541f7cec684cd27d28eef7b3b614`, read with `git grep`. Ticket read through the Jira REST API on 2026-10-25 (Open, reporter Jan Høydahl, unassigned, updated 2026-10-06, six comments). Paths are under `solr/webapp/web/` or `solr/core/src/java/org/apache/solr/` as noted.

The ticket lists five open items and one "Done elsewhere" item. The "Done elsewhere" item (security dialogs under automation) is not checked here.

## Results

| # | Item | Verdict on main | Evidence |
| --- | --- | --- | --- |
| 1 | Menu TypeError from `$scope.showCore` on null core | not confirmed as stated | `js/angular/app.js:665-668` guards with `if (core)`. The only direct caller is `index.html:238`, `ng-change="showCore(currentCore)"`. The null read must come from elsewhere; not identified. Needs a browser run. |
| 2 | Collections screen blank without the js-client bundle | confirmed in code | `js/angular/services.js:91-96`: the `CollectionsV2` factory body reads `solrApi.ApiClient.instance` at injection time. A missing bundle fails at injection. Which controllers inject it was not checked. Blank-screen behaviour not run. |
| 3 | Schema Designer races itself, version-mismatch error | error path confirmed; race not confirmed in code | `core/.../schema/ManagedIndexSchema.java:213-217` throws `SchemaChangedInZkException` with "Failed to persist managed schema at ... - version mismatch, retry." The status is `ErrorCode.CONFLICT`, which is HTTP 409, not 5xx as the ticket's workaround says. The "Reload Schema" button is in `js/angular/controllers/schema-designer.js:66`. Whether the designer's prep and analyze calls reach this path was not traced. |
| 4 | Core overview ping widget logs a 503 with no healthcheck | not reproduced on main | `ping?action=status` returns 200 with `status=not_configured` (`handler/PingRequestHandler.java:221-222`). The widget handles that state (`js/angular/controllers/core-overview.js:57-72`, with a comment about 503). The 503 comes from enable and disable with no healthcheck file (`PingRequestHandler.java:301-304`) and from a disabled ping (`:206-209`). |
| 5 | Reload success is a one-second flash (Collections) | not confirmed for Collections; true for Cores | `js/angular/controllers/collections.js:291-309` sets `reloadSuccess = true` and never resets it. The label and class stay until the next click (`partials/collections.html:131-132`). The one-second reset is in `js/angular/controllers/cores.js:184-198`, on the Cores screen. |

## Items that need a live run

- Item 1: the reported TypeError signature is not reproducible from `showCore` on main. A browser run would show where the null read comes from.
- Item 2: the blank screen needs a run with the bundle missing.
- Item 3: the designer race needs a run with concurrent prep and analyze calls.

## Corrections to the ticket's text

- Item 3: the workaround text says the designer's "own 5xx console errors". The version-mismatch error is a 409 CONFLICT on main.
- Item 4: the status call does not return 503 on main. The widget already handles the `not_configured` state.
- Item 5: the one-second flash is on the Cores screen, not Collections.

## Limits

- Code reading and `git grep` at the stated commit. No browser, no Selenium run, no Solr instance.
- Verdicts apply to main at `13c693d805f`. The ticket dates from 2026-10-06 and the code may have changed since.
- No Jira or GitHub write. The draft is not reviewed by Nick.
