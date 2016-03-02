package com.example.dam.zkeep;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import com.db4o.config.AndroidSupport;
import com.db4o.config.EmbeddedConfiguration;
import com.db4o.query.Query;
import com.example.dam.zkeep.gestion.GestionKeep;
import com.example.dam.zkeep.pojo.Keep;
import com.example.dam.zkeep.pojo.Usuario;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class KeepsActivity extends AppCompatActivity {

    private Usuario user;
    private ListView lv;
    private ObjectContainer db;
    private Adaptador ad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_keeps);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(KeepsActivity.this, NewKeepActivity.class);
                startActivityForResult(i,0);
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
            }
        });

        lv = (ListView) findViewById(R.id.listView);
        try {
            db = Db4oEmbedded.openFile(dbConfig(), getExternalFilesDir(null) + "/keeps.db4o");
        } catch (IOException e) {
            Log.v("MIAPP", "fallo: "+e.getMessage());
        }

        user = getIntent().getParcelableExtra("user");
        Log.v("MIAPP", "usuario: " + user);

        setAdapter();

        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(final AdapterView<?> parent, View view, final int position, long id) {
                final long nid = id;
                AlertDialog.Builder b = new AlertDialog.Builder(KeepsActivity.this);
                b.setMessage("¿Borrar?");
                b.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Query q = db.query();
                        q.constrain(Keep.class);
                        ObjectSet<Keep> os = q.execute();
                        List<Keep> list = new ArrayList<>();
                        while (os.hasNext()){
                            Keep k = os.next();
                            list.add(k);
                        }
                        Keep k = list.get(position);
                        db.delete(k);
                        Toast.makeText(KeepsActivity.this, "keep "+k.getContenido(), Toast.LENGTH_SHORT).show();
                        notifyDataChanged();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();
                return false;
            }
        });
    }

    private void notifyDataChanged(){
        ad.notifyDataSetChanged();
    }

    @Override
    public void onBackPressed() {
        db.close();
        super.onBackPressed();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK){
            String texto = data.getStringExtra("texto");
            GestionKeep.addKeep(db,texto,"android");

            setAdapter();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_sync) {
            Tarea t = new Tarea();
            t.execute();
            setAdapter();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private EmbeddedConfiguration dbConfig() throws IOException {
        EmbeddedConfiguration configuration = Db4oEmbedded.newConfiguration();
        configuration.common().add(new AndroidSupport());
        configuration.common().activationDepth(25);
        configuration.common().exceptionsOnNotStorable(false);
        configuration.common().objectClass(Keep.class).objectField("id").indexed(true);
        return configuration;
    }

    public class Tarea extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... params) {
            GestionKeep.syncKeeps(user, db);
            return null;
        }

        @Override
        protected void onPostExecute(Void res) {
            Toast.makeText(KeepsActivity.this, "Sincronizado", Toast.LENGTH_SHORT).show();
        }
    }

    private void setAdapter(){
        Query q = db.query();
        q.constrain(Keep.class);
        ObjectSet<Keep> os = q.execute();
        List<Keep> list = new ArrayList<>();

        while (os.hasNext()){
            Keep k = os.next();
            if(k.getId()<0)
                db.delete(k);
            Log.v("MIAPP", "keep: "+k.toString());
            list.add(k);
        }
        ad = new Adaptador(this, (ArrayList<Keep>) list);
        lv.setAdapter(ad);
    }


}
