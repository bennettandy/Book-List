package com.avsoftware.quilterdemo.di

import coil.ImageLoader
import coil.disk.DiskCache
import coil.memory.MemoryCache
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import javax.inject.Singleton

    @Module
    @InstallIn(SingletonComponent::class)
    object AppModule {
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
                .diskCache {
                    DiskCache.Builder()
                        .directory(context.cacheDir.resolve("image_cache"))
                        .maxSizePercent(0.02)
                        .build()
                }
                .crossfade(true)
                .build()
        }
    }