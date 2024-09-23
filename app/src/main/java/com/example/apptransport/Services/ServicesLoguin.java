package com.example.apptransport.Services;

import com.apreciasoft.mobile.asremis.Entity.ClientRecoveryPass;
import com.apreciasoft.mobile.asremis.Entity.RequetClient;
import com.apreciasoft.mobile.asremis.Entity.Client;
import com.apreciasoft.mobile.asremis.Entity.ClienteFull;
import com.apreciasoft.mobile.asremis.Entity.TermAndConditionResult;
import com.apreciasoft.mobile.asremis.Entity.TermsAndConditionsUpdate;
import com.apreciasoft.mobile.asremis.Entity.login;
import com.apreciasoft.mobile.asremis.Entity.paramEntity;
import com.apreciasoft.mobile.asremis.Entity.reporte;
import com.apreciasoft.mobile.asremis.Entity.resp;
import com.apreciasoft.mobile.asremis.Entity.token;
import com.apreciasoft.mobile.asremis.Entity.userFull;
import com.apreciasoft.mobile.asremis.core.models.SocketWeb;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by Admin on 01-01-2017.
 */

public interface ServicesLoguin {

    @Headers("Content-Type: application/json")
    @POST("auth")
    Call<userFull> getUser(@Body login user);

    @Headers("Content-Type: application/json")
    @POST("support/add")
    Call<reporte> reporteFalla(@Body reporte falla);

    @Headers("Content-Type: application/json")
    @POST("auth/token")
    Call<Boolean> token(@Body token token);

    @Headers("Content-Type: application/json")
    @POST("auth/updateSocketWeb")
    Call<Boolean> updateSocketWeb(@Body token token);

    @Headers("Content-Type: application/json")
    @POST("auth/updateClientLiteMobil")
    Call<Boolean> updateClientLiteMobil(@Body ClienteFull cl);

    @Headers("Content-Type: application/json")
    @POST("auth/updateClientLiteMobil")
    Call<Client> updateClientLiteMobilParticular(@Body ClienteFull cl);

    @Headers("Content-Type: application/json")
    @GET("client/find/{id}")
    Call<RequetClient> find(@Path("id") int id);

    @Headers("Content-Type: application/json")
    @GET("client/find/{id}/{id_profile}")
    Call<RequetClient> findWithDiscriminate(@Path("id") int id, @Path("id_profile") int profile_id);

    @Headers("Content-Type: application/json")
    @GET("auth/checkVersion/{ver}")
    Call<Boolean> checkVersion(@Path("ver") String ver);

    @Headers("Content-Type: application/json")
    @POST("/auth/clearSocket")
    Call<Boolean> cleanSocketWeb(@Body SocketWeb token);

    @Headers("Content-Type: application/json")
    @POST("client/recoverPassword")
    Call<resp> postRecoveryPass_old(@Body ClientRecoveryPass clientRecoveryPass);

    @Headers("Content-Type: application/json")
    @POST("userSystem/recoverUserPassword")
    Call<resp> postRecoveryPass(@Body ClientRecoveryPass clientRecoveryPass);

    @Headers("Content-Type: application/json")
    @GET("config/param")
    Call<List<paramEntity>> getParams();

    @Headers("Content-Type: application/json")
    @POST("userSystem/acceptTerms")
    Call<resp> updateTermsAndConditions(@Body TermsAndConditionsUpdate termsAndConditionsUpdate);
}
