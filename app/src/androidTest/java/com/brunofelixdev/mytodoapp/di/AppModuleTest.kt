package com.brunofelixdev.mytodoapp.di

import android.content.Context
import androidx.room.Room
import com.brunofelixdev.mytodoapp.data.db.AppDatabase
import com.brunofelixdev.mytodoapp.rv.adapter.ItemAdapter
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Named

@Module
@InstallIn(SingletonComponent::class)
object AppModuleTest {

    @Provides
    @Named("test_db")
    fun provideInMemoryDb(@ApplicationContext context: Context) =
        Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()

    @Provides
    @Named("test_adapter")
    fun provideItemAdapter(): ItemAdapter = ItemAdapter()
}