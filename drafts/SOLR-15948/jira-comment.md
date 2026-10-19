We checked this against Solr main at commit 498a09aea97 and could not reproduce it yet.

What we checked:

- The stop script (bin/solr, function stop_solr) sends a stop command to the Jetty stop port, waits for the Solr process to exit for up to the stop wait (180 seconds by default), and then kills it. A stop after a startup error that never opened the stop port will therefore wait the full stop wait and then kill the process.

How we tried to reproduce it:

- Not attempted yet. Reproducing it needs the packaged server with a startup error, such as a low memory limit.

What this means:

- The timeout is the stop wait followed by a forced kill. The open question is why the server process stays alive after a startup error, which we have not yet checked.

If you still see this:

- The Solr version, the startup error from the log, and the time from the stop command to the forced kill would help us confirm the cause. Please reopen with that information.
