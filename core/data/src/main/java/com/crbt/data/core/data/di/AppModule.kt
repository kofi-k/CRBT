package com.crbt.data.core.data.di


import android.content.ContentResolver
import android.content.Context
import com.romellfudi.ussdlibrary.USSDController
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {


    @Provides
    @Singleton
    fun provideContentResolver(@ApplicationContext context: Context): ContentResolver {
        return context.contentResolver
    }

    @Provides
    @Singleton
    fun provideUssdController(
        @ApplicationContext context: Context
    ): USSDController {
        return USSDController.getInstance(context)
    }

}