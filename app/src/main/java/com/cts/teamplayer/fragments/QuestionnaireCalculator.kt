package com.cts.teamplayer.fragments

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.text.method.LinkMovementMethod
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.text.HtmlCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.cts.teamplayer.R
import com.cts.teamplayer.activities.MainActivity
import com.cts.teamplayer.adapters.AnswerAdapter
import com.cts.teamplayer.models.AnswerResponse
import com.cts.teamplayer.models.AnswersItemNew
import com.cts.teamplayer.models.QuestionWithAnswerResponse
import com.cts.teamplayer.models.QuestionsItemNew
import com.cts.teamplayer.network.ApiClient
import com.cts.teamplayer.network.CheckNetworkConnection
import com.cts.teamplayer.network.ItemClickListner
import com.cts.teamplayer.util.ImageGetter
import com.cts.teamplayer.util.MyConstants
import com.cts.teamplayer.util.MyConstants.QUESTION_WITH_ANSWER_CHECKBOX
import com.cts.teamplayer.util.TeamPlayerSharedPrefrence
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_questionnairecalculator.*
import kotlinx.android.synthetic.main.fragment_questionnairecalculator.view.*
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.util.*
import java.util.concurrent.TimeUnit


class QuestionnaireCalculator: Fragment(), View.OnClickListener, ItemClickListner {
    lateinit var v: View
    private var mpref: TeamPlayerSharedPrefrence? = null
    private lateinit var countDownTimer:CountDownTimer

