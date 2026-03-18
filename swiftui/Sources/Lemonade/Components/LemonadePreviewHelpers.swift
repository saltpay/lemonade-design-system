import SwiftUI

#if DEBUG
/// A wrapper view that provides a `@State`-backed `Binding` for use in
/// SwiftUI Previews. This avoids the need to create a dedicated preview
/// host whenever a component requires a `Binding`.
///
/// Usage:
/// ```swift
/// StatefulPreviewWrapper("") { text in
///     LemonadeUi.TextField(input: text, label: "Name")
/// }
/// ```
struct StatefulPreviewWrapper<Value, Content: View>: View {
    @State private var value: Value
    let content: (Binding<Value>) -> Content

    init(_ value: Value, @ViewBuilder content: @escaping (Binding<Value>) -> Content) {
        _value = State(initialValue: value)
        self.content = content
    }

    var body: some View {
        content($value)
    }
}
#endif
