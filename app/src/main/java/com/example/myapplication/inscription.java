package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
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

public class inscription extends AppCompatActivity implements View.OnClickListener {
    private EditText nom;
    private EditText prenom;
    private EditText username;
    private EditText tel;
    private EditText password;
    private EditText email;
    private EditText cnib;
    private Button enregistrer;
    private static String URL_REGIST="http://192.168.1.100:8000/client/save";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inscription);
        nom=findViewById(R.id.nom);
        prenom=findViewById(R.id.prenom);
        username=findViewById(R.id.username);
        tel=findViewById(R.id.tel);
        email=findViewById(R.id.email);
        password=findViewById(R.id.password);
        cnib=findViewById(R.id.cnib);
        enregistrer=findViewById(R.id.enregistrer);
        enregistrer.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
if (v==enregistrer)
{
 register();
}
    }

    private void register()

    {   final String user=username.getText().toString().trim();
        final String surname=prenom.getText().toString().trim();
        final String name=nom.getText().toString().trim();
        final String mail=email.getText().toString().trim();
        final String pass=password.getText().toString().trim();
        final String telephone=tel.getText().toString().trim();
        final String identifiant=cnib.getText().toString().trim();
        final ProgressDialog progressDialog=new ProgressDialog(this);
        progressDialog.setTitle("Patientez svp...");
        if (TextUtils.isEmpty(identifiant))
        {
            Toast.makeText(this,"Numero de CNIB  est vide",Toast.LENGTH_LONG).show();
        }
        if (TextUtils.isEmpty(mail))
        {
            Toast.makeText(this,"l'adresse email est vide",Toast.LENGTH_LONG).show();
        }
        if (TextUtils.isEmpty(pass))
        {
            Toast.makeText(this,"le mot de passe est vide",Toast.LENGTH_LONG).show();
        }
        if (TextUtils.isEmpty(telephone))
        {
            Toast.makeText(this,"le telephone est vide",Toast.LENGTH_LONG).show();
        }
        if (TextUtils.isEmpty(user))
        {
            Toast.makeText(this,"le nom d'utilisateur  est vide",Toast.LENGTH_LONG).show();
        }
        if (TextUtils.isEmpty(name))
        {
            Toast.makeText(this,"le nom est vide",Toast.LENGTH_LONG).show();
        }
        if (TextUtils.isEmpty(surname))
        {
            Toast.makeText(this,"le prenom  est vide",Toast.LENGTH_LONG).show();
        }
        else
        {
            progressDialog.show();

            StringRequest req=new StringRequest(Request.Method.POST, URL_REGIST,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            progressDialog.dismiss();


                        try {
                            JSONObject obj=new JSONObject(response);
                            String succes=obj.getString("message");
                            if (succes.equals("email_existe"))
                            {
                                email.setError("Email existe deja");
                            }
                            else if (succes.equals("bon"))
                            {
                                Toast.makeText(inscription.this,"l'ennregistrement a ete fait avec succès ",Toast.LENGTH_LONG).show();
                            }
                        }
                       catch (JSONException e)
                       {
e.printStackTrace();
                           Toast.makeText(inscription.this,"Bad  v"+e.toString(),Toast.LENGTH_LONG).show();

                       }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            progressDialog.dismiss();
                            Toast.makeText(inscription.this,"Erreur de connexion"+error.toString(),Toast.LENGTH_LONG).show();
                        }
                    })
            {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String,String>params=new HashMap<>();
                    params.put("email",mail);
                    params.put("password",pass);
                    params.put("name",name);
                    params.put("surname",surname);
                    params.put("tel",telephone);
                    params.put("username",user);
                    params.put("cnib",identifiant);
                    return params;
                }
            };
            RequestQueue requestQueue= Volley.newRequestQueue(this);
            requestQueue.add(req);
        }
    }
}
