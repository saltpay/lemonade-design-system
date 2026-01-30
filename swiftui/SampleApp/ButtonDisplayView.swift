import SwiftUI
import Lemonade

struct ButtonDisplayView: View {
    var body: some View {
        ScrollView {
            VStack(alignment: .leading, spacing: 32) {
                // Primary Variant
                sectionView(title: "Primary") {
                    VStack(spacing: 16) {
                        HStack(spacing: 12) {
                            LemonadeUi.Button(label: "Small", onClick: {}, variant: .primary, size: .small)
                            LemonadeUi.Button(label: "Medium", onClick: {}, variant: .primary, size: .medium)
                            LemonadeUi.Button(label: "Large", onClick: {}, variant: .primary, size: .large)
                        }

                        HStack(spacing: 12) {
                            LemonadeUi.Button(label: "Leading", onClick: {}, leadingIcon: .heart, variant: .primary, size: .medium)
                            LemonadeUi.Button(label: "Trailing", onClick: {}, trailingIcon: .arrowRight, variant: .primary, size: .medium)
                        }

                        LemonadeUi.Button(label: "Disabled", onClick: {}, variant: .primary, size: .medium, enabled: false)
                    }
                }

                // Secondary Variant
                sectionView(title: "Secondary") {
                    VStack(spacing: 16) {
                        HStack(spacing: 12) {
                            LemonadeUi.Button(label: "Small", onClick: {}, variant: .secondary, size: .small)
                            LemonadeUi.Button(label: "Medium", onClick: {}, variant: .secondary, size: .medium)
                            LemonadeUi.Button(label: "Large", onClick: {}, variant: .secondary, size: .large)
                        }

                        HStack(spacing: 12) {
                            LemonadeUi.Button(label: "Leading", onClick: {}, leadingIcon: .star, variant: .secondary, size: .medium)
                            LemonadeUi.Button(label: "Trailing", onClick: {}, trailingIcon: .chevronRight, variant: .secondary, size: .medium)
                        }

                        LemonadeUi.Button(label: "Disabled", onClick: {}, variant: .secondary, size: .medium, enabled: false)
                    }
                }

                // Neutral Variant
                sectionView(title: "Neutral") {
                    VStack(spacing: 16) {
                        HStack(spacing: 12) {
                            LemonadeUi.Button(label: "Small", onClick: {}, variant: .neutral, size: .small)
                            LemonadeUi.Button(label: "Medium", onClick: {}, variant: .neutral, size: .medium)
                            LemonadeUi.Button(label: "Large", onClick: {}, variant: .neutral, size: .large)
                        }

                        LemonadeUi.Button(label: "Disabled", onClick: {}, variant: .neutral, size: .medium, enabled: false)
                    }
                }

                // Critical Variant
                sectionView(title: "Critical") {
                    VStack(spacing: 16) {
                        HStack(spacing: 12) {
                            LemonadeUi.Button(label: "Small", onClick: {}, variant: .critical, size: .small)
                            LemonadeUi.Button(label: "Medium", onClick: {}, variant: .critical, size: .medium)
                            LemonadeUi.Button(label: "Large", onClick: {}, variant: .critical, size: .large)
                        }

                        HStack(spacing: 12) {
                            LemonadeUi.Button(label: "Delete", onClick: {}, leadingIcon: .trash, variant: .critical, size: .medium)
                        }

                        LemonadeUi.Button(label: "Disabled", onClick: {}, variant: .critical, size: .medium, enabled: false)
                    }
                }

                // Special Variant
                sectionView(title: "Special") {
                    VStack(spacing: 16) {
                        HStack(spacing: 12) {
                            LemonadeUi.Button(label: "Small", onClick: {}, variant: .special, size: .small)
                            LemonadeUi.Button(label: "Medium", onClick: {}, variant: .special, size: .medium)
                            LemonadeUi.Button(label: "Large", onClick: {}, variant: .special, size: .large)
                        }

                        LemonadeUi.Button(label: "Disabled", onClick: {}, variant: .special, size: .medium, enabled: false)
                    }
                }
            }
            .padding()
        }
        .navigationTitle("Button")
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
        ButtonDisplayView()
    }
}
