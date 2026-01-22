import 'package:lemonade_design_system/lemonade_design_system.dart';

/// {@template LemonadeComponents}
/// Component-specific theme configurations for the Lemonade Design System.
///
/// This class wraps all component themes, providing a centralized way to
/// configure the appearance of individual components.
/// {@endtemplate}
@immutable
class LemonadeComponents {
  /// {@macro LemonadeComponents}
  const LemonadeComponents({
    required this.iconTheme,
    required this.switchTheme,
    required this.tileTheme,
    required this.badgeTheme,
    required this.brandLogoTheme,
    required this.countryFlagTheme,
    required this.toastTheme,
    required this.spinnerTheme,
    required this.textFieldTheme,
    required this.searchFieldTheme,
    required this.buttonTheme,
    required this.chipTheme,
    required this.tagTheme,
    required this.cardTheme,
    required this.symbolContainerTheme,
  });

  /// Creates a [LemonadeComponents] with default mobile configurations.
  factory LemonadeComponents.from(LemonadeTokens tokens) {
    return LemonadeComponents(
      iconTheme: LemonadeIconTheme.from(tokens),
      switchTheme: LemonadeSwitchTheme.from(tokens),
      tileTheme: LemonadeTileTheme.from(tokens),
      badgeTheme: LemonadeBadgeTheme.from(tokens),
      brandLogoTheme: LemonadeBrandLogoTheme.from(tokens),
      countryFlagTheme: LemonadeCountryFlagTheme.from(tokens),
      toastTheme: LemonadeToastTheme.from(tokens),
      spinnerTheme: LemonadeSpinnerTheme.from(tokens),
      textFieldTheme: LemonadeTextFieldTheme.from(tokens),
      searchFieldTheme: LemonadeSearchFieldTheme.from(tokens),
      buttonTheme: LemonadeButtonTheme.from(tokens),
      chipTheme: LemonadeChipTheme.from(tokens),
      tagTheme: LemonadeTagTheme.from(tokens),
      cardTheme: LemonadeCardTheme.from(tokens),
      symbolContainerTheme: LemonadeSymbolContainerTheme.from(tokens),
    );
  }

  /// Linearly interpolates between two [LemonadeComponents] objects.
  factory LemonadeComponents.lerp(
    LemonadeComponents a,
    LemonadeComponents b,
    double t,
  ) {
    if (identical(a, b)) return a;
    return LemonadeComponents(
      iconTheme: LemonadeIconTheme.lerp(a.iconTheme, b.iconTheme, t),
      switchTheme: LemonadeSwitchTheme.lerp(a.switchTheme, b.switchTheme, t),
      tileTheme: LemonadeTileTheme.lerp(a.tileTheme, b.tileTheme, t),
      badgeTheme: LemonadeBadgeTheme.lerp(a.badgeTheme, b.badgeTheme, t),
      brandLogoTheme: LemonadeBrandLogoTheme.lerp(
        a.brandLogoTheme,
        b.brandLogoTheme,
        t,
      ),
      countryFlagTheme: LemonadeCountryFlagTheme.lerp(
        a.countryFlagTheme,
        b.countryFlagTheme,
        t,
      ),
      toastTheme: LemonadeToastTheme.lerp(a.toastTheme, b.toastTheme, t),
      spinnerTheme: LemonadeSpinnerTheme.lerp(
        a.spinnerTheme,
        b.spinnerTheme,
        t,
      ),
      textFieldTheme: LemonadeTextFieldTheme.lerp(
        a.textFieldTheme,
        b.textFieldTheme,
        t,
      ),
      searchFieldTheme: LemonadeSearchFieldTheme.lerp(
        a.searchFieldTheme,
        b.searchFieldTheme,
        t,
      ),
      buttonTheme: LemonadeButtonTheme.lerp(a.buttonTheme, b.buttonTheme, t),
      chipTheme: LemonadeChipTheme.lerp(a.chipTheme, b.chipTheme, t),
      tagTheme: LemonadeTagTheme.lerp(a.tagTheme, b.tagTheme, t),
      cardTheme: LemonadeCardTheme.lerp(a.cardTheme, b.cardTheme, t),
      symbolContainerTheme: LemonadeSymbolContainerTheme.lerp(
        a.symbolContainerTheme,
        b.symbolContainerTheme,
        t,
      ),
    );
  }

  /// The icon component theme configuration.
  final LemonadeIconTheme iconTheme;

  /// The switch component theme configuration.
  final LemonadeSwitchTheme switchTheme;

  /// The tile component theme configuration.
  final LemonadeTileTheme tileTheme;

  /// The badge component theme configuration.
  final LemonadeBadgeTheme badgeTheme;

  /// The brand logo component theme configuration.
  final LemonadeBrandLogoTheme brandLogoTheme;

  /// The country flag component theme configuration.
  final LemonadeCountryFlagTheme countryFlagTheme;

  /// The toast component theme configuration.
  final LemonadeToastTheme toastTheme;

  /// The spinner component theme configuration.
  final LemonadeSpinnerTheme spinnerTheme;

  /// The text field component theme configuration.
  final LemonadeTextFieldTheme textFieldTheme;

  /// The search field component theme configuration.
  final LemonadeSearchFieldTheme searchFieldTheme;

