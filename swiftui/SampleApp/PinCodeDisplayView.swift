import SwiftUI
import Lemonade

struct PinCodeDisplayView: View {
    private let samplePin = "159999"

    @State private var numericPin = ""
    @State private var numericError = false
    @State private var unmaskedPin = ""
    @State private var alphaMaskedPin = ""
    @State private var alphaUnmaskedPin = ""

    var body: some View {
        ScrollView {
            VStack(alignment: .leading, spacing: 32) {
                sectionView(title: "Numeric (masked) with biometry") {
                    VStack(spacing: 16) {
                        LemonadeUi.PinCode(
                            value: $numericPin,
                            error: numericError,
                            onBiometryClick: { numericPin = samplePin },
                            onComplete: { numericError = $0 != samplePin }
                        )
                        Text("Entered: \(numericPin.count) digit(s)")
                            .font(.caption)
                            .foregroundStyle(.content.contentSecondary)
                    }
                }

                sectionView(title: "Numeric (unmasked)") {
                    LemonadeUi.PinCode(value: $unmaskedPin, masked: false)
                }

                sectionView(title: "Alphanumeric (system keyboard, masked)") {
                    LemonadeUi.PinCode(value: $alphaMaskedPin, variant: .alphanumeric)
                }

                sectionView(title: "Alphanumeric (system keyboard, unmasked)") {
                    LemonadeUi.PinCode(value: $alphaUnmaskedPin, variant: .alphanumeric, masked: false)
                }

                sectionView(title: "Submitting") {
                    LemonadeUi.PinCode(value: .constant(samplePin), submitting: true)
                }
            }
            .padding()
        }
        .navigationTitle("PinCode")
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
        PinCodeDisplayView()
    }
}
