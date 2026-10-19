We checked Solr main at commit 498a09aea9 (October 7, 2026). The solr.log.level setting is still applied by changing the level of the root logger in code at startup, which is the behavior this report describes. Loggers configured separately in log4j2.xml keep their own levels, and logging that happens before that point is not affected.

What we checked:

- The startup code that reads solr.log.level and changes the root logger level.
- The other place that uses the same method (a command-line tool), which means the change is not limited to the server startup.

How we tried to reproduce it:

- We did not run a reproduction for this check.

What this means:

- The behavior is as described. Moving the level into log4j2.xml, as proposed, would change how several parts of Solr start up. That is a design decision, and it is not settled yet, so we are leaving the ticket open.

If you still see this:

- Please send the log4j2.xml you use, the value of solr.log.level, and an example of the logger output that does not match the level. That will show which logger is affected.
