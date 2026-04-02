#!/bin/bash
set -e

# Configuration
SCHEME="Lemonade"
PROJECT="Lemonade.xcodeproj"
FRAMEWORK_NAME="Lemonade"
OUTPUT_DIR="build"
XCFRAMEWORK_OUTPUT="$OUTPUT_DIR/$FRAMEWORK_NAME.xcframework"

echo "🔧 Generating Xcode project from project.yml..."
if ! command -v xcodegen &> /dev/null; then
    echo "Installing XcodeGen..."
    brew install xcodegen
fi
xcodegen generate

echo "🧹 Cleaning previous builds..."
rm -rf "$OUTPUT_DIR"
mkdir -p "$OUTPUT_DIR"

echo "📱 Building for iOS Device (arm64)..."
xcodebuild archive \
    -project "$PROJECT" \
    -scheme "$SCHEME" \
    -configuration Release \
    -destination "generic/platform=iOS" \
    -archivePath "$OUTPUT_DIR/ios-device.xcarchive" \
    SKIP_INSTALL=NO \
    BUILD_LIBRARY_FOR_DISTRIBUTION=YES \
    CODE_SIGNING_ALLOWED=NO

echo "📱 Building for iOS Simulator (arm64 + x86_64)..."
xcodebuild archive \
    -project "$PROJECT" \
    -scheme "$SCHEME" \
    -configuration Release \
    -destination "generic/platform=iOS Simulator" \
    -archivePath "$OUTPUT_DIR/ios-simulator.xcarchive" \
    SKIP_INSTALL=NO \
    BUILD_LIBRARY_FOR_DISTRIBUTION=YES \
    CODE_SIGNING_ALLOWED=NO

echo "📦 Creating XCFramework..."
xcodebuild -create-xcframework \
    -framework "$OUTPUT_DIR/ios-device.xcarchive/Products/Library/Frameworks/$FRAMEWORK_NAME.framework" \
    -framework "$OUTPUT_DIR/ios-simulator.xcarchive/Products/Library/Frameworks/$FRAMEWORK_NAME.framework" \
    -output "$XCFRAMEWORK_OUTPUT"

echo "🗜️ Zipping XCFramework..."
cd "$OUTPUT_DIR"
zip -r "$FRAMEWORK_NAME.xcframework.zip" "$FRAMEWORK_NAME.xcframework"
cd ..

echo "✅ XCFramework created at: $XCFRAMEWORK_OUTPUT"
echo "✅ Zip file created at: $OUTPUT_DIR/$FRAMEWORK_NAME.xcframework.zip"

# Calculate checksum for SPM binary target (if needed later)
echo ""
echo "📝 SHA256 Checksum:"
shasum -a 256 "$OUTPUT_DIR/$FRAMEWORK_NAME.xcframework.zip"
