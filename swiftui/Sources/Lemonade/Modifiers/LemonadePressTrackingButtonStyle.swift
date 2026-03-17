import SwiftUI

// MARK: - Press Tracking Button Style

/// A shared `ButtonStyle` that tracks press state via a binding.
///
/// Syncs `configuration.isPressed` to the provided `isPressed` binding
/// with a short ease-in-out animation. Used internally by Lemonade button
/// components (Button, IconButton, Link, SegmentedControl) to drive
/// pressed-state visual feedback.
struct LemonadePressTrackingButtonStyle: ButtonStyle {
    @Binding var isPressed: Bool

    func makeBody(configuration: Configuration) -> some View {
        configuration.label
            .onChange(of: configuration.isPressed) { newValue in
                withAnimation(.easeInOut(duration: 0.1)) {
                    isPressed = newValue
                }
            }
    }
}
