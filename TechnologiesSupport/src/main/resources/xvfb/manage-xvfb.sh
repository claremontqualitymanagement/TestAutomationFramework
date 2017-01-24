#!/bin/bash
#Note that you'll need to have NOPASSWD set in your sudoers file.

XVFB_CMD="sudo /usr/bin/Xvfb :15 -ac -screen 0 1024x768x8"

function stop_xvfb {
  XVFB_PID=`ps ax | pgrep "Xvfb"`
  if [ "${XVFB_PID}" != "" ]; then
    sudo kill ${XVFB_PID}
  fi
}

if [ "${1}" == "start" ]; then
  stop_xvfb
  ${XVFB_CMD} &
elif [ "${1}" == "stop" ]; then
  stop_xvfb
fi