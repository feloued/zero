package com.example.myapplication;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class dateHeure extends AppCompatActivity implements View.OnClickListener {
    private static final int MY_SOCKET_TIMEOUT_MS = 90000;
    EditText date;
    DatePickerDialog datePickerDialog;
    private Button btn_date;
    private String email;
    private  String societe;
    private  String id_car;
    private  String prix;
    private String trajet;
    private Spinner heure;
    private Spinner typeCar;
    private static String URL_REGIST="http://192.168.1.100:8000/client/verifyDisponibilite/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date_heure);
        date = findViewById(R.id.date);
        btn_date = findViewById(R.id.btn_date);
        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(dateHeure.this);
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

        final ArrayList<String> liste = new ArrayList<String>();
        heure=findViewById(R.id.heure_depart);
        typeCar=findViewById(R.id.type_car);
        btn_date.setOnClickListener(this);
        final ArrayList<String> typeBus = new ArrayList<String>();
        String[] items = new String[]{"Ordinaire", "Climatise"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, items);
        typeCar.setAdapter(adapter);

        date.setOnClickListener(this);
        date.setKeyListener(null);
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
            if (getIntent().hasExtra("trajet")){ // vérifie qu'une valeur est associée à la clé “edittext”
                trajet= getIntent().getStringExtra("trajet"); // on récupère la valeur associée à la clé
               // Toast.makeText(getApplicationContext(),trajet,Toast.LENGTH_LONG).show();
            }

        }
        JsonArrayRequest getreq = new JsonArrayRequest("http://192.168.1.100:8000/getHeure/"+societe,
                new Response.Listener<JSONArray>() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onResponse(JSONArray response) {
                        // Toast.makeText(trajet.this,response.toString(), Toast.LENGTH_LONG).show();
                        for (int i=0;i<response.length();i++)
                        {
                            try {

                                JSONObject obj=response.getJSONObject(i);
                                String trajet=obj.getString("heure_depart");
                                liste.add(trajet);

                                // Toast.makeText(societe.this,societe, Toast.LENGTH_LONG).show();
                                // result.setText("GGHHHHHHHHHH");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, liste);
                        heure.setAdapter(adapter);


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(getApplicationContext(), "Erreur de connexion" + error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {

        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(getreq);


    }



    @Override
    public void onClick(View v) {
        if (v == date) {
            final Calendar c = Calendar.getInstance();
            int mYear = c.get(Calendar.YEAR); // current year
            int mMonth = c.get(Calendar.MONTH); // current month
            int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
            // date picker dialog
            datePickerDialog = new DatePickerDialog(dateHeure.this,
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {
                            // set day of month , month and year value in the edit text
                            date.setText(dayOfMonth + "/"
                                    + (monthOfYear + 1) + "/" + year);

                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();
        }
        else if (v == btn_date) {
            Toast.makeText(getApplicationContext(),date.getText().toString(),Toast.LENGTH_LONG).show();
            final ProgressDialog progressDialog=new ProgressDialog(this);
            progressDialog.setTitle("Veuillez patienter!");
            progressDialog.setMessage("Verification de la disponibilite de place en cours....");
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
            if (TextUtils.isEmpty(date.getText().toString().trim())) {
                date.setError("Precisez la date");
            }

            else
            {
                progressDialog.show();
                StringRequest req = new StringRequest(Request.Method.POST, "http://192.168.1.100:8000/client/verifyDisponibilite",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            try {
                                JSONObject obj=new JSONObject(response);
                                String succes=obj.getString("message");
                                if (succes.equals("disponible"))
                                {
                                    id_car=obj.getString("id_car");
                                    prix=obj.getString("prix");
                                    progressDialog.dismiss();
                                    //Toast.makeText(dateHeure.this,email,Toast.LENGTH_LONG).show();
                                    Intent intent=new Intent(dateHeure.this,payement.class);
                                    intent.putExtra("email",email);
                                    intent.putExtra("car",id_car);
                                    intent.putExtra("montant",prix);

                                    //intent.putExtra("email",email.getText().toString());
                                    startActivity(intent);
                                }
                                else
                                {
                                    progressDialog.dismiss();
                                    Toast.makeText(dateHeure.this,"PLACE INDISPONIBLE POUR LA DATE ET L'HEURE CHOISI",Toast.LENGTH_LONG).show();
                                }
                            }
                            catch (JSONException e)
                            {
                                e.printStackTrace();
                                Toast.makeText(dateHeure.this,e.toString(),Toast.LENGTH_LONG).show();
                                progressDialog.dismiss();

                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            progressDialog.dismiss();
                            Toast.makeText(dateHeure.this, "Erreur de connexion"+error.toString(), Toast.LENGTH_LONG).show();
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("soc", societe);
                    params.put("date", date.getText().toString());
                    params.put("heure", heure.getSelectedItem().toString());
                    params.put("type",typeCar.getSelectedItem().toString());
                    return params;
                }
            };
                RequestQueue requestQueue = Volley.newRequestQueue(this);
                requestQueue.add(req);
                req.setRetryPolicy(new DefaultRetryPolicy(
                        MY_SOCKET_TIMEOUT_MS,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            }

                //Intent intent=new Intent(this,payement.class);
                //intent.putExtra("email",email);
                //intent.putExtra("societe",selectbox.getSelectedItem().toString());
               // startActivity(intent);
            }

        }
    }

