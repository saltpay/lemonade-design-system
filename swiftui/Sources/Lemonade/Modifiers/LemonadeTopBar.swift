import SwiftUI
import CoreImage.CIFilterBuiltins

#if os(iOS)

// MARK: - TopBarAction

/// The type of navigation action displayed in the leading slot of a top bar.
/// Mirrors the KMP `TopBarAction` enum.
public enum TopBarAction {
    case back
    case close
}

// MARK: - NavigationAction

/// Configuration for the navigation action in the leading slot of a top bar.
/// Mirrors the KMP `NavigationAction` data class.
public struct NavigationAction {
    public let action: TopBarAction
    let onAction: () -> Void

    public init(action: TopBarAction, onAction: @escaping () -> Void) {
        self.action = action
        self.onAction = onAction
    }
}

// MARK: - Shared Toolbar Slots

private extension View {
    @ViewBuilder
    func topBarToolbarSlots<TrailingContent: View, BottomContent: View>(
        navigationAction: NavigationAction?,
        trailingSlot: (() -> TrailingContent)?,
        bottomSlot: (() -> BottomContent)?
    ) -> some View {
        self
            .navigationBarBackButtonHidden(navigationAction?.action == .close)
            .toolbar {
                ToolbarItem(placement: .navigationBarLeading) {
                    if let navigationAction, navigationAction.action == .close {
                        Button(action: navigationAction.onAction) {
                            LemonadeUi.Icon(
                                icon: .times,
                                contentDescription: "Close"
                            )
                        }
                    }
                }

                ToolbarItem(placement: .navigationBarTrailing) {
                    if let trailingSlot {
                        trailingSlot()
                    }
                }
            }
            .safeAreaInset(edge: .top, spacing: 0) {
                if let bottomSlot {
                    bottomSlot()
                }
            }
    }
}

// MARK: - Basic TopBar Modifier

/// A modifier that applies a native iOS navigation bar styled with Lemonade tokens.
/// Displays a large title that collapses inline on scroll, with optional navigation
/// action, trailing slot, and bottom slot.
private struct BasicTopBarModifier<TrailingContent: View, BottomContent: View>: ViewModifier {
    let label: String
    let collapsedLabel: String?
    let navigationAction: NavigationAction?
    let trailingSlot: (() -> TrailingContent)?
    let bottomSlot: (() -> BottomContent)?

    func body(content: Content) -> some View {
        content
            .navigationTitle(collapsedLabel ?? label)
            .navigationBarTitleDisplayMode(.large)
            .topBarToolbarSlots(
                navigationAction: navigationAction,
                trailingSlot: trailingSlot,
                bottomSlot: bottomSlot
            )
    }
}

// MARK: - Search TopBar Modifier

/// A modifier that applies a native iOS navigation bar with an integrated search field.
/// Uses `.searchable` with `.navigationBarDrawer` placement for native collapse behavior.
private struct SearchTopBarModifier<TrailingContent: View, BottomContent: View>: ViewModifier {
    let label: String
    @Binding var searchInput: String
    let searchPrompt: String
    let expandedLabel: String?
    let navigationAction: NavigationAction?
    let trailingSlot: (() -> TrailingContent)?
    let bottomSlot: (() -> BottomContent)?

    func body(content: Content) -> some View {
        content
            .navigationTitle(expandedLabel ?? label)
            .navigationBarTitleDisplayMode(.large)
            .searchable(
                text: $searchInput,
                placement: .navigationBarDrawer(displayMode: .automatic),
                prompt: searchPrompt
            )
            .topBarToolbarSlots(
                navigationAction: navigationAction,
                trailingSlot: trailingSlot,
                bottomSlot: bottomSlot
            )
    }
}

// MARK: - Header Height PreferenceKey

private struct HeaderHeightKey: PreferenceKey {
    static var defaultValue: CGFloat = 0
    static func reduce(value: inout CGFloat, nextValue: () -> CGFloat) {
        value = nextValue()
    }
}

// MARK: - Variable Blur View

/// Variable blur using the same private `CAFilter` API that Apple uses internally
/// in Music, Photos, and the App Store. Based on nikstar/VariableBlur (MIT license).
private enum VariableBlurDirection {
    case blurredTopClearBottom
    case blurredBottomClearTop
}

private struct VariableBlurView: UIViewRepresentable {
    let maxBlurRadius: CGFloat
    let direction: VariableBlurDirection

