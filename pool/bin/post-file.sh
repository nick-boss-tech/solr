#!/bin/bash
# post-file.sh: post ONE file to a branch of nick-boss-tech/solr through the
# GitHub contents API, as a server-side commit on the branch's current tip.
#
# Use this for single-file records (claims, reports, receipts, assignments,
# status edits) instead of a local clone push. There is no local tree to fall
# behind and nothing to rebase: the commit lands on whatever the tip is when
# the call is made. Multi-file atomic changes still go through git push.
#
# Usage:
#   post-file.sh <branch-path> <local-file> <commit-message> [--branch <name>] [--base-sha <blob-sha>]
#
# --branch defaults to coordination.
# --base-sha is the git blob SHA of the branch file the caller's edit is based
# on. If the file on the branch no longer matches it, the post is refused
# (exit 5) instead of overwriting someone else's newer edit: re-read the
# file, re-apply the edit, and post again.
#
# Exit codes: 0 posted; 4 the API refused the write (conflict or validation);
# 5 stale --base-sha; 6 usage or credential problem.
# The credential is read from line 2 of ~/.git-credentials and never printed.
set -euo pipefail

PATH_ON_BRANCH=""
LOCAL_FILE=""
MESSAGE=""
BRANCH="coordination"
BASE_SHA=""

while [ $# -gt 0 ]; do
  case "$1" in
    --branch) BRANCH="$2"; shift 2 ;;
    --base-sha) BASE_SHA="$2"; shift 2 ;;
    *)
      if [ -z "$PATH_ON_BRANCH" ]; then PATH_ON_BRANCH="$1";
      elif [ -z "$LOCAL_FILE" ]; then LOCAL_FILE="$1";
      elif [ -z "$MESSAGE" ]; then MESSAGE="$1";
      else echo "post-file: unexpected argument: $1" >&2; exit 6; fi
      shift ;;
  esac
done

if [ -z "$PATH_ON_BRANCH" ] || [ -z "$LOCAL_FILE" ] || [ -z "$MESSAGE" ]; then
  echo "usage: post-file.sh <branch-path> <local-file> <commit-message> [--branch <name>] [--base-sha <blob-sha>]" >&2
  exit 6
fi
[ -f "$LOCAL_FILE" ] || { echo "post-file: no such local file: $LOCAL_FILE" >&2; exit 6; }

TOKEN=$(sed -n '2p' ~/.git-credentials | sed 's|.*://[^:]*:\([^@]*\)@.*|\1|')
[ -n "$TOKEN" ] || { echo "post-file: could not read the credential" >&2; exit 6; }

API="https://api.github.com/repos/nick-boss-tech/solr/contents/$PATH_ON_BRANCH"

# Current blob SHA of the file on the branch (empty when the file is absent).
CURRENT_SHA=$(curl -s -H "Authorization: Bearer $TOKEN" "$API?ref=$BRANCH" \
  | python3 -c 'import json,sys
try:
    d = json.load(sys.stdin)
    print(d.get("sha", ""))
except Exception:
    print("")')

if [ -n "$BASE_SHA" ] && [ "$BASE_SHA" != "$CURRENT_SHA" ]; then
  echo "post-file: stale base: $PATH_ON_BRANCH is at blob ${CURRENT_SHA:-<absent>}, edit was based on $BASE_SHA" >&2
  echo "post-file: re-read the file, re-apply the edit, and post again" >&2
  exit 5
fi

PAYLOAD=$(python3 - "$LOCAL_FILE" "$MESSAGE" "$BRANCH" "$CURRENT_SHA" <<'PYEOF'
import base64, json, sys
local_file, message, branch, sha = sys.argv[1:5]
payload = {
    "message": message,
    "content": base64.b64encode(open(local_file, "rb").read()).decode(),
    "branch": branch,
}
if sha:
    payload["sha"] = sha
print(json.dumps(payload))
PYEOF
)

RESPONSE=$(curl -s -w '\n%{http_code}' -X PUT -H "Authorization: Bearer $TOKEN" \
  -H 'Content-Type: application/json' -d "$PAYLOAD" "$API")
STATUS=$(printf '%s' "$RESPONSE" | tail -n 1)
BODY=$(printf '%s' "$RESPONSE" | sed '$d')

case "$STATUS" in
  200|201)
    printf '%s' "$BODY" | python3 -c 'import json,sys
d = json.load(sys.stdin)
print("posted:", d["content"]["path"], "at commit", d["commit"]["sha"])' ;;
  *)
    echo "post-file: the API refused the write (HTTP $STATUS) for $PATH_ON_BRANCH" >&2
    printf '%s' "$BODY" | python3 -c 'import json,sys
try:
    print("post-file:", json.load(sys.stdin).get("message", ""), file=sys.stderr)
except Exception:
    pass' >&2 || true
    echo "post-file: if the file changed under you, re-read it, re-apply the edit, and post again" >&2
    exit 4 ;;
esac
