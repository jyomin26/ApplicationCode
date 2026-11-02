package com.android.applicationcode.di

import android.content.Context
import androidx.room.Room
import com.android.data.local.AppDatabase
import com.android.data.local.VehicleDao
import com.android.data.remote.VehicleApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    private const val BASE_URL = "https://carfax-for-consumers.firebaseio.com/"
    private const val DATABASE_NAME = "vehicles_db"

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Provides
    @Singleton
    fun provideUserApi(retrofit: Retrofit): VehicleApi =
        retrofit.create(VehicleApi::class.java)

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): AppDatabase =
        Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            DATABASE_NAME
        ).build()

    @Provides
    @Singleton
    fun provideUserDao(database: AppDatabase): VehicleDao =
        database.vehicleDao()
}