🤖 *AI text below* 🤖 *(posted on behalf of Nick Shanin)*

https://issues.apache.org/jira/browse/SOLR-17914

Part of this diff is the SOLR-17913 collection details card. The create dialog builds on it, so it is included here. SOLR-17913 was not part of the work Christos Malliaridis asked for, and no approval was sought for it; it is here only because SOLR-17914 requires it. The card is on its own branch, `solr-17913-collection-details-card`, gated at its head, and it can be split out or opened as its own PR if that is preferred. Christos is the sponsor and promised reviewer for this ticket. He offered the former designs as a temporary way to create collections while the new designs are in progress (comment 18133018). This PR follows the new work-in-progress design (Figma node 2635-40394), so the layout may change when that design is final.

## What happens today

**The new UI cannot create a collection.** Nothing in the new UI sends a create request. The Collections section is not in the new UI yet. The old UI's duplicate-name check is a `@todo` ([collections.js:230](https://github.com/apache/solr/blob/c8ab71430f6e7547c773da596101d680862310ff/solr/webapp/web/js/angular/controllers/collections.js#L230)). The server rejects a duplicate name with a 400 "collection already exists" ([CreateCollectionCmd.java:134](https://github.com/apache/solr/blob/c8ab71430f6e7547c773da596101d680862310ff/solr/core/src/java/org/apache/solr/cloud/api/collections/CreateCollectionCmd.java#L134)).

## What this change does

**It adds the create dialog, the create request, and the error mapping.**

- `CreateCollectionForm` shows the name, configset, shards, NRT, TLOG and PULL counts, a read-only placement field, and a collapsed advanced section with router, route field, async and properties. Create is enabled only when the input is valid ([line 131](https://github.com/nick-boss-tech/solr/blob/6170ba10ac5061203339fa0a96e714c7e64616c2/ui/shared/src/commonMain/kotlin/org/apache/solr/ui/views/collections/CreateCollectionForm.kt#L131)). The name rule matches the server pattern but also rejects periods ([line 84](https://github.com/nick-boss-tech/solr/blob/6170ba10ac5061203339fa0a96e714c7e64616c2/ui/shared/src/commonMain/kotlin/org/apache/solr/ui/views/collections/CreateCollectionForm.kt#L84)).
- `HttpCollectionsRepository` posts the request to `api/collections`. A 400 whose message starts with "collection already exists" becomes `NameExists`. Other failures keep the server message ([lines 34-50](https://github.com/nick-boss-tech/solr/blob/6170ba10ac5061203339fa0a96e714c7e64616c2/ui/shared/src/commonMain/kotlin/org/apache/solr/ui/components/collections/data/HttpCollectionsRepository.kt#L34-L50)).
- `CreateCollectionResult.nameError()` and `generalError()` turn those results into form text ([CreateCollectionErrors.kt](https://github.com/nick-boss-tech/solr/blob/6170ba10ac5061203339fa0a96e714c7e64616c2/ui/shared/src/commonMain/kotlin/org/apache/solr/ui/views/collections/CreateCollectionErrors.kt)).

Nothing in the app calls the form yet. The Collections page that hosts it is SOLR-17912.

## Proof

**The new tests pass, and the full JVM suite passes at head `6170ba10ac`.** Run 2026-10-28 on the software renderer, because the test host has no GPU:

- `HttpCollectionsRepositoryTest` 5 of 5: request body, duplicate name, alias conflict, server message kept, non-JSON failure.
- `CreateCollectionFormTest` 9 of 9.
- `CreateCollectionErrorsTest` 3 of 3.
- `CollectionDetailsCardTest` 2 of 2.
- Full `:shared:jvmTest`: 66 tests, 0 failures, 0 errors, 2 skipped (`UserAuthenticationContentTest` and `StartContentTest`, outside this change). `:shared:spotlessCheck` passes.

Mutation check: widening the duplicate-name match to any 400 with a message makes `testAliasConflictIsNotMappedToNameExists` and `testKeepsTheServerMessageForOtherFailures` fail. A base run is not possible here: the repository is new code, so the base has nothing to fail on.

Browser verification: not done. The form is not reachable in the app yet.

## The choices, with recommendations

**1. The server's response is the duplicate-name check.** Recommended. The new UI has no collections list to check against. A pre-check would need a new data source and could still race. The server's response cannot race, and the server already returns it (rung 1, see the link above). Is the server response the right check, or should the form also load the collection list?

**2. The name rule is stricter than the server.** Recommended as built. The design's helper text, "Letters, digits, _ and -", is what the user reads. The server also allows periods, so the form never sends a name it told the user was invalid. This choice is settled here (rung 3).

**3. Properties are typed as `key=value` pairs, separated by commas.** Recommended. The server takes a string map ([CreateCollectionRequestBody.java:54](https://github.com/apache/solr/blob/c8ab71430f6e7547c773da596101d680862310ff/solr/api/src/java/org/apache/solr/client/api/model/CreateCollectionRequestBody.java#L54)). The design names "properties" but draws no control, so a plain text field is the smallest input that maps to that type. Would you rather have a key and value row editor?

**4. A failure shows the server's message, or "Could not create the collection." when the response has none.** Recommended. A server message is more specific than any fixed text, and the fallback covers responses without one. This choice is settled here (rung 4).

**5. The create request ships with the form.** Recommended. The first acceptance criterion is "the user can successfully create a new collection", and only the request meets it. The header action that opens this dialog stays with SOLR-17912. Should the request wait for that PR instead?

## Limits

- The header "+ Create Collection" action, drawn beside Restore on the Collections page, is not built. It depends on SOLR-17912. A follow-up PR can cover it on request.
- Not built: the placement preview card, the "Preview configset" link and configset usage count, and the replica-type help link. None is in the ticket's acceptance criteria, and the card needs node and zone data the new UI does not have.
- Placement shows only the one option drawn. No other option is invented.
- Tooltips and input limits are not in this change. The ticket puts them in a separate ticket.

*AI-generated description. Nick Shanin directed the work and takes responsibility for this contribution.*
