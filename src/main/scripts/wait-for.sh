#!/usr/bin/env sh

PORT=3306
MAX_RETRY=90

echo "Testing connection to host $MYSQL_HOST and port $PORT."

count=0
while [ $count -lt $MAX_RETRY ]
do
    count=$((count+1))
    nc -z $MYSQL_HOST $PORT
    result=$?
    if [ $result -eq 0 ]; then
        echo "Connection is available after $count second(s)."
        exit 0
    fi
    echo "Retrying..."
    sleep 1
done

>&2 echo "Timeout occurred after waiting $MAX_RETRY seconds for $MYSQL_HOST:$PORT."
exit -1