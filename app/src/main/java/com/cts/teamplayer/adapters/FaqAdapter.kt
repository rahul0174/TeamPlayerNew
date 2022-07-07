package com.cts.teamplayer.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cts.teamplayer.R
import com.cts.teamplayer.models.CountryList
import com.cts.teamplayer.models.DataItem
import kotlinx.android.synthetic.main.adapter_faq.view.*

import kotlinx.android.synthetic.main.country_list.view.*
import kotlinx.android.synthetic.main.fragment_faq.*

class FaqAdapter (val mContext: Context, var data : java.util.ArrayList<DataItem>?/*, var listener:TextBookNow*/) :
    androidx.recyclerview.widget.RecyclerView.Adapter<FaqAdapter.MyHolderView>(){
    lateinit var countryFilterList : ArrayList<CountryList>
  var currentPosition=-1
    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): MyHolderView {
        val v = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.adapter_faq, viewGroup, false)
        return MyHolderView(v)
    }
    override fun onBindViewHolder(myHolderView: MyHolderView, position: Int) {
        myHolderView.bind(data!!.get(position),position)
    }

    override fun getItemCount(): Int {
        return data!!.size
    }

    inner class MyHolderView(itemView: View) :
        androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {

        @SuppressLint("NotifyDataSetChanged")
        fun bind(data: DataItem, position: Int) {
            itemView.tv_question_one_adapter.text = data.question
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                itemView.tv_reach_out_ansswer_adapter.setText(
                    Html.fromHtml(data.answer,
                        Html.FROM_HTML_MODE_LEGACY));
            } else {
                itemView.tv_reach_out_ansswer_adapter.setText(Html.fromHtml(data.answer))
            }
            itemView.tv_reach_out_ansswer_adapter.visibility = View.GONE
            if (currentPosition == position) {
                itemView.tv_reach_out_ansswer_adapter.visibility = View.VISIBLE
                itemView.iv_minus_reach_out.visibility = View.GONE
                itemView.iv_plus_what_group.visibility = View.VISIBLE
            } else {
                itemView.tv_reach_out_ansswer_adapter.visibility = View.GONE
                itemView.iv_minus_reach_out.visibility = View.VISIBLE
                itemView.iv_plus_what_group.visibility = View.GONE
            }
            itemView.iv_minus_reach_out.setOnClickListener {
                currentPosition = position
                notifyDataSetChanged();
            }
            itemView.iv_plus_what_group.setOnClickListener {
                currentPosition = position
                itemView.tv_reach_out_ansswer_adapter.visibility = View.GONE
                itemView.iv_minus_reach_out.visibility = View.VISIBLE
                itemView.iv_plus_what_group.visibility = View.GONE
                notifyDataSetChanged();
            }
        }

    }

}