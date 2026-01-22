import 'package:lemonade_design_system/lemonade_design_system.dart';

/// {@template LemonadeSegmentItem}
/// An individual segment inside a [LemonadeSegmentedControl].
///
/// Each segment has a [value] that is emitted when selected, a [label]
/// displayed as text, and an optional leading [leadingIcon].
/// {@endtemplate}
@immutable
class LemonadeSegmentItem<T> {
  /// {@macro LemonadeSegmentItem}
  const LemonadeSegmentItem({
    required this.value,
    required this.label,

    @Deprecated('Use leadingIcon instead.') this.icon,
    this.leadingIcon,
  });

  /// {@template LemonadeSegmentItem.value}
  /// The value emitted when this segment is selected.
  /// {@endtemplate}
  final T value;

  /// {@template LemonadeSegmentItem.label}
  /// Text shown inside the segment.
  /// {@endtemplate}
  final String label;

  /// {@template LemonadeSegmentItem.icon}
  /// Optional leading icon widget.
  /// {@endtemplate}
  @Deprecated('Use [leadingIcon] instead.')
  final Widget? icon;

  /// {@template LemonadeSegmentItem.icon}
  /// Optional leading icon widget.
  /// {@endtemplate}
  final LemonadeIcons? leadingIcon;
}

/// {@template LemonadeSegmentedControl}
/// A generic segmented control from the Lemonade Design System.
///
/// A [LemonadeSegmentedControl] displays a horizontal row of mutually
/// exclusive options. When the user taps a segment, the [onChanged] callback
/// is invoked with the corresponding [LemonadeSegmentItem.value].
///
/// This is a controlled component: the parent owns [selectedValue] and
/// handles [onChanged] to update state.
///
/// ## Example
/// ```dart
/// enum ViewMode { list, grid }
///
/// ViewMode selectedMode = ViewMode.list;
///
/// LemonadeSegmentedControl<ViewMode>(
///   items: [
///     LemonadeSegmentItem(value: ViewMode.list, label: 'List'),
///     LemonadeSegmentItem(value: ViewMode.grid, label: 'Grid'),
///   ],
///   selectedValue: selectedMode,
///   onChanged: (ViewMode value) {
///     setState(() {
///       selectedMode = value;
///     });
///   },
/// )
/// ```
///
/// See also:
/// - [LemonadeSegmentItem], which defines individual segments
/// - [LemonadeTheme], which provides the design tokens
/// {@endtemplate}
class LemonadeSegmentedControl<T> extends StatelessWidget {
  /// {@macro LemonadeSegmentedControl}
  const LemonadeSegmentedControl({
    required this.items,
    required this.selectedValue,
    required this.onChanged,
    super.key,
    this.isEnabled = true,
    this.height = 40,
    this.semanticIdentifier,
    this.semanticLabel,
  }) : assert(items.length >= 2, 'Segmented control needs at least 2 items.');

  /// {@template LemonadeSegmentedControl.items}
  /// All available segments.
  ///
  /// Must contain at least 2 items.
  /// {@endtemplate}
  final List<LemonadeSegmentItem<T>> items;

  /// {@template LemonadeSegmentedControl.selectedValue}
  /// Currently selected value.
  ///
  /// Must match one of the [LemonadeSegmentItem.value] in [items].
  /// {@endtemplate}
  final T selectedValue;

  /// {@template LemonadeSegmentedControl.onChanged}
  /// Called when user taps a segment.
  ///
  /// The callback receives the [LemonadeSegmentItem.value] of the tapped
  /// segment.
  /// {@endtemplate}
  final ValueChanged<T> onChanged;

  /// {@template LemonadeSegmentedControl.isEnabled}
  /// Whether the whole control is interactive.
  ///
  /// When false, the control is displayed with reduced opacity and does not
  /// respond to taps. Defaults to true.
  /// {@endtemplate}
  final bool isEnabled;

  /// {@template LemonadeSegmentedControl.height}
  /// Fixed height for the control.
  ///
  /// Defaults to 40.
  /// {@endtemplate}
  final double height;

  /// {@template LemonadeSegmentedControl.semanticIdentifier}
  /// An identifier for the control used for accessibility and testing.
  /// {@endtemplate}
  final String? semanticIdentifier;

  /// {@template LemonadeSegmentedControl.semanticLabel}
  /// A label for the control used for accessibility purposes.
  /// {@endtemplate}
  final String? semanticLabel;