    func makeUIView(context: Context) -> VariableBlurUIView {
        VariableBlurUIView(maxBlurRadius: maxBlurRadius, direction: direction)
    }

    func updateUIView(_ uiView: VariableBlurUIView, context: Context) {}
}

private class VariableBlurUIView: UIVisualEffectView {
    private static let ciContext = CIContext()

    init(maxBlurRadius: CGFloat, direction: VariableBlurDirection) {
        // Falls back to standard .regular blur if the private API is unavailable
        super.init(effect: UIBlurEffect(style: .regular))

        let clsName = String("retliFAC".reversed())
        guard let Cls = NSClassFromString(clsName) as? NSObject.Type else { return }
        let selName = String(":epyThtiWretlif".reversed())
        guard let variableBlur = Cls.perform(NSSelectorFromString(selName), with: "variableBlur")
            .takeUnretainedValue() as? NSObject else { return }

        guard let gradientImage = Self.makeGradientImage(direction: direction) else { return }
        variableBlur.setValue(maxBlurRadius, forKey: "inputRadius")
        variableBlur.setValue(gradientImage, forKey: "inputMaskImage")
        variableBlur.setValue(true, forKey: "inputNormalizeEdges")

        let backdropLayer = subviews.first?.layer
        backdropLayer?.filters = [variableBlur]

        for subview in subviews.dropFirst() {
            subview.alpha = 0
        }
    }

    required init?(coder: NSCoder) { fatalError() }

    override func didMoveToWindow() {
        guard let window, let backdropLayer = subviews.first?.layer else { return }
        backdropLayer.setValue(window.traitCollection.displayScale, forKey: "scale")
    }

    // Intentionally empty — suppresses crash in super on trait changes.
    // Deprecated in iOS 17; the blur filter does not need trait-driven updates.
    override func traitCollectionDidChange(_ previousTraitCollection: UITraitCollection?) {}

    private static func makeGradientImage(
        width: CGFloat = 100,
        height: CGFloat = 100,
        direction: VariableBlurDirection
    ) -> CGImage? {
        let gradientFilter = CIFilter.linearGradient()
        gradientFilter.color0 = CIColor.black
        gradientFilter.color1 = CIColor.clear
        gradientFilter.point0 = CGPoint(x: 0, y: height)
        gradientFilter.point1 = CGPoint(x: 0, y: 0)
        if case .blurredBottomClearTop = direction {
            gradientFilter.point0 = .init(x: 0, y: 0)
            gradientFilter.point1 = .init(x: 0, y: height)
        }
        guard let outputImage = gradientFilter.outputImage else { return nil }
        return ciContext.createCGImage(
            outputImage,
            from: CGRect(x: 0, y: 0, width: width, height: height)
        )
    }
}

// MARK: - Compact Large TopBar Header

/// Custom header row matching KMP `CompactLargeTopBarHeading` — title left, trailing slot right,
/// on the same line. No navigation bar involvement.
private struct CompactLargeHeader<TrailingContent: View>: View {
    let label: String
    let subheading: String?
    let trailingSlot: (() -> TrailingContent)?

    var body: some View {
        HStack(alignment: .top, spacing: LemonadeSpacing.spacing200.value) {
            VStack(alignment: .leading, spacing: 0) {
                LemonadeUi.Text(
                    label,
                    textStyle: LemonadeTypography.shared.headingLarge,
                    color: LemonadeTheme.colors.content.contentPrimary
                )
                .lineLimit(2)

                if let subheading {
                    LemonadeUi.Text(
                        subheading,
                        textStyle: LemonadeTypography.shared.bodySmallRegular,
                        color: LemonadeTheme.colors.content.contentSecondary
                    )
                    .lineLimit(1)
                }
            }
            .frame(maxWidth: .infinity, alignment: .leading)

            if let trailingSlot {
                let content = HStack(spacing: LemonadeSpacing.spacing100.value) {
                    trailingSlot()
                }
                .padding(.horizontal, LemonadeSpacing.spacing100.value)

                if #available(iOS 26, *) {
                    content
                        .glassEffect(.regular.interactive(), in: .capsule)
                } else {
                    content
                        .background(.ultraThinMaterial, in: Capsule())
                }
            }
        }
        .padding(.horizontal, LemonadeSpacing.spacing400.value)
        .padding(.bottom, LemonadeSpacing.spacing200.value)
    }
}

// MARK: - Compact Large Blur Layout

