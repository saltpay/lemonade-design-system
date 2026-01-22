import 'package:lemonade_design_system/lemonade_design_system.dart';
import 'package:lemonade_example/components.dart';

/// Example screen demonstrating the usage of [LemonadeRadioButton].
class RadioButtonExampleScreen extends StatefulWidget {
  /// Creates a new instance of [RadioButtonExampleScreen].
  const RadioButtonExampleScreen({super.key});

  @override
  State<RadioButtonExampleScreen> createState() =>
      _RadioButtonExampleScreenState();
}

class _RadioButtonExampleScreenState extends State<RadioButtonExampleScreen> {
  int _selectedOption = 0;
  int _selectedPayment = 1;

  @override
  Widget build(BuildContext context) {
    final theme = LemonadeTheme.of(context);

    return ExampleScaffold(
      title: 'Radio Button Example',
      body: SingleChildScrollView(
        padding: EdgeInsets.all(theme.spaces.spacing600),
        child: Column(
          children: [
            ExampleSection(
              title: 'Interactive',
              children: [
                ExampleRow(
                  label: 'Select a plan',
                  children: [
                    Column(
                      crossAxisAlignment: CrossAxisAlignment.start,
                      children: [
                        LemonadeRadioButton(
                          checked: _selectedOption == 0,
                          onChanged: () => setState(() => _selectedOption = 0),
                          label: 'Free Plan',
                          supportText: 'Basic features for personal use',
                        ),
                        SizedBox(height: theme.spaces.spacing300),
                        LemonadeRadioButton(
                          checked: _selectedOption == 1,
                          onChanged: () => setState(() => _selectedOption = 1),
                          label: 'Pro Plan',
                          supportText: 'Advanced features for professionals',
                        ),
                        SizedBox(height: theme.spaces.spacing300),
                        LemonadeRadioButton(
                          checked: _selectedOption == 2,
                          onChanged: () => setState(() => _selectedOption = 2),
                          label: 'Enterprise Plan',
                          supportText: 'Full access with priority support',
                        ),
                      ],
                    ),
                  ],
                ),
              ],
            ),
            SizedBox(height: theme.spaces.spacing600),
            ExampleSection(
              title: 'Without Support Text',
              children: [
                ExampleRow(
                  label: 'Payment method',
                  children: [
                    Column(
                      crossAxisAlignment: CrossAxisAlignment.start,
                      children: [
                        LemonadeRadioButton(
                          checked: _selectedPayment == 0,
                          onChanged: () => setState(() => _selectedPayment = 0),
                          label: 'Credit Card',
                        ),
                        SizedBox(height: theme.spaces.spacing300),
                        LemonadeRadioButton(
                          checked: _selectedPayment == 1,
                          onChanged: () => setState(() => _selectedPayment = 1),
                          label: 'Bank Transfer',
                        ),
                        SizedBox(height: theme.spaces.spacing300),
                        LemonadeRadioButton(
                          checked: _selectedPayment == 2,
                          onChanged: () => setState(() => _selectedPayment = 2),
                          label: 'PayPal',
                        ),
                      ],
                    ),
                  ],
                ),
              ],
            ),
            SizedBox(height: theme.spaces.spacing600),
            ExampleSection(
              title: 'States',
              children: [
                ExampleRow(
                  label: 'Unchecked',
                  children: [
                    LemonadeRadioButton(
                      checked: false,
                      onChanged: () {},
                      label: 'Option',
                      supportText: 'Support text',
                    ),
                  ],
                ),
                ExampleRow(
                  label: 'Checked',
                  children: [
                    LemonadeRadioButton(
                      checked: true,
                      onChanged: () {},
                      label: 'Option',
                      supportText: 'Support text',
                    ),
                  ],
                ),
                ExampleRow(
                  label: 'Disabled Unchecked',
                  children: [
                    LemonadeRadioButton(
                      checked: false,
                      enabled: false,
                      onChanged: () {},
                      label: 'Option',
                      supportText: 'Support text',
                    ),
                  ],
                ),
                ExampleRow(
                  label: 'Disabled Checked',
                  children: [
                    LemonadeRadioButton(
                      checked: true,
                      enabled: false,
                      onChanged: () {},
                      label: 'Option',
                      supportText: 'Support text',
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
