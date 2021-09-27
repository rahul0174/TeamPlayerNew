package com.cts.teamplayer.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.fragment.app.Fragment
import com.cts.teamplayer.R
import com.cts.teamplayer.fragments.QuestionnaireCalculator
import com.cts.teamplayer.models.AnswersItemNew
import com.cts.teamplayer.models.CountryList
import com.cts.teamplayer.network.ItemClickListner
import com.cts.teamplayer.util.MyConstants.QUESTION_WITH_ANSWER
import com.cts.teamplayer.util.TeamPlayerSharedPrefrence
import kotlinx.android.synthetic.main.adapter_answer.view.*
import kotlinx.android.synthetic.main.country_list.view.*


class AnswerAdapter(
    val mContext: Context,
    var data: java.util.ArrayList<AnswersItemNew>?,
    var itemclick: ItemClickListner,
    var mian_answer: Int,
    var questionnaireCalculator: QuestionnaireCalculator,

) :
    androidx.recyclerview.widget.RecyclerView.Adapter<AnswerAdapter.MyHolderView>(){
      var answerFilterList : ArrayList<AnswersItemNew> = ArrayList()
   var mCheckedPosition:Int=0
    private var mpref: TeamPlayerSharedPrefrence? = null
    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): MyHolderView {
        val v = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.adapter_answer, viewGroup, false)
        return MyHolderView(v)
    }
    override fun onBindViewHolder(myHolderView: MyHolderView, position: Int) {
        myHolderView.bind(data!!.get(position), position)
    }

    override fun getItemCount(): Int {
        return data!!.size
    }

    inner class MyHolderView(itemView: View) :
        androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {

        fun bind(data: AnswersItemNew, position: Int) {

            mpref = TeamPlayerSharedPrefrence.getInstance(mContext)
            itemView.tv_answer.text=data.answer
            if(mian_answer>1){

              itemView.radiobtn_answer_select.visibility=View.GONE
              itemView.checkbox_answer_select.visibility=View.VISIBLE
            }else{
                itemView.radiobtn_answer_select.visibility=View.VISIBLE
                itemView.checkbox_answer_select.visibility=View.GONE
            }


            //Click on chackbox
            itemView.checkbox_answer_select.setOnClickListener{
                if (itemView.checkbox_answer_select.isChecked) {

                    answerFilterList.add(data)
                    questionnaireCalculator.getanswerSelect(answerFilterList)
                    itemView.checkbox_answer_select.isChecked=true
               //     itemclick.onClickItem(position, QUESTION_WITH_ANSWER)
                }else{
                    answerFilterList.remove(data)
                    questionnaireCalculator.getanswerSelect(answerFilterList)
                    itemView.checkbox_answer_select.isChecked=false
                }

                //    listener.bookSession(position,data)
            }


            itemView.radiobtn_answer_select.setOnCheckedChangeListener(null)
            itemView.radiobtn_answer_select.setChecked(position === mCheckedPosition)
            itemView.radiobtn_answer_select.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener {
                    buttonView, isChecked ->
                mCheckedPosition = position

                notifyDataSetChanged()
                itemclick.onClickItem(position, QUESTION_WITH_ANSWER)
            })
        }

    }

    interface TextBookNow{
        fun bookSession(position: Int, data: CountryList)
    }



}