import Foundation

extension Foundation.Bundle {
    static let module: Bundle = {
        let mainPath = Bundle.main.bundleURL.appendingPathComponent("Lemonade_Lemonade.bundle").path
        let buildPath = "/private/var/folders/k8/mxxsx6tj0_d1hdq079cqzs0m0000gn/T/vibe-kanban/worktrees/6647-fix-pr-comments/lemonade-design-system/.build/arm64-apple-macosx/debug/Lemonade_Lemonade.bundle"

        let preferredBundle = Bundle(path: mainPath)

        guard let bundle = preferredBundle ?? Bundle(path: buildPath) else {
            // Users can write a function called fatalError themselves, we should be resilient against that.
            Swift.fatalError("could not load resource bundle: from \(mainPath) or \(buildPath)")
        }

        return bundle
    }()
}