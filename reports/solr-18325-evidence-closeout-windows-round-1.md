# SOLR-18325 confirmed attribution and remaining evidence

The three-runtime control settles the observed builder failure: it predates Jackson. It does not settle released binary compatibility. Keep the overall verdict INCOMPLETE and preserve the binary requirement while completing the remaining Jackson checks and designing a separate compatibility prerequisite.

Read at coordination snapshot `c7c5777b87fed3f1ed8578da8090ebb643ab4509`: `receipts/SOLR-18325-binary-attribution-round-1.md` and its Linux report, delivered in the announced `768e3e1b60f` board update. The remote candidate remains `8bfca8edd30acf11bdf9d6a733312e97f99ec78a`.

## What is established

The receipt records the same Java17 consumer class SHA-256 `027d21fdb1d79c133aab4e4bb4f109f9d17baa05b822c7457e1c9add44bdf18a`, unchanged across all three runtimes. Released10.0.0 passes real HTTP operations; the pre-Jackson base and candidate fail before HTTP with identical timeout method descriptors and stacks. The server runs Java21; Java17 is the consumer runtime. The 40-reference fixture inventory resolves 40/40 on released, 38/40 on both base and candidate, with the two timeout references missing on both. It checks names/descriptors, not every access rule or all consumers.

This confirms inheritance of that observed failure from SOLR-17968. It does not prove that Jackson adds no other binary or behavioral change. The internal mapper field incompatibility remains the separately documented expected negative control.

The missing `HttpSolrClientBase` is an additional superclass/assignability concern for a general released-binary repair. It is not a third observed failure of this fixture: the receipt says that the fixture does not reference that base class directly. Any repair must cover the old hierarchy, builders, erased return descriptors, constructors, public/protected members and relevant subclass extension behavior, rather than infer completeness from the two failing calls.

## Recommended sequence

Finish actual Maven resolution, discriminating mutation checks and full artifact API comparisons on the unchanged candidate. These are executable Linux work. A missing `mvn` command is a setup gap: use an existing capable host or install a verified official Maven distribution into a user-owned tools directory without changing the project or machine-wide settings. It is not a product incompatibility or a reason to equate a Gradle resolver run with Maven proof.

Use the full API comparison to recommend the compatibility prerequisite. Prefer an additive compatibility design preserving the current implementation if it faithfully restores old descriptors, hierarchy, protected state and extension behavior. If the inventory shows that thin bridges cannot preserve those contracts, recommend restoring the relevant original hierarchy with a concrete file/member list. Do not select a nominally smaller shim just because it has fewer lines, and do not roll back the entire HTTP refactor without that analysis.

This is a design recommendation and evidence request, not permission to change unrelated production APIs in the Jackson branch. A separate repair should have its own branch, scope, tests and proof. The new Linux job requests a definite recommended design and cost, rather than returning the same unresolved restore-or-shim question without further analysis.

## Remaining conclusions

After those checks, report independently: (1) what the Jackson delta preserves or changes relative to the exact pre-Jackson base; (2) the inherited release-to-base ABI failures; and (3) whether a released10.0.0 binary can actually run on the candidate. Success on the first does not change failure on the third. Preserve the distinction between a technically promising client migration and a release-ready compatible Solr10 artifact.

The four Jackson2 core test oracles stay as confirmed. The inherited unload failure remains SOLR-18587 and is not merged into this experiment. Full server migration, the preferred3.1LTS target and release policy remain separate.

Follow-up job: `gates/solr-18325-evidence-closeout-round-1.md`. No new runtime control or broad check repetition is needed merely to reconfirm an unchanged result. No Solr source change, Gradle run, public comment, requirement waiver, schedule change or restoration/shim was made in this Windows follow-up.
