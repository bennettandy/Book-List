package com.avsoftware.data

import com.avsoftware.data.api.OpenLibraryApiService
import com.avsoftware.domain.repository.BookRepository
import com.avsoftware.domain.usecase.GetBooksUseCase
import com.avsoftware.domain.usecase.impl.GetBooksUseCaseImpl
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.reactivex.android.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    private const val BASE_URL = "https://openlibrary.org/"

    @Provides
    fun provideGetBooksUseCase(getBooksUseCaseImpl: GetBooksUseCaseImpl): GetBooksUseCase =
        getBooksUseCaseImpl

    @Provides
    fun provideBookRepository(bookRepository: OpenLibraryBookRepository): BookRepository =
        bookRepository

    @Provides
    fun provideGetBooksApiService(retrofit: Retrofit): OpenLibraryApiService =
        retrofit.create(OpenLibraryApiService::class.java)

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

}