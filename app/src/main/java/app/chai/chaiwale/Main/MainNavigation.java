package app.chai.chaiwale.Main;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import app.chai.chaiwale.API_URL;
import app.chai.chaiwale.Activity.Login;
import app.chai.chaiwale.Activity.SessionManager;
import app.chai.chaiwale.R;
import de.hdodenhof.circleimageview.CircleImageView;

public class MainNavigation extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    LinearLayout refend,about,offers, privcypolicy,tnc,navbar,menMe,cart,mainamenu,orderhistory,mysbcriptop,profile,Myprofile,logout;

    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    CircleImageView btncamera,dsfs;
    TextView Phone_No,name;
    String useid;
    TextView walletamount,totalcart;


    public static final int notify = 500;  //interval between two services(Here Service run every 20 Sec)
    private Handler mHandler = new Handler();   //run on another Thread to avoid crash
    private Timer mTimer = null;    //timer handling

    String countime = "";

    WebView webView;
    ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_navigation);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        preferences=getSharedPreferences("item",Context.MODE_PRIVATE);

        editor=preferences.edit();
        final SessionManager sessionManager;
        sessionManager=new SessionManager(this);
        HashMap<String, String> user = sessionManager.getUserDetails();
         String Phone = user.get(sessionManager.KEY_Phone);
         useid= user.get(SessionManager.KEY_UserID);
        menMe=findViewById(R.id.menMe);
        dsfs=findViewById(R.id.dsfs);
        totalcart=findViewById(R.id.totalcart);
        navbar=findViewById(R.id.navbar);
        cart=findViewById(R.id.cart);
        mainamenu=findViewById(R.id.mainamenu);
        navbar.setVisibility(View.VISIBLE);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View headerview = navigationView.getHeaderView(0);
        getLoginVerify();



        orderhistory =  headerview.findViewById(R.id.orderhistory);
        mysbcriptop =  headerview.findViewById(R.id.mysbcriptop);
        profile =  headerview.findViewById(R.id.profile);
        Myprofile =  headerview.findViewById(R.id.Myprofile);
        Phone_No =  headerview.findViewById(R.id.Phone_No);
        name =  headerview.findViewById(R.id.name);
        logout =  headerview.findViewById(R.id.logout);
        btncamera =  headerview.findViewById(R.id.btncamera);
        walletamount =  headerview.findViewById(R.id.walletamount);
        privcypolicy =  headerview.findViewById(R.id.privcypolicy);
        tnc =  headerview.findViewById(R.id.tnc);
        about =  headerview.findViewById(R.id.about);
        refend =  headerview.findViewById(R.id.refend);
        offers =  headerview.findViewById(R.id.offers);

        offers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainNavigation.this,Offers.class));

            }
        });


        refend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainNavigation.this,Refund.class));

            }
        });
        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainNavigation.this,Aboutus.class));

            }
        });
        tnc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                startActivity(new Intent(MainNavigation.this,Tnc.class));

            }
        });

        privcypolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(MainNavigation.this,PrivecyPolicy.class));

            }
        });

        dsfs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction ft = fragmentManager.beginTransaction();
                MenuFirst fragment = new MenuFirst();
                ft.replace(R.id.frame, fragment);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
                ft.addToBackStack(null);
                ft.commit();


            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sessionManager.logoutUser();

                editor.clear();
                editor.apply();

                Intent intent=new Intent(MainNavigation.this,Login.class);
                startActivity(intent);
                MainNavigation.this.finish();

            }
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity( new Intent(MainNavigation.this,UserProfile_update.class));


            }
        });
        Myprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity( new Intent(MainNavigation.this,UserProfile_update.class));


            }
        });
        orderhistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction ft = fragmentManager.beginTransaction();
                OrderHistory fragment = new OrderHistory();
                ft.replace(R.id.frame, fragment);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
                ft.addToBackStack(null);
                ft.commit();

               // getSupportFragmentManager().beginTransaction().replace(R.id.frame, new OrderHistory()).commit();

                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                } else {
                    drawer.openDrawer(GravityCompat.START);
                }


            }
        });
        mysbcriptop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

              /*  getSupportFragmentManager().beginTransaction().replace(R.id.frame, new MyPackage()).addToBackStack(null).commit();
                *//*FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction ft = fragmentManager.beginTransaction();
                MyPackage fragment = new MyPackage();
                ft.replace(R.id.frame, fragment);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
                ft.addToBackStack(null);
                ft.commit();*//*
              //  getSupportFragmentManager().beginTransaction().replace(R.id.frame, new MyPackage()).commit();

                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                } else {
                    drawer.openDrawer(GravityCompat.START);
                }
*/

            }
        });


        menMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                } else {
                    drawer.openDrawer(GravityCompat.START);
                }

            }
        });


        mainamenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction ft = fragmentManager.beginTransaction();
                MenuFirst fragment = new MenuFirst();
                ft.replace(R.id.frame, fragment);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
                ft.addToBackStack(null);
                ft.commit();

              //  getSupportFragmentManager().beginTransaction().replace(R.id.frame, new MenuFirst()).commit();


            }
        });

        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


               /* FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction ft = fragmentManager.beginTransaction();
                MenuFirst fragment = new MenuFirst();
                ft.replace(R.id.frame, fragment);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
                ft.addToBackStack(null);
                ft.commit();*/

              /* getSupportFragmentManager().beginTransaction().replace(R.id.frame, new Cart()).commit();
                navbar.setVisibility(View.GONE);*/



                startActivity( new Intent(MainNavigation.this,Cart_Activity.class));


            }
        });

        getSupportFragmentManager().beginTransaction().replace(R.id.frame, new MenuFirst()).commit();
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
            navbar.setVisibility(View.VISIBLE);


        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_navigation, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void getLoginVerify() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, API_URL.USeprofile, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

                Log.e("USeprofile",s);
                try {


                    JSONObject jsonObject1=new JSONObject(s);


                    String message= jsonObject1.getString("message");

                    JSONObject jsonObject11=jsonObject1.getJSONObject("data");


                    String image=jsonObject11.getString("image");

                    String name1=jsonObject11.getString("userName");

                    String phone=jsonObject11.getString("phone");
                    Phone_No.setText("+91 "+phone);
                    name.setText(name1);
                    Glide.with(MainNavigation.this).load(image)
                            .thumbnail(0.5f)
                            .crossFade()
                            .error(R.drawable.user)
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(btncamera);

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
    protected void onResume() {
        super.onResume();

        if (isNetworkAvailable())
        {

            getWallet();

        }else {

            Toast.makeText(this, "Please Check internet ", Toast.LENGTH_SHORT).show();
        }
        if (mTimer != null) {// Cancel if already existed
            // mTimer.cancel();
            Log.wtf("service", "all ready started");


        } else {
            if (isNetworkAvailable())
            {

            }else {

                Toast.makeText(this, "Please Check internet ", Toast.LENGTH_SHORT).show();
            }
            mTimer = new Timer();   //recreate new
            //Toast.makeText(getContext(),"Refreshed",Toast.LENGTH_SHORT).show();
            mTimer.scheduleAtFixedRate(new TimeDisplay(), 0, notify);
        }
    }

    public void getWallet() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, API_URL.getWallet, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Log.e("getwallet",s);
                try {

                    JSONObject jsonObject1=new JSONObject(s);

                    JSONObject jsonObject= jsonObject1.getJSONObject("data");

                   String walletamoutn=jsonObject.getString("wallet_amount");
                    walletamount.setText("Rs."+walletamoutn+"/-");
                   // Toast.makeText(MainNavigation.this, ""+walletamoutn, Toast.LENGTH_SHORT).show();

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
                params.put("userId",useid);
                Log.e("post", String.valueOf(params));
                return params;
            }
        };
        Volley.newRequestQueue(this).add(stringRequest);
    }


    class TimeDisplay extends TimerTask {
        @Override
        public void run() {
            // run on another thread
            mHandler.post(new Runnable() {
                @Override
                public void run() {


                    // SharedddProedfff

                   // editor.putString("totlaitem",totalItem);

                    String totalitem = preferences.getString("totlaitem","");
                    if (totalitem.equalsIgnoreCase(""))
                    {

                    }
                    else
                    {
                        totalcart.setText(totalitem);
                       // Toast.makeText(MainNavigation.this, ""+totalitem, Toast.LENGTH_SHORT).show();

                    }




                  /*  if ()
                    {

                    }
                    else
                    {

                    }
*/

                }
            });
        }
    }


    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();


    }


}
