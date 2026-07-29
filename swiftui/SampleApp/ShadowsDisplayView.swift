import SwiftUI
import Lemonade

struct ShadowsDisplayView: View {
    var body: some View {
        ScrollView {
            LazyVStack(alignment: .leading, spacing: .space.spacing1200) {
                ForEach(LemonadeShadow.allCases, id: \.self) { shadow in
                    VStack(alignment: .leading, spacing: .space.spacing100) {
                        Text(shadow.displayName)
                            .font(.caption)
                            .foregroundStyle(.content.contentSecondary)

                        RoundedRectangle(cornerRadius: .radius.radius600)
                            .fill(.bg.bgDefault)
                            .frame(height: 100)
                            .lemonadeShadow(shadow)
                    }
                }
            }
            // The largest token (.xlarge) draws 20pt below the swatch plus a 25pt blur,
            // i.e. ~28pt outside the layout bounds. Anything less than that vertically
            // and the ScrollView clips the bottom shadow of the last swatch.
            .padding(.horizontal, .space.spacing400)
            .padding(.vertical, .space.spacing800)
        }
        .background(.bg.bgSubtle)
        .navigationTitle("Shadows")
    }
}

#Preview {
    NavigationStack {
        ShadowsDisplayView()
    }
}
