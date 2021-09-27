package com.cts.teamplayer.models

import com.google.gson.annotations.SerializedName

data class RequestDemoResponse(

	@field:SerializedName("data")
	val data: RequestDemoData? = null,

	@field:SerializedName("success")
	val success: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class RequestDemoData(

	@field:SerializedName("user_role")
	val userRole: String? = null,

	@field:SerializedName("agreePrivacy")
	val agreePrivacy: Boolean? = null,

	@field:SerializedName("phone")
	val phone: Long? = null,

	@field:SerializedName("selected_date")
	val selectedDate: Any? = null,

	@field:SerializedName("last_name")
	val lastName: String? = null,

	@field:SerializedName("no_of_employees")
	val noOfEmployees: String? = null,

	@field:SerializedName("organization_name")
	val organizationName: String? = null,

	@field:SerializedName("agreeTerms")
	val agreeTerms: Boolean? = null,

	@field:SerializedName("first_name")
	val firstName: String? = null,

	@field:SerializedName("email")
	val email: String? = null
)
