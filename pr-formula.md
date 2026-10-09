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
5. **Limits.** What is not covered, not tested, or deliberately out of
   scope. Named, not omitted.
6. AI header at the top AND the AI assistance footer, on every PR
   description (Nick confirmed both stay, 2026-10-04; the header on
   #4965 specifically answers sigram's disclosure request there).
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
(github.com/nick-boss-tech/solr/blob/<head-sha>/<path>#L<a>-L<b>),
and cross-repo PR references as full markdown links, never bare
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

## How this maps to the gate

The harden gate already produces every input: tidy, Error Prone
compile, pre-fix proof result, focused test counts from the JUnit XML,
module check. The receipt for a branch is therefore also the skeleton
of its PR description: sections 3 and 5 come straight from the
receipt, section 2 from the branch's commits, section 4 from
design-decisions-open.md.

## Standing rules that still apply

- Description edits are Muse's to make; exception: a maintainer
  actively commenting on that PR means leave the description alone
  and check with Nick first.
- Minimize comments on PRs; correct by editing the description.
- No "Claude" in commit authors, committers, or trailers.

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

<Options considered; the one implemented; the alternative's cost.
Ends with the pointed question: Was this the right call?>

## Limits

<What is not covered, not tested, or deliberately out of scope.
Named, not omitted.>

Changelog: `changelog/unreleased/SOLR-<ticket>.yml`

### AI assistance

AI agents assisted with research, implementation, review, and
drafting. Nick Shanin directed the work and takes responsibility
for this contribution.
```

Rules for filling it in:

- The Jira link line stays (David Smiley asked for it on #4965; the
  Apache template wants it kept).
- "A choice to check" appears only for a real decision with a live
  alternative. A section that merely restates what the code does is
  not a design choice (Nick's #4959 parking call) and is left out.
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
