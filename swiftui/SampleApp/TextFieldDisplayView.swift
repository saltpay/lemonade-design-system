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
    @State private var passwordText = ""
    @State private var isPasswordVisible = false
    @State private var emailText = ""
    @State private var pinText = ""
    @State private var amountValue = LemonadeTextFieldValue(text: "")
    @State private var urlText = ""
    @State private var usernameText = ""
    @State private var autofillPasswordText = ""
    @State private var isAutofillPasswordVisible = false

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
                            tint: .content.contentSecondary
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
                            tint: .content.contentSecondary
                        )
                    }
                }

                // Secure / Password with show-hide toggle
                sectionView(title: "Secure (Password)") {
                    LemonadeUi.TextField(
                        input: $passwordText,
                        label: "Password",
                        placeholderText: "Enter password"
                    ) {
                        LemonadeUi.Icon(
                            icon: .padlock,
                            contentDescription: nil,
                            size: .medium,
                            tint: .content.contentSecondary
                        )
                    } trailingContent: {
                        Button {
                            isPasswordVisible.toggle()
                        } label: {
                            LemonadeUi.Icon(
                                icon: isPasswordVisible ? .eyeOpen : .eyeClosed,
                                contentDescription: isPasswordVisible ? "Hide password" : "Show password",
                                size: .medium,
                                tint: .content.contentSecondary
                            )
                        }
                        .buttonStyle(.plain)
                    }
                    .secureTextEntry(!isPasswordVisible)
                }

                // Keyboard Type — email (via modifier, String binding)
                sectionView(title: "Keyboard Type — Email") {
                    LemonadeUi.TextField(
                        input: $emailText,
                        label: "Email Address",
                        supportText: "Shows the email keyboard with @ and .",
                        placeholderText: "you@example.com"
                    )
                    .lemonadeKeyboardType(.emailAddress)
                }

                // Keyboard Type — numeric PIN (via modifier, String binding)
                sectionView(title: "Keyboard Type — Number Pad") {
                    LemonadeUi.TextField(
                        input: $pinText,
                        label: "PIN",
                        supportText: "Digits-only number pad",
                        placeholderText: "0000"
                    )
                    .lemonadeKeyboardType(.numberPad)
                }

                // Keyboard Type — URL (via modifier, String binding)
                sectionView(title: "Keyboard Type — URL") {
                    LemonadeUi.TextField(
                        input: $urlText,
                        label: "Website",
                        placeholderText: "https://example.com"
                    )
                    .lemonadeKeyboardType(.URL)
                }

                // Keyboard Type — decimal (via explicit parameter, value binding)
                sectionView(title: "Keyboard Type — Decimal (parameter)") {
                    LemonadeUi.TextField(
                        value: $amountValue,
                        label: "Amount",
                        supportText: "Set via the keyboardType: parameter",
                        placeholderText: "0.00",
                        keyboardType: .decimalPad
                    )
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

                // AutoFill + capitalization — username (String binding, via modifiers)
                sectionView(title: "AutoFill — Username") {
                    LemonadeUi.TextField(
                        input: $usernameText,
                        label: "Username",
                        supportText: "AutoFill, no capitalization, no autocorrect",
                        placeholderText: "you@example.com"
                    )
                    .lemonadeTextContentType(.username)
                    .lemonadeKeyboardType(.emailAddress)
                    .lemonadeTextInputAutocapitalization(.none)
                    .lemonadeAutocorrectionDisabled()
                }

                // AutoFill — password with show/hide toggle
                sectionView(title: "AutoFill — Password") {
                    LemonadeUi.TextField(
                        input: $autofillPasswordText,
                        label: "Password",
                        placeholderText: "Enter password"
                    ) {
                        EmptyView()
                    } trailingContent: {
                        Button {
                            isAutofillPasswordVisible.toggle()
                        } label: {
                            LemonadeUi.Icon(
                                icon: isAutofillPasswordVisible ? .eyeOpen : .eyeClosed,
                                contentDescription: isAutofillPasswordVisible ? "Hide password" : "Show password",
                                size: .medium,
                                tint: LemonadeTheme.colors.content.contentSecondary
                            )
                        }
                        .buttonStyle(.plain)
                    }
                    .lemonadeTextContentType(.password)
                    .secureTextEntry(!isAutofillPasswordVisible)
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
                .foregroundStyle(.content.contentSecondary)

            content()
        }
    }
}

#Preview {
    NavigationStack {
        TextFieldDisplayView()
    }
}