/// Shared layout for compact-large variants: ZStack with scrollable content,
/// progressive blur layer, and a measured header slot.
private struct CompactLargeBlurLayout<HeaderContent: View>: View {
    let scrollableContent: AnyView
    @ViewBuilder let headerContent: () -> HeaderContent

    private let fadeExtension: CGFloat = 64
    @State private var headerHeight: CGFloat = 76
    @Environment(\.colorScheme) private var colorScheme

    var body: some View {
        ZStack(alignment: .top) {
            scrollableContent
                .navigationBarHidden(true)
                .safeAreaInset(edge: .top, spacing: 0) {
                    Color.clear.frame(height: headerHeight)
                }

            let totalHeight = headerHeight + fadeExtension
            VariableBlurView(maxBlurRadius: 5, direction: .blurredTopClearBottom)
                .overlay {
                    LinearGradient(stops: [
                        .init(color: fadeTint.opacity(0.7), location: 0),
                        .init(color: fadeTint.opacity(0.5), location: 90 / totalHeight),
                        .init(color: fadeTint.opacity(0), location: 1),
                    ], startPoint: .top, endPoint: .bottom)
                }
                .frame(height: totalHeight)
                .ignoresSafeArea(edges: .top)
                .allowsHitTesting(false)

            headerContent()
                .frame(maxWidth: .infinity, alignment: .leading)
                .overlay(
                    GeometryReader { geo in
                        Color.clear.preference(key: HeaderHeightKey.self, value: geo.size.height)
                    }
                )
        }
        .onPreferenceChange(HeaderHeightKey.self) { headerHeight = $0 }
    }

    private var fadeTint: Color {
        colorScheme == .dark ? .black : .white
    }
}

// MARK: - Compact Large TopBar Modifier

/// A modifier that applies a large left-aligned title with optional subheading.
/// Designed for top-level screens without navigation actions.
/// The trailing slot sits on the same row as the title (not in the navigation bar).
/// Content scrolls behind the header with a progressive blur effect.
private struct CompactLargeTopBarModifier<TrailingContent: View, BottomContent: View>: ViewModifier {
    let label: String
    let subheading: String?
    let trailingSlot: (() -> TrailingContent)?
    let bottomSlot: (() -> BottomContent)?

    func body(content: Content) -> some View {
        CompactLargeBlurLayout(scrollableContent: AnyView(content)) {
            VStack(alignment: .leading, spacing: 0) {
                CompactLargeHeader(
                    label: label,
                    subheading: subheading,
                    trailingSlot: trailingSlot
                )
                .padding(.top, LemonadeSpacing.spacing200.value)

                if let bottomSlot {
                    bottomSlot()
                }
            }
        }
    }
}

// MARK: - Compact Large Search TopBar Modifier

/// A modifier that applies a large left-aligned title with optional subheading
/// and an integrated search field. Content scrolls behind the header with blur.
///
/// When the search field gains focus, the title animates out (shrinks vertically)
/// and only the search field remains — matching KMP behavior.
private struct CompactLargeSearchTopBarModifier<TrailingContent: View>: ViewModifier {
    let label: String
    let subheading: String?
    @Binding var searchInput: String
    let searchPrompt: String
    let trailingSlot: (() -> TrailingContent)?

    @State private var isSearchFocused: Bool = false
    @FocusState private var searchFieldFocused: Bool

    func body(content: Content) -> some View {
        CompactLargeBlurLayout(scrollableContent: AnyView(content)) {
            VStack(alignment: .leading, spacing: 0) {
                if !isSearchFocused {
                    CompactLargeHeader(
                        label: label,
                        subheading: subheading,
                        trailingSlot: trailingSlot
                    )
                    .padding(.top, LemonadeSpacing.spacing200.value)
                    .transition(.move(edge: .top).combined(with: .opacity))
                }

                LemonadeUi.SearchField(
                    input: $searchInput,
                    placeholder: searchPrompt
                )
                .focused($searchFieldFocused)
                .padding(.horizontal, LemonadeSpacing.spacing400.value)
                .padding(.vertical, LemonadeSpacing.spacing300.value)
            }
            .animation(.easeInOut(duration: 0.25), value: isSearchFocused)
        }
        .onChange(of: searchFieldFocused) { focused in
            isSearchFocused = focused
        }
    }
}

// MARK: - View Extensions

public extension View {

    // MARK: 1. Basic TopBar

