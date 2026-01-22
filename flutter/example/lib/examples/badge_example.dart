import 'package:lemonade_design_system/lemonade_design_system.dart';
import 'package:lemonade_example/components.dart';
import 'package:lemonade_example/extensions.dart';

/// Example screen demonstrating the usage of [LemonadeBadge].
class BadgeExampleScreen extends StatefulWidget {
  /// Creates a new instance of [BadgeExampleScreen].
  const BadgeExampleScreen({super.key});

  @override
  State<BadgeExampleScreen> createState() => _BadgeExampleScreenState();
}

class _BadgeExampleScreenState extends State<BadgeExampleScreen> {
  @override
  Widget build(BuildContext context) {
    final theme = LemonadeTheme.of(context);

    return ExampleScaffold(
      title: 'Badge Example',
      body: SingleChildScrollView(
        padding: EdgeInsets.all(theme.spaces.spacing600),
        child: ExampleSection(
          title: 'Badge',
          children: [
            ExampleRow(
              label: 'Sizes',
              children: LemonadeBadgeSize.values.map((size) {
                return LemonadeBadge(
                  label: '${size.name.capitalize} Badge',
                  size: size,
                  icon: LemonadeIcons.heart,
                );
              }).toList(),
            ),
          ],
        ),
      ),
    );
  }
}
