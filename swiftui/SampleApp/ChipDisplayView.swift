import SwiftUI
import Lemonade

struct ChipDisplayView: View {
    @State private var selectedChips: Set<String> = ["Option 1"]

    var body: some View {
        ScrollView(.vertical) {
            VStack(alignment: .leading, spacing: 32) {
                statesSection
                counterSection
                iconsSection
                interactiveSection
                disabledSection
            }
            .padding()
        }
        .navigationTitle("Chip")
    }

    private var statesSection: some View {
        sectionView(title: "States") {
            HStack(spacing: 12) {
                VStack(spacing: 8) {
                    LemonadeUi.Chip(label: "Unselected", selected: false)
                    Text("Unselected")
                        .font(.caption)
                }

                VStack(spacing: 8) {
                    LemonadeUi.Chip(label: "Selected", selected: true)
                    Text("Selected")
                        .font(.caption)
                }
            }
        }
    }

    private var counterSection: some View {
        sectionView(title: "With Counter") {
            HStack(spacing: 12) {
                LemonadeUi.Chip(label: "Messages", selected: false, counter: 5)
                LemonadeUi.Chip(label: "Notifications", selected: true, counter: 12)
            }
        }
    }

    private var iconsSection: some View {
        sectionView(title: "With Icons") {
            VStack(spacing: 12) {
                HStack(spacing: 12) {
                    LemonadeUi.Chip(label: "Favorites", selected: false, leadingIcon: .heart)
                    LemonadeUi.Chip(label: "Favorites", selected: true, leadingIcon: .heart)
                }

                HStack(spacing: 12) {
                    LemonadeUi.Chip(label: "Remove", selected: false, trailingIcon: .circleX)
                    LemonadeUi.Chip(label: "Remove", selected: true, trailingIcon: .circleX)
                }
            }
        }
    }

    private var interactiveSection: some View {
        sectionView(title: "Interactive Selection") {
            VStack(alignment: .leading, spacing: 12) {
                Text("Tap to select/deselect:")
                    .font(.subheadline)

                HStack(spacing: 8) {
                    chipButton(option: "Option 1")
                    chipButton(option: "Option 2")
                    chipButton(option: "Option 3")
                }

                Text("Selected: \(selectedChips.sorted().joined(separator: ", "))")
                    .font(.caption)
                    .foregroundColor(.secondary)
            }
        }
    }

    private func chipButton(option: String) -> some View {
        LemonadeUi.Chip(
            label: option,
            selected: selectedChips.contains(option),
            onChipClicked: {
                if selectedChips.contains(option) {
                    selectedChips.remove(option)
                } else {
                    selectedChips.insert(option)
                }
            }
        )
    }

    private var disabledSection: some View {
        sectionView(title: "Disabled") {
            HStack(spacing: 12) {
                LemonadeUi.Chip(label: "Disabled", selected: false, enabled: false)
                LemonadeUi.Chip(label: "Disabled", selected: true, enabled: false)
            }
        }
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
        ChipDisplayView()
    }
}
