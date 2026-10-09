# SOLR-4841 pipeline report (round 28 fresh arrivals)

## Ticket

SOLR-4841 (Bug, Open, filed 2013): "DetectedLanguage constructor should be public". `LanguageIdentifierUpdateProcessor.detectLanguage` returns `List<DetectedLanguage>`, but the `DetectedLanguage` constructor is package-private, so a custom subclass of `LanguageIdentifierUpdateProcessor` in any other package cannot construct the results it must return. Uwe Schindler commented on the ticket that "protected should be enough!"; the reporter was fine with that. Whether protected actually suffices is checked by run below (it does not).

## Branch shape

Not stacked. Three received commits on b6b2b8f10e9827e3b86e44649fb7966ce646c185 (verified as the fix commit's parent; the langid module is byte-identical between that base and cabedd1d968, checked by diff, so the base choice does not affect any file this branch touches): fix f66693deb0b ("make DetectedLanguage constructor public", one production line plus the new test), changelog 0bff90e26bb, handoff doc 524afb62181 (received tip, verified live against the fork at the start of this run and again before pushing). The change: the `DetectedLanguage` constructor in `solr/modules/langid` gains the `public` modifier; new test `CustomLanguageIdentifierTest` in package `org.apache.solr.update.processor.custom`; changelog fragment, type `fixed`. Author and committer Nick Shanin throughout, no trailer in the received history; the fix commit body carried the line "Hypothetical, unrun regression test; see SOLR-4841-TESTING.md." (see Defects).

## Review

- The ticket's mechanism is exact in the base code: `LanguageIdentifierUpdateProcessor` is public with a public constructor and a `protected abstract List<DetectedLanguage> detectLanguage(Reader)` (line 345 at base), while `DetectedLanguage` is a public immutable bean whose only constructor has no modifier. Every in-tree implementation sits in `org.apache.solr.update.processor`, so the barrier only appears for the first outside implementer, which is the ticket's reporter.
- The change is the smallest possible surface widening: one modifier on one constructor. No in-tree caller can observe a difference; the class keeps the same fields, getters, and immutability.
- The test is honest about the defect's nature: this is a compile-time access barrier, so the discriminating proof is that the test cannot compile against base production and compiles and passes at head. Placing the test in a subpackage (`...processor.custom`) is what makes the package-private barrier real rather than nominal. It extends `SolrTestCase`, not J4, and its single test method is counted from the JUnit XML (tests=1), so the pass is a run, not a silent skip. The nested `FixedLanguageIdentifier` subclass is compile-only by design: it proves a subclass outside the package can override `detectLanguage` and build its return value.
- The handoff doc's two open guesses were settled by the gate: spotless wanted no formatting change anywhere (tidy rc=0 and the shipped production and test bytes are identical to the received ones), and the compile-only nested class passes Error Prone, the module check's lint, and forbiddenApis as written, so it stays.

## Premise: GROUNDED, discriminating

Verdict from the gate worktree (log g4841-gate.log, step 3: shipped tree with `DetectedLanguage.java` reverted to base b6b2b8f10e9, focused class, seed 4841C0FFEE4841): `:solr:modules:langid:compileTestJava` FAILED with two errors, at both construction sites in the test: "DetectedLanguage(String,Double) is not public in DetectedLanguage; cannot be accessed from outside package" (CustomLanguageIdentifierTest.java:40 and :45). Gradle rc=1 and no JUnit XML was produced, because compilation failed before any test could run. At the shipped head the same class passes 1/1 (step 4a, same seed).

Step 3b probed the ticket thread's alternative: with the constructor set to `protected` instead of `public`, `compileTestJava` fails the same way, "DetectedLanguage(String,Double) has protected access in DetectedLanguage" at the same two lines (log g4841-protected-build.log). A protected constructor is reachable only from subclasses of `DetectedLanguage` itself; a custom identifier extends `LanguageIdentifierUpdateProcessor`, so protected does not enable the extension the ticket asks for. Public is the narrowest modifier that works.

## Defects

One, packaging only; the received production code and test are byte-identical in the shipped history.

- The received history carried the handoff doc SOLR-4841-TESTING.md as its tip commit, and the fix commit body called the test hypothetical and unrun. The shipped history drops the doc and replaces the body with a factual description of the change and the premise result. Process note: the gate script's first `git rm` refused on the staged-new doc, so the gate ran on a one-commit tree whose content is identical to the received tip (diff against the received tip was empty); the shipped two-commit history was built after the gate and differs from the gated tree only by the dropped doc, verified by diff (see Heads and push).

## Gate receipt at gated tree 03b23f1dee7

Log g4841-gate.log (per-branch gate script, blocking flock, BASE b6b2b8f10e9): changelog YAML parse ok (step 0, the branch's one entry); tidy :solr:modules:langid:tidy rc=0 with no file changes; Error Prone compile rc=0; pre-fix proof as above (compile failure on base production, both construction sites); protected-variant probe as above; focused tests at head from fresh JUnit XML in this worktree, CustomLanguageIdentifierTest 1/1, 0 failures/errors/skipped; neighbor tests in the module, LangDetectLanguageIdentifierUpdateProcessorFactoryTest 13/13, OpenNLPLangDetectUpdateProcessorFactoryTest 13/13, SolrInputDocumentReaderTest 4/4; :solr:modules:langid:check -x test rc=0. The gate queued on the flock behind the SOLR-15003 RCA gate and ran 11:22 to 11:28 UTC; no VM replacement touched this branch's runs and nothing needed relaunching. The shipped tree differs from the gated tree only by the handoff doc's removal, so every step covers the shipped code byte for byte.

## Heads and push

Received tip 524afb62181bd0b3233968c4aa01c5251f2112b5. Shipped: two commits on base b6b2b8f10e9, a0dc26bc448 (fix and test, message cleaned) and f8850ffd421 (changelog fragment, message as received); handoff doc dropped; production and test content byte-identical to the received commits. Author and committer Nick Shanin, no trailer. Pushed by --force-with-lease pinned to the received tip re-read in the same step (fork tip re-checked immediately before pushing: still 524afb62181b, no SOLR-4841 DONE entry in the takeover log); ls-remote verified at f8850ffd421439378cb55049d0ecaeaed347a476. GH corroboration run 37614488227 on ci/4841-langid-r28 (org.apache.solr.update.processor.custom.CustomLanguageIdentifierTest, :solr:modules:langid) at the shipped head completed SUCCESS (job steps verified via the API: the focused test step and the upload step both succeeded, no runner defect). No PR opened, no comments posted.

## Draft PR description

🤖 *AI text below* 🤖 *(posted on behalf of Nick Shanin)*

https://issues.apache.org/jira/browse/SOLR-4841

## What happens today

LanguageIdentifierUpdateProcessor is the abstract base for language detection in the langid module. Its detectLanguage(Reader) method is protected abstract and returns List<DetectedLanguage>, so writing a custom identifier means extending the class in your own package and returning DetectedLanguage instances. That is impossible today: DetectedLanguage is a public class, but its only constructor is package-private, so code outside org.apache.solr.update.processor cannot construct one. Every in-tree implementation sits in that package, which is why the gap went unnoticed; the first custom implementation in another package hits it immediately.

## What this change does

The DetectedLanguage constructor is now public. Nothing else changes: the class stays immutable with the same two fields and getters, and all existing callers use the constructor exactly as before.

## Proof

CustomLanguageIdentifierTest (new) lives in org.apache.solr.update.processor.custom, outside the processor package. It constructs DetectedLanguage directly and defines a fixed-result subclass of LanguageIdentifierUpdateProcessor whose detectLanguage returns a constructed instance. On the base code the test does not compile: "DetectedLanguage(String,Double) is not public in DetectedLanguage; cannot be accessed from outside package" at both construction sites. With this change it passes 1/1, and the module's existing tests still pass (LangDetectLanguageIdentifierUpdateProcessorFactoryTest 13/13, OpenNLPLangDetectUpdateProcessorFactoryTest 13/13, SolrInputDocumentReaderTest 4/4); verified 2026-10-07 at f8850ffd421.

## A choice to check

On the ticket, Uwe Schindler suggested a protected constructor would be enough. It is not, for this use: a protected constructor is only reachable from subclasses of DetectedLanguage itself, and a custom identifier extends LanguageIdentifierUpdateProcessor, not DetectedLanguage. Verified by compiling the new test against a protected variant of the constructor: it fails with "DetectedLanguage(String,Double) has protected access in DetectedLanguage" at the same two sites. Public is therefore the narrowest modifier that enables the extension the ticket asks for, and the class is an immutable value bean, so a public constructor exposes no mutable state. Was going straight to public the right call, rather than protected plus asking custom identifiers to subclass the bean?

## Limits

Only construction is opened up; the bean gains no factory, builder, or equality methods, and no other langid class changes visibility. If maintainers want a narrower or wider API surface for custom identifiers, that can be done in this PR. Test coverage is compile-level plus one direct construction check, by design: the defect is a compile-time barrier, so there is no runtime behavior to exercise beyond the constructor and getters.

Changelog: `changelog/unreleased/SOLR-4841-detectedlanguage-public-constructor.yml`

### AI assistance

AI agents assisted with research, implementation, review, and drafting. Nick Shanin directed the work and takes responsibility for this contribution.

## Choices and Limits owed at PR opening

- Choices: public versus protected constructor, as drafted in the description, with the protected variant's compile failure as the evidence. No other design call: the ticket asks for exactly this widening and nothing else in the class changes.
- Limits: as drafted (construction only, no factory or builder, no other visibility changes, compile-level coverage by design).

## Verdict

PR-ready.
