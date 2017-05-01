package com.tmaprojects.tarekkma.tedxainshamstickets;

import com.tmaprojects.tarekkma.tedxainshamstickets.model.Attender;
import com.tmaprojects.tarekkma.tedxainshamstickets.model.Status;

import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by tarekkma on 4/22/17.
 */

public class WebAPI {
    private static final String API_URL = BuildConfig.API_URL;

    private static OkHttpClient.Builder httpClient = new OkHttpClient.Builder();


    private static Retrofit.Builder builder =
            new Retrofit.Builder()
                    .baseUrl(API_URL)
                    .addConverterFactory(GsonConverterFactory.create());

    public static ApiCalls createService() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        // set your desired log level
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        httpClient.addInterceptor(logging);
        Retrofit retrofit = builder.client(httpClient.build()).build();
        return retrofit.create(ApiCalls.class);
    }

    public interface ApiCalls {
        @GET("tickets/get.php")
        Call<Attender> getTicket(@Query("id")int id);

        @GET("tickets/checkAll.php")
        Call<List<Attender>> checkTickets(@Query("ids[]")int... ids);

        @GET("tickets/mark.php")
        Call<Status> markTicket(@Query("id") int id);

        @GET("tickets/unmark.php")
        Call<Status> unmarkTicket(@Query("id") int id);
    }
}
