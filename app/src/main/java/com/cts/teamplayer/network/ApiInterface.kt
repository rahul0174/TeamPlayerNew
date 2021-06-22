package com.cts.teamplayer.network

import com.google.gson.JsonArray
import com.google.gson.JsonObject

import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*


interface ApiInterface {
   /* @GET("api/v1/login/member_api/login_member")
    fun login(@Header("business-name") business_name: String,@Header("password") password: String,@Header("email") email: String): Call<JsonObject>

    @FormUrlEncoded
    @POST("api/v1/sign_up/member_api/sign_up/complete")
    fun signup(@FieldMap data: Map<String,String>,@Header("business-name") business_name: String,@Header("password") password: String,@Header("password-confirmation") confiermPass: String): Call<JsonObject>



    @GET("api/v1/sign_up/member_api/sign_up/check_email")
    fun checkEmail(@Header("business-name") business_name: String,@Query("email") email: String): Call<JsonObject>


    @GET("api/v1/sign_up/member_api/sign_up/waiver")
    fun memberInformation(@Header("business-name") business_name: String): Call<JsonObject>

    @GET("api/v1/pos/categories")
    fun getCategory(@Header("access-token") acessToken: String,@Header("business-name") businessName: String ): Call<ArrayList<CategoryPojo>>

    @GET("api/v1/activities")
    fun getActivity(@Header("access-token") acessToken: String,@Header("business-name") businessName: String ): Call<ArrayList<SelectActivityPojo>>

    @GET("api/v1/users/trainers")
    fun getTrainerList(@Header("access-token") acessToken: String,@Header("business-name") businessName: String ): Call<ArrayList<TrainerListPojo>>

    @GET("api/v1/members/user_api/members/{id}/memberships")
    fun getMembershipList(@Header("access-token") acessToken: String,@Header("business-name") businessName: String ,
                          @Path("id") id: String ): Call<ArrayList<MembershipPojo>>

    @GET("api/v1/users/trainers/{trainerId}/business_hours?")
    fun getBusinessHours(@Header("access-token") acessToken: String,@Header("business-name") businessName: String,
                         @Path("trainerId") trainerId :String, @Query("date") date :String): Call<ArrayList<BusinessHoursPojo>>

    @GET("api/v1/pos/products/?")
    fun getProduct(@Query("category_id") id: String, @Header("access-token") acessToken: String, @Header("business-name") businessName: String ): Call<ArrayList<ProductPojo>>

    @GET("api/v1/login/member_api/refresh_token")
    fun refreshToken(@Header("access-token") acessToken: String, @Header("business-name") businessName: String ): Call<JsonObject>


    @POST("api/v1/login/member_api/set_reset_password_token")
    fun forgot(@Header("email") email: String,@Header("business-name") businessName: String ): Call<JsonObject>

    @POST("api/v1/members/member_api/member/change_password")
    fun changePassword(@Header("access-token") accessToken: String,@Header("business-name") businessName: String,
                       @Header("old-password") oldPassword: String,@Header("new-password") newPassword: String,
                       @Header("password-confirmation") passwordConfirmation: String): Call<JsonObject>

    @POST("api/v1/login/member_api/reset_password")
    fun resetPasswordToken(@Header("business-name") businessName: String,@Header("email") email: String,
                       @Header("reset-password-token") resetPasswordToken: String,@Header("new-password") newPassword: String,
                       @Header("password-confirmation") passwordConfirmation: String): Call<JsonObject>

    @GET("api/v1/members/member_api/member")
    fun getProfile(@Header("access-token") acessToken: String, @Header("business-name") businessName: String ): Call<JsonObject>

    @FormUrlEncoded
    @POST("api/v1/appointments/member_api/member/appointments")
    fun confirmBooking(@FieldMap data: Map<String,String>,@Header("business-name") business_name: String,@Header("access-token") acessToken: String): Call<JsonObject>

    @GET("api/v1/pos/products?")
    fun getProductFromActivity(@Header("business-name") business_name: String,@Header("access-token") acessToken: String,
                               @Query("activity_id") activityId: String): Call<ArrayList<ProductPojo>>

    @GET("api/v1/notifications/member_api/member/notifications")
    fun getNotification(@Header("business-name") business_name: String,@Header("access-token") acessToken: String
                               ): Call<ArrayList<NotificationResponse>>


    @POST("api/v1/notifications/member_api/member/notifications/seen/{id}")
    fun getNotificationDetail(@Header("business-name") business_name: String,@Header("access-token") acessToken: String,@Path("id") id: String
    ): Call<NotificationResponse>

    @GET("api/v1/pos/config?")
    fun getPublishKey(@Header("business-name") business_name: String,@Header("access-token") acessToken: String,
                               @Query("site_id") site_id: String): Call<JsonObject>

    @POST("api/v1/pos/member_api/orders")
    fun confirmOrder(@Body data: JsonObject, @Header("business-name") business_name: String, @Header("access-token") acessToken: String): Call<JsonObject>

    @POST("/api/v1/pos/member_api/payment_on_account/{id}")
    fun accountPayment(@Body data: JsonObject,@Path("id") id: String , @Header("business-name") business_name: String, @Header("access-token") acessToken: String): Call<JsonObject>


    @GET("/api/v1/members/user_api/members/{id}/memberships/{membership_id}")
    fun getMembershipDetail(@Header("access-token") acessToken: String,@Header("business-name") businessName: String ,
                            @Path("membership_id") memberId: String ,@Path("id") id: String): Call<MembershidDetailData>

    @GET("api/v1/pos/member_api/orders")
    fun getSaleHistory(@Header("access-token") acessToken: String, @Header("business-name") businessName: String,
                       @Query("start_date") start_date: String, @Query("end_date") end_date: String): Call<ArrayList<SaleHistoryData>>


    @GET("api/v1/pos/member_api/orders/{order_id}")
    fun getSaleHistoryDetails(@Path("order_id") order_id: String,@Header("access-token") acessToken: String, @Header("business-name") businessName: String): Call<SaleDetailData>




    @GET("api/v1/members/member_api/member/attendance")
    fun getAttendanceHistory(@Header("access-token") acessToken: String, @Header("business-name") businessName: String,
                             @Query("start_date") start_date: String, @Query("end_date") end_date: String): Call<ArrayList<AttendanceHistorydata>>

    @GET("api/v1/appointments/member_api/member/appointments")
    fun getAppointmentsHistory(@Header("access-token") acessToken: String, @Header("business-name") businessName: String,
                               @Query("start_date") start_date: String, @Query("end_date") end_date: String): Call<ArrayList<AppointmentHistoryData>>


    @GET("api/v1/members/member_api/member/accounts")
    fun getAccountList(@Header("access-token") acessToken: String, @Header("business-name") businessName: String)
            : Call<ArrayList<AccountsData>>

    @GET("api/v1/members/member_api/member/accounts/{id}")
    fun getAccountDetail(@Header("access-token") acessToken: String, @Header("business-name") businessName: String,
                         @Path("id") id: String)
            : Call<AccountsData>

    @FormUrlEncoded
    @POST("api/v1/members/member_api/member/save_token")
    fun saveFcmToken(@FieldMap data: Map<String,String>, @Header("business-name") business_name: String, @Header("access-token") acessToken: String): Call<JsonObject>

*/
}