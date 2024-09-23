package com.example.apptransport.PlaceToPay;

import com.apreciasoft.mobile.asremis.Entity.InfoTravelEntity;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface PlaceToPayRetrofitInterface {

    @Headers("Content-Type: application/json")
    @POST("api/session")
    Call<PlaceToPayPaymentResult> getPaymentRequest(@Body PlaceToPayPaymentRequest paymentRequest);

    @Headers("Content-Type: application/json")
    @POST("api/session/{request_id}")
    Call<PlaceToPayPaymentResult> getPaymentInformation(@Path("request_id")  int request_id, @Body PlaceToPayPaymentRequest authInformation);

    @Headers("Content-Type: application/json")
    @POST("api/reverse")
    Call<PlaceToPayPaymentResult> reversePayment(@Body PlaceToPayPaymentRequest reversePaymentInformation);

    @Headers("Content-Type: application/json")
    @POST("api/session/{request_id}")
    Call<PlaceToPayPaymentResult> getStatusPayment(@Path("request_id")  int request_id, @Body PlaceToPayPaymentRequest paymentRequest);

}
