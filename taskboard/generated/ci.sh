#!/usr/bin/env bash
# Engine-authored (ADR-010 PR-6.W1): the project's own gate, run from any directory.
# Backend `mvn -o verify`, frontend `npm ci`, `tsc --strict`, `ng build`, then `docker build`.
# Any failing step ends the script with a non-zero status. The Maven repository must hold
# the pinned dependencies (the build is offline).
set -euo pipefail
cd "$(dirname "${BASH_SOURCE[0]}")"
image="${IMAGE_NAME:-taskboard:ci}"

echo '== backend: mvn -o verify'
mvn -o -B verify

echo '== frontend: install'
(cd frontend && npm ci)
echo '== frontend: tsc --strict'
(cd frontend && npx --no-install tsc --noEmit --strict -p tsconfig.app.json)
echo '== frontend: ng build'
(cd frontend && npx --no-install ng build)

echo '== docker build'
docker build -t "$image" .
echo "ci.sh: all steps passed ($image)"
