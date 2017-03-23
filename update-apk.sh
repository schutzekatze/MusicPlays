#!/bin/bash

cp app/build/outputs/apk/app-debug.apk MusicPlays.apk
cat app/build.gradle | grep versionCode | sed 's/.*versionCode \(.*\)/\1/' >VersionCode
