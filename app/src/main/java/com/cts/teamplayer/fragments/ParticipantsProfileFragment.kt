package com.cts.teamplayer.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.cts.teamplayer.R
import com.cts.teamplayer.activities.SignUpActivity
import com.cts.teamplayer.activities.UpdateProfileActivity
import com.cts.teamplayer.util.TeamPlayerSharedPrefrence
import kotlinx.android.synthetic.main.fragment_participants_profile.*

class ParticipantsProfileFragment : Fragment(),View.OnClickListener {
    lateinit var v: View
    private var mpref: TeamPlayerSharedPrefrence? = null
    lateinit var iv_no_data_cancel_order: ImageView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_participants_profile, container, false)
        mpref = TeamPlayerSharedPrefrence.getInstance(activity!!)
        rl_profile_edit.setOnClickListener(this)
        return v
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.rl_profile_edit -> {
                startActivity(Intent(activity, UpdateProfileActivity::class.java))

            }
        }

    }

}