import SwiftUI
import Lemonade

struct ToastDisplayView: View {
    @EnvironmentObject private var toastManager: LemonadeToastManager
    @State private var textFieldValue: String = ""
    
    @State private var counter = 0


    var body: some View {
        List {
            Section("Toast Voices") {
                Button("Show Success Toast") {
                    toastManager.show(
                        label: "Changes saved successfully",
                        voice: .success
                    )
                }

                Button("Show Error Toast") {
                    toastManager.show(
                        label: "Something went wrong",
                        voice: .error
                    )
                }

                Button("Show Neutral Toast") {
                    toastManager.show(
                        label: "Your session will expire soon",
                        voice: .neutral
                    )
                }
                
                Button("Show Long Content Toast") {
                    toastManager.show(
                        label: "Really long label that should wrap onto multiple lines to demonstrate text wrapping in the toast component",
                        voice: .neutral
                    )
                }
            }

            Section("Custom Icon (Neutral Only)") {
                Button("Toast with Heart Icon") {
                    toastManager.show(
                        label: "Added to favorites",
                        voice: .neutral,
                        icon: .heart
                    )
                }

                Button("Toast with Bell Icon") {
                    toastManager.show(
                        label: "Notifications enabled",
                        voice: .neutral,
                        icon: .bell
                    )
                }

                Button("Toast with Star Icon") {
                    toastManager.show(
                        label: "Item starred",
                        voice: .neutral,
                        icon: .star
                    )
                }
            }

            Section("Durations") {
                Button("Short Duration (3s)") {
                    toastManager.show(
                        label: "This disappears quickly",
                        voice: .neutral,
                        duration: .short
                    )
                }

                Button("Medium Duration (6s)") {
                    toastManager.show(
                        label: "This stays a bit longer",
                        voice: .neutral,
                        duration: .medium
                    )
                }

                Button("Long Duration (9s)") {
                    toastManager.show(
                        label: "This stays for a while",
                        voice: .neutral,
                        duration: .long
                    )
                }
            }

            Section("Behavior") {
                Button("Non-Dismissible Toast") {
                    toastManager.show(
                        label: "You cannot swipe this away",
                        voice: .neutral,
                        dismissible: false
                    )
                }

                Button("Queue Multiple Toasts") {
                    toastManager.show(
                        label: "First toast",
                        voice: .success
                    )
                    toastManager.show(
                        label: "Second toast",
                        voice: .neutral
                    )
                    toastManager.show(
                        label: "Third toast",
                        voice: .error
                    )
                }
            }

            Section("Keyboard Handling") {
                VStack(alignment: .leading, spacing: 12) {
                    Text("Tap the text field to open keyboard, then show a toast:")
                        .font(.caption)
                        .foregroundStyle(.secondary)

                    TextField("Type something...", text: $textFieldValue)
                        .textFieldStyle(.roundedBorder)

                    Button("Show Toast Above Keyboard") {
                        toastManager.show(
                            label: "Toast appears above keyboard",
                            voice: .success
                        )
                    }
                    .buttonStyle(.borderedProminent)
                }
                .padding(.vertical, 8)
            }

            Section("Static Previews") {
                VStack(alignment: .leading, spacing: 16) {
                    LemonadeUi.Toast(label: "Success message", voice: .success)
                    LemonadeUi.Toast(label: "Error message", voice: .error)
                    LemonadeUi.Toast(label: "Neutral message", voice: .neutral)
                    LemonadeUi.Toast(label: "With custom icon", voice: .neutral, icon: .sparkles)
                }
                .padding(.vertical, 8)
            }
        }
        .navigationTitle("Toast")
    }
}

#Preview {
    NavigationStack {
        ToastDisplayView()
    }
    .lemonadeToastContainer()
}
