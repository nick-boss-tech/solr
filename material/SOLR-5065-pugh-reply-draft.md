🤖 *AI text below* 🤖 *(posted on behalf of Nick Shanin)*

**No library I checked does this, and the two exponent fixes cover exponents only, so other number forms still fail.**

On JDK 21, I checked three parsers: Double.parseDouble, BigDecimal, and NumberUtils from commons-lang3 3.20.0. Solr core already depends on commons-lang3. None of the three accepts grouping separators or takes a locale. The locale-aware parser these factories use is NumberFormat. It handles grouping. But it rejects a plus sign in the exponent and a lowercase exponent marker. So none of those libraries would replace the rewrite. The JDK has no single call that covers locale grouping and these exponent forms.

This PR changes only the Double and Float factories. Int and Long do not call the rewrite. The rewrite also touches only a trailing exponent. A trailing space stops it. A dot right before the exponent also stops it, as in 5.e3. Hex floats and a trailing d suffix stay rejected, as they were before. So this PR does not cover every form.
