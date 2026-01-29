import SwiftUI
import Lemonade

struct DividerDisplayView: View {
    var body: some View {
        ScrollView(.vertical) {
            VStack(alignment: .leading, spacing: LemonadeTheme.spaces.spacing600) {
                // Horizontal Divider
                dividerSection(title: "Horizontal Divider") {
                    VStack(alignment: .leading, spacing: LemonadeTheme.spaces.spacing400) {
                        VStack(alignment: .leading, spacing: LemonadeTheme.spaces.spacing200) {
                            LemonadeUi.Text(
                                "Default",
                                textStyle: LemonadeTypography().bodySmallRegular,
                                color: LemonadeTheme.colors.content.contentSecondary
                            )
                            LemonadeUi.HorizontalDivider()
                        }

                        VStack(alignment: .leading, spacing: LemonadeTheme.spaces.spacing200) {
                            LemonadeUi.Text(
                                "Dashed",
                                textStyle: LemonadeTypography().bodySmallRegular,
                                color: LemonadeTheme.colors.content.contentSecondary
                            )
                            LemonadeUi.HorizontalDivider(isDashed: true)
                        }
                    }
                }

                // Horizontal Divider with Label
                dividerSection(title: "Horizontal Divider with Label") {
                    VStack(alignment: .leading, spacing: LemonadeTheme.spaces.spacing400) {
                        VStack(alignment: .leading, spacing: LemonadeTheme.spaces.spacing200) {
                            LemonadeUi.Text(
                                "Default with Label",
                                textStyle: LemonadeTypography().bodySmallRegular,
                                color: LemonadeTheme.colors.content.contentSecondary
                            )
                            LemonadeUi.HorizontalDivider(label: "OR")
                        }

                        VStack(alignment: .leading, spacing: LemonadeTheme.spaces.spacing200) {
                            LemonadeUi.Text(
                                "Dashed with Label",
                                textStyle: LemonadeTypography().bodySmallRegular,
                                color: LemonadeTheme.colors.content.contentSecondary
                            )
                            LemonadeUi.HorizontalDivider(label: "OR", isDashed: true)
                        }
                    }
                }

                // Vertical Divider
                dividerSection(title: "Vertical Divider") {
                    HStack(spacing: LemonadeTheme.spaces.spacing600) {
                        VStack(spacing: LemonadeTheme.spaces.spacing200) {
                            LemonadeUi.Text(
                                "Default",
                                textStyle: LemonadeTypography().bodySmallRegular,
                                color: LemonadeTheme.colors.content.contentSecondary
                            )
                            LemonadeUi.VerticalDivider()
                                .frame(height: 48)
                        }

                        VStack(spacing: LemonadeTheme.spaces.spacing200) {
                            LemonadeUi.Text(
                                "Dashed",
                                textStyle: LemonadeTypography().bodySmallRegular,
                                color: LemonadeTheme.colors.content.contentSecondary
                            )
                            LemonadeUi.VerticalDivider(isDashed: true)
                                .frame(height: 48)
                        }
                    }
                }

                // In Context
                dividerSection(title: "In Context") {
                    VStack(spacing: LemonadeTheme.spaces.spacing600) {
                        // Content separation example
                        VStack(alignment: .leading, spacing: LemonadeTheme.spaces.spacing300) {
                            LemonadeUi.Text(
                                "Section 1",
                                textStyle: LemonadeTypography().bodyMediumMedium
                            )
                            LemonadeUi.Text(
                                "Some content for the first section",
                                textStyle: LemonadeTypography().bodySmallRegular,
                                color: LemonadeTheme.colors.content.contentSecondary
                            )
                            LemonadeUi.HorizontalDivider()
                            LemonadeUi.Text(
                                "Section 2",
                                textStyle: LemonadeTypography().bodyMediumMedium
                            )
                            LemonadeUi.Text(
                                "Some content for the second section",
                                textStyle: LemonadeTypography().bodySmallRegular,
                                color: LemonadeTheme.colors.content.contentSecondary
                            )
                        }
                        .frame(maxWidth: .infinity, alignment: .leading)
                        .padding(LemonadeTheme.spaces.spacing400)
                        .background(LemonadeTheme.colors.background.bgElevated)
                        .clipShape(RoundedRectangle(cornerRadius: LemonadeTheme.radius.radius300))

                        // Login separator example
                        VStack(spacing: LemonadeTheme.spaces.spacing400) {
                            LemonadeUi.Button(
                                label: "Continue with Email",
                                onClick: {}
                            )
                            .frame(maxWidth: .infinity)

                            LemonadeUi.HorizontalDivider(label: "OR")

                            LemonadeUi.Button(
                                label: "Continue with Google",
                                onClick: {}
                            )
                            .frame(maxWidth: .infinity)
                        }
                        .padding(LemonadeTheme.spaces.spacing400)
                        .background(LemonadeTheme.colors.background.bgElevated)
                        .clipShape(RoundedRectangle(cornerRadius: LemonadeTheme.radius.radius300))

                        // Vertical divider in row example
                        HStack {
                            Spacer()
                            VStack {
                                LemonadeUi.Text(
                                    "125",
                                    textStyle: LemonadeTypography().headingSmall
                                )
                                LemonadeUi.Text(
                                    "Posts",
                                    textStyle: LemonadeTypography().bodySmallRegular,
                                    color: LemonadeTheme.colors.content.contentSecondary
                                )
                            }
                            Spacer()
                            LemonadeUi.VerticalDivider()
                                .frame(height: 40)
                            Spacer()
                            VStack {
                                LemonadeUi.Text(
                                    "1.2K",
                                    textStyle: LemonadeTypography().headingSmall
                                )
                                LemonadeUi.Text(
                                    "Followers",
                                    textStyle: LemonadeTypography().bodySmallRegular,
                                    color: LemonadeTheme.colors.content.contentSecondary
                                )
                            }
                            Spacer()
                            LemonadeUi.VerticalDivider()
                                .frame(height: 40)
                            Spacer()
                            VStack {
                                LemonadeUi.Text(
                                    "348",
                                    textStyle: LemonadeTypography().headingSmall
                                )
                                LemonadeUi.Text(
                                    "Following",
                                    textStyle: LemonadeTypography().bodySmallRegular,
                                    color: LemonadeTheme.colors.content.contentSecondary
                                )
                            }
                            Spacer()
                        }
                        .padding(LemonadeTheme.spaces.spacing400)
                        .background(LemonadeTheme.colors.background.bgElevated)
                        .clipShape(RoundedRectangle(cornerRadius: LemonadeTheme.radius.radius300))
                    }
                }
            }
            .padding(LemonadeTheme.spaces.spacing400)
        }
        .background(LemonadeTheme.colors.background.bgSubtle)
        .navigationTitle("Divider")
    }

    private func dividerSection<Content: View>(
        title: String,
        @ViewBuilder content: () -> Content
    ) -> some View {
        VStack(alignment: .leading, spacing: LemonadeTheme.spaces.spacing300) {
            LemonadeUi.Text(
                title,
                textStyle: LemonadeTypography().headingXSmall,
                color: LemonadeTheme.colors.content.contentSecondary
            )
            content()
        }
    }
}

#Preview {
    NavigationView {
        DividerDisplayView()
    }
}
