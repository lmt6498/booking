#!/bin/bash

# file used: https://gist.github.com/andkirby/67a774513215d7ba06384186dd441d9e

# installation in linux:

# put this if you want to post in #devops:
# echo "APP_SLACK_WEBHOOK=https://hooks.slack.com/services/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" >> ~/.slackrc
# put this if you want to post in #system_v3:
# echo "APP_SLACK_WEBHOOK=https://hooks.slack.com/services/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" >> ~/.slackrc

# echo "APP_SLACK_CHANNEL=devops-alerts" >> ~/.slackrc

# usage:
# sh notify.sh <channel and usernames> <message>

CIRCLE_WORKFLOW_ID=$1
CIRCLE_USERNAME=$2
CIRCLE_REPOSITORY_URL=$3
CIRCLE_BRANCH=$4
CIRCLE_PROJECT_REPONAME=$5
CIRCLE_SHA1=$6
CIRCLE_BUILD_NUMBER=$6

MESSAGE="Starting deployment of ms-booking.\nWorkflow: https://circleci.com/workflow-run/$CIRCLE_WORKFLOW_ID\nProject: ${CIRCLE_PROJECT_REPONAME}\nBranch: ${CIRCLE_BRANCH}\nDeveloper: $CIRCLE_USERNAME\nBuild Number: $CIRCLE_BUILD_NUMBER\nCommit: https://github.com/pouchnation/${CIRCLE_PROJECT_REPONAME}/commit/${CIRCLE_SHA1}"

slack '#system_v3' $MESSAGE
