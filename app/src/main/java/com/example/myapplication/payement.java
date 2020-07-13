package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class payement extends AppCompatActivity implements View.OnClickListener {
    private static final int MY_SOCKET_TIMEOUT_MS = 90000;
    private Button btn;
    private String email;
    private String car;
    private String montant;
    private String ticket;
    private EditText otp;
    private TextView montant_a_payer;
    private static String URL_REGIST="http://192.168.1.100:8000/paiement_ticket";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payement);
        montant_a_payer=findViewById(R.id.montant);
        otp=findViewById(R.id.otp);
        Intent intent = getIntent();
        if (intent != null){
            String str = "";
            if (getIntent().hasExtra("email")){ // vérifie qu'une valeur est associée à la clé “edittext”
                email= getIntent().getStringExtra("email"); // on récupère la valeur associée à la clé
                Toast.makeText(getApplicationContext(),email,Toast.LENGTH_LONG).show();
            }
            if (getIntent().hasExtra("car")){ // vérifie qu'une valeur est associée à la clé “edittext”
                car= getIntent().getStringExtra("car"); // on récupère la valeur associée à la clé
                // Toast.makeText(getApplicationContext(),societe,Toast.LENGTH_LONG).show();
            }
            if (getIntent().hasExtra("montant")){ // vérifie qu'une valeur est associée à la clé “edittext”
                montant= getIntent().getStringExtra("montant"); // on récupère la valeur associée à la clé
                // Toast.makeText(getApplicationContext(),trajet,Toast.LENGTH_LONG).show();
            }

        }
        btn=findViewById(R.id.valider_paiement);
        btn.setOnClickListener(this);
        montant_a_payer.setText("Le montant à payer est de :"+' '+montant);
    }

    @Override
    public void onClick(View v) {
        if (TextUtils.isEmpty(otp.getText())) {
            otp.setError("OTP est requis");
        } else {


            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setMessage("Etes-vous sur de proceder l'achat?");
            alertDialogBuilder.setTitle("Confirmation de l'achat");
            alertDialogBuilder.setIcon(R.drawable.info);
                    alertDialogBuilder.setPositiveButton("Oui",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface arg0, int arg1) {
                                    final ProgressDialog progressDialog = new ProgressDialog(payement.this);
                                    progressDialog.setTitle("Veuillez patienter!");
                                    progressDialog.setMessage("Operation en cours...");
                                    progressDialog.setCancelable(false);
                                    progressDialog.setCanceledOnTouchOutside(false);
                                    progressDialog.show();
                                    StringRequest req = new StringRequest(Request.Method.POST, URL_REGIST,
                                            new Response.Listener<String>() {
                                                @Override
                                                public void onResponse(String response) {

                                                    try {
                                                        JSONObject test = new JSONObject(response);

                                                        String result = test.getString("status");
                                                       // result="bon";
                                                       // Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
                                                        if (result.equals("bon")) {
                                                            ticket = test.getString("ticket");
                                                            //Toast.makeText(getApplicationContext(),ticket,Toast.LENGTH_LONG).show();
                                                            progressDialog.dismiss();
                                                            Intent intent = new Intent(payement.this, ticket.class);
                                                            intent.putExtra("ticket_name",ticket);
                                                            //intent.putExtra("ticket_name",ticket);
                                                            payement.this.startActivity(intent);
                                                        } else {
                                                            progressDialog.dismiss();

                                                        }
                                                    } catch (JSONException e) {
                                                        e.printStackTrace();
                                                        Toast.makeText(payement.this, e.toString(), Toast.LENGTH_LONG).show();
                                                        progressDialog.dismiss();

                                                    }
                                                }
                                            },
                                            new Response.ErrorListener() {
                                                @Override
                                                public void onErrorResponse(VolleyError error) {
                                                    progressDialog.dismiss();
                                                    Toast.makeText(payement.this, "Erreur de connexion" + error.toString(), Toast.LENGTH_LONG).show();
                                                }
                                            }) {
                                        @Override
                                        protected Map<String, String> getParams() throws AuthFailureError {
                                            Map<String, String> params = new HashMap<>();
                                            params.put("otp", otp.getText().toString());
                                            params.put("email", email);
                                            params.put("montant", montant);
                                            params.put("id", car);
                                            return params;
                                        }
                                    };
                                    RequestQueue requestQueue = Volley.newRequestQueue(payement.this);
                                    requestQueue.add(req);
                                    req.setRetryPolicy(new DefaultRetryPolicy(
                                            MY_SOCKET_TIMEOUT_MS,
                                            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                                }
                            });

            alertDialogBuilder.setNegativeButton("Non",new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });

            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();


            //


        }

    }
}
