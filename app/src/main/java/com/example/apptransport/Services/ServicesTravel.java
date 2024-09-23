package com.example.apptransport.Services;

import com.apreciasoft.mobile.asremis.Entity.BeneficioEntity;
import com.apreciasoft.mobile.asremis.Entity.CalificationStar;
import com.apreciasoft.mobile.asremis.Entity.ChatMessageReceiveSaved;
import com.apreciasoft.mobile.asremis.Entity.ChatOperator;
import com.apreciasoft.mobile.asremis.Entity.ChatOperatorReceive;
import com.apreciasoft.mobile.asremis.Entity.CompanyData;
import com.apreciasoft.mobile.asremis.Entity.Driver;
import com.apreciasoft.mobile.asremis.Entity.DriverSemaforo;
import com.apreciasoft.mobile.asremis.Entity.GuardarPagoTcd;
import com.apreciasoft.mobile.asremis.Entity.InfoTravelEntity;
import com.apreciasoft.mobile.asremis.Entity.InformacionPagoCredit;
import com.apreciasoft.mobile.asremis.Entity.ItenMercadoPago;
import com.apreciasoft.mobile.asremis.Entity.Passenger;
import com.apreciasoft.mobile.asremis.Entity.PointToPointItem;
import com.apreciasoft.mobile.asremis.Entity.PointToPointMaster;
import com.apreciasoft.mobile.asremis.Entity.RemisSocketInfo;
import com.apreciasoft.mobile.asremis.Entity.ResponseFilterForm;
import com.apreciasoft.mobile.asremis.Entity.TraveInfoSendEntity;
import com.apreciasoft.mobile.asremis.Entity.TravelBodyForClientPayment;
import com.apreciasoft.mobile.asremis.Entity.TravelEntity;
import com.apreciasoft.mobile.asremis.Entity.TravelLocationEntity;
import com.apreciasoft.mobile.asremis.Entity.VehicleType;
import com.apreciasoft.mobile.asremis.Entity.paramEntity;
import com.apreciasoft.mobile.asremis.Entity.reasonEntity;
import com.apreciasoft.mobile.asremis.Entity.resp;
import com.apreciasoft.mobile.asremis.Entity.valuesTravelPreview;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by Admin on 05/01/2017.
 */

public interface ServicesTravel {

    @Headers("Content-Type: application/json")
    @GET("travel/getCurrentTravelByIdDriver/{id}")
    Call<InfoTravelEntity> getCurrentTravelByIdDriver(@Path("id") int id);

    @Headers("Content-Type: application/json")
    @GET("travel/getCurrentTravelByIdClient/{id}")
    Call<InfoTravelEntity> getCurrentTravelByIdClient(@Path("id") int id);


    @Headers("Content-Type: application/json")
    @GET("travel/getCurrentTravelByIdUserCompany/{id}")
    Call<InfoTravelEntity> getCurrentTravelByIdUserCompany(@Path("id") int id);

    @Headers("Content-Type: application/json")
    @GET("travel/getDriverMapBiIdTravel/{id}")
    Call<TravelLocationEntity> getDriverMapBiIdTravel(@Path("id") int id);


    @Headers("Content-Type: application/json")
    @POST("travel/add")
    Call<resp> addTravel(@Body TravelEntity user);

    @Headers("Content-Type: application/json")
    @POST("travel/sendPosition")
    Call<Boolean> sendPosition(@Body TraveInfoSendEntity travel);

    @Headers("Content-Type: application/json")
    @POST("travel/infoTravelByDriver")
    Call<List<InfoTravelEntity>> infoTravelByDriver(@Body TraveInfoSendEntity filter);

    @Headers("Content-Type: application/json")
    @GET("travel/isRoundTrip/{id}")
    Call<Boolean> isRoundTrip(@Path("id") int id);

    @Headers("Content-Type: application/json")
    @GET("travel/verifickTravelFinish/{id}")
    Call<Boolean> verifickTravelFinish(@Path("id") int id);

    @Headers("Content-Type: application/json")
    @GET("travel/verifickTravelCancel/{id}")
    Call<Boolean> verifickTravelCancel(@Path("id") int id);


    @Headers("Content-Type: application/json")
    @GET("travel/isWait/{id}/{value}")
    Call<Boolean> isWait(@Path("id") int id, @Path("value") int value);

    @Headers("Content-Type: application/json")
    @GET("travel/confirmCancelByClient/{id}")
    Call<Boolean> confirmCancelByClient(@Path("id") int id);

    @Headers("Content-Type: application/json")
    @GET("travel/confirmAceptByClient/{id}")
    Call<Boolean> confirmAceptaByClient(@Path("id") int id);

    @Headers("Content-Type: application/json")
    @GET("travel/calificateDriver/{idTravel}/{start}")
    Call<Boolean> calificateDriver(@Path("idTravel") int idTravel, @Path("start") int start);

    @Headers("Content-Type: application/json")
    @POST("driver/postRating/{idDriver}")
    Call<String> calificateDriver(@Path("idDriver") int idDriver, @Body CalificationStar star);


    @Headers("Content-Type: application/json")
    @GET("travel/confirmCancelByDriver/{id}")
    Call<Boolean> confirmCancelByDriver(@Path("id") int id);

    @Headers("Content-Type: application/json")
    @POST("travel/finishMobil")
    Call<InfoTravelEntity> finishPost(@Body TraveInfoSendEntity travel);

