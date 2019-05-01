package com.example.mohan.ridesharing.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mohan.ridesharing.Activity.ShowCars;
import com.example.mohan.ridesharing.Activity.SingleTrip;
import com.example.mohan.ridesharing.Activity.YourTrips;
import com.example.mohan.ridesharing.Pojo.Car;
import com.example.mohan.ridesharing.Pojo.UpTrips;
import com.example.mohan.ridesharing.Request.BookRequest;
import com.example.mohan.ridesharing.Request.EndTripRequest;
import com.example.mohan.ridesharing.Response.BookResponse;
import com.example.mohan.ridesharing.Response.EndTripResponse;
import com.example.mohan.ridesharing.Rest.ApiClient;
import com.example.mohan.ridesharing.Rest.ApiInterface;
import com.example.mohan.ridesharing.Utils.PrefUtils;
import com.example.mohan.ridesharing.R;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TripAdapter extends RecyclerView.Adapter<TripAdapter.ImageViewHolder> {
    private Context mContext;
    private List<UpTrips> tripsList;
    UpTrips trip;
    private int TRIP_VALUE;
    String tripId,vin,status;
    ApiInterface apiServices;
    //private OnItemClickListener mListener;
    public TripAdapter(Context context,List<UpTrips> tripsList,int TRIP_VALUE){
        this.mContext = context;
        this.tripsList = tripsList;
        this.TRIP_VALUE = TRIP_VALUE;
    }
    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.trip_card,viewGroup,false);
        return new ImageViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ImageViewHolder imageViewHolder, int i) {
        final String tripID = tripsList.get(i).getTripID();

        trip = tripsList.get(i);


        imageViewHolder.tripid.setText("Trip Id : \t"+tripsList.get(i).getTripID());
        imageViewHolder.vin.setText("Vehicle No. : \t"+tripsList.get(i).getVin());
        imageViewHolder.from.setText("Start Date : \t"+tripsList.get(i).getFrom());
        imageViewHolder.to.setText("End Date : \t"+tripsList.get(i).getTo());
        if (imageViewHolder.Start.getVisibility()==View.VISIBLE) {
            imageViewHolder.Start.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(view.getContext(), SingleTrip.class);
                    intent.putExtra("tripID",tripID);
                    mContext.startActivity(intent);
                }
            });
        }
        if (imageViewHolder.Cancel.getVisibility()==View.VISIBLE) {
            imageViewHolder.Cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String message;
                    if (imageViewHolder.Cancel.getText() == "EndTrip") {
                        status = "finished";
                        message = "You are about to End trip";
                    }
                    else{ status="cancelled";message = "You are about to Cancel trip";
                    }

                    new AlertDialog.Builder(mContext)
                                .setTitle("Trip details")
                                .setMessage(message)
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .setNeutralButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        sendRequest();
                                    /*Intent in = new Intent(mContext,YourTrips.class);
                                    in.putExtra("trip",3);
                                    mContext.startActivity(in);*/
                                    }
                                }).show();
                        //Toast.makeText(mContext,"Cancelled",Toast.LENGTH_SHORT).show();
                }
            });

        }
    }

    private void sendRequest() {

        String user_name = PrefUtils.getFromPrefs(mContext,PrefUtils.user_email,"");
        final String mcaddress = PrefUtils.getFromPrefs(mContext,PrefUtils.mc_address,"");
        Log.d("adapter",user_name+":"+"VIN : "+vin);
        if (trip!=null) {
            EndTripRequest EndRequest = new EndTripRequest();
            EndRequest.setTripID(trip.getTripID());
            EndRequest.setUserName(user_name);
            EndRequest.setMcaddress(mcaddress);
            EndRequest.setVin(trip.getVin());
            EndRequest.setStatus(status);
            Gson gson = new Gson();
            String json = gson.toJson(EndRequest);
            Map<String, String> map = new HashMap<String, String>();
            map = gson.fromJson(json, map.getClass());
            apiServices = ApiClient.getClient().create(ApiInterface.class);
            Call<EndTripResponse> call = apiServices.sendTripRequest(map);
            call.enqueue(new Callback<EndTripResponse>() {
                @Override
                public void onResponse(Call<EndTripResponse> call, Response<EndTripResponse> response) {
                    Log.d("Finished", trip.getTripID());
                    if (response.body().getStatus().equalsIgnoreCase("fail")) {
                        new AlertDialog.Builder(mContext)
                                .setTitle("End Trip")
                                .setMessage(response.body().getMessage())
                                .setIcon(android.R.drawable.ic_dialog_alert)
//                            .setNeutralButton(android.R.string.ok, new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialogInterface, int i) {
//                                    if(mContext instanceof ShowCars)
//                                    {
//                                        ((ShowCars) mContext).callCarApi();
//                                    }
//                                }
//                            })
                                .show();
                    } else {
                        new AlertDialog.Builder(mContext)
                                .setTitle("End Trip")
                                .setMessage("You have successfully finished trip")
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .setNeutralButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        Intent in = new Intent(mContext, YourTrips.class);
                                        in.putExtra("trip", 3);
                                        mContext.startActivity(in);
                                    }
                                }).show();
                    }

                }

                @Override
                public void onFailure(Call<EndTripResponse> call, Throwable t) {
                    Log.d("Failed", "Fail");
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return tripsList.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder {

        public TextView tripid;
        public TextView vin;
        public TextView from;
        public TextView to;
        public ImageView carImageV;
        public Button Start, Cancel;

        public ImageViewHolder(View itemView) {
            super(itemView);
            //carImageV = (ImageView) itemView.findViewById(R.id.car_image);
            tripid = (TextView) itemView.findViewById(R.id.trip_Id);
            vin = (TextView) itemView.findViewById(R.id.vin);
            from = (TextView) itemView.findViewById(R.id.from);
            to = (TextView) itemView.findViewById(R.id.to);
            Start = (Button) itemView.findViewById(R.id.startTrip_btn);
            Cancel =(Button) itemView.findViewById(R.id.cancelTrip_btn);

            if (TRIP_VALUE==1){
                //End.setVisibility(View.INVISIBLE);
                Start.setVisibility(View.VISIBLE);
                Cancel.setVisibility(View.VISIBLE);
            }
            else if (TRIP_VALUE ==2){
                Cancel.setText("End Trip");
                Start.setVisibility(View.GONE);
                Cancel.setVisibility(View.VISIBLE);
            }
            else if (TRIP_VALUE==3){
                Start.setVisibility(View.GONE);
                Cancel.setVisibility(View.GONE);
            }
            //itemView.setOnClickListener(this);
        }

       /* @Override
        public void onClick(View view) {
            if(mListener !=null){*//* if suppose an image is clicked *//*
                int position = getAdapterPosition();*//* position of image is obtained *//*
         *//*The below condition is VVimp condition it gives the position of image
                 only if IMAGE doesn't fade by clicking on it
                 *//*
                if(position!=RecyclerView.NO_POSITION){
                    mListener.onItemClick(position);// calling "onItemClick" method
                }
            }
        }
    }

    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    // Note : Below in click listener ,we have to pass interface we created
    public void setOnItemClickListener(OnItemClickListener listener){
        // we have to use the object of OnItemClickListener
        mListener = listener;

    }*/


    }
}
