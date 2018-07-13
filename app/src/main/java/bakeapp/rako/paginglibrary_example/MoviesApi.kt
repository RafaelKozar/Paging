package bakeapp.rako.paginglibrary_example

import android.util.Log
import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Created by rako on 13/07/2018.
 */
interface MoviesApi {

    @GET("3/movie/{search}") //?api_key=$KEY&language=pt-BR") //add parameter page
    fun getMovies(@Path("search") searchMovies: String,
                   @Query("api_key") api_key : String,
                   @Query("language") language : String,
                   @Query("page") page : String?): Call<Movies>

    companion object {
        fun create(url : String = "https://api.themoviedb.org/") : MoviesApi{
            /*val logger = HttpLoggingInterceptor(HttpLoggingInterceptor.Logger {
                Log.d("API", it)
            })*/
            val gson = GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create()
            val client = OkHttpClient.Builder()
                    .build()

            return  Retrofit.Builder().baseUrl(url)
//                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build()
                    .create(MoviesApi::class.java)
        }
    }
}