import SwiftUI

@available(iOS 16, macOS 13, *)
struct DefaultMinSize: Layout {
    var minWidth: CGFloat = 0
    var minHeight: CGFloat = 0

    func sizeThatFits(proposal: ProposedViewSize, subviews: Subviews, cache: inout ()) -> CGSize {
        guard let child = subviews.first else { return .zero }

        let proposedWidth = proposal.width
        let proposedHeight = proposal.height

        // If parent proposes less than our minimum, respect the parent (don't overflow)
        // If parent proposes more, enforce our minimum
        let effectiveMinWidth: CGFloat = if let pw = proposedWidth, pw < minWidth {
            0 // Let child size naturally within the tight constraint
        } else {
            minWidth
        }

        let effectiveMinHeight: CGFloat = if let ph = proposedHeight, ph < minHeight {
            0
        } else {
            minHeight
        }

        let childSize = child.sizeThatFits(proposal)
        return CGSize(
            width: max(effectiveMinWidth, childSize.width),
            height: max(effectiveMinHeight, childSize.height)
        )
    }

    func placeSubviews(in bounds: CGRect, proposal: ProposedViewSize, subviews: Subviews, cache: inout ()) {
        guard let child = subviews.first else { return }
        child.place(at: CGPoint(x: bounds.midX, y: bounds.midY), anchor: .center, proposal: ProposedViewSize(bounds.size))
    }
}
