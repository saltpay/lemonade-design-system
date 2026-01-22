import 'package:lemonade_design_system/lemonade_design_system.dart';
import 'package:lemonade_example/components.dart';

/// Example screen demonstrating the usage of [LemonadeTile].
class TileExampleScreen extends StatefulWidget {
  /// Creates a new instance of [TileExampleScreen].
  const TileExampleScreen({super.key});

  @override
  State<TileExampleScreen> createState() => _TileExampleScreenState();
}

class _TileExampleScreenState extends State<TileExampleScreen> {
  @override
  Widget build(BuildContext context) {
    final theme = LemonadeTheme.of(context);

    return ExampleScaffold(
      title: 'Tile Example',
      body: SingleChildScrollView(
        padding: EdgeInsets.all(theme.spaces.spacing600),
        child: ExampleSection(
          title: 'Tile',
          children: [
            const ExampleRow(
              label: 'Neutral',
              children: [
                LemonadeTile(
                  leadingIcon: LemonadeIcons.heart,
                  label: 'Enabled Tile with really long text',
                ),
                LemonadeTile(
                  leadingIcon: LemonadeIcons.heart,
                  label: 'With Add-on',
                  addOnSlot: LemonadeBadge(label: 'Label'),
                ),
                LemonadeTile(
                  leadingIcon: LemonadeIcons.heart,
                  label: 'Disabled',
                  enabled: false,
                ),
                LemonadeTile(
                  leadingIcon: LemonadeIcons.heart,
                  label: 'Disabled + Add-on',
                  addOnSlot: LemonadeBadge(label: 'Label'),
                  enabled: false,
                ),
              ],
            ),
            const ExampleRow(
              label: 'Muted',
              children: [
                LemonadeTile(
                  leadingIcon: LemonadeIcons.heart,
                  label: 'Enabled',
                  variant: LemonadeTileVariants.muted,
                ),
                LemonadeTile(
                  leadingIcon: LemonadeIcons.heart,
                  label: 'Enabled + Add-on',
                  addOnSlot: LemonadeBadge(label: 'Label'),
                  variant: LemonadeTileVariants.muted,
                ),
                LemonadeTile(
                  leadingIcon: LemonadeIcons.heart,
                  label: 'Disabled',
                  variant: LemonadeTileVariants.muted,
                  enabled: false,
                ),
                LemonadeTile(
                  leadingIcon: LemonadeIcons.heart,
                  label: 'Disabled + Add-on',
                  addOnSlot: LemonadeBadge(label: 'Label'),
                  variant: LemonadeTileVariants.muted,
                  enabled: false,
                ),
              ],
            ),
            ExampleRow(
              label: 'On Color',
              children: [
                Container(
                  padding: EdgeInsets.all(theme.spaces.spacing400),
                  decoration: BoxDecoration(
                    color: theme.colors.background.bgBrand,
                    borderRadius: BorderRadius.circular(theme.radius.radius300),
                  ),
                  width: double.infinity,
                  child: Wrap(
                    spacing: theme.spaces.spacing400,
                    runSpacing: theme.spaces.spacing400,
                    children: const [
                      LemonadeTile(
                        leadingIcon: LemonadeIcons.heart,
                        label: 'Enabled',
                        variant: LemonadeTileVariants.onBrand,
                      ),
                      LemonadeTile(
                        leadingIcon: LemonadeIcons.heart,
                        label: 'Disabled',
                        variant: LemonadeTileVariants.onBrand,
                        enabled: false,
                      ),
                    ],
                  ),
                ),
              ],
            ),
          ],
        ),
      ),
    );
  }
}
