/*
package app.in.chaiwale.Dialog;

import android.app.Dialog;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.paytm.pgsdk.PaytmOrder;
import com.paytm.pgsdk.PaytmPGService;
import com.paytm.pgsdk.PaytmPaymentTransactionCallback;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import app.in.chaiwale.Main.UserProfile_update;
import app.in.chaiwale.PaytmPack.Api;
import app.in.chaiwale.PaytmPack.Checksum;
import app.in.chaiwale.PaytmPack.Constants;
import app.in.chaiwale.PaytmPack.Paytm;
import app.in.chaiwale.PaytmPack.PaytmWallet;
import app.in.chaiwale.R;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.content.Context.MODE_PRIVATE;

public class AddMoney implements PaytmPaymentTransactionCallback{

    private Dialog dialog;
    private Context context;
    private Button mMakePayment;
    EditText mAmount;
    private String id, bankName, txnAmount, txnDate, gateWayName, txnId, checkSumHash, respMsg;


    public void AddMoney(final Context context) {
        try {
            this.context = context;
           dialog = new Dialog(context);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.add_money);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(true);
            mAmount = (EditText) dialog.findViewById(R.id.amount);

          mMakePayment = (Button) dialog.findViewById(R.id.makepayment);
            
            mMakePayment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mAmount.getText().toString().equals("")){
                        Toast.makeText(context, "Please Enter Amount", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        generateCheckSum(String.valueOf(mAmount.getText().toString()));
                    }

                    Toast.makeText(context, "Payment", Toast.LENGTH_SHORT).show();
                }
            });

            dialog.show();
        }catch (Exception ex){
            Log.e("tag_e", String.valueOf(ex));
        }
    }

    private void generateCheckSum(String amount) {

        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(5, TimeUnit.MINUTES)
                .readTimeout(5, TimeUnit.MINUTES)
                .build();

        //creating a retrofit object.
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Api.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

        //creating the retrofit api service
        Api apiService = retrofit.create(Api.class);

        //creating paytm object
        //containing all the values required
        final PaytmWallet paytm = new PaytmWallet(
                Constants.M_ID,
                Constants.CHANNEL_ID,
                amount,
                Constants.WEBSITE,
                Constants.CALLBACK_URL,
                Constants.INDUSTRY_TYPE_ID
        );

        //creating a call object from the apiService
        Call<Checksum> call = apiService.getChecksum(
                paytm.getmId(),
                paytm.getOrderId(),
                paytm.getCustId(),
                paytm.getChannelId(),
                paytm.getTxnAmount(),
                paytm.getWebsite(),
                paytm.getCallBackUrl() + paytm.getOrderId(),
                paytm.getIndustryTypeId()
        );

        //making the call to generate checksum
        call.enqueue(new Callback<Checksum>() {
            @Override
            public void onResponse(Call<Checksum> call, retrofit2.Response<Checksum> response) {

                Log.d("mytag","Response=="+response.body().getPaytStatus());
                initializePaytmPayment(response.body().getChecksumHash(), paytm);
                Log.d("mytag","All Check_sum==>"+paytm);
            }

            @Override
            public void onFailure(Call<Checksum> call, Throwable t) {
                Log.d("mytag","Faileds==>"+paytm);
                t.printStackTrace();

            }
        });
    }

    private void initializePaytmPayment(String checksumHash, PaytmWallet paytm) {

        //getting paytm service
        PaytmPGService Service = PaytmPGService.getStagingService();

        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("MID", Constants.M_ID);
        paramMap.put("ORDER_ID", paytm.getOrderId());
        paramMap.put("CUST_ID", paytm.getCustId());
        paramMap.put("CHANNEL_ID", paytm.getChannelId());
        paramMap.put("TXN_AMOUNT", paytm.getTxnAmount());
        paramMap.put("WEBSITE", paytm.getWebsite());
        paramMap.put("CALLBACK_URL", paytm.getCallBackUrl() + paytm.getOrderId());
        paramMap.put("CHECKSUMHASH", checksumHash);
        paramMap.put("INDUSTRY_TYPE_ID", paytm.getIndustryTypeId());


        //creating a paytm order object using the hashmap
        PaytmOrder order = new PaytmOrder(paramMap);

        //intializing the paytm service
        Service.initialize(order, null);

        //finally starting the payment transaction
        Service.startPaymentTransaction(context, true, true, (PaytmPaymentTransactionCallback) context);
        PaytmPaymentTransactionCallback zfzs =   Service.getmPaymentTransactionCallback();
        Log.d("mytag","Paytm_call_back"+zfzs.toString());

    }

    //all these overriden method is to detect the payment result accordingly
    @Override
    public void onTransactionResponse(Bundle bundle) {

        for (int i=0; i<bundle.size(); i++){

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
        Toast.makeText(context, bundle.toString(), Toast.LENGTH_LONG).show();
      //  callSuccessPaymentApi();
    }

    @Override
    public void networkNotAvailable() {
        Toast.makeText(context, "Network error", Toast.LENGTH_LONG).show();
    }

    @Override
    public void clientAuthenticationFailed(String s) {
        Toast.makeText(context, s, Toast.LENGTH_LONG).show();
    }

    @Override
    public void someUIErrorOccurred(String s) {
        Toast.makeText(context, s, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onErrorLoadingWebPage(int i, String s, String s1) {
        Toast.makeText(context, s, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onBackPressedCancelTransaction() {
        Toast.makeText(context, "Back Pressed", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onTransactionCancel(String s, Bundle bundle) {
        Toast.makeText(context, s + bundle.toString(), Toast.LENGTH_LONG).show();

    }



}
*/
