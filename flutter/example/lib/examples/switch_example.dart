import 'package:lemonade_design_system/lemonade_design_system.dart';
import 'package:lemonade_example/components.dart';

/// Example screen demonstrating the usage of [LemonadeSwitch].
class SwitchExampleScreen extends StatefulWidget {
  /// Creates a new instance of [SwitchExampleScreen].
  const SwitchExampleScreen({super.key});

  @override
  State<SwitchExampleScreen> createState() => _SwitchExampleScreenState();
}

class _SwitchExampleScreenState extends State<SwitchExampleScreen> {
  bool switchValue = false;

  @override
  Widget build(BuildContext context) {
    final theme = LemonadeTheme.of(context);

    return ExampleScaffold(
      title: 'Switch Example',
      body: SingleChildScrollView(
        padding: EdgeInsets.all(theme.spaces.spacing600),
        child: ExampleSection(
          title: 'Switch',
          children: [
            ExampleRow(
              label: 'Enabled',
              children: [
                LemonadeSwitch(
                  checked: switchValue,
                  onCheckedChange: (bool newValue) {
                    setState(() {
                      switchValue = newValue;
                    });
                  },
                ),
              ],
            ),
            ExampleRow(
              label: 'Disabled',
              children: [
                const LemonadeSwitch(
                  checked: true,
                  enabled: false,
                ),
                SizedBox(height: theme.spaces.spacing500),
                const LemonadeSwitch(
                  checked: false,
                  enabled: false,
                ),
              ],
            ),
          ],
        ),
      ),
    );
  }
}
