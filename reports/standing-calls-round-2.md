# Standing calls, round 2

Item 1 (SOLR-15358, the default docValues as stored call) is settled. It survived rung 4 of the decision ladder: a genuine choice between two defensible behaviors. It is posed as a Choice in `pr-drafts/schema-analysis/SOLR-15358.md`, with the implemented position stated. It is not returned to Nick, because the assignment's settlement shape for a Choice is a draft Choice, not a decision by the owner.

- Assignment: `assignments/pool-standing-calls-round-2.md`. Claim: `claims/pool-standing-calls-round-2-1.md`.
- Branch: `solr-15358-submit`, tip `cbb345f2e7df452e20036d357c82cc63db96ffd3`, checked by `ls-remote` at claim time (2026-10-15T18:23Z). Unchanged.
- Host: Windows review lane. Read-only: no build, no test, no branch push, no PR or JIRA write.

## Question

What does a sub-field that does not declare docValues end up with under this change, and is the outcome the branch should ship?

## Facts, checked in the code

Branch head (`solr/core/src/java/org/apache/solr/schema/CurrencyFieldType.java`):
- L177: `createFields(SchemaField field, Object externalVal)` builds the currency field.
- L182 and L184: the amount and currency code sub-fields call `createFields`, not `createField`.

Base (`upstream/main`):
- `FieldType.java` L342 to L352: the base `createFields` returns the `createField` result unchanged when the field does not declare docValues. It throws `UnsupportedOperationException` only when docValues are declared and the type returns no docValues field. So a sub-field without declared docValues gets exactly the field it got before.
- `FieldType.java` L190: `if (schemaVersion >= 1.6f) properties |= USE_DOCVALUES_AS_STORED`. The default applies on schema version 1.6 and later, as the draft says.
- `FieldProperties.java` L74: `useDocValuesAsStored` is a per-field property name, so a field can opt out in its own definition.
- `SolrDocumentFetcher.java` L103 and L186: the normal document fetch treats a `docValues=true, stored=false, useDocValuesAsStored=true` field as a stored value. That is the path that makes `fl=*` return it.
- `RealTimeGetComponent.java` L998: real-time get does the same.

## Answer to the question

- A sub-field that does not declare docValues: nothing changes. It gets the same single field as before, with no docValues, and it is not returned by `fl=*` or real-time get.
- A sub-field that declares `docValues="true"`: it now gets docValues, which the old `createField` call dropped silently. Because it is unstored, the default rule on schema 1.6 and later returns it in `fl=*` and real-time get. That is the same rule every other docValues field already follows.

## Decision

The branch ships option (a): keep the default. Reasons:

1. The rule is already the base behavior for every docValues field on schema 1.6 and later (`FieldType.java` L190, `SolrDocumentFetcher.java` L103). Option (a) adds no currency-specific rule.
2. Option (b) needs either a currency-only special case, which base has no precedent for, or an opt-out written on every sub-field type. That is a configuration choice, not a fix for this change.
3. Option (a) does not lock anyone out. A user who wants the sub-fields hidden sets `useDocValuesAsStored="false"` on the sub-field type (`FieldProperties.java` L74).
4. The choice is genuine. A maintainer could plausibly prefer that derived currency sub-fields stay out of `fl=*`. So it is posed to them, not settled in code: that is rung 4, and the draft's Choice is the settlement.

## Draft change

`pr-drafts/schema-analysis/SOLR-15358.md`, section "A choice to check": the Choice now says "The branch ships (a)." and names the per-field opt-out for option (b) (`useDocValuesAsStored="false"` on each sub-field). The question to maintainers is unchanged.

## Record change

`reports/schema-convergence-round-2.md`: a settlement note is appended at the end. The round 2 verdict lines are history and stay as written. The settlement note says the owner call on this item is closed as a posed Choice, and that the only hold left on SOLR-15358 is landing after SOLR-15357.

## Left open (not part of this item)

- The change's visible output effect (`fl=*` and real-time get for a currency document with a docValues sub-field) has no test. The draft's Limits already says so. A test is an implementation task, not a settlement, and is a follow-up offer if the maintainers pick (a).
- Landing after SOLR-15357 remains a hold on the draft (not part of this item).
- RC-S5 from `reports/schema-recheck-15358-record-close-round-1.md` is still open: `reports/schema-convergence-round-2.md` line 14 says the placeholder is unfilled. It is a one-sentence fix in the schema record. It is not part of this item, so it is left for the schema lane.

## Checks

- Draft text: no em dash or en dash in the edited sentence.
- Nothing was built, tested, pushed, posted, or edited on GitHub or JIRA.
