import SwiftUI
import Lemonade

struct ShadowsDisplayView: View {
    var body: some View {
        ScrollView {
            VStack(alignment: .leading, spacing: .space.spacing1200) {
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
            .padding(.space.spacing400)
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