    /// Applies a Lemonade-styled navigation bar with a collapsible large title.
    ///
    /// Uses native iOS `.navigationTitle` and `.toolbar` under the hood,
    /// preserving all native navigation interactions (swipe-to-go-back, title morphing, etc.).
    ///
    /// Mirrors KMP `LemonadeUi.TopBar(label, collapsedLabel, navigationAction, trailingSlot, bottomSlot)`.
    ///
    /// ## Usage
    /// ```swift
    /// ScrollView { content }
    ///     .lemonadeTopBar(
    ///         label: "Settings",
    ///         navigationAction: NavigationAction(action: .back, onAction: { dismiss() }),
    ///         trailingSlot: {
    ///             LemonadeUi.IconButton(icon: .bell, contentDescription: "Notifications", onClick: {})
    ///         }
    ///     )
    /// ```
    func lemonadeTopBar<TrailingContent: View, BottomContent: View>(
        label: String,
        collapsedLabel: String? = nil,
        navigationAction: NavigationAction? = nil,
        @ViewBuilder trailingSlot: @escaping () -> TrailingContent,
        @ViewBuilder bottomSlot: @escaping () -> BottomContent
    ) -> some View {
        modifier(BasicTopBarModifier(
            label: label,
            collapsedLabel: collapsedLabel,
            navigationAction: navigationAction,
            trailingSlot: trailingSlot,
            bottomSlot: bottomSlot
        ))
    }

    /// Applies a Lemonade-styled navigation bar with a collapsible large title (no bottom slot).
    func lemonadeTopBar<TrailingContent: View>(
        label: String,
        collapsedLabel: String? = nil,
        navigationAction: NavigationAction? = nil,
        @ViewBuilder trailingSlot: @escaping () -> TrailingContent
    ) -> some View {
        modifier(BasicTopBarModifier(
            label: label,
            collapsedLabel: collapsedLabel,
            navigationAction: navigationAction,
            trailingSlot: trailingSlot,
            bottomSlot: nil as (() -> EmptyView)?
        ))
    }

    /// Applies a Lemonade-styled navigation bar with a collapsible large title (no trailing or bottom slot).
    func lemonadeTopBar(
        label: String,
        collapsedLabel: String? = nil,
        navigationAction: NavigationAction? = nil
    ) -> some View {
        modifier(BasicTopBarModifier<EmptyView, EmptyView>(
            label: label,
            collapsedLabel: collapsedLabel,
            navigationAction: navigationAction,
            trailingSlot: nil,
            bottomSlot: nil
        ))
    }

    // MARK: 2. Search TopBar

    /// Applies a Lemonade-styled navigation bar with an integrated search field.
    ///
    /// Uses native `.searchable` with `.navigationBarDrawer` placement.
    /// The search field collapses automatically on scroll.
    ///
    /// Mirrors KMP `LemonadeUi.TopBar(label, searchInput, onSearchChanged, expandedLabel, navigationAction, trailingSlot, bottomSlot)`.
    ///
    /// ## Usage
    /// ```swift
    /// ScrollView { content }
    ///     .lemonadeTopBar(
    ///         label: "Search",
    ///         searchInput: $query,
    ///         expandedLabel: "Discover",
    ///         navigationAction: NavigationAction(action: .back, onAction: { dismiss() })
    ///     )
    /// ```
    func lemonadeTopBar<TrailingContent: View, BottomContent: View>(
        label: String,
        searchInput: Binding<String>,
        searchPrompt: String = "Search...",
        expandedLabel: String? = nil,
        navigationAction: NavigationAction? = nil,
        @ViewBuilder trailingSlot: @escaping () -> TrailingContent,
        @ViewBuilder bottomSlot: @escaping () -> BottomContent
    ) -> some View {
        modifier(SearchTopBarModifier(
            label: label,
            searchInput: searchInput,
            searchPrompt: searchPrompt,
            expandedLabel: expandedLabel,
            navigationAction: navigationAction,
            trailingSlot: trailingSlot,
            bottomSlot: bottomSlot
        ))
    }

    /// Applies a Lemonade-styled navigation bar with search (no bottom slot).
    func lemonadeTopBar<TrailingContent: View>(
        label: String,
        searchInput: Binding<String>,
        searchPrompt: String = "Search...",
        expandedLabel: String? = nil,
        navigationAction: NavigationAction? = nil,
        @ViewBuilder trailingSlot: @escaping () -> TrailingContent
    ) -> some View {
        modifier(SearchTopBarModifier(
            label: label,
            searchInput: searchInput,
            searchPrompt: searchPrompt,
            expandedLabel: expandedLabel,
            navigationAction: navigationAction,
            trailingSlot: trailingSlot,
            bottomSlot: nil as (() -> EmptyView)?
        ))
    }

