import 'package:lemonade_design_system/lemonade_design_system.dart';
import 'package:lemonade_example/components.dart';

/// Example screen demonstrating the usage of [LemonadeTextField].
class TextFieldExampleScreen extends StatelessWidget {
  /// Creates a new instance of [TextFieldExampleScreen].
  const TextFieldExampleScreen({super.key});

  @override
  Widget build(BuildContext context) {
    final theme = LemonadeTheme.of(context);
    var obscureText = true;

    return ExampleScaffold(
      title: 'TextField Example',
      body: SingleChildScrollView(
        padding: EdgeInsets.all(theme.spaces.spacing600),
        child: ExampleSection(
          title: 'TextField',
          children: [
            const ExampleColumn(
              label: 'Basic',
              children: [
                LemonadeTextField(
                  label: 'Label',
                  placeholder: 'Enter text...',
                ),
              ],
            ),
            SizedBox(height: theme.spaces.spacing400),
            const ExampleColumn(
              label: 'With Support Text',
              children: [
                LemonadeTextField(
                  label: 'Email',
                  placeholder: 'Enter your email',
                  supportText: 'We will never share your email',
                ),
              ],
            ),
            SizedBox(height: theme.spaces.spacing400),
            ExampleColumn(
              label: 'Error State',
              children: [
                StatefulBuilder(
                  builder: (context, setState) {
                    return LemonadeTextField(
                      label: 'Password',
                      placeholder: 'Enter password',
                      hasError: true,
                      obscureText: obscureText,
                      errorMessage: 'Password must be at least 8 characters',
                      leadingIcon: LemonadeIcons.padlock,
                      trailingIcon: obscureText
                          ? LemonadeIcons.eyeClosed
                          : LemonadeIcons.eyeOpen,
                      onTrailingIconTap: () {
                        setState(() {
                          obscureText = !obscureText;
                        });
                      },
                    );
                  },
                ),
              ],
            ),
            SizedBox(height: theme.spaces.spacing400),
            const ExampleColumn(
              label: 'Optional Field',
              children: [
                LemonadeTextField(
                  label: 'Phone',
                  placeholder: 'Enter phone number',
                  optionalIndicator: 'Optional',
                  keyboardType: TextInputType.phone,
                ),
              ],
            ),
            SizedBox(height: theme.spaces.spacing400),
            const ExampleColumn(
              label: 'Disabled',
              children: [
                LemonadeTextField(
                  label: 'Disabled Field',
                  placeholder: 'Cannot edit',
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
