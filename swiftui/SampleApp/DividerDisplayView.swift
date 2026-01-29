import SwiftUI
import Lemonade

struct DividerDisplayView: View {
    var body: some View {
        ScrollView(.vertical) {
            VStack(alignment: .leading, spacing: .space.spacing600) {
                // Horizontal Divider
                dividerSection(title: "Horizontal Divider") {
                    VStack(alignment: .leading, spacing: .space.spacing400) {
                        VStack(alignment: .leading, spacing: .space.spacing200) {
                            LemonadeUi.Text(
                                "Default",
                                textStyle: LemonadeTypography.shared.bodySmallRegular,
                                color: .content.contentSecondary
                            )
                            LemonadeUi.HorizontalDivider()
                        }

                        VStack(alignment: .leading, spacing: .space.spacing200) {
                            LemonadeUi.Text(
                                "Dashed",
                                textStyle: LemonadeTypography.shared.bodySmallRegular,
                                color: .content.contentSecondary
                            )
                            LemonadeUi.HorizontalDivider(variant: .dashed)
                        }
                    }
                }

                // Horizontal Divider with Label
                dividerSection(title: "Horizontal Divider with Label") {
                    VStack(alignment: .leading, spacing: .space.spacing400) {
                        VStack(alignment: .leading, spacing: .space.spacing200) {
                            LemonadeUi.Text(
                                "Default with Label",
                                textStyle: LemonadeTypography.shared.bodySmallRegular,
                                color: .content.contentSecondary
                            )
                            LemonadeUi.HorizontalDivider(label: "OR")
                        }

                        VStack(alignment: .leading, spacing: .space.spacing200) {
                            LemonadeUi.Text(
                                "Dashed with Label",
                                textStyle: LemonadeTypography.shared.bodySmallRegular,
                                color: .content.contentSecondary
                            )
                            LemonadeUi.HorizontalDivider(label: "OR", variant: .dashed)
                        }
                    }
                }

                // Vertical Divider
                dividerSection(title: "Vertical Divider") {
                    HStack(spacing: .space.spacing600) {
                        VStack(spacing: .space.spacing200) {
                            LemonadeUi.Text(
                                "Default",
                                textStyle: LemonadeTypography.shared.bodySmallRegular,
                                color: .content.contentSecondary
                            )
                            LemonadeUi.VerticalDivider()
                                .frame(height: 48)
                        }

                        VStack(spacing: .space.spacing200) {
                            LemonadeUi.Text(
                                "Dashed",
                                textStyle: LemonadeTypography.shared.bodySmallRegular,
                                color: .content.contentSecondary
                            )
                            LemonadeUi.VerticalDivider(variant: .dashed)
                                .frame(height: 48)
                        }
                    }
                }

                // In Context
                dividerSection(title: "In Context") {
                    VStack(spacing: .space.spacing600) {
                        // Content separation example
                        VStack(alignment: .leading, spacing: .space.spacing300) {
                            LemonadeUi.Text(
                                "Section 1",
                                textStyle: LemonadeTypography.shared.bodyMediumMedium
                            )
                            LemonadeUi.Text(
                                "Some content for the first section",
                                textStyle: LemonadeTypography.shared.bodySmallRegular,
                                color: .content.contentSecondary
                            )
                            LemonadeUi.HorizontalDivider()
                            LemonadeUi.Text(
                                "Section 2",
                                textStyle: LemonadeTypography.shared.bodyMediumMedium
                            )
                            LemonadeUi.Text(
                                "Some content for the second section",
                                textStyle: LemonadeTypography.shared.bodySmallRegular,
                                color: .content.contentSecondary
                            )
                        }
                        .frame(maxWidth: .infinity, alignment: .leading)
                        .padding(.space.spacing400)
                        .background(.bg.bgElevated)
                        .clipShape(RoundedRectangle(cornerRadius: .radius.radius300))

                        // Login separator example
                        VStack(spacing: .space.spacing400) {
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
                        .padding(.space.spacing400)
                        .background(.bg.bgElevated)
                        .clipShape(RoundedRectangle(cornerRadius: .radius.radius300))

                        // Vertical divider in row example
                        HStack {
                            Spacer()
                            VStack {
                                LemonadeUi.Text(
                                    "125",
                                    textStyle: LemonadeTypography.shared.headingSmall
                                )
                                LemonadeUi.Text(
                                    "Posts",
                                    textStyle: LemonadeTypography.shared.bodySmallRegular,
                                    color: .content.contentSecondary
                                )
                            }
                            Spacer()
                            LemonadeUi.VerticalDivider()
                                .frame(height: 40)
                            Spacer()
                            VStack {
                                LemonadeUi.Text(
                                    "1.2K",
                                    textStyle: LemonadeTypography.shared.headingSmall
                                )
                                LemonadeUi.Text(
                                    "Followers",
                                    textStyle: LemonadeTypography.shared.bodySmallRegular,
                                    color: .content.contentSecondary
                                )
                            }
                            Spacer()
                            LemonadeUi.VerticalDivider()
                                .frame(height: 40)
                            Spacer()
                            VStack {
                                LemonadeUi.Text(
                                    "348",
                                    textStyle: LemonadeTypography.shared.headingSmall
                                )
                                LemonadeUi.Text(
                                    "Following",
                                    textStyle: LemonadeTypography.shared.bodySmallRegular,
                                    color: .content.contentSecondary
                                )
                            }
                            Spacer()
                        }
                        .padding(.space.spacing400)
                        .background(.bg.bgElevated)
                        .clipShape(RoundedRectangle(cornerRadius: .radius.radius300))
                    }
                }
            }
            .padding(.space.spacing400)
        }
        .background(.bg.bgSubtle)
        .navigationTitle("Divider")
    }

    private func dividerSection<Content: View>(
        title: String,
        @ViewBuilder content: () -> Content
    ) -> some View {
        VStack(alignment: .leading, spacing: .space.spacing300) {
            LemonadeUi.Text(
                title,
                textStyle: LemonadeTypography.shared.headingXSmall,
                color: .content.contentSecondary
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
