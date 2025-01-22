package com.example.digital_flake.network

import com.example.digital_flake.api.ApiService
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

import javax.net.ssl.SSLContext
import javax.net.ssl.X509TrustManager
import javax.net.ssl.TrustManager
import java.security.cert.X509Certificate

object RetrofitInstance {
    private const val BASE_URL = "https://demo0413095.mockable.io/"

    fun create(): ApiService {
        // Create a TrustManager that accepts all certificates
        val trustAllCertificates = arrayOf<X509Certificate>()
        val trustManager = object : X509TrustManager {
            override fun getAcceptedIssuers(): Array<X509Certificate> = trustAllCertificates
            override fun checkClientTrusted(certificates: Array<out X509Certificate>?, authType: String?) {}
            override fun checkServerTrusted(certificates: Array<out X509Certificate>?, authType: String?) {}
        }

        // Set up SSLContext with TrustManager that accepts all certificates
        val sslContext = SSLContext.getInstance("TLS")
        sslContext.init(null, arrayOf<TrustManager>(trustManager), java.security.SecureRandom())
        val sslSocketFactory = sslContext.socketFactory

        // Set up OkHttpClient with SSL bypass and logging
        val okHttpClient = OkHttpClient.Builder()
            .sslSocketFactory(sslSocketFactory, trustManager)
            .hostnameVerifier { _, _ -> true }  // Disable hostname verification
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .build()

        // Build Retrofit instance
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)  // Use the custom OkHttpClient
            .build()

        return retrofit.create(ApiService::class.java)
    }
}