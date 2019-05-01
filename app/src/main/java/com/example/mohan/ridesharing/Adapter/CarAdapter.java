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

//import com.bumptech.glide.Glide;
import com.example.mohan.ridesharing.Activity.ShowCars;
import com.example.mohan.ridesharing.Activity.YourTrips;
import com.example.mohan.ridesharing.Pojo.Car;
import com.example.mohan.ridesharing.Request.BookRequest;
import com.example.mohan.ridesharing.Response.BookResponse;
import com.example.mohan.ridesharing.Rest.ApiClient;
import com.example.mohan.ridesharing.Rest.ApiInterface;
import com.example.mohan.ridesharing.Utils.PrefUtils;
import com.example.mohan.ridesharing.R;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CarAdapter extends RecyclerView.Adapter<CarAdapter.ImageViewHolder> {
    private Context mContext;
    private List<Car> cars_list;
    public String from,to,vin;
    ApiInterface apiServices;
    //private OnItemClickListener mListener;
    public CarAdapter(Context context,String from,String to,List<Car> cars_list){
        this.mContext = context;
        this.from = from;
        this.to = to;
        this.cars_list = cars_list;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.car_item,viewGroup,false);// Inflator points towards the XML layout
        return new ImageViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder imageViewHolder, int i) {
        final Car car_list = cars_list.get(i);
        vin = car_list.getVin();
        /*Glide.with(mContext)
                .load(car_list.getImage_url())
                .placeholder(R.drawable.carimg)
                .into(imageViewHolder.carImageV);*/
        Picasso.get()
                .load(car_list.getImage_url())
                .placeholder(R.mipmap.ic_launcher) // place holder is like a hint(if image is not loaded it shows image at R.mipmap.ic_launcher )
//                .fitCenter()
                // or can use centerInside instead of crop
                .into(imageViewHolder.carImageV);
        imageViewHolder.title.setText(cars_list.get(i).getCar_title());
        imageViewHolder.No_seat.setText(cars_list.get(i).getNo_seats()+" Seater");
        imageViewHolder.Vid.setText("VehicleNo: "+vin);
        imageViewHolder.price.setText(cars_list.get(i).getPrice());
        imageViewHolder.price100.setText(cars_list.get(i).getPrice100());
        imageViewHolder.mBook_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                apiCall();
            }
        });
    }

    private void apiCall() {
        String user_name = PrefUtils.getFromPrefs(mContext,PrefUtils.user_email,"");
        final String mcaddress = PrefUtils.getFromPrefs(mContext,PrefUtils.mc_address,"");
        Log.d("adapter",user_name+":"+from+":"+to +"VIN : "+vin);
        BookRequest bookRequest = new BookRequest();
        bookRequest.setVin(vin);
        bookRequest.setFrom(from);
        bookRequest.setTo(to);
        bookRequest.setUser(user_name);
        bookRequest.setMcaddress(mcaddress);
        Gson gson = new Gson();
        String json = gson.toJson(bookRequest);
        Map<String,String> map = new HashMap<String,String>();
        map = gson.fromJson(json, map.getClass());
        apiServices = ApiClient.getClient().create(ApiInterface.class);
        Call<BookResponse> call = apiServices.sendBookRequest(map);
        call.enqueue(new Callback<BookResponse>() {
            @Override
            public void onResponse(Call<BookResponse> call, Response<BookResponse> response) {
                Log.d("Booked",response.body().getMessage());
                if(response.body().getStatus().equalsIgnoreCase("fail"))
                {
                    new AlertDialog.Builder(mContext)
                            .setTitle("Booking Details")
                            .setMessage(response.body().getMessage())
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setNeutralButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    if(mContext instanceof ShowCars)
                                    {
                                        ((ShowCars) mContext).callCarApi();
                                    }
                                }
                            }).show();
                }
                else
                {
                    new AlertDialog.Builder(mContext)
                            .setTitle("Booking Details")
                            .setMessage(response.body().getMessage())
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setNeutralButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Intent in = new Intent(mContext,YourTrips.class);
                                    in.putExtra("trip",1);
                                    mContext.startActivity(in);
                                }
                            }).show();
                }

            }

            @Override
            public void onFailure(Call<BookResponse> call, Throwable t) {
                Log.d("Failed", "Fail");
            }
        });

    }

    @Override
    public int getItemCount() {
        return cars_list.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder {

        public TextView title,price,No_seat,Vid,price100;
        public ImageView carImageV;
        public Button mBook_btn;

        public ImageViewHolder(View itemView) {
            super(itemView);
            carImageV = (ImageView) itemView.findViewById(R.id.car_image);
            title = (TextView) itemView.findViewById(R.id.car_name);
            price = (TextView) itemView.findViewById(R.id.price50);
            No_seat = (TextView) itemView.findViewById(R.id.no_seat);
            Vid = (TextView) itemView.findViewById(R.id.v_id);
            mBook_btn = (Button) itemView.findViewById(R.id.book_btn);
            price100 = (TextView) itemView.findViewById(R.id.price100);
        }
    }
}
