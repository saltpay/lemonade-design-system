import 'package:flutter_test/flutter_test.dart';
import 'package:lemonade_design_system/lemonade_design_system.dart';

import '../../helpers/widget_tester_extension.dart';

void main() {
  group('LemonadeContentItem', () {
    testWidgets('renders correctly with default configuration', (tester) async {
      await tester.pumpLemonadeWidget(
        const LemonadeContentItem(
          label: 'Test Label',
          value: 'Test Value',
        ),
      );

      expect(find.byType(LemonadeContentItem), findsOneWidget);
      expect(find.text('Test Label'), findsOneWidget);
      expect(find.text('Test Value'), findsOneWidget);

      final contentItemWidget = tester.widget<LemonadeContentItem>(
        find.byType(LemonadeContentItem),
      );
      expect(
        contentItemWidget.orientation,
        equals(LemonadeContentItemOrientation.horizontal),
      );
      expect(contentItemWidget.size, equals(LemonadeContentItemSize.small));
    });

    testWidgets('renders horizontal layout correctly', (tester) async {
      await tester.pumpLemonadeWidget(
        const LemonadeContentItem(
          label: 'Email',
          value: 'user@example.com',
        ),
      );

      expect(find.text('Email'), findsOneWidget);
      expect(find.text('user@example.com'), findsOneWidget);
    });

    testWidgets('renders vertical small layout correctly', (tester) async {
      await tester.pumpLemonadeWidget(
        const LemonadeContentItem(
          label: 'Username',
          value: 'johndoe',
          orientation: LemonadeContentItemOrientation.vertical,
        ),
      );

      expect(find.text('Username'), findsOneWidget);
      expect(find.text('johndoe'), findsOneWidget);
    });

    testWidgets('renders vertical large layout correctly', (tester) async {
      await tester.pumpLemonadeWidget(
        const LemonadeContentItem(
          label: 'Balance',
          value: '€1,234.56',
          orientation: LemonadeContentItemOrientation.vertical,
          size: LemonadeContentItemSize.large,
        ),
      );

      expect(find.text('Balance'), findsOneWidget);
      expect(find.text('€1,234.56'), findsOneWidget);
    });

    testWidgets('renders with leading slot', (tester) async {
      await tester.pumpLemonadeWidget(
        const LemonadeContentItem(
          label: 'Account',
          value: '12345',
          leadingSlot: LemonadeIcon(icon: LemonadeIcons.user),
        ),
      );

      expect(find.text('Account'), findsOneWidget);
      expect(find.text('12345'), findsOneWidget);
      expect(find.byType(LemonadeIcon), findsOneWidget);
    });

    testWidgets('renders with trailing slot', (tester) async {
      await tester.pumpLemonadeWidget(
        const LemonadeContentItem(
          label: 'Status',
          value: 'Active',
          trailingSlot: LemonadeIcon(icon: LemonadeIcons.pencilLine),
        ),
      );

      expect(find.text('Status'), findsOneWidget);
      expect(find.text('Active'), findsOneWidget);
      expect(find.byType(LemonadeIcon), findsOneWidget);
    });

    testWidgets('renders with addon slot in horizontal layout', (tester) async {
      await tester.pumpLemonadeWidget(
        const LemonadeContentItem(
          label: 'Product',
          value: 'Widget',
          addonSlot: LemonadeTag(
            label: 'Addon',
          ),
        ),
      );

      expect(find.text('Product'), findsOneWidget);
      expect(find.text('Widget'), findsOneWidget);
      expect(find.byType(LemonadeTag), findsOneWidget);
    });

    testWidgets('renders with addon slot in vertical large layout', (
      tester,
    ) async {
      await tester.pumpLemonadeWidget(
        const LemonadeContentItem(
          label: 'Total',
          value: '€999.99',
          orientation: LemonadeContentItemOrientation.vertical,
          size: LemonadeContentItemSize.large,
          addonSlot: LemonadeTag(
            label: 'Addon',
          ),
        ),
      );

      expect(find.text('Total'), findsOneWidget);
      expect(find.text('€999.99'), findsOneWidget);
      expect(find.byType(LemonadeTag), findsOneWidget);
    });

    testWidgets('renders with all slots', (tester) async {
      await tester.pumpLemonadeWidget(
        const LemonadeContentItem(
          label: 'Payment',
          value: '€50.00',
          orientation: LemonadeContentItemOrientation.vertical,
          size: LemonadeContentItemSize.large,
          leadingSlot: LemonadeSymbolContainer.text(
            text: 'AC',
          ),
          trailingSlot: LemonadeIcon(icon: LemonadeIcons.pencilLine),
          addonSlot: LemonadeTag(
            label: 'Addon',
          ),
        ),
      );

      expect(find.text('Payment'), findsOneWidget);
      expect(find.text('€50.00'), findsOneWidget);
      expect(find.byType(LemonadeSymbolContainer), findsOneWidget);
      expect(find.byType(LemonadeIcon), findsOneWidget);
      expect(find.byType(LemonadeTag), findsOneWidget);
    });

    testWidgets('renders with semantic identifier', (tester) async {
      const semanticId = 'content_item_1';

      await tester.pumpLemonadeWidget(
        const LemonadeContentItem(
          label: 'Test',
          value: 'Value',
          semanticIdentifier: semanticId,
        ),
      );

      final contentItemWidget = tester.widget<LemonadeContentItem>(
        find.byType(LemonadeContentItem),
      );
      expect(contentItemWidget.semanticIdentifier, equals(semanticId));
    });

    testWidgets('renders all valid layout and size combinations', (
      tester,
    ) async {
      // Horizontal + Small (valid)
      await tester.pumpLemonadeWidget(
        const LemonadeContentItem(
          label: 'Label horizontal small',
          value: 'Value horizontal small',
        ),
      );

      var contentItemWidget = tester.widget<LemonadeContentItem>(
        find.byType(LemonadeContentItem),
      );
      expect(
        contentItemWidget.orientation,
        equals(LemonadeContentItemOrientation.horizontal),
      );
      expect(contentItemWidget.size, equals(LemonadeContentItemSize.small));

      // Vertical + Small (valid)
      await tester.pumpLemonadeWidget(
        const LemonadeContentItem(
          label: 'Label vertical small',
          value: 'Value vertical small',
          orientation: LemonadeContentItemOrientation.vertical,
        ),
      );

      contentItemWidget = tester.widget<LemonadeContentItem>(
        find.byType(LemonadeContentItem),
      );
      expect(
        contentItemWidget.orientation,
        equals(LemonadeContentItemOrientation.vertical),
      );
      expect(contentItemWidget.size, equals(LemonadeContentItemSize.small));

      // Vertical + Large (valid)
      await tester.pumpLemonadeWidget(
        const LemonadeContentItem(
          label: 'Label vertical large',
          value: 'Value vertical large',
          orientation: LemonadeContentItemOrientation.vertical,
          size: LemonadeContentItemSize.large,
        ),
      );

      contentItemWidget = tester.widget<LemonadeContentItem>(
        find.byType(LemonadeContentItem),
      );
      expect(
        contentItemWidget.orientation,
        equals(LemonadeContentItemOrientation.vertical),
      );
      expect(contentItemWidget.size, equals(LemonadeContentItemSize.large));
    });

    testWidgets('renders with LemonadeSymbolContainer as leading slot', (
      tester,
    ) async {
      await tester.pumpLemonadeWidget(
        const LemonadeContentItem(
          label: 'Hearts',
          value: '42',
          leadingSlot: LemonadeSymbolContainer.icon(
            icon: LemonadeIcons.heart,
          ),
        ),
      );

      expect(find.text('Hearts'), findsOneWidget);
      expect(find.text('42'), findsOneWidget);
      expect(find.byType(LemonadeSymbolContainer), findsOneWidget);
    });

    testWidgets('calls onPressed when tapped', (tester) async {
      var pressed = false;

      await tester.pumpLemonadeWidget(
        LemonadeContentItem(
          label: 'Clickable',
          value: 'Tap me',
          onPressed: () {
            pressed = true;
          },
        ),
      );

      expect(pressed, isFalse);

      await tester.tap(find.text('Tap me'));
      await tester.pump();

      expect(pressed, isTrue);
    });

    testWidgets('does not call onPressed when null', (tester) async {
      await tester.pumpLemonadeWidget(
        const LemonadeContentItem(
          label: 'Not Clickable',
          value: 'Cannot tap',
        ),
      );

      // Should not throw when tapped without onPressed
      await tester.tap(find.text('Cannot tap'));
      await tester.pump();
    });

    testWidgets('renders with custom valueStyle in horizontal layout', (
      tester,
    ) async {
      const customColor = Color(0xFFFF0000);

      await tester.pumpLemonadeWidget(
        const LemonadeContentItem(
          label: 'Custom',
          value: 'Styled Value',
          valueStyle: TextStyle(color: customColor),
        ),
      );

      expect(find.text('Styled Value'), findsOneWidget);

      final valueText = tester.widget<Text>(find.text('Styled Value'));
      expect(valueText.style?.color, equals(customColor));
    });

    testWidgets('renders with custom valueStyle in vertical small layout', (
      tester,
    ) async {
      const customColor = Color(0xFF00FF00);

      await tester.pumpLemonadeWidget(
        const LemonadeContentItem(
          label: 'Custom',
          value: 'Styled Value',
          orientation: LemonadeContentItemOrientation.vertical,
          valueStyle: TextStyle(color: customColor),
        ),
      );

      expect(find.text('Styled Value'), findsOneWidget);

      final valueText = tester.widget<Text>(find.text('Styled Value'));
      expect(valueText.style?.color, equals(customColor));
    });

    testWidgets('renders with custom valueStyle in vertical large layout', (
      tester,
    ) async {
      const customColor = Color(0xFF0000FF);

      await tester.pumpLemonadeWidget(
        const LemonadeContentItem(
          label: 'Custom',
          value: 'Styled Value',
          orientation: LemonadeContentItemOrientation.vertical,
          size: LemonadeContentItemSize.large,
          valueStyle: TextStyle(color: customColor),
        ),
      );

      expect(find.text('Styled Value'), findsOneWidget);

      final valueText = tester.widget<Text>(find.text('Styled Value'));
      expect(valueText.style?.color, equals(customColor));
    });

    testWidgets('renders with custom valueStyle font weight', (tester) async {
      await tester.pumpLemonadeWidget(
        const LemonadeContentItem(
          label: 'Custom',
          value: 'Bold Value',
          valueStyle: TextStyle(fontWeight: FontWeight.w900),
        ),
      );

      expect(find.text('Bold Value'), findsOneWidget);

      final valueText = tester.widget<Text>(find.text('Bold Value'));
      expect(valueText.style?.fontWeight, equals(FontWeight.w900));
    });

    testWidgets('renders with custom valueStyle font size', (tester) async {
      const customFontSize = 24.0;

      await tester.pumpLemonadeWidget(
        const LemonadeContentItem(
          label: 'Custom',
          value: 'Large Value',
          valueStyle: TextStyle(fontSize: customFontSize),
        ),
      );

      expect(find.text('Large Value'), findsOneWidget);

      final valueText = tester.widget<Text>(find.text('Large Value'));
      expect(valueText.style?.fontSize, equals(customFontSize));
    });

    testWidgets('renders with multiple custom valueStyle properties', (
      tester,
    ) async {
      const customColor = Color(0xFFFFAA00);
      const customFontSize = 20.0;
      const customFontWeight = FontWeight.w700;

      await tester.pumpLemonadeWidget(
        const LemonadeContentItem(
          label: 'Custom',
          value: 'Multi Styled',
          valueStyle: TextStyle(
            color: customColor,
            fontSize: customFontSize,
            fontWeight: customFontWeight,
          ),
        ),
      );

      expect(find.text('Multi Styled'), findsOneWidget);

      final valueText = tester.widget<Text>(find.text('Multi Styled'));
      expect(valueText.style?.color, equals(customColor));
      expect(valueText.style?.fontSize, equals(customFontSize));
      expect(valueText.style?.fontWeight, equals(customFontWeight));
    });
  });
}