    val timer:Timer?=null
    var questionList: java.util.ArrayList<QuestionsItemNew>? = null
    var answerList: java.util.ArrayList<AnswersItemNew>? = null
    private var selectedList: ArrayList<AnswersItemNew> = ArrayList()
    var question_size:Int?=null
    var i:Int?=0
    var p:Int?=0
    var newvalue:Int?=0
    var radioButton:String?=null
    var question_id:String?=null
    var answer_id:String?=null
    var max_number:String?=null
    var btn_click:String?="1"
    var main_answer:Int?=null
    lateinit var homeFragment: androidx.fragment.app.Fragment


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_questionnairecalculator, container, false)
        mpref = TeamPlayerSharedPrefrence.getInstance(activity!!)
        findId()
        if(activity!!.equals(null)){

        }else{
        }
        return v
    }

    private fun findId() {
        showCounterTimer()
        questionAnswerList()
       v.btn_submit_questionnaire.setOnClickListener(this)
       v.btn_sent_invite.setOnClickListener(this)
    }
    fun getanswerSelect(selected: ArrayList<AnswersItemNew>) {
        selectedList.clear()
        selectedList.addAll(selected)
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.btn_submit_questionnaire -> {
                if (question_size!! >= i!!) {
                    if(radioButton.equals("1")){
                        btn_click="2"
                        countDownTimer.cancel()
                        tv_minutes.text=""
                        tv_seond.text=""
                        val jsonObject = JsonObject()
                        jsonObject.addProperty("question_id", question_id)
                        jsonObject.addProperty("answer_given", answer_id)
                        jsonObject.addProperty("maxanswers", max_number)

                        saveanswerRequest(jsonObject)

                        countDownTimer.start()
                    }else if(radioButton.equals("0")){
                        Toast.makeText(
                            activity!!,
                            "Please select answer ",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                    else{
                        if(selectedList.size==3){
                            countDownTimer.cancel()
                            tv_minutes.text=""
                            tv_seond.text=""
                            saveAnswerJson()
                            countDownTimer.start()
                        }/*else{
                            Toast.makeText(
                                activity!!,
                                "Please select max 3 ",
                                Toast.LENGTH_LONG
                            ).show()
                        }*/


                    }

                }

            }
            R.id.btn_sent_invite -> {
                activity!!.tv_title_header.text=TeamPlayerSharedPrefrence.getInstance(activity!!).getBusinessName("")

               // activity!!.tv_title_header.text="Group"
                homeFragment = InviteGroupListFragment()
                val manager = activity!!.supportFragmentManager
                val transaction = manager.beginTransaction()
                transaction.replace(R.id.container, homeFragment)
                // transaction.addToBackStack(null);
                transaction.commit()

            }
        }
    }
    public fun addAapter(list: ArrayList<AnswersItemNew>?){
        var manager = LinearLayoutManager(activity!!, LinearLayoutManager.VERTICAL, false)
        recyler_question_answer.layoutManager = manager


        val   answerListAdapter =  AnswerAdapter(activity!!, list, this, main_answer!!,this@QuestionnaireCalculator)
        recyler_question_answer.adapter = answerListAdapter
        recyler_question_answer.setItemViewCacheSize(list!!.size)


    }

    private fun questionAnswerList(){
        if (CheckNetworkConnection.isConnection1(activity!!, true)) {
            val progress = ProgressDialog(activity!!)
            progress.setMessage(resources.getString(R.string.please_wait))
            progress.setCancelable(false)
            progress.isIndeterminate = true
            progress.show()
            val apiInterface = ApiClient.getConnection(activity!!)
            var call: Call<QuestionWithAnswerResponse>? = null//apiInterface.profileImage(body,token);
            call = apiInterface!!.getDemoQuestionWithAnswer(mpref!!.getAccessToken("").toString())
            call.enqueue(object : Callback<QuestionWithAnswerResponse> {
                override fun onResponse(
                    call: Call<QuestionWithAnswerResponse>,
                    response: retrofit2.Response<QuestionWithAnswerResponse>
                ) {
                    Log.e("log", response.body().toString());
                    progress.dismiss()
                    if (response.code() >= 200 && response.code() < 210) {
                        try {
                            questionList =
                                response.body()!!.data!!.questions as ArrayList<QuestionsItemNew>?
                            question_size = response.body()!!.data!!.questions!!.size
                            for (j in 0..questionList!!.size-1) {

                             if(response.body()!!.data!!.questions!!.get(j)!!.answerSaved!!.equals(true)){

                                 if(response.body()!!.data!!.questions!!.size-1==i){
                                     ll_1.visibility=View.VISIBLE
                                     ll_2.visibility=View.GONE
                                     val jsonObject = JsonObject()
                                     jsonObject.addProperty("test", "2")
                                     jsonObject.addProperty("group_id", mpref!!.getEmail(""))
                                     setScoreApi(jsonObject)

                                 }else{
                                     i=i!!+1
                                 }
                             } else{
                                 i=i!!+1
                                 main_answer = response.body()!!.data!!.questions!!.get(j)!!.minanswers

                                 displayHtml(  "Q" + response.body()!!.data!!.questions!!.get(j)!!.subpart + ". " + response.body()!!.data!!.questions!!.get(
                                     j
                                 )!!.question)
                           /*      tv_question.text =
                                     "Q" + response.body()!!.data!!.questions!!.get(j)!!.subpart + ". " + response.body()!!.data!!.questions!!.get(
                                         0
                                     )!!.question*/
                                 answerList =
                                     response.body()!!.data!!.questions!!.get(j)!!.answers as ArrayList<AnswersItemNew>?
                                 addAapter(answerList)
                                 if(btn_click.equals("1")){
                                     countDownTimer.start()
                                 }
                                 else{

                                 }
                                 break
                             }

                            }

                         //   showCounterTimer()


                            /*for (i in 1..questionList!!.size) {
                                answerList =
                                    response.body()!!.data!!.questions!!.get(i)!!.answers as ArrayList<AnswersItemNew>?
                                addAapter(answerList)

                            }*/


                        } catch (e: JSONException) {
                            e.printStackTrace()
                        }

                    } else if (response.code() == 500) {
                        Toast.makeText(
                            activity!!,
                            "Internal server error",
                            Toast.LENGTH_LONG
                        ).show()
                    } else {
                        var reader: BufferedReader? = null
                        val sb = StringBuilder()
                        try {
                            reader = BufferedReader(
                                InputStreamReader(
                                    response.errorBody()!!.byteStream()
                                )
                            )
                            var line = reader.readLine()
                            try {
                                if (line != null) {
                                    sb.append(line)
                                }
                            } catch (e: IOException) {
                                e.printStackTrace()
                            }

                        } catch (e: Exception) {
                            e.printStackTrace()
                        }

                        try {
                            val finallyError = sb.toString()
                            val jsonObjectError = JSONObject(finallyError)
                            val message = jsonObjectError.optString("message")
                            Toast.makeText(activity!!, message, Toast.LENGTH_LONG).show()
                        } catch (e: JSONException) {
                            e.printStackTrace()
                            Toast.makeText(
                                activity!!,
                                "Some error occurred",
                                Toast.LENGTH_LONG
                            ).show()
                        }

                    }
                }

                override fun onFailure(call: Call<QuestionWithAnswerResponse>, t: Throwable) {
                    progress.dismiss()
                    Toast.makeText(
                        activity!!,
                        resources.getString(R.string.Something_went_worng),
                        Toast.LENGTH_LONG
                    ).show()
                }
            })
        } else {
            Toast.makeText(
                activity!!,
                resources.getString(R.string.please_check_internet),
                Toast.LENGTH_LONG
            )
                .show()
        }
    }

    override fun onClickItem(position: Int, requestcode: Int) {
        if(requestcode== MyConstants.QUESTION_WITH_ANSWER){
          /*  {"question_id":"143","answer_given":"589","maxanswers":"1"}*/
            radioButton="1"
           // question_id=   mpref!!.getQuestionID("")
            answer_id=answerList!!.get(position).answerId
            question_id=answerList!!.get(position).questionid
            max_number=questionList!!.get(answerList!!.size+1).maxanswers



         /*   {"question_id":"142","answer_given":[{"answer_id":"580","questionid":"142","answer":"Talk to a colleague\n","sortorder":"1","image":"","created_at":null,"updated_at":null,"status":true}],"maxanswers":"3"}
       */
        }
        if (requestcode==QUESTION_WITH_ANSWER_CHECKBOX){
            radioButton="0"
        }

    }



    private fun saveAnswerJson(){
        val jsonObject = JsonObject()
        val array = JsonArray()
        for (i in 0..selectedList.size - 1){
            val jsonObjectAnswerGiven = JsonObject()
            jsonObjectAnswerGiven.addProperty("answer_id", selectedList.get(i).answerId)
            jsonObjectAnswerGiven.addProperty("questionid", selectedList.get(i).questionid)
            jsonObjectAnswerGiven.addProperty("answer", "")
            jsonObjectAnswerGiven.addProperty("sortorder", selectedList.get(i).sortorder)
            jsonObjectAnswerGiven.addProperty("image", selectedList.get(i).image)
            jsonObjectAnswerGiven.addProperty("created_at", "")
            jsonObjectAnswerGiven.addProperty("updated_at", "")
            jsonObjectAnswerGiven.addProperty("status", true)
            array.add(jsonObjectAnswerGiven)
        }

            jsonObject.addProperty("question_id", questionList!!.get(i!!).id)
        if (radioButton.equals("1")){

        }else{
            jsonObject.add("answer_given", array)
        }

            jsonObject.addProperty("maxanswers", main_answer)
            saveanswerRequest(jsonObject)

    }



    private fun showCounterTimer(){
        countDownTimer = object : CountDownTimer(90000, 1000) {
            override fun onTick(millisUntilFinished: Long) {

                try {
                    val seconds = (millisUntilFinished / 1000 % 60)
                    v.tv_minutes.setText("" + millisUntilFinished / 1000 / 60)
                    v.tv_seond.setText("" + seconds)
                    val minutes: Long = TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished / 1000)

                }catch (e:Exception){

                }

            }

            override fun onFinish() {
                questionAnswerListTimerFinish()
                showCounterTimer()
              /*  displayHtml(    "Q" + questionList!!.get(p!!)!!.subpart + ". " + questionList!!.get(
                    p!!)!!.question)*/

            //    mTextField.setText("done!")
            }
        }

    }

    private fun saveanswerRequest(jsonObject: JsonObject){
        if (CheckNetworkConnection.isConnection1(activity!!, true)) {
            val progress = ProgressDialog(activity!!)
            progress.setMessage(resources.getString(R.string.please_wait))
            progress.setCancelable(false)
            progress.isIndeterminate = true
            progress.show()
            val apiInterface = ApiClient.getConnection(activity!!)
            var call: Call<AnswerResponse>? = null//apiInterface.profileImage(body,token);
            call = apiInterface!!.saveAnswerParameter(mpref!!.getAccessToken("").toString(),jsonObject)
            call!!.enqueue(object : Callback<AnswerResponse> {
                @SuppressLint("SetTextI18n")
                override fun onResponse(
                    call: Call<AnswerResponse>,
                    response: retrofit2.Response<AnswerResponse>
                ) {
                    Log.e("log", response.body().toString());
                    progress.dismiss()
                    if (response.code() >= 200 && response.code() < 210) {
                        try {
                            Toast.makeText(
                                activity!!,
                                response.body()!!.message,
                                Toast.LENGTH_LONG
                            ).show()
                            radioButton="0"

                            if(questionList!!.size>i!!){

                                for (j in i!!..questionList!!.size-1) {
                                    p=j;
                                    if(questionList!!.get(j)!!.answerSaved!!.equals(true)){
                                        i=i!!+1
                                    }
                                    else{
                                        i=i!!+1
                                        main_answer = questionList!!.get(j)!!.minanswers
                             /*           tv_question.text =
                                            "Q" + questionList!!.get(j)!!.subpart + ". " + questionList!!.get(
                                                j)!!.question*/

                                        displayHtml(    "Q" + questionList!!.get(j)!!.subpart + ". " + questionList!!.get(
                                            j)!!.question)
                                        answerList =
                                            questionList!!.get(j)!!.answers as ArrayList<AnswersItemNew>?
                                        addAapter(answerList)
                                        selectedList.clear()
                                        //     showCounterTimer()
                                        ll_2.visibility=View.VISIBLE
                                        ll_1.visibility=View.GONE
                                        if(btn_click.equals("1")){
                                            countDownTimer.start()
                                        }else{

                                        }
                                        break
                                    }
                                    radioButton=null

                                }
                        /*        main_answer = questionList!!.get(i!!).minanswers
                                tv_question.text =
                                    "Q" + questionList!!.get(i!!).subpart + ". " + questionList!!.get(i!!).question
                                answerList =
                                    questionList!!.get(i!!).answers as ArrayList<AnswersItemNew>?
                                addAapter(answerList)
                                selectedList.clear()
                           //     showCounterTimer()
                                ll_2.visibility=View.VISIBLE
                                ll_1.visibility=View.GONE*/

                            }else{
                                countDownTimer.cancel()
                                ll_2.visibility=View.GONE
                                ll_1.visibility=View.VISIBLE
                                val jsonObject = JsonObject()
                                jsonObject.addProperty("test", "2")
                                jsonObject.addProperty("group_id", mpref!!.getEmail(""))
                                setScoreApi(jsonObject)
                           /*     Toast.makeText(
                                    activity!!,
                                    "You have completed questionare.",
                                    Toast.LENGTH_LONG
                                ).show()*/


                              //  questionAnswerList()
                            }
                            tv_minutes.text=""
                            tv_seond.text=""




                            /*for (i in 1..questionList!!.size) {
                                answerList =
                                    response.body()!!.data!!.questions!!.get(i)!!.answers as ArrayList<AnswersItemNew>?
                                addAapter(answerList)

                            }*/


                        } catch (e: JSONException) {
                            e.printStackTrace()
                        }

                    } else if (response.code() == 500) {
                        Toast.makeText(
                            activity!!,
                            "Internal server error",
                            Toast.LENGTH_LONG
                        ).show()
                    } else {
                        var reader: BufferedReader? = null
                        val sb = StringBuilder()
                        try {
                            reader = BufferedReader(
                                InputStreamReader(
                                    response.errorBody()!!.byteStream()
                                )
                            )
                            var line = reader.readLine()
                            try {
                                if (line != null) {
                                    sb.append(line)
                                }
                            } catch (e: IOException) {
                                e.printStackTrace()
                            }

                        } catch (e: Exception) {
                            e.printStackTrace()
                        }

                        try {
                            val finallyError = sb.toString()
                            val jsonObjectError = JSONObject(finallyError)
                            val message = jsonObjectError.optString("message")
                            Toast.makeText(activity!!, message, Toast.LENGTH_LONG).show()
                        } catch (e: JSONException) {
                            e.printStackTrace()
                            Toast.makeText(
                                activity!!,
                                "Some error occurred",
                                Toast.LENGTH_LONG
                            ).show()
                        }

                    }
                }

                override fun onFailure(call: Call<AnswerResponse>, t: Throwable) {
                    progress.dismiss()
                    Toast.makeText(
                        activity!!,
                        resources.getString(R.string.Something_went_worng),
                        Toast.LENGTH_LONG
                    ).show()
                }
            })
        } else {
            Toast.makeText(
                activity!!,
                resources.getString(R.string.please_check_internet),
                Toast.LENGTH_LONG
            )
                .show()
        }
    }

    private fun displayHtml(html: String) {

        // Creating object of ImageGetter class you just created
        val imageGetter = ImageGetter(resources, tv_question)

        // Using Html framework to parse html
        val styledText= HtmlCompat.fromHtml(html,
            HtmlCompat.FROM_HTML_MODE_LEGACY,
            imageGetter,null)

        // to enable image/link clicking
        tv_question.movementMethod = LinkMovementMethod.getInstance()

        // setting the text after formatting html and downloading and setting images
        tv_question.text = styledText
    }

    private fun questionAnswerListTimerFinish(){
        if (CheckNetworkConnection.isConnection1(activity!!, true)) {
            val progress = ProgressDialog(activity!!)
            progress.setMessage(resources.getString(R.string.please_wait))
            progress.setCancelable(false)
            progress.isIndeterminate = true
            progress.show()
            val apiInterface = ApiClient.getConnection(activity!!)
            var call: Call<QuestionWithAnswerResponse>? = null//apiInterface.profileImage(body,token);
            call = apiInterface!!.getDemoQuestionWithAnswer(mpref!!.getAccessToken("").toString())
            call!!.enqueue(object : Callback<QuestionWithAnswerResponse> {
                override fun onResponse(
                    call: Call<QuestionWithAnswerResponse>,
                    response: retrofit2.Response<QuestionWithAnswerResponse>
                ) {
                    Log.e("log", response.body().toString());
                    progress.dismiss()
                    if (response.code() >= 200 && response.code() < 210) {
                        try {
                            questionList =
                                response.body()!!.data!!.questions as ArrayList<QuestionsItemNew>?
                            question_size = response.body()!!.data!!.questions!!.size
                            for (j in 0..questionList!!.size-1) {

                                if(response.body()!!.data!!.questions!!.get(j)!!.answerSaved!!.equals(true)){

                                    if((response.body()!!.data!!.questions!!.size-1).toString().equals(i.toString())){
                                        ll_1.visibility=View.VISIBLE
                                        ll_2.visibility=View.GONE
                                        val jsonObject = JsonObject()
                                        jsonObject.addProperty("test", "2")
                                        jsonObject.addProperty("group_id", mpref!!.getEmail(""))
                                        setScoreApi(jsonObject)

                                    }

                                 //   i=i!!+1
                                } else if (response.body()!!.data!!.questions!!.size==j+1){
                                    i=0
                                    showCounterTimer()
                                    questionAnswerList()
                                }
                                else {
                                    i=i!!+1
                                    main_answer = response.body()!!.data!!.questions!!.get(j+1)!!.minanswers

                                    displayHtml(  "Q" + response.body()!!.data!!.questions!!.get(j+1)!!.subpart + ". " + response.body()!!.data!!.questions!!.get(
                                        j+1
                                    )!!.question)
                                    /*      tv_question.text =
                                              "Q" + response.body()!!.data!!.questions!!.get(j)!!.subpart + ". " + response.body()!!.data!!.questions!!.get(
                                                  0
                                              )!!.question*/
                                    answerList =
                                        response.body()!!.data!!.questions!!.get(j+1)!!.answers as ArrayList<AnswersItemNew>?
                                    addAapter(answerList)
                                    if(btn_click.equals("1")){
                                        countDownTimer.start()
                                    }
                                    else{

                                    }
                                    break
                                }

                            }

                            //   showCounterTimer()


                            /*for (i in 1..questionList!!.size) {
                                answerList =
                                    response.body()!!.data!!.questions!!.get(i)!!.answers as ArrayList<AnswersItemNew>?
                                addAapter(answerList)

                            }*/


                        } catch (e: JSONException) {
                            e.printStackTrace()
                        }

                    } else if (response.code() == 500) {
                        Toast.makeText(
                            activity!!,
                            "Internal server error",
                            Toast.LENGTH_LONG
                        ).show()
                    } else {
                        var reader: BufferedReader? = null
                        val sb = StringBuilder()
                        try {
                            reader = BufferedReader(
                                InputStreamReader(
                                    response.errorBody()!!.byteStream()
                                )
                            )
                            var line = reader.readLine()
                            try {
                                if (line != null) {
                                    sb.append(line)
                                }
                            } catch (e: IOException) {
                                e.printStackTrace()
                            }

                        } catch (e: Exception) {
                            e.printStackTrace()
                        }

                        try {
                            val finallyError = sb.toString()
                            val jsonObjectError = JSONObject(finallyError)
                            val message = jsonObjectError.optString("message")
                            Toast.makeText(activity!!, message, Toast.LENGTH_LONG).show()
                        } catch (e: JSONException) {
                            e.printStackTrace()
                            Toast.makeText(
                                activity!!,
                                "Some error occurred",
                                Toast.LENGTH_LONG
                            ).show()
                        }

                    }
                }

                override fun onFailure(call: Call<QuestionWithAnswerResponse>, t: Throwable) {
                    progress.dismiss()
                    Toast.makeText(
                        activity!!,
                        resources.getString(R.string.Something_went_worng),
                        Toast.LENGTH_LONG
                    ).show()
                }
            })
        } else {
            Toast.makeText(
                activity!!,
                resources.getString(R.string.please_check_internet),
                Toast.LENGTH_LONG
            )
                .show()
        }
    }
    private fun setScoreApi(jsonObject: JsonObject) {
        if (CheckNetworkConnection.isConnection1(activity!!, true)) {
            val progress = ProgressDialog(activity!!)
            progress.setMessage(resources.getString(R.string.please_wait))
            progress.setCancelable(false)
            progress.isIndeterminate = true
           // progress.show()
            val apiInterface = ApiClient.getConnection(activity!!)
            var call: Call<JsonObject>? = null//apiInterface.profileImage(body,token);
            call = apiInterface!!.setScoreParameter(mpref!!.getAccessToken("").toString(),jsonObject)
            call!!.enqueue(object : Callback<JsonObject> {
                override fun onResponse(
                    call: Call<JsonObject>,
                    response: retrofit2.Response<JsonObject>
                ) {
                    // Log.e("log",response.body().toString());
                    progress.dismiss()
                    if (response.code() >= 200 && response.code() < 210) {
                        try {
                            Log.d("response", response.body()!!.toString())
                            val jsonObject = JSONObject(response.body().toString())
                        /*    Toast.makeText(
                                activity!!,
                                jsonObject.optString("message"),
                                Toast.LENGTH_LONG
                            ).show()
*/
                            val token = jsonObject.getJSONObject("data").optString("token")
                            val role = jsonObject.getJSONObject("data").optString("role")
                            mpref!!.setAccessToken(token)
                            mpref!!.setRoal(role)
                            val i = Intent(activity!!, MainActivity::class.java).addFlags(
                                Intent.FLAG_ACTIVITY_NEW_TASK
                            ).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                            startActivity(i)

                            //
                            // Log.d("usertype",user_type);
                            /*       mpref!!.setToken(token)
                            mpref!!.setUserType(usertype)
                            mpref!!.setUserId(usertype)
                           */

                        } catch (e: JSONException) {
                            e.printStackTrace()
                        }

                    } else if (response.code() == 500) {
                        Toast.makeText(
                            activity!!,
                            "Internal server error",
                            Toast.LENGTH_LONG
                        ).show()
                    } else {
                        var reader: BufferedReader? = null
                        val sb = StringBuilder()
                        try {
                            reader = BufferedReader(
                                InputStreamReader(
                                    response.errorBody()!!.byteStream()
                                )
                            )
                            var line = reader.readLine()
                            try {
                                if (line != null) {
                                    sb.append(line)
                                }
                            } catch (e: IOException) {
                                e.printStackTrace()
                            }

                        } catch (e: Exception) {
                            e.printStackTrace()
                        }

                        try {
                            val finallyError = sb.toString()
                            val jsonObjectError = JSONObject(finallyError)
                            val message = jsonObjectError.optString("message")
                            Toast.makeText(activity!!, message, Toast.LENGTH_LONG).show()
                        } catch (e: JSONException) {
                            e.printStackTrace()
                            Toast.makeText(
                                activity!!,
                                "Some error occurred",
                                Toast.LENGTH_LONG
                            ).show()
                        }

                    }
                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    progress.dismiss()
                    Toast.makeText(
                        activity!!,
                        resources.getString(R.string.Something_went_worng),
                        Toast.LENGTH_LONG
                    ).show()
                }
            })
        } else {
            Toast.makeText(
                activity!!,
                resources.getString(R.string.please_check_internet),
                Toast.LENGTH_LONG
            )
                .show()
        }
    }




}