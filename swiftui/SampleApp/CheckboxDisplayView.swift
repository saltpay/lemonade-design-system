import SwiftUI
import Lemonade

struct CheckboxDisplayView: View {
    @State private var isChecked1 = false
    @State private var isChecked2 = true
    @State private var isIndeterminate = true
    @State private var labeledChecked = false

    var body: some View {
        ScrollView {
            VStack(alignment: .leading, spacing: 32) {
                // Basic States
                sectionView(title: "States") {
                    VStack(alignment: .leading, spacing: 16) {
                        HStack(spacing: 24) {
                            VStack(spacing: 8) {
                                LemonadeUi.Checkbox(
                                    status: .unchecked,
                                    onCheckboxClicked: {}
                                )
                                Text("Unchecked")
                                    .font(.caption)
                            }

                            VStack(spacing: 8) {
                                LemonadeUi.Checkbox(
                                    status: .checked,
                                    onCheckboxClicked: {}
                                )
                                Text("Checked")
                                    .font(.caption)
                            }

                            VStack(spacing: 8) {
                                LemonadeUi.Checkbox(
                                    status: .indeterminate,
                                    onCheckboxClicked: {}
                                )
                                Text("Indeterminate")
                                    .font(.caption)
                            }
                        }
                    }
                }

                // Interactive
                sectionView(title: "Interactive") {
                    VStack(alignment: .leading, spacing: 16) {
                        HStack(spacing: 24) {
                            LemonadeUi.Checkbox(
                                status: isChecked1 ? .checked : .unchecked,
                                onCheckboxClicked: { isChecked1.toggle() }
                            )
                            Text("Tap to toggle")
                        }

                        HStack(spacing: 24) {
                            LemonadeUi.Checkbox(
                                status: isChecked2 ? .checked : .unchecked,
                                onCheckboxClicked: { isChecked2.toggle() }
                            )
                            Text("Initially checked")
                        }
                    }
                }

                // With Label
                sectionView(title: "With Label") {
                    VStack(alignment: .leading, spacing: 16) {
                        LemonadeUi.Checkbox(
                            status: labeledChecked ? .checked : .unchecked,
                            onCheckboxClicked: { labeledChecked.toggle() },
                            label: "Accept terms and conditions"
                        )

                        LemonadeUi.Checkbox(
                            status: .checked,
                            onCheckboxClicked: {},
                            label: "Remember me"
                        )

                        LemonadeUi.Checkbox(
                            status: .indeterminate,
                            onCheckboxClicked: {},
                            label: "Select all items"
                        )
                    }
                }

                // Disabled States
                sectionView(title: "Disabled") {
                    VStack(alignment: .leading, spacing: 16) {
                        HStack(spacing: 24) {
                            LemonadeUi.Checkbox(
                                status: .unchecked,
                                onCheckboxClicked: {},
                                enabled: false
                            )
                            Text("Disabled unchecked")
                                .foregroundStyle(.secondary)
                        }

                        HStack(spacing: 24) {
                            LemonadeUi.Checkbox(
                                status: .checked,
                                onCheckboxClicked: {},
                                enabled: false
                            )
                            Text("Disabled checked")
                                .foregroundStyle(.secondary)
                        }

                        LemonadeUi.Checkbox(
                            status: .checked,
                            onCheckboxClicked: {},
                            label: "Disabled with label",
                            enabled: false
                        )
                    }
                }
            }
            .padding()
        }
        .navigationTitle("Checkbox")
    }

    private func sectionView<Content: View>(title: String, @ViewBuilder content: () -> Content) -> some View {
        VStack(alignment: .leading, spacing: 12) {
            Text(title)
                .font(.headline)
                .foregroundStyle(.secondary)

            content()
        }
    }
}

#Preview {
    NavigationStack {
        CheckboxDisplayView()
    }
}
