import 'package:lemonade_design_system/lemonade_design_system.dart';
import 'package:lemonade_example/components.dart';
import 'package:lemonade_example/extensions.dart';

/// Example screen demonstrating the usage of [LemonadeTag].
class TagExampleScreen extends StatelessWidget {
  /// Creates a new instance of [TagExampleScreen].
  const TagExampleScreen({super.key});

  LemonadeIcons _getIconByVariant(LemonadeTagVoice voice) {
    return switch (voice) {
      LemonadeTagVoice.neutral => LemonadeIcons.heart,
      LemonadeTagVoice.critical => LemonadeIcons.circleX,
      LemonadeTagVoice.warning => LemonadeIcons.triangleAlert,
      LemonadeTagVoice.info => LemonadeIcons.circleInfo,
      LemonadeTagVoice.positive => LemonadeIcons.circleCheck,
    };
  }

  @override
  Widget build(BuildContext context) {
    final theme = LemonadeTheme.of(context);

    return ExampleScaffold(
      title: 'Tag Example',
      body: SingleChildScrollView(
        padding: EdgeInsets.all(theme.spaces.spacing600),
        child: ExampleSection(
          title: 'Tag',
          children: [
            ExampleRow(
              label: 'Voice Variants',
              children: LemonadeTagVoice.values.map((voice) {
                return LemonadeTag(
                  label: voice.name.capitalize,
                  voice: voice,
                );
              }).toList(),
            ),
            ExampleRow(
              label: 'With Icons',
              children: LemonadeTagVoice.values.map((voice) {
                return LemonadeTag(
                  label: voice.name.capitalize,
                  voice: voice,
                  icon: _getIconByVariant(voice),
                );
              }).toList(),
            ),
          ],
        ),
      ),
    );
  }
}
