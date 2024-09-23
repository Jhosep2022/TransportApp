package com.example.apptransport.Services;

import com.apreciasoft.mobile.asremis.Entity.Company;
import com.apreciasoft.mobile.asremis.Entity.DriverCurrentAcountEntity;
import com.apreciasoft.mobile.asremis.Entity.DriverFull;
import com.apreciasoft.mobile.asremis.Entity.InfoTravelEntity;
import com.apreciasoft.mobile.asremis.Entity.PointToPointItem;
import com.apreciasoft.mobile.asremis.Entity.RequetClient;
import com.apreciasoft.mobile.asremis.Entity.TermAndConditionResult;
import com.apreciasoft.mobile.asremis.Entity.acountCompany;
import com.apreciasoft.mobile.asremis.Entity.costCenterCompany;
import com.apreciasoft.mobile.asremis.Entity.dataAddPlusDriverEntity;
import com.apreciasoft.mobile.asremis.Entity.Driver;
import com.apreciasoft.mobile.asremis.Entity.fleetType;
import com.apreciasoft.mobile.asremis.Entity.modelEntity;
import com.apreciasoft.mobile.asremis.Entity.resp;
import com.apreciasoft.mobile.asremis.Entity.responseFilterVehicle;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by Admin on 19/1/2017.
 */

public interface ServicesDriver {

    @Headers("Content-Type: application/json")
    @GET("driver/getAllTravel/{id}")
    Call<List<InfoTravelEntity>> getAllTravel(@Path("id") int id);

    @Headers("Content-Type: application/json")
    @GET("travel/travelsByIdUser/{id}")
    Call<List<InfoTravelEntity>> getAllTravelClient(@Path("id") int id);

    @Headers("Content-Type: application/json")
    @GET("driver/inactive/{id}")
    Call<Boolean> inactive(@Path("id") int id);

    @Headers("Content-Type: application/json")
    @GET("driver/active/{id}")
    Call<Boolean> active(@Path("id") int id);

    @Headers("Content-Type: application/json")
    @GET("invoice/listLiquidationDriver/{id}")
    Call<DriverCurrentAcountEntity> listLiquidationDriver(@Path("id") int id);

    @Headers("Content-Type: application/json")
    @POST("driver/updateLiteMobil")
    Call<Driver> updateLiteMobil(@Body Driver dr);


    @Headers("Content-Type: application/json")
    @GET("driver/find/{id}")
    Call<DriverFull> find(@Path("id") int id);

    @Headers("Content-Type: application/json")
    @GET("Brand")
    Call<List<modelEntity>> filterForm();

    @Headers("Content-Type: application/json")
    @GET("fleetType")
    Call<List<fleetType>> filterFormfleetType();


    @Headers("Content-Type: application/json")
    @GET("model/byidBrand/{id}")
    Call<responseFilterVehicle> getModelDetail(@Path("id") int id);

    @Headers("Content-Type: application/json")
    @POST("driver/plusLite")
    Call<String> addPluDriver(@Body dataAddPlusDriverEntity data);

    @Headers("Content-Type: application/json")
    @POST("client")
    Call<resp> addClient(@Body RequetClient data);

    @Headers("Content-Type: application/json")
    @GET("company/validatorDomaint/{mail}")
    Call<List<Company>> validatorDomaint(@Path("mail") String mail);

    @Headers("Content-Type: application/json")
    @GET("company/costCenterByidAcount/{id}")
    Call<List<costCenterCompany>> costCenterByidAcount(@Path("id") int id);

    @Headers("Content-Type: application/json")
    @GET("company/getAcountByidCompany/{id}")
    Call<List<acountCompany>> getAcountByidCompany(@Path("id") int id);

    @Headers("Content-Type: application/json")
    @GET("driver/findImage/{id}")
    Call<String> getImageDriver(@Path("id") int id);

    @Headers("Content-Type: application/json")
    @GET("client/findImage/{id}")
    Call<String> getImageUser(@Path("id") int id);

    @Headers("Content-Type: application/json")
    @GET("document/find/id/{id}")
    Call<TermAndConditionResult> getTermsAndConditions(@Path("id") int id);

}
