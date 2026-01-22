import 'package:lemonade_design_system/lemonade_design_system.dart';
import 'package:lemonade_example/components.dart';

/// Example screen demonstrating the usage of [LemonadeSpinner].
class SpinnerExampleScreen extends StatelessWidget {
  /// Creates a new instance of [SpinnerExampleScreen].
  const SpinnerExampleScreen({super.key});

  @override
  Widget build(BuildContext context) {
    final theme = LemonadeTheme.of(context);

    return ExampleScaffold(
      title: 'Spinner Example',
      body: SingleChildScrollView(
        padding: EdgeInsets.all(theme.spaces.spacing600),
        child: const ExampleSection(
          title: 'Spinner',
          children: [
            ExampleRow(
              label: 'Sizes',
              children: [
                LemonadeSpinner(size: LemonadeSpinnerSize.xSmall),
                LemonadeSpinner(size: LemonadeSpinnerSize.small),
                LemonadeSpinner(),
                LemonadeSpinner(size: LemonadeSpinnerSize.large),
                LemonadeSpinner(size: LemonadeSpinnerSize.xLarge),
                LemonadeSpinner(size: LemonadeSpinnerSize.xxLarge),
                LemonadeSpinner(size: LemonadeSpinnerSize.xxxLarge),
              ],
            ),
          ],
        ),
      ),
    );
  }
}
