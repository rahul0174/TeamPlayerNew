package com.cts.teamplayer.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.cts.teamplayer.R
import com.cts.teamplayer.util.TeamPlayerSharedPrefrence
import kotlinx.android.synthetic.main.activity_signin.*
import kotlinx.android.synthetic.main.activity_signin.edit_user_name
import kotlinx.android.synthetic.main.fragment_contact_us.*
import kotlinx.android.synthetic.main.fragment_contact_us.view.*

class ContactUsFragment: Fragment(), View.OnClickListener {
    lateinit var v: View
    private var mpref: TeamPlayerSharedPrefrence? = null
    lateinit var iv_no_data_cancel_order: ImageView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_contact_us, container, false)
        mpref = TeamPlayerSharedPrefrence.getInstance(activity!!)
        return v
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.btn_submit_in_contact_us -> {
                if (edit_ful_name_contact.text!!.toString().trim { it <= ' ' }.length == 0) {
                    Toast.makeText(
                        activity,
                        resources.getString(R.string.full_name),
                        Toast.LENGTH_SHORT
                    ).show()
                } else if (edit_title_in_contact.text!!.toString().trim { it <= ' ' }.length == 0) {
                    Toast.makeText(
                        activity,
                        resources.getString(R.string.enter_title),
                        Toast.LENGTH_SHORT
                    ).show()
                }else if (edit_phone_num_in_contact.text!!.toString().trim { it <= ' ' }.length == 0) {
                    Toast.makeText(
                        activity,
                        resources.getString(R.string.phone_num),
                        Toast.LENGTH_SHORT
                    ).show()
                }else if (edit_subject_in_contact_us.text!!.toString().trim { it <= ' ' }.length == 0) {
                    Toast.makeText(
                        activity,
                        resources.getString(R.string.enter_subject),
                        Toast.LENGTH_SHORT
                    ).show()
                }else if (edit_message_contact.text!!.toString().trim { it <= ' ' }.length == 0) {
                    Toast.makeText(
                        activity,
                        resources.getString(R.string.enter_message),
                        Toast.LENGTH_SHORT
                    ).show()
                }else{
                    Toast.makeText(
                        activity,
                        "Succesfully",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            }
        }
    }

}