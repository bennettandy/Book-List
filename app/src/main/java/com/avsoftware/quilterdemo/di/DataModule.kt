package com.avsoftware.quilterdemo.di

import coil.ImageLoader
import coil.memory.MemoryCache
import com.avsoftware.quilterdemo.BuildConfig
import com.avsoftware.quilterdemo.data.api.OpenLibraryApiService
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataModule {

    companion object {
        private const val BASE_URL = "https://openlibrary.org/"
    }

    @Provides
    fun provideGson(): Gson = GsonBuilder()
        .setLenient()
        .create()

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            // Enable BODY logging only in debug builds
            level = if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor.Level.BODY
            } else {
                HttpLoggingInterceptor.Level.NONE
            }
        }

        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(gson: Gson, okHttpClient: OkHttpClient): Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient) // Attach OkHttpClient with LoggingInterceptor
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()

    @Provides
    fun provideOpenLibraryApiService(retrofit: Retrofit): OpenLibraryApiService = retrofit
                .create(OpenLibraryApiService::class.java)

    @Module
    @InstallIn(SingletonComponent::class)
    object DataModule {
        // ...

        @Provides
        @Singleton
        fun provideImageLoader(
            @ApplicationContext context: android.content.Context,
            okHttpClient: OkHttpClient
        ): ImageLoader {
            return ImageLoader.Builder(context)
                .okHttpClient(okHttpClient)
                // may be overkill but we have these caches
                .memoryCache {
                    MemoryCache.Builder(context)
                        .maxSizePercent(0.25)
                        .build()
                }
//                .diskCache {
//                    DiskCache.Builder()
//                        .directory(context.cacheDir.resolve("image_cache"))
//                        .maxSizePercent(0.02)
//                        .build()
//                }
                .crossfade(true)
                .build()
        }
    }

}