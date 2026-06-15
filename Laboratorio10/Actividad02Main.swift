import Foundation

// Función para leer números con validación
func leerNumero(mensaje: String) -> Double {
    while true {
        print(mensaje, terminator: " ")
        
        if let entrada = readLine(), let numero = Double(entrada) {
            return numero
        } else {
            print("❌ Error: Ingrese un número válido.")
        }
    }
}

// Solicitar los 3 números
let num1 = leerNumero(mensaje: "Ingrese el primer número:")
let num2 = leerNumero(mensaje: "Ingrese el segundo número:")
let num3 = leerNumero(mensaje: "Ingrese el tercer número:")

// Determinar el mayor
var mayor = num1

if num2 > mayor {
    mayor = num2
}

if num3 > mayor {
    mayor = num3
}

// Mostrar resultado
print("✅ El número mayor es: \(mayor)")