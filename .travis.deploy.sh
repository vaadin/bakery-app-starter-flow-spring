#!/bin/bash

PORT=5196
PERF_PORT=5202
scp -o StrictHostKeyChecking=no -P $PORT target/bakery-app-starter-flow-spring-*.war dev@app.fi:bakery.war
ssh -o StrictHostKeyChecking=no -p $PORT dev@app.fi mv bakery.war tomcat/webapps/ROOT.war
scp -o StrictHostKeyChecking=no -P $PERF_PORT target/bakery-app-starter-flow-spring-*.war dev@app.fi:bakery.war
ssh -o StrictHostKeyChecking=no -p $PERF_PORT dev@app.fi mv bakery.war webapps/ROOT.war

