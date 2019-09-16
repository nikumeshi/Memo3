package com.example.nikumeshi_azddi9.memo3

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable
import java.math.BigInteger
import java.nio.ByteBuffer
import java.util.*

@Entity(tableName = "memo")
data class MemoData(
    @PrimaryKey
    var _id: Long,
    @ColumnInfo(name = "title")
    var title: String,
    @ColumnInfo(name = "time")
    var time: String,
    @ColumnInfo(name = "contents")
    var memoContents: String,
    @ColumnInfo(name = "isEnable")
    var isEnable: Boolean = true

) : Serializable