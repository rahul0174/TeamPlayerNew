package com.cts.teamplayer.models

import com.google.gson.annotations.SerializedName

data class PlanListResponse(

	@field:SerializedName("data")
	val data: List<PlanList?>? = null,

	@field:SerializedName("success")
	val success: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class PlanList(

	@field:SerializedName("number_survay")
	val numberSurvay: String? = null,

	@field:SerializedName("amount")
	val amount: String? = null,

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("updated_by")
	val updatedBy: String? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("created_by")
	val createdBy: String? = null
)
