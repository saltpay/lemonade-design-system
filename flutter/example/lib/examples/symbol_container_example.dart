import 'package:lemonade_design_system/lemonade_design_system.dart';
import 'package:lemonade_example/components.dart';

/// Example screen demonstrating the usage of [LemonadeSymbolContainer].
class SymbolContainerExampleScreen extends StatelessWidget {
  /// Creates a new instance of [SymbolContainerExampleScreen].
  const SymbolContainerExampleScreen({super.key});

  @override
  Widget build(BuildContext context) {
    final theme = LemonadeTheme.of(context);

    return ExampleScaffold(
      title: 'Symbol Container Example',
      body: SingleChildScrollView(
        padding: EdgeInsets.all(theme.spaces.spacing600),
        child: const ExampleSection(
          title: 'Symbol Container',
          children: [
            ExampleRow(
              label: 'With Icon',
              children: [
                LemonadeSymbolContainer.icon(
                  icon: LemonadeIcons.heart,
                  size: LemonadeSymbolContainerSize.xSmall,
                ),
                LemonadeSymbolContainer.icon(
                  icon: LemonadeIcons.heart,
                  size: LemonadeSymbolContainerSize.small,
                ),
                LemonadeSymbolContainer.icon(
                  icon: LemonadeIcons.heart,
                ),
                LemonadeSymbolContainer.icon(
                  icon: LemonadeIcons.heart,
                  size: LemonadeSymbolContainerSize.large,
                ),
                LemonadeSymbolContainer.icon(
                  icon: LemonadeIcons.heart,
                  size: LemonadeSymbolContainerSize.xLarge,
                ),
              ],
            ),
            ExampleRow(
              label: 'With Text',
              children: [
                LemonadeSymbolContainer.text(
                  text: 'A',
                  size: LemonadeSymbolContainerSize.small,
                ),
                LemonadeSymbolContainer.text(
                  text: 'B',
                ),
                LemonadeSymbolContainer.text(
                  text: 'C',
                  size: LemonadeSymbolContainerSize.large,
                ),
              ],
            ),
            ExampleRow(
              label: 'Voice Variants',
              children: [
                LemonadeSymbolContainer.icon(
                  icon: LemonadeIcons.heart,
                ),
                LemonadeSymbolContainer.icon(
                  icon: LemonadeIcons.triangleAlert,
                  voice: LemonadeSymbolContainerVoice.critical,
                ),
                LemonadeSymbolContainer.icon(
                  icon: LemonadeIcons.triangleAlert,
                  voice: LemonadeSymbolContainerVoice.warning,
                ),
                LemonadeSymbolContainer.icon(
                  icon: LemonadeIcons.circleInfo,
                  voice: LemonadeSymbolContainerVoice.info,
                ),
                LemonadeSymbolContainer.icon(
                  icon: LemonadeIcons.check,
                  voice: LemonadeSymbolContainerVoice.positive,
                ),
                LemonadeSymbolContainer.icon(
                  icon: LemonadeIcons.heart,
                  voice: LemonadeSymbolContainerVoice.brand,
                ),
              ],
            ),
          ],
        ),
      ),
    );
  }
}
