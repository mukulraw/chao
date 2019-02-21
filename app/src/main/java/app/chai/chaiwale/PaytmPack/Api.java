package app.chai.chaiwale.PaytmPack;

import app.chai.chaiwale.paytmBean;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface Api {
 

    String BASE_URL = "http://ec2-13-126-246-74.ap-south-1.compute.amazonaws.com/";
    //String BASE_URL = "http://chaiwale.info/";
    //String BASE_URL = "http://192.168.0.120/paytm/";

    @Multipart
  //  @POST("generateChecksum.php")
 //   @POST("http://afsanaonline.com/chaiwala/paytm/generateChecksum.php")
    //@POST("paytm/generateChecksum.php")
    @POST("pmay/paytm/generateChecksum.php")
    Call<Checksum> getChecksum(
            @Part("MID") String MID,
            @Part("ORDER_ID") String ORDER_ID,
            @Part("CUST_ID") String CUST_ID,
            @Part("INDUSTRY_TYPE_ID") String INDUSTRY_TYPE_ID,
            @Part("CHANNEL_ID") String CHANNEL_ID,
            @Part("TXN_AMOUNT") String TXN_AMOUNT,
            @Part("WEBSITE") String WEBSITE,
            @Part("CALLBACK_URL") String CALLBACK_URL
    );

    @Multipart
    @POST("paytm/generateChecksum.php")
        //@POST("pmay/paytm/generateChecksum.php")
    Call<paytmBean> pay(
            @Part("MID") String MID,
            @Part("ORDER_ID") String ORDER_ID,
            @Part("CUST_ID") String CUST_ID,
            @Part("CHANNEL_ID") String CHANNEL_ID,
            @Part("TXN_AMOUNT") String TXN_AMOUNT,
            @Part("WEBSITE") String WEBSITE,
            @Part("CALLBACK_URL") String CALLBACK_URL,
            @Part("INDUSTRY_TYPE_ID") String INDUSTRY_TYPE_ID
    );

}