import 'package:lemonade_design_system/lemonade_design_system.dart';
import 'package:lemonade_example/components.dart';

/// Example screen demonstrating the usage of [LemonadeChip].
class ChipExampleScreen extends StatefulWidget {
  /// Creates a new instance of [ChipExampleScreen].
  const ChipExampleScreen({super.key});

  @override
  State<ChipExampleScreen> createState() => _ChipExampleScreenState();
}

class _ChipExampleScreenState extends State<ChipExampleScreen> {
  bool _selected = false;

  @override
  Widget build(BuildContext context) {
    final theme = LemonadeTheme.of(context);

    return ExampleScaffold(
      title: 'Chip Example',
      body: SingleChildScrollView(
        padding: EdgeInsets.all(theme.spaces.spacing600),
        child: ExampleSection(
          title: 'Chip',
          children: [
            ExampleRow(
              label: 'States',
              children: [
                LemonadeChip(
                  label: 'Unselected',
                  onTap: () {},
                ),
                LemonadeChip(
                  label: 'Selected',
                  selected: true,
                  onTap: () {},
                ),
              ],
            ),
            ExampleRow(
              label: 'Interactive',
              children: [
                LemonadeChip(
                  label: _selected ? 'Selected' : 'Tap me',
                  selected: _selected,
                  onTap: () => setState(() => _selected = !_selected),
                ),
              ],
            ),
            ExampleRow(
              label: 'With Icons',
              children: [
                LemonadeChip(
                  label: 'Leading',
                  leadingIcon: LemonadeIcons.heart,
                  onTap: () {},
                ),
                LemonadeChip(
                  label: 'Trailing',
                  trailingIcon: LemonadeIcons.chevronDown,
                  onTap: () {},
                  onTrailingIconTap: () {},
                ),
              ],
            ),
            ExampleRow(
              label: 'With Counter',
              children: [
                LemonadeChip(
                  label: 'Filter',
                  selected: true,
                  counter: 5,
                  onTap: () {},
                ),
              ],
            ),
            const ExampleRow(
              label: 'Disabled',
              children: [
                LemonadeChip(
                  label: 'Disabled',
                  enabled: false,
                ),
              ],
            ),
          ],
        ),
      ),
    );
  }
}
