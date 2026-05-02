package com.example.laboratorio06.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun PantallaFormulario(navController: NavController) {

    var nombre by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {

        OutlinedTextField(
            value = nombre,
            onValueChange = { if (it.length <= 8) nombre = it },
            label = { Text("Ingresa tu nombre") },
            supportingText = { Text("${nombre.length}/8") },
            trailingIcon = {
                if (nombre.isNotEmpty()) {
                    IconButton(onClick = { nombre = "" }) {
                        Icon(Icons.Default.Clear, contentDescription = "Limpiar")
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                navController.navigate("bienvenida/$nombre")
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Continuar")
        }
    }
}