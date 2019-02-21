package app.chai.chaiwale.Main;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
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

import app.chai.chaiwale.API_URL;
import app.chai.chaiwale.Activity.SessionManager;
import app.chai.chaiwale.Pojo_Class.HistoryPojo;
import app.chai.chaiwale.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class OrderHistory extends Fragment {

    RecyclerView recyclerview;
    private List<HistoryPojo> myProfessionaarrlist = new ArrayList<>();
    private AcceptedAdaptor mAdapter;
    ImageView backbutton;


    String useid;
    SessionManager sessionManager;

    public OrderHistory() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_order_history, container, false);
        recyclerview=view.findViewById(R.id.recyclerview);

        sessionManager=new SessionManager(getActivity());
        HashMap<String, String> user = sessionManager.getUserDetails();
        final String Phone = user.get(sessionManager.KEY_Phone);

         useid= user.get(SessionManager.KEY_UserID);
       // mAdapter = new AcceptedAdaptor(myProfessionaarrlist,getActivity());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerview.setLayoutManager(mLayoutManager);
       // recyclerview.setLayoutManager(mLayoutManager);
        recyclerview.setItemAnimator(new DefaultItemAnimator());
        //recyclerview.setAdapter(mAdapter);
        backbutton=view.findViewById(R.id.next_btn);
        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment someFragment = new MenuFirst();
                Bundle b=new Bundle();
                b.putString("id","1");
                someFragment.setArguments(b);
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.frame, someFragment ); // give your fragment container id in first parameter
                /* transaction.addToBackStack(null);*/  // if written, this transaction will be added to backstack
                transaction.commit();

            }
        });

        //prepareMovieData();
        getOrderHistory();
        return  view;
    }


    public class AcceptedAdaptor extends RecyclerView.Adapter<AcceptedAdaptor.MyViewHolder> {
        Activity activity;
        public List<HistoryPojo> moviesList;

        public class MyViewHolder extends RecyclerView.ViewHolder {



            TextView tea_name,orderid,date,status,walldeduct,codamount , COD;
            public MyViewHolder(View view) {
                super(view);

                tea_name=view.findViewById(R.id.price);
                orderid=view.findViewById(R.id.orderid);
                date=view.findViewById(R.id.date);
                status=view.findViewById(R.id.status);
                walldeduct=view.findViewById(R.id.walldeduct);
                codamount=view.findViewById(R.id.codamount);
                COD = view.findViewById(R.id.COD);

            }
        }


        public AcceptedAdaptor(List<HistoryPojo> moviesList , Activity activity) {
            this.moviesList = moviesList;
            this.activity=activity;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(activity)
                    .inflate(R.layout.orderhistory_adapror, parent, false);

            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, int position) {
            HistoryPojo movie = moviesList.get(position);

            holder.COD.setText( movie.getMethod() + " Amount");

            holder.tea_name.setText("Rs."+movie.getPrice());
            holder.orderid.setText(""+movie.getOrderid());
            holder.date.setText(""+movie.getDatee());
            holder.status.setText(""+movie.getStatus());
            holder.walldeduct.setText(" - Rs."+movie.getWalletdud());
            holder.codamount.setText(" - Rs."+movie.getCodamount());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    // startActivity(new Intent(getActivity(), Budeget_Details.class));
                }
            });


        }

        @Override
        public int getItemCount() {
            return moviesList.size();
        }
    }


    void getOrderHistory()
    {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, API_URL.orderHistory, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Log.e("getCartDetails",s);

                try {
                    JSONObject jsonObject1=new JSONObject(s);
                    String message= jsonObject1.getString("message");
                    String status= jsonObject1.getString("status");

                    if (status.equalsIgnoreCase("1"))
                    {




                        JSONArray array=jsonObject1.getJSONArray("data");
                        for (int i=0;i<array.length();i++)

                        {

                            JSONObject jsonObject=array.getJSONObject(i);
                            myProfessionaarrlist.add(new HistoryPojo(

                                    jsonObject.getString("user_id"),
                                    jsonObject.getString("orderId"),
                                    jsonObject.getString("placed_date"),
                                    jsonObject.getString("total_price"),
                                    jsonObject.getString("order_status"),
                                    jsonObject.getString("wallet_amount"),
                                    jsonObject.getString("cash_amount"),
                                    jsonObject.getString("payment_method")

                            ));

                        }

                        AcceptedAdaptor acceptedAdaptor=new AcceptedAdaptor(myProfessionaarrlist,getActivity());
                       recyclerview.setAdapter(acceptedAdaptor);
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
                Log.e("send", String.valueOf(params));
                return params;
            }
        };

        Volley.newRequestQueue(getActivity()).add(stringRequest);

    }

}