    /// Applies a Lemonade-styled navigation bar with search (no trailing or bottom slot).
    func lemonadeTopBar(
        label: String,
        searchInput: Binding<String>,
        searchPrompt: String = "Search...",
        expandedLabel: String? = nil,
        navigationAction: NavigationAction? = nil
    ) -> some View {
        modifier(SearchTopBarModifier<EmptyView, EmptyView>(
            label: label,
            searchInput: searchInput,
            searchPrompt: searchPrompt,
            expandedLabel: expandedLabel,
            navigationAction: navigationAction,
            trailingSlot: nil,
            bottomSlot: nil
        ))
    }

    // MARK: 3. Compact Large TopBar

    /// Applies a Lemonade-styled large left-aligned title bar with optional subheading.
    ///
    /// Designed for top-level screens without navigation actions.
    /// When `bottomSlot` is provided, the title scrolls away and the slot becomes sticky.
    ///
    /// Mirrors KMP `LemonadeUi.TopBar(label, subheading, trailingSlot, bottomSlot)`.
    ///
    /// ## Usage
    /// ```swift
    /// ScrollView { content }
    ///     .lemonadeTopBar(
    ///         label: "Home",
    ///         subheading: "Welcome back",
    ///         trailingSlot: {
    ///             LemonadeUi.IconButton(icon: .bell, contentDescription: "Notifications", onClick: {})
    ///         }
    ///     )
    /// ```
    func lemonadeTopBar<TrailingContent: View, BottomContent: View>(
        label: String,
        subheading: String?,
        @ViewBuilder trailingSlot: @escaping () -> TrailingContent,
        @ViewBuilder bottomSlot: @escaping () -> BottomContent
    ) -> some View {
        modifier(CompactLargeTopBarModifier(
            label: label,
            subheading: subheading,
            trailingSlot: trailingSlot,
            bottomSlot: bottomSlot
        ))
    }

    /// Applies a Lemonade-styled large title bar with subheading (no bottom slot).
    func lemonadeTopBar<TrailingContent: View>(
        label: String,
        subheading: String?,
        @ViewBuilder trailingSlot: @escaping () -> TrailingContent
    ) -> some View {
        modifier(CompactLargeTopBarModifier(
            label: label,
            subheading: subheading,
            trailingSlot: trailingSlot,
            bottomSlot: nil as (() -> EmptyView)?
        ))
    }

    /// Applies a Lemonade-styled large title bar with subheading (no trailing or bottom slot).
    func lemonadeTopBar(
        label: String,
        subheading: String?
    ) -> some View {
        modifier(CompactLargeTopBarModifier<EmptyView, EmptyView>(
            label: label,
            subheading: subheading,
            trailingSlot: nil,
            bottomSlot: nil
        ))
    }

    // MARK: 4. Compact Large Search TopBar

    /// Applies a Lemonade-styled large title bar with subheading and search.
    ///
    /// The title stays fixed while the search field collapses on scroll.
    ///
    /// Mirrors KMP `LemonadeUi.TopBar(label, subheading, searchInput, onSearchChanged, trailingSlot)`.
    ///
    /// ## Usage
    /// ```swift
    /// ScrollView { content }
    ///     .lemonadeTopBar(
    ///         label: "Discover",
    ///         subheading: "Find what you need",
    ///         searchInput: $query
    ///     )
    /// ```
    func lemonadeTopBar<TrailingContent: View>(
        label: String,
        subheading: String?,
        searchInput: Binding<String>,
        searchPrompt: String = "Search...",
        @ViewBuilder trailingSlot: @escaping () -> TrailingContent
    ) -> some View {
        modifier(CompactLargeSearchTopBarModifier(
            label: label,
            subheading: subheading,
            searchInput: searchInput,
            searchPrompt: searchPrompt,
            trailingSlot: trailingSlot
        ))
    }

    /// Applies a Lemonade-styled large title bar with subheading and search (no trailing slot).
    func lemonadeTopBar(
        label: String,
        subheading: String?,
        searchInput: Binding<String>,
        searchPrompt: String = "Search..."
    ) -> some View {
        modifier(CompactLargeSearchTopBarModifier<EmptyView>(
            label: label,
            subheading: subheading,
            searchInput: searchInput,
            searchPrompt: searchPrompt,
            trailingSlot: nil
        ))
    }
}

#endif
