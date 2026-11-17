package projeto.interdiciplinar.ames.data

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ApiClient {
    const val BASE_URL = "http://127.0.0.1:3000/"

    private val cliente = OkHttpClient.Builder()
        .connectTimeout(10, TimeUnit.SECONDS)
        .readTimeout(20, TimeUnit.SECONDS)
        .writeTimeout(20, TimeUnit.SECONDS)
        .addInterceptor { chain ->
            val pedido = chain.request().newBuilder()

            Sessao.token?.let { token ->
                pedido.header("Authorization", "Bearer $token")
            }

            chain.proceed(pedido.build())
        }
        .build()

    val api: Api = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(cliente)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(Api::class.java)
}