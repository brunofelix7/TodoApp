package com.brunofelixdev.mytodoapp.di

import android.content.Context
import androidx.room.Room
import com.brunofelixdev.mytodoapp.data.db.AppDatabase
import com.brunofelixdev.mytodoapp.data.db.DbSchema
import com.brunofelixdev.mytodoapp.data.db.ItemDao
import com.brunofelixdev.mytodoapp.data.db.ItemRepositoryImpl
import com.brunofelixdev.mytodoapp.data.db.ItemRepository
import com.brunofelixdev.mytodoapp.rv.adapter.ItemAdapter
import com.brunofelixdev.mytodoapp.util.ResourceProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase = Room.databaseBuilder(
        context.applicationContext,
        AppDatabase::class.java,
        DbSchema.DB_NAME
    ).build()

    @Singleton
    @Provides
    fun provideItemDao(db: AppDatabase): ItemDao = db.itemDao()

    @Singleton
    @Provides
    fun provideItemRepository(dao: ItemDao): ItemRepository = ItemRepositoryImpl(dao)

    @Singleton
    @Provides
    fun provideIoDispatcher(): CoroutineDispatcher = Dispatchers.IO

    @Singleton
    @Provides
    fun provideItemAdapter(): ItemAdapter = ItemAdapter()

    @Singleton
    @Provides
    fun provideResourceProvider(@ApplicationContext context: Context) = ResourceProvider(context)
}