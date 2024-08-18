package com.crbt.data.core.data.di

import android.content.Context
import com.crbt.data.core.data.repository.UssdRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object UssdModule {

    @Provides
    @ViewModelScoped
    fun provideUssdRepository(@ApplicationContext context: Context): UssdRepository {
        return UssdRepository(context)
    }
}