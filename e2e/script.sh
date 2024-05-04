#!/bin/bash
version="0.0.1-SNAPSHOT"
response=""
while [ $version != "$response" ]
  do
    echo "Product service is not responding!"
    sleep 1s
    response=$(curl -k https://localhost/api/products/info | jq -r ".appVersion")
done
  echo "Successfully!"