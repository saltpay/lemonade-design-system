import 'package:lemonade_design_system/lemonade_design_system.dart';
import 'package:lemonade_example/components.dart';

/// Example screen demonstrating the usage of [LemonadeToast].
class ToastExampleScreen extends StatelessWidget {
  /// Creates a new instance of [ToastExampleScreen].
  const ToastExampleScreen({super.key});

  @override
  Widget build(BuildContext context) {
    final theme = LemonadeTheme.of(context);

    return ExampleScaffold(
      title: 'Toast Example',
      body: SingleChildScrollView(
        padding: EdgeInsets.all(theme.spaces.spacing600),
        child: ExampleSection(
          title: 'Toast',
          children: [
            const ExampleRow(
              label: 'Visual Components',
              children: [
                LemonadeToast.success(
                  label: 'Success message',
                ),
                LemonadeToast.error(
                  label: 'Error message',
                ),
                LemonadeToast.neutral(
                  label: 'Neutral message',
                ),
                LemonadeToast.neutral(
                  label:
                      // ignore: lines_longer_than_80_chars
                      'Really long label that should wrap onto multiple lines to demonstrate text wrapping in the toast component',
                ),
              ],
            ),
            const ExampleRow(
              label: 'Custom Icons (Neutral)',
              children: [
                LemonadeToast.neutral(
                  label: 'Your link is ready',
                  icon: LemonadeIcons.heart,
                ),
                LemonadeToast.neutral(
                  label: 'New notification',
                  icon: LemonadeIcons.circleInfo,
                ),
                LemonadeToast.neutral(
                  label: 'Changes saved',
                  icon: LemonadeIcons.circleCheck,
                ),
              ],
            ),
            ExampleRow(
              label: 'Interactive Toasts',
              children: [
                LemonadeButton(
                  label: 'Show Success',
                  onPressed: () {
                    LemonadeToastManager.show(
                      context,
                      voice: LemonadeToastVoice.success,
                      message: 'Changes saved successfully!',
                    );
                  },
                ),
                LemonadeButton(
                  label: 'Show Error',
                  variant: LemonadeButtonVariant.criticalSolid,
                  onPressed: () {
                    LemonadeToastManager.show(
                      context,
                      voice: LemonadeToastVoice.error,
                      message: 'Something went wrong',
                    );
                  },
                ),
                LemonadeButton(
                  label: 'Show Neutral',
                  variant: LemonadeButtonVariant.neutralSubtle,
                  onPressed: () {
                    LemonadeToastManager.show(
                      context,
                      voice: LemonadeToastVoice.neutral,
                      message: 'Your session will expire soon',
                      icon: LemonadeIcons.circleAlert,
                    );
                  },
                ),
                LemonadeButton(
                  label: 'Show long label',
                  variant: LemonadeButtonVariant.neutralSubtle,
                  onPressed: () {
                    LemonadeToastManager.show(
                      context,
                      voice: LemonadeToastVoice.neutral,
                      message:
                          // ignore: lines_longer_than_80_chars
                          'Really long label that should wrap onto multiple lines to demonstrate text wrapping in the toast component',
                      icon: LemonadeIcons.circleAlert,
                      duration: LemonadeToastDuration.long,
                    );
                  },
                ),
              ],
            ),
            ExampleRow(
              label: 'Custom Duration',
              children: [
                LemonadeButton(
                  label: 'Short (3s)',
                  onPressed: () {
                    LemonadeToastManager.show(
                      context,
                      voice: LemonadeToastVoice.success,
                      message: 'Short duration toast',
                    );
                  },
                ),
                LemonadeButton(
                  label: 'Medium (6s)',
                  onPressed: () {
                    LemonadeToastManager.show(
                      context,
                      voice: LemonadeToastVoice.neutral,
                      message: 'Medium duration toast',
                      duration: LemonadeToastDuration.medium,
                    );
                  },
                ),
                LemonadeButton(
                  label: 'Long (9s)',
                  onPressed: () {
                    LemonadeToastManager.show(
                      context,
                      voice: LemonadeToastVoice.error,
                      message: 'Long duration toast',
                      duration: LemonadeToastDuration.long,
                    );
                  },
                ),
                LemonadeButton(
                  label: 'Custom (15s)',
                  onPressed: () {
                    LemonadeToastManager.show(
                      context,
                      voice: LemonadeToastVoice.neutral,
                      message: 'Custom 15 second toast',
                      duration: const Duration(seconds: 15),
                    );
                  },
                ),
              ],
            ),
            ExampleRow(
              label: 'Custom Icons',
              children: [
                LemonadeButton(
                  label: 'Heart Icon',
                  leadingIcon: LemonadeIcons.heart,
                  onPressed: () {
                    LemonadeToastManager.show(
                      context,
                      voice: LemonadeToastVoice.neutral,
                      message: 'Added to favorites',
                      icon: LemonadeIcons.heart,
                    );
                  },
                ),
                LemonadeButton(
                  label: 'Info Icon',
                  leadingIcon: LemonadeIcons.circleInfo,
                  onPressed: () {
                    LemonadeToastManager.show(
                      context,
                      voice: LemonadeToastVoice.neutral,
                      message: 'New update available',
                      icon: LemonadeIcons.circleInfo,
                    );
                  },
                ),
              ],
            ),
          ],
        ),
      ),
    );
  }
}
