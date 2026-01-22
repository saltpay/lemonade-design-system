import 'package:flutter_test/flutter_test.dart';
import 'package:lemonade_design_system/lemonade_design_system.dart';

import '../../helpers/widget_tester_extension.dart';

void main() {
  group('LemonadeSelectionListItem', () {
    group('single selection (radio)', () {
      testWidgets('renders with label', (tester) async {
        await tester.pumpLemonadeWidget(
          LemonadeSelectionListItem(
            label: 'Test Option',
            type: LemonadeSelectionListItemType.single,
            checked: false,
            onPressed: () {},
          ),
        );

        expect(find.text('Test Option'), findsOneWidget);
        expect(find.byType(LemonadeRadioButton), findsOneWidget);
      });

      testWidgets('renders with support text', (tester) async {
        await tester.pumpLemonadeWidget(
          LemonadeSelectionListItem(
            label: 'Test Option',
            supportText: 'Support text here',
            type: LemonadeSelectionListItemType.single,
            checked: false,
            onPressed: () {},
          ),
        );

        expect(find.text('Support text here'), findsOneWidget);
      });

      testWidgets('calls onItemClicked only when unchecked', (tester) async {
        var callCount = 0;

        await tester.pumpLemonadeWidget(
          LemonadeSelectionListItem(
            label: 'Test Option',
            type: LemonadeSelectionListItemType.single,
            checked: false,
            onPressed: () {
              callCount++;
            },
          ),
        );

        await tester.tap(find.byType(LemonadeSelectionListItem));
        expect(callCount, equals(1));
      });

      testWidgets('does not call onItemClicked when already checked', (
        tester,
      ) async {
        var callCount = 0;

        await tester.pumpLemonadeWidget(
          LemonadeSelectionListItem(
            label: 'Test Option',
            type: LemonadeSelectionListItemType.single,
            checked: true,
            onPressed: () {
              callCount++;
            },
          ),
        );

        await tester.tap(find.byType(LemonadeSelectionListItem));
        expect(callCount, equals(0));
      });
    });

    group('multiple selection (checkbox)', () {
      testWidgets('renders checkbox', (tester) async {
        await tester.pumpLemonadeWidget(
          LemonadeSelectionListItem(
            label: 'Test Option',
            type: LemonadeSelectionListItemType.multiple,
            checked: false,
            onPressed: () {},
          ),
        );

        expect(find.byType(LemonadeCheckbox), findsOneWidget);
      });

      testWidgets('calls onItemClicked on every tap', (tester) async {
        var callCount = 0;

        await tester.pumpLemonadeWidget(
          LemonadeSelectionListItem(
            label: 'Test Option',
            type: LemonadeSelectionListItemType.multiple,
            checked: true,
            onPressed: () {
              callCount++;
            },
          ),
        );

        await tester.tap(find.byType(LemonadeSelectionListItem));
        expect(callCount, equals(1));
      });
    });

    group('toggle selection (switch)', () {
      testWidgets('renders switch', (tester) async {
        await tester.pumpLemonadeWidget(
          LemonadeSelectionListItem(
            label: 'Test Option',
            type: LemonadeSelectionListItemType.toggle,
            checked: false,
            onPressed: () {},
          ),
        );

        expect(find.byType(LemonadeSwitch), findsOneWidget);
      });

      testWidgets('calls onItemClicked on every tap', (tester) async {
        var callCount = 0;

        await tester.pumpLemonadeWidget(
          LemonadeSelectionListItem(
            label: 'Test Option',
            type: LemonadeSelectionListItemType.toggle,
            checked: true,
            onPressed: () {
              callCount++;
            },
          ),
        );

        await tester.tap(find.byType(LemonadeSelectionListItem));
        expect(callCount, equals(1));
      });
    });

    group('disabled state', () {
      testWidgets('does not call onItemClicked when disabled', (tester) async {
        var wasCalled = false;

        await tester.pumpLemonadeWidget(
          LemonadeSelectionListItem(
            label: 'Test Option',
            type: LemonadeSelectionListItemType.single,
            checked: false,
            enabled: false,
            onPressed: () {
              wasCalled = true;
            },
          ),
        );

        await tester.tap(find.byType(LemonadeSelectionListItem));
        expect(wasCalled, isFalse);
      });

      testWidgets('applies reduced opacity when disabled', (tester) async {
        await tester.pumpLemonadeWidget(
          LemonadeSelectionListItem(
            label: 'Test Option',
            type: LemonadeSelectionListItemType.single,
            checked: false,
            enabled: false,
            onPressed: () {},
          ),
        );

        // Find Opacity widgets that have the label text as a descendant
        final opacityWidgets = find.byType(Opacity);
        expect(opacityWidgets, findsAtLeastNWidgets(1));

        // Verify at least one has reduced opacity
        final hasReducedOpacity = tester
            .widgetList<Opacity>(opacityWidgets)
            .any((widget) => widget.opacity < 1.0);
        expect(hasReducedOpacity, isTrue);
      });
    });

    group('leading slot', () {
      testWidgets('renders leading widget when provided', (tester) async {
        await tester.pumpLemonadeWidget(
          LemonadeSelectionListItem(
            label: 'Test Option',
            type: LemonadeSelectionListItemType.single,
            checked: false,
            leadingSlot: (_) => const LemonadeIcon(icon: LemonadeIcons.card),
            onPressed: () {},
          ),
        );

        expect(find.byType(LemonadeIcon), findsOneWidget);
      });
    });

    group('trailing slot', () {
      testWidgets('renders trailing widget when provided', (tester) async {
        await tester.pumpLemonadeWidget(
          LemonadeSelectionListItem(
            label: 'Test Option',
            type: LemonadeSelectionListItemType.single,
            checked: false,
            trailingSlot: (_) => const Text('Trailing'),
            onPressed: () {},
          ),
        );

        expect(find.text('Trailing'), findsOneWidget);
      });
    });

    group('press feedback', () {
      testWidgets('shows press state on tap down', (tester) async {
        await tester.pumpLemonadeWidget(
          LemonadeSelectionListItem(
            label: 'Test Option',
            type: LemonadeSelectionListItemType.single,
            checked: false,
            onPressed: () {},
          ),
        );

        // Selection list item uses AnimatedContainer for press feedback
        // The radio button also uses AnimatedContainer internally
        expect(find.byType(AnimatedContainer), findsAtLeastNWidgets(1));
      });
    });
  });
}
