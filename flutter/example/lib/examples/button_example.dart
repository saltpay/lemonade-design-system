import 'package:lemonade_design_system/lemonade_design_system.dart';
import 'package:lemonade_example/components.dart';
import 'package:lemonade_example/extensions.dart';

/// Example screen demonstrating the usage of [LemonadeButton].
class ButtonExampleScreen extends StatelessWidget {
  /// Creates a new instance of [ButtonExampleScreen].
  const ButtonExampleScreen({super.key});

  @override
  Widget build(BuildContext context) {
    final theme = LemonadeTheme.of(context);

    return ExampleScaffold(
      title: 'Button Example',
      body: SingleChildScrollView(
        padding: EdgeInsets.all(theme.spaces.spacing600),
        child: ExampleSection(
          title: 'Button',
          children: [
            ExampleRow(
              label: 'Variants',
              children: LemonadeButtonVariant.values.map((variant) {
                return LemonadeButton(
                  label: variant.name.capitalize,
                  variant: variant,
                  onPressed: () {},
                );
              }).toList(),
            ),
            ExampleRow(
              label: 'Sizes',
              children: LemonadeButtonSize.values.map((size) {
                return LemonadeButton(
                  label: size.name.capitalize,
                  size: size,
                  onPressed: () {},
                );
              }).toList(),
            ),
            ExampleRow(
              label: 'With Icons',
              children: [
                LemonadeButton(
                  label: 'Leading',
                  leadingIcon: LemonadeIcons.heart,
                  onPressed: () {},
                ),
                LemonadeButton(
                  label: 'Trailing',
                  trailingIcon: LemonadeIcons.arrowRight,
                  onPressed: () {},
                ),
              ],
            ),
            ExampleRow(
              label: 'Loading',
              children: LemonadeButtonVariant.values.map((variant) {
                return LemonadeButton(
                  label: 'Loading',
                  variant: variant,
                  loading: true,
                  onPressed: () {},
                );
              }).toList(),
            ),
            ExampleRow(
              label: 'Disabled',
              children: [
                LemonadeButton(
                  label: 'Disabled',
                  enabled: false,
                  onPressed: () {},
                ),
              ],
            ),
          ],
        ),
      ),
    );
  }
}
