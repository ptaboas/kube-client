#!/usr/bin/env bash
if [ "$TRAVIS_BRANCH" = 'master' ]; then
    mvn jar:jar deploy:deploy --settings=./.travis/settings.xml
fi
