[~dsmiley], thank you for the note on the dev list. I have read it. I will not do further work on SOLR-18470 unless a reviewer says yes.

Two clarifications, so my earlier comment is not read as more than it is:
 * [PR #5202|https://github.com/apache/solr/pull/5202] does the narrowing step only. The URL path decides the collection, and the {{collection}} parameter can only narrow the set the path names. The PR does not remove or deprecate the parameter. Whether it goes away for Solr 11 is the project's decision, and the PR does not settle it.
 * The plan in my earlier comment is a design for path scope. I offered it as questions, not as work to start now.

If there is a shape of plan you would want to hear about before any Solr 11 work on this is real, I would welcome a pointer. If not, I will leave the ticket as it is.

🤖 _AI text above_ 🤖 _(reviewed by Nick Shanin)_
