import SwiftUI
import Lemonade

struct SymbolContainerDisplayView: View {
    var body: some View {
        ScrollView {
            VStack(alignment: .leading, spacing: 32) {
                // Sizes with Icon
                sectionView(title: "Sizes (Icon)") {
                    HStack(spacing: 16) {
                        VStack(spacing: 8) {
                            LemonadeUi.SymbolContainer(icon: .heart, contentDescription: nil, size: .xSmall)
                            Text("XSmall")
                                .font(.caption)
                        }

                        VStack(spacing: 8) {
                            LemonadeUi.SymbolContainer(icon: .heart, contentDescription: nil, size: .small)
                            Text("Small")
                                .font(.caption)
                        }

                        VStack(spacing: 8) {
                            LemonadeUi.SymbolContainer(icon: .heart, contentDescription: nil, size: .medium)
                            Text("Medium")
                                .font(.caption)
                        }

                        VStack(spacing: 8) {
                            LemonadeUi.SymbolContainer(icon: .heart, contentDescription: nil, size: .large)
                            Text("Large")
                                .font(.caption)
                        }

                        VStack(spacing: 8) {
                            LemonadeUi.SymbolContainer(icon: .heart, contentDescription: nil, size: .xLarge)
                            Text("XLarge")
                                .font(.caption)
                        }
                    }
                }

                // Voices
                sectionView(title: "Voices") {
                    VStack(spacing: 16) {
                        HStack(spacing: 16) {
                            VStack(spacing: 8) {
                                LemonadeUi.SymbolContainer(icon: .heart, contentDescription: nil, voice: .neutral, size: .medium)
                                Text("Neutral")
                                    .font(.caption)
                            }

                            VStack(spacing: 8) {
                                LemonadeUi.SymbolContainer(icon: .circleX, contentDescription: nil, voice: .critical, size: .medium)
                                Text("Critical")
                                    .font(.caption)
                            }

                            VStack(spacing: 8) {
                                LemonadeUi.SymbolContainer(icon: .triangleAlert, contentDescription: nil, voice: .warning, size: .medium)
                                Text("Warning")
                                    .font(.caption)
                            }

                            VStack(spacing: 8) {
                                LemonadeUi.SymbolContainer(icon: .circleInfo, contentDescription: nil, voice: .info, size: .medium)
                                Text("Info")
                                    .font(.caption)
                            }
                        }

                        HStack(spacing: 16) {
                            VStack(spacing: 8) {
                                LemonadeUi.SymbolContainer(icon: .circleCheck, contentDescription: nil, voice: .positive, size: .medium)
                                Text("Positive")
                                    .font(.caption)
                            }

                            VStack(spacing: 8) {
                                LemonadeUi.SymbolContainer(icon: .star, contentDescription: nil, voice: .brand, size: .medium)
                                Text("Brand")
                                    .font(.caption)
                            }

                            VStack(spacing: 8) {
                                LemonadeUi.SymbolContainer(icon: .star, contentDescription: nil, voice: .brandSubtle, size: .medium)
                                Text("Brand Subtle")
                                    .font(.caption)
                            }
                        }
                    }
                }

                // Text Variant
                sectionView(title: "Text Variant") {
                    HStack(spacing: 16) {
                        LemonadeUi.SymbolContainer(text: "A", voice: .neutral, size: .small)
                        LemonadeUi.SymbolContainer(text: "B", voice: .info, size: .medium)
                        LemonadeUi.SymbolContainer(text: "C", voice: .positive, size: .large)
                        LemonadeUi.SymbolContainer(text: "1", voice: .critical, size: .medium)
                        LemonadeUi.SymbolContainer(text: "99", voice: .warning, size: .large)
                    }
                }

                // Custom Content
                sectionView(title: "Custom Content") {
                    HStack(spacing: 16) {
                        LemonadeUi.SymbolContainer(voice: .neutral, size: .large) {
                            Image(systemName: "star.fill")
                                .foregroundColor(.yellow)
                        }

                        LemonadeUi.SymbolContainer(voice: .info, size: .large) {
                            Image(systemName: "person.fill")
                                .foregroundColor(LemonadeTheme.colors.content.contentInfo)
                        }
                    }
                }

                // Use Cases
                sectionView(title: "Use Cases") {
                    VStack(spacing: 16) {
                        // User avatar
                        HStack(spacing: 12) {
                            LemonadeUi.SymbolContainer(text: "JD", voice: .brand, size: .large)
                            VStack(alignment: .leading, spacing: 4) {
                                Text("John Doe")
                                    .font(.headline)
                                Text("john@example.com")
                                    .font(.subheadline)
                                    .foregroundColor(.secondary)
                            }
                            Spacer()
                        }
                        .padding()
                        .background(Color(uiColor: .secondarySystemGroupedBackground))
                        .cornerRadius(12)

                        // Status indicators
                        HStack(spacing: 24) {
                            VStack(spacing: 8) {
                                LemonadeUi.SymbolContainer(icon: .circleCheck, contentDescription: nil, voice: .positive, size: .large)
                                Text("Completed")
                                    .font(.caption)
                            }

                            VStack(spacing: 8) {
                                LemonadeUi.SymbolContainer(icon: .clock, contentDescription: nil, voice: .warning, size: .large)
                                Text("Pending")
                                    .font(.caption)
                            }

                            VStack(spacing: 8) {
                                LemonadeUi.SymbolContainer(icon: .circleX, contentDescription: nil, voice: .critical, size: .large)
                                Text("Failed")
                                    .font(.caption)
                            }
                        }
                    }
                }
            }
            .padding()
        }
        .navigationTitle("SymbolContainer")
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
        SymbolContainerDisplayView()
    }
}
