package com.example.nikumeshi_azddi9.memo3

import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.text.format.DateFormat
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_input.*
import kotlinx.android.synthetic.main.fragment_memo_list.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.math.BigInteger
import java.nio.ByteBuffer
import java.util.*

const val DATE_FORMAT = "yyyy-MM-dd-hh-mm-ss"

class MainActivity : AppCompatActivity() {

    private var actBarDrawerToggle: ActionBarDrawerToggle? = null
    private var currentMemoId: Long = 0

    private val memoVM by lazy { ViewModelProviders.of(this).get(MemoViewModel::class.java) }

    private fun setupDrawer(drawerLayout: DrawerLayout){
        val toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.app_name, R.string.app_name).apply {
            isDrawerIndicatorEnabled = true
        }

        drawerLayout.addDrawerListener(toggle)
        actBarDrawerToggle = toggle

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeButtonEnabled(true)
        }
    }

    private fun initUuid():Long{
        var value: Long
        do {
            val buffer = ByteBuffer.wrap(ByteArray(16)).apply {
                UUID.randomUUID().also { uid ->
                    putLong(uid.leastSignificantBits)
                    putLong(uid.mostSignificantBits)
                }
            }
            value = BigInteger(buffer.array()).toLong()
        } while (value < 0)
        return value
    }



    private val btnListener = View.OnClickListener {
        when (it){
            saveBtn -> {
                GlobalScope.launch{
                    val targetTask = memoVM.getMemoAsync(currentMemoId).await()
                    memoVM.update(
                        MemoData(
                            targetTask._id,
                            targetTask.title,
                            getString(R.string.last_modified, Date()),
                            content.text.toString()
                        )
                    )
                }
            }

            delBtn -> {

                AlertDialog.Builder(this@MainActivity).apply {
                    setMessage(R.string.del_alertMessage)
                    setPositiveButton(R.string.del_yes) { _, _ ->
                        memoVM.logicalDelete(currentMemoId)
                        content.setText("")
                        delBtn.isEnabled = false
                        saveBtn.isEnabled = false
                    }
                    setNegativeButton(R.string.del_no) { _, _ ->

                    }
                }.show()
            }

            addBtn -> {
                val timestamp = DateFormat.format(DATE_FORMAT, Date()) as CharSequence
                currentMemoId = initUuid()
                memoVM.insert(
                    MemoData(
                        currentMemoId,
                        "memo-$timestamp",
                        getString(R.string.last_modified, Date()),
                        content.text.toString()
                    )
                )
                saveBtn.isEnabled = true
                delBtn.isEnabled = true
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        memoVM.allMemo.observe(this, Observer {
            val enableMemoList = mutableListOf<MemoData>()
            for (x in it){ if (x.isEnable) enableMemoList.add(x) }

            val memoAdapter = MemoAdapter(this, enableMemoList){ memo->
                currentMemoId = memo._id
                content.setText(memo.memoContents)
                drawerLayout?.closeDrawer(GravityCompat.START)
                saveBtn.isEnabled = true
                delBtn.isEnabled = true
            }
            memoList.run{
                setHasFixedSize(true)
                adapter = memoAdapter
                layoutManager = LinearLayoutManager(this@MainActivity, RecyclerView.VERTICAL, false)

            }
        })

        saveBtn.setOnClickListener(btnListener)
        addBtn.setOnClickListener(btnListener)
        delBtn.setOnClickListener(btnListener)

        val drawerLayout = drawerLayout
        drawerLayout?.let { setupDrawer(drawerLayout) }
    }

    override fun onPostCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onPostCreate(savedInstanceState, persistentState)
        actBarDrawerToggle?.syncState()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        actBarDrawerToggle?.onConfigurationChanged(newConfig)
    }

    override fun onOptionsItemSelected(item: MenuItem?) = if(actBarDrawerToggle?.onOptionsItemSelected(item) == true) true else super.onOptionsItemSelected(item)
}
