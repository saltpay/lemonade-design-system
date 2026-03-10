import SwiftUI

@available(iOS 16, macOS 13, *)
struct DefaultMinSize: Layout {
    var minWidth: CGFloat = 0
    var minHeight: CGFloat = 0

    func sizeThatFits(proposal: ProposedViewSize, subviews: Subviews, cache: inout ()) -> CGSize {
        guard let child = subviews.first else { return .zero }

        let childSize = child.sizeThatFits(proposal)
        let idealSize = child.sizeThatFits(.unspecified)

        return CGSize(
            width: resolvedDimension(proposed: proposal.width, minimum: minWidth, ideal: idealSize.width, child: childSize.width),
            height: resolvedDimension(proposed: proposal.height, minimum: minHeight, ideal: idealSize.height, child: childSize.height)
        )
    }

    func placeSubviews(in bounds: CGRect, proposal: ProposedViewSize, subviews: Subviews, cache: inout ()) {
        guard let child = subviews.first else { return }
        child.place(at: CGPoint(x: bounds.midX, y: bounds.midY), anchor: .center, proposal: ProposedViewSize(bounds.size))
    }

    /// Resolves a single dimension (width or height) based on the proposed size.
    ///
    /// - `proposed` is nil, ≤0, or ∞ → return `contentSize` (appear inflexible to HStack probes)
    /// - `minimum > 0 && proposed > contentSize` → return `proposed` (stretch to fill, e.g. .frame(maxWidth: .infinity))
    /// - `proposed < contentSize` → return `max(child, proposed)` (graceful shrink in tight containers)
    /// - Otherwise → return `contentSize` (content-hugging)
    ///
    /// The ≤0 and ∞ checks make the view report the same size for HStack's min/max probes,
    /// so HStack treats it as inflexible and won't distribute excess space to it.
    /// The `minimum > 0` guard prevents vertical stretching (minHeight defaults to 0).
    private func resolvedDimension(proposed: CGFloat?, minimum: CGFloat, ideal: CGFloat, child: CGFloat) -> CGFloat {
        let contentSize = max(minimum, ideal)
        guard let proposed else { return contentSize }
        if proposed < minimum { return max(child, proposed) }
        return contentSize
    }
}
