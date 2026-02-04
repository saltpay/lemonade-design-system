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

    var icon: LemonadeIcon {
        switch self {
        case .success: return .circleCheck
        case .error: return .circleX
        case .neutral: return .circleAlert
        }
    }

    func iconColor(colors: LemonadeSemanticColors) -> Color {
        switch self {
        case .success: return colors.content.contentPositiveOnColor
        case .error: return colors.content.contentCritical
        case .neutral: return colors.content.contentNeutralOnColor
        }
    }
}

// MARK: - Toast Duration

/// Predefined duration values for toast notifications.
public enum LemonadeToastDuration {
    /// Short duration: 3 seconds
    case short
    /// Medium duration: 6 seconds
    case medium
    /// Long duration: 9 seconds
    case long
    /// Custom duration
    case custom(TimeInterval)

    public var timeInterval: TimeInterval {
        switch self {
        case .short: return 3
        case .medium: return 6
        case .long: return 9
        case .custom(let duration): return duration
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

    private var displayIcon: LemonadeIcon {
        switch voice {
        case .neutral:
            return customIcon ?? voice.icon
        case .success, .error:
            return voice.icon
        }
    }

    var body: some View {
        HStack(spacing: LemonadeTheme.spaces.spacing300) {
            LemonadeUi.Icon(
                icon: displayIcon,
                contentDescription: nil,
                size: .medium,
                tint: voice.iconColor(colors: LemonadeTheme.colors)
            )

            Text(label)
                .font(LemonadeTypography.shared.bodySmallMedium.font)
                .foregroundStyle(LemonadeTheme.colors.content.contentAlwaysLight)
                .lineLimit(nil)
        }
        .padding(
            EdgeInsets(
                top: LemonadeTheme.spaces.spacing300,
                leading: LemonadeTheme.spaces.spacing400,
                bottom: LemonadeTheme.spaces.spacing300,
                trailing: LemonadeTheme.spaces.spacing500
            )
        )
        .frame(minHeight: LemonadeTheme.sizes.size1100)
        .background(LemonadeTheme.colors.background.bgAlwaysDark)
        .clipShape(RoundedRectangle(cornerRadius: .radius.radiusFull))
        .lemonadeShadow(.large)
        .accessibilityElement(children: .combine)
        .accessibilityLabel(label)
    }
}

// MARK: - Previews

#if DEBUG
struct LemonadeToast_Previews: PreviewProvider {
    static var previews: some View {
        VStack(spacing: 24) {
            LemonadeUi.Toast(label: "Changes saved successfully", voice: .success)
            LemonadeUi.Toast(label: "Something went wrong", voice: .error)
            LemonadeUi.Toast(label: "Your session will expire soon", voice: .neutral)
            LemonadeUi.Toast(label: "Added to favorites", voice: .neutral, icon: .heart)
            LemonadeUi.Toast(label: "Really long label that should wrap onto multiple lines to demonstrate text wrapping in the toast component", voice: .neutral, icon: .heart)
        }
        .frame(maxWidth: .infinity, maxHeight: .infinity)
        .padding()
        .background(.bg.bgDefault)
        .previewLayout(.sizeThatFits)
    }
}
#endif
