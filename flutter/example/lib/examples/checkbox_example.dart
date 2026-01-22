import 'package:lemonade_design_system/lemonade_design_system.dart';
import 'package:lemonade_example/components.dart';

/// Example screen demonstrating the usage of [LemonadeCheckbox].
class CheckboxExampleScreen extends StatefulWidget {
  /// Creates a new instance of [CheckboxExampleScreen].
  const CheckboxExampleScreen({super.key});

  @override
  State<CheckboxExampleScreen> createState() => _CheckboxExampleScreenState();
}

class _CheckboxExampleScreenState extends State<CheckboxExampleScreen> {
  CheckboxStatus _option1 = CheckboxStatus.unchecked;
  CheckboxStatus _option2 = CheckboxStatus.checked;
  CheckboxStatus _option3 = CheckboxStatus.unchecked;

  void _toggleCheckbox(
    CheckboxStatus current,
    void Function(CheckboxStatus) update,
  ) {
    setState(() {
      update(
        current == CheckboxStatus.checked
            ? CheckboxStatus.unchecked
            : CheckboxStatus.checked,
      );
    });
  }

  @override
  Widget build(BuildContext context) {
    final theme = LemonadeTheme.of(context);

    return ExampleScaffold(
      title: 'Checkbox Example',
      body: SingleChildScrollView(
        padding: EdgeInsets.all(theme.spaces.spacing600),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            ExampleSection(
              title: 'With Label & Support Text',
              children: [
                ExampleRow(
                  label: 'Full checkbox examples',
                  children: [
                    LemonadeCheckbox(
                      status: _option1,
                      onChanged: () =>
                          _toggleCheckbox(_option1, (v) => _option1 = v),
                      label: 'Accept terms and conditions',
                      supportText: 'You must accept to continue',
                    ),
                    SizedBox(height: theme.spaces.spacing400),
                    LemonadeCheckbox(
                      status: _option2,
                      onChanged: () =>
                          _toggleCheckbox(_option2, (v) => _option2 = v),
                      label: 'Subscribe to newsletter',
                      supportText: 'Receive updates about new features',
                    ),
                    SizedBox(height: theme.spaces.spacing400),
                    LemonadeCheckbox(
                      status: _option3,
                      onChanged: () =>
                          _toggleCheckbox(_option3, (v) => _option3 = v),
                      label: 'Remember me',
                    ),
                    SizedBox(height: theme.spaces.spacing400),
                    LemonadeCheckbox(
                      status: CheckboxStatus.checked,
                      onChanged: () {},
                      label: 'Disabled option',
                      supportText: 'This option cannot be changed',
                      enabled: false,
                    ),
                  ],
                ),
              ],
            ),
            SizedBox(height: theme.spaces.spacing600),
            ExampleSection(
              title: 'All States',
              children: [
                ExampleRow(
                  label: 'Unchecked',
                  children: [
                    LemonadeCheckbox(
                      status: CheckboxStatus.unchecked,
                      onChanged: () {},
                      label: 'Label',
                    ),
                  ],
                ),
                ExampleRow(
                  label: 'Checked',
                  children: [
                    LemonadeCheckbox(
                      status: CheckboxStatus.checked,
                      onChanged: () {},
                      label: 'Label',
                    ),
                  ],
                ),
                ExampleRow(
                  label: 'Indeterminate',
                  children: [
                    LemonadeCheckbox(
                      status: CheckboxStatus.indeterminate,
                      onChanged: () {},
                      label: 'Label',
                    ),
                  ],
                ),
                ExampleRow(
                  label: 'Disabled Unchecked',
                  children: [
                    LemonadeCheckbox(
                      status: CheckboxStatus.unchecked,
                      enabled: false,
                      onChanged: () {},
                      label: 'Label',
                    ),
                  ],
                ),
                ExampleRow(
                  label: 'Disabled Checked',
                  children: [
                    LemonadeCheckbox(
                      status: CheckboxStatus.checked,
                      enabled: false,
                      onChanged: () {},
                      label: 'Label',
                    ),
                  ],
                ),
                ExampleRow(
                  label: 'Disabled Indeterminate',
                  children: [
                    LemonadeCheckbox(
                      status: CheckboxStatus.indeterminate,
                      enabled: false,
                      onChanged: () {},
                      label: 'Label',
                    ),
                  ],
                ),
              ],
            ),
          ],
        ),
      ),
    );
  }
}