  double _getButtonPosition(
    double segmentWidth,
    int selectedIndex,
    double parentPadding,
  ) {
    double position;

    final isFirstSegment = selectedIndex == 0;
    final isLastSegment = selectedIndex == items.length - 1;

    if (isFirstSegment) {
      position = segmentWidth * selectedIndex;
    } else if (isLastSegment) {
      position = segmentWidth * selectedIndex - (parentPadding * 2);
    } else {
      position = segmentWidth * selectedIndex - parentPadding;
    }

    return position;
  }

  @override
  Widget build(BuildContext context) {
    final theme = LemonadeTheme.of(context);

    final containerBackground = theme.colors.background.bgElevated;
    final containerRadius = theme.radius.radius200;
    final containerPadding = theme.spaces.spacing100;

    final buttonBackground = theme.colors.background.bgDefault;
    final buttonRadius = theme.radius.radius150;

    final selectedIndex = items.indexWhere(
      (item) => item.value == selectedValue,
    );
    assert(selectedIndex != -1, 'selectedValue must exist in items.');

    return Semantics(
      identifier: semanticIdentifier,
      label: semanticLabel,
      enabled: isEnabled,
      child: Opacity(
        opacity: isEnabled ? 1.0 : 0.6,
        child: LayoutBuilder(
          builder: (context, constraints) {
            final width = constraints.maxWidth + containerPadding * 2;
            final segmentWidth = width / items.length;

            return DecoratedBox(
              decoration: BoxDecoration(
                color: containerBackground,
                borderRadius: BorderRadius.circular(containerRadius),
              ),
              child: SizedBox(
                height: theme.sizes.size1000,
                child: Stack(
                  children: <Widget>[
                    // Sliding thumb background
                    AnimatedPositioned(
                      duration: const Duration(milliseconds: 200),
                      curve: Curves.easeInOut,
                      left: _getButtonPosition(
                        segmentWidth,
                        selectedIndex,
                        containerPadding,
                      ),
                      width: segmentWidth,
                      top: 0,
                      bottom: 0,
                      child: Padding(
                        padding: const EdgeInsets.all(4),
                        child: DecoratedBox(
                          decoration: BoxDecoration(
                            color: buttonBackground,
                            borderRadius: BorderRadius.circular(buttonRadius),
                            boxShadow: theme.shadows.small,
                          ),
                        ),
                      ),
                    ),

                    // Segments (tap targets + labels)
                    Container(
                      padding: EdgeInsets.all(theme.spaces.spacing100),
                      child: Row(
                        children: List<Widget>.generate(items.length, (index) {
                          final item = items[index];
                          final isSelected = item.value == selectedValue;
                          final contentColor = isSelected
                              ? theme.colors.content.contentPrimary
                              : theme.colors.content.contentSecondary;

                          return Expanded(
                            child: GestureDetector(
                              behavior: HitTestBehavior.opaque,
                              onTap: isEnabled && !isSelected
                                  ? () => onChanged(item.value)
                                  : null,
                              child: Container(
                                padding: EdgeInsets.symmetric(
                                  horizontal: theme.spaces.spacing200,
                                  vertical: theme.spaces.spacing100,
                                ),
                                height: theme.sizes.size800,
                                alignment: Alignment.center,
                                child: Center(
                                  child: Row(
                                    mainAxisSize: MainAxisSize.min,
                                    mainAxisAlignment: MainAxisAlignment.center,
                                    children: <Widget>[
                                      // TODO(felipeemarcon): Remove support for deprecated icon in future major release.
                                      if (item.icon != null) ...[
                                        item.icon!,
                                      ],
                                      if (item.leadingIcon != null) ...[
                                        LemonadeIcon(
                                          icon: item.leadingIcon!,
                                          color: contentColor,
                                          size: LemonadeIconSize.small,
                                        ),
                                        SizedBox(
                                          width: theme.spaces.spacing200,
                                        ),
                                      ],
                                      // TODO(felipeemarcon): Fix the text overflow when there is no space.
                                      Text(
                                        textAlign: TextAlign.center,
                                        item.label,
                                        maxLines: 1,
                                        overflow: TextOverflow.ellipsis,
                                        style: theme.typography.bodySmallMedium
                                            .apply(color: contentColor),
                                      ),
                                    ],
                                  ),
                                ),
                              ),
                            ),
                          );
                        }),
                      ),
                    ),
                  ],
                ),
              ),
            );
          },
        ),
      ),
    );
  }
}
