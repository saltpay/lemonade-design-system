Pod::Spec.new do |spec|
  spec.name         = "Lemonade"
  spec.version      = "0.0.6-local"
  spec.summary      = "Lemonade Design System for SwiftUI"
  spec.description  = <<-DESC
    Lemonade is Teya's design system providing consistent UI components,
    colors, typography, and spacing for iOS applications built with SwiftUI.
  DESC

  spec.homepage     = "https://github.com/teya-com/lemonade-design-system"
  spec.license      = { :type => "MIT", :file => "LICENSE" }
  spec.author       = { "Teya" => "engineering@teya.com" }

  spec.platform     = :ios, "15.0"
  spec.swift_version = "5.9"

  # Binary distribution via JFrog (using Maven repository structure)
  spec.source = {
    :http => "https://saltpay.jfrog.io/artifactory/main-maven-local/com/teya/lemonade-design-system/lemonade-swiftui/#{spec.version}/Lemonade.xcframework.zip",
    :type => "zip"
  }

  spec.vendored_frameworks = "Lemonade.xcframework"

  spec.frameworks = "SwiftUI", "Foundation"
end
