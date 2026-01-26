import 'package:flutter/material.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:lemonade_design_system/lemonade_design_system.dart';

void main() {
  // Helper to pump widget with MaterialApp for Overlay support
  Future<void> pumpToastTest(WidgetTester tester, Widget child) async {
    await tester.pumpWidget(
      LemonadeUi(
        builder: (context) => MaterialApp(
          home: Scaffold(
            body: child,
          ),
        ),
      ),
    );
  }

  // Cleanup helper to dismiss and pump out all animations
  Future<void> cleanupToast(WidgetTester tester) async {
    LemonadeToastManager.dismiss();
    await tester.pumpAndSettle();
  }

  group('LemonadeToastManager', () {
    testWidgets('shows toast overlay when show is called', (tester) async {
      await pumpToastTest(
        tester,
        Builder(
          builder: (context) {
            return LemonadeButton(
              onPressed: () {
                LemonadeToastManager.show(
                  context,
                  voice: LemonadeToastVoice.success,
                  message: 'Test toast',
                  duration: LemonadeToastDuration.medium,
                );
              },
              label: 'Show Toast',
            );
          },
        ),
      );

      // Toast should not be visible initially
      expect(find.text('Test toast'), findsNothing);

      // Trigger toast
      await tester.tap(find.text('Show Toast'));
      await tester.pumpAndSettle();

      // Toast should be visible
      expect(find.text('Test toast'), findsOneWidget);
      expect(find.byType(LemonadeToast), findsOneWidget);

      await cleanupToast(tester);
    });

    testWidgets('shows toast with correct voice variant', (tester) async {
      for (final voice in LemonadeToastVoice.values) {
        await pumpToastTest(
          tester,
          Builder(
            builder: (context) {
              return LemonadeButton(
                onPressed: () {
                  LemonadeToastManager.show(
                    context,
                    voice: voice,
                    message: 'Test ${voice.name}',
                    duration: LemonadeToastDuration.medium,
                  );
                },
                label: 'Show ${voice.name}',
              );
            },
          ),
        );

        await tester.tap(find.text('Show ${voice.name}'));
        await tester.pumpAndSettle();

        final toastWidget = tester.widget<LemonadeToast>(
          find.byType(LemonadeToast),
        );
        expect(toastWidget.voice, equals(voice));

        await cleanupToast(tester);
      }
    });

    testWidgets('shows neutral toast with custom icon', (tester) async {
      await pumpToastTest(
        tester,
        Builder(
          builder: (context) {
            return LemonadeButton(
              label: 'Show Toast',
              onPressed: () {
                LemonadeToastManager.show(
                  context,
                  voice: LemonadeToastVoice.neutral,
                  message: 'Custom icon',
                  icon: LemonadeIcons.heart,
                  duration: LemonadeToastDuration.medium,
                );
              },
            );
          },
        ),
      );

      await tester.tap(find.text('Show Toast'));
      await tester.pumpAndSettle();

      final toastWidget = tester.widget<LemonadeToast>(
        find.byType(LemonadeToast),
      );
      expect(toastWidget.icon, equals(LemonadeIcons.heart));

      await cleanupToast(tester);
    });

    testWidgets('auto-dismisses toast after duration', (tester) async {
      await pumpToastTest(
        tester,
        Builder(
          builder: (context) {
            return LemonadeButton(
              onPressed: () {
                LemonadeToastManager.show(
                  context,
                  voice: LemonadeToastVoice.success,
                  message: 'Auto dismiss',
                  duration: const Duration(milliseconds: 500),
                );
              },
              label: 'Show Toast',
            );
          },
        ),
      );

      await tester.tap(find.text('Show Toast'));
      await tester.pump();
      await tester.pump(const Duration(milliseconds: 100));

      // Toast should be visible
      expect(find.text('Auto dismiss'), findsOneWidget);

      // Wait for auto-dismiss duration
      await tester.pump(const Duration(milliseconds: 500));

      // Wait for exit animation
      await tester.pumpAndSettle();

      // Toast should be gone
      expect(find.text('Auto dismiss'), findsNothing);
    });

    testWidgets('uses predefined duration constants', (tester) async {
      await pumpToastTest(
        tester,
        Builder(
          builder: (context) {
            return LemonadeButton(
              onPressed: () {
                LemonadeToastManager.show(
                  context,
                  voice: LemonadeToastVoice.success,
                  message: 'Short duration',
                );
              },
              label: 'Show Toast',
            );
          },
        ),
      );

      await tester.tap(find.text('Show Toast'));
      await tester.pumpAndSettle();

      expect(find.text('Short duration'), findsOneWidget);

      await cleanupToast(tester);
    });

    testWidgets('accepts custom duration', (tester) async {
      await pumpToastTest(
        tester,
        Builder(
          builder: (context) {
            return LemonadeButton(
              onPressed: () {
                LemonadeToastManager.show(
                  context,
                  voice: LemonadeToastVoice.neutral,
                  message: 'Custom duration',
                  duration: const Duration(milliseconds: 300),
                );
              },
              label: 'Show Toast',
            );
          },
        ),
      );

      await tester.tap(find.text('Show Toast'));
      await tester.pump();
      await tester.pump(const Duration(milliseconds: 100));

      // Toast should be visible
      expect(find.text('Custom duration'), findsOneWidget);

      // Wait for custom duration
      await tester.pump(const Duration(milliseconds: 300));

      // Wait for exit animation
      await tester.pumpAndSettle();

      // Toast should be gone after custom duration
      expect(find.text('Custom duration'), findsNothing);
    });

    testWidgets('dismisses toast manually', (tester) async {
      await pumpToastTest(
        tester,
        Builder(
          builder: (context) {
            return LemonadeButton(
              onPressed: () {
                LemonadeToastManager.show(
                  context,
                  voice: LemonadeToastVoice.success,
                  message: 'Manual dismiss',
                  duration: LemonadeToastDuration.medium,
                );
              },
              label: 'Show Toast',
            );
          },
        ),
      );

      await tester.tap(find.text('Show Toast'));
      await tester.pumpAndSettle();

      expect(find.text('Manual dismiss'), findsOneWidget);

      // Manually dismiss
      await cleanupToast(tester);

      expect(find.text('Manual dismiss'), findsNothing);
    });

    testWidgets('handles pending toast with delay', (tester) async {
      await pumpToastTest(
        tester,
        Builder(
          builder: (context) {
            return Column(
              children: [
                LemonadeButton(
                  onPressed: () {
                    LemonadeToastManager.show(
                      context,
                      voice: LemonadeToastVoice.success,
                      message: 'First toast',
                      duration: const Duration(seconds: 5),
                    );
                  },
                  label: 'First',
                ),
                LemonadeButton(
                  onPressed: () {
                    LemonadeToastManager.show(
                      context,
                      voice: LemonadeToastVoice.error,
                      message: 'Second toast',
                      duration: const Duration(seconds: 5),
                    );
                  },
                  label: 'Second',
                ),
              ],
            );
          },
        ),
      );

      // Show first toast
      await tester.tap(find.text('First'));
      await tester.pump();
      await tester.pump(const Duration(milliseconds: 100));
      expect(find.text('First toast'), findsOneWidget);

      // Trigger second toast immediately
      await tester.tap(find.text('Second'));
      await tester.pump();
      await tester.pump(const Duration(milliseconds: 100));

      // Wait for pending delay (300ms) and transition
      await tester.pump(const Duration(milliseconds: 400));
      await tester.pumpAndSettle();

      // Second toast should now be visible, first should be gone
      expect(find.text('First toast'), findsNothing);
      expect(find.text('Second toast'), findsOneWidget);

      await cleanupToast(tester);
    });

    testWidgets('shows dismissible toast with swipe gesture', (tester) async {
      await pumpToastTest(
        tester,
        Builder(
          builder: (context) {
            return LemonadeButton(
              onPressed: () {
                LemonadeToastManager.show(
                  context,
                  voice: LemonadeToastVoice.success,
                  message: 'Swipe to dismiss',
                  duration: LemonadeToastDuration.medium,
                );
              },
              label: 'Show Toast',
            );
          },
        ),
      );

      await tester.tap(find.text('Show Toast'));
      await tester.pump();
      await tester.pump(const Duration(milliseconds: 100));

      expect(find.text('Swipe to dismiss'), findsOneWidget);
      expect(find.byType(Dismissible), findsOneWidget);

      await cleanupToast(tester);
    });

    testWidgets('shows non-dismissible toast without Dismissible widget', (
      tester,
    ) async {
      await pumpToastTest(
        tester,
        Builder(
          builder: (context) {
            return LemonadeButton(
              onPressed: () {
                LemonadeToastManager.show(
                  context,
                  voice: LemonadeToastVoice.error,
                  message: 'Cannot swipe',
                  duration: LemonadeToastDuration.medium,
                  dismissible: false,
                );
              },
              label: 'Show Toast',
            );
          },
        ),
      );

      await tester.tap(find.text('Show Toast'));
      await tester.pump();
      await tester.pump(const Duration(milliseconds: 100));

      expect(find.text('Cannot swipe'), findsOneWidget);
      expect(find.byType(Dismissible), findsNothing);

      await cleanupToast(tester);
    });

    testWidgets('shows custom widget toast with showWidget', (tester) async {
      await pumpToastTest(
        tester,
        Builder(
          builder: (context) {
            return LemonadeButton(
              onPressed: () {
                LemonadeToastManager.showWidget(
                  context,
                  toast: const Text('Custom widget'),
                  duration: LemonadeToastDuration.medium,
                );
              },
              label: 'Show Toast',
            );
          },
        ),
      );

      await tester.tap(find.text('Show Toast'));
      await tester.pump();
      await tester.pump(const Duration(milliseconds: 100));

      expect(find.text('Custom widget'), findsOneWidget);

      await cleanupToast(tester);
    });

    testWidgets('respects semantic label', (tester) async {
      await pumpToastTest(
        tester,
        Builder(
          builder: (context) {
            return LemonadeButton(
              onPressed: () {
                LemonadeToastManager.show(
                  context,
                  voice: LemonadeToastVoice.success,
                  message: 'Test',
                  semanticLabel: 'Success notification',
                  duration: LemonadeToastDuration.medium,
                );
              },
              label: 'Show Toast',
            );
          },
        ),
      );

      await tester.tap(find.text('Show Toast'));
      await tester.pump();
      await tester.pump(const Duration(milliseconds: 100));

      final toastWidget = tester.widget<LemonadeToast>(
        find.byType(LemonadeToast),
      );
      expect(toastWidget.semanticLabel, equals('Success notification'));

      await cleanupToast(tester);
    });

    testWidgets('queues toasts sequentially with delay', (tester) async {
      await pumpToastTest(
        tester,
        Builder(
          builder: (context) {
            return Column(
              children: [
                LemonadeButton(
                  onPressed: () {
                    LemonadeToastManager.show(
                      context,
                      voice: LemonadeToastVoice.success,
                      message: 'Toast 0',
                      duration: const Duration(seconds: 2),
                    );
                  },
                  label: 'First',
                ),
                LemonadeButton(
                  onPressed: () {
                    LemonadeToastManager.show(
                      context,
                      voice: LemonadeToastVoice.success,
                      message: 'Toast 1',
                      duration: const Duration(seconds: 2),
                    );
                  },
                  label: 'Second',
                ),
                LemonadeButton(
                  onPressed: () {
                    LemonadeToastManager.show(
                      context,
                      voice: LemonadeToastVoice.success,
                      message: 'Toast 2',
                      duration: const Duration(seconds: 2),
                    );
                  },
                  label: 'Third',
                ),
              ],
            );
          },
        ),
      );

      // Show first toast
      await tester.tap(find.text('First'));
      await tester.pump();
      await tester.pump(const Duration(milliseconds: 100));

      // First toast should be visible immediately
      expect(find.text('Toast 0'), findsOneWidget);
      expect(find.byType(LemonadeToast), findsOneWidget);

      // Trigger second toast while first is showing
      await tester.tap(find.text('Second'));
      await tester.pump();
      await tester.pump(const Duration(milliseconds: 100));

      // First toast should still be visible (300ms delay before dismissing)
      expect(find.text('Toast 0'), findsOneWidget);
      expect(find.byType(LemonadeToast), findsOneWidget);

      // Wait for pending delay (300ms) and transition animations
      await tester.pump(const Duration(milliseconds: 400));
      await tester.pumpAndSettle();

      // Second toast should now be visible, first should be gone
      expect(find.text('Toast 0'), findsNothing);
      expect(find.text('Toast 1'), findsOneWidget);
      expect(find.byType(LemonadeToast), findsOneWidget);

      // Trigger third toast while second is showing
      await tester.tap(find.text('Third'));
      await tester.pump();
      await tester.pump(const Duration(milliseconds: 100));

      // Second toast should still be visible (300ms delay before dismissing)
      expect(find.text('Toast 1'), findsOneWidget);

      // Wait for pending delay and transition
      await tester.pump(const Duration(milliseconds: 400));
      await tester.pumpAndSettle();

      // Third toast should now be visible, second should be gone
      expect(find.text('Toast 1'), findsNothing);
      expect(find.text('Toast 2'), findsOneWidget);
      expect(find.byType(LemonadeToast), findsOneWidget);

      // Cleanup
      LemonadeToastManager.dismiss();
      await tester.pumpAndSettle(const Duration(seconds: 2));
    });
  });
}
