package app.in.chaiwale.Main;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import app.in.chaiwale.API_URL;
import app.in.chaiwale.Activity.SessionManager;
import app.in.chaiwale.Pojo_Class.getCartPojo;
import app.in.chaiwale.R;

public class Cart_Activity extends AppCompatActivity {
    RecyclerView recyclerview;

     List<getCartPojo> myProfessionaarrlist = new ArrayList<>();

     String useid;
     CardView cardview;
    private AcceptedAdaptor mAdapter;
    ImageView backbutton;


    public  int itmposition;
    TextView totalamount,finalprie;
    RequestQueue requestQueue;
    Button proceedOrder;

    String totalPrice;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    SessionManager sessionManager;

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    Cart_Activity.this.finish();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_);
        requestQueue = Volley.newRequestQueue(Cart_Activity.this);
        preferences=getSharedPreferences("item",Context.MODE_PRIVATE);

        editor=preferences.edit();


        sessionManager=new SessionManager(this);
        HashMap<String, String> user = sessionManager.getUserDetails();
        final String Phone = user.get(sessionManager.KEY_Phone);

         useid= user.get(SessionManager.KEY_UserID);


        cardview=findViewById(R.id.cardview);
        totalamount=findViewById(R.id.totalamount);
        finalprie=findViewById(R.id.finalprie);
      //  subtotoal=findViewById(R.id.subtotoal);
        backbutton=findViewById(R.id.next_btn);
        proceedOrder=findViewById(R.id.proceedOrder);
        recyclerview=findViewById(R.id.recyclerview);
       // mAdapter = new AcceptedAdaptor(myProfessionaarrlist,Cart_Activity.this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(Cart_Activity.this);
        recyclerview.setLayoutManager(mLayoutManager);
        recyclerview.setLayoutManager(mLayoutManager);
        recyclerview.setItemAnimator(new DefaultItemAnimator());
       cardview.setVisibility(View.INVISIBLE);
        proceedOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(Cart_Activity.this,Proceed_order.class));
                Intent intent=new Intent(Cart_Activity.this,Proceed_order.class);
                intent.putExtra("totalamoutn",totalPrice);
                startActivity(intent);
            }
        });
        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cart_Activity.this.finish();

            }
        });

        getCartItem();

    }

    public class AcceptedAdaptor extends RecyclerView.Adapter<AcceptedAdaptor.MyViewHolder> {
        Activity activity;
        public List<getCartPojo> moviesList;

        public class MyViewHolder extends RecyclerView.ViewHolder {
            TextView tea_name,quantity,price;
            ImageView remove;
            LinearLayout up,down;
            public MyViewHolder(View view) {
                super(view);
                tea_name=view.findViewById(R.id.tea_name);
                quantity=view.findViewById(R.id.quantity);
                up=view.findViewById(R.id.up);
                down=view.findViewById(R.id.down);
                price=view.findViewById(R.id.price);
                remove=view.findViewById(R.id.remove);
            }
        }
        public void removeItem(int position) {
            myProfessionaarrlist.remove(position);
            notifyItemRemoved(position);
        }

        public AcceptedAdaptor(List<getCartPojo> moviesList , Activity activity) {
            this.moviesList = moviesList;
            this.activity=activity;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(activity)
                    .inflate(R.layout.cartadaptor, parent, false);

            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int position) {
          final   getCartPojo movie = moviesList.get(position);

            holder.price.setText("Rs. "+movie.getUnitprice()+"/-");
            holder.tea_name.setText(movie.getProduct_name());
            holder.quantity.setText(movie.getQuantity());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    // startActivity(new Intent(getActivity(), Budeget_Details.class));
                }
            });


            holder.down.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    final String q = holder.quantity.getText().toString();


                    if (q.equalsIgnoreCase("0"))
                    {

                    }else
                    {


                        int q1 = Integer.parseInt(q);

                        if (q1!=1)
                        {
                            q1--;
                        }
                        final int finalQ = q1;
                        StringRequest stringRequest = new StringRequest(Request.Method.POST, API_URL.AddBucket, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String s) {
                                Log.e("respoance",s);
                                holder.quantity.setText(String.valueOf(finalQ));
                                try {
                                    JSONObject jsonObject1=new JSONObject(s);
                                    String message= jsonObject1.getString("message");
                                    String status= jsonObject1.getString("status");
                                    if (status.equalsIgnoreCase("1"))
                                    {
                                         totalPrice= jsonObject1.getString("totalPrice");
                                        String totalItem= jsonObject1.getString("totalItem");
                                        totalamount.setText("Rs."+totalPrice);
                                     //   subtotoal.setText("Rs."+totalPrice);
                                        finalprie.setText("Rs."+totalPrice);
                                        Toast.makeText(activity, ""+message, Toast.LENGTH_SHORT).show();



                                    }
                                } catch (Exception  e) {
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError volleyError) {
                                // pGif.setVisibility(View.GONE);

                            }
                        }) {
                            @Override
                            protected Map<String, String> getParams() {
                                Map<String, String> params = new HashMap<>();
                                params.put("userId", useid);
                                params.put("productId", movie.getProduct_id());
                                params.put("quantity",String.valueOf(finalQ));
                                params.put("price", movie.getUnitprice());
                                Log.e("post", String.valueOf(params));
                                return params;
                            }
                        };

                      requestQueue.add(stringRequest);
                    }



                }
            });


            holder.up.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    final String q = holder.quantity.getText().toString();

                     int q1 = Integer.parseInt(q);
                    ++q1;
                    final int finalQ = q1;
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, API_URL.AddBucket, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String s) {

                            Log.e("respoance",s);

                            try {
                                JSONObject jsonObject1=new JSONObject(s);
                                String message= jsonObject1.getString("message");

                                String status= jsonObject1.getString("status");
                                if (status.equalsIgnoreCase("1"))
                                {
                                     totalPrice= jsonObject1.getString("totalPrice");
                                    String totalItem= jsonObject1.getString("totalItem");
                                    totalamount.setText("Rs."+totalPrice);
                                 //   subtotoal.setText("Rs."+totalPrice);
                                    finalprie.setText("Rs."+totalPrice);
                                    Toast.makeText(activity, ""+message, Toast.LENGTH_SHORT).show();



                                }



                                holder.quantity.setText(String.valueOf(finalQ));

                            } catch (Exception  e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            // pGif.setVisibility(View.GONE);

                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() {
                            Map<String, String> params = new HashMap<>();
                            params.put("userId", useid);
                            params.put("productId", movie.getProduct_id());
                            params.put("quantity", String.valueOf(finalQ));
                            params.put("price", movie.getUnitprice());
                            Log.e("post", String.valueOf(params));
                            return params;
                        }
                    };
                    requestQueue.add(stringRequest);



                }
            });


            holder.remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    StringRequest stringRequest = new StringRequest(Request.Method.POST, API_URL.Remove, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String s) {
                            itmposition=position;
                            removeItem(itmposition);
                            Log.e("respoance",s);
                            try {
                                JSONObject jsonObject1=new JSONObject(s);
                                String message= jsonObject1.getString("message");
                                String status= jsonObject1.getString("status");
                                if (status.equalsIgnoreCase("1"))
                                {
                                     totalPrice= jsonObject1.getString("totalPrice");
                                    String totalItem= jsonObject1.getString("totalItem");
                                    editor.putString("totlaitem",totalItem);
                                    editor.apply();
                                    editor.commit();

                                    totalamount.setText("Rs."+totalPrice);
                                 //   subtotoal.setText("Rs."+totalPrice);
                                    finalprie.setText("Rs."+totalPrice);
                                    Toast.makeText(activity, ""+message, Toast.LENGTH_SHORT).show();

                                    if (totalItem.equals("")){
                                        startActivity(new Intent(Cart_Activity.this, MainNavigation.class));
                                    }
                                    else {
                                    }
                                }

                            } catch (Exception  e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            // pGif.setVisibility(View.GONE);

                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() {
                            Map<String, String> params = new HashMap<>();
                            params.put("userId", useid);
                            params.put("productId", movie.getProduct_id());
                            params.put("cartid", movie.getCart_id());
                            Log.e("post", String.valueOf(params));
                            return params;
                        }
                    };
                    requestQueue.add(stringRequest);



                }
            });



        }
        @Override
        public int getItemCount() {
            return moviesList.size();
        }
    }


    void getCartItem()
    {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, API_URL.getCart, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

                Log.e("getCartDetails",s);

                try {
                    JSONObject jsonObject1=new JSONObject(s);
                    String message= jsonObject1.getString("message");
                    String status= jsonObject1.getString("status");

                    if (status.equalsIgnoreCase("1"))
                    {
                        cardview.setVisibility(View.VISIBLE);
                        totalPrice= jsonObject1.getString("totalPrice");
                        totalamount.setText("Rs."+totalPrice);
                     //   subtotoal.setText("Rs."+totalPrice);
                        finalprie.setText("Rs."+totalPrice);
                        String totalItem= jsonObject1.getString("totalItem");


                        JSONArray array=jsonObject1.getJSONArray("data");


                        for (int i=0;i<array.length(); i++)
                        {
                            JSONObject jsonObject=array.getJSONObject(i);
                            myProfessionaarrlist.add(new getCartPojo(

                                    jsonObject.getString("userId"),
                                    jsonObject.getString("cart_id"),
                                    jsonObject.getString("category_id"),
                                    jsonObject.getString("category_name"),
                                    jsonObject.getString("product_id"),
                                    jsonObject.getString("product_name"),
                                    jsonObject.getString("quantity"),
                                    jsonObject.getString("unitprice"),
                                    jsonObject.getString("toprice"),
                                    jsonObject.getString("image")

                                    ));
                            }

                            AcceptedAdaptor acceptedAdaptor=new AcceptedAdaptor(myProfessionaarrlist,Cart_Activity.this);
                            recyclerview.setAdapter(acceptedAdaptor);
                        }

                        else
                    {

                        Toast.makeText(Cart_Activity.this, "No Item", Toast.LENGTH_SHORT).show();
                        proceedOrder.setVisibility(View.INVISIBLE);
                        cardview.setVisibility(View.INVISIBLE);
                    }

                } catch (Exception  e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                // pGif.setVisibility(View.GONE);

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("userid", useid);
                Log.e("send", String.valueOf(params));
                return params;
            }
        };

        Volley.newRequestQueue(this).add(stringRequest);

    }
}
