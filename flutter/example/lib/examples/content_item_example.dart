import 'package:lemonade_design_system/lemonade_design_system.dart';
import 'package:lemonade_example/components.dart';
import 'package:lemonade_example/extensions.dart';

/// Example screen demonstrating the usage of [LemonadeContentItem].
class ContentItemExampleScreen extends StatelessWidget {
  /// Creates a new instance of [ContentItemExampleScreen].
  const ContentItemExampleScreen({super.key});

  @override
  Widget build(BuildContext context) {
    final theme = LemonadeTheme.of(context);

    return ExampleScaffold(
      title: 'Content Item Example',
      body: ColoredBox(
        color: theme.colors.background.bgSubtle,
        child: SingleChildScrollView(
          padding: EdgeInsets.all(theme.spaces.spacing600),
          child: ExampleSection(
            title: 'Content Item',
            children: [
              ...LemonadeContentItemOrientation.values.map(
                (orientation) => ExampleRow(
                  label: '${orientation.name} Content Item'.capitalize,
                  children: [
                    Padding(
                      padding: EdgeInsets.only(bottom: theme.spaces.spacing400),
                      child: SizedBox(
                        width: double.infinity,
                        child: LemonadeContentItem(
                          label: 'Contact name',
                          value: 'Joe Lime',
                          orientation: orientation,
                          addonSlot: const LemonadeTag(label: 'Addon'),
                        ),
                      ),
                    ),
                  ],
                ),
              ),

              ExampleRow(
                label: 'With Leading Item',
                children: [
                  SizedBox(
                    width: double.infinity,
                    child: LemonadeContentItem(
                      size: LemonadeContentItemSize.large,
                      leadingSlot: const LemonadeSymbolContainer.text(
                        text: 'AC',
                      ),
                      label: 'To',
                      value: 'Acme Corporation',
                      orientation: LemonadeContentItemOrientation.vertical,
                      addonSlot: Text(
                        'HSBC • 12-34-56 • 99990101',
                        style: theme.typography.bodyMediumRegular.copyWith(
                          color: theme.colors.content.contentSecondary,
                        ),
                      ),
                    ),
                  ),

                  const SizedBox(
                    width: double.infinity,
                    child: LemonadeContentItem(
                      leadingSlot: LemonadeIcon(icon: LemonadeIcons.heart),
                      label: 'Contact name',
                      value: 'Joe Lime',
                      addonSlot: LemonadeTag(label: 'Addon'),
                    ),
                  ),
                ],
              ),

              ExampleRow(
                label: 'With Trailing Item',
                children: [
                  SizedBox(
                    width: double.infinity,
                    child: LemonadeContentItem(
                      size: LemonadeContentItemSize.large,
                      label: 'Vendor',
                      value: 'Acme Corporation',
                      trailingSlot: LemonadeIcon(
                        icon: LemonadeIcons.pencilLine,
                        color: theme.colors.content.contentBrand,
                      ),
                      orientation: LemonadeContentItemOrientation.vertical,
                      addonSlot: Text(
                        'HSBC • 12-34-56 • 99990101',
                        style: theme.typography.bodyMediumRegular.copyWith(
                          color: theme.colors.content.contentSecondary,
                        ),
                      ),
                      onPressed: () => {
                        debugPrint('Content Item Pressed!'),
                      },
                    ),
                  ),
                ],
              ),

              ExampleRow(
                label: 'Custom value style',
                children: [
                  SizedBox(
                    width: double.infinity,
                    child: LemonadeContentItem(
                      label: 'Label',
                      value: 'Custom',
                      valueStyle: TextStyle(
                        color: theme.colors.content.contentCritical,
                      ),
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
