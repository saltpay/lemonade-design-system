import 'package:lemonade_design_system/lemonade_design_system.dart';
import 'package:lemonade_example/components.dart';

/// Example screen demonstrating the usage of [LemonadeSelectionListItem].
class SelectionListItemExampleScreen extends StatefulWidget {
  /// Creates a new instance of [SelectionListItemExampleScreen].
  const SelectionListItemExampleScreen({super.key});

  @override
  State<SelectionListItemExampleScreen> createState() =>
      _SelectionListItemExampleScreenState();
}

class _SelectionListItemExampleScreenState
    extends State<SelectionListItemExampleScreen> {
  int? _singleSelection;
  final Set<int> _multipleSelection = {};
  bool _notificationsEnabled = true;
  bool _darkModeEnabled = false;

  @override
  Widget build(BuildContext context) {
    final theme = LemonadeTheme.of(context);

    return ExampleScaffold(
      title: 'Selection List Item Example',
      body: ColoredBox(
        color: theme.colors.background.bgSubtle,
        child: SingleChildScrollView(
          padding: EdgeInsets.all(theme.spaces.spacing600),
          child: Column(
            children: [
              ExampleSection(
                title: 'Single Selection (Radio)',
                children: [
                  LemonadeCard(
                    padding: LemonadeCardPadding.xSmall,
                    child: Column(
                      children: [
                        LemonadeSelectionListItem(
                          label: 'Option 1',
                          type: LemonadeSelectionListItemType.single,
                          checked: _singleSelection == 0,
                          onPressed: () {
                            setState(() => _singleSelection = 0);
                          },
                        ),
                        LemonadeSelectionListItem(
                          label: 'Option 2',
                          supportText: 'With support text',
                          type: LemonadeSelectionListItemType.single,
                          checked: _singleSelection == 1,
                          onPressed: () {
                            setState(() => _singleSelection = 1);
                          },
                        ),
                        LemonadeSelectionListItem(
                          label: 'Option 3',
                          type: LemonadeSelectionListItemType.single,
                          checked: _singleSelection == 2,
                          onPressed: () {
                            setState(() => _singleSelection = 2);
                          },
                        ),
                      ],
                    ),
                  ),
                ],
              ),
              SizedBox(height: theme.spaces.spacing600),
              ExampleSection(
                title: 'Multiple Selection (Checkbox)',
                children: [
                  LemonadeCard(
                    padding: LemonadeCardPadding.xSmall,
                    child: Column(
                      children: [
                        LemonadeSelectionListItem(
                          label: 'Item A',
                          type: LemonadeSelectionListItemType.multiple,
                          checked: _multipleSelection.contains(0),
                          onPressed: () {
                            setState(() {
                              if (_multipleSelection.contains(0)) {
                                _multipleSelection.remove(0);
                              } else {
                                _multipleSelection.add(0);
                              }
                            });
                          },
                        ),
                        LemonadeSelectionListItem(
                          label: 'Item B',
                          supportText: 'You can select multiple items',
                          type: LemonadeSelectionListItemType.multiple,
                          checked: _multipleSelection.contains(1),
                          onPressed: () {
                            setState(() {
                              if (_multipleSelection.contains(1)) {
                                _multipleSelection.remove(1);
                              } else {
                                _multipleSelection.add(1);
                              }
                            });
                          },
                        ),
                        LemonadeSelectionListItem(
                          label: 'Item C',
                          type: LemonadeSelectionListItemType.multiple,
                          checked: _multipleSelection.contains(2),
                          onPressed: () {
                            setState(() {
                              if (_multipleSelection.contains(2)) {
                                _multipleSelection.remove(2);
                              } else {
                                _multipleSelection.add(2);
                              }
                            });
                          },
                        ),
                      ],
                    ),
                  ),
                ],
              ),
              SizedBox(height: theme.spaces.spacing600),
              ExampleSection(
                title: 'With Leading Icon',
                children: [
                  LemonadeCard(
                    padding: LemonadeCardPadding.xSmall,
                    child: Column(
                      children: [
                        LemonadeSelectionListItem(
                          label: 'Credit Card',
                          type: LemonadeSelectionListItemType.single,
                          checked: true,
                          leadingSlot: (_) => LemonadeIcon(
                            icon: LemonadeIcons.card,
                            color: theme.colors.content.contentPrimary,
                          ),
                          onPressed: () {},
                        ),
                        LemonadeSelectionListItem(
                          label: 'Bank Transfer',
                          type: LemonadeSelectionListItemType.single,
                          checked: false,
                          leadingSlot: (_) => LemonadeIcon(
                            icon: LemonadeIcons.buildings,
                            color: theme.colors.content.contentSecondary,
                          ),
                          onPressed: () {},
                        ),
                      ],
                    ),
                  ),
                ],
              ),
              SizedBox(height: theme.spaces.spacing600),
              ExampleSection(
                title: 'Toggle Selection (Switch)',
                children: [
                  LemonadeCard(
                    padding: LemonadeCardPadding.xSmall,
                    child: Column(
                      children: [
                        LemonadeSelectionListItem(
                          label: 'Notifications',
                          supportText: 'Receive push notifications',
                          type: LemonadeSelectionListItemType.toggle,
                          checked: _notificationsEnabled,
                          onPressed: () {
                            setState(() {
                              _notificationsEnabled = !_notificationsEnabled;
                            });
                          },
                        ),
                        LemonadeSelectionListItem(
                          label: 'Dark Mode',
                          supportText: 'Use dark theme',
                          type: LemonadeSelectionListItemType.toggle,
                          checked: _darkModeEnabled,
                          onPressed: () {
                            setState(() {
                              _darkModeEnabled = !_darkModeEnabled;
                            });
                          },
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
                        LemonadeSelectionListItem(
                          label: 'Disabled unchecked',
                          type: LemonadeSelectionListItemType.single,
                          checked: false,
                          enabled: false,
                          onPressed: () {},
                        ),
                        LemonadeSelectionListItem(
                          label: 'Disabled checked',
                          type: LemonadeSelectionListItemType.single,
                          checked: true,
                          enabled: false,
                          onPressed: () {},
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
