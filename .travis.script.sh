#!/usr/bin/env bash

# TRAVIS_PULL_REQUEST == "false" for a normal branch commit, the PR number for a PR
# TRAVIS_BRANCH == target of normal commit or target of PR
# TRAVIS_SECURE_ENV_VARS == true if encrypted variables, e.g. SONAR_HOST is available
# TRAVIS_REPO_SLUG == the repository, e.g. vaadin/vaadin

TESTBENCH=-Dvaadin.proKey=$VAADIN_PRO_KEY

mvn -B -e -V -Pit clean verify -DrunLint -Dvaadin.productionMode $TESTBENCH
