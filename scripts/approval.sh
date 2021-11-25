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

MESSAGE="Deployment of ${CIRCLE_PROJECT_REPONAME} requires approval, <@U63QFMF16> <@U61ET53U5> <@U1ZUALP0U>! \nApprove Here: https://circleci.com/workflow-run/$CIRCLE_WORKFLOW_ID\nProject: ${CIRCLE_PROJECT_REPONAME}\nBranch: ${CIRCLE_BRANCH}\nDeveloper: $CIRCLE_USERNAME\nCommit: https://github.com/pouchnation/${CIRCLE_PROJECT_REPONAME}/commit/${CIRCLE_SHA1}"

/usr/bin/slack '#system_v3' $MESSAGE