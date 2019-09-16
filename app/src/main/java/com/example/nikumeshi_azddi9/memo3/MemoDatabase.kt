package com.example.nikumeshi_azddi9.memo3

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [MemoData::class], version = 1)
abstract class MemoDatabase : RoomDatabase(){
    abstract fun memoDao(): MemoDao

    companion object{
        @Volatile
        private var instance: MemoDatabase? = null

        fun getInstance(context: Context): MemoDatabase = instance ?: synchronized(this){
            Room.databaseBuilder(context, MemoDatabase::class.java, "memoDB.db").build()
        }
    }
}