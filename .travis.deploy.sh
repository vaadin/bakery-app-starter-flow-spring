#!/bin/bash

PORT=5196
PERF_PORT=5202

# deploy to bakery-flow-spring.app.fi (internal demo)
scp -o StrictHostKeyChecking=no -P $PORT target/bakery-app-starter-flow-spring-*.war dev@app.fi:bakery.war
ssh -o StrictHostKeyChecking=no -p $PORT dev@app.fi mv bakery.war tomcat/webapps/ROOT.war

# deploy to bakery-flow-spring-perf.app.fi (for performance testing - automatically logs in as admin)
scp -o StrictHostKeyChecking=no -P $PERF_PORT target/bakery-app-starter-flow-spring-*.war dev@app.fi:bakery.war
ssh -o StrictHostKeyChecking=no -p $PERF_PORT dev@app.fi mv bakery.war webapps/ROOT.war

# wait until the perf test server redeploys the .war and starts responding
PERF_TEST_URL="https://bakery-flow-spring-perf.app.fi/login"
ATTEMPT_NUM=0
ATTEMPT_NUM_LIMIT=18
TIMEOUT_SEC=10

function sleepsec () {
    # sleep does not work in travis ci
    # https://medium.com/@shamasis/sleeping-is-not-for-everybody-nor-for-every-script-cb5a23883a68
    node -e "setTimeout(() => {}, $1 * 1000)";
}

sleepsec $TIMEOUT_SEC
while [ $ATTEMPT_NUM -lt $ATTEMPT_NUM_LIMIT ]; do
    ATTEMPT_NUM=$(( $ATTEMPT_NUM + 1 ))
    echo "Checking if $PERF_TEST_URL is accessible (attempt #$ATTEMPT_NUM)..."

    if curl --output /dev/null --silent --head --fail $PERF_TEST_URL; then
        echo "  the URL is up"
        break
    fi

    echo "the URL is down. Waiting for $TIMEOUT_SEC seconds until the next attempt..."

    sleepsec $TIMEOUT_SEC
done

# trigger a performance test
curl "https://api.speedcurve.com/v1/deploys" \
        -u $SPEEDCURVE_API_KEY:x \
        --request POST \
        --data site_id=$SPEEDCURVE_SITE_ID \
        --data note=master%40${TRAVIS_COMMIT:0:7}
