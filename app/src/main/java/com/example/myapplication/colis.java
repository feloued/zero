package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class colis extends AppCompatActivity implements View.OnClickListener {
    private Spinner selectbox;
    private Button verifier;
    private EditText code;
    ProgressDialog progressDialog;
    Handler handle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_colis);
        selectbox = findViewById(R.id.societe);
        String[] items = new String[]{"FTS", "RAHIMO"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, items);
        selectbox.setAdapter(adapter);
        code = findViewById(R.id.code_colis);
        verifier = findViewById(R.id.btn_verifier);
        verifier.setOnClickListener(this);

    }

    public void lance() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(colis.this);
        alertDialogBuilder.setMessage("Etes-vous sur de proceder l'achat?");
        alertDialogBuilder.setTitle("Confirmation de l'achat");
        alertDialogBuilder.setIcon(R.drawable.info);
        alertDialogBuilder.setNegativeButton("Non",new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    @Override
    public void onClick(View v) {
        if (v == verifier) {
           /* final ProgressDialog progressDialog;
            progressDialog = new ProgressDialog(colis.this);
            progressDialog.setMessage("Chargement des données..."); // Setting Message
            progressDialog.setTitle("Veuillez patienter"); // Setting Title
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
            progressDialog.show(); // Display Progress Dialog
            progressDialog.setCancelable(false);
            new Thread(new Runnable() {
                public void run() {
                    try {
                        Thread.sleep(5000);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    progressDialog.dismiss();
                  //  lance();
                }
            }).start();
*/
        }

    }
}
