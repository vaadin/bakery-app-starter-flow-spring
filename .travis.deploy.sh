#!/bin/bash

PORT=5196
PERF_PORT=5202

# deploy to bakery-flow-spring-perf.app.fi (for performance testing - automatically logs in as admin)
scp -o StrictHostKeyChecking=no -P $PERF_PORT target/bakery-app-starter-flow-spring-*.war dev@app.fi:bakery.war
ssh -o StrictHostKeyChecking=no -p $PERF_PORT dev@app.fi mv bakery.war webapps/ROOT.war

# deploy to bakery-flow-spring.app.fi (internal demo)
scp -o StrictHostKeyChecking=no -P $PORT target/bakery-app-starter-flow-spring-*.war dev@app.fi:bakery.war
ssh -o StrictHostKeyChecking=no -p $PORT dev@app.fi mv bakery.war tomcat/webapps/ROOT.war

# There needs to be some time between the .war file is copied to the performance test server and a performance
# test is requested (to make sure the application server has the time to deploy the new archive).
# That's why there is another SCP task (copying a .war file to the staging server) in between.
if [ "$TRAVIS_PULL_REQUEST" = "false" ]; then
    SPEEDCURVE_NOTE=master%40$TRAVIS_COMMIT
else
    SPEEDCURVE_NOTE=PR%20$TRAVIS_PULL_REQUEST
fi

curl "https://api.speedcurve.com/v1/deploys" \
        -u $SPEEDCURVE_API_KEY:x \
        --request POST \
        --data site_id=$SPEEDCURVE_SITE_ID \
        --data note=$SPEEDCURVE_NOTE
