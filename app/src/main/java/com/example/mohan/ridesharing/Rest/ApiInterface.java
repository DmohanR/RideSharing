package com.example.mohan.ridesharing.Rest;

import com.example.mohan.ridesharing.Response.BookResponse;
import com.example.mohan.ridesharing.Response.EndTripResponse;
import com.example.mohan.ridesharing.Response.TripResponse;
import com.example.mohan.ridesharing.Response.LoginResponse;
import com.example.mohan.ridesharing.Response.UnlockResponse;
import com.example.mohan.ridesharing.Response.UpcomingTripResponse;
import com.example.mohan.ridesharing.Response.carResponse;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

    public interface ApiInterface {
//        @FormUrlEncoded
        @POST("/book")
        Call<BookResponse> sendBookRequest(@Body Map<String, String> request);

        @PUT("/endtrip")
        Call<EndTripResponse> sendTripRequest(@Body Map<String, String> request);

        @GET("/login")
        Call<LoginResponse> sendLoginDetails(@Query("userName") String userName, @Query("password") String password);

        @GET("/unlockDevice")
        Call<UnlockResponse> unlockCar(@Query("mcaddress") String mcaddress, @Query("enckey") String enckey,@Query("tripID") String tripID);

        /*@GET("/upcomingtrip")
        Call<UpcomingTripResponse> getTripDetails(@Query("userName") String userName);*/

        @GET("/userslist?flag=carData&type=vehicle&status=open")
        Call<carResponse> getPackage();

        @GET("/trips")
        Call<TripResponse> getTripDetails(@Query("userName") String userName, @Query("tripsType") String tripsType);


//        @PUT("/logistic/updateASN/")
//        Call<PackageAcceptResponse> sendAcceptance(@Body Map<String,String> accept);
    }
