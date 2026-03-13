import SwiftUI
import Lemonade

struct DatePickerDisplayView: View {
    @StateObject private var singleState = LemonadeDatePickerState()
    @StateObject private var rangeState = LemonadeDateRangePickerState()
    @StateObject private var constrainedState: LemonadeDatePickerState
    @StateObject private var maxRangeState = LemonadeDateRangePickerState(maxRangeDays: 7)

    private let monthFormatter: (Int) -> String = { month in
        DateFormatter().monthSymbols[month - 1]
    }

    private let weekdays = ["S", "M", "T", "W", "T", "F", "S"]

    private var dateFormatter: DateFormatter {
        let f = DateFormatter()
        f.dateStyle = .medium
        return f
    }

    init() {
        let today = Calendar.current.startOfDay(for: Date())
        let minDate = Calendar.current.date(byAdding: .day, value: -7, to: today)!
        let maxDate = Calendar.current.date(byAdding: .day, value: 30, to: today)!
        _constrainedState = StateObject(wrappedValue: LemonadeDatePickerState(minDate: minDate, maxDate: maxDate))
    }

    var body: some View {
        ScrollView {
            VStack(alignment: .leading, spacing: 32) {
                sectionView(title: "Single Date Picker") {
                    VStack(alignment: .leading, spacing: 12) {
                        LemonadeUi.DatePicker(
                            state: singleState,
                            monthFormatter: monthFormatter,
                            weekdayAbbreviations: weekdays
                        )

                        if let date = singleState.selectedDate {
                            LemonadeUi.Text(
                                "Selected: \(dateFormatter.string(from: date))",
                                textStyle: LemonadeTypography.shared.bodySmallSemiBold,
                                color: LemonadeTheme.colors.content.contentSecondary
                            )
                        }
                    }
                }

                sectionView(title: "Date Range Picker") {
                    VStack(alignment: .leading, spacing: 12) {
                        LemonadeUi.DateRangePicker(
                            state: rangeState,
                            monthFormatter: monthFormatter,
                            weekdayAbbreviations: weekdays
                        )

                        if let start = rangeState.selectedStartDate, let end = rangeState.selectedEndDate {
                            LemonadeUi.Text(
                                "Range: \(dateFormatter.string(from: start)) - \(dateFormatter.string(from: end))",
                                textStyle: LemonadeTypography.shared.bodySmallSemiBold,
                                color: LemonadeTheme.colors.content.contentSecondary
                            )
                        }
                    }
                }

                sectionView(title: "With Min/Max Constraints") {
                    VStack(alignment: .leading, spacing: 12) {
                        LemonadeUi.DatePicker(
                            state: constrainedState,
                            monthFormatter: monthFormatter,
                            weekdayAbbreviations: weekdays
                        )

                        LemonadeUi.Text(
                            "Range: past 7 days to next 30 days",
                            textStyle: LemonadeTypography.shared.bodySmallSemiBold,
                            color: LemonadeTheme.colors.content.contentSecondary
                        )

                        if let date = constrainedState.selectedDate {
                            LemonadeUi.Text(
                                "Selected: \(dateFormatter.string(from: date))",
                                textStyle: LemonadeTypography.shared.bodySmallSemiBold,
                                color: LemonadeTheme.colors.content.contentSecondary
                            )
                        }
                    }
                }

                sectionView(title: "Range with Max Days (7)") {
                    LemonadeUi.DateRangePicker(
                        state: maxRangeState,
                        monthFormatter: monthFormatter,
                        weekdayAbbreviations: weekdays
                    )
                }
            }
            .padding()
        }
        .navigationTitle("DatePicker")
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
        DatePickerDisplayView()
    }
}
