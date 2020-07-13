package com.example.myapplication;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class trajet extends AppCompatActivity implements View.OnClickListener {
    private static final int MY_SOCKET_TIMEOUT_MS = 90000;
    private Spinner selectbox;
    private Button btn_trajet;
    private String email;
    private String societe;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trajet);
        btn_trajet=findViewById(R.id.btn_trajet);
        btn_trajet.setOnClickListener(this);
        selectbox=findViewById(R.id.selectbox_trajet);
        final ArrayList<String> liste = new ArrayList<String>();
        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(trajet.this);
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
        }).start();

        Intent intent = getIntent();
        if (intent != null){
            String str = "";
            if (getIntent().hasExtra("email")){ // vérifie qu'une valeur est associée à la clé “edittext”
                email= getIntent().getStringExtra("email"); // on récupère la valeur associée à la clé
                //Toast.makeText(getApplicationContext(),email,Toast.LENGTH_LONG).show();
            }
            if (getIntent().hasExtra("societe")){ // vérifie qu'une valeur est associée à la clé “edittext”
                societe= getIntent().getStringExtra("societe"); // on récupère la valeur associée à la clé
                // Toast.makeText(getApplicationContext(),societe,Toast.LENGTH_LONG).show();
            }

        }

        JsonArrayRequest getreq = new JsonArrayRequest("http://192.168.1.100:8000/getTrajet/"+societe,
                new Response.Listener<JSONArray>() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onResponse(JSONArray response) {
                       // Toast.makeText(trajet.this,response.toString(), Toast.LENGTH_LONG).show();
                        for (int i=0;i<response.length();i++)
                        {
                            try {
                                JSONObject obj=response.getJSONObject(i);
                                String trajet=obj.getString("trajet");
                                liste.add(trajet);

                                // Toast.makeText(societe.this,societe, Toast.LENGTH_LONG).show();
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

                        Toast.makeText(trajet.this, "Erreur de connexion" + error.toString(), Toast.LENGTH_LONG).show();
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
        Intent intent=new Intent(this,dateHeure.class);
        intent.putExtra("email",email);
        intent.putExtra("societe",societe);
        intent.putExtra("trajet",selectbox.getSelectedItem().toString());
        startActivity(intent);
    }
}
