package com.cts.teamplayer.models

import com.google.gson.annotations.SerializedName

data class AppSubscriptionResponse(

	@field:SerializedName("data")
	val data: List<AppSubscriptionList?>? = null,

	@field:SerializedName("success")
	val success: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class AppSubscriptionList(

	@field:SerializedName("duration")
	val duration: String? = null,

	@field:SerializedName("amount")
	val amount: String? = null,

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("detail")
	val detail: String? = null,

	@field:SerializedName("frequency_type")
	val frequencyType: String? = null,

	@field:SerializedName("title")
	val title: String? = null,

	@field:SerializedName("frequency")
	val frequency: String? = null
)
