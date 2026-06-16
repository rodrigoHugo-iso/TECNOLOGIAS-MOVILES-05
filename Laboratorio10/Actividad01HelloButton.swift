import SwiftUI

struct ContentView: View {
    var body: some View {
        VStack(spacing: 20) {

            Text("Noah")
                .font(.largeTitle)

            Button("Presionar") {
                print("Botón presionado")
            }

        }
        .padding()
    }
}

#Preview {
    ContentView()
}