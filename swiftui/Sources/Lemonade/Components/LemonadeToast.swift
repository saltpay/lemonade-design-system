import SwiftUI

// MARK: - Toast Voice

/// The voice/variant of a toast notification.
/// Determines the icon and color scheme used for the toast.
public enum LemonadeToastVoice {
    /// Success toast with a checkmark icon.
    case success
    /// Error toast with an error icon.
    case error
    /// Neutral toast with a customizable icon.
    case neutral

    internal var icon: LemonadeIcon? {
        switch self {
        case .success: return .circleCheck
        case .error: return .circleX
        case .neutral: return nil
        }
    }

    internal func iconColor(colors: LemonadeSemanticColors) -> Color {
        switch self {
        case .success: return colors.content.contentPositiveOnColor
        case .error: return colors.content.contentCritical
        case .neutral: return colors.content.contentNeutralOnColor
        }
    }

    /// The sensory feedback type for this voice.
    @available(iOS 17.0, macOS 14.0, tvOS 17.0, watchOS 10.0, *)
    var sensoryFeedback: SensoryFeedback {
        switch self {
        case .success:
            return .success
        case .error:
            return .error
        case .neutral:
            return .impact
        }
    }
}

// MARK: - Toast Component

public extension LemonadeUi {
    /// Toast component for displaying brief feedback messages.
    ///
    /// Displays a brief message with an icon in a pill-shaped container.
    /// Used for non-intrusive feedback to user actions.
    ///
    /// ## Usage
    /// ```swift
    /// // Success toast
    /// LemonadeUi.Toast(label: "Changes saved", voice: .success)
    ///
    /// // Error toast
    /// LemonadeUi.Toast(label: "Something went wrong", voice: .error)
    ///
    /// // Neutral toast with custom icon
    /// LemonadeUi.Toast(label: "Added to favorites", voice: .neutral, icon: .heart)
    /// ```
    ///
    /// - Parameters:
    ///   - label: The message to display in the toast
    ///   - voice: The voice/variant of the toast (success, error, neutral)
    ///   - icon: Custom icon for neutral toasts only. Ignored for success/error voices.
    /// - Returns: A styled Toast view
    @ViewBuilder
    static func Toast(
        label: String,
        voice: LemonadeToastVoice = .neutral,
        icon: LemonadeIcon? = nil
    ) -> some View {
        LemonadeToastView(
            label: label,
            voice: voice,
            customIcon: icon
        )
    }
}

// MARK: - Internal Toast View

private struct LemonadeToastView: View {
    let label: String
    let voice: LemonadeToastVoice
    let customIcon: LemonadeIcon?

    private var displayIcon: LemonadeIcon? {
        switch voice {
        case .neutral:
            return customIcon
        case .success, .error:
            return voice.icon
        }
    }

    private var voiceAccessibilityText: String {
        switch voice {
        case .success: return "Success"
        case .error: return "Error"
        case .neutral: return "Notice"
        }
    }

    var body: some View {
        HStack(spacing: .space.spacing300) {
            if let icon = displayIcon {
                LemonadeUi.Icon(
                    icon: icon,
                    contentDescription: nil,
                    size: .medium,
                    tint: voice.iconColor(colors: LemonadeTheme.colors)
                )
            }

            Text(label)
                .font(LemonadeTypography.shared.bodySmallMedium.font)
                .foregroundStyle(.content.contentAlwaysLight)
                .lineLimit(nil)
        }
        .padding(
            EdgeInsets(
                top: .space.spacing300,
                leading: .space.spacing400,
                bottom: .space.spacing300,
                trailing: .space.spacing500
            )
        )
        .frame(minHeight: .size.size1100)
        .background(.bg.bgAlwaysDark)
        .clipShape(RoundedRectangle(cornerRadius: .radius.radiusFull))
        .lemonadeShadow(.large)
        .accessibilityElement(children: .combine)
        .accessibilityLabel("\(voiceAccessibilityText): \(label)")
    }
}

// MARK: - Previews

#if DEBUG
struct LemonadeToast_Previews: PreviewProvider {
    static var previews: some View {
        VStack(spacing: .space.spacing600) {
            LemonadeUi.Toast(label: "Changes saved successfully", voice: .success)
            LemonadeUi.Toast(label: "Something went wrong", voice: .error)
            LemonadeUi.Toast(label: "Your session will expire soon", voice: .neutral, icon: .circleAlert)
            LemonadeUi.Toast(label: "Added to favorites", voice: .neutral, icon: .heart)
            LemonadeUi.Toast(label: "Toast without an icon", voice: .neutral)
            LemonadeUi.Toast(label: "Really long label that should wrap onto multiple lines to demonstrate text wrapping in the toast component", voice: .neutral, icon: .heart)
        }
        .frame(maxWidth: .infinity, maxHeight: .infinity)
        .padding()
        .background(.bg.bgDefault)
        .previewLayout(.sizeThatFits)
    }
}
#endif
