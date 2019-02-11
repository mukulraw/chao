package app.in.chaiwale.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import app.in.chaiwale.API_URL;
import app.in.chaiwale.Main.MainNavigation;
import app.in.chaiwale.R;


public class Login extends AppCompatActivity implements View.OnClickListener {
    ProgressDialog progressDialog;
    Button Submit_Btn;
    RequestQueue requestQueue;
    EditText Phone_No;


    SessionManager sessionManager;
    EditText otp;
    String otp1;
    String useid;

    LinearLayout otplayout;


    String name1="";
    private int phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);
        requestQueue = Volley.newRequestQueue(this);
        Submit_Btn=findViewById(R.id.Submit_Btn);
        Submit_Btn.setOnClickListener(this);
        Phone_No=findViewById(R.id.Phone_No);


        otp=findViewById(R.id.otp);
        otplayout=findViewById(R.id.otplayout);
        otplayout.setVisibility(View.INVISIBLE);




        sessionManager=new SessionManager(this);
        HashMap<String, String> user = sessionManager.getUserDetails();
        final String Phone = user.get(sessionManager.KEY_Phone);

        String useid= user.get(SessionManager.KEY_UserID);

       // Toast.makeText(this, ""+useid, Toast.LENGTH_SHORT).show();

        if (useid!=null)
        {
            startActivity(new Intent(Login.this,Profile.class));
            Login.this.finish();
        }
        else {

        }


/*
            Submit_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Intent intent=new Intent(Login.this,Profile.class);
               // startActivity(intent);


                if (Phone_No.getText().toString().isEmpty())
                {
                    Toast.makeText(Login.this, "Please Enter Valid No", Toast.LENGTH_SHORT).show();
                }
                else if (otp.getText().toString().equalsIgnoreCase(otp1))
                {
                    if (name1.equalsIgnoreCase(""))
                    {
                        Intent intent=new Intent(Login.this,Profile.class);
                        startActivity(intent);
                        Login.this.finish();
                    }
                    else
                    {
                        Intent intent=new Intent(Login.this,MainNavigation.class);
                        startActivity(intent);
                        Login.this.finish();
                    }




                }
                else

                {
                    getLoginVerify();
                }
            }
        });
*/
    }

    public void generate_opt() {

        progressDialog = progressDialog.show(Login.this, "", "Please wait...", false, false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, API_URL.generate_opt, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

                Log.e("respoance",s);
                progressDialog.dismiss();

                otplayout.setVisibility(View.VISIBLE);
                try {

                    JSONObject jsonObject1=new JSONObject(s);
                    String message= jsonObject1.getString("message");

                    JSONObject jsonObject11=jsonObject1.getJSONObject("data");
                  //  useid = jsonObject11.getString("userId");
                    useid = jsonObject11.getString("1");
                    String Phone1 = jsonObject11.getString("phone");
                    otp1 = jsonObject11.getString("otp");

                    //Toast.makeText(Login.this, ""+otp1, Toast.LENGTH_SHORT).show();

                    sessionManager.createLoginSession(""+Phone1,""+useid);
                    sessionManager.getUserDetails();


                    HashMap<String, String> user = sessionManager.getUserDetails();
                    String Phone = user.get(sessionManager.KEY_Phone);
                   // getProfile();
                    // Toast.makeText(Login.this, ""+Phone, Toast.LENGTH_SHORT).show();
                    //String email = user.get(SessionManager.KEY_UserID);
                    //Toast.makeText(Login.this, ""+email, Toast.LENGTH_SHORT).show();

                } catch (Exception  e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                // pGif.setVisibility(View.GONE);
                progressDialog.dismiss();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("mobile", Phone_No.getText().toString());
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }
    
    public void getLoginVerify() {

         progressDialog = progressDialog.show(Login.this, "", "Please wait...", false, false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, API_URL.Login_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

                Log.e("respoance",s);
                progressDialog.dismiss();

                otplayout.setVisibility(View.VISIBLE);
                try {

                    JSONObject jsonObject1=new JSONObject(s);
                   String message= jsonObject1.getString("message");
                    Toast.makeText(Login.this, message, Toast.LENGTH_SHORT).show();

                   JSONObject jsonObject11=jsonObject1.getJSONObject("data");
                    useid = jsonObject11.getString("userId");
                    String Phone1 = jsonObject11.getString("phone");
                    otp1 = jsonObject11.getString("otp");

                    //Toast.makeText(Login.this, ""+otp1, Toast.LENGTH_SHORT).show();


                       // Toast.makeText(Login.this, ""+Phone, Toast.LENGTH_SHORT).show();
                        //String email = user.get(SessionManager.KEY_UserID);
                        //Toast.makeText(Login.this, ""+email, Toast.LENGTH_SHORT).show();

                    if (message.equals("user already exist.")){
                        sessionManager.createLoginSession(""+Phone1,""+useid);
                        sessionManager.getUserDetails();

                        HashMap<String, String> user = sessionManager.getUserDetails();
                        String Phone = user.get(sessionManager.KEY_Phone);

                        getProfile();
                    }
                    else if (message.equals("Registered Successfully"))
                    {

                        sessionManager.createLoginSession(""+Phone1,""+useid);
                        sessionManager.getUserDetails();

                        HashMap<String, String> user = sessionManager.getUserDetails();
                        String Phone = user.get(sessionManager.KEY_Phone);

                        getProfile();

                    }
                } catch (Exception  e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                // pGif.setVisibility(View.GONE);
                progressDialog.dismiss();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("mobile", Phone_No.getText().toString());
                params.put("otp", otp.getText().toString());
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

    public void getProfile() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, API_URL.USeprofile, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

                Log.e("USeprofile",s);
                try {

                    JSONObject jsonObject1=new JSONObject(s);

                    String message= jsonObject1.getString("message");

                    JSONObject jsonObject11=jsonObject1.getJSONObject("data");

                     name1=jsonObject11.getString("userName");

                    sessionManager.createaddress("",""+name1);

                    startActivity(new Intent(Login.this,Profile.class));
                    Login.this.finish();
                } catch (Exception  e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("userId", useid);
                return params;
            }
        };
        Volley.newRequestQueue(this).add(stringRequest);
    }

    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.Submit_Btn){
/*
            if (!Phone_No.getText().toString().isEmpty()){
                phone = Integer.parseInt(Phone_No.getText().toString());
                Log.e("tag__", String.valueOf(phone));
            }
*/

            if (Phone_No.getText().toString().length() !=10)
            {
                Toast.makeText(Login.this, "Please Enter Valid No", Toast.LENGTH_SHORT).show();

            }
            else {

                if (!otp.getText().toString().isEmpty()){
                    getLoginVerify();
                }
                else {
                    generate_opt();


                }
            }
            /*else {
                Toast.makeText(this, "else   _if", Toast.LENGTH_SHORT).show();
                if (!(Phone_No.getText().toString().equals("") && otp.getText().toString().equals(""))){
                    getLoginVerify();
            }
            else {
                    Toast.makeText(this, "else", Toast.LENGTH_SHORT).show();
                generate_opt();
                }


            }*/
        }
    }
}
