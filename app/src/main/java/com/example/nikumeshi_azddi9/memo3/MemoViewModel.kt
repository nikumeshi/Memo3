package com.example.nikumeshi_azddi9.memo3

import android.app.Application
import android.content.Intent
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import kotlinx.coroutines.*
import kotlin.concurrent.thread

class MemoViewModel (application: Application) : AndroidViewModel(application){
    private val db = MemoDatabase.getInstance(application).memoDao()
    private val _allMemo = db.getAllMemo()
    val allMemo = _allMemo

    fun insert(memo: MemoData) = GlobalScope.launch { db.insert(memo) }
    fun update(memo: MemoData) = GlobalScope.launch { db.update(memo) }
    fun logicalDelete(id: Long) = GlobalScope.launch { db.logicalDelete(id) }
    fun getMemoAsync(id: Long): Deferred<MemoData> = GlobalScope.async { db.getMemo(id) }
}