#!/usr/bin/env bash

mvn --settings=./.travis/settings.xml jar:jar source:jar-no-fork gpg:sign deploy:deploy