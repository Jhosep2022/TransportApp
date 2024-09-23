package com.example.apptransport.Http;


import android.os.Build;

import com.apreciasoft.mobile.asremis.BuildConfig;
import com.apreciasoft.mobile.asremis.Entity.InfoTravelEntity;
import com.apreciasoft.mobile.asremis.R;
import com.apreciasoft.mobile.asremis.Util.GlovalVar;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;

import okhttp3.ConnectionSpec;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Admin on 01-01-2017.
 */

public class HttpConexion {

    private static SSLSocketFactory ssl_socket_factory=null;
    public static String base;
    /* public static String ip = "192.168.0.5";*/
    public static String ip = BuildConfig.URL_SERVER;

    public  static String instance = BuildConfig.INSTANCE;// "developer";
    public static int portWsWeb = Integer.parseInt(BuildConfig.PORT);// 8085;
    public static int portWsCliente = 3000;
    public  static  String PROTOCOL = "https";
    public  static  String COUNTRY = BuildConfig.COUNTRY;// "ARG";


    public static final String BASE_URL = PROTOCOL+"://"+ip+"/";

    //public static final String BASE_URL = PROTOCOL+"://"+ip+":8888/";

    public static Retrofit retrofit = null;

    public  static void  setBase(String folder)
        {
            HttpConexion.base =  folder;
        }

    private static String getUrlBase()
    {
        String result =BASE_URL+base+"/Api/index.php/";
        try {
            switch (base) {
                case "araucomobility":
                case "RemisHorizonte":
                    result = BASE_URL  + "api/";
                    break;
                default:
                    result = BASE_URL + base + "/Api/index.php/";
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        return result;
    }

    public static String getUrlBaseForVehiclesImages()
    {
        String result =BASE_URL;
        try {
            if (isUrlMinimal()) {
                result = BASE_URL;
            } else {
                result = BASE_URL.concat(HttpConexion.base).concat("/Frond/");
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        return result;
    }

    private static boolean isUrlMinimal()
    {
        boolean result=false;
        try {
            switch (base) {
                case "araucomobility":
                case "RemisHorizonte":
                    result = true;
                    break;
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        return result;
    }

    public static Retrofit getUri() {

        // Check if we're running on Android 5.0 or higher
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP_MR1) {
            return getUri_WithLocalCertificate();
        }


        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        //getTrustManager();
       /* GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setLenient();
        Gson gson = gsonBuilder.create();*/


        final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(120, TimeUnit.SECONDS)
                .writeTimeout(120, TimeUnit.SECONDS)
                .readTimeout(120, TimeUnit.SECONDS)
                .addInterceptor(interceptor)

                .build();

        //TODO: TENGO QUE HACER EL CASE PARA ARAUCO Y EL RESTO

            retrofit = new Retrofit.Builder()
                        .baseUrl(getUrlBase())
                        //.addConverterFactory(new ToStringConverterFactory())
                        //.addConverterFactory(GsonConverterFactory.create(gson))
                        .addConverterFactory(GsonConverterFactory.create())
                        .client(okHttpClient)
                        .build();

            return retrofit;
    }

    public static Retrofit getUri_WithLocalCertificate() {

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        SSLSocketFactory socketFactory = getTrustManager();
        if(socketFactory!=null)
        {
            final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .connectTimeout(120, TimeUnit.SECONDS)
                    .writeTimeout(120, TimeUnit.SECONDS)
                    .readTimeout(120, TimeUnit.SECONDS)
                    .addInterceptor(interceptor)
                    .sslSocketFactory(socketFactory)
                    .build();

        /*    GsonBuilder gsonBuilder = new GsonBuilder();
            gsonBuilder.setLenient();
            Gson gson = gsonBuilder.create();*/

            //TODO: ESTO ESTA REPETIDO; VER CUAL USAR
            String baseUrl =BASE_URL+base+"/Api/index.php/";

            retrofit = new Retrofit.Builder()
                    .baseUrl(getUrlBase())
                    //.addConverterFactory(new ToStringConverterFactory())
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(okHttpClient)
                    .build();
        }


        return retrofit;
    }

    private static SSLSocketFactory getTrustManager()
    {
        if(ssl_socket_factory != null)
        {
            return ssl_socket_factory;
        }

        try{

            InputStream caInput = GlovalVar.getContextStatic().getResources().openRawResource(GlovalVar.getContextStatic().getResources().getIdentifier("as_nube_com",
                    "raw",
                    GlovalVar.getContextStatic().getPackageName()));

            // Load CAs from an InputStream
            // (could be from a resource or ByteArrayInputStream or ...)
            CertificateFactory cf = CertificateFactory.getInstance("X.509");

            Certificate ca;
            try {
                ca = cf.generateCertificate(caInput);
                System.out.println("ca=" + ((X509Certificate) ca).getSubjectDN());
            } finally {
                caInput.close();
            }

            // Create a KeyStore containing our trusted CAs
            String keyStoreType = KeyStore.getDefaultType();
            KeyStore keyStore = KeyStore.getInstance(keyStoreType);
            keyStore.load(null, null);
            keyStore.setCertificateEntry("ca", ca);

            // Create a TrustManager that trusts the CAs in our KeyStore
            String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
            TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
            tmf.init(keyStore);


            // Create an SSLContext that uses our TrustManager
            SSLContext sslcontext = SSLContext.getInstance("TLS");
            sslcontext.init(null, tmf.getTrustManagers(), null);

            ssl_socket_factory= sslcontext.getSocketFactory();
            return ssl_socket_factory;
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            return null;
        }


    }

    public static class ToStringConverterFactory extends Converter.Factory {
        private  final MediaType MEDIA_TYPE = MediaType.parse("text/plain");


        @Override
        public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations,
                                                                Retrofit retrofit) {
            if (InfoTravelEntity.class.equals(type)) {
                return new Converter<ResponseBody, String>() {
                    @Override
                    public String convert(ResponseBody value) throws IOException
                    {
                        int index = value.toString().indexOf("{");
                        String temp =value.string().substring(index);
                        return temp;
                    }
                };
            }
            return null;
        }

        @Override
        public Converter<?, RequestBody> requestBodyConverter(Type type, Annotation[] parameterAnnotations,
                                                              Annotation[] methodAnnotations, Retrofit retrofit) {

            if (String.class.equals(type)) {
                return new Converter<String, RequestBody>() {
                    @Override
                    public RequestBody convert(String value) throws IOException {
                        return RequestBody.create(MEDIA_TYPE, value);
                    }
                };
            }
            return null;
        }
    }


}
