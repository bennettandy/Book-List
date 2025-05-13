package com.avsoftware.quilterdemo.di

import com.avsoftware.quilterdemo.data.OpenLibraryBookRepository
import com.avsoftware.quilterdemo.domain.repository.BookRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface DomainModule {

    @Binds
    @Singleton
    fun provideBookRepository(bookRepository: OpenLibraryBookRepository): BookRepository
}