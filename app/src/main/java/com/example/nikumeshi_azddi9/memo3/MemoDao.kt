package com.example.nikumeshi_azddi9.memo3

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface MemoDao{
    @Query("select * from memo")
    fun getAllMemo(): LiveData<List<MemoData>>

    @Query("select * from memo where _id = :id")
    suspend fun getMemo(id: Long): MemoData

    @Insert
    suspend fun insert(memo: MemoData)
    @Update
    suspend fun update(memo: MemoData)
    @Delete
    suspend fun delete(memo: MemoData)

    @Query("update memo set isEnable = 0 where _id = :id")
    suspend fun logicalDelete(id: Long)

    @Query("delete from memo")
    suspend fun deleteAll()

}