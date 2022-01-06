package com.cts.teamplayer.models

import com.google.gson.annotations.SerializedName

data class AppQuestionPurchaseResponse(

	@field:SerializedName("data")
	val data: List<AppQuestionPurchase?>? = null,

	@field:SerializedName("success")
	val success: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class AppQuestionPurchase(

	@field:SerializedName("amount")
	val amount: String? = null,

	@field:SerializedName("test")
	val test: String? = null,
	@field:SerializedName("plan_title")
	val plantitle: String? = null,

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("user_id")
	val userId: String? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("no_of_participant")
	val noOfParticipant: String? = null,

	@field:SerializedName("on_date")
	val onDate: String? = null,

	@field:SerializedName("plan_id")
	val planId: String? = null
)
