# PR formula (Nick, 2026-10-04)

Goal: every PR reads the same predictable way, so a maintainer can judge
it in minutes. The pattern Nick named: a detailed statement of what is
happening, then a pointed question with a mostly obvious answer, with
the work already done so the answer costs the maintainer nothing.

## Shape of every PR description

1. **What happens today.** The concrete failure or gap, stated in
   detail: inputs, the wrong output or error, where it surfaces.
   Verifiable against the ticket and the code. Symptom only; the fix
   belongs in section 2.
   Exception (2026-10-08, from PR #5061): when the ticket has
   competing open PRs or a genuinely unsettled direction, this
   section may carry a short context passage naming the alternatives
   and the state of the discussion, because the direction is part of
   what the maintainer is being asked to judge. Keep it factual and
   brief; the symptom still leads.
2. **What this change does.** The route taken, in a few sentences.
   Behavior changes stated openly, including ones wider than the
   ticket's narrowest reading.
3. **Proof.** A test that fails on the base code and passes
   with the change, named in the description with its run counts.
   The maintainer can check out the base, run one test, and see the
   failure themselves. One line per test class, counts inline, with
   the verification date and head. If the proof is inconclusive by
   construction (new API, behavior-preserving refactor, test-only
   change), say so plainly instead of dressing it up.
   Proof reports outcomes only. It does not narrate internal process
   catches: our own lint, formatting, or forbidden-API slips that the
   gate caught and fixed before push are noise to a maintainer and
   stay out, unless the catch changed the behavior being shipped
   (Nick, on #5017, 2026-10-04).
4. **The choice, as a question.** When a real design decision existed:
   the options considered, the one implemented, and a pointed question
   asking whether the call was right. The evidence in sections 1 to 3
   should make the intended answer mostly obvious; the maintainer is
   confirming or redirecting, not designing from scratch. The bar for
   "live alternative" (tightened 2026-10-04, Nick): a maintainer must
   plausibly pick the other route, usually because the implemented
   route carries a cost or a behavior change someone could reject. A
   decision that was only "scope the PR narrowly versus fix the
   broader issue" is NOT a choice: narrow scope is a community
   default, and asking for ratification is noise. Instead the
   broader issue gets one line in Limits, named, with an offer to
   open a follow-up ticket and PR for it on request. The same
   applies to an adjacent bug found during the work but left
   unfixed for scope: name it in Limits and make the same offer
   (Nick, 2026-10-04; precedent: #4968's cleanup=true drive-by
   bug, which that PR's description carries as its own section).
   **Big-PR amendment (Nick, 2026-10-05):** the one-question
   shape above fits straightforward patches. A big PR (multi-part
   change, new API surface, real design space) may list several
   choices to check, and scope questions on a big PR belong in
   that section rather than only in Limits: pose the question,
   state the position we implemented, and say plainly that we
   are willing to do the broader thing in this PR if maintainers
   prefer it. The balance between narrow-and-easily-reviewed and
   thorough is expected to be worked out in discussion on the
   PR; the description opens that discussion instead of settling
   it alone, the way a repo is not built in one commit. Expect
   more maintainer discussion on big PRs and write for it. The
   small-patch rule is unchanged: on a straightforward patch a
   narrow-scope call is still not a choice section, and an
   adjacent bug still gets the Limits line with the follow-up
   offer.
   **Recommendation amendment (Nick, 2026-10-22):** a choice is
   not complete when it is posed; posing is the first step and
   the follow-up is a recommendation. Every choice section
   carries three things. (1) **Context:** the verified current
   state of the world the options live in: what exists today,
   what is broken, what each option costs in that world. A
   maintainer must not need to rediscover a fact the choice
   depends on; if an option rests on something outside the PR
   (a dead service, a gated workspace, another page that is
   itself broken), that fact is stated with its evidence. (2)
   **A decision ladder for the reader:** our own ladder runs
   before drafting and decides what, if anything, is worth
   posing; it is never posted. What the section gives the
   maintainer is a ladder of theirs: the deciding
   considerations in order, each rung a question they can
   answer from the stated context, with what follows from each
   answer, ending at the option those answers select. A
   maintainer who weighs a rung differently can stop there and
   land on the other option, and the section makes that
   landing visible too. The rungs are written in the subject's
   own terms (readers, costs, behavior), never in our process
   terms. (3) **A recommendation:** one option named as
   recommended, with the reason tied to the context and to
   where our answers land on the reader's ladder, and the
   implemented position matching the recommendation unless the
   text says why it does not. The pointed question stays, but
   it asks the maintainer to confirm or redirect a
   recommendation, not to pick from a bare list.
   **Rival-PR rule (Nick, 2026-10-22):** an earlier open PR by
   another author on the same ticket is never posed as a
   choice, and the text never recommends closing it or claims
   either PR is "the way to go". Instead, a short note thanks
   and praises the earlier author, credits whatever this PR
   takes from theirs, and says plainly that this PR opens the
   fix fresh because the issue matters. Then it moves on; the
   earlier PR's fate is its author's and the maintainers'.
5. **Limits.** What is not covered, not tested, or deliberately out of
   scope. Named, not omitted.
6. AI header at the top AND the AI footer, on every PR
   description (Nick confirmed both stay, 2026-10-04; the header on
   #4965 specifically answers sigram's disclosure request there).
   Footer form (Nick, 2026-10-18, replacing the headed "AI
   assistance" block): one italic line, "*AI-generated description.
   Nick Shanin directed the work and takes responsibility for this
   contribution.*" The header does the disclosure; the footer's job
   is the responsibility statement, kept in one line.
   Comments longer than one sentence carry the header; one-sentence
   comments stay plain. The footer goes only on PR descriptions and
   new Jira ticket descriptions, never on comments.

Length guide: the whole description should read in about one
screenful, roughly under 3,500 characters unless the ticket is
unusually complex.

Presentation rule (Nick, 2026-10-08, from the PR #5062 review):
every section opens with a bold one-line summary that carries the
claim once, and the text beneath it carries only the evidence and
the citations (file and line anchors, PR and ticket links). A claim
stated in the summary is not restated in the body; repetition across
Change, Choice and Limits is how overstated claims survive review,
because each restatement drifts a little wider than the evidence.
Citations are real links, not bare text (Nick, 2026-10-08: he
checked and the plain file:line citations do not render as links):
link each file citation to the blob at the PR head SHA
(github.com/nick-boss-tech/solr/blob/<head-sha>/<path>#L<a>-L<b>).
Citations to code the change produces link the PR head SHA;
citations to the pre-change symptom link the merge-base commit,
and the text says so.
Cross-repo PR references go as full markdown links, never bare
#NNNN, which GitHub resolves against the wrong repo.

Simple language (Nick, 2026-10-08: "Even AI does much better with
simpler language"): short sentences, one idea per sentence, plain
words in place of compressed jargon ("planted error", not
"negative control"; "checked", not "dispositioned"). Simplification
applies to the wording only, never to the claims: measurements,
counts, hashes and citations stay exact and complete. Expect the
same length; the gain is readability. The first drafts under this
rule are the -simple.md bodies for PRs #5061 and #5062 in the
round 37 folder.

No signposting (Nick, 2026-10-22). Never announce content before
presenting it, and never recap it after. Announcing a thing, then
saying it, then summarizing it makes the reader process the same
content three times, and it is the pattern he named as what makes
AI text annoying to humans. Worked examples from the SOLR-16155
draft, before and after:

- Before: "An example shows what changes. A document with id 42
  fails because..." After: the text starts with the document
  itself: "Take a document with id 42 whose price field holds the
  text abc."
- Before: "Three things follow. The value abc no longer reaches
  the client at all..." After: the first of the three sentences
  stands on its own, with no announcement in front of it.
- Before: "One question decides it: is dropping the exception text
  from the response acceptable? If not, that piece can be reverted
  on its own; the message changes do not depend on it." Then, as
  the wrap-up, the same question a second time: "Keep the response
  scrub, or revert just that piece?" After, asked once: "Keep the
  response scrub, or revert just that piece? It stands on its own;
  the message changes do not depend on it."

The rule covers section openings and closings in a description and
whole comments alike: say the thing itself, once.

## How this maps to the gate

The harden gate already produces every input: tidy, Error Prone
compile, pre-fix proof result, focused test counts from the JUnit XML,
module check. The receipt for a branch is therefore also the skeleton
of its PR description: sections 3 and 5 come straight from the
receipt, section 2 from the branch's commits, section 4 from
design-decisions-open.md.

## Contribution guidance (summary of dev-docs/how-to-contribute.adoc)

The project's contribution guidance lives in
`dev-docs/how-to-contribute.adoc`. Agents do not need to read it:
this summary is sufficient, and it is what our PRs are held to.
AI-assisted work is human-reviewed, its use is declared
transparently, and the named human takes responsibility for it (the
header and footer in section 6 do this). A unit test shows the bug
was fixed or the feature works (section 3). Code is tidy-formatted
and dependency licenses are current before the PR. The rest of the
adoc is common sense or process steps that assume Jira write
actions; our agents cannot write to Jira at this time, so any step
of that kind stays with Nick and is never a reason to stall a
branch. The adoc's readying command is `./gradlew tidy
updateLicenses check -x test`. Owner ruling (Nick, 2026-10-19):
the full `check` is not required for our branches; the gate's
module check is sufficient, with tidy and licenses kept current.

## Approval freeze (2026-10-23, from the #5180 night)

Once a maintainer approves a PR, push nothing to it without the owner's explicit go, even a change the reviewer suggested. An approval freezes the tree in the approver's mind; a new commit after approval tells them the target moved and resets whatever validation they had planned against the approved head. The reviewer's own suggestion can wait a day; the approval cannot be un-spent.

## Frame a design comment in the reviewer's own terms (2026-10-23, from SOLR-18470)

When a ticket's subject is a deprecation or removal, name that question in the comment's own terms. A plan comment that frames the work as narrowing a parameter's authority, while the reviewer frames it as deprecation for a named release, reads as a quiet re-scope of the ticket and lands as a review request for work the reviewer did not agree to scope. State the deprecation question, the narrowing step, and the relation between them, in that order.

## Standing rules that still apply

- Description edits are Muse's to make; exception: a maintainer
  actively commenting on that PR means leave the description alone
  and check with Nick first.
- Minimize comments on PRs; correct by editing the description.
- No "Claude" in commit authors, committers, or trailers.
- A prepared branch is not finished until its PR description draft exists (Nick, 2026-10-22). The draft lives as the final section of the branch receipt (receipts/SOLR-<n>.md), written when the gate goes green, so any host can open the PR from the record alone.
- Changelog fragments list the author by name only (`name: Nick Shanin`), with no `nick:` line (Nick, 2026-10-15). The upstream `dev-docs/changelog.adoc` example includes one; remove it from any fragment before committing.

## Worked example: offering a tested fix on an existing PR (2026-10-27)

Use the [posted PR 5251 follow-up](https://github.com/apache/solr/pull/5251#issuecomment-6457683261) as an example when offering a completed, verified patch to another contributor. Nick approved this wording and its diff link and fast-forward commands. The proof is in [receipts/SOLR-18598.md](receipts/SOLR-18598.md), GREEN at `e7834ca954505c27778e59a61140bf02490226a6`; the original contributor head is `ca539cad196f26c78e14a1dc698b84e0f5c1f22e`.

- Lead with the tested fix and a pinned diff from the contributor's head to the tested commit. State the behavior changed and the relevant compatibility preserved.
- Include passing focused and neighboring test counts, the exact tested head, and completed formatting, build and documentation checks. State material execution conditions such as unsetting `NODE_ENV`.
- Summarize the fail-before result alongside the positive proof. Claim only observed failures or behavior established by the code; this example reports missing rejections and an array-bounds exception, not a measured wrong count.
- Verify ancestry against the current PR head, then give commands to fetch and fast-forward to the tested commit without rewriting the contributor's history.
- End with one question about including the ready patch. The contributor decides whether to take it; the comment does not ask them to design work that is already implemented.

For this clean, exact-head follow-up, the pinned diff and pull-in commands belong in the comment. The default no-diff rule for an unsolicited proposal in `comment-formula.md` does not require another decision about whether this completed fix is mechanical. Existing public-posting authorization rules still apply. Keep internal draft-status lines out of the posted text, and use the reviewed-by-Nick disclosure after Nick has reviewed the wording. Copy the structure, not this example's ticket-specific counts, hashes or environment claims.

Posted text, verbatim:

> 🤖 *AI text below* 🤖 *(reviewed by Nick Shanin)*
>
> Hi @elangelo, I have a tested follow-up for the mixed-precision case you noted: [one-commit diff](https://github.com/nick-boss-tech/solr/compare/ca539cad196f26c78e14a1dc698b84e0f5c1f22e...e7834ca954505c27778e59a61140bf02490226a6).
>
> It rejects different register counts or widths before union, with an error naming `solr.facet.hll.log2m` and both layouts. Different explicit thresholds still merge. It also adds regression tests and documents the restart requirement.
>
> Verified on Linux at `e7834ca…`, using JDK 21.0.12.1, Gradle 9.7.0 and seed `5251C0DE`:
>
> - 11 focused tests, 12 local/distributed JSON facet cases, and 27 neighboring tests passed, with no skips.
> - Tidy, root `check -x test`, and reference-guide build/link checks passed, with `NODE_ENV` unset.
> - All five rejection regressions fail against the original production code: four allow the mismatch, and the smaller-source merge throws `ArrayIndexOutOfBoundsException`.
>
> The commit is a fast-forward of the current PR head:
>
> ```sh
> git switch json-facet-hll-precision
> git fetch https://github.com/nick-boss-tech/solr.git solr-18598-submit
> git merge --ff-only e7834ca954505c27778e59a61140bf02490226a6
> ```
>
> Would you like to include this guard in the PR?

## Template (approved draft, 2026-10-04)

```markdown
🤖 *AI text below* 🤖 *(posted on behalf of Nick Shanin)*

https://issues.apache.org/jira/browse/SOLR-<ticket>

## What happens today

<The failure or gap in detail: exact inputs, the wrong output or
error, where it surfaces. Two to five sentences a maintainer can
verify against the ticket and the code.>

## What this change does

<The route taken, briefly. Every behavior change stated openly,
including effects wider than the ticket's narrowest reading.>

## Proof

<The new or changed test class, named. It fails on the base code
with <the observed failure> and passes with this change: <counts
from the test run, e.g. TestX 12/12; verified <date> at this head>.>
If the proof is inconclusive by construction (new API, refactor,
test-only): <say exactly that, and why.>

## A choice to check   (only when a real decision exists)

<The verified context the options live in; the options; a short
decision ladder written for the reader, the deciding
considerations in order and where each answer lands; the one
implemented; our recommendation and its reason. Ends with the
pointed question, now confirming or redirecting the
recommendation: Was this the right call?>

## Limits

<What is not covered, not tested, or deliberately out of scope.
Named, not omitted.>

Changelog: [changelog/unreleased/SOLR-<ticket>.yml](https://github.com/nick-boss-tech/solr/blob/<head-sha>/changelog/unreleased/SOLR-<ticket>.yml)

*AI-generated description. Nick Shanin directed the work and
takes responsibility for this contribution.*
```

Rules for filling it in:

- The Jira link line stays (David Smiley asked for it on #4965; the
  Apache template wants it kept).
- The changelog line is a link to the fragment at the head SHA, like
  every other file citation (main-side decision, 2026-10-10).
- "A choice to check" appears only for a real decision with a live
  alternative. A section that merely restates what the code does is
  not a design choice (Nick's #4959 parking call) and is left out.
  A choice section always carries its context, a decision ladder
  written for the reader, and a recommendation (Nick, 2026-10-22):
  a bare list of options with a question is an unfinished choice,
  because recommending an option is the follow-up to posing one,
  and the reader is handed the ladder our recommendation climbed,
  not our own internal one.
  Narrow-scope-versus-broader-fix is never a choice section on a
  straightforward patch; the broader issue is named in Limits with a
  follow-up ticket and PR offer (Nick, 2026-10-04; first applied to
  #5000; extended the same day to adjacent bugs found but left
  unfixed for scope, precedent #4968). On a big PR, scope questions
  may appear in the choice section when each carries our position
  plus an explicit willingness to do the broader thing in this PR
  (Nick, 2026-10-05 amendment in section 4 above).
- Proof numbers come from the gate receipt or a proof re-run, never
  from memory. If no proof was ever run, run the proof step before
  rewriting the description.
