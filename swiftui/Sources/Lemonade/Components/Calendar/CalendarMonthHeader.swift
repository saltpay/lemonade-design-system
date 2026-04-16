import SwiftUI

// MARK: - Calendar Month Header

/// A reusable month header with navigation arrows used by calendar components.
///
/// Displays a centered month/year label flanked by previous and next navigation buttons.
/// Used internally by ``LemonadeUi/DatePicker`` and ``LemonadeUi/InlineCalendar``.
struct CalendarMonthHeader: View {
    let headerLabel: String
    let canGoPrev: Bool
    let canGoNext: Bool
    let onPrev: () -> Void
    let onNext: () -> Void
    
    var body: some View {
        HStack {
            LemonadeUi.IconButton(
                icon: .chevronLeft,
                contentDescription: NSLocalizedString("calendar.navigation.previous_month", value: "Previous month", comment: "VoiceOver label for previous month navigation button"),
                onClick: onPrev,
                enabled: canGoPrev,
                type: .ghost
            )
            
            Spacer()
            
            headerText
            
            Spacer()
            
            LemonadeUi.IconButton(
                icon: .chevronRight,
                contentDescription: NSLocalizedString("calendar.navigation.next_month", value: "Next month", comment: "VoiceOver label for next month navigation button"),
                onClick: onNext,
                enabled: canGoNext,
                type: .ghost
            )
        }
    }
    
    /// Header label with a 150ms cross-fade transition driven by `headerLabel`
    /// changes. Mirrors the Compose reference, which wraps the header text in
    /// `AnimatedContent` with `fadeIn(tween(150)) togetherWith fadeOut(tween(150))`.
    /// On iOS 16+ `.contentTransition(.opacity)` makes the cross-fade explicit;
    /// on iOS 15 the value-driven `.animation(...)` still produces a SwiftUI
    /// implicit text animation, which degrades gracefully without breaking.
    @ViewBuilder
    private var headerText: some View {
        let label = LemonadeUi.Text(
            headerLabel,
            textStyle: LemonadeTypography.shared.bodySmallSemiBold,
            color: LemonadeTheme.colors.content.contentPrimary
        )
        
        if #available(iOS 16.0, macOS 13.0, *) {
            label
                .contentTransition(.identity)
                .animation(.spring(response: 0.35, dampingFraction: 0.8), value: headerLabel)
        } else {
            label
                .animation(.spring(response: 0.35, dampingFraction: 0.8), value: headerLabel)
        }
    }
}

struct CalendarMonthHeader_Previews: PreviewProvider {
    static let months: [String] = [
        "January", "February", "March", "April", "May", "June",
        "July", "August", "September", "October", "November", "December"
    ]
    
    struct PreviewWrapper: View {
        @State private var index: Int = 0
        
        var body: some View {
            ScrollView {
                VStack(spacing: 32) {
                    CalendarMonthHeader(
                        headerLabel: months[index],
                        canGoPrev: index > 0,
                        canGoNext: index < months.count - 1,
                        onPrev: { index -= 1 },
                        onNext: { index += 1 }
                    )
                }
                .padding()
            }
            .previewLayout(.sizeThatFits)
        }
    }
    
    static var previews: some View {
        PreviewWrapper()
    }
}
