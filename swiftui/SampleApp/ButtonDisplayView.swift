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
                            LemonadeUi.Button(label: "XSmall", onClick: {}, variant: .primary, size: .xSmall)
                            LemonadeUi.Button(label: "Small", onClick: {}, variant: .primary, size: .small)
                            LemonadeUi.Button(label: "Medium", onClick: {}, variant: .primary, size: .medium)
                            LemonadeUi.Button(label: "Large", onClick: {}, variant: .primary, size: .large)
                        }

                        HStack(spacing: 12) {
                            LemonadeUi.Button(label: "Leading", onClick: {}, leadingIcon: .heart, variant: .primary, size: .medium)
                            LemonadeUi.Button(label: "Trailing", onClick: {}, trailingIcon: .arrowRight, variant: .primary, size: .medium)
                        }

                        LemonadeUi.Button(label: "Disabled", onClick: {}, variant: .primary, size: .medium, enabled: false)

                        LemonadeUi.Button(label: "Loading", onClick: {}, variant: .primary, size: .medium, loading: true)
                    }
                }

                // Secondary Variant
                sectionView(title: "Secondary") {
                    VStack(spacing: 16) {
                        HStack(spacing: 12) {
                            LemonadeUi.Button(label: "XSmall", onClick: {}, variant: .secondary, size: .xSmall)
                            LemonadeUi.Button(label: "Small", onClick: {}, variant: .secondary, size: .small)
                            LemonadeUi.Button(label: "Medium", onClick: {}, variant: .secondary, size: .medium)
                            LemonadeUi.Button(label: "Large", onClick: {}, variant: .secondary, size: .large)
                        }

                        HStack(spacing: 12) {
                            LemonadeUi.Button(label: "Leading", onClick: {}, leadingIcon: .star, variant: .secondary, size: .medium)
                            LemonadeUi.Button(label: "Trailing", onClick: {}, trailingIcon: .chevronRight, variant: .secondary, size: .medium)
                        }

                        LemonadeUi.Button(label: "Disabled", onClick: {}, variant: .secondary, size: .medium, enabled: false)

                        LemonadeUi.Button(label: "Loading", onClick: {}, variant: .secondary, size: .medium, loading: true)
                    }
                }

                // Neutral Variant
                sectionView(title: "Neutral") {
                    VStack(spacing: 16) {
                        HStack(spacing: 12) {
                            LemonadeUi.Button(label: "XSmall", onClick: {}, variant: .neutral, size: .xSmall)
                            LemonadeUi.Button(label: "Small", onClick: {}, variant: .neutral, size: .small)
                            LemonadeUi.Button(label: "Medium", onClick: {}, variant: .neutral, size: .medium)
                            LemonadeUi.Button(label: "Large", onClick: {}, variant: .neutral, size: .large)
                        }

                        LemonadeUi.Button(label: "Disabled", onClick: {}, variant: .neutral, size: .medium, enabled: false)

                        LemonadeUi.Button(label: "Loading", onClick: {}, variant: .neutral, size: .medium, loading: true)
                    }
                }

                // Neutral Ghost Variant
                sectionView(title: "Neutral Ghost") {
                    VStack(spacing: 16) {
                        HStack(spacing: 12) {
                            LemonadeUi.Button(label: "XSmall", onClick: {}, variant: .neutral, type: .ghost, size: .xSmall)
                            LemonadeUi.Button(label: "Small", onClick: {}, variant: .neutral, type: .ghost, size: .small)
                            LemonadeUi.Button(label: "Medium", onClick: {}, variant: .neutral, type: .ghost, size: .medium)
                            LemonadeUi.Button(label: "Large", onClick: {}, variant: .neutral, type: .ghost, size: .large)
                        }

                        LemonadeUi.Button(label: "Disabled", onClick: {}, variant: .neutral, type: .ghost, size: .medium, enabled: false)

                        LemonadeUi.Button(label: "Loading", onClick: {}, variant: .neutral, type: .ghost, size: .medium, loading: true)
                    }
                }

                // Critical Variant
                sectionView(title: "Critical") {
                    VStack(spacing: 16) {
                        HStack(spacing: 12) {
                            LemonadeUi.Button(label: "XSmall", onClick: {}, variant: .critical, size: .xSmall)
                            LemonadeUi.Button(label: "Small", onClick: {}, variant: .critical, size: .small)
                            LemonadeUi.Button(label: "Medium", onClick: {}, variant: .critical, size: .medium)
                            LemonadeUi.Button(label: "Large", onClick: {}, variant: .critical, size: .large)
                        }

                        HStack(spacing: 12) {
                            LemonadeUi.Button(label: "Delete", onClick: {}, leadingIcon: .trash, variant: .critical, size: .medium)
                        }

                        LemonadeUi.Button(label: "Disabled", onClick: {}, variant: .critical, size: .medium, enabled: false)

                        LemonadeUi.Button(label: "Loading", onClick: {}, variant: .critical, size: .medium, loading: true)
                    }
                }

                // Critical Solid Variant
                sectionView(title: "Critical Solid") {
                    VStack(spacing: 16) {
                        HStack(spacing: 12) {
                            LemonadeUi.Button(label: "XSmall", onClick: {}, variant: .critical, type: .solid, size: .xSmall)
                            LemonadeUi.Button(label: "Small", onClick: {}, variant: .critical, type: .solid, size: .small)
                            LemonadeUi.Button(label: "Medium", onClick: {}, variant: .critical, type: .solid, size: .medium)
                            LemonadeUi.Button(label: "Large", onClick: {}, variant: .critical, type: .solid, size: .large)
                        }

                        HStack(spacing: 12) {
                            LemonadeUi.Button(label: "Delete", onClick: {}, leadingIcon: .trash, variant: .critical, type: .solid, size: .medium)
                        }

                        LemonadeUi.Button(label: "Disabled", onClick: {}, variant: .critical, type: .solid, size: .medium, enabled: false)

                        LemonadeUi.Button(label: "Loading", onClick: {}, variant: .critical, type: .solid, size: .medium, loading: true)
                    }
                }

                // On Brand — meant to sit on top of a brand-filled surface
                onSurfaceSection(title: "On Brand", variant: .onBrand, background: LemonadeTheme.colors.background.bgBrand)

                // On Color — meant to sit on top of a color-filled (voice) surface
                onSurfaceSection(title: "On Color", variant: .onColor, background: LemonadeTheme.colors.background.bgSubtleInverse)

                // Full Shape (pill)
                sectionView(title: "Full Shape (.fullShape())") {
                    VStack(spacing: 16) {
                        HStack(spacing: 12) {
                            LemonadeUi.Button(label: "XSmall", onClick: {}, size: .xSmall)
                                .fullShape()
                            LemonadeUi.Button(label: "Small", onClick: {}, size: .small)
                                .fullShape()
                            LemonadeUi.Button(label: "Medium", onClick: {}, size: .medium)
                                .fullShape()
                            LemonadeUi.Button(label: "Large", onClick: {}, size: .large)
                                .fullShape()
                        }

                        LemonadeUi.Button(label: "Pill with icon", onClick: {}, leadingIcon: .heart, variant: .secondary)
                            .fullShape()
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
                .foregroundStyle(.content.contentSecondary)

            content()
        }
    }

    // On Brand / On Color are single Subtle treatments meant to sit on a filled surface, so they
    // share one section shape rendered on top of the given backdrop.
    private func onSurfaceSection(title: String, variant: LemonadeButtonVariant, background: Color) -> some View {
        sectionView(title: title) {
            VStack(spacing: 16) {
                HStack(spacing: 12) {
                    LemonadeUi.Button(label: "XSmall", onClick: {}, variant: variant, size: .xSmall)
                    LemonadeUi.Button(label: "Small", onClick: {}, variant: variant, size: .small)
                    LemonadeUi.Button(label: "Medium", onClick: {}, variant: variant, size: .medium)
                    LemonadeUi.Button(label: "Large", onClick: {}, variant: variant, size: .large)
                }

                HStack(spacing: 12) {
                    LemonadeUi.Button(label: "Leading", onClick: {}, leadingIcon: .heart, variant: variant, size: .medium)
                    LemonadeUi.Button(label: "Trailing", onClick: {}, trailingIcon: .arrowRight, variant: variant, size: .medium)
                }

                LemonadeUi.Button(label: "Disabled", onClick: {}, variant: variant, size: .medium, enabled: false)

                LemonadeUi.Button(label: "Loading", onClick: {}, variant: variant, size: .medium, loading: true)
            }
            .padding(16)
            .frame(maxWidth: .infinity)
            .background(background)
            .clipShape(RoundedRectangle(cornerRadius: LemonadeTheme.radius.radius400))
        }
    }
}

#Preview {
    NavigationStack {
        ButtonDisplayView()
    }
}
