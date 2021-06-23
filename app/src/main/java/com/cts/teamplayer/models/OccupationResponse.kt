package com.cts.teamplayer.models

import com.google.gson.annotations.SerializedName

data class OccupationResponse(

	@field:SerializedName("data")
	val data: List<OccupationsList?>? = null,

	@field:SerializedName("success")
	val success: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class OccupationsList(

	@field:SerializedName("code")
	val code: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("text")
	val text: String? = null
)
