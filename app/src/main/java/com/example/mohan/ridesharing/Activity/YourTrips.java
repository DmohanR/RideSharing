package com.example.mohan.ridesharing.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mohan.ridesharing.Adapter.CarAdapter;
import com.example.mohan.ridesharing.Adapter.TripAdapter;
import com.example.mohan.ridesharing.Pojo.Car;
import com.example.mohan.ridesharing.Pojo.UpTrips;
import com.example.mohan.ridesharing.R;
import com.example.mohan.ridesharing.Response.TripData;
import com.example.mohan.ridesharing.Response.TripResponse;
import com.example.mohan.ridesharing.Response.UpcomingTripData;
import com.example.mohan.ridesharing.Response.UpcomingTripResponse;
import com.example.mohan.ridesharing.Response.carResponse;
import com.example.mohan.ridesharing.Rest.ApiClient;
import com.example.mohan.ridesharing.Rest.ApiInterface;
import com.example.mohan.ridesharing.Utils.PrefUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class YourTrips extends AppCompatActivity  {

    public int Trip_VALUE;
    private String label;
    private String tripType;
    List <TripData> upTripList;
    List <UpTrips> upTrips;
    RecyclerView mCarRecyclerView;
    ApiInterface apiInterface;
    private TextView noDatatv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_your_trips);
        Bundle mbundle= getIntent().getExtras();
        if (mbundle!=null){
            Trip_VALUE = mbundle.getInt("trip");
        }
        if (Trip_VALUE==1){
            label = "Upcoming Trips";
            tripType = "upcoming";
            loadTripApi();
        }
        if (Trip_VALUE ==2){
            label = "Ongoing Trips";
            tripType = "running";
            loadTripApi();
        }
        if (Trip_VALUE ==3){
            label = "Past Trips";
            tripType = "finished";
            loadTripApi();
        }
        setTitle(label);
       // noDatatv = (TextView) findViewById(R.id.no_data);
    }
    public void loadTripApi()
    {
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Log.d("reached","insideload");
        Call<TripResponse> call = apiInterface.getTripDetails(PrefUtils.getFromPrefs(this,PrefUtils.user_email,""),tripType);
        call.enqueue(new Callback<TripResponse>() {
            @Override
            public void onResponse(Call<TripResponse> call, Response<TripResponse> response) {
                Log.d("Got",response.body().getStatus()+"");
                upTripList = response.body().getTripDataList();
                initViews();
            }
            @Override
            public void onFailure(Call<TripResponse> call, Throwable t) {
                Log.d("fail","Failed");
            }
        });
    }
    public void initViews()
    {
        Log.d("init",":views");
        mCarRecyclerView = (RecyclerView) findViewById(R.id.recycleView_Trips);
        mCarRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        mCarRecyclerView.setLayoutManager(layoutManager);
        upTrips = new ArrayList<>();

        Log.d("init",":recycler");

        if (Trip_VALUE==1){
            for (int i = 0; i < upTripList.size(); i++) {
                UpTrips upTrip = new UpTrips(upTripList.get(i).getTripID(), upTripList.get(i).getVin(), upTripList.get(i).getFrom(), upTripList.get(i).getTo());
                upTrips.add(upTrip);
            }
        }

        if (Trip_VALUE==2){
            for (int i = 0; i < upTripList.size(); i++) {
                UpTrips upTrip = new UpTrips(upTripList.get(i).getTripID(), upTripList.get(i).getVin(), upTripList.get(i).getFrom(), upTripList.get(i).getTo());
                upTrips.add(upTrip);
            }
        }

        if (Trip_VALUE==3){

            for(int i=0; i<upTripList.size(); i++)
            {
                UpTrips upTrip = new UpTrips(upTripList.get(i).getTripID(),upTripList.get(i).getVin(),upTripList.get(i).getFrom(),upTripList.get(i).getTo(),upTripList.get(i).getFinishedTime());
                upTrips.add(upTrip);
            }
        }

       /* if (upTrips==null){
            noDatatv.setVisibility(View.VISIBLE);
        }*/

        RecyclerView.Adapter adapter = new TripAdapter(this,upTrips,Trip_VALUE);
        mCarRecyclerView.setAdapter(adapter);

        //((TripAdapter) adapter).setOnItemClickListener(this);
    }

   /* @Override
    public void onItemClick(int position) {
        Toast.makeText(this,position+"",Toast.LENGTH_SHORT).show();
        UpTrips upTrip = upTrips.get(position);
        Intent intent_unlock= new Intent(YourTrips.this,SingleTrip.class);
        intent_unlock.putExtra("title",upTrip.getVin());
        startActivity(intent_unlock);

    }*/
}
