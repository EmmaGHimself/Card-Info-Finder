package com.demo.restapi

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import com.demo.restapi.models.CardInfoDetails
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class CardInfoFinderCloud private constructor(var context: Context) {
    /**
     * Get card details.
     *
     * @param bin      Bank Identification Number.
     * @param callback Retrofit Callback.
     */
    fun getCardInfoDetails(bin: String?, callback: Callback<CardInfoDetails?>?) {
        val asyncFetch = apiService.getCardInfoDetails(bin)
        asyncFetch!!.enqueue(callback)
    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        private var instance: CardInfoFinderCloud? = null
        private lateinit var apiService: CardInfoFinderAPI
        fun hasNetwork(context: Context): Boolean {
            var isConnected = false // Initial Value
            val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetwork = connectivityManager.activeNetworkInfo
            if (activeNetwork != null && activeNetwork.isConnectedOrConnecting) isConnected = true
            return isConnected
        }

        /**
         * Singleton Get instance method
         *
         * @param context Application Context.
         * @return CardInfoFinderCloud Instance.
         */
        fun getInstance(context: Context): CardInfoFinderCloud? {
            if (instance == null) {
                synchronized(CardInfoFinderCloud::class.java) {
                    if (instance == null) {
                        instance = CardInfoFinderCloud(context)
                    }
                }
            }
            return instance
        }
    }

    /**
     * Private CardInfoFinderCloud constructor.
     *
     * @param context Application Context.
     */
    init {
        val okHttpClient = OkHttpClient.Builder()
                .cache(Cache(context.cacheDir, 10 * 1024 * 1024))
                .addInterceptor { chain: Interceptor.Chain ->
                    var request = chain.request()
                    request = if (hasNetwork(context)) request.newBuilder().header("Cache-Control", "public, max-age=" + 60).build() else request.newBuilder().header("Cache-Control", "public, only-if-cached, max-stale=" + 60 * 60 * 24 * 7).build()
                    chain.proceed(request)
                }
                .build()
        val httpClient = OkHttpClient.Builder()
        httpClient.readTimeout(60, TimeUnit.SECONDS)
        // httpClient.writeTimeout(60, TimeUnit.SECONDS);
        if (BuildConfig.DEBUG) {
            // Intercept and Log Response Body in Debug Mode.
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY
            httpClient.addInterceptor(interceptor)
        }
        val retrofitBuilder = Retrofit.Builder()
        retrofitBuilder.addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
        apiService = retrofitBuilder.baseUrl(CardInfoFinderCloudConfig.instance!!.baseUrl).build().create(CardInfoFinderAPI::class.java)
    }
}