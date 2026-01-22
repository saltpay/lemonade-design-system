import 'package:lemonade_design_system/lemonade_design_system.dart';
import 'package:lemonade_example/components.dart';

/// Example screen demonstrating the usage of [LemonadeCountryFlag].
class CountryFlagExampleScreen extends StatelessWidget {
  /// Creates a new instance of [CountryFlagExampleScreen].
  const CountryFlagExampleScreen({super.key});

  @override
  Widget build(BuildContext context) {
    final theme = LemonadeTheme.of(context);

    return ExampleScaffold(
      title: 'Country Flag Example',
      body: SingleChildScrollView(
        padding: EdgeInsets.all(theme.spaces.spacing600),
        child: ExampleSection(
          title: 'Country Flag',
          children: [
            ExampleRow(
              label: 'Different sizes',
              children: List.of(
                LemonadeFlagSize.values.map(
                  (size) => LemonadeCountryFlag(
                    flag: LemonadeFlags.gbENGEngland,
                    size: size,
                  ),
                ),
              ),
            ),
            ExampleGridView(
              label: 'All flags',
              children: List.of(
                LemonadeFlags.values.map(
                  (flag) => LemonadeCountryFlag(
                    flag: flag,
                    size: LemonadeFlagSize.xxxLarge,
                  ),
                ),
              ),
            ),
          ],
        ),
      ),
    );
  }
}
