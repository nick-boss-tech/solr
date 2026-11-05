# Evidence safety scan

Strict scan of 90 local text evidence files (`.log`, `.xml`, `.diff`) found zero actual `Authorization:` or `Proxy-Authorization:` header lines, zero bearer-like tokens, and zero `password`/`passwd`/`secret`/`token` assignments. The earlier broad regex matches were the Solr startup message `authentication=disabled authorization=disabled`, not HTTP headers. All `-D` matches are the non-secret JUnit `-Dtests.*` reproduction settings (`seed`, `locale`, `timezone`, assertions, and file encoding). The selected evidence is copied byte-for-byte without redaction.
