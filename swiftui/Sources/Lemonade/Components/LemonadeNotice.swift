import SwiftUI

// MARK: - Voice

public enum NoticeVoice {
    case info, positive, warning, critical, neutral

    var containerColor: Color {
        switch self {
        case .info: return LemonadeTheme.colors.background.bgInfoSubtle
        case .positive: return LemonadeTheme.colors.background.bgPositiveSubtle
        case .warning: return LemonadeTheme.colors.background.bgCautionSubtle
        case .critical: return LemonadeTheme.colors.background.bgCriticalSubtle
        case .neutral: return LemonadeTheme.colors.background.bgElevated
        }
    }

    var iconTintColor: Color {
        switch self {
        case .info: return LemonadeTheme.colors.content.contentInfo
        case .positive: return LemonadeTheme.colors.content.contentPositive
        case .warning: return LemonadeTheme.colors.content.contentCaution
        case .critical: return LemonadeTheme.colors.content.contentCritical
        case .neutral: return LemonadeTheme.colors.content.contentSecondary
        }
    }

    var actionTextColor: Color {
        switch self {
        case .info: return LemonadeTheme.colors.content.contentInfo
        case .positive: return LemonadeTheme.colors.content.contentPositive
        case .warning: return LemonadeTheme.colors.content.contentCaution
        case .critical: return LemonadeTheme.colors.content.contentCritical
        case .neutral: return LemonadeTheme.colors.content.contentNeutral
        }
    }

    var defaultIcon: LemonadeIcon {
        switch self {
        case .info: return .circleInfo
        case .positive: return .circleCheck
        case .warning: return .triangleAlert
        case .critical: return .triangleAlert
        case .neutral: return .heart
        }
    }
}

// MARK: - Public API

public extension LemonadeUi {

    /// A banner used to display brief, important messages within content.
    /// Can include an icon and action to draw attention to contextual information or status updates.
    ///
    /// - Parameters:
    ///   - content: The body text displayed in the notice.
    ///   - voice: Semantic tone controlling background, icon tint, and action text colors.
    ///   - title: Optional bold heading displayed above the content.
    ///   - showIcon: Whether to display the leading icon. Defaults to `true`.
    ///   - actionLabel: Optional text for the action button below the content.
    ///   - onActionClick: Callback invoked when the action is tapped.
    @ViewBuilder
    static func Notice(
        content: String,
        voice: NoticeVoice,
        title: String? = nil,
        showIcon: Bool = true,
        actionLabel: String? = nil,
        onActionClick: (() -> Void)? = nil
    ) -> some View {
        LemonadeNoticeView(
            content: content,
            voice: voice,
            title: title,
            icon: showIcon ? voice.defaultIcon : nil,
            actionLabel: actionLabel,
            onActionClick: onActionClick
        )
    }
}

// MARK: - Internal View

private struct LemonadeNoticeView: View {
    let content: String
    let voice: NoticeVoice
    let title: String?
    let icon: LemonadeIcon?
    let actionLabel: String?
    let onActionClick: (() -> Void)?

    var body: some View {
        HStack(alignment: .top, spacing: LemonadeTheme.spaces.spacing300) {
            if let icon {
                LemonadeUi.Icon(
                    icon: icon,
                    contentDescription: nil,
                    size: .medium,
                    tint: voice.iconTintColor
                )
                .frame(
                    width: LemonadeTheme.sizes.size500,
                    height: LemonadeTheme.sizes.size600
                )
            }

            VStack(alignment: .leading, spacing: LemonadeTheme.spaces.spacing100) {
                if let title {
                    LemonadeUi.Text(
                        title,
                        textStyle: LemonadeTypography.shared.bodyMediumSemiBold,
                        color: LemonadeTheme.colors.content.contentPrimary
                    )
                }

                LemonadeUi.Text(
                    content,
                    textStyle: title != nil
                        ? LemonadeTypography.shared.bodySmallRegular
                        : LemonadeTypography.shared.bodyMediumRegular,
                    color: LemonadeTheme.colors.content.contentPrimary
                )

                if let actionLabel {
                    Spacer()
                        .frame(height: LemonadeTheme.spaces.spacing200)

                    if let onActionClick {
                        SwiftUI.Button(action: onActionClick) {
                            LemonadeUi.Text(
                                actionLabel,
                                textStyle: LemonadeTypography.shared.bodyMediumSemiBold,
                                color: voice.actionTextColor
                            )
                        }
                        .buttonStyle(.plain)
                    } else {
                        LemonadeUi.Text(
                            actionLabel,
                            textStyle: LemonadeTypography.shared.bodyMediumSemiBold,
                            color: voice.actionTextColor
                        )
                    }
                }
            }
        }
        .frame(maxWidth: .infinity, alignment: .leading)
        .padding(LemonadeTheme.spaces.spacing400)
        .background(voice.containerColor)
        .clipShape(RoundedRectangle(cornerRadius: LemonadeTheme.radius.radius500))
    }
}

// MARK: - Previews

#if DEBUG
struct LemonadeNotice_Previews: PreviewProvider {
    static var previews: some View {
        ScrollView {
            VStack(alignment: .leading, spacing: 24) {
                // Voices
                sectionTitle("Voices")
                ForEach(voices, id: \.self) { voice in
                    LemonadeUi.Notice(
                        content: "This is a \(voiceName(voice)) notice.",
                        voice: voice,
                        actionLabel: "Action",
                        onActionClick: {}
                    )
                }

                // With Title
                sectionTitle("With Title")
                ForEach(voices, id: \.self) { voice in
                    LemonadeUi.Notice(
                        content: "Description text for this notice.",
                        voice: voice,
                        title: voiceName(voice),
                        actionLabel: "Action",
                        onActionClick: {}
                    )
                }

                // Without Icon
                sectionTitle("Without Icon")
                LemonadeUi.Notice(
                    content: "A notice without an icon.",
                    voice: .info,
                    showIcon: false
                )
            }
            .padding()
        }
        .previewLayout(.sizeThatFits)
    }

    private static let voices: [NoticeVoice] = [.info, .positive, .warning, .critical, .neutral]

    private static func voiceName(_ voice: NoticeVoice) -> String {
        switch voice {
        case .info: return "Info"
        case .positive: return "Positive"
        case .warning: return "Warning"
        case .critical: return "Critical"
        case .neutral: return "Neutral"
        }
    }

    @ViewBuilder
    private static func sectionTitle(_ text: String) -> some View {
        LemonadeUi.Text(
            text,
            textStyle: LemonadeTypography.shared.headingXSmall,
            color: LemonadeTheme.colors.content.contentSecondary
        )
        .padding(.top, 8)
    }
}
#endif
