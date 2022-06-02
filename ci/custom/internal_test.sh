#!/usr/bin/env bash

set -x -o errexit -o nounset -o pipefail

echo
echo test step is accomplished by maven during build step
echo in the future, we may split that in build, test, package, deliver
echo however in java is faster to do all in once...

true
