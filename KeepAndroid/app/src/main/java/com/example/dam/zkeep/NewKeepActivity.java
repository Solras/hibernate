package com.example.dam.zkeep;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class NewKeepActivity extends AppCompatActivity {

    private EditText et;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_keep);
        et = (EditText) findViewById(R.id.newKeep);
    }

    public void guardar(View v){
        this.getIntent().putExtra("texto", et.getText().toString());
        setResult(RESULT_OK, this.getIntent());
        finish();
    }
}
