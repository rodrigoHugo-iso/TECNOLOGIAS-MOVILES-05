package com.example.conversorjava;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private EditText editTextCantidad;
    private RadioButton radioDolares;
    private RadioButton radioSoles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextCantidad = findViewById(R.id.editTextText);
        radioDolares = findViewById(R.id.radio0);
        radioSoles = findViewById(R.id.radio1);
    }

    public void miClicManejador(View view) {
        if (view.getId() == R.id.bntConvertir) {
            convertirMoneda();
        }
    }

    private void convertirMoneda() {
        String texto = editTextCantidad.getText().toString().trim();

        if (texto.isEmpty()) {
            Toast.makeText(this, "Ingresa una cantidad", Toast.LENGTH_SHORT).show();
            return;
        }

        float cantidad;
        try {
            cantidad = Float.parseFloat(texto);
        } catch (Exception e) {
            Toast.makeText(this, "Número inválido", Toast.LENGTH_SHORT).show();
            return;
        }

        if (cantidad <= 0) {
            Toast.makeText(this, "Número inválido", Toast.LENGTH_SHORT).show();
            return;
        }

        float tipoCambio = 3.41f;

        if (radioDolares.isChecked()) {
            float resultado = cantidad * tipoCambio;
            Toast.makeText(
                    this,
                    String.format("%.2f dólares = %.2f soles", cantidad, resultado),
                    Toast.LENGTH_LONG
            ).show();

        } else if (radioSoles.isChecked()) {
            float resultado = cantidad / tipoCambio;
            Toast.makeText(
                    this,
                    String.format("%.2f soles = %.2f dólares", cantidad, resultado),
                    Toast.LENGTH_LONG
            ).show();

        } else {
            Toast.makeText(this, "Selecciona una opción", Toast.LENGTH_SHORT).show();
        }
    }
}