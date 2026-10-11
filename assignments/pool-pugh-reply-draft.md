# Pool assignment: draft the reply to Eric Pugh's question on the SOLR-5065 pull request

Capability tags: `draft`. Staffing: 1. Claim path: claims/pool-pugh-reply-draft.md.

Background: on the SOLR-5065 pull request (apache/solr, branch solr-5065-submit at head ab894a996c8aa9825c4ac5af38536aec1ee865eb), Eric Pugh left a review comment on ParseNumericFieldUpdateProcessorFactory.java line 62. His question, verbatim: "is there a math library we could be using to handle all variotions of string that are numbers? I guess I am wondering if this PR fixes tehse two specific examples, does that me we fixed em all? Or are there many other variations that people will stumble across that need mroe regexing? Seems like taking a string that represents a number and making it a true number isn't a werid thing to do in java? Maybe we get lucky and already depend on a lib that does this?" The owner wants a fresh draft of the reply from this agent. Nothing is posted under this assignment; the main side posts after the owner approves the text.

Verified facts (checked by the main side against the branch code at the head and by a parser matrix run on JDK 21 on 2026-10-10; re-verify any fact you rely on):
- The four Parse factories (ParseDouble, ParseFloat, ParseInt, ParseLong FieldUpdateProcessorFactory) parse with a ThreadLocal NumberFormat in a configured locale (Locale.ROOT by default). Grouping separators are parsed: the class javadoc gives a French locale example, "12 345,899" parsed as 12345.899.
- normalizeExponent (in the base factory) makes exactly two rewrites, both anchored to a trailing exponent: a lowercase e right after a digit becomes E, and a plus sign in the exponent is removed. Only the Double and Float factories call it.
- NumberFormat rejects both forms (a plus in the exponent, a lowercase marker). Java, JSON and the Solr field types accept both.
- Measured on JDK 21: Double.parseDouble, BigDecimal, and NumberUtils in commons-lang3 (Solr's version catalog pins commons-lang3 3.20.0 at this head) all reject grouping separators, and none of them supports locales.
- Exponent completeness: after the two rewrites, the accepted exponent spellings are E or e with a plus, a minus, or no sign, with or without grouping, which is the full set that Java, JSON and BigDecimal accept. Spellings Double.parseDouble accepts beyond that (hex floats, a trailing d) stay rejected; they were rejected before this change and are out of scope.

Style requirements (the owner's direction for this reply):
- Open with one bold summary sentence that carries the whole answer, heading style, the way PR descriptions open each section.
- Medium length over all: aim for roughly 120 to 200 words after the summary sentence. Not a long message; no section headings needed beyond the bold opener.
- Plain language, short sentences. No em dashes. No internal process vocabulary. Write as the author ("I"), never "we" for the author plus anyone else.
- The AI header goes at the top: the robot emoji, *AI text below*, *(posted on behalf of Nick Shanin)*.
- Answer both halves of the question: whether a library already does this, and whether the two fixed forms are the whole set or the first of many.

Deliverable: material/SOLR-5065-pugh-reply-draft.md holding only the reply text, ready to post. Rules: WORKFLOW.md binds (claim before work, mark the claim DONE in the same push as the deliverable). No builds or test runs.
