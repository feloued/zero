package com.example.myapplication;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Application;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class societe extends AppCompatActivity implements View.OnClickListener {
    private static final int MY_SOCKET_TIMEOUT_MS = 9000;
    private Spinner selectbox;
    private Button btn_societe;
    private String email;
    private TextView result;

    private String test;

    private static String URL_REGIST = "http://192.168.1.100:8000/getSociete";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_societe);
        selectbox = findViewById(R.id.selectbox);
        btn_societe = findViewById(R.id.btn_societe);
        final ArrayList<model_societe> list = new ArrayList<model_societe>();
        final ArrayList<String> liste = new ArrayList<String>();
        /*final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(societe.this);
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
            }
        }).start();*/
       // String[] items = new String[]{"TRS-Transport Sana et Rasmane", "FTS-Fairness Transport and Services"};

        btn_societe.setOnClickListener(this);
        Intent intent = getIntent();
        if (intent != null) {
            String str = "";
            if (getIntent().hasExtra("email")) { // vérifie qu'une valeur est associée à la clé “edittext”
                email = getIntent().getStringExtra("email"); // on récupère la valeur associée à la clé
                Toast.makeText(getApplicationContext(), email, Toast.LENGTH_LONG).show();
            }

        }

        JsonArrayRequest getreq = new JsonArrayRequest(URL_REGIST,
                new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {

                        for (int i=0;i<response.length();i++)
                        {
                            try {
                                JSONObject obj=response.getJSONObject(i);
                                String societe=obj.getString("societe_sigle");
                                test=societe;
                                liste.add(societe);

                               Toast.makeText(societe.this,societe, Toast.LENGTH_LONG).show();
                               // result.setText("GGHHHHHHHHHH");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, liste);
                        selectbox.setAdapter(adapter);


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(societe.this, "Erreur de connexion" + error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {

        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(getreq);
        getreq.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

    }


    @Override
    public void onClick(View v) {
      if (v == btn_societe) {

           Intent intent = new Intent(this, trajet.class);
            intent.putExtra("email", email);
            intent.putExtra("societe", selectbox.getSelectedItem().toString());
            startActivity(intent);
         // Toast.makeText(societe.this,test, Toast.LENGTH_LONG).show();

        }
    }
}
