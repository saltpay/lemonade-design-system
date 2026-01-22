import 'package:lemonade_design_system/lemonade_design_system.dart';
import 'package:lemonade_example/components.dart';

/// Example screen demonstrating the usage of [LemonadeIcon].
class IconsExampleScreen extends StatelessWidget {
  /// Creates a new instance of [IconsExampleScreen].
  const IconsExampleScreen({super.key});

  @override
  Widget build(BuildContext context) {
    final theme = LemonadeTheme.of(context);

    return ExampleScaffold(
      title: 'Icon Example',
      body: SingleChildScrollView(
        padding: EdgeInsets.all(theme.spaces.spacing600),
        child: ExampleSection(
          title: 'Icon',
          children: [
            ExampleRow(
              label: 'Different sizes',
              children: List.of(
                LemonadeIconSize.values.map(
                  (size) => LemonadeIcon(
                    icon: LemonadeIcons.search,
                    size: size,
                  ),
                ),
              ),
            ),
            ExampleRow(
              label: 'Colored icons',
              children: [
                LemonadeIcon(
                  icon: LemonadeIcons.circleCheck,
                  color: theme.colors.content.contentPositive,
                  size: LemonadeIconSize.large,
                ),
                LemonadeIcon(
                  icon: LemonadeIcons.circleCheck,
                  color: theme.colors.content.contentCritical,
                  size: LemonadeIconSize.large,
                ),
                LemonadeIcon(
                  icon: LemonadeIcons.circleCheck,
                  color: theme.colors.content.contentInfo,
                  size: LemonadeIconSize.large,
                ),
                LemonadeIcon(
                  icon: LemonadeIcons.circleCheck,
                  color: theme.colors.content.contentCaution,
                  size: LemonadeIconSize.large,
                ),
                LemonadeIcon(
                  icon: LemonadeIcons.circleCheck,
                  color: theme.colors.content.contentBrand,
                  size: LemonadeIconSize.large,
                ),
              ],
            ),
            ExampleGridView(
              label: 'All icons',
              count: 3,
              children: List.of(
                LemonadeIcons.values.map(
                  (icon) => Container(
                    padding: EdgeInsets.symmetric(
                      horizontal: theme.spaces.spacing300,
                    ),
                    decoration: BoxDecoration(
                      border: Border.all(
                        color: theme.colors.border.borderNeutralLow,
                      ),
                      borderRadius: BorderRadius.circular(
                        theme.radius.radius400,
                      ),
                    ),
                    child: Column(
                      mainAxisAlignment: MainAxisAlignment.center,
                      spacing: theme.spaces.spacing200,
                      children: [
                        LemonadeIcon(
                          icon: icon,
                          size: LemonadeIconSize.large,
                        ),
                        Text(
                          icon.name,
                          overflow: TextOverflow.ellipsis,
                          style: theme.typography.bodySmallRegular.copyWith(
                            color: theme.colors.content.contentSecondary,
                          ),
                        ),
                      ],
                    ),
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
