# SOLR-7323 - hypothetical reproduction (NOT RUN)

Guessed, never compiled or executed. No Gradle was run.

JIRA: `CREATE ... configSet=basic_configs` fails with "Could not load configuration from directory /var/solr/data/configsets/..." because
configsets live under the install dir (installer script, Docker). Maintainers (Hoydahl, Smiley) agreed copying/design is out of scope;
`configSetBaseDir` in solr.xml already exists. What is left is the error message, which gave no hint why that directory was searched.

Change: `FileSystemConfigSetService.locateInstanceDir` message now includes the configSet name, the base directory and the `configSetBaseDir` hint.
The exception type/status is unchanged (SERVER_ERROR).

Test: `TestFileSystemConfigSetService.testMissingConfigSetErrorNamesBaseDirectory` (Mockito `CoreDescriptor`).

Guesses to verify first:
- Mockito can mock `CoreDescriptor` (non-final) and `locateInstanceDir` (protected, same package) is reachable from the test.
- Other tests asserting the old message text (grep found none in `solr/core/src/test`, but other modules were not searched).
- A 400 would be more accurate than SERVER_ERROR; left alone to avoid a behaviour change.
