import SwiftUI
import Lemonade

struct TextFieldDisplayView: View {
    @State private var basicText = ""
    @State private var labeledText = ""
    @State private var errorText = "Invalid input"
    @State private var supportText = ""
    @State private var leadingText = ""
    @State private var trailingText = ""
    @State private var selectorText = ""
    @State private var selectedPrefix = "+1"

    var body: some View {
        ScrollView {
            VStack(alignment: .leading, spacing: 32) {
                // Basic
                sectionView(title: "Basic") {
                    LemonadeUi.TextField(
                        input: $basicText,
                        placeholderText: "Enter text..."
                    )
                }

                // With Label
                sectionView(title: "With Label") {
                    LemonadeUi.TextField(
                        input: $labeledText,
                        label: "Email Address",
                        placeholderText: "you@example.com"
                    )
                }

                // With Error
                sectionView(title: "With Error") {
                    LemonadeUi.TextField(
                        input: $errorText,
                        label: "Username",
                        placeholderText: "Enter username",
                        errorMessage: "Username is already taken",
                        error: true
                    )
                }

                // With Support Text
                sectionView(title: "With Support Text") {
                    LemonadeUi.TextField(
                        input: $supportText,
                        label: "Password",
                        supportText: "Must be at least 8 characters",
                        placeholderText: "Enter password"
                    )
                }

                // With Leading Content
                sectionView(title: "With Leading Icon") {
                    LemonadeUi.TextField(
                        input: $leadingText,
                        label: "Search",
                        placeholderText: "Search..."
                    ) {
                        LemonadeUi.Icon(
                            icon: .search,
                            contentDescription: nil,
                            size: .medium,
                            tint: LemonadeTheme.colors.content.contentSecondary
                        )
                    } trailingContent: {
                        EmptyView()
                    }
                }

                // With Trailing Content
                sectionView(title: "With Trailing Icon") {
                    LemonadeUi.TextField(
                        input: $trailingText,
                        label: "Amount",
                        placeholderText: "0.00"
                    ) {
                        EmptyView()
                    } trailingContent: {
                        LemonadeUi.Icon(
                            icon: .circleInfo,
                            contentDescription: nil,
                            size: .medium,
                            tint: LemonadeTheme.colors.content.contentSecondary
                        )
                    }
                }

                // With Selector
                sectionView(title: "TextField With Selector") {
                    LemonadeUi.TextFieldWithSelector(
                        input: $selectorText,
                        leadingAction: {
                            print("Show country code picker")
                        },
                        leadingContent: {
                            HStack(spacing: LemonadeTheme.spaces.spacing100) {
                                LemonadeUi.Text(
                                    selectedPrefix,
                                    textStyle: LemonadeTypography.shared.bodyMediumMedium
                                )
                                LemonadeUi.Icon(
                                    icon: .chevronDown,
                                    contentDescription: nil,
                                    size: .small
                                )
                            }
                        },
                        label: "Phone Number",
                        placeholderText: "Enter phone number"
                    )
                }

                // Disabled
                sectionView(title: "Disabled") {
                    LemonadeUi.TextField(
                        input: .constant("Disabled content"),
                        label: "Disabled Field",
                        placeholderText: "Cannot edit",
                        enabled: false
                    )
                }
            }
            .padding()
        }
        .navigationTitle("TextField")
    }

    private func sectionView<Content: View>(title: String, @ViewBuilder content: () -> Content) -> some View {
        VStack(alignment: .leading, spacing: 12) {
            Text(title)
                .font(.headline)
                .foregroundStyle(.secondary)

            content()
        }
    }
}

#Preview {
    NavigationStack {
        TextFieldDisplayView()
    }
}