  /// The button component theme configuration.
  final LemonadeButtonTheme buttonTheme;

  /// The chip component theme configuration.
  final LemonadeChipTheme chipTheme;

  /// The tag component theme configuration.
  final LemonadeTagTheme tagTheme;

  /// The card component theme configuration.
  final LemonadeCardTheme cardTheme;

  /// The symbol container component theme configuration.
  final LemonadeSymbolContainerTheme symbolContainerTheme;

  /// Creates a copy of this components configuration with the given fields
  /// replaced.
  LemonadeComponents copyWith({
    LemonadeIconTheme? iconTheme,
    LemonadeSwitchTheme? switchTheme,
    LemonadeTileTheme? tileTheme,
    LemonadeBadgeTheme? badgeTheme,
    LemonadeBrandLogoTheme? brandLogoTheme,
    LemonadeCountryFlagTheme? countryFlagTheme,
    LemonadeToastTheme? toastTheme,
    LemonadeSpinnerTheme? spinnerTheme,
    LemonadeTextFieldTheme? textFieldTheme,
    LemonadeSearchFieldTheme? searchFieldTheme,
    LemonadeButtonTheme? buttonTheme,
    LemonadeChipTheme? chipTheme,
    LemonadeTagTheme? tagTheme,
    LemonadeCardTheme? cardTheme,
    LemonadeSymbolContainerTheme? symbolContainerTheme,
  }) {
    return LemonadeComponents(
      iconTheme: iconTheme ?? this.iconTheme,
      switchTheme: switchTheme ?? this.switchTheme,
      tileTheme: tileTheme ?? this.tileTheme,
      badgeTheme: badgeTheme ?? this.badgeTheme,
      brandLogoTheme: brandLogoTheme ?? this.brandLogoTheme,
      countryFlagTheme: countryFlagTheme ?? this.countryFlagTheme,
      toastTheme: toastTheme ?? this.toastTheme,
      spinnerTheme: spinnerTheme ?? this.spinnerTheme,
      textFieldTheme: textFieldTheme ?? this.textFieldTheme,
      searchFieldTheme: searchFieldTheme ?? this.searchFieldTheme,
      buttonTheme: buttonTheme ?? this.buttonTheme,
      chipTheme: chipTheme ?? this.chipTheme,
      tagTheme: tagTheme ?? this.tagTheme,
      cardTheme: cardTheme ?? this.cardTheme,
      symbolContainerTheme: symbolContainerTheme ?? this.symbolContainerTheme,
    );
  }

  /// Merges this components configuration with another.
  ///
  /// Component themes are merged using their individual mergeWith methods.
  LemonadeComponents mergeWith(LemonadeComponents? other) {
    if (other == null) return this;
    return LemonadeComponents(
      iconTheme: iconTheme.mergeWith(other.iconTheme),
      switchTheme: switchTheme.mergeWith(other.switchTheme),
      tileTheme: tileTheme.mergeWith(other.tileTheme),
      badgeTheme: badgeTheme.mergeWith(other.badgeTheme),
      brandLogoTheme: brandLogoTheme.mergeWith(other.brandLogoTheme),
      countryFlagTheme: countryFlagTheme.mergeWith(other.countryFlagTheme),
      toastTheme: toastTheme.mergeWith(other.toastTheme),
      spinnerTheme: spinnerTheme.mergeWith(other.spinnerTheme),
      textFieldTheme: textFieldTheme.mergeWith(other.textFieldTheme),
      searchFieldTheme: searchFieldTheme.mergeWith(other.searchFieldTheme),
      buttonTheme: buttonTheme.mergeWith(other.buttonTheme),
      chipTheme: chipTheme.mergeWith(other.chipTheme),
      tagTheme: tagTheme.mergeWith(other.tagTheme),
      cardTheme: cardTheme.mergeWith(other.cardTheme),
      symbolContainerTheme: symbolContainerTheme.mergeWith(
        other.symbolContainerTheme,
      ),
    );
  }

  @override
  bool operator ==(Object other) {
    if (identical(this, other)) return true;
    return other is LemonadeComponents &&
        other.iconTheme == iconTheme &&
        other.switchTheme == switchTheme &&
        other.tileTheme == tileTheme &&
        other.badgeTheme == badgeTheme &&
        other.brandLogoTheme == brandLogoTheme &&
        other.countryFlagTheme == countryFlagTheme &&
        other.toastTheme == toastTheme &&
        other.spinnerTheme == spinnerTheme &&
        other.textFieldTheme == textFieldTheme &&
        other.searchFieldTheme == searchFieldTheme &&
        other.buttonTheme == buttonTheme &&
        other.chipTheme == chipTheme &&
        other.tagTheme == tagTheme &&
        other.cardTheme == cardTheme &&
        other.symbolContainerTheme == symbolContainerTheme;
  }

  @override
  int get hashCode => Object.hashAll([
    iconTheme,
    switchTheme,
    tileTheme,
    badgeTheme,
    brandLogoTheme,
    countryFlagTheme,
    toastTheme,
    spinnerTheme,
    textFieldTheme,
    searchFieldTheme,
    buttonTheme,
    chipTheme,
    tagTheme,
    cardTheme,
    symbolContainerTheme,
  ]);
}
