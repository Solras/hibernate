package com.example.dam.zkeep;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.dam.zkeep.gestion.GestionUsuario;
import com.example.dam.zkeep.pojo.Keep;
import com.example.dam.zkeep.pojo.Usuario;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final String IP = "http://192.168.1.3:8080/";
//    public static final String IP = "http://192.168.208.110:8080/";
    private EditText et1, et2;
    private String user = "pepe", pass = "pepe";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        et1 = (EditText) findViewById(R.id.editText);
        et2 = (EditText) findViewById(R.id.editText2);
    }

    public class Tarea extends AsyncTask<String, Void, Boolean> {

        @Override
        protected Boolean doInBackground(String... params) {
            Boolean u = GestionUsuario.usuarioValido(new Usuario(params[0], params[1]));
            return u;
        }

        @Override
        protected void onPostExecute(Boolean res) {
            Log.v("MIAPP", "post: " + res);
            if(res){
                Intent i = new Intent(MainActivity.this, KeepsActivity.class);
                i.putExtra("user", new Usuario(user, pass));
                startActivity(i);
            }
            else{
                Toast.makeText(MainActivity.this,"Usuario incorrecto",Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void usu(View v){
//        user = et1.getText().toString();
//        pass = et2.getText().toString();
        Tarea t = new Tarea();
        t.execute(user,pass);
    }
}
