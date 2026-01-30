import SwiftUI
import Lemonade

struct IconButtonDisplayView: View {
    var body: some View {
        ScrollView {
            VStack(alignment: .leading, spacing: 32) {
                // Subtle Variant
                sectionView(title: "Subtle") {
                    VStack(spacing: 16) {
                        HStack(spacing: 12) {
                            LemonadeUi.IconButton(
                                icon: .heart,
                                contentDescription: "Small",
                                onClick: {},
                                variant: .subtle,
                                size: .small
                            )
                            LemonadeUi.IconButton(
                                icon: .heart,
                                contentDescription: "Medium",
                                onClick: {},
                                variant: .subtle,
                                size: .medium
                            )
                            LemonadeUi.IconButton(
                                icon: .heart,
                                contentDescription: "Large",
                                onClick: {},
                                variant: .subtle,
                                size: .large
                            )
                        }

                        HStack(spacing: 12) {
                            LemonadeUi.IconButton(
                                icon: .heart,
                                contentDescription: "Disabled",
                                onClick: {},
                                enabled: false,
                                variant: .subtle,
                                size: .medium
                            )
                        }
                    }
                }

                // Ghost Variant
                sectionView(title: "Ghost") {
                    VStack(spacing: 16) {
                        HStack(spacing: 12) {
                            LemonadeUi.IconButton(
                                icon: .circleX,
                                contentDescription: "Small",
                                onClick: {},
                                variant: .ghost,
                                size: .small
                            )
                            LemonadeUi.IconButton(
                                icon: .circleX,
                                contentDescription: "Medium",
                                onClick: {},
                                variant: .ghost,
                                size: .medium
                            )
                            LemonadeUi.IconButton(
                                icon: .circleX,
                                contentDescription: "Large",
                                onClick: {},
                                variant: .ghost,
                                size: .large
                            )
                        }

                        HStack(spacing: 12) {
                            LemonadeUi.IconButton(
                                icon: .circleX,
                                contentDescription: "Disabled",
                                onClick: {},
                                enabled: false,
                                variant: .ghost,
                                size: .medium
                            )
                        }
                    }
                }

                // Various Icons
                sectionView(title: "Various Icons") {
                    HStack(spacing: 12) {
                        LemonadeUi.IconButton(
                            icon: .bell,
                            contentDescription: "Notifications",
                            onClick: {},
                            variant: .subtle,
                            size: .medium
                        )
                        LemonadeUi.IconButton(
                            icon: .gear,
                            contentDescription: "Settings",
                            onClick: {},
                            variant: .subtle,
                            size: .medium
                        )
                        LemonadeUi.IconButton(
                            icon: .trash,
                            contentDescription: "Delete",
                            onClick: {},
                            variant: .subtle,
                            size: .medium
                        )
                        LemonadeUi.IconButton(
                            icon: .star,
                            contentDescription: "Bookmark",
                            onClick: {},
                            variant: .subtle,
                            size: .medium
                        )
                    }
                }
            }
            .padding()
        }
        .navigationTitle("IconButton")
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
        IconButtonDisplayView()
    }
}
