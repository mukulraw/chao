package app.in.chaiwale.Main;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.paytm.pgsdk.PaytmOrder;
import com.paytm.pgsdk.PaytmPGService;
import com.paytm.pgsdk.PaytmPaymentTransactionCallback;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import app.in.chaiwale.API_URL;
import app.in.chaiwale.Activity.SessionManager;

import app.in.chaiwale.PaytmPack.Api;
import app.in.chaiwale.PaytmPack.Checksum;


import app.in.chaiwale.PaytmPack.PaytmWallet;
import app.in.chaiwale.R;
import app.in.chaiwale.paytmBean;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class UserProfile_update extends AppCompatActivity implements PaytmPaymentTransactionCallback {
    String useid;

    TextView name_text, email, phone, address, topname, topph, address22;
    LinearLayout name_lner, email_lner, address_lner1, address_lner2;
    TextView mAddMore;

    ProgressDialog progressDialog;
    String address1;
    ImageView banc;
    private String id, bankName, txnAmount, txnDate, gateWayName, txnId, checkSumHash, respMsg;

/*
    String M_ID = "SSFOOD57880232643221"; //Paytm Merchand Id we got it in paytm credentials
    String CHANNEL_ID = "WAP"; //Paytm Channel Id, got it in paytm credentials
    String INDUSTRY_TYPE_ID = "Retail"; //Paytm industry type got it in paytm credential
    String WEBSITE = "APPSTAGING";
    String CALLBACK_URL = "https://securegw-stage.paytm.in/theia/processTransaction?ORDER_ID=";
*/

    TextView credit;

    String name1, phone1, email1, address3, address2;

    private Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile_update);

        SessionManager sessionManager;
        sessionManager = new SessionManager(this);
        HashMap<String, String> user = sessionManager.getUserDetails();
        String Phone = user.get(sessionManager.KEY_Phone);

        useid = user.get(SessionManager.KEY_UserID);

        sessionManager = new SessionManager(this);
        HashMap<String, String> user1 = sessionManager.GetAddress();
        address1 = user1.get(sessionManager.KEY_Address);
        email_lner = findViewById(R.id.email_lner);
        name_lner = findViewById(R.id.name_lner);

        credit = findViewById(R.id.credit);

        banc = findViewById(R.id.banc);
        address_lner1 = findViewById(R.id.address_lner1);
        address_lner2 = findViewById(R.id.address_lner2);
        name_text = findViewById(R.id.name_text);
        email = findViewById(R.id.email);
        phone = findViewById(R.id.phone);
        address = findViewById(R.id.address1);
        address22 = findViewById(R.id.address2);
        topph = findViewById(R.id.topph);
        topname = findViewById(R.id.topname);
        address.setText(address3);

        mAddMore = findViewById(R.id.tv_addmore);

        // spinner locality
        final String[] arraySpinner = new String[]{
                "Jheel khuranja", "Rajgarh colony"
        };
        spinner = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, arraySpinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });

