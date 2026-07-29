import Foundation

/// Resolved exactly once, on first access.
///
/// This must not be recomputed per call: every generated colour token passes
/// `bundle: .lemonade`, so this accessor sits on the hot path of essentially
/// every view body. In framework builds the resolution below costs an ObjC
/// class-to-bundle lookup plus an `NSBundle` resource lookup that misses
/// (resources are compiled directly into the framework bundle, not into a
/// nested `Lemonade.bundle`), which is far too expensive to repeat.
private let lemonadeBundle: Bundle = {
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
}()

/// Bundle accessor that works for both SPM and XcodeGen builds.
/// SPM generates a `Bundle.module` accessor, while framework builds need to find the bundle differently.
public extension Bundle {
    /// The Lemonade framework bundle
    static var lemonade: Bundle { lemonadeBundle }
}

/// Private class used to locate the framework bundle
private class BundleFinder {}
