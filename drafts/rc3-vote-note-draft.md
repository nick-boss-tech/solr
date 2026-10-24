Subject: Re: [VOTE] Release Solr 10.1.0 RC3

Hi all,

I checked two release-note items against the RC3 tip (749712b719). I did not build or run the release, so this comes from the files only.

1. Dependency list in CHANGELOG.md, lines 291 to 293. google-api-services-storage-v1 appears twice: as added at rev20260524 (line 291) and as removed at rev20240621 (line 293). It is one upgrade. The cause is in dev-tools/scripts/dependencyChanges.py: parse_jar keeps the "rev" token in the artifact name, so each revision is treated as a different artifact. The langchain4j entries are not duplicates. The five new jars on line 291 (anthropic, google-ai-gemini, http-client, http-client-jdk, ollama) are different from the five upgraded on line 292. Suggested fix: one upgrade line for the storage library, and parse_jar should drop the revision token before comparing.

2. cuVS wording. major-changes-in-solr-10.adoc line 88 and dense-vector-search.adoc line 1038 say the cuVS problem "is expected to be fixed in a subsequent Solr 10.x release". SOLR-18556 is open ("To Do"), affects 10.1, and has no fix version or target. Suggested wording: "no fix is scheduled yet; see SOLR-18556".

Both items are changelog and documentation issues. In the files I checked I found no problem with the release artifacts. Whether either should change before the release is for the release manager to decide. This note is not a vote.

Nick