/*
        spinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

            @Override public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                Toast.makeText(UserProfile_update.this, "clicked" + item, Toast.LENGTH_SHORT).show();
            }
        });
*/

        banc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                UserProfile_update.this.finish();
            }
        });

        email_lner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String hint = "please enter Email";
                String tile = "Please Update Email";
                showdiloge(hint, tile, name1, email1, address3, address2);

            }
        });
        address_lner1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*String hint="Please enter Address 1";
                String tile="Please Update Address 1";
                showdiloge(hint,tile,name1,email1,address3,address2);*/

                final Dialog dialog = new Dialog(UserProfile_update.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.address_dialog);
                dialog.setCancelable(false);
                dialog.show();


                ImageView close = dialog.findViewById(R.id.crossimgae);
                final EditText add = dialog.findViewById(R.id.address2);
                Spinner locality = dialog.findViewById(R.id.spinner);
                Button done = dialog.findViewById(R.id.done);

                final String[] loca = {""};

                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(UserProfile_update.this,
                        android.R.layout.simple_spinner_item, arraySpinner);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                locality.setAdapter(adapter);

                locality.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {
                        // TODO Auto-generated method stub
                        loca[0] = arraySpinner[position];
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> arg0) {
                        // TODO Auto-generated method stub
                    }
                });


                done.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        final String a = add.getText().toString();

                        if (a.length() > 0) {

                            progressDialog = progressDialog.show(UserProfile_update.this, "", "Please wait...", false, false);
                            StringRequest stringRequest = new StringRequest(Request.Method.POST, API_URL.getProfile, new Response.Listener<String>() {
                                @Override
                                public void onResponse(String s) {


                                    Log.e("Profile_get", s);
                                    //alert.cancel();
                                    progressDialog.dismiss();
                                    try {
                                        JSONObject jsonObject1 = new JSONObject(s);


                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    getProfile();
                                    dialog.dismiss();

                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError volleyError) {
                                    // pGif.setVisibility(View.GONE);
                                    Log.d("errrr", volleyError.toString());
                                    progressDialog.dismiss();
                                }
                            }) {
                                @Override
                                protected Map<String, String> getParams() {
                                    Map<String, String> params = new HashMap<>();
                                    params.put("userId", useid);
                                    params.put("name", topname.getText().toString());
                                    params.put("email", email.getText().toString());
                                    params.put("address", a + ", " + loca[0]);
                                    params.put("address2", address22.getText().toString());
                                    params.put("locality", spinner.getSelectedItem().toString());

                                    Log.e("tag_post", String.valueOf(params));
                                    return params;
                                }
                            };
                            Volley.newRequestQueue(UserProfile_update.this).add(stringRequest);

                        } else {
                            Toast.makeText(UserProfile_update.this, "Please enter an address", Toast.LENGTH_SHORT).show();
                        }

                    }
                });


            }
        });

        address_lner2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
