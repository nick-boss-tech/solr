# Comment draft: SOLR-8331

- Status: DRAFT (not posted). Posting needs the owner's go.
- Shape: stale-check status note
- Addressee: [~uschindler] — the maintainer who requested the JVM details in the ticket.

[~uschindler], the reporter's follow-up identifies Solr 4.10.4 on Tomcat 7.0.55 with Java 1.7.0_67 and supplies JVM arguments, but the ticket has no subsequent reproduction or current supported-version confirmation. I have not reproduced the reported index corruption on current main, and the original environment is obsolete.

Is this still seen on a supported Solr/JDK combination, and if so can the ticket include the exact versions, JVM options, index setup, and a minimal reproducer?

 * Keep it open for a supported-version reproduction with those details.
 * Close as stale if the failure is limited to the reported 4.10.4/Java 7 environment and cannot be reproduced on a supported line.

I recommend not treating the old report as a current defect without a supported reproduction; the ticket's own details identify only the historical runtime.

🤖 _AI text above_ 🤖 _(reviewed by Nick Shanin)_
