package com.aykuttasil.sweetloc.di.modules

import android.content.Context
import com.aykuttasil.sweetloc.BuildConfig
import com.aykuttasil.sweetloc.di.ApplicationContext
import com.facebook.stetho.okhttp3.StethoInterceptor
import com.orhanobut.logger.Logger
import com.readystatesoftware.chuck.ChuckInterceptor
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

/**
 * Created by aykutasil on 9.12.2017.
 */

@Module(includes = arrayOf(ApiModule::class))
class NetworkModule {

    protected fun getBaseUrl() = "http://api.xyz.com"

    @Provides
    @Singleton
    internal fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
                .baseUrl(getBaseUrl())
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()

    }

    @Provides
    @Singleton
    internal fun provideOkHttpClient(httpLoggingInterceptor: HttpLoggingInterceptor,
                                     chuckInterceptor: ChuckInterceptor,
                                     stethoInterceptor: StethoInterceptor): OkHttpClient {
        val httpClientBuilder = OkHttpClient.Builder()
        if (BuildConfig.DEBUG) {
            httpClientBuilder.addInterceptor(httpLoggingInterceptor)
            httpClientBuilder.addInterceptor(chuckInterceptor)
            httpClientBuilder.addNetworkInterceptor(stethoInterceptor)
        }
        return httpClientBuilder.build()
    }

    @Provides
    @Singleton
    internal fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor { message ->
            Logger.d(message)
        }.setLevel(HttpLoggingInterceptor.Level.BODY)
    }

    @Provides
    @Singleton
    internal fun provideChuckInterceptor(@ApplicationContext context: Context): ChuckInterceptor {
        return ChuckInterceptor(context)
    }

    @Provides
    @Singleton
    internal fun provideStetho(): StethoInterceptor {
        return StethoInterceptor()
    }


    /*
    @Provides
    @Singleton
    internal fun provideMoshi(): Moshi {
        return Moshi
                .Builder()
                .add(KotlinJsonAdapterFactory())
                .build()

    }
    */

}