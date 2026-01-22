import 'package:go_router/go_router.dart';
import 'package:lemonade_example/examples/action_list_item_example.dart';
import 'package:lemonade_example/examples/badge_example.dart';
import 'package:lemonade_example/examples/brand_logo_example.dart';
import 'package:lemonade_example/examples/button_example.dart';
import 'package:lemonade_example/examples/card_example.dart';
import 'package:lemonade_example/examples/checkbox_example.dart';
import 'package:lemonade_example/examples/chip_example.dart';
import 'package:lemonade_example/examples/country_flag_example.dart';
import 'package:lemonade_example/examples/date_picker_example.dart';
import 'package:lemonade_example/examples/icon_example.dart';
import 'package:lemonade_example/examples/radio_button_example.dart';
import 'package:lemonade_example/examples/resource_list_item_example.dart';
import 'package:lemonade_example/examples/search_field_example.dart';
import 'package:lemonade_example/examples/segmented_control_example.dart';
import 'package:lemonade_example/examples/selection_list_item_example.dart';
import 'package:lemonade_example/examples/spinner_example.dart';
import 'package:lemonade_example/examples/switch_example.dart';
import 'package:lemonade_example/examples/symbol_container_example.dart';
import 'package:lemonade_example/examples/tag_example.dart';
import 'package:lemonade_example/examples/text_field_example.dart';
import 'package:lemonade_example/examples/tile_example.dart';
import 'package:lemonade_example/examples/toast_example.dart';
import 'package:lemonade_example/main.dart';

/// The router configuration for the Lemonade example application.
final router = GoRouter(
  initialLocation: '/',
  routes: [
    GoRoute(
      path: '/',
      builder: (context, state) => const MainScreen(),
    ),
    GoRoute(
      path: '/action_list_item',
      builder: (context, state) => const ActionListItemExampleScreen(),
    ),
    GoRoute(
      path: '/badge',
      builder: (context, state) => const BadgeExampleScreen(),
    ),
    GoRoute(
      path: '/brand_logo',
      builder: (context, state) => const BrandLogoExampleScreen(),
    ),
    GoRoute(
      path: '/button',
      builder: (context, state) => const ButtonExampleScreen(),
    ),
    GoRoute(
      path: '/card',
      builder: (context, state) => const CardExampleScreen(),
    ),
    GoRoute(
      path: '/checkbox',
      builder: (context, state) => const CheckboxExampleScreen(),
    ),
    GoRoute(
      path: '/chip',
      builder: (context, state) => const ChipExampleScreen(),
    ),
    GoRoute(
      path: '/country_flag',
      builder: (context, state) => const CountryFlagExampleScreen(),
    ),
    GoRoute(
      path: '/date_picker',
      builder: (context, state) => const DatePickerExampleScreen(),
    ),
    GoRoute(
      path: '/icon',
      builder: (context, state) => const IconsExampleScreen(),
    ),
    GoRoute(
      path: '/radio_button',
      builder: (context, state) => const RadioButtonExampleScreen(),
    ),
    GoRoute(
      path: '/resource_list_item',
      builder: (context, state) => const ResourceListItemScreen(),
    ),
    GoRoute(
      path: '/search_field',
      builder: (context, state) => const SearchFieldExampleScreen(),
    ),
    GoRoute(
      path: '/segmented_control',
      builder: (context, state) => const SegmentedControlExampleScreen(),
    ),
    GoRoute(
      path: '/selection_list_item',
      builder: (context, state) => const SelectionListItemExampleScreen(),
    ),
    GoRoute(
      path: '/spinner',
      builder: (context, state) => const SpinnerExampleScreen(),
    ),
    GoRoute(
      path: '/switch',
      builder: (context, state) => const SwitchExampleScreen(),
    ),
    GoRoute(
      path: '/symbol_container',
      builder: (context, state) => const SymbolContainerExampleScreen(),
    ),
    GoRoute(
      path: '/tag',
      builder: (context, state) => const TagExampleScreen(),
    ),
    GoRoute(
      path: '/text_field',
      builder: (context, state) => const TextFieldExampleScreen(),
    ),
    GoRoute(
      path: '/tile',
      builder: (context, state) => const TileExampleScreen(),
    ),
    GoRoute(
      path: '/toast',
      builder: (context, state) => const ToastExampleScreen(),
    ),
  ],
);
