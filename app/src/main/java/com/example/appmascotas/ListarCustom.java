package com.example.appmascotas;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class ListarCustom extends AppCompatActivity implements MascotaAdapter.OnAccionListener {
    RecyclerView recyclerMascotas;
    MascotaAdapter adapter;
    ArrayList<Mascota> listaMascotas;
    RequestQueue requestQueue;


    private final String URL = "http://192.168.101.60:3000/mascotas/";

    private void loadUI() {
        recyclerMascotas = findViewById(R.id.recyclerMascotas);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setDecorFitsSystemWindows(false);
        setContentView(R.layout.activity_listar_custom);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            //v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        loadUI();

        //Preparar lista y adapter antes de utilizar WS
        listaMascotas = new ArrayList<>(); //vacio...
        adapter = new MascotaAdapter(this, listaMascotas, this); //Implemetar definicion de clase ...
        recyclerMascotas.setLayoutManager(new LinearLayoutManager(this));
        recyclerMascotas.setAdapter(adapter);

        //WS..
        obtenerDatos();
    }
        private void obtenerDatos(){
            requestQueue = Volley.newRequestQueue(this);

            JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                    Request.Method.GET,
                    URL,
                    null,
                    jsonArray -> renderizarLista(jsonArray),
                    error -> {
                        Log.e("ErrorWS", error.toString());
                        Toast.makeText(this, "No se obtuvieron los datos", Toast.LENGTH_SHORT).show();
                    }

            );

            requestQueue.add(jsonArrayRequest);

    }

    private void renderizarLista(JSONArray jsonMascotas){
        //Con los datos obtenidos. cargaremos la lista<Mascotas> ya que esta
        //esta vinculada al MascotaAdapter > RecyclerView
        try {
            listaMascotas.clear();

            for (int i = 0; i < jsonMascotas.length(); i++){
                //Tomaremos un JSON a la vex utilizando su indice
                JSONObject json = jsonMascotas.getJSONObject(i);
                listaMascotas.add(new Mascota(
                   json.getInt("id"),
                   json.getString("tipo"),
                   json.getString("nombre"),
                   json.getString("color"),
                   json.getDouble("pesokg")
                ));
            }//fin for

            adapter.notifyDataSetChanged();

        }catch (Exception e){
            Log.e("ErrorJSON", e.toString());
        }

    }

    @Override
    public void onEditar(int position, Mascota mascota) {
        Toast.makeText(this, "Editar: "+ mascota.getNombre(), Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onEliminar(int position, Mascota mascota) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Mascotas");
        builder.setMessage("¿Confirma que desea eliminar a " + mascota.getNombre() + "?");

        builder.setPositiveButton("Sí",(a, b) -> {
            eliminarMascota(mascota.getId(), position);
        });
        builder.setNegativeButton("No", null);

        AlertDialog dialog = builder.create();
        dialog.show();

    }

    private void eliminarMascota(int id, int position){
        requestQueue = Volley.newRequestQueue(this);

        //Su constante llamada URL, debería terminar en "/" para que se pueda concatenar el ID
        String urlEliminar = this.URL + String.valueOf(id);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.DELETE,
                urlEliminar,
                null,
                jsonObject -> {
                    try{
                        //WS Respondio correctamente (Se logré eliminar)
                        //Leemos las dos claves (key) que no envié el ws através de un JSON
                        boolean eliminado = jsonObject.getBoolean("success");
                        String mensaje = jsonObject.getString("message");

                        if (eliminado){
                            adapter.eliminarItem(position);
                            Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show();
                        }
                    }
                    catch (Exception e){
                        Log.e("ErrorJSON", e.toString());
                    }
                },
                error -> {
                    Log.e("ErrorWS", error.toString());
                    Toast.makeText(this, "No se pudo eliminar el registro", Toast.LENGTH_SHORT).show();
                }
        );
        requestQueue.add(jsonObjectRequest);
    }



}