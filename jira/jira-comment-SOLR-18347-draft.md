DRAFT, NOT POSTED. Owner note (not part of the comment): Nick has not reviewed this text. Do not post until he does. The footer's "reviewed by Nick Shanin" wording is proposed until that review. Addressee: Jan Høydahl (Jira user janhoy), reporter. Evidence: reports/solr-18347-adminui-bugs-check-round-1.md.

----

[~janhoy] I checked the five open items against apache/solr main at {{13c693d805f}}, from the code only. Two of them hold, and three do not hold as written.

 * Item 2 holds in code. The {{CollectionsV2}} factory in services.js:91-96 reads {{solrApi}} when it is injected, so a missing bundle fails there. I did not run a browser to see the blank screen.
 * Item 3's error path exists. ManagedIndexSchema.java:213-217 throws the version-mismatch error with ", retry". It is a 409 CONFLICT, not a 5xx. I did not trace whether the designer's own calls hit it.
 * Item 4 does not hold on main. {{ping?action=status}} returns 200 with {{not_configured}} (PingRequestHandler.java:221-222), and core-overview.js:57-72 already handles that state. The 503 comes from enable and disable only.
 * Item 5 does not hold for Collections. collections.js:291-309 never resets {{reloadSuccess}}. The one-second reset is on the Cores screen, cores.js:184-198.
 * Item 1: I could not reproduce the TypeError from {{showCore}}, because app.js:666 already guards a null core. The null read may come from elsewhere.

Items 1 to 3 need a browser run to settle. Should I draft the run as a follow-up, or would you rather close items 4 and 5 now?

🤖 _AI text above_ 🤖 _(reviewed by Nick Shanin)_
