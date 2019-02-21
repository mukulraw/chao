package app.chai.chaiwale.Main;


import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
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
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

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

import app.chai.chaiwale.API_URL;
import app.chai.chaiwale.Activity.SessionManager;
import app.chai.chaiwale.Pojo_Class.Category_Pojo;
import app.chai.chaiwale.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class MenuSecond extends Fragment {

    RecyclerView recyclerview;
    private List<Category_Pojo> myProfessionaarrlist = new ArrayList<>();
    private AcceptedAdaptor mAdapter;
    ImageView backbutton;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    ProgressBar progress_circular;

    TextView menuname;

    SessionManager sessionManager;
    String useid;
    RequestQueue  requestQueue;
    public MenuSecond() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_menu_second, container, false);
        recyclerview=view.findViewById(R.id.recyclerview);
        preferences=getActivity().getSharedPreferences("item",Context.MODE_PRIVATE);

        editor=preferences.edit();
        progress_circular=view.findViewById(R.id.progress_circular);
        requestQueue = Volley.newRequestQueue(getActivity());

        sessionManager=new SessionManager(getActivity());
        HashMap<String, String> user = sessionManager.getUserDetails();
        final String Phone = user.get(sessionManager.KEY_Phone);

        useid= user.get(SessionManager.KEY_UserID);


        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerview.setLayoutManager(mLayoutManager);

        recyclerview.setItemAnimator(new DefaultItemAnimator());

        backbutton=view.findViewById(R.id.backbutton);
        menuname=view.findViewById(R.id.menuname);


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

        getPRoducet();

    return  view;
    }

    public class AcceptedAdaptor extends RecyclerView.Adapter<AcceptedAdaptor.MyViewHolder> {
        Activity activity;
        public List<Category_Pojo> moviesList;

        public class MyViewHolder extends RecyclerView.ViewHolder {



            TextView tea_name,quantity,price,title;
            LinearLayout up,down;

            public MyViewHolder(View view) {
                super(view);

                tea_name=view.findViewById(R.id.tea_name);
                quantity=view.findViewById(R.id.quantity);
                up=view.findViewById(R.id.up);
                down=view.findViewById(R.id.down);
                price=view.findViewById(R.id.price);
                title=view.findViewById(R.id.title);

            }
        }


        public AcceptedAdaptor(List<Category_Pojo> moviesList , Activity activity) {
            this.moviesList = moviesList;
            this.activity=activity;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(activity)
                    .inflate(R.layout.firstmanu_adaptor, parent, false);

            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, int position) {
         final    Category_Pojo movie = moviesList.get(position);


            holder.tea_name.setText(movie.getProduct_name());
            //holder.title.setText("Mystery Oreo");
            holder.price.setText("Rs. "+movie.getProduct_price()+"/-");
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
                                    String totalItem= jsonObject1.getString("totalItem");
                                    editor.putString("totlaitem",totalItem);
                                    editor.apply();
                                    editor.commit();
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
                                params.put("price", movie.getProduct_price());
                                Log.e("post", String.valueOf(params));
                                return params;
                            }
                        };

                        requestQueue.add(stringRequest);
                    }



                }
            });


           /* holder.up.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    final String q = holder.quantity.getText().toString();

                    int q1 = Integer.parseInt(q);
                    q1++;
                    final int finalQ = q1;
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, API_URL.AddBucket, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String s) {

                            Log.e("respoance",s);

                            try {
                                JSONObject jsonObject1=new JSONObject(s);
                                String message= jsonObject1.getString("message");
                                String status= jsonObject1.getString("status");
                                String totalItem= jsonObject1.getString("totalItem");
                                editor.putString("totlaitem",totalItem);
                                editor.apply();
                                editor.commit();
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
                            params.put("price", movie.getProduct_price());
                            Log.e("post", String.valueOf(params));
                            return params;
                        }
                    };
                    requestQueue.add(stringRequest);



                }
            });


            holder.up.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    final String q = holder.quantity.getText().toString();

                    final int[] q1 = {Integer.parseInt(q)};

                    StringRequest stringRequest = new StringRequest(Request.Method.POST, API_URL.AddBucket, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String s) {

                            Log.e("respoance",s);

                            try {
                                JSONObject jsonObject1=new JSONObject(s);
                                String message= jsonObject1.getString("message");
                                String status= jsonObject1.getString("status");



                                q1[0]++;
                                holder.quantity.setText(String.valueOf(q1[0]));

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
                            params.put("quantity",q);
                            params.put("price", movie.getProduct_price());
                            return params;
                        }
                    };
                    requestQueue.add(stringRequest);



                }
            });
*/

            holder.up.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    final String q = holder.quantity.getText().toString();
                    int q1 = Integer.parseInt(q);
                    q1++;
                    final int finalQ = q1;
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, API_URL.AddBucket, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String s) {

                            Log.e("respoance",s);

                            try {
                                JSONObject jsonObject1=new JSONObject(s);
                                String message= jsonObject1.getString("message");
                                String status= jsonObject1.getString("status");
                                String totalItem= jsonObject1.getString("totalItem");

                                editor.putString("totlaitem",totalItem);
                                editor.apply();
                                editor.commit();

                                //Toast.makeText(activity, ""+totalItem, Toast.LENGTH_SHORT).show();
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
                            params.put("price", movie.getProduct_price());
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


    void getPRoducet()
    {
        progress_circular.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, API_URL.GetProduct, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

                Log.e("respoance",s);

                progress_circular.setVisibility(View.INVISIBLE);
                try {
                    JSONObject jsonObject1=new JSONObject(s);
                    String message= jsonObject1.getString("message");
                    String status= jsonObject1.getString("status");

                    if (status.equalsIgnoreCase("1"))
                    {
                        JSONArray array=jsonObject1.getJSONArray("data");


                        for (int i=0;i<array.length(); i++)




                        {


                            JSONObject jsonObject=array.getJSONObject(i);

                            String id=jsonObject.getString("category_id");
                            String category_name=jsonObject.getString("category_name");
                           // menuname.setText(category_name);
                            if (id.equalsIgnoreCase("2"))
                            {
                                JSONArray jsonArray=jsonObject.getJSONArray("products");


                                for (int j=0;j<jsonArray.length();j++)
                                {

                                    JSONObject jsonObject2=jsonArray.getJSONObject(j);

                                    myProfessionaarrlist.add( new Category_Pojo(
                                            jsonObject2.getString("product_id"),
                                            jsonObject2.getString("product_price"),
                                            jsonObject2.getString("product_image"),
                                            jsonObject2.getString("product_name")

                                    ));

                                }
                            }

                            AcceptedAdaptor acceptedAdaptor=new AcceptedAdaptor(myProfessionaarrlist,getActivity());
                            recyclerview.setAdapter(acceptedAdaptor);
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

                progress_circular.setVisibility(View.INVISIBLE);

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                /*params.put("mobile", Phone_No.getText().toString());*/
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }


}
