#! /bin/sh

./gradlew clean domain:profile:test domain:dashboard:test domain:measurements:test testRelease --parallel