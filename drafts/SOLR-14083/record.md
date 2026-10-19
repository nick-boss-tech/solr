# Skip record: SOLR-14083

- Ticket: SOLR-14083, Solr GUI access through Internet Explorer. JIRA read 2026-10-19 (this pass, evidence tool): status Open, resolution None, created 2019-12-13, updated 2020-01-21, no fix versions, no links; two comments (2019-12-14 janhoy: IE is not a target he supports; 2019-12-15 reporter: IE 11).
- Verdict: keep-open.
- Draft branch: solr-14083-draft-open (the existing keep-open branch; this record replaces its draft files).
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942. Grep and code reads run at this head (detached worktree).
- Source: remediation redo of batch 05, item 6, under assignments/pool-aaa-skip-verdict-remediation-05.md. The earlier record (vm2, from the B3 reading) cited a grep and lines; those are re-run here.

## Ticket reading

With BasicAuth on, IE 11 shows a limited Admin UI with no password prompt, while Firefox, Chrome and Edge show the prompt. The reporter says the same setup worked on Solr 7.4.

## Grep and code reading at the pinned head

- `git grep -n -i "internet explorer" 498a09aea97aac86c2cc7ac114113b946d249942 -- solr/solr-ref-guide | wc -l` gives 0. The reference guide does not mention IE.
- solr/webapp/web/js/angular/app.js:18: `/* SOLR-14120: Providing a manual definition for the methods 'includes' and 'startsWith' to support Internet Explorer 11. */`. The IE11 fix for the Admin UI is on the pin.
- The SOLR-14120 commit is 3ab59aa1c9 (2019-12-24, "SOLR-14120: Solr Admin UI breaks when using IE11"); `git merge-base --is-ancestor 3ab59aa1c9 498a09aea97aac86c2cc7ac114113b946d249942` succeeds.
- solr/solr-ref-guide/modules/deployment-guide/pages/basic-authentication-plugin.adoc:150-164, "Special Case for Basic Scheme": the Basic scheme in WWW-Authenticate triggers the browser prompt for non-AJAX requests, and the plugin maps it to xBasic for AJAX requests. The section does not name IE.

## History

- SOLR-14120 (3ab59aa1c9) is the partial IE11 fix (a polyfill for two methods). The ticket's symptom (no prompt, limited UI) was not checked against that fix.

## Reproduction attempts

Not run. A run needs IE 11 (or an IE 11 emulation) against a BasicAuth-protected Admin UI at the pinned head; this pass did not run a browser test.

## Gap analysis

The IE11 Admin UI has a partial fix on main, and the docs describe the prompt behavior without IE. Whether the current UI works on IE 11 with auth is not shown. Whether IE is a supported browser at all is a maintainer decision; the first comment says it is not a target.

## Verdict justification

keep-open: the attempts made are the greps and code reads above at the pinned head, the Jira read, and the commit ancestry check. The missing piece is (1) a browser run on IE 11 (or an emulation) against an auth-protected Admin UI on the pin, with the result; and (2) the maintainer decision on IE support. close-fixed is not supported: the polyfill is partial and the symptom was not shown fixed. close-answered is not supported: the docs do not answer the IE question.

Missing for a closing verdict: the browser run and the support decision.

## Limits

This record does not reproduce the IE symptom. It does not post anything to JIRA or GitHub. The draft comment is a draft until someone posts it by hand.
