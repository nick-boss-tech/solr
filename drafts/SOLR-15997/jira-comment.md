We checked this against Solr main at commit 498a09aea97 and could not reproduce it yet.

What we checked:

- The code path for setting perReplicaState with a MODIFY collection request (CollectionMutator in the Overseer) reads the collection state and then writes it back with a version check. The version check is where the BadVersionException comes from.

How we tried to reproduce it:

- Not attempted. The report describes a multi-node cluster with a few hundred collections and a restart, and it does not include a log or a stack trace.

What this means:

- A BadVersionException here means the collection state changed between the read and the write. We have not established which other update causes that change.

If you still see this:

- The Overseer log around the time of the exception, with the stack trace, and the Solr version would let us narrow it down. Please reopen with that information.
