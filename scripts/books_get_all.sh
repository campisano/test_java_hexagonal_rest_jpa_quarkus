#!/bin/bash
#

show_usage()
{
    echo "Usage:    "`basename $0`" <protocol://host:port>"
    echo "Example:  "`basename $0`" http://127.0.0.1:8080"
}

if test -z "$1"
then
    show_usage >&2
    exit 1
fi

BASE_URL="$1"

URL="${BASE_URL}/v1/books"

set -x
curl -q -sSw "\n\nHTTP code: %{http_code}\n" \
-k --ciphers 'DEFAULT:!DH' \
-X GET \
"${URL}" \
-H "Cache-Control: no-cache" \
-H "accept: application/json"



# End
