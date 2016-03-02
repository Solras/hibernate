package com.example.dam.zkeep.gestion;

import android.util.Log;

import com.example.dam.zkeep.MainActivity;
import com.example.dam.zkeep.pojo.Usuario;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

public class GestionUsuario {

    public static boolean usuarioValido(Usuario u){
        URL url = null;
        BufferedReader in = null;
        String res = "";
        boolean r = false;

        try {
            url = new URL(MainActivity.IP+"Keep2/go?login="+u.getEmail()+"&pass="+u.getPass()+"&tabla=usuario&accion=&op=login&origen=android");
            in = new BufferedReader(new InputStreamReader(url.openStream()));
            String linea;
            while ((linea= in.readLine()) != null){
                res+=linea;
            }
            in.close();
            Log.v("MIAPP", "res: " + res);
            JSONObject obj = new JSONObject(res);
            Log.v("MIAPP", "usuario: "+obj.getBoolean("r"));
            r = obj.getBoolean("r");
        } catch (IOException | JSONException e) {
            Log.v("MIAPP", e.getMessage());
        }

        return r;
    }
}
