// swift-tools-version: 5.9
import PackageDescription

let package = Package(
  name: "exportedSwiftlaunchdarkly",
  platforms: [.iOS("12.0"), .macOS("10.13"), .tvOS("12.0"), .watchOS("4.0")],
  products: [
    .library(
      name: "exportedSwiftlaunchdarkly",
      type: .static,
      targets: ["exportedSwiftlaunchdarkly"])
  ],
  dependencies: [
    .package(url: "https://github.com/launchdarkly/ios-client-sdk.git", exact: "9.12.3")
  ],
  targets: [
    .target(
      name: "exportedSwiftlaunchdarkly",
      dependencies: [
        .product(name: "LaunchDarkly", package: "ios-client-sdk")
      ],
      path: "Sources")

  ]
)
