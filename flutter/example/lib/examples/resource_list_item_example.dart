import 'package:lemonade_design_system/lemonade_design_system.dart';
import 'package:lemonade_example/components.dart';

/// Example screen demonstrating the usage of [LemonadeResourceListItem].
class ResourceListItemScreen extends StatefulWidget {
  /// Creates a new instance of [ResourceListItemScreen].
  const ResourceListItemScreen({super.key});

  @override
  State<ResourceListItemScreen> createState() => _ResourceListItemScreenState();
}

class _ResourceListItemScreenState extends State<ResourceListItemScreen> {
  @override
  Widget build(BuildContext context) {
    final theme = LemonadeTheme.of(context);

    return ExampleScaffold(
      title: 'Resource List Item Example',
      body: ColoredBox(
        color: theme.colors.background.bgSubtle,
        child: SingleChildScrollView(
          padding: EdgeInsets.all(theme.spaces.spacing400),
          child: Column(
            children: [
              ExampleRow(
                label: 'With addon',
                children: [
                  LemonadeCard(
                    child: Column(
                      children: [
                        ...List.generate(
                          3,
                          (it) => LemonadeResourceListItem(
                            leadingSlot: (_) =>
                                const LemonadeSymbolContainer.custom(
                                  child: LemonadeBrandLogo(
                                    logo: LemonadeBrandLogos.mastercard,
                                  ),
                                ),
                            label: 'Credit ···· 9074',
                            description: '18:25 • Camden Corner',
                            value: '£64.25',
                            addonSlot: (_) => const LemonadeTag(
                              label: 'Approved',
                              voice: LemonadeTagVoice.positive,
                              icon: LemonadeIcons.circleCheck,
                            ),
                            showDivider: it < 2,
                            onPressed: () => {},
                          ),
                        ),
                      ],
                    ),
                  ),
                ],
              ),
              SizedBox(height: theme.spaces.spacing600),
              ExampleRow(
                label: 'Without addon',
                children: [
                  LemonadeCard(
                    child: Column(
                      children: [
                        ...List.generate(
                          3,
                          (it) => LemonadeResourceListItem(
                            leadingSlot: (_) =>
                                const LemonadeSymbolContainer.custom(
                                  child: LemonadeBrandLogo(
                                    logo: LemonadeBrandLogos.mastercard,
                                  ),
                                ),
                            label: 'Credit ···· 9074',
                            description: '18:25 • Camden Corner',
                            value: '£64.25',
                            showDivider: it < 2,
                            onPressed: () => {},
                          ),
                        ),
                      ],
                    ),
                  ),
                ],
              ),
              SizedBox(height: theme.spaces.spacing600),
              ExampleRow(
                label: 'Disabled',
                children: [
                  LemonadeCard(
                    child: Column(
                      children: [
                        ...List.generate(
                          3,
                          (it) => LemonadeResourceListItem(
                            leadingSlot: (_) =>
                                const LemonadeSymbolContainer.custom(
                                  child: LemonadeBrandLogo(
                                    logo: LemonadeBrandLogos.mastercard,
                                  ),
                                ),
                            label: 'Credit ···· 9074',
                            description: '18:25 • Camden Corner',
                            value: '£64.25',
                            addonSlot: (_) => const LemonadeTag(
                              label: 'Approved',
                              voice: LemonadeTagVoice.positive,
                              icon: LemonadeIcons.circleCheck,
                            ),
                            showDivider: it < 2,
                            onPressed: () => {},
                            enabled: false,
                          ),
                        ),
                      ],
                    ),
                  ),
                ],
              ),
            ],
          ),
        ),
      ),
    );
  }
}