    @Headers("Content-Type: application/json")
    @POST("travel/finishMobil")
    Call<ResponseBody> finishPostTarjeta(@Body TraveInfoSendEntity travel);

    @Headers("Content-Type: application/json")
    @POST("travel/preFinishMobil")
    Call<InfoTravelEntity> preFinishMobil(@Body TraveInfoSendEntity travel);

    @Headers("Content-Type: application/json")
    @GET("travel/accept/{id}")
    Call<InfoTravelEntity> accept(@Path("id") int id);

    @Headers("Content-Type: application/json")
    @GET("travel/refuse/{id}")
    Call<InfoTravelEntity> refuse(@Path("id") int id);

    @Headers("Content-Type: application/json")
    @GET("travel/init/{id}")
    Call<InfoTravelEntity> init(@Path("id") int id);


    @Headers("Content-Type: application/json")
    @GET("travel/rservations/{idDriver}/{idUserClient}/{idProfileUser}")
    Call<List<InfoTravelEntity>> getReservations(@Path("idDriver") int idDriver, @Path("idUserClient") int idUserClient, @Path("idProfileUser") int idProfileUser);

    @Headers("Content-Type: application/json")
    @GET("travel/readrservations/{id}/{idDriver}")
    Call<List<InfoTravelEntity>> readReservation(@Path("id") int id, @Path("idDriver") int idDriver);


    @Headers("Content-Type: application/json")
    @GET("travel/cacelReservation/{id}/{idDriver}")
    Call<List<InfoTravelEntity>> cacelReservation(@Path("id") int id, @Path("idDriver") int idDriver);

    @Headers("Content-Type: application/json")
    @GET("travel/cancelByClient/{id}/{idReasonCancelKf}/{idTravel}")
    Call<Boolean> cancelByClient(@Path("id") int id, @Path("idReasonCancelKf") int idReasonCancelKf, @Path("idTravel") int idTravel);

    @Headers("Content-Type: application/json")
    @GET("travel/reasonForId/{id}")
    Call<reasonEntity> obtIdMotivo(@Path("id") int id);

    @Headers("Content-Type: application/json")
    @GET("config/param")
    Call<List<paramEntity>> getparam();

    @Headers("Content-Type: application/json")
    @POST("travel/amountStimate")
    Call<Double> amountStimate(@Body valuesTravelPreview travel);

    @Headers("Content-Type: application/json")
    @POST("travel/update")
    Call<InfoTravelEntity> asigned(@Body TravelEntity travel);

    @Headers("Content-Type: application/json")
    @POST("mppaycar/preference")
    Call<String> preferenceMercado(@Body ItenMercadoPago itenMercadoPago);

    @Headers("Content-Type: application/json")
    @POST("mppaycar/tdcpayment")
    Call<GuardarPagoTcd> saveInfoPayCredit(@Body InformacionPagoCredit informacionPagoCredit);

    @Headers("Content-Type: application/json")
    @GET("BenefitKm/kmsByIdHeader/{id}")
    Call<List<BeneficioEntity>> getListBenefitPerKm(@Path("id") int id);

    @Headers("Content-Type: application/json")
    @GET("driver/getRating/{idDriver}")
    Call<Double> getDriverRating(@Path("idDriver") int idDriver);

    @Headers("Content-Type: application/json")
    @GET("PointToPoint/index")
    Call<PointToPointMaster> getListPointToPointClient();

    @Headers("Content-Type: application/json")
    @GET("PointToPoint/index/{id}")
    Call<PointToPointMaster> getListPointToPointCompany(@Path("id") int id);

    @Headers("Content-Type: application/json")
    @GET("travel/clientCategoryPrices/id/{id}")
    Call<List<VehicleType>> getVehiclesTypeByCompanyId(@Path("id") int id);



    @Headers("Content-Type: application/json")
    @GET("travel/clientCategoryPrices/id/")
    Call<List<VehicleType>> getVehiclesTypeGeneral();

    @Headers("Content-Type: application/json")
    @GET("pasaguer/ByIdTravel/{travelId}")
    Call<List<Passenger>> getPassengerFromTravel(@Path("travelId") int travelId);

    @Headers("Content-Type: application/json")
    @POST("travel/payWithCardByClient")
    Call<InfoTravelEntity> payWithCardByClient(@Body TravelBodyForClientPayment request);

    @Headers("Content-Type: application/json")
    @POST("travel/finishPaymentProcessByClient")
    Call<InfoTravelEntity> finishPaymentProcessByClient(@Body TravelBodyForClientPayment request);

    @Headers("Content-Type: application/json")
    @GET("travel/pasangue/{companyId}")
    Call<CompanyData> getCompanyData(@Path("companyId") int companyId);

    @Headers("Content-Type: application/json")
    @POST("driver/online")
    Call<resp> setDriverOnline(@Body DriverSemaforo driver);

    @Headers("Content-Type: application/json")
    @GET("driver/chatmessages/driver/{idDriver}/user/{idOperator}/total/{cantMessages}")
    Call<ChatMessageReceiveSaved> listChatMessages(@Path("idDriver") String idDriver, @Path("cantMessages") String cantMessages, @Path("idOperator") String idOperator);

    @Headers("Content-Type: application/json")
    @GET("driver/operators")
    Call<List<ChatOperator>> listChatOperators();


}
