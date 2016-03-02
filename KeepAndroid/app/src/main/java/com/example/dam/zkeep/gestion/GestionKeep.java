package com.example.dam.zkeep.gestion;

import android.util.Log;

import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import com.db4o.query.Query;
import com.example.dam.zkeep.MainActivity;
import com.example.dam.zkeep.pojo.Keep;
import com.example.dam.zkeep.pojo.Usuario;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class GestionKeep {

    public static void syncKeeps(Usuario u, ObjectContainer db){
        URL url;
        BufferedReader in;
        String res = "";

        try {
            url = new URL( MainActivity.IP+"Keep2/go?tabla=keep&op=read&origen=android&accion=&login="+u.getEmail());
            in = new BufferedReader(new InputStreamReader(url.openStream()));
            String linea;
            while ((linea= in.readLine()) != null) {
                res+=linea;
            }
            in.close();
            JSONObject obj = new JSONObject(res);
            Log.v("MIAPP", "obj: "+obj.toString());
            JSONArray array = (JSONArray) obj.get("r");
            for (int i = 0; i < array.length(); i++) {
                obj = (JSONObject) array.get(i);
                Log.v("MIAPP", "obj: "+obj.toString());
                if(obj.getString("est").compareTo("inestable")==0) {
                    int id = addKeep(db,obj.getString("cont"), "web");
                    url = new URL( MainActivity.IP+"Keep2/go?tabla=keep&op=update&origen=android&accion=&id="+obj.getInt("id")+"&contenido="+obj.getString("cont")+
                            "&idAndroid="+id+"&estado=estable");
                    new BufferedReader(new InputStreamReader(url.openStream()));
                }
                Query q;
                ObjectSet<Keep> os;

                q = db.query();
                q.constrain(Keep.class);
                q.descend("id").constrain(obj.getInt("ida"));
                os = q.execute();

                if(os.hasNext()){
                    Keep k = os.next();
                    k.setContenido(obj.getString("cont"));
                }

                q = db.query();
                q.constrain(Keep.class);
                q.descend("estado").constrain(false);
                os = q.execute();

                while (os.hasNext()){
                    Keep k = os.next();
                    url = new URL( MainActivity.IP+"Keep2/go?tabla=keep&op=create&origen=android&accion=&idAndroid="+k.getId()+
                            "&contenido="+k.getContenido()+"&login="+u.getEmail());
                    new BufferedReader(new InputStreamReader(url.openStream()));
                    k.setEstado(true);
                }
            }
        } catch (IOException e) {
            Log.v("MIAPP", "fallo IO: "+e.getMessage());
        } catch (JSONException e) {
            Log.v("MIAPP", "fallo JSON: "+e.getMessage());
        }
    }

    private static int newId(ObjectSet<Keep> lista){
        int id = Integer.MIN_VALUE;
        while(lista.hasNext()){
            Keep k = lista.next();
            if(k.getId()>id){
                id = k.getId();
            }
        }
        if(id < 0) {
            return 0;
        }
        return id+1;
    }

    public static int addKeep(ObjectContainer db, String texto, String origen){
        Query consulta = db.query();
        consulta.constrain(Keep.class);
        ObjectSet<Keep> keeps = consulta.execute();
        int id = newId(keeps);
        switch(origen){
            case "android": {
                db.store(new Keep(id,texto,false));
            }break;
            case "web": {
                db.store(new Keep(id,texto,true));
            }break;
        }
        db.commit();
        return id;
    }
}
