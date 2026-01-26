import 'package:lemonade_design_system/lemonade_design_system.dart';
import 'package:lemonade_example/components.dart';

const _kMonthNames = [
  'January',
  'February',
  'March',
  'April',
  'May',
  'June',
  'July',
  'August',
  'September',
  'October',
  'November',
  'December',
];

const _kWeekdayAbbreviations = ['S', 'M', 'T', 'W', 'T', 'F', 'S'];

String _formatMonthHeader(int year, int month) {
  return '${_kMonthNames[month - 1]} $year';
}

/// Example screen demonstrating the usage of [LemonadeDatePicker].
class DatePickerExampleScreen extends StatefulWidget {
  /// Creates a new instance of [DatePickerExampleScreen].
  const DatePickerExampleScreen({super.key});

  @override
  State<DatePickerExampleScreen> createState() =>
      _DatePickerExampleScreenState();
}

class _DatePickerExampleScreenState extends State<DatePickerExampleScreen> {
  DateTime? selectedDate;
  DateTime? selectedDateFutureOnly;
  DateTime? selectedDatePastOnly;
  DateTime? rangeStartDate;
  DateTime? rangeEndDate;

  @override
  Widget build(BuildContext context) {
    final theme = LemonadeTheme.of(context);

    return ExampleScaffold(
      title: 'Date Picker Example',
      body: SingleChildScrollView(
        padding: EdgeInsets.all(theme.spaces.spacing600),
        child: ExampleSection(
          title: 'Date Picker',
          children: [
            ExampleRow(
              label: 'Default (all dates selectable)',
              children: [
                Container(
                  decoration: ShapeDecoration(
                    color: theme.colors.background.bgSubtle,
                    shape: theme.shapes.radius200,
                  ),
                  child: LemonadeDatePicker(
                    monthHeaderFormatter: _formatMonthHeader,
                    weekdayAbbreviations: _kWeekdayAbbreviations,
                    initialDate: DateTime.now(),
                    onDateChanged: (DateTime newDate) {
                      setState(() {
                        selectedDate = newDate;
                      });
                    },
                  ),
                ),
              ],
            ),
            if (selectedDate != null)
              Text(
                'Selected: ${_formatDate(selectedDate!)}',
                style: theme.typography.bodySmallMedium.copyWith(
                  color: theme.colors.content.contentSecondary,
                ),
              ),
            SizedBox(height: theme.spaces.spacing600),
            ExampleRow(
              label: 'Future dates only',
              children: [
                Container(
                  decoration: ShapeDecoration(
                    color: theme.colors.background.bgSubtle,
                    shape: theme.shapes.radius200,
                  ),
                  child: LemonadeDatePicker(
                    monthHeaderFormatter: _formatMonthHeader,
                    weekdayAbbreviations: _kWeekdayAbbreviations,
                    allowBeforeToday: false,
                    onDateChanged: (DateTime newDate) {
                      setState(() {
                        selectedDateFutureOnly = newDate;
                      });
                    },
                  ),
                ),
              ],
            ),
            if (selectedDateFutureOnly != null)
              Text(
                'Selected: ${_formatDate(selectedDateFutureOnly!)}',
                style: theme.typography.bodySmallMedium.copyWith(
                  color: theme.colors.content.contentSecondary,
                ),
              ),
            SizedBox(height: theme.spaces.spacing600),
            ExampleRow(
              label: 'Past dates only (allowAfterToday: false)',
              children: [
                Container(
                  decoration: ShapeDecoration(
                    color: theme.colors.background.bgSubtle,
                    shape: theme.shapes.radius200,
                  ),
                  child: LemonadeDatePicker(
                    monthHeaderFormatter: _formatMonthHeader,
                    weekdayAbbreviations: _kWeekdayAbbreviations,
                    allowAfterToday: false,
                    onDateChanged: (DateTime newDate) {
                      setState(() {
                        selectedDatePastOnly = newDate;
                      });
                    },
                  ),
                ),
              ],
            ),
            if (selectedDatePastOnly != null)
              Text(
                'Selected: ${_formatDate(selectedDatePastOnly!)}',
                style: theme.typography.bodySmallMedium.copyWith(
                  color: theme.colors.content.contentSecondary,
                ),
              ),
            SizedBox(height: theme.spaces.spacing600),
            ExampleRow(
              label: 'Date Range Mode',
              children: [
                Container(
                  decoration: ShapeDecoration(
                    color: theme.colors.background.bgSubtle,
                    shape: theme.shapes.radius200,
                  ),
                  child: LemonadeDatePicker(
                    monthHeaderFormatter: _formatMonthHeader,
                    weekdayAbbreviations: _kWeekdayAbbreviations,
                    isDateRange: true,
                    initialStartDate: rangeStartDate,
                    initialEndDate: rangeEndDate,
                    onDateRangeChanged: (DateTime start, DateTime end) {
                      setState(() {
                        rangeStartDate = start;
                        rangeEndDate = end;
                      });
                    },
                  ),
                ),
              ],
            ),
            if (rangeStartDate != null && rangeEndDate != null)
              Text(
                'Range: ${_formatDate(rangeStartDate!)} - '
                '${_formatDate(rangeEndDate!)}',
                style: theme.typography.bodySmallMedium.copyWith(
                  color: theme.colors.content.contentSecondary,
                ),
              ),
          ],
        ),
      ),
    );
  }

  String _formatDate(DateTime date) {
    return '${date.day}/${date.month}/${date.year}';
  }
}
