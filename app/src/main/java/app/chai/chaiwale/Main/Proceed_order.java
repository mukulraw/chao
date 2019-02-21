package app.chai.chaiwale.Main;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.paytm.pgsdk.PaytmOrder;
import com.paytm.pgsdk.PaytmPGService;
import com.paytm.pgsdk.PaytmPaymentTransactionCallback;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import app.chai.chaiwale.API_URL;
import app.chai.chaiwale.Activity.SessionManager;
import app.chai.chaiwale.PaytmPack.Api;

import app.chai.chaiwale.PaytmPack.Paytm;
import app.chai.chaiwale.R;
import app.chai.chaiwale.paytmBean;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class Proceed_order extends AppCompatActivity implements PaytmPaymentTransactionCallback {


    TextView totaamount, total_cart;
    String totalamount;

    SessionManager sessionManager;
    ProgressDialog progressDialog;
    String useid, address1;
    Button pay;
    Bundle bundle;
    CheckBox mywallet_check;
    ProgressBar progress;

    String walletamoutn;

    int cash_amount;

    RadioButton address2_rd, address1_rd;

    String walletamonyt = "0";
    String is_wallet = "0";
    RequestQueue requestQueue;
    public static int order_id;
    String total_amount;
    private String id, bankName, txnAmount, txnDate, gateWayName, txnId, checkSumHash, respMsg;

    /*String M_ID = "SSFOOD57880232643221"; //Paytm Merchand Id we got it in paytm credentials
    String CHANNEL_ID = "WAP"; //Paytm Channel Id, got it in paytm credentials
    String INDUSTRY_TYPE_ID = "Retail"; //Paytm industry type got it in paytm credential
    String WEBSITE = "APPSTAGING";
    String CALLBACK_URL = "https://securegw.stage.in/theia/paytmCallback?ORDER_ID=";*/
    //String CALLBACK_URL = "https://securegw.paytm.in/theia/paytmCallback?ORDER_ID=";
    String paymentMode = "";
    private int cashAmount;
    private int order_id_checkout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proceed_order);
        requestQueue = Volley.newRequestQueue(Proceed_order.this);

        total_cart = findViewById(R.id.total_cart);
        totaamount = findViewById(R.id.totaamount);
        address2_rd = findViewById(R.id.address2_rd);
        address1_rd = findViewById(R.id.address1_rd);
        //   paytm=findViewById(R.id.rb_paytm);
        pay = findViewById(R.id.pay);
        progress = findViewById(R.id.progress);
        mywallet_check = findViewById(R.id.mywallet_check);
        //   mywallet_paytm=findViewById(R.id.mywallet_paytm);
        progress.setVisibility(View.GONE);

        bundle = getIntent().getExtras();
        totalamount = bundle.getString("totalamoutn");

        total_cart.setText(totalamount);
        totaamount.setText(totalamount);

        sessionManager = new SessionManager(this);
        HashMap<String, String> user = sessionManager.getUserDetails();
        final String Phone = user.get(sessionManager.KEY_Phone);
        useid = user.get(SessionManager.KEY_UserID);
        sessionManager = new SessionManager(this);
        HashMap<String, String> user1 = sessionManager.GetAddress();
        address1 = user1.get(sessionManager.KEY_Address);

        getWallet();
        getProfile();
        //Toast.makeText(this, ""+address1, Toast.LENGTH_SHORT).show();
        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*if(!paytm.isChecked())
                {
                    Toast.makeText(Proceed_order.this, "Please Select Payment Gateway", Toast.LENGTH_SHORT).show();

                }
                else*/
                if (!(address1_rd.isChecked() || address2_rd.isChecked())) {
                    Toast.makeText(Proceed_order.this, "Please Choose Any One Address", Toast.LENGTH_SHORT).show();

                } else {
                    if (mywallet_check.isChecked()) {
                        if (Integer.parseInt(totalamount) > Integer.parseInt(walletamoutn)) {


                            int ca = Integer.parseInt(totalamount) - Integer.parseInt(walletamoutn);

                            String wa = walletamoutn;


                            WalletPayment(wa , String.valueOf(ca));


                        } else if (Integer.parseInt(totalamount) < Integer.parseInt(walletamoutn)) {
                            cashAmount = 0;
                            walletamoutn = totalamount;
                            paybywallet(cashAmount);
                        } else {
                            Toast.makeText(Proceed_order.this, "uncheck", Toast.LENGTH_SHORT).show();
                            // paybywallet(cashAmount, walletamoutn);

                        }
                    } else {
                        WalletPayment("0" , totalamount);
                    }

                    // getpay();
                }

            }
        });

        address1_rd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                address1_rd.setChecked(true);
                address2_rd.setChecked(false);

            }
        });


        address2_rd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                address1_rd.setChecked(false);
                address2_rd.setChecked(true);
            }
        });


        mywallet_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mywallet_check.isChecked()) {

                    cash_amount = Integer.parseInt(walletamoutn) - Integer.parseInt(totalamount);
                    //cash_amount = Integer.parseInt(totalamount) - Integer.parseInt(walletamoutn);
                    totaamount.setText("" + String.valueOf(cash_amount));
                    walletamonyt = String.valueOf(cash_amount);
                    is_wallet = "1";
                } else {
                    cash_amount = Integer.parseInt(totalamount);
                    totaamount.setText("" + String.valueOf(cash_amount));
                    cash_amount = 0;
                    walletamonyt = "0";
                    is_wallet = "0";

                }
            }
        });

