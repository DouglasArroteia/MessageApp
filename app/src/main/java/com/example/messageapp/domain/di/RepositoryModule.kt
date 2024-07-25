package com.example.messageapp.domain.di

import com.example.messageapp.data.repository.MessageRepositoryImpl
import com.example.messageapp.domain.repository.MessageRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindMyRepository(
        myRepositoryImpl: MessageRepositoryImpl
    ): MessageRepository
}