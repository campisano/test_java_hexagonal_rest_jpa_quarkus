#!/bin/bash
#

show_usage()
{
    echo "Usage:    "`basename $0`" <protocol://host:port> <isbn>"
    echo "Example:  "`basename $0`" http://127.0.0.1:8080 0123"
}

if test -z "$1" -o -z "$2"
then
    show_usage >&2
    exit 1
fi

BASE_URL="$1"
ISBN="$2"

URL="${BASE_URL}/v1/books/${ISBN}"

set -x
curl -q -sSw "\n\nHTTP code: %{http_code}\n" \
-k --ciphers 'DEFAULT:!DH' \
-X GET \
"${URL}" \
-H "Cache-Control: no-cache" \
-H "accept: application/json"



# End