/*
        mywallet_paytm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mywallet_check.isChecked())
                {
                    cash_amount = Integer.parseInt(totalamount) - Integer.parseInt(walletamoutn);
                    totaamount.setText(""+String.valueOf(cash_amount));
                    walletamonyt=String.valueOf(cash_amount);
                    is_wallet="1";
                }
                else if (mywallet_paytm.isChecked())
                {
                    if (!totaamount.equals("0")){
                        generateCheckSum(String.valueOf(totalamount));
                    }
                    else {
                        Toast.makeText(Proceed_order.this, "No Valid Amount", Toast.LENGTH_SHORT).show();
                    }
                    
                }
                else
                {
                    cash_amount = Integer.parseInt(totalamount);
                    totaamount.setText(""+String.valueOf(cash_amount));
                    cash_amount=0;
                    walletamonyt="0";
                    is_wallet="0";

                }

            }
        });
*/

    }

    public void GetOrderId() {
        progress.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, API_URL.GetOrderId, new Response.Listener<String>() {

            @Override
            public void onResponse(String s) {
                progress.setVisibility(View.GONE);
                Log.e("USeprofile", s);
                try {
                    /*JSONObject jsonObject1=new JSONObject(s);

                    Intent intent=new Intent(Proceed_order.this,MainNavigation.class);
                    startActivity(intent);
                    Proceed_order.this.finish();
                    Toast.makeText(Proceed_order.this, "Successful Order Placed", Toast.LENGTH_SHORT).show();
*/
                    progress.setVisibility(View.INVISIBLE);
                    Log.e("respoance", s);

                    try {
                        JSONObject jsonObject1 = new JSONObject(s);
                        //  String message= jsonObject1.getString("order id");
                        String status = jsonObject1.getString("status");

                        if (status.equalsIgnoreCase("1")) {
                            JSONArray array = jsonObject1.getJSONArray("data");

                            for (int i = 0; i < array.length(); i++) {
                                JSONObject jsonObject = array.getJSONObject(i);
                                order_id = jsonObject.getInt("order_id");


                            }

                            generateCheckSum(String.valueOf(cashAmount) , "");


                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e("tag_e", String.valueOf(e));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                progress.setVisibility(View.GONE);
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("userid", useid);
                params.put("billaddress", address1);
                params.put("grand_total", totalamount);
                params.put("payment_method", "Online");
                params.put("is_wallet", is_wallet);
                params.put("wallet_amount", walletamoutn);
                params.put("cash_amount", String.valueOf(cashAmount));
                Log.e("post iiid_r", String.valueOf(params));
                return params;
            }
        };
        Volley.newRequestQueue(this).add(stringRequest);
    }

    public void paybywallet(final int cashAmount) {
        progress.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, API_URL.paybywallet, new Response.Listener<String>() {

            @Override
            public void onResponse(String s) {
                progress.setVisibility(View.GONE);
                Log.e("USeprofile", s);
                try {
                    /*JSONObject jsonObject1=new JSONObject(s);

                    Intent intent=new Intent(Proceed_order.this,MainNavigation.class);
                    startActivity(intent);
                    Proceed_order.this.finish();
                    Toast.makeText(Proceed_order.this, "Successful Order Placed", Toast.LENGTH_SHORT).show();
*/
                    progress.setVisibility(View.INVISIBLE);
                    Log.e("respoance", s);

                    try {
                        JSONObject jsonObject1 = new JSONObject(s);
                        //  String message= jsonObject1.getString("order id");
                        String status = jsonObject1.getString("status");

                        if (status.equalsIgnoreCase("1")) {
                            JSONArray array = jsonObject1.getJSONArray("data");

                            for (int i = 0; i < array.length(); i++) {
                                JSONObject jsonObject = array.getJSONObject(i);

                                order_id = jsonObject.getInt("order_id");
                                total_amount = jsonObject.getString("total_amount");
                                Log.e("tag_orderid_r", String.valueOf(order_id) + "," + total_amount);

                                Intent intent = new Intent(Proceed_order.this, MainNavigation.class);
                                startActivity(intent);
                                Proceed_order.this.finish();


                            }
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e("tag_e", String.valueOf(e));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                progress.setVisibility(View.GONE);
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("userid", useid);
                params.put("billaddress", address1);
                params.put("grand_total", totalamount);
                params.put("payment_method", "Wallet");
                params.put("is_wallet", is_wallet);
                params.put("wallet_amount", totalamount);
                params.put("cash_amount", String.valueOf(cashAmount));
                Log.e("post iiid_r", String.valueOf(params));
                return params;
            }
        };
        Volley.newRequestQueue(this).add(stringRequest);
    }

    public void cod_online(final String paymentmode , final String cash , final String wallet , final String is_wallet , final String graand) {
        progress.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, API_URL.orderproceed, new Response.Listener<String>() {

            @Override
            public void onResponse(String s) {
                progress.setVisibility(View.GONE);
                Log.e("USeprofile", s);
                try {

                    progress.setVisibility(View.INVISIBLE);
                    Log.e("respoance", s);

                    try {
                        JSONObject jsonObject1 = new JSONObject(s);
                        //  String message= jsonObject1.getString("order id");
                        String status = jsonObject1.getString("status");

                        if (status.equalsIgnoreCase("1")) {
                            JSONArray array = jsonObject1.getJSONArray("data");

                            for (int i = 0; i < array.length(); i++) {
                                JSONObject jsonObject = array.getJSONObject(i);

                                order_id_checkout = jsonObject.getInt("order_id");
                                Log.e("tag", String.valueOf(order_id_checkout));
                                total_amount = jsonObject.getString("total_amount");
                                Log.e("tag_orderid_r", String.valueOf(order_id) + "," + total_amount);

                                // call paytm method
                                //if (paymentmode.equals("paytm")) {
//                                    callSuccessPaymentApi(order_id_checkout);
                                    Intent intent = new Intent(Proceed_order.this, MainNavigation.class);
                                    startActivity(intent);
                                    Proceed_order.this.finish();

/*
                                } else if (paymentmode.equals("cod")) {
                                    Intent intent = new Intent(Proceed_order.this, MainNavigation.class);
                                    startActivity(intent);
                                    Proceed_order.this.finish();

                                }*/

                            }
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e("tag_e", String.valueOf(e));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                progress.setVisibility(View.GONE);
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("userid", useid);
                params.put("billaddress", address1);
                params.put("grand_total", graand);
                if (paymentmode.equals("cod")) {
                    params.put("payment_method", "cod");
                } else {
                    params.put("payment_method", "Online");
                }

                params.put("is_wallet", is_wallet);
                params.put("wallet_amount", wallet);
                params.put("cash_amount", cash);
                Log.e("post iiid_r", String.valueOf(params));
                return params;
            }
        };
        Volley.newRequestQueue(this).add(stringRequest);
    }

    public void getWallet() {

        progress.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, API_URL.getWallet, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

                progress.setVisibility(View.GONE);
                Log.e("getwallet", s);
                try {

                    JSONObject jsonObject1 = new JSONObject(s);
                    JSONObject jsonObject = jsonObject1.getJSONObject("data");

                    walletamoutn = jsonObject.getString("wallet_amount");
                    total_cart.setText("" + walletamoutn + "");
                    // Toast.makeText(Proceed_order.this, ""+walletamoutn, Toast.LENGTH_SHORT).show();


                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                progress.setVisibility(View.GONE);

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


    public void getProfile() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, API_URL.USeprofile, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

                Log.e("USeprofile", s);
                try {

                    JSONObject jsonObject1 = new JSONObject(s);
                    String message = jsonObject1.getString("message");

                    JSONObject jsonObject11 = jsonObject1.getJSONObject("data");

                    String address3 = jsonObject11.getString("address");
                    String address2 = jsonObject11.getString("address2");

                    address1_rd.setText(address2);
                    address2_rd.setText(address3);


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


    // paytm
    private void generateCheckSum(final String cash_amount , final String wa) {

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
        final String TXN_AMOUNT = cash_amount;
        final String WEBSITE = "SSFOOD";
        final String CALLBACK_URL = "https://pguat.paytm.com/paytmchecksum/paytmCallback.jsp";


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
                paramMap.put("CHECKSUMHASH", response.body().getCHECKSUMHASH());
                PaytmOrder Order = new PaytmOrder(paramMap);

                Service.initialize(Order, null);

                //Service.enableLog(Proceed_order.this);

                Service.startPaymentTransaction(Proceed_order.this, true, true, new PaytmPaymentTransactionCallback() {
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
                        //Toast.makeText(Proceed_order.this, bundle.toString(), Toast.LENGTH_LONG).show();

                        cod_online("Online" , cash_amount , wa ,  is_wallet ,totalamount);
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

    }


    private void initializePaytmPayment(String checksumHash, Paytm paytm) {

        //getting paytm service
        /*PaytmPGService Service = PaytmPGService.getStagingService();

        HashMap<String, String> paramMap = new HashMap<>();
        paramMap.put("MID", M_ID);
        paramMap.put("ORDER_ID", paytm.getOrderId());
        paramMap.put("CUST_ID", paytm.getCustId());
        paramMap.put("CHANNEL_ID", paytm.getChannelId());
        paramMap.put("TXN_AMOUNT", paytm.getTxnAmount());
        paramMap.put("WEBSITE", paytm.getWebsite());
        paramMap.put("CALLBACK_URL", CALLBACK_URL + order_id);
        paramMap.put("CHECKSUMHASH", checksumHash);
        paramMap.put("INDUSTRY_TYPE_ID", paytm.getIndustryTypeId());


        //creating a paytm order object using the hashmap
        PaytmOrder order = new PaytmOrder(paramMap);

        //intializing the paytm service
        Service.initialize(order, null);

        //finally starting the payment transaction
        Service.startPaymentTransaction(Proceed_order.this, true, true, Proceed_order.this);
        PaytmPaymentTransactionCallback zfzs = Service.getmPaymentTransactionCallback();
        Log.d("mytag", "Paytm_call_back" + zfzs.toString());*/

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
        Toast.makeText(this, bundle.toString(), Toast.LENGTH_LONG).show();

        //getpay("paytm");

    }

    @Override
    public void networkNotAvailable() {
        Toast.makeText(this, "Network error", Toast.LENGTH_LONG).show();
    }

    @Override
    public void clientAuthenticationFailed(String s) {
        Toast.makeText(this, s, Toast.LENGTH_LONG).show();
    }

    @Override
    public void someUIErrorOccurred(String s) {
        Toast.makeText(this, s, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onErrorLoadingWebPage(int i, String s, String s1) {
        Toast.makeText(this, s, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onBackPressedCancelTransaction() {
        Toast.makeText(this, "Back Pressed", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onTransactionCancel(String s, Bundle bundle) {
        Toast.makeText(this, s + bundle.toString(), Toast.LENGTH_LONG).show();

    }

    private void callSuccessPaymentApi(final int order_id) {
        {
            progress.setVisibility(View.VISIBLE);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, API_URL.successPayment, new Response.Listener<String>() {

                @Override
                public void onResponse(String s) {
                    progress.setVisibility(View.GONE);
                    Log.e("USeprofile", s);
                    try {
                        JSONObject jsonObject1 = new JSONObject(s);
                        Intent intent = new Intent(Proceed_order.this, MainNavigation.class);
                        startActivity(intent);
                        Proceed_order.this.finish();

                        Toast.makeText(Proceed_order.this, "Successful Order Placed", Toast.LENGTH_SHORT).show();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    progress.setVisibility(View.GONE);
                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("userid", useid);
                    params.put("amount", total_amount);
                    params.put("transaction_status", respMsg);
                    params.put("payment_method", "Online");
                    params.put("gateway_name", gateWayName);
                    params.put("transaction_id", txnId);
                    params.put("bank_name", bankName);
                    params.put("check_sum_hash", checkSumHash);
                    params.put("order_id", String.valueOf(order_id));
                    Log.e("post iiid", String.valueOf(params));
                    return params;
                }
            };
            Volley.newRequestQueue(this).add(stringRequest);
        }
    }

    void WalletPayment(final String wa , final String ca) {
        final RadioGroup radioGroup;
        RadioButton rb1, rb2, rb3;
        LayoutInflater inflater = LayoutInflater.from(this);
        final View vs = inflater.inflate(R.layout.payment_mode, null);
        AlertDialog.Builder builer = new AlertDialog.Builder(Proceed_order.this);
        builer.setView(vs);
        final LinearLayout Comfirm = (LinearLayout) vs.findViewById(R.id.Comfirm);

        // radio button

        radioGroup = vs.findViewById(R.id.radioGroup);
        rb1 = (RadioButton) vs.findViewById(R.id.radioButton1);
        // rb2 = (RadioButton) vs.findViewById(R.id.radioButton2);
        rb3 = (RadioButton) vs.findViewById(R.id.radioButton3);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                Log.d("chk", "id" + checkedId);

                if (checkedId == R.id.radioButton1) {
                    paymentMode = "paytm";
                } /*else if (checkedId == R.id.radioButton2) {
                    paymentMode = "xyz";
                }*/ else if (checkedId == R.id.radioButton3) {
                    paymentMode = "cod";
                }
            }
        });


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

                if (paymentMode.equals("")) {
                    Toast.makeText(Proceed_order.this, "Please choose payment mode", Toast.LENGTH_SHORT).show();
                } else {
                    if (paymentMode.equals("paytm")) {
                        cashAmount = Integer.parseInt(totalamount) - Integer.parseInt(walletamoutn);
                        //GetOrderId();

                        generateCheckSum(ca , wa);

                        //   getpay(cashAmount);
                    }
                    /*else if (paymentMode.equals("xyz")){
                        Toast.makeText(Proceed_order.this, "Coming Soon", Toast.LENGTH_SHORT).show();
                    }*/
                    else if (paymentMode.equals("cod")) {

                        Log.d("total" , totalamount);

                        cash_amount = Integer.parseInt(totalamount);

                        cod_online("cod" , ca , wa ,  is_wallet ,totalamount);
                    }
                }

            }
        });

    }


}