/*
                String hint="Please enter Address 2";
                String tile="Please Update Address 2";
                showdiloge(hint,tile,name1,email1,address3,address2);
*/

                final Dialog dialog = new Dialog(UserProfile_update.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.address_dialog);
                dialog.setCancelable(false);
                dialog.show();


                ImageView close = dialog.findViewById(R.id.crossimgae);
                final EditText add = dialog.findViewById(R.id.address2);
                Spinner locality = dialog.findViewById(R.id.spinner);
                Button done = dialog.findViewById(R.id.done);

                final String[] loca = {""};

                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(UserProfile_update.this,
                        android.R.layout.simple_spinner_item, arraySpinner);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                locality.setAdapter(adapter);

                locality.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {
                        // TODO Auto-generated method stub
                        loca[0] = arraySpinner[position];
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> arg0) {
                        // TODO Auto-generated method stub
                    }
                });


                done.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        final String a = add.getText().toString();

                        if (a.length() > 0) {

                            progressDialog = progressDialog.show(UserProfile_update.this, "", "Please wait...", false, false);
                            StringRequest stringRequest = new StringRequest(Request.Method.POST, API_URL.getProfile, new Response.Listener<String>() {
                                @Override
                                public void onResponse(String s) {


                                    Log.e("Profile_get", s);
                                    //alert.cancel();
                                    progressDialog.dismiss();
                                    try {
                                        JSONObject jsonObject1 = new JSONObject(s);


                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    getProfile();
                                    dialog.dismiss();

                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError volleyError) {
                                    // pGif.setVisibility(View.GONE);
                                    Log.d("errrr", volleyError.toString());
                                    progressDialog.dismiss();
                                }
                            }) {
                                @Override
                                protected Map<String, String> getParams() {
                                    Map<String, String> params = new HashMap<>();
                                    params.put("userId", useid);
                                    params.put("name", topname.getText().toString());
                                    params.put("email", email.getText().toString());
                                    params.put("address", address.getText().toString());
                                    params.put("address2", a + ", " + loca[0]);
                                    params.put("locality", spinner.getSelectedItem().toString());

                                    Log.e("tag_post", String.valueOf(params));
                                    return params;
                                }
                            };
                            Volley.newRequestQueue(UserProfile_update.this).add(stringRequest);

                        } else {
                            Toast.makeText(UserProfile_update.this, "Please enter an address", Toast.LENGTH_SHORT).show();
                        }

                    }
                });


            }
        });


        name_lner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String hint = "please enter name";
                String tile = "Please Update Name";
                showdiloge(hint, tile, name1, email1, address3, address2);

            }
        });

        mAddMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(UserProfile_update.this, "Under Construction", Toast.LENGTH_SHORT).show();
                WalletPayment();
                //AddMoney addMoney = new AddMoney();
                //addMoney.AddMoney(UserProfile_update.this);
            }
        });

        getProfile();
    }

    void showdiloge(String hint, String titl, String name1, String email1, String address3, String address223) {
        LayoutInflater inflater = LayoutInflater.from(this);
        final View vs = inflater.inflate(R.layout.diloge, null);
        AlertDialog.Builder builer = new AlertDialog.Builder(UserProfile_update.this);
        builer.setView(vs);
        final LinearLayout Comfirm = (LinearLayout) vs.findViewById(R.id.Comfirm);
        final Button confirm = vs.findViewById(R.id.confirm);
        final EditText customlist = vs.findViewById(R.id.customlist);
        final EditText email = vs.findViewById(R.id.email);
        final EditText address = vs.findViewById(R.id.address);
        final EditText address2 = vs.findViewById(R.id.address2);
        final TextView tytle = vs.findViewById(R.id.tytle);
        final LinearLayout Not = (LinearLayout) vs.findViewById(R.id.Not);
        final LinearLayout address_lner = (LinearLayout) vs.findViewById(R.id.address_lner);
        final LinearLayout address_lner2 = (LinearLayout) vs.findViewById(R.id.address_lner2);
        final LinearLayout email_lner = (LinearLayout) vs.findViewById(R.id.email_lner);
        final LinearLayout name_lner = (LinearLayout) vs.findViewById(R.id.name_lner);
        final Button done = (Button) vs.findViewById(R.id.done);
        final Button Notbtn = (Button) vs.findViewById(R.id.Notbtn);
        final ImageView crossimgae = vs.findViewById(R.id.crossimgae);
        builer.setCancelable(false);
        final AlertDialog alert = builer.create();
        alert.getWindow().setGravity(Gravity.CENTER);
        alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alert.show();


        if (hint.equalsIgnoreCase("Please enter Address")) {
            address_lner2.setVisibility(View.GONE);
            name_lner.setVisibility(View.GONE);
            email_lner.setVisibility(View.GONE);
        } else if (hint.equalsIgnoreCase("please enter Email")) {
            address_lner2.setVisibility(View.GONE);
            name_lner.setVisibility(View.GONE);
            address_lner.setVisibility(View.GONE);

        } else if (hint.equalsIgnoreCase("please enter name")) {
            address_lner2.setVisibility(View.GONE);
            address_lner.setVisibility(View.GONE);
            email_lner.setVisibility(View.GONE);

        } else if (hint.equalsIgnoreCase("Please enter Address 2")) {
            name_lner.setVisibility(View.GONE);
            address_lner.setVisibility(View.GONE);
            email_lner.setVisibility(View.GONE);

        } else if (hint.equalsIgnoreCase("Please enter Address 1")) {
            name_lner.setVisibility(View.GONE);
            address_lner2.setVisibility(View.GONE);
            email_lner.setVisibility(View.GONE);

        }

        address2.setText(address223);
        address.setText(address3);
        customlist.setText(name1);
        email.setText(email1);
        tytle.setText(titl);
        customlist.setHint(hint);

        crossimgae.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.cancel();
            }
        });

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressDialog = progressDialog.show(UserProfile_update.this, "", "Please wait...", false, false);
                StringRequest stringRequest = new StringRequest(Request.Method.POST, API_URL.getProfile, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {

                        Log.e("Profile_get", s);
                        alert.cancel();
                        progressDialog.dismiss();
                        try {
                            JSONObject jsonObject1 = new JSONObject(s);


                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        getProfile();

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
                        params.put("name", customlist.getText().toString());
                        params.put("email", email.getText().toString());
                        params.put("address", address.getText().toString());
                        params.put("address2", address2.getText().toString());
                        params.put("locality", spinner.getSelectedItem().toString());

                        Log.e("tag_post", String.valueOf(params));
                        return params;
                    }
                };
                Volley.newRequestQueue(UserProfile_update.this).add(stringRequest);


            }
        });
    }


    public void getProfile() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, API_URL.USeprofile, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

                Log.e("USeprofile", s);
                try {


                    JSONObject jsonObject1 = new JSONObject(s);


                    String message = jsonObject1.getString("message");

                    JSONObject jsonObject11 = jsonObject1.getJSONObject("data");

                    name1 = jsonObject11.getString("userName");
                    phone1 = jsonObject11.getString("phone");
                    email1 = jsonObject11.getString("email");
                    address3 = jsonObject11.getString("address");
                    address2 = jsonObject11.getString("address2");
                    name_text.setText(name1);
                    topname.setText(name1);
                    phone.setText("+91" + phone1);
                    topph.setText("+91" + phone1);
                    email.setText(email1);
                    address.setText(address3);
                    address22.setText(address2);


                } catch (Exception e) {
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


    void WalletPayment() {
        LayoutInflater inflater = LayoutInflater.from(this);
        final View vs = inflater.inflate(R.layout.add_money, null);
        AlertDialog.Builder builer = new AlertDialog.Builder(UserProfile_update.this);
        builer.setView(vs);
        final LinearLayout Comfirm = (LinearLayout) vs.findViewById(R.id.Comfirm);
        final EditText mAmount = vs.findViewById(R.id.amount);

        final TextView tytle = vs.findViewById(R.id.tytle);
        final Button payment = (Button) vs.findViewById(R.id.makepayment);
        final ImageView crossimgae = vs.findViewById(R.id.crossimgae);
        builer.setCancelable(false);
        final AlertDialog alert = builer.create();
        alert.getWindow().setGravity(Gravity.CENTER);
        alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alert.show();


        crossimgae.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.cancel();
            }
        });

        payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mAmount.getText().toString().equals("")) {
                    Toast.makeText(UserProfile_update.this, "Please Enter Amount", Toast.LENGTH_SHORT).show();
                } else {
                    generateCheckSum(String.valueOf(mAmount.getText().toString()));
                }

            }
        });

    }

    private void generateCheckSum(String amount) {

        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(5, TimeUnit.MINUTES)
                .readTimeout(5, TimeUnit.MINUTES)
                .build();

        //creating a retrofit object.
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://afsanaonline.com/chaiwala/")
                //.baseUrl("http://ec2-13-126-246-74.ap-south-1.compute.amazonaws.com/")
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();


        final String MID = "SSFOOD05684844524320";
        final String ORDER_ID = String.valueOf(System.currentTimeMillis());
        final String INDUSTRY_TYPE_ID = "Retail109";
        final String CUST_ID = ORDER_ID;
        final String CHANNEL_ID = "WAP";
        final String TXN_AMOUNT = amount;
        final String WEBSITE = "SSFOOD";
        final String CALLBACK_URL = "https://pguat.paytm.com/paytmchecksum/paytmCallback.jsp";
        //final String CALLBACK_URL = "https://securegw.paytm.in/theia/paytmCallback?ORDER_ID=" + ORDER_ID;


        Api cr = retrofit.create(Api.class);

        Call<paytmBean> call = cr.pay(
                MID,
                ORDER_ID,
                CUST_ID,
                CHANNEL_ID,
                TXN_AMOUNT,
                WEBSITE,
                CALLBACK_URL,
                INDUSTRY_TYPE_ID
        );


        call.enqueue(new Callback<paytmBean>() {
            @Override
            public void onResponse(Call<paytmBean> call, final retrofit2.Response<paytmBean> response) {

                //status.setText("CHECKSUM : " + response.body().getCHECKSUMHASH());


                Log.d("CHECK", response.body().getCHECKSUMHASH());

                //progress.setVisibility(View.GONE);

                PaytmPGService Service = PaytmPGService.getProductionService();

                HashMap<String, String> paramMap = new HashMap<String, String>();
                paramMap.put("MID", MID);
                paramMap.put("ORDER_ID", ORDER_ID);
                paramMap.put("CUST_ID", CUST_ID);
                paramMap.put("CHANNEL_ID", CHANNEL_ID);
                paramMap.put("TXN_AMOUNT", TXN_AMOUNT);
                paramMap.put("WEBSITE", WEBSITE);
                paramMap.put("CALLBACK_URL", CALLBACK_URL);
                paramMap.put("CHECKSUMHASH", response.body().getCHECKSUMHASH());
                paramMap.put("INDUSTRY_TYPE_ID", INDUSTRY_TYPE_ID);
                PaytmOrder Order = new PaytmOrder(paramMap);

                Service.initialize(Order, null);

                Service.enableLog(UserProfile_update.this);

                Service.startPaymentTransaction(UserProfile_update.this, true, true, new PaytmPaymentTransactionCallback() {
                    /*Call Backs*/
                    public void someUIErrorOccurred(String inErrorMessage) {
                    }

                    public void onTransactionResponse(Bundle bundle) {

                        for (int i = 0; i < bundle.size(); i++) {

                            id = bundle.getString("ORDERID");
                            bankName = bundle.getString("BANKNAME");
                            txnAmount = bundle.getString("TXNAMOUNT");
                            txnDate = bundle.getString("TXNDATE");
                            gateWayName = bundle.getString("GATEWAYNAME");
                            txnId = bundle.getString("TXNID");
                            checkSumHash = bundle.getString("CHECKSUMHASH");
                            respMsg = bundle.getString("RESPMSG");
                            Log.e("tag_id", id + "," + bankName + ", " + txnAmount + "," + gateWayName);

                        }
                        String orderid = String.valueOf(bundle.equals("ORDERID"));
                        Log.e("tag_orderid", orderid);

                        Log.e("tag_bundle_response", bundle.toString());
                        //Toast.makeText(UserProfile_update.this, bundle.toString(), Toast.LENGTH_LONG).show();
                        callSuccessPaymentApi();
                    }

                    public void networkNotAvailable() {
                    }

                    public void clientAuthenticationFailed(String inErrorMessage) {
                        //  status.setText(inErrorMessage);
                    }

                    public void onErrorLoadingWebPage(int iniErrorCode, String inErrorMessage, String inFailingUrl) {
                        //    status.setText(inErrorMessage);
                    }

                    public void onBackPressedCancelTransaction() {
                    }

                    public void onTransactionCancel(String inErrorMessage, Bundle inResponse) {
                        //      status.setText(inErrorMessage);
                    }
                });

            }

            @Override
            public void onFailure(Call<paytmBean> call, Throwable t) {
                //progress.setVisibility(View.GONE);
            }
        });


        //creating paytm object
        //containing all the values required

        //Log.d("orderr" , PaytmWallet.orderId);



        /*final PaytmWallet paytm = new PaytmWallet(
                M_ID,
                CHANNEL_ID,
                amount,
                WEBSITE,
                CALLBACK_URL+ PaytmWallet.orderId,
                INDUSTRY_TYPE_ID
        );*/


        //creating a call object from the apiService
        /*Call<Checksum> call = apiService.getChecksum(
                MID,
                ORDER_ID,
                CUST_ID,
                INDUSTRY_TYPE_ID,
                CHANNEL_ID,
                TXN_AMOUNT,
                WEBSITE,
                CALLBACK_URL
        );*/

        //making the call to generate checksum
        /*call.enqueue(new Callback<Checksum>() {
            @Override
            public void onResponse(Call<Checksum> call, retrofit2.Response<Checksum> response) {

                Log.d("mytag","Response=="+response.body().getPaytStatus());

                PaytmPGService Service = PaytmPGService.getStagingService();


                HashMap<String, String> paramMap = new HashMap<String, String>();
                paramMap.put("MID", MID);
// Key in your staging and production MID available in your dashboard
                paramMap.put("ORDER_ID", ORDER_ID);
                paramMap.put("CUST_ID", CUST_ID);
                paramMap.put("INDUSTRY_TYPE_ID", INDUSTRY_TYPE_ID);
                //paramMap.put("MOBILE_NO", "7777777777");
                //paramMap.put("EMAIL", "username@emailprovider.com");
                paramMap.put("CHANNEL_ID", CHANNEL_ID);
                paramMap.put("TXN_AMOUNT", TXN_AMOUNT);
                paramMap.put("WEBSITE", WEBSITE);
// This is the staging value. Production value is available in your dashboard

// This is the staging value. Production value is available in your dashboard
                paramMap.put("CALLBACK_URL", CALLBACK_URL);
                paramMap.put("CHECKSUMHASH", response.body().getChecksumHash());
                PaytmOrder Order = new PaytmOrder(paramMap);

                Service.initialize(Order, null);

                Service.enableLog(UserProfile_update.this);
                //finally starting the payment transaction
                Service.startPaymentTransaction(UserProfile_update.this, true, true, UserProfile_update.this);
                PaytmPaymentTransactionCallback zfzs =   Service.getmPaymentTransactionCallback();
                Log.d("mytag","Paytm_call_back"+zfzs.toString());


                //initializePaytmPayment(response.body().getChecksumHash(), paytm);
                //Log.d("mytag","All Check_sum==>"+paytm);
            }

            @Override
            public void onFailure(Call<Checksum> call, Throwable t) {
                Log.d("mytag","Faileds==>"+t.toString());
                t.printStackTrace();

            }
        });*/
    }


    private void initializePaytmPayment(String checksumHash, PaytmWallet paytm) {

        //getting paytm service
        /*PaytmPGService Service = PaytmPGService.getStagingService();

        HashMap<String, String> paramMap = new HashMap<>();
        paramMap.put("MID", M_ID);
        paramMap.put("ORDER_ID", paytm.getOrderId());
        paramMap.put("CUST_ID", paytm.getCustId());
        paramMap.put("CHANNEL_ID", paytm.getChannelId());
        paramMap.put("TXN_AMOUNT", paytm.getTxnAmount());
        paramMap.put("WEBSITE", paytm.getWebsite());
        paramMap.put("CALLBACK_URL", CALLBACK_URL+ paytm.orderId);
        paramMap.put("CHECKSUMHASH", checksumHash);
        paramMap.put("INDUSTRY_TYPE_ID", paytm.getIndustryTypeId());


        //creating a paytm order object using the hashmap
        PaytmOrder order = new PaytmOrder(paramMap);

        //intializing the paytm service
        Service.initialize(order, null);

        Service.enableLog(UserProfile_update.this);
        //finally starting the payment transaction
        Service.startPaymentTransaction(UserProfile_update.this, true, true, UserProfile_update.this);
        PaytmPaymentTransactionCallback zfzs =   Service.getmPaymentTransactionCallback();
        Log.d("mytag","Paytm_call_back"+zfzs.toString());*/

    }

    //all these overriden method is to detect the payment result accordingly
    @Override
    public void onTransactionResponse(Bundle bundle) {

        for (int i = 0; i < bundle.size(); i++) {

            id = bundle.getString("ORDERID");
            bankName = bundle.getString("BANKNAME");
            txnAmount = bundle.getString("TXNAMOUNT");
            txnDate = bundle.getString("TXNDATE");
            gateWayName = bundle.getString("GATEWAYNAME");
            txnId = bundle.getString("TXNID");
            checkSumHash = bundle.getString("CHECKSUMHASH");
            respMsg = bundle.getString("RESPMSG");
            Log.e("tag_id", id + "," + bankName + ", " + txnAmount + "," + gateWayName);

        }
        String orderid = String.valueOf(bundle.equals("ORDERID"));
        Log.e("tag_orderid", orderid);

        Log.e("tag_bundle_response", bundle.toString());
        //Toast.makeText(UserProfile_update.this, bundle.toString(), Toast.LENGTH_LONG).show();
        callSuccessPaymentApi();
    }

    @Override
    public void networkNotAvailable() {
        Toast.makeText(UserProfile_update.this, "Network error", Toast.LENGTH_LONG).show();
    }

    @Override
    public void clientAuthenticationFailed(String s) {
        Toast.makeText(UserProfile_update.this, s, Toast.LENGTH_LONG).show();
    }

    @Override
    public void someUIErrorOccurred(String s) {
        Toast.makeText(UserProfile_update.this, s, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onErrorLoadingWebPage(int i, String s, String s1) {
        Toast.makeText(UserProfile_update.this, s, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onBackPressedCancelTransaction() {
        Toast.makeText(UserProfile_update.this, "Back Pressed", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onTransactionCancel(String s, Bundle bundle) {
        Toast.makeText(UserProfile_update.this, s + bundle.toString(), Toast.LENGTH_LONG).show();

    }

    private void callSuccessPaymentApi() {
        {
            progressDialog = progressDialog.show(UserProfile_update.this, "", "Please wait...", false, false);

            StringRequest stringRequest = new StringRequest(Request.Method.POST, API_URL.add_wallet_ammount, new Response.Listener<String>() {

                @Override
                public void onResponse(String s) {
                    progressDialog.dismiss();
                    Log.e("USeprofile", s);
                    try {
                        JSONObject jsonObject1 = new JSONObject(s);

                       /* Intent intent=new Intent(UserProfile_update.this,MainNavigation.class);
                        startActivity(intent);
                        UserProfile_update.this.finish();
*/
                        Toast.makeText(UserProfile_update.this, "Successful Order Placed", Toast.LENGTH_SHORT).show();

                        getWallet();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    progressDialog.dismiss();
                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("userid", useid);
                    params.put("amount", txnAmount);
                    params.put("transaction_status", respMsg);
                    //  params.put("payment_method", "Online");
                    params.put("gateway_name", gateWayName);
                    params.put("transaction_id", txnId);
                    params.put("bank_name", bankName);
                    //  params.put("check_sum_hash", checkSumHash);
                    // params.put("order_id", id);
                    Log.e("post iiid", String.valueOf(params));
                    return params;
                }
            };
            Volley.newRequestQueue(this).add(stringRequest);
        }
    }

    public void getWallet() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, API_URL.getWallet, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Log.e("getwallet", s);
                try {

                    JSONObject jsonObject1 = new JSONObject(s);

                    JSONObject jsonObject = jsonObject1.getJSONObject("data");

                    String walletamoutn = jsonObject.getString("wallet_amount");
                    credit.setText("Credit  Rs." + walletamoutn + "/-");
                    // Toast.makeText(MainNavigation.this, ""+walletamoutn, Toast.LENGTH_SHORT).show();

                } catch (Exception e) {
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
                Log.e("post", String.valueOf(params));
                return params;
            }
        };
        Volley.newRequestQueue(this).add(stringRequest);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getWallet();
    }
}
