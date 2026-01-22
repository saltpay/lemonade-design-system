import 'package:lemonade_design_system/lemonade_design_system.dart';
import 'package:lemonade_example/components.dart';

/// Example screen demonstrating the usage of [LemonadeSearchField].
class SearchFieldExampleScreen extends StatefulWidget {
  /// Creates a new instance of [SearchFieldExampleScreen].
  const SearchFieldExampleScreen({super.key});

  @override
  State<SearchFieldExampleScreen> createState() =>
      _SearchFieldExampleScreenState();
}

class _SearchFieldExampleScreenState extends State<SearchFieldExampleScreen> {
  String _searchText = '';

  @override
  Widget build(BuildContext context) {
    final theme = LemonadeTheme.of(context);

    return ExampleScaffold(
      title: 'SearchField Example',
      body: SingleChildScrollView(
        padding: EdgeInsets.all(theme.spaces.spacing600),
        child: ExampleSection(
          title: 'Search Field',
          children: [
            SizedBox(
              width: double.infinity,
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.stretch,
                children: [
                  Text(
                    'Basic',
                    style: theme.typography.bodyMediumRegular.apply(
                      color: theme.colors.content.contentSecondary,
                    ),
                  ),
                  SizedBox(height: theme.spaces.spacing300),
                  const LemonadeSearchField(
                    placeholder: 'Search...',
                  ),
                ],
              ),
            ),
            SizedBox(height: theme.spaces.spacing600),
            SizedBox(
              width: double.infinity,
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.stretch,
                children: [
                  Text(
                    'Interactive',
                    style: theme.typography.bodyMediumRegular.apply(
                      color: theme.colors.content.contentSecondary,
                    ),
                  ),
                  SizedBox(height: theme.spaces.spacing300),
                  LemonadeSearchField(
                    placeholder: 'Type to search...',
                    onChanged: (value) => setState(() => _searchText = value),
                  ),
                  if (_searchText.isNotEmpty) ...[
                    SizedBox(height: theme.spaces.spacing200),
                    Text(
                      'Searching for: $_searchText',
                      style: theme.typography.bodySmallRegular.apply(
                        color: theme.colors.content.contentSecondary,
                      ),
                    ),
                  ],
                ],
              ),
            ),
            SizedBox(height: theme.spaces.spacing600),
            SizedBox(
              width: double.infinity,
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.stretch,
                children: [
                  Text(
                    'Disabled',
                    style: theme.typography.bodyMediumRegular.apply(
                      color: theme.colors.content.contentSecondary,
                    ),
                  ),
                  SizedBox(height: theme.spaces.spacing300),
                  const LemonadeSearchField(
                    placeholder: 'Search disabled...',
                    enabled: false,
                  ),
                ],
              ),
            ),
          ],
        ),
      ),
    );
  }
}
