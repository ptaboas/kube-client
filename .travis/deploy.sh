#!/usr/bin/env bash
if [ "$TRAVIS_BRANCH" = 'master' ]; then
    mvn deploy:deploy --settings=./.travis/settings.xml
fi
