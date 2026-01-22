import Foundation

/// Bundle accessor that works for both SPM and XcodeGen builds.
/// SPM generates a `Bundle.module` accessor, while framework builds need to find the bundle differently.
public extension Bundle {
    /// The Lemonade framework bundle
    static var lemonade: Bundle {
        #if SWIFT_PACKAGE
        return .module
        #else
        // For framework builds, find the bundle containing this class
        let bundle = Bundle(for: BundleFinder.self)

        // If resources are in a separate bundle (e.g., Lemonade.bundle inside the framework)
        if let resourceBundleURL = bundle.url(forResource: "Lemonade", withExtension: "bundle"),
           let resourceBundle = Bundle(url: resourceBundleURL) {
            return resourceBundle
        }

        // Otherwise, resources are directly in the framework bundle
        return bundle
        #endif
    }
}

/// Private class used to locate the framework bundle
private class BundleFinder {}
