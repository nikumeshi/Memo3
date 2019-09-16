package com.example.nikumeshi_azddi9.memo3

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.list_item_row.view.*

class MemoAdapter(context: Context,
                  private val memoList: List<MemoData>,
                  private val onItemClicked: (MemoData)->Unit) : RecyclerView.Adapter<MemoAdapter.MemoViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MemoViewHolder {
        val view = inflater.inflate(R.layout.list_item_row, parent, false)
        val vh = MemoViewHolder(view)

        view.setOnClickListener {
            val memo = memoList[vh.adapterPosition]
            onItemClicked(memo)
        }
        return vh
    }

    override fun getItemCount() = memoList.size

    override fun onBindViewHolder(holder: MemoAdapter.MemoViewHolder, position: Int) {
        holder.title.text = memoList[position].title
        holder.lastModified.text = memoList[position].time
    }

    class MemoViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val title: TextView = view.title
        val lastModified: TextView = view.lastModified
    }

    private val inflater = LayoutInflater.from(context)

}