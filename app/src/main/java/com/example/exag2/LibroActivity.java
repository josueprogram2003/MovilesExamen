package com.example.exag2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONException;
import org.json.JSONObject;
import com.example.exag2.model.Editorial;
import com.example.exag2.model.Libro;
import com.example.exag2.service.LibroService;
import com.example.exag2.service.apis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class LibroActivity extends AppCompatActivity {
    LibroService libroService;
    List<Editorial> listEditorial=new ArrayList<>();
    Spinner spinner;
    int clave=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_libro);


        EditText txtnombre = (EditText)findViewById(R.id.txtnombre);
        TextView nombre = (TextView) findViewById(R.id.nombre);
        EditText txtautor = (EditText)findViewById(R.id.txtAutorp);
        TextView autor = (TextView) findViewById(R.id.Autorp);
        EditText txtpaginas = (EditText)findViewById(R.id.txtPaginasp);
        TextView paginas = (TextView) findViewById(R.id.Paginasp);

        spinner = findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                ((TextView) adapterView.getChildAt(0)).setTextColor(Color.BLACK);
                ((TextView) adapterView.getChildAt(0)).setTextSize(18);

                String idCat = adapterView.getSelectedItem().toString();
                clave = Integer.parseInt(idCat.split(" ")[0]);



                Toast.makeText(getApplicationContext(), String.valueOf(clave) , Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        Button btnSave=(Button)findViewById(R.id.btnSave);
        Button btnVolver=(Button)findViewById(R.id.btnVolver);
        Button btnEliminar=(Button)findViewById(R.id.btnEliminar);

        Bundle bundle=getIntent().getExtras();
        String ide = bundle.getString("idlibro");
        String nom= bundle.getString("Titulo");
        String aut = bundle.getString("Autor");
        String pag = bundle.getString("Paginas");
        String idedit = bundle.getString("IdEditorial");
        String nombreedit = bundle.getString("Nombre");

        listspinner();
        txtnombre.setText(nom);
        txtautor.setText(aut);
        txtpaginas.setText(pag);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Libro p=new Libro();
                p.setNombre(txtnombre.getText().toString());
                p.setAutor(txtautor.getText().toString());
                p.setPaginas(Integer.valueOf(txtautor.getText().toString()));
                if(ide.trim().length()==0||ide.equals("")){
                    addLibro(p);
                    Intent intent =new Intent(LibroActivity.this,MainActivity.class);
                    startActivity(intent);
                }else{
                    updateLibro(p,Integer.valueOf(ide));
                    Intent intent =new Intent(LibroActivity.this,MainActivity.class);
                    startActivity(intent);
                }

            }
        });
        btnEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteLibro(Integer.valueOf(ide));
                Intent intent =new Intent(LibroActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });
        btnVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(LibroActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(LibroActivity.this,"Se modifico Correctamente",Toast.LENGTH_LONG).show();
            }
        });

    }
    public void deleteLibro(int id){
        libroService = apis.getLibroService();
        Call<Libro> call=libroService.deleteLibro(id);
        call.enqueue(new Callback<Libro>() {
            @Override
            public void onResponse(Call<Libro> call, Response<Libro> response) {
                if(response.isSuccessful()){
                    Toast.makeText(LibroActivity.this,"Se Elimino el registro",Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(Call<Libro> call, Throwable t) {
                Log.e("Error:",t.getMessage());
            }
        });

    }
    public void updateLibro(Libro p,int id){
        libroService= apis.getLibroService();
        Call<Libro>call=libroService.updateLibro(p,id);
        call.enqueue(new Callback<Libro>() {
            @Override
            public void onResponse(Call<Libro> call, Response<Libro> response) {
                if(response.isSuccessful()){
                    Toast.makeText(LibroActivity.this,"Se Actualizó conéxito",Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(Call<Libro> call, Throwable t) {
                Log.e("Error:",t.getMessage());
            }
        });
        Intent intent =new Intent(LibroActivity.this,MainActivity.class);
        startActivity(intent);
    }

    public void addLibro(Libro p){
        libroService= apis.getLibroService();
        Call<Libro> call=libroService.addLibro(p);
        call.enqueue(new Callback<Libro>() {
            @Override
            public void onResponse(Call<Libro> call, Response<Libro> response) {
                if(response.isSuccessful()){
                    Toast.makeText(LibroActivity.this,"Se agrego conéxito",Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(Call<Libro> call, Throwable t) {
                Log.e("Error:",t.getMessage());
            }
        });
        Intent intent =new Intent(LibroActivity.this,MainActivity.class);
        startActivity(intent);
    }
    private void listspinner(){
        libroService= apis.getLibroService();
        Call<List<Editorial>> call=libroService.getEditorial();
        call.enqueue(new Callback<List<Editorial>>() {
            @Override
            public void onResponse(Call<List<Editorial>> call, Response<List<Editorial>> response) {
                listEditorial = response.body();
                String[] s =new String[listEditorial.size()];
                for(int i=0;i<listEditorial.size();i++)
                {
                    s[i]= (listEditorial.get(i).getIdeditorial())+" "+listEditorial.get(i).getNombre();
                    final ArrayAdapter a = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_spinner_item, s);
                    a.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner.setAdapter(a);
                }
            }

            @Override
            public void onFailure(Call<List<Editorial>> call, Throwable t) {

            }
        });
    }

    private void create(String name, String marca, String stock, String precio, String idcategoria) {
        /*    libroService= apis.getLibroService();
        HashMap<String,String> map=new HashMap<>();
        map.put("nombre",name);
        map.put("marca",marca);
        map.put("stock",stock);
        map.put("precio",precio);
        map.put("idcategoria",idcategoria);

        Call<Void> call=libroService.addLibro();
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(!response.isSuccessful()) {
                    Toast toast=Toast.makeText(getApplicationContext(),response.message(),Toast.LENGTH_LONG);
                    toast.show();
                    Log.e("Response err: ", response.message());
                    return;
                }
                callMain();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast toast=Toast.makeText(getApplicationContext(),t.getMessage(),Toast.LENGTH_LONG);
                toast.show();
                Log.e("Throw err: ", t.getMessage());
            }
        });*/





    }

}