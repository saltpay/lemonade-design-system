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
                            LemonadeUi.Badge(text: "New")
                                .badgeSize(.xSmall)
                            Text("XSmall")
                                .font(.caption)
                        }
                        
                        VStack(spacing: 8) {
                            LemonadeUi.Badge(text: "New")
                                .badgeSize(.small)
                            Text("Small")
                                .font(.caption)
                        }
                    }
                }
                
                // With Numbers
                sectionView(title: "With Numbers") {
                    HStack(spacing: 16) {
                        LemonadeUi.Badge(text: "1")
                            .badgeSize(.xSmall)
                        LemonadeUi.Badge(text: "5")
                            .badgeSize(.small)
                        LemonadeUi.Badge(text: "99")
                            .badgeSize(.small)
                        LemonadeUi.Badge(text: "99+")
                            .badgeSize(.small)
                    }
                }
                
                // Labels
                sectionView(title: "Labels") {
                    HStack(spacing: 16) {
                        LemonadeUi.Badge(text: "New")
                            .badgeSize(.xSmall)
                        LemonadeUi.Badge(text: "Hot")
                            .badgeSize(.xSmall)
                        LemonadeUi.Badge(text: "Sale")
                            .badgeSize(.xSmall)
                        LemonadeUi.Badge(text: "Beta")
                            .badgeSize(.xSmall)
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
                                LemonadeUi.Badge(text: "3")
                                    .badgeSize(.xSmall)
                                    .offset(x: 8, y: -8)
                            }
                            
                            ZStack(alignment: .topTrailing) {
                                LemonadeUi.Icon(
                                    icon: .envelope,
                                    contentDescription: "Messages",
                                    size: .large
                                )
                                LemonadeUi.Badge(text: "12")
                                    .badgeSize(.xSmall)
                                    .offset(x: 8, y: -8)
                            }
                            
                            ZStack(alignment: .topTrailing) {
                                LemonadeUi.Icon(
                                    icon: .shoppingBag,
                                    contentDescription: "Cart",
                                    size: .large
                                )
                                LemonadeUi.Badge(text: "99+")
                                    .badgeSize(.xSmall)
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
                            LemonadeUi.Badge(text: "5")
                                .badgeSize(.small)
                        }
                        .padding()
                        .background(.bg.bgSubtle)
                        .clipShape(.rect(cornerRadius: 12))
                        
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
                                    LemonadeUi.Badge(text: "2")
                                        .badgeSize(.xSmall)
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
                                    LemonadeUi.Badge(text: "New")
                                        .badgeSize(.xSmall)
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
                .foregroundStyle(.content.contentSecondary)
            
            content()
        }
    }
}

#Preview {
    NavigationStack {
        BadgeDisplayView()
    }
}
