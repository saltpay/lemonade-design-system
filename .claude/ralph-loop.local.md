---
active: true
iteration: 2
max_iterations: 50
completion_promise: "COMPLETE"
started_at: "2026-01-30T03:24:56Z"
---

Improve the Lemonade SwiftUI Design System following modern SwiftUI best practices. SCOPE: Review and improve ALL files in swiftui/Sources/Lemonade/ EXCLUDE SampleApp folder. IMPROVEMENTS: 1) Replace foregroundColor() with foregroundStyle() 2) Replace onChange(of:) { newValue in } with onChange(of:) { oldValue, newValue in } 3) Replace LemonadeTypography() instantiation in view body with LemonadeTypography.shared 4) Replace AnyView with proper generics where possible. PROCESS: Start with Components folder, then Modifiers, then token files. After EACH file run swift build in swiftui/ directory to verify. Fix any compilation errors before proceeding. Commit working changes. FILE ORDER: LemonadeButton, LemonadeTextField, LemonadeListItem, LemonadeCard, LemonadeCheckbox, LemonadeRadioButton, LemonadeSwitch, LemonadeChip, LemonadeTag, LemonadeBadge, LemonadeIcon, LemonadeIconButton, LemonadeSearchField, LemonadeSegmentedControl, LemonadeSymbolContainer, LemonadeTile, LemonadeCountryFlag, LemonadeBrandLogo, LemonadeText, LemonadeBorder. VERIFICATION: cd swiftui && swift build. COMPLETION: All deprecated APIs replaced, no LemonadeTypography() in view bodies, AnyView removed where practical, all files compile, changes committed. Output COMPLETE when done. If stuck after 3 attempts on a file, skip and continue. If stuck 30+ min output STUCK. --max-time 3h
