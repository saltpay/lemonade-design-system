import 'package:go_router/go_router.dart';
import 'package:lemonade_design_system/lemonade_design_system.dart';

/// A scaffold widget for example screens.
class ExampleScaffold extends StatelessWidget {
  /// Creates a new instance of [ExampleScaffold].
  const ExampleScaffold({
    required this.title,
    required this.body,
    super.key,
  });

  /// The title of the scaffold.
  final String title;

  /// The body of the scaffold.
  final Widget body;

  @override
  Widget build(BuildContext context) {
    final theme = LemonadeTheme.of(context);

    return GestureDetector(
      onTap: () {
        FocusScope.of(context).unfocus();
      },
      child: ColoredBox(
        color: theme.colors.background.bgDefault,
        child: Column(
          children: [
            ExampleNavigationBar(
              title: title,
            ),
            Expanded(
              child: MediaQuery.removePadding(
                context: context,
                removeTop: true,
                child: body,
              ),
            ),
          ],
        ),
      ),
    );
  }
}

/// A simple navigation bar for example screens.
class ExampleNavigationBar extends StatelessWidget {
  /// Creates a new instance of [ExampleNavigationBar].
  const ExampleNavigationBar({
    this.title,
    super.key,
  });

  /// The title of the navigation bar.
  final String? title;

  @override
  Widget build(BuildContext context) {
    final theme = LemonadeTheme.of(context);

    void Function()? onBackPressed;
    if (context.canPop()) {
      onBackPressed = () => context.pop();
    }

    return Container(
      decoration: BoxDecoration(
        color: theme.colors.background.bgDefault,
        border: Border(
          bottom: BorderSide(
            color: theme.colors.border.borderNeutralMedium,
            width: theme.border.base.border25,
          ),
        ),
      ),
      child: SafeArea(
        bottom: false,
        child: Container(
          padding: EdgeInsets.symmetric(
            horizontal: theme.spaces.spacing200,
            vertical: theme.spaces.spacing200,
          ),
          height: 44,
          child: Row(
            spacing: theme.spaces.spacing300,
            children: [
              if (onBackPressed != null)
                GestureDetector(
                  behavior: HitTestBehavior.opaque,
                  onTap: onBackPressed,
                  child: Container(
                    height: 40,
                    width: 44,
                    alignment: Alignment.center,
                    child: const LemonadeIcon(
                      icon: LemonadeIcons.arrowLeft,
                    ),
                  ),
                ),
              if (title != null)
                Expanded(
                  child: Text(
                    title!,
                    textAlign: TextAlign.center,
                    style: theme.typography.heading2XSmall.apply(
                      color: theme.colors.content.contentPrimary,
                    ),
                  ),
                ),
              if (onBackPressed != null)
                SizedBox(
                  width: theme.spaces.spacing800,
                ),
            ],
          ),
        ),
      ),
    );
  }
}

/// A list tile widget for example actions.
class ExampleListTile extends StatelessWidget {
  /// Creates a new instance of [ExampleListTile].
  const ExampleListTile({
    required this.title,
    this.onPressed,
    super.key,
  });

  /// The label of the action.
  final String title;

  /// The callback to be invoked when the action is pressed.
  final void Function()? onPressed;

  @override
  Widget build(BuildContext context) {
    final theme = LemonadeTheme.of(context);

    return GestureDetector(
      behavior: HitTestBehavior.opaque,
      onTap: onPressed,
      child: Container(
        decoration: ShapeDecoration(
          color: theme.colors.background.bgSubtle,
          shape: theme.shapes.radius200,
        ),
        padding: EdgeInsets.symmetric(
          vertical: theme.spaces.spacing600,
          horizontal: theme.spaces.spacing400,
        ),
        child: Row(
          spacing: theme.spaces.spacing200,
          children: [
            const LemonadeIcon(
              icon: LemonadeIcons.package,
            ),
            Text(
              title,
              style: theme.typography.bodyMediumMedium.apply(
                color: theme.colors.content.contentPrimary,
              ),
            ),
          ],
        ),
      ),
    );
  }
}

/// A section widget for grouping example items.
class ExampleSection extends StatelessWidget {
  /// Creates a new instance of [ExampleSection].
  const ExampleSection({
    required this.title,
    required this.children,
    super.key,
  });

  /// The title of the section.
  final String title;

  /// The children widgets of the section.
  final List<Widget> children;

  @override
  Widget build(BuildContext context) {
    final theme = LemonadeTheme.of(context);

    return Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      spacing: theme.spaces.spacing400,
      children: [
        Text(
          title,
          style: theme.typography.headingLarge.apply(
            color: theme.colors.content.contentPrimary,
          ),
        ),
        ...children,
      ],
    );
  }
}

/// A row widget for displaying example items.
class ExampleRow extends StatelessWidget {
  /// Creates a new instance of [ExampleRow].
  const ExampleRow({
    required this.label,
    required this.children,
    super.key,
  });

  /// The label of the row.
  final String label;

  /// The children widgets of the row.
  final List<Widget> children;

  @override
  Widget build(BuildContext context) {
    final theme = LemonadeTheme.of(context);

    return Column(
      crossAxisAlignment: CrossAxisAlignment.stretch,
      spacing: theme.spaces.spacing300,
      children: [
        Text(
          label,
          style: theme.typography.bodyMediumSemibold.apply(
            color: theme.colors.content.contentSecondary,
          ),
        ),
        Wrap(
          spacing: theme.spaces.spacing400,
          runSpacing: theme.spaces.spacing400,
          children: children,
        ),
      ],
    );
  }
}

/// A column widget for displaying example items.
class ExampleColumn extends StatelessWidget {
  /// Creates a new instance of [ExampleColumn].
  const ExampleColumn({
    required this.label,
    required this.children,
    super.key,
  });

  /// The label of the column.
  final String label;

  /// The children widgets of the column.
  final List<Widget> children;

  @override
  Widget build(BuildContext context) {
    final theme = LemonadeTheme.of(context);

    return Column(
      crossAxisAlignment: CrossAxisAlignment.stretch,
      spacing: theme.spaces.spacing300,
      children: [
        Text(
          label,
          style: theme.typography.bodyMediumRegular.apply(
            color: theme.colors.content.contentSecondary,
          ),
        ),
        Column(
          crossAxisAlignment: CrossAxisAlignment.stretch,
          spacing: theme.spaces.spacing600,
          children: children,
        ),
      ],
    );
  }
}

/// A grid view widget for displaying example items.
class ExampleGridView extends StatelessWidget {
  /// Creates a new instance of [ExampleGridView].
  const ExampleGridView({
    required this.label,
    required this.children,
    this.count = 4,
    super.key,
  });

  /// The label of the grid view.
  final String label;

  /// The number of columns in the grid view.
  final int count;

  /// The children widgets of the grid view.
  final List<Widget> children;

  @override
  Widget build(BuildContext context) {
    final theme = LemonadeTheme.of(context);

    return Column(
      crossAxisAlignment: CrossAxisAlignment.stretch,
      spacing: theme.spaces.spacing300,
      children: [
        Text(
          label,
          style: theme.typography.bodyMediumRegular.apply(
            color: theme.colors.content.contentSecondary,
          ),
        ),
        GridView.count(
          crossAxisCount: count,
          crossAxisSpacing: theme.spaces.spacing400,
          mainAxisSpacing: theme.spaces.spacing400,
          shrinkWrap: true,
          physics: const NeverScrollableScrollPhysics(),
          children: children,
        ),
      ],
    );
  }
}
