# SOLR-9852 - hypothetical reproduction (nothing was compiled or run)

JIRA: SQuirreL SQL and ODBC bridges cannot read column metadata from the Solr JDBC driver; the ticket's stack trace is
`DatabaseMetaDataImpl.getTypeInfo` throwing `UnsupportedOperationException`. Kevin Risden: most metadata is unpopulated, patch
welcome; Uwe Schindler: UOE is wrong, it must be an `SQLException`. A later commenter named `getColumns` as the missing piece.
Earlier audit note: "JDBC metadata feature". On `upstream/main` `getColumns` returns `null` and `getTypeInfo` throws UOE.

## Change
- `getColumns` runs `select ... from metadata.COLUMNS where tableSchem like ? and tableName like ? and columnName like ?`
  through the connection statement, the same way `getTables` uses `metadata.TABLES`. Null patterns mean `%`; single quotes are
  doubled (`quotePattern`). The catalog argument is ignored (as the driver's catalog is the ZK host, `tableCat` is null).
- `getTypeInfo` throws `SQLFeatureNotSupportedException` (Uwe's point), not UOE.

## Test
`JdbcTest.testJDBCMethods` (run for both aggregation modes) now calls `getColumns(null, zkHost, "collection1", "%")` and checks
`id`, `a_i`, `a_s` appear, then a `a_s` filter returns one row, and `getTypeInfo` throws the SQL exception type.

## Guesses to verify first
- Calcite's `metadata.COLUMNS` exposes camelCase columns (`columnName`, `dataType`, `typeName`, `columnSize`, `bufferLength`,
  `decimalDigits`, `numPrecRadix`, `nullable`, `remarks`, `columnDef`, `sqlDataType`, `sqlDatetimeSub`, `charOctetLength`,
  `ordinalPosition`, `isNullable`) the way `metadata.TABLES` exposes `tableSchem`. If any name is wrong the query fails: trim it.
- Solr's Calcite schema populates `COLUMNS` for a collection (field list from the schema/Luke), including dynamic `*_s`
  fields that hold data; the test only needs `id`, `a_i`, `a_s` and may need `a_i` removed if dynamic fields are absent.
- `tableSchem` for columns equals the ZK host, as for `getTables`.
- Other methods that return `null` (`getTableTypes`, `getPrimaryKeys`, ...) are untouched.

## Fail-before
Expected: `getColumns` returns null, so `assertNotNull` fails; `getTypeInfo` throws UOE, so `assertThrows` fails.
