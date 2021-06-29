package com.cts.teamplayer.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.cts.teamplayer.R
import com.cts.teamplayer.util.TeamPlayerSharedPrefrence
import kotlinx.android.synthetic.main.fragment_faq.*
import kotlinx.android.synthetic.main.fragment_faq.view.*

class FaqsFragment: Fragment(), View.OnClickListener {
    lateinit var v: View
    private var mpref: TeamPlayerSharedPrefrence? = null
    lateinit var iv_no_data_cancel_order: ImageView
    var reach_out = 0
    var what_group = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_faq, container, false)
        mpref = TeamPlayerSharedPrefrence.getInstance(activity!!)
     finid()
        return v
    }

    private fun finid() {
        v.iv_minus_reach_out.setOnClickListener(this)
        v.iv_plus_what_group.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.iv_minus_reach_out -> {
                if(reach_out==0){
                    v.iv_minus_reach_out.setImageResource(R.drawable.plus)
                   tv_reach_out.visibility=View.GONE
                    reach_out=1
                }else{
                    v.iv_minus_reach_out.setImageResource(R.drawable.minus)
                    tv_reach_out.visibility=View.GONE
                    reach_out=0

                }

            }
            R.id.iv_plus_what_group -> {
                if(what_group==0){
                    iv_minus_reach_out.setImageResource(R.drawable.minus)
                    what_group=1
               //    v.tv_reach_out.visibility=View.GONE
                }else{
                    iv_minus_reach_out.setImageResource(R.drawable.plus)
                    what_group=0
                  //  v.tv_reach_out.visibility=View.GONE

                }

            }
        }
    }

}