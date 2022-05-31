#!/bin/bash
#

show_usage()
{
    echo "Usage:    "`basename $0`" <protocol://host:port> <isbn> <title> <authors> <description>"
    echo "Example:  "`basename $0`" http://127.0.0.1:8080 0123 'Foo Bar' '\"Kim\",\"Bob\"' 'Book of Foo and Bar'"
}

if test -z "$1" -o -z "$2" -o -z "$3" -o -z "$4" -o -z "$5"
then
    show_usage >&2
    exit 1
fi

BASE_URL="$1"
ISBN="$2"
TITLE="$3"
AUTHORS="$4"
DESCRIPTION="$5"

URL="${BASE_URL}/v1/books"

set -x
curl -q -sSw "\n\nHTTP code: %{http_code}\n" \
-k --ciphers 'DEFAULT:!DH' \
-X POST \
"${URL}" \
-H "Cache-Control: no-cache" \
-H "Content-Type: application/json" \
-d '{ "isbn": "'"${ISBN}"'", "title": "'"${TITLE}"'", "authors": ['"${AUTHORS}"'], "description": "'"${DESCRIPTION}"'"}'



# End
