#/bin/sh

interval=10
target="./target"

while true
do
    if ls $target/*.json > /dev/null 2>&1
    then
        echo "ok"
        sudo php src/index.php $target/*.json
        rm $target/*.json
    else
        echo "No File"
    fi

    sleep $interval
done
