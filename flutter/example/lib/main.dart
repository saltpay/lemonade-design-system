import 'package:go_router/go_router.dart';
import 'package:lemonade_design_system/lemonade_design_system.dart';
import 'package:lemonade_example/components.dart';
import 'package:lemonade_example/router.dart';

void main() {
  runApp(const MainApp());
}

/// The main application widget.
class MainApp extends StatelessWidget {
  /// Creates a new instance of [MainApp].
  const MainApp({super.key});

  @override
  Widget build(BuildContext context) {
    return LemonadeUi(
      builder: (context) => WidgetsApp.router(
        color: LemonadeTheme.of(context).colors.content.contentBrand,
        routerConfig: router,
      ),
    );
  }
}

/// The main screen of the application.
class MainScreen extends StatelessWidget {
  /// Creates a new instance of [MainScreen].
  const MainScreen({super.key});

  @override
  Widget build(BuildContext context) {
    final theme = LemonadeTheme.of(context);
    return ExampleScaffold(
      title: 'Lemonade Design System',
      body: SingleChildScrollView(
        padding: EdgeInsets.all(theme.spaces.spacing600),
        child: ExampleSection(
          title: 'Components',
          children: [
            ExampleListTile(
              title: 'Action List Item',
              onPressed: () => context.push('/action_list_item'),
            ),
            ExampleListTile(
              title: 'Badge',
              onPressed: () => context.push('/badge'),
            ),
            ExampleListTile(
              title: 'Brand Logo',
              onPressed: () => context.push('/brand_logo'),
            ),
            ExampleListTile(
              title: 'Button',
              onPressed: () => context.push('/button'),
            ),
            ExampleListTile(
              title: 'Card',
              onPressed: () => context.push('/card'),
            ),
            ExampleListTile(
              title: 'Checkbox',
              onPressed: () => context.push('/checkbox'),
            ),
            ExampleListTile(
              title: 'Chip',
              onPressed: () => context.push('/chip'),
            ),
            ExampleListTile(
              title: 'Country Flag',
              onPressed: () => context.push('/country_flag'),
            ),
            ExampleListTile(
              title: 'Date Picker',
              onPressed: () => context.push('/date_picker'),
            ),
            ExampleListTile(
              title: 'Divider',
              onPressed: () => context.push('/divider'),
            ),
            ExampleListTile(
              title: 'Icon',
              onPressed: () => context.push('/icon'),
            ),
            ExampleListTile(
              title: 'Radio Button',
              onPressed: () => context.push('/radio_button'),
            ),
            ExampleListTile(
              title: 'Resource List Item',
              onPressed: () => context.push('/resource_list_item'),
            ),
            ExampleListTile(
              title: 'Search Field',
              onPressed: () => context.push('/search_field'),
            ),
            ExampleListTile(
              title: 'Segmented Control',
              onPressed: () => context.push('/segmented_control'),
            ),
            ExampleListTile(
              title: 'Selection List Item',
              onPressed: () => context.push('/selection_list_item'),
            ),
            ExampleListTile(
              title: 'Spinner',
              onPressed: () => context.push('/spinner'),
            ),
            ExampleListTile(
              title: 'Switch',
              onPressed: () => context.push('/switch'),
            ),
            ExampleListTile(
              title: 'Symbol Container',
              onPressed: () => context.push('/symbol_container'),
            ),
            ExampleListTile(
              title: 'Tag',
              onPressed: () => context.push('/tag'),
            ),
            ExampleListTile(
              title: 'Text Field',
              onPressed: () => context.push('/text_field'),
            ),
            ExampleListTile(
              title: 'Tile',
              onPressed: () => context.push('/tile'),
            ),
            ExampleListTile(
              title: 'Toast',
              onPressed: () => context.push('/toast'),
            ),
          ],
        ),
      ),
    );
  }
}
