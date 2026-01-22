import 'package:lemonade_design_system/lemonade_design_system.dart';
import 'package:lemonade_example/components.dart';

/// Example screen demonstrating the usage of [LemonadeCard].
class CardExampleScreen extends StatelessWidget {
  /// Creates a new instance of [CardExampleScreen].
  const CardExampleScreen({super.key});

  @override
  Widget build(BuildContext context) {
    final theme = LemonadeTheme.of(context);

    return ExampleScaffold(
      title: 'Card Example',
      body: ColoredBox(
        color: theme.colors.background.bgSubtle,
        child: SingleChildScrollView(
          padding: EdgeInsets.all(theme.spaces.spacing600),
          child: ExampleSection(
            title: 'Card',
            children: [
              ExampleRow(
                label: 'Basic Card',
                children: [
                  SizedBox(
                    width: double.infinity,
                    child: LemonadeCard(
                      padding: LemonadeCardPadding.medium,
                      child: Text(
                        'Card content goes here',
                        style: theme.typography.bodyMediumRegular.apply(
                          color: theme.colors.content.contentPrimary,
                        ),
                      ),
                    ),
                  ),
                ],
              ),
              ExampleRow(
                label: 'With Header',
                children: [
                  SizedBox(
                    width: double.infinity,
                    child: LemonadeCard(
                      header: const LemonadeCardHeader(title: 'Card Title'),
                      padding: LemonadeCardPadding.medium,
                      child: Text(
                        'Card content with header',
                        style: theme.typography.bodyMediumRegular.apply(
                          color: theme.colors.content.contentPrimary,
                        ),
                      ),
                    ),
                  ),
                ],
              ),
              ExampleRow(
                label: 'Header with Trailing',
                children: [
                  SizedBox(
                    width: double.infinity,
                    child: LemonadeCard(
                      header: const LemonadeCardHeader(
                        title: 'Card Title',
                        trailing: LemonadeTag(
                          label: 'New',
                          voice: LemonadeTagVoice.positive,
                        ),
                      ),
                      padding: LemonadeCardPadding.medium,
                      child: Text(
                        'Card with trailing tag',
                        style: theme.typography.bodyMediumRegular.apply(
                          color: theme.colors.content.contentPrimary,
                        ),
                      ),
                    ),
                  ),
                ],
              ),
              ExampleRow(
                label: 'Background Variants',
                children: [
                  SizedBox(
                    width: double.infinity,
                    child: LemonadeCard(
                      padding: LemonadeCardPadding.medium,
                      child: Text(
                        'Default',
                        style: theme.typography.bodyMediumRegular.apply(
                          color: theme.colors.content.contentPrimary,
                        ),
                      ),
                    ),
                  ),
                  Container(
                    color: theme.colors.background.bgDefault,
                    width: double.infinity,
                    padding: EdgeInsets.all(theme.spaces.spacing200),
                    child: LemonadeCard(
                      background: LemonadeCardBackground.subtle,
                      padding: LemonadeCardPadding.medium,
                      child: Text(
                        'Subtle',
                        style: theme.typography.bodyMediumRegular.apply(
                          color: theme.colors.content.contentPrimary,
                        ),
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
