import SwiftUI
import Lemonade

struct RadioButtonDisplayView: View {
    @State private var selectedOption = 0

    var body: some View {
        ScrollView {
            VStack(alignment: .leading, spacing: 32) {
                // Basic States
                sectionView(title: "States") {
                    HStack(spacing: 24) {
                        VStack(spacing: 8) {
                            LemonadeUi.RadioButton(
                                checked: false,
                                onRadioButtonClicked: {}
                            )
                            Text("Unchecked")
                                .font(.caption)
                        }

                        VStack(spacing: 8) {
                            LemonadeUi.RadioButton(
                                checked: true,
                                onRadioButtonClicked: {}
                            )
                            Text("Checked")
                                .font(.caption)
                        }
                    }
                }

                // Interactive Group
                sectionView(title: "Interactive Group") {
                    VStack(alignment: .leading, spacing: 16) {
                        ForEach(0..<3) { index in
                            HStack(spacing: 12) {
                                LemonadeUi.RadioButton(
                                    checked: selectedOption == index,
                                    onRadioButtonClicked: { selectedOption = index }
                                )
                                Text("Option \(index + 1)")
                            }
                        }
                    }
                }

                // With Label
                sectionView(title: "With Label") {
                    VStack(alignment: .leading, spacing: 16) {
                        LemonadeUi.RadioButton(
                            checked: selectedOption == 0,
                            onRadioButtonClicked: { selectedOption = 0 },
                            label: "Free shipping"
                        )

                        LemonadeUi.RadioButton(
                            checked: selectedOption == 1,
                            onRadioButtonClicked: { selectedOption = 1 },
                            label: "Express delivery"
                        )

                        LemonadeUi.RadioButton(
                            checked: selectedOption == 2,
                            onRadioButtonClicked: { selectedOption = 2 },
                            label: "Same day delivery"
                        )
                    }
                }

                // With Support Text
                sectionView(title: "With Support Text") {
                    VStack(alignment: .leading, spacing: 16) {
                        LemonadeUi.RadioButton(
                            checked: true,
                            onRadioButtonClicked: {},
                            label: "Standard Plan",
                            supportText: "$9.99/month - Basic features"
                        )

                        LemonadeUi.RadioButton(
                            checked: false,
                            onRadioButtonClicked: {},
                            label: "Premium Plan",
                            supportText: "$19.99/month - All features included"
                        )
                    }
                }

                // Disabled States
                sectionView(title: "Disabled") {
                    VStack(alignment: .leading, spacing: 16) {
                        HStack(spacing: 24) {
                            LemonadeUi.RadioButton(
                                checked: false,
                                onRadioButtonClicked: {},
                                enabled: false
                            )
                            Text("Disabled unchecked")
                                .foregroundColor(.secondary)
                        }

                        HStack(spacing: 24) {
                            LemonadeUi.RadioButton(
                                checked: true,
                                onRadioButtonClicked: {},
                                enabled: false
                            )
                            Text("Disabled checked")
                                .foregroundColor(.secondary)
                        }

                        LemonadeUi.RadioButton(
                            checked: true,
                            onRadioButtonClicked: {},
                            label: "Disabled with label",
                            enabled: false
                        )
                    }
                }
            }
            .padding()
        }
        .navigationTitle("RadioButton")
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
        RadioButtonDisplayView()
    }
}
