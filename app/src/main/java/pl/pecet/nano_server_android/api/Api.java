package pl.pecet.nano_server_android.api;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface Api {

    @GET("/test_get/{name}/file")
    @Headers("User-Agent: mobile")
    Call<JsonObject> getTest(
            @Header("custom-header") String header,
            @Path("name") String name,
            @Query("q") String q,
            @Query("d") Integer d
    );

    @POST("/test_post/{name}/file")
    @Headers("User-Agent: mobile")
    Call<JsonObject> postTest(
            @Header("custom-header") String header,
            @Path("name") String name,
            @Query("q") String q,
            @Query("d") Integer d,
            @Body JsonElement body
    );
}
