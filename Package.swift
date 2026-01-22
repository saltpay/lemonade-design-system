// swift-tools-version: 5.9
// The swift-tools-version declares the minimum version of Swift required to build this package.

import PackageDescription

let package = Package(
    name: "Lemonade",
    platforms: [
        .iOS(.v15),
        .macOS(.v12)
    ],
    products: [
        .library(
            name: "Lemonade",
            targets: ["Lemonade"]
        ),
    ],
    targets: [
        .target(
            name: "Lemonade",
            path: "swiftui/Sources/Lemonade",
            exclude: ["Info.plist"],
            resources: [
                .process("Resources")
            ]
        ),
    ]
)
