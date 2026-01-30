import SwiftUI
import Lemonade

struct BadgeDisplayView: View {
    var body: some View {
        ScrollView {
            VStack(alignment: .leading, spacing: 32) {
                // Sizes
                sectionView(title: "Sizes") {
                    HStack(spacing: 16) {
                        VStack(spacing: 8) {
                            LemonadeUi.Badge(text: "New", size: .xSmall)
                            Text("XSmall")
                                .font(.caption)
                        }

                        VStack(spacing: 8) {
                            LemonadeUi.Badge(text: "New", size: .small)
                            Text("Small")
                                .font(.caption)
                        }
                    }
                }

                // With Numbers
                sectionView(title: "With Numbers") {
                    HStack(spacing: 16) {
                        LemonadeUi.Badge(text: "1", size: .xSmall)
                        LemonadeUi.Badge(text: "5", size: .small)
                        LemonadeUi.Badge(text: "99", size: .small)
                        LemonadeUi.Badge(text: "99+", size: .small)
                    }
                }

                // Labels
                sectionView(title: "Labels") {
                    HStack(spacing: 16) {
                        LemonadeUi.Badge(text: "New", size: .small)
                        LemonadeUi.Badge(text: "Hot", size: .small)
                        LemonadeUi.Badge(text: "Sale", size: .small)
                        LemonadeUi.Badge(text: "Beta", size: .small)
                    }
                }

                // In Context
                sectionView(title: "In Context") {
                    VStack(spacing: 24) {
                        // Notification icon with badge
                        HStack(spacing: 32) {
                            ZStack(alignment: .topTrailing) {
                                LemonadeUi.Icon(
                                    icon: .bell,
                                    contentDescription: "Notifications",
                                    size: .large
                                )
                                LemonadeUi.Badge(text: "3", size: .xSmall)
                                    .offset(x: 8, y: -8)
                            }

                            ZStack(alignment: .topTrailing) {
                                LemonadeUi.Icon(
                                    icon: .envelope,
                                    contentDescription: "Messages",
                                    size: .large
                                )
                                LemonadeUi.Badge(text: "12", size: .xSmall)
                                    .offset(x: 8, y: -8)
                            }

                            ZStack(alignment: .topTrailing) {
                                LemonadeUi.Icon(
                                    icon: .shoppingBag,
                                    contentDescription: "Cart",
                                    size: .large
                                )
                                LemonadeUi.Badge(text: "99+", size: .xSmall)
                                    .offset(x: 12, y: -8)
                            }
                        }

                        // Menu item with badge
                        HStack {
                            LemonadeUi.Icon(
                                icon: .inbox,
                                contentDescription: nil,
                                size: .medium
                            )
                            Text("Inbox")
                            Spacer()
                            LemonadeUi.Badge(text: "5", size: .small)
                        }
                        .padding()
                        .background(Color(uiColor: .secondarySystemGroupedBackground))
                        .cornerRadius(12)

                        // Tab-like item with badge
                        HStack(spacing: 24) {
                            VStack(spacing: 4) {
                                ZStack(alignment: .topTrailing) {
                                    LemonadeUi.Icon(
                                        icon: .home,
                                        contentDescription: nil,
                                        size: .medium
                                    )
                                }
                                Text("Home")
                                    .font(.caption)
                            }

                            VStack(spacing: 4) {
                                ZStack(alignment: .topTrailing) {
                                    LemonadeUi.Icon(
                                        icon: .bell,
                                        contentDescription: nil,
                                        size: .medium
                                    )
                                    LemonadeUi.Badge(text: "2", size: .xSmall)
                                        .offset(x: 8, y: -8)
                                }
                                Text("Alerts")
                                    .font(.caption)
                            }

                            VStack(spacing: 4) {
                                ZStack(alignment: .topTrailing) {
                                    LemonadeUi.Icon(
                                        icon: .user,
                                        contentDescription: nil,
                                        size: .medium
                                    )
                                    LemonadeUi.Badge(text: "New", size: .xSmall)
                                        .offset(x: 12, y: -8)
                                }
                                Text("Profile")
                                    .font(.caption)
                            }
                        }
                    }
                }
            }
            .padding()
        }
        .navigationTitle("Badge")
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
        BadgeDisplayView()
    }
}
