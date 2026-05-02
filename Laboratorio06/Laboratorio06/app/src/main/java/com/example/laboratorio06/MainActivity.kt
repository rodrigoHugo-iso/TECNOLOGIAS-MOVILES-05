package com.example.laboratorio06

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.laboratorio06.navigation.NavGraph
import com.example.laboratorio06.ui.theme.Laboratorio06Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            Laboratorio06Theme {
                NavGraph()
            }
        }
    }
}