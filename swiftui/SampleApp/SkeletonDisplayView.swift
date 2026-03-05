import SwiftUI
import Lemonade

struct SkeletonDisplayView: View {
    var body: some View {
        ScrollView {
            VStack(alignment: .leading, spacing: 32) {
                sectionView(title: "Line Skeleton") {
                    VStack(alignment: .leading, spacing: 12) {
                        ForEach(skeletonSizes, id: \.label) { item in
                            VStack(alignment: .leading, spacing: 4) {
                                Text(item.label)
                                    .font(.caption)
                                    .foregroundStyle(.content.contentSecondary)
                                LemonadeUi.LineSkeleton(size: item.size)
                            }
                        }
                    }
                }

                sectionView(title: "Circle Skeleton") {
                    HStack(spacing: 16) {
                        ForEach(skeletonSizes, id: \.label) { item in
                            VStack(spacing: 4) {
                                LemonadeUi.CircleSkeleton(size: item.size)
                                Text(item.label)
                                    .font(.caption2)
                                    .foregroundStyle(.content.contentSecondary)
                            }
                        }
                    }
                }

                sectionView(title: "Block Skeleton") {
                    LemonadeUi.BlockSkeleton()
                }

                sectionView(title: "Content Placeholder") {
                    HStack(alignment: .top, spacing: 12) {
                        LemonadeUi.CircleSkeleton(size: .large)
                        VStack(alignment: .leading, spacing: 8) {
                            LemonadeUi.LineSkeleton(size: .small)
                            LemonadeUi.LineSkeleton(size: .xSmall)
                                .frame(width: 160)
                        }
                    }
                }
            }
            .padding()
        }
        .navigationTitle("Skeleton")
    }

    private var skeletonSizes: [(label: String, size: LemonadeSkeletonSize)] {
        [
            ("XS", .xSmall),
            ("S", .small),
            ("M", .medium),
            ("L", .large),
            ("XL", .xLarge)
        ]
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
        SkeletonDisplayView()
    }
}
