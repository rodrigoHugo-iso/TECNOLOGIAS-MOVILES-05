package com.example.laboratorio06.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.*
import com.example.laboratorio06.screens.PantallaFormulario
import com.example.laboratorio06.screens.PantallaBienvenida

@Composable
fun NavGraph() {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "formulario"
    ) {

        composable("formulario") {
            PantallaFormulario(navController)
        }

        composable("bienvenida/{nombre}") { backStackEntry ->
            val nombre = backStackEntry.arguments?.getString("nombre") ?: ""
            PantallaBienvenida(nombre)
        }
    }
}