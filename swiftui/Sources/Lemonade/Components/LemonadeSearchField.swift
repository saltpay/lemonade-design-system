import SwiftUI

// MARK: - SearchField Component

public extension LemonadeUi {
    /// Input field designated to use for search and querying.
    ///
    /// ## Usage
    /// ```swift
    /// LemonadeUi.SearchField(
    ///     input: $searchText,
    ///     onInputChanged: { newValue in searchText = newValue },
    ///     placeholder: "Search..."
    /// )
    /// ```
    ///
    /// - Parameters:
    ///   - input: Binding to the search text
    ///   - onInputChanged: Callback when the input changes
    ///   - placeholder: Optional placeholder text
    ///   - onInputClear: Callback when user requests input to be cleared
    ///   - enabled: Flag to enable or disable the component
    /// - Returns: A styled SearchField view
    @ViewBuilder
    static func SearchField(
        input: Binding<String>,
        onInputChanged: ((String) -> Void)? = nil,
        placeholder: String? = nil,
        onInputClear: (() -> Void)? = nil,
        enabled: Bool = true
    ) -> some View {
        LemonadeSearchFieldView(
            input: input,
            onInputChanged: onInputChanged,
            placeholder: placeholder,
            onInputClear: onInputClear,
            enabled: enabled
        )
    }
}

// MARK: - Internal SearchField View

private struct LemonadeSearchFieldView: View {
    @Binding var input: String
    let onInputChanged: ((String) -> Void)?
    let placeholder: String?
    let onInputClear: (() -> Void)?
    let enabled: Bool

    @FocusState private var isFocused: Bool

    private let height: CGFloat = 44 // size1100
    private let horizontalPadding: CGFloat = 12 // spacing300

    private var backgroundColor: Color {
        isFocused
            ? LemonadeTheme.colors.background.bgDefault
            : LemonadeTheme.colors.background.bgElevated
    }

    private var borderColor: Color {
        isFocused
            ? LemonadeTheme.colors.border.borderSelected
            : .clear
    }

    var body: some View {
        HStack(spacing: LemonadeTheme.spaces.spacing200) {
            // Search icon
            LemonadeUi.Icon(
                icon: .search,
                contentDescription: nil,
                size: .medium,
                tint: LemonadeTheme.colors.content.contentPrimary
            )

            // Text input
            ZStack(alignment: .leading) {
                if let placeholder = placeholder, input.isEmpty {
                    LemonadeUi.Text(
                        placeholder,
                        textStyle: LemonadeTypography.shared.bodyMediumRegular,
                        color: LemonadeTheme.colors.content.contentTertiary
                    )
                }

                SwiftUI.TextField("", text: $input)
                    .font(LemonadeTypography.shared.bodyMediumRegular.font)
                    .foregroundStyle(LemonadeTheme.colors.content.contentPrimary)
                    .focused($isFocused)
                    .disabled(!enabled)
                    .onChange(of: input) { newValue in
                        onInputChanged?(newValue)
                    }
            }
            .frame(maxWidth: .infinity, alignment: .leading)

            // Clear button
            if !input.isEmpty && enabled {
                SwiftUI.Button(action: {
                    if let onInputClear = onInputClear {
                        onInputClear()
                    } else {
                        input = ""
                    }
                }) {
                    LemonadeUi.Icon(
                        icon: .circleXSolid,
                        contentDescription: "Clear",
                        size: .medium,
                        tint: LemonadeTheme.colors.content.contentSecondary
                    )
                }
                .buttonStyle(PlainButtonStyle())
            }
        }
        .padding(.horizontal, horizontalPadding)
        .frame(height: height)
        .background(backgroundColor)
        .clipShape(Capsule())
        .overlay(
            Capsule()
                .stroke(borderColor, lineWidth: LemonadeTheme.borderWidth.state.borderSelected)
        )
        .overlay(
            Capsule()
                .stroke(
                    isFocused ? LemonadeTheme.colors.background.bgElevatedHigh : .clear,
                    lineWidth: LemonadeTheme.borderWidth.base.border50
                )
                .padding(-2)
        )
        .opacity(enabled ? 1.0 : LemonadeTheme.opacity.state.opacityDisabled)
        .animation(.easeInOut(duration: 0.15), value: isFocused)
    }
}

// MARK: - Previews

#if DEBUG
struct LemonadeSearchField_Previews: PreviewProvider {
    static var previews: some View {
        VStack(spacing: 24) {
            StatefulPreview("") { input in
                LemonadeUi.SearchField(
                    input: input,
                    placeholder: "Search..."
                )
            }

            StatefulPreview("Sample text") { input in
                LemonadeUi.SearchField(
                    input: input,
                    placeholder: "Search..."
                )
            }

            StatefulPreview("") { input in
                LemonadeUi.SearchField(
                    input: input,
                    placeholder: "Disabled search",
                    enabled: false
                )
            }
        }
        .padding()
        .previewLayout(.sizeThatFits)
    }
}

private struct StatefulPreview<Value, Content: View>: View {
    @State private var value: Value
    let content: (Binding<Value>) -> Content

    init(_ value: Value, @ViewBuilder content: @escaping (Binding<Value>) -> Content) {
        _value = State(initialValue: value)
        self.content = content
    }

    var body: some View {
        content($value)
    }
}
#endif
