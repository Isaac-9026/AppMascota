package com.example.appmascotas;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.Network;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class Registrar extends AppCompatActivity {

    String tipo, nombre, color;
    double peso;

    EditText edtTipo, edtNombre, edtColor, edtPeso;

    Button btnRegistrarMascota;

    //Enviar / recibir los datos hacia el servicio
    RequestQueue requestQueue;

    //URL
    private final String URL = "http://192.168.101.63:3000/mascotas/";

    private void loadUI(){
        edtTipo = findViewById(R.id.edtTipo);
        edtNombre = findViewById(R.id.edtNombre);
        edtColor = findViewById(R.id.edtColor);
        edtPeso = findViewById(R.id.edtPeso);
        btnRegistrarMascota = findViewById(R.id.btnRegistrarMascota);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_registrar);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            //v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //Metodo con referencias
        loadUI();

        //Eventos
        btnRegistrarMascota.setOnClickListener((v) -> { validarRegistro(); });
    }

    private  void resetUI(){
        edtTipo.setText(null);
        edtNombre.setText(null);
        edtColor.setText(null);
        edtPeso.setText(null);
    }

    private void validarRegistro(){
        if (edtTipo.getText().toString().isEmpty()){
            edtTipo.setError("Complete con Perro, Gato");
            edtTipo.requestFocus();
            return;
        }

        if (edtNombre.getText().toString().isEmpty()){
            edtNombre.setError("Escriba el nombre");
            edtNombre.requestFocus();
            return;
        }

        if (edtColor.getText().toString().isEmpty()){
            edtColor.setError("Este campo es obligatorio");
            edtColor.requestFocus();
            return;
        }

        if (edtPeso.getText().toString().isEmpty()){
            edtPeso.setError("Ingrese un valor");
            edtPeso.requestFocus();
            return;
        }

        tipo = edtTipo.getText().toString().trim();
        nombre = edtNombre.getText().toString().trim();
        color = edtColor.getText().toString().trim();
        double peso = Double.parseDouble(edtPeso.getText().toString());

        //Error SI no es Perro o Gato
        if (!tipo.equals("Perro") && !tipo.equals("Gato")){
            edtTipo.setError("Solo se permite: Perro, Gato");
            edtTipo.requestFocus();
        }

        if (peso < 0){
            edtPeso.setError("Solo se permite valores positivos");
            edtPeso.requestFocus();
            return;
        }

        //Solicitar la confirmación
        AlertDialog.Builder builder = new AlertDialog.Builder(Registrar.this);
        builder.setTitle("Mascotas");
        builder.setMessage("¿Seguro de registrar?");

        builder.setPositiveButton("Sí", (a,b) -> {
            registrarMascota();
            Toast.makeText(getApplicationContext(), "Guardado", Toast.LENGTH_SHORT).show();
        });
        builder.setNegativeButton("No", null);

        AlertDialog dialog = builder.create();
        dialog.show();

    }

    private void registrarMascota(){
        //Comunicacion
        requestQueue = Volley.newRequestQueue(this);

        //POST = requiere JSON(Datos a enviar)
        JSONObject jsonObject = new JSONObject();

        //Asignar los valores de las cajas
        try{
            jsonObject.put("tipo", tipo);
            jsonObject.put("nombre", nombre);
            jsonObject.put("color", color);
            jsonObject.put("pesokg", peso);
        }catch (JSONException e){
            Log.e("Error", e.toString());
        }

        Log.d("ValoresWS", jsonObject.toString());

        //Definir un objeto (respuesta obtener)
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                URL,
                jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        //EXITO
                        Log.d("Resultado", jsonObject.toString());
                        Toast.makeText(getApplicationContext(), "Guardado correctamente", Toast.LENGTH_SHORT).show();
                        resetUI();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        NetworkResponse response = volleyError.networkResponse;

                        //evaluar por codigo de error
                        if (response != null && response.data != null){
                            //capturar el codigo de error 4xx, 5xx
                            int statusCode = response.statusCode;
                            String errorJson = new String(response.data);

                            Log.e("VolleyError", "Código: "+ statusCode);
                            Log.e("VolleyError", "Cuerpo" + errorJson);
                        }else{
                            Log.e("VolleyError","sin respuesta de red");
                        }
                    }
                }
        );

        //Ejecutamos el proceso
        requestQueue.add(jsonObjectRequest);
    }
}