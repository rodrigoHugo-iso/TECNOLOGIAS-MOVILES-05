import SwiftUI

struct ContentView: View {

    @State private var numero1 = ""
    @State private var numero2 = ""
    @State private var resultado = ""

    var body: some View {
        VStack(spacing: 15) {

            Text("Calculadora")
                .font(.largeTitle)

            TextField("Primer número", text: $numero1)
                .textFieldStyle(.roundedBorder)

            TextField("Segundo número", text: $numero2)
                .textFieldStyle(.roundedBorder)

            Button("Sumar") {
                if let n1 = Double(numero1),
                   let n2 = Double(numero2) {
                    resultado = "\(n1 + n2)"
                }
            }

            Button("Restar") {
                if let n1 = Double(numero1),
                   let n2 = Double(numero2) {
                    resultado = "\(n1 - n2)"
                }
            }

            Button("Multiplicar") {
                if let n1 = Double(numero1),
                   let n2 = Double(numero2) {
                    resultado = "\(n1 * n2)"
                }
            }

            Button("Dividir") {
                if let n1 = Double(numero1),
                   let n2 = Double(numero2),
                   n2 != 0 {
                    resultado = "\(n1 / n2)"
                } else {
                    resultado = "Error en la división"
                }
            }

            Button("Potencia") {
                if let n1 = Double(numero1),
                   let n2 = Double(numero2) {
                    resultado = "\(pow(n1, n2))"
                }
            }

            Button("Raíz Cuadrada") {
                if let n1 = Double(numero1),
                   n1 >= 0 {
                    resultado = "\(sqrt(n1))"
                } else {
                    resultado = "Número inválido"
                }
            }

            Text("Resultado: \(resultado)")
                .font(.title3)

        }
        .padding()
    }
}

#Preview {
    ContentView()
}