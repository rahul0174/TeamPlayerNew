package com.cts.teamplayer.network

import com.cts.teamplayer.models.*
import com.google.gson.JsonArray
import com.google.gson.JsonObject

import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*


interface ApiInterface {

    @GET("countries")
    fun getCountry(): Call<CountryListResponse>
    @GET("sector")
    fun getSector(): Call<SectorListResponse>
    @GET("occupation")
    fun getOccupation(): Call<OccupationResponse>
    @GET("states")
    fun getState(@Query("country_id") city_id: String): Call<StateListResponse>
    @GET("cities")
    fun getCity(@Query("state_id") state_id: String): Call<CityListResponse>

    @Multipart
    @POST("user/upload")
    fun uploadSingleFile(@Part list: List<MultipartBody.Part>): Call<JsonObject>

    @POST("user/register")
    fun userSignup(@Body data: JsonObject): Call<JsonObject>

    @POST("user/register_org")
    fun companySignup(@Body data: JsonObject): Call<JsonObject>

    @POST("user/login")
    fun login(@Body data: JsonObject): Call<JsonObject>

    @POST("user/forgot_password")
    fun forgotpass(@Body data: JsonObject): Call<JsonObject>

    @GET("user/profile")
    fun getUserDetailByToken(@Header("authorization") token: String?): Call<UserProfileResponse>

    @POST("user/profile/update")
    fun updateProfileRequest(@Header("authorization") token: String?,@Body data: JsonObject): Call<JsonObject>

    @POST("user/demo_request")
    fun remoRequest(@Body data: JsonObject): Call<JsonObject>

    @POST("user/contact")
    fun contactUsRequest(@Body data: JsonObject): Call<JsonObject>

    @GET("faq")
    fun getfaqDetailByToken(@Header("authorization") token: String?): Call<FaqResultResponse>

    @GET("demo/questions")
    fun getDemoQuestionWithAnswer(@Header("authorization") token: String?): Call<QuestionWithAnswerResponse>

 @GET("demo/group_joined")
    fun getgroupJoinListParameter(@Header("authorization") token: String?): Call<GroupJoinResponse>

    @POST("demo/questions/save_answer")
    fun saveAnswerParameter(@Header("authorization") token: String,@Body data: JsonObject): Call<AnswerResponse>

    @POST("demo/send_invite")
    fun sendInviteParameter(@Header("authorization") token: String?,@Body data: JsonObject): Call<SendInviteResponse>

   @GET("demo/questionnaire")
    fun groupListParameter(@Header("authorization") token: String?): Call<GroupListResponse>


    @GET("demo/invitation")
    fun penddingListParameter(@Header("authorization") token: String?): Call<PendingJoinGroupResponse>

    @GET("demo/questionnaire/detail")
    fun questionnaireGroupDetailsParameter(@Header("authorization") token: String?,@Query("id") id: String): Call<QuestionnaireGroupDetailResponse>

    @POST("demo/get_subgroup")
    fun subGroupListParameter(@Header("authorization") token: String?,@Body data: JsonObject): Call<SubGroupResponse>

    @GET("demo/plan")
    fun plabListParameter(@Header("authorization") token: String?): Call<PlanListResponse>

      @POST("demo/add_subgroup")
      fun addSubGroupParameter(@Header("authorization") token: String?,@Body data: JsonObject): Call<SendInviteResponse>

    @GET("braintree/getclienttoken")
    fun getclienttokenParameter(@Header("authorization") token: String?): Call<JsonObject>


    @POST("braintree/createpurchase")
    fun authenticatePayment(@Header("token") token: String?,@Body data: JsonObject): Call<JsonObject>

    @POST("user/updateAppSubscriptionPayment")
    fun updateAppSubscriptionPayment(@Header("authorization") token: String?,@Body data: JsonObject): Call<JsonObject>


    @POST("demo/add_user_subgroup")
    fun addtoteamRequest(@Header("authorization") token: String?,@Body data: JsonObject): Call<JsonObject>

    @POST("demo/send_reminder")
    fun sendReminderRequest(@Header("authorization") token: String?,@Body data: JsonObject): Call<JsonObject>

    @PUT("demo/team_usertype")
    fun surveyResultParameter(@Header("authorization") token: String?,@Body data: JsonObject): Call<JsonObject>

    @POST("demo/survey_result_team")
    fun compatibilityParameter(@Header("authorization") token: String?,@Body data: JsonObject): Call<CompatibilitySurveyResponse>


    @POST("demo/join_group")
    fun joinGroupRequest(@Header("authorization") token: String?,@Body data: JsonObject): Call<JsonObject>

    @GET("demo/questionnaire/invitee_list")
    fun inviteeListParameter(@Header("authorization") token: String?,@Query("id") id: String): Call<InviteeListResponse>

    @GET("demo/plan")
    fun getDemoPlan(): Call<PerQuestionPriceResponse>


    @GET("news")
    fun getnewsDetailByToken(@Header("authorization") token: String?): Call<NewsListNewResponse>

    @GET("user/app/subscription")
    fun getAppSubscription(@Header("authorization") token: String?): Call<AppSubscriptionResponse>

    @GET("user/appQuestionnairePurchase")
    fun getAppQuestionnairePurchase(@Header("authorization") token: String?): Call<AppQuestionPurchaseResponse>


    @GET("user/appPPCPurchase")
    fun getAppPPCPurchase(@Header("authorization") token: String?): Call<AppQuestionPurchaseResponse>


    @GET("user/renewalPurchase")
    fun getRenewalPurchase(@Header("authorization") token: String?): Call<AppQuestionPurchaseResponse>


    @GET("user/fullQuestionnairePurchase")
    fun getfullQuestionnairePurchase(@Header("authorization") token: String?): Call<AppQuestionPurchaseResponse>



    @GET("user/appSubscriptionPurchase")
    fun getAppSubscriptionPurchase(@Header("authorization") token: String?): Call<AppQuestionPurchaseResponse>



    @GET("user/subscriptionPurchase")
    fun subscriptionPurchase(@Header("authorization") token: String?): Call<AppQuestionPurchaseResponse>

    @GET("user/appQuestionnairePurchase")
    fun getPPCAmount(@Header("authorization") token: String?): Call<PPCResponse>

    @POST("user/updatePPCPayment")
    fun getUpdateupdatePPCPaymentt(@Header("authorization") token: String?,@Body data: JsonObject): Call<JsonObject>



}