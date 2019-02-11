package app.in.chaiwale.Main;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import app.in.chaiwale.Pojo_Class.HistoryPojo;
import app.in.chaiwale.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyPackage extends Fragment {
    RecyclerView recyclerview;
    private List<HistoryPojo> myProfessionaarrlist = new ArrayList<>();
    private AcceptedAdaptor mAdapter;
    ImageView backbutton;

    public MyPackage() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_my_package, container, false);
        recyclerview=view.findViewById(R.id.recyclerview);


        mAdapter = new AcceptedAdaptor(myProfessionaarrlist,getActivity());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerview.setLayoutManager(mLayoutManager);

        recyclerview.setItemAnimator(new DefaultItemAnimator());
        recyclerview.setAdapter(mAdapter);
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

        prepareMovieData();

        return  view;
    }

    public class AcceptedAdaptor extends RecyclerView.Adapter<AcceptedAdaptor.MyViewHolder> {
        Activity activity;
        public List<HistoryPojo> moviesList;

        public class MyViewHolder extends RecyclerView.ViewHolder {



            TextView tea_name;

            Button active;
            public MyViewHolder(View view) {
                super(view);

                tea_name=view.findViewById(R.id.teaname);
                active=view.findViewById(R.id.active);

            }
        }


        public AcceptedAdaptor(List<HistoryPojo> moviesList , Activity activity) {
            this.moviesList = moviesList;
            this.activity=activity;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(activity)
                    .inflate(R.layout.mypackage_adapror, parent, false);

            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final AcceptedAdaptor.MyViewHolder holder, int position) {
            HistoryPojo movie = moviesList.get(position);


            if (holder.getLayoutPosition()!=0)
            {
                holder.active.setText("Buy Now");
                holder.active.setBackgroundResource(R.drawable.gray);

            }

            holder.tea_name.setText(movie.getPrice());
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
    private void prepareMovieData() {
        HistoryPojo movie;
        movie = new HistoryPojo("dfd","dfd","dfd", "Biscuit Name",  "","( mashala tea)","" , "");
        myProfessionaarrlist.add(movie);
        movie = new HistoryPojo("dfd", "dfd", "dfd", "Biscuit Name",  "","( mashala tea)","", "");
        myProfessionaarrlist.add(movie);
        movie = new HistoryPojo("dfd", "dfd", "dfd", "Biscuit Name",  "","( mashala tea)","", "");
        myProfessionaarrlist.add(movie);
        movie  = new HistoryPojo("dfd", "dfd", "dfd", "Biscuit Name",  "","( mashala tea)","", "");
        myProfessionaarrlist.add(movie);
        movie  = new HistoryPojo("dfd", "dfd", "dfd", "Biscuit Name",  "","( mashala tea)","", "");
        myProfessionaarrlist.add(movie);
        movie  = new HistoryPojo("dfd", "dfd", "dfd", "Biscuit Name",  "","( mashala tea)","", "");
        myProfessionaarrlist.add(movie);


        mAdapter.notifyDataSetChanged();

    }

}
