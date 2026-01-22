import 'package:lemonade_design_system/lemonade_design_system.dart';
import 'package:lemonade_example/components.dart';

/// Example screen demonstrating the usage of [LemonadeBrandLogo].
class BrandLogoExampleScreen extends StatelessWidget {
  /// Creates a new instance of [BrandLogoExampleScreen].
  const BrandLogoExampleScreen({super.key});

  @override
  Widget build(BuildContext context) {
    final theme = LemonadeTheme.of(context);

    return ExampleScaffold(
      title: 'Brand Logo Example',
      body: SingleChildScrollView(
        padding: EdgeInsets.all(theme.spaces.spacing600),
        child: ExampleSection(
          title: 'Brand Logo',
          children: [
            ExampleRow(
              label: 'Different sizes',
              children: List.of(
                LemonadeBrandLogoSize.values.map(
                  (size) => LemonadeBrandLogo(
                    logo: LemonadeBrandLogos.visa,
                    size: size,
                  ),
                ),
              ),
            ),
            ExampleGridView(
              label: 'All logos',
              children: List.of(
                LemonadeBrandLogos.values.map(
                  (logo) => LemonadeBrandLogo(
                    logo: logo,
                    size: LemonadeBrandLogoSize.xxLarge,
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
