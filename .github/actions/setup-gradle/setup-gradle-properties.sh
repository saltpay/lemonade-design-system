#! /usr/bin/env bash

set -e

readonly workdir="$GITHUB_WORKSPACE"
readonly properties_file="$workdir/kmp/gradle.properties"
readonly gha_jdk_path="$1"

write_property() {
    echo "$1" >>"$properties_file"
}

write_common_properties() {
    echo "🔥 Writting common Gradle properties regarding the GHA runner"

    # Gradle properties common to all build environments
    write_property "org.gradle.parallel=true"
    write_property "org.gradle.configureondemand=false"
    write_property "org.gradle.caching=true"
    write_property "org.gradle.daemon=false"
    write_property "org.gradle.logging.stacktrace=all"

    # Compose common properties
    write_property "org.jetbrains.compose.experimental.macos.enabled=true"

    # Kotlin properties common to all build environments
    write_property "kotlin.code.style=official"
    write_property "kotlin.incremental=false"

    # Android properties common to all build environments
    write_property "android.nonTransitiveRClass=true"
    write_property "android.useAndroidX=true"
    write_property "android.defaults.buildfeatures.viewbinding=false"
    write_property "android.defaults.buildfeatures.resvalues=true"
    write_property "android.defaults.buildfeatures.aidl=false"
    write_property "android.defaults.buildfeatures.renderscript=false"
    write_property "android.defaults.buildfeatures.shaders=false"
    write_property "android.defaults.buildfeatures.buildconfig=true"

    # JDK path exposed by actions/setup-java
    write_property "org.gradle.java.installations.paths=$gha_jdk_path"
    echo "✅ Common Gradle properties written"
}

write_macos_properties() {
    echo "🔥 Fine tuning Gradle properties for MacOS GHA runner"
    write_property "org.gradle.jvmargs=-Xmx7g -XX:MaxMetaspaceSize=1536m -XX:+UseParallelGC -Dfile.encoding=UTF-8"
    write_property "kotlin.daemon.jvmargs=-Xmx3g -Xms512m -XX:+UseParallelGC -Dfile.encoding=UTF-8"
    write_property "org.gradle.parallel.threads=3"
    echo "✅ Fine tuning complete"
}

write_linux_properties() {
    echo "🔥 Fine tuning Gradle properties for Linux GHA runner"
    write_property "org.gradle.jvmargs=-Xmx6g -Xms1g -XX:MaxMetaspaceSize=1g -XX:+UseParallelGC -Dfile.encoding=UTF-8"
    write_property "kotlin.daemon.jvmargs=-Xmx6g -Xms1g -XX:+UseParallelGC -Dfile.encoding=UTF-8"
    write_property "org.gradle.parallel.threads=4"
    echo "✅ Fine tuning complete"
}

# Preserve the "NOT REPLACEABLE AREA" section if it exists
readonly not_replaceable_marker="############## NOT REPLACEABLE AREA ##############"
readonly temp_file=$(mktemp)

if [ -f "$properties_file" ] && grep -q "^$not_replaceable_marker" "$properties_file"; then
    echo "📌 Preserving NOT REPLACEABLE AREA section"
    sed -n "/^$not_replaceable_marker/,\$p" "$properties_file" > "$temp_file"
fi

rm "$properties_file" && touch "$properties_file"

echo
write_common_properties

case "$RUNNER_OS" in
"macOS")
    write_macos_properties
    ;;
*)
    write_linux_properties
    ;;
esac

# Append the preserved "NOT REPLACEABLE AREA" section back
if [ -s "$temp_file" ]; then
    echo
    echo "📌 Restoring NOT REPLACEABLE AREA section"
    echo >> "$properties_file"
    cat "$temp_file" >> "$properties_file"
    rm "$temp_file"
fi

echo
cat "$properties_file"
echo