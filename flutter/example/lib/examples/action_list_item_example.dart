import 'package:lemonade_design_system/lemonade_design_system.dart';
import 'package:lemonade_example/components.dart';

/// Example screen demonstrating the usage of [LemonadeActionListItem].
class ActionListItemExampleScreen extends StatefulWidget {
  /// Creates a new instance of [ActionListItemExampleScreen].
  const ActionListItemExampleScreen({super.key});

  @override
  State<ActionListItemExampleScreen> createState() =>
      _ActionListItemExampleScreenState();
}

class _ActionListItemExampleScreenState
    extends State<ActionListItemExampleScreen> {
  @override
  Widget build(BuildContext context) {
    final theme = LemonadeTheme.of(context);

    return ExampleScaffold(
      title: 'Action List Item Example',
      body: ColoredBox(
        color: theme.colors.background.bgSubtle,
        child: SingleChildScrollView(
          padding: EdgeInsets.all(theme.spaces.spacing400),
          child: Column(
            children: [
              ExampleSection(
                title: 'Basic Navigation',
                children: [
                  LemonadeCard(
                    padding: LemonadeCardPadding.xSmall,
                    child: Column(
                      children: [
                        LemonadeActionListItem(
                          label: 'Account Settings',
                          withNavigation: true,
                          onPressed: () => {},
                        ),
                        LemonadeActionListItem(
                          label: 'Billing',
                          withNavigation: true,
                          onPressed: () => {},
                        ),
                        LemonadeActionListItem(
                          label: 'Security',
                          withNavigation: true,
                          onPressed: () => {},
                        ),
                      ],
                    ),
                  ),
                ],
              ),
              SizedBox(height: theme.spaces.spacing600),
              ExampleSection(
                title: 'With Description',
                children: [
                  LemonadeCard(
                    padding: LemonadeCardPadding.xSmall,
                    child: Column(
                      children: [
                        LemonadeActionListItem(
                          label: 'Profile',
                          description: 'View and edit your profile information',
                          withNavigation: true,
                          onPressed: () => {},
                        ),
                        LemonadeActionListItem(
                          label: 'Preferences',
                          description: 'Customize your app experience',
                          withNavigation: true,
                          onPressed: () => {},
                        ),
                        LemonadeActionListItem(
                          label: 'Notifications',
                          description: 'Manage notification settings',
                          withNavigation: true,
                          onPressed: () => {},
                        ),
                      ],
                    ),
                  ),
                ],
              ),
              SizedBox(height: theme.spaces.spacing600),
              ExampleSection(
                title: 'With Leading Icons',
                children: [
                  LemonadeCard(
                    padding: LemonadeCardPadding.xSmall,
                    child: Column(
                      children: [
                        LemonadeActionListItem(
                          label: 'Account Settings',
                          description: 'Manage your account',
                          withNavigation: true,
                          onPressed: () => {},
                          leadingSlot: (_) => LemonadeIcon(
                            icon: LemonadeIcons.user,
                            color: theme.colors.content.contentPrimary,
                          ),
                        ),
                        LemonadeActionListItem(
                          label: 'Security',
                          description: 'Password and login options',
                          withNavigation: true,
                          onPressed: () => {},
                          leadingSlot: (_) => LemonadeIcon(
                            icon: LemonadeIcons.padlock,
                            color: theme.colors.content.contentPrimary,
                          ),
                        ),
                        LemonadeActionListItem(
                          label: 'Privacy',
                          description: 'Control your privacy settings',
                          withNavigation: true,
                          onPressed: () => {},
                          leadingSlot: (_) => LemonadeIcon(
                            icon: LemonadeIcons.shield,
                            color: theme.colors.content.contentPrimary,
                          ),
                        ),
                      ],
                    ),
                  ),
                ],
              ),
              SizedBox(height: theme.spaces.spacing600),
              ExampleSection(
                title: 'With Trailing Content',
                children: [
                  LemonadeCard(
                    padding: LemonadeCardPadding.xSmall,
                    child: Column(
                      children: [
                        LemonadeActionListItem(
                          label: 'Verified',
                          description: 'Your account is verified',
                          withNavigation: true,
                          onPressed: () => {},
                          trailingSlot: (_) => const LemonadeTag(
                            label: 'New',
                            voice: LemonadeTagVoice.positive,
                          ),
                        ),
                        LemonadeActionListItem(
                          label: 'Unread Notifications',
                          description: 'You have 3 new messages',
                          withNavigation: true,
                          onPressed: () => {},
                          trailingSlot: (_) => const LemonadeTag(
                            label: 'Danger',
                            voice: LemonadeTagVoice.critical,
                          ),
                        ),
                        LemonadeActionListItem(
                          label: 'Premium Member',
                          description: 'Enjoy exclusive benefits',
                          withNavigation: true,
                          onPressed: () => debugPrint('Click'),
                          leadingSlot: (_) => const LemonadeIcon(
                            icon: LemonadeIcons.star,
                          ),
                          trailingSlot: (_) => const LemonadeTag(
                            label: 'PRO',
                            voice: LemonadeTagVoice.info,
                          ),
                        ),
                      ],
                    ),
                  ),
                ],
              ),
              SizedBox(height: theme.spaces.spacing600),
              ExampleSection(
                title: 'Without Navigation',
                children: [
                  LemonadeCard(
                    padding: LemonadeCardPadding.xSmall,
                    child: Column(
                      children: [
                        const LemonadeActionListItem(
                          label: 'Item without navigation',
                          description: 'No chevron shown',
                          onPressed: null,
                        ),
                        LemonadeActionListItem(
                          label: 'With trailing badge',
                          description: 'But no navigation',
                          onPressed: () => {},
                          trailingSlot: (_) => const LemonadeTag(
                            label: 'PRO',
                            voice: LemonadeTagVoice.info,
                          ),
                        ),
                      ],
                    ),
                  ),
                ],
              ),
              SizedBox(height: theme.spaces.spacing600),
              ExampleSection(
                title: 'Disabled State',
                children: [
                  LemonadeCard(
                    padding: LemonadeCardPadding.xSmall,
                    child: Column(
                      children: [
                        const LemonadeActionListItem(
                          label: 'Disabled navigation item',
                          withNavigation: true,
                          onPressed: null,
                          enabled: false,
                        ),
                        const LemonadeActionListItem(
                          label: 'Disabled with description',
                          description: 'This feature is unavailable',
                          withNavigation: true,
                          onPressed: null,
                          enabled: false,
                        ),
                        LemonadeActionListItem(
                          label: 'Disabled with leading',
                          description: 'Locked for this user',
                          onPressed: () => {},
                          leadingSlot: (_) => const LemonadeIcon(
                            icon: LemonadeIcons.padlock,
                          ),
                          withNavigation: true,
                          enabled: false,
                        ),
                        LemonadeActionListItem(
                          label: 'Disabled with trailing',
                          description: 'Locked for this user',
                          onPressed: () => {},
                          trailingSlot: (_) => const LemonadeTag(
                            label: 'PRO',
                            voice: LemonadeTagVoice.info,
                          ),
                          withNavigation: true,
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
      ),
    );
  }
}
