# Jira comment draft: SOLR-10928 (status note, gated implementation exists)

Status: DRAFT (not posted). Branch `solr-10928-submit` live at `f2001e640a`; the gate was green at `1ff57c1b21` with two factory commits on top (coverage hardening, a gate repair). The ticket is unassigned and untouched since 2019. Posting needs Nick Shanin's explicit go.

---

I implemented the {{elevate.q}} support from this ticket on a branch, [solr-10928-submit|https://github.com/nick-boss-tech/solr/tree/solr-10928-submit]. The change is small: a parameter in {{QueryElevationParams}}, the handling in {{QueryElevationComponent}}, and tests ({{QueryElevationComponentTest}}, {{QueryElevationComponentParamTest}}, {{DistributedQueryElevationComponentTest}}), plus a short Ref Guide note. The branch has been through a full local test gate.

Should I open this as a PR, or is the elevation parameter set settled in a way this misses?

🤖 _AI text above_ 🤖 _(reviewed by Nick Shanin)_
