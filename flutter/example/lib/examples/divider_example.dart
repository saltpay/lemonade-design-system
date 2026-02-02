import 'package:lemonade_design_system/lemonade_design_system.dart';
import 'package:lemonade_example/components.dart';
import 'package:lemonade_example/extensions.dart';

/// Example screen demonstrating the usage of [LemonadeDivider].
class DividerExampleScreen extends StatelessWidget {
  /// Creates a new instance of [DividerExampleScreen].
  const DividerExampleScreen({super.key});

  @override
  Widget build(BuildContext context) {
    final theme = LemonadeTheme.of(context);

    return ExampleScaffold(
      title: 'Divider Example',
      body: ColoredBox(
        color: theme.colors.background.bgSubtle,
        child: SingleChildScrollView(
          padding: EdgeInsets.all(theme.spaces.spacing600),
          child: ExampleSection(
            title: 'Divider',
            children: [
              ...LemonadeDividerVariant.values.map(
                (variant) => ExampleRow(
                  label: '${variant.name} Divider'.capitalize,
                  children: [
                    LemonadeDivider(
                      variant: variant,
                    ),
                    LemonadeDivider(
                      variant: variant,
                      label: 'With label',
                    ),
                    SizedBox(
                      height: 200,
                      child: LemonadeDivider(
                        variant: variant,
                        orientation: LemonadeDividerOrientation.vertical,
                      ),
                    ),
                  ],
                ),
              ),
            ],
          ),
        ),
      ),
    );
  }
}
