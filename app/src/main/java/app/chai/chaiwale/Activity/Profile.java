package app.chai.chaiwale.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

import app.chai.chaiwale.API_URL;
import app.chai.chaiwale.Main.MainNavigation;
import app.chai.chaiwale.R;

public class Profile extends AppCompatActivity {

    RequestQueue requestQueue;
    ProgressDialog progressDialog;
    Button  Submit_Btn;

    EditText name,email,address;
    SessionManager sessionManager;
    String useid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        requestQueue = Volley.newRequestQueue(this);

        Submit_Btn=findViewById(R.id.Submit_Btn);
        address=findViewById(R.id.address);
        email=findViewById(R.id.email);
        name=findViewById(R.id.name);

        sessionManager=new SessionManager(this);
        HashMap<String, String> user = sessionManager.getUserDetails();
        final String Phone = user.get(sessionManager.KEY_Phone);

         useid= user.get(SessionManager.KEY_UserID);

        sessionManager=new SessionManager(this);
        HashMap<String, String> user1 = sessionManager.GetAddress();
        final String address1= user1.get(sessionManager.KEY_Name);

        String useid= user.get(SessionManager.KEY_Address);

        //Toast.makeText(this, ""+useid, Toast.LENGTH_SHORT).show();

        if (address1!=null)
        {
            startActivity(new Intent(Profile.this,MainNavigation.class));
            Profile.this.finish();
        }
        else {

        }

        Submit_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // startActivity(new Intent(Profile.this,MainNavigation.class));

                if (name.getText().toString().isEmpty())
                {
                    Toast.makeText(Profile.this, "Enter Name", Toast.LENGTH_SHORT).show();
                }
                else if (email.getText().toString().isEmpty())
                {
                    Toast.makeText(Profile.this, "Enter Address 1", Toast.LENGTH_SHORT).show();
                }else if (address.getText().toString().isEmpty())
                {
                    Toast.makeText(Profile.this, "Enter Address 2", Toast.LENGTH_SHORT).show();
                }

                else
                {
                    UpdateProfile();
                }

            }
        });


    }

    public void UpdateProfile() {

        progressDialog = progressDialog.show(Profile.this, "", "Please wait...", false, false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, API_URL.getProfile, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

                Log.e("Profile_get",s);
                progressDialog.dismiss();
                try {

                    JSONObject jsonObject1=new JSONObject(s);
                    String status=jsonObject1.getString("status");
                    if (status.equalsIgnoreCase("1"))
                    {
                        JSONObject jsonObject=jsonObject1.getJSONObject("data");
                        String userName=jsonObject.getString("address");
                        String email=jsonObject.getString("email");
                        sessionManager.createaddress(""+userName,""+email);
                        sessionManager.getUserDetails();
                        Intent intent=new Intent(Profile.this,MainNavigation.class);
                        startActivity(intent);
                        Profile.this.finish();

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
                params.put("userId", useid);
                params.put("name", name.getText().toString());
                params.put("address", email.getText().toString());
                params.put("address2", address.getText().toString());
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }
}
