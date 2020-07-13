package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
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

public class Connexion extends AppCompatActivity implements View.OnClickListener {
    private static final int MY_SOCKET_TIMEOUT_MS =90000 ;
    private EditText email;
    private EditText password;
    private Button login_btn;
    private Button create_btn;
    private static String URL_REGIST="http://192.168.1.100:8000/client/login";
    //private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connexion);
        email=findViewById(R.id.email);
        password=findViewById(R.id.password);
        login_btn=findViewById(R.id.login_btn);

        create_btn=findViewById(R.id.btn_create);
        create_btn.setOnClickListener(this);
        login_btn.setOnClickListener(this);


    }
    public void login() {
        final String mail = email.getText().toString().trim();
        final String pass = password.getText().toString().trim();
        final ProgressDialog progressDialog=new ProgressDialog(this);
        progressDialog.setTitle("Patientez svp...");
        if (TextUtils.isEmpty(mail)) {
            email.setError("Adresse email est requis");
        }
            else if (TextUtils.isEmpty(pass)) {
                password.setError("le mot de passe est requis");
            }
            else
        {
            progressDialog.show();
            StringRequest req = new StringRequest(Request.Method.POST, URL_REGIST,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                        try {
                            JSONObject obj=new JSONObject(response);
                            String succes=obj.getString("message");
                            if (succes.equals("connected"))
                            {
                                progressDialog.dismiss();
                                Toast.makeText(Connexion.this,"vous etes connecté",Toast.LENGTH_LONG).show();
                                Intent intent=new Intent(Connexion.this,societe.class);
                                intent.putExtra("email",email.getText().toString());
                                Connexion.this.startActivity(intent);

                            }
                        }
                       catch (JSONException e)
                       {
                            e.printStackTrace();
                           Toast.makeText(Connexion.this,"identifiants incorrects"+e.toString(),Toast.LENGTH_LONG).show();
                           progressDialog.dismiss();

                       }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            progressDialog.dismiss();
                            Toast.makeText(Connexion.this, error.toString(), Toast.LENGTH_LONG).show();
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("email", mail);
                    params.put("password", pass);
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

        }



    @Override
    public void onClick(View v) {
        if (v==login_btn)
        {
login();
        }
        if (v==create_btn)
        {
            startActivity(new Intent(this,inscription.class));
        }
        
    }
}
