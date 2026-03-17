#!/bin/sh
# Gradle wrapper bootstrap - downloads and runs Gradle
APP_NAME="Gradle"
CLASSPATH=$APP_HOME/gradle/wrapper/gradle-wrapper.jar
JAVACMD="java"

# Determine the Java command to use
if [ -n "$JAVA_HOME" ] ; then
    JAVACMD="$JAVA_HOME/bin/java"
fi

# Try to use the wrapper jar if it exists, otherwise download Gradle directly
GRADLE_VERSION="8.5"
GRADLE_DIST_URL="https://services.gradle.org/distributions/gradle-${GRADLE_VERSION}-bin.zip"

if [ ! -f "$HOME/.gradle/wrapper/dists/gradle-${GRADLE_VERSION}-bin/gradle-${GRADLE_VERSION}/bin/gradle" ]; then
    GRADLE_USER_HOME="${GRADLE_USER_HOME:-$HOME/.gradle}"
    GRADLE_DIST_DIR="$GRADLE_USER_HOME/wrapper/dists/gradle-${GRADLE_VERSION}-bin"
    mkdir -p "$GRADLE_DIST_DIR"
    
    if [ ! -f "$GRADLE_DIST_DIR/gradle-${GRADLE_VERSION}-bin.zip" ]; then
        echo "Downloading Gradle ${GRADLE_VERSION}..."
        curl -fsSL "$GRADLE_DIST_URL" -o "$GRADLE_DIST_DIR/gradle-${GRADLE_VERSION}-bin.zip"
    fi
    
    if [ ! -d "$GRADLE_DIST_DIR/gradle-${GRADLE_VERSION}" ]; then
        echo "Extracting Gradle..."
        cd "$GRADLE_DIST_DIR" && unzip -q "gradle-${GRADLE_VERSION}-bin.zip"
        cd - > /dev/null
    fi
fi

exec "$HOME/.gradle/wrapper/dists/gradle-${GRADLE_VERSION}-bin/gradle-${GRADLE_VERSION}/bin/gradle" "$@"
