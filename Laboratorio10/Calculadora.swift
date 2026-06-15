import Foundation

func leerNumero(mensaje: String) -> Double {
    while true {
        print(mensaje, terminator: " ")
        
        if let entrada = readLine(), let numero = Double(entrada) {
            return numero
        } else {
            print("Error: Ingrese un número válido.")
        }
    }
}

print("===== CALCULADORA EN SWIFT =====")
print("1. Suma")
print("2. Resta")
print("3. Multiplicación")
print("4. División")
print("5. Potencia")
print("6. Raíz cuadrada")

print("Seleccione una opción:", terminator: " ")

if let opcion = readLine(), let op = Int(opcion) {

    switch op {

    case 1:
        let a = leerNumero(mensaje: "Ingrese el primer número:")
        let b = leerNumero(mensaje: "Ingrese el segundo número:")
        print("Resultado: \(a + b)")

    case 2:
        let a = leerNumero(mensaje: "Ingrese el primer número:")
        let b = leerNumero(mensaje: "Ingrese el segundo número:")
        print("Resultado: \(a - b)")

    case 3:
        let a = leerNumero(mensaje: "Ingrese el primer número:")
        let b = leerNumero(mensaje: "Ingrese el segundo número:")
        print("Resultado: \(a * b)")

    case 4:
        let a = leerNumero(mensaje: "Ingrese el primer número:")
        let b = leerNumero(mensaje: "Ingrese el segundo número:")

        if b != 0 {
            print("Resultado: \(a / b)")
        } else {
            print("No se puede dividir entre cero.")
        }

    case 5:
        let base = leerNumero(mensaje: "Ingrese la base:")
        let exponente = leerNumero(mensaje: "Ingrese el exponente:")
        print("Resultado: \(pow(base, exponente))")

    case 6:
        let numero = leerNumero(mensaje: "Ingrese el número:")

        if numero >= 0 {
            print("Resultado: \(sqrt(numero))")
        } else {
            print("No existe raíz cuadrada real para números negativos.")
        }

    default:
        print("Opción no válida.")
    }

} else {
    print("Debe ingresar una opción válida.")
}