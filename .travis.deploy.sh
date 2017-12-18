#!/bin/bash

PORT=5196
PERF_PORT=5202

# deploy to bakery-flow-spring.app.fi (internal demo)
scp -o StrictHostKeyChecking=no -P $PORT target/bakery-app-starter-flow-spring-*.war dev@app.fi:bakery.war
ssh -o StrictHostKeyChecking=no -p $PORT dev@app.fi mv bakery.war tomcat/webapps/ROOT.war

# deploy to bakery-flow-spring-perf.app.fi (for performance testing - automatically logs in as admin)
scp -o StrictHostKeyChecking=no -P $PERF_PORT target/bakery-app-starter-flow-spring-*.war dev@app.fi:bakery.war
ssh -o StrictHostKeyChecking=no -p $PERF_PORT dev@app.fi mv bakery.war webapps/ROOT.war

