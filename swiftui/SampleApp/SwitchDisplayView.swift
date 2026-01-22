import SwiftUI
import Lemonade

struct SwitchDisplayView: View {
    @State private var isOn1 = false
    @State private var isOn2 = true
    @State private var isOn3 = false
    @State private var isOn4 = true

    var body: some View {
        ScrollView {
            VStack(alignment: .leading, spacing: 32) {
                // Basic States
                sectionView(title: "States") {
                    HStack(spacing: 24) {
                        VStack(spacing: 8) {
                            LemonadeUi.Switch(
                                checked: false,
                                onCheckedChange: { _ in }
                            )
                            Text("Off")
                                .font(.caption)
                        }

                        VStack(spacing: 8) {
                            LemonadeUi.Switch(
                                checked: true,
                                onCheckedChange: { _ in }
                            )
                            Text("On")
                                .font(.caption)
                        }
                    }
                }

                // Interactive
                sectionView(title: "Interactive") {
                    VStack(alignment: .leading, spacing: 16) {
                        HStack {
                            Text("Dark Mode")
                            Spacer()
                            LemonadeUi.Switch(
                                checked: isOn1,
                                onCheckedChange: { isOn1 = $0 }
                            )
                        }

                        HStack {
                            Text("Notifications")
                            Spacer()
                            LemonadeUi.Switch(
                                checked: isOn2,
                                onCheckedChange: { isOn2 = $0 }
                            )
                        }
                    }
                }

                // With Label
                sectionView(title: "With Label") {
                    VStack(alignment: .leading, spacing: 16) {
                        LemonadeUi.Switch(
                            checked: isOn3,
                            onCheckedChange: { isOn3 = $0 },
                            label: "Enable push notifications"
                        )

                        LemonadeUi.Switch(
                            checked: isOn4,
                            onCheckedChange: { isOn4 = $0 },
                            label: "Auto-update apps"
                        )
                    }
                }

                // With Support Text
                sectionView(title: "With Support Text") {
                    VStack(alignment: .leading, spacing: 16) {
                        LemonadeUi.Switch(
                            checked: true,
                            onCheckedChange: { _ in },
                            label: "Location Services",
                            supportText: "Allow app to access your location"
                        )

                        LemonadeUi.Switch(
                            checked: false,
                            onCheckedChange: { _ in },
                            label: "Analytics",
                            supportText: "Help us improve by sharing anonymous usage data"
                        )
                    }
                }

                // Disabled States
                sectionView(title: "Disabled") {
                    VStack(alignment: .leading, spacing: 16) {
                        HStack {
                            Text("Disabled Off")
                                .foregroundColor(.secondary)
                            Spacer()
                            LemonadeUi.Switch(
                                checked: false,
                                onCheckedChange: { _ in },
                                enabled: false
                            )
                        }

                        HStack {
                            Text("Disabled On")
                                .foregroundColor(.secondary)
                            Spacer()
                            LemonadeUi.Switch(
                                checked: true,
                                onCheckedChange: { _ in },
                                enabled: false
                            )
                        }

                        LemonadeUi.Switch(
                            checked: true,
                            onCheckedChange: { _ in },
                            label: "Disabled with label",
                            enabled: false
                        )
                    }
                }
            }
            .padding()
        }
        .navigationTitle("Switch")
    }

    private func sectionView<Content: View>(title: String, @ViewBuilder content: () -> Content) -> some View {
        VStack(alignment: .leading, spacing: 12) {
            Text(title)
                .font(.headline)
                .foregroundColor(.secondary)

            content()
        }
    }
}

#Preview {
    NavigationView {
        SwitchDisplayView()
    }
}
