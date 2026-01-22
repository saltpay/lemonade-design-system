import 'package:lemonade_design_system/lemonade_design_system.dart';
import 'package:lemonade_example/components.dart';

enum _ViewMode { list, grid, table }

enum _IconViewMode { list, grid }

enum _TimeRange { day, week }

/// Example screen demonstrating the usage of [LemonadeSegmentedControl].
class SegmentedControlExampleScreen extends StatefulWidget {
  /// Creates a new instance of [SegmentedControlExampleScreen].
  const SegmentedControlExampleScreen({super.key});

  @override
  State<SegmentedControlExampleScreen> createState() =>
      _SegmentedControlExampleScreenState();
}

class _SegmentedControlExampleScreenState
    extends State<SegmentedControlExampleScreen> {
  _ViewMode _selectedViewMode = _ViewMode.list;
  _IconViewMode _selectedIconViewMode = _IconViewMode.list;
  _TimeRange _selectedTimeRange = _TimeRange.week;

  @override
  Widget build(BuildContext context) {
    final theme = LemonadeTheme.of(context);

    return ExampleScaffold(
      title: 'Segmented Control Example',
      body: SingleChildScrollView(
        padding: EdgeInsets.all(theme.spaces.spacing600),
        child: ExampleSection(
          title: 'Segmented Control',
          children: [
            ExampleRow(
              label: '2 segments',
              children: [
                LemonadeSegmentedControl<_TimeRange>(
                  items: const [
                    LemonadeSegmentItem(value: _TimeRange.day, label: 'Day'),
                    LemonadeSegmentItem(value: _TimeRange.week, label: 'Week'),
                  ],
                  selectedValue: _selectedTimeRange,
                  onChanged: (value) {
                    setState(() {
                      _selectedTimeRange = value;
                    });
                  },
                ),
              ],
            ),
            SizedBox(height: theme.spaces.spacing400),
            ExampleRow(
              label: '3 segments',
              children: [
                LemonadeSegmentedControl<_ViewMode>(
                  items: const [
                    LemonadeSegmentItem(value: _ViewMode.list, label: 'List'),
                    LemonadeSegmentItem(value: _ViewMode.grid, label: 'Grid'),
                    LemonadeSegmentItem(value: _ViewMode.table, label: 'Table'),
                  ],
                  selectedValue: _selectedViewMode,
                  onChanged: (value) {
                    setState(() {
                      _selectedViewMode = value;
                    });
                  },
                ),
              ],
            ),
            SizedBox(height: theme.spaces.spacing400),
            ExampleRow(
              label: 'With icons',
              children: [
                LemonadeSegmentedControl<_IconViewMode>(
                  items: const [
                    LemonadeSegmentItem(
                      value: _IconViewMode.list,
                      label: 'List',
                      leadingIcon: LemonadeIcons.list,
                    ),
                    LemonadeSegmentItem(
                      value: _IconViewMode.grid,
                      label: 'Grid',
                      leadingIcon: LemonadeIcons.grid,
                    ),
                  ],
                  selectedValue: _selectedIconViewMode,
                  onChanged: (value) {
                    setState(() {
                      _selectedIconViewMode = value;
                    });
                  },
                ),
              ],
            ),
            SizedBox(height: theme.spaces.spacing400),
            ExampleRow(
              label: 'Disabled',
              children: [
                LemonadeSegmentedControl<_ViewMode>(
                  items: const [
                    LemonadeSegmentItem(value: _ViewMode.list, label: 'List'),
                    LemonadeSegmentItem(value: _ViewMode.grid, label: 'Grid'),
                  ],
                  selectedValue: _ViewMode.list,
                  isEnabled: false,
                  onChanged: (_) {},
                ),
              ],
            ),
          ],
        ),
      ),
    );
  }
}
