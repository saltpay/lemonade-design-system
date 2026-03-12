import SwiftUI
import Lemonade

struct DatePickerDisplayView: View {
    @State private var selectedDate: Date?
    @State private var rangeStart: Date?
    @State private var rangeEnd: Date?
    @State private var constrainedDate: Date?

    private let monthFormatter: (Int) -> String = { month in
        DateFormatter().monthSymbols[month - 1]
    }

    private let weekdays = ["S", "M", "T", "W", "T", "F", "S"]

    private var dateFormatter: DateFormatter {
        let f = DateFormatter()
        f.dateStyle = .medium
        return f
    }

    var body: some View {
        ScrollView {
            VStack(alignment: .leading, spacing: 32) {
                sectionView(title: "Single Date Picker") {
                    VStack(alignment: .leading, spacing: 12) {
                        LemonadeUi.DatePicker(
                            monthFormatter: monthFormatter,
                            weekdayAbbreviations: weekdays,
                            onDateChanged: { date in
                                selectedDate = date
                            }
                        )

                        if let date = selectedDate {
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
                            monthFormatter: monthFormatter,
                            weekdayAbbreviations: weekdays,
                            onDateRangeChanged: { start, end in
                                rangeStart = start
                                rangeEnd = end
                            }
                        )

                        if let start = rangeStart, let end = rangeEnd {
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
                        let today = Calendar.current.startOfDay(for: Date())
                        let minDate = Calendar.current.date(byAdding: .day, value: -7, to: today)!
                        let maxDate = Calendar.current.date(byAdding: .day, value: 30, to: today)!

                        LemonadeUi.DatePicker(
                            monthFormatter: monthFormatter,
                            weekdayAbbreviations: weekdays,
                            onDateChanged: { date in
                                constrainedDate = date
                            },
                            minDate: minDate,
                            maxDate: maxDate
                        )

                        LemonadeUi.Text(
                            "Range: past 7 days to next 30 days",
                            textStyle: LemonadeTypography.shared.bodySmallSemiBold,
                            color: LemonadeTheme.colors.content.contentSecondary
                        )

                        if let date = constrainedDate {
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
                        monthFormatter: monthFormatter,
                        weekdayAbbreviations: weekdays,
                        onDateRangeChanged: { start, end in
                            print("Max range: \(start) - \(end)")
                        },
                        maxRangeDays: 7
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
