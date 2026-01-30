import SwiftUI
import Lemonade

struct TagDisplayView: View {
    var body: some View {
        ScrollView {
            VStack(alignment: .leading, spacing: 32) {
                // All Voices
                sectionView(title: "Voices") {
                    VStack(alignment: .leading, spacing: 12) {
                        HStack(spacing: 8) {
                            LemonadeUi.Tag(label: "Neutral", voice: .neutral)
                            LemonadeUi.Tag(label: "Critical", voice: .critical)
                            LemonadeUi.Tag(label: "Warning", voice: .warning)
                        }

                        HStack(spacing: 8) {
                            LemonadeUi.Tag(label: "Info", voice: .info)
                            LemonadeUi.Tag(label: "Positive", voice: .positive)
                        }
                    }
                }

                // With Icons
                sectionView(title: "With Icons") {
                    VStack(alignment: .leading, spacing: 12) {
                        LemonadeUi.Tag(label: "Neutral", icon: .heart, voice: .neutral)
                        LemonadeUi.Tag(label: "Error", icon: .circleX, voice: .critical)
                        LemonadeUi.Tag(label: "Warning", icon: .triangleAlert, voice: .warning)
                        LemonadeUi.Tag(label: "Info", icon: .circleInfo, voice: .info)
                        LemonadeUi.Tag(label: "Success", icon: .circleCheck, voice: .positive)
                    }
                }

                // Use Cases
                sectionView(title: "Use Cases") {
                    VStack(alignment: .leading, spacing: 16) {
                        // Status tags
                        HStack(spacing: 8) {
                            Text("Order Status:")
                            LemonadeUi.Tag(label: "Shipped", icon: .check, voice: .positive)
                        }

                        HStack(spacing: 8) {
                            Text("Payment:")
                            LemonadeUi.Tag(label: "Pending", voice: .warning)
                        }

                        HStack(spacing: 8) {
                            Text("Account:")
                            LemonadeUi.Tag(label: "Verified", icon: .circleCheck, voice: .info)
                        }

                        // Category tags
                        VStack(alignment: .leading, spacing: 8) {
                            Text("Categories:")
                                .font(.subheadline)

                            HStack(spacing: 8) {
                                LemonadeUi.Tag(label: "Electronics", voice: .neutral)
                                LemonadeUi.Tag(label: "Sale", voice: .critical)
                                LemonadeUi.Tag(label: "New", voice: .positive)
                            }
                        }
                    }
                }

                // In Context
                sectionView(title: "In Context") {
                    VStack(spacing: 16) {
                        // Product card example
                        HStack(alignment: .top, spacing: 12) {
                            RoundedRectangle(cornerRadius: 8)
                                .fill(Color.gray.opacity(0.2))
                                .frame(width: 60, height: 60)

                            VStack(alignment: .leading, spacing: 4) {
                                HStack {
                                    Text("Product Name")
                                        .font(.headline)
                                    LemonadeUi.Tag(label: "New", voice: .positive)
                                }

                                Text("$99.99")
                                    .foregroundStyle(.secondary)

                                HStack(spacing: 4) {
                                    LemonadeUi.Tag(label: "In Stock", voice: .info)
                                    LemonadeUi.Tag(label: "Free Shipping", voice: .neutral)
                                }
                            }

                            Spacer()
                        }
                        .padding()
                        .background(Color(uiColor: .secondarySystemGroupedBackground))
                        .cornerRadius(12)
                    }
                }
            }
            .padding()
        }
        .navigationTitle("Tag")
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
        TagDisplayView()
    }
}
