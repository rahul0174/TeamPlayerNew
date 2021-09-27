package com.cts.teamplayer.models

import com.google.gson.annotations.SerializedName

data class AnswerResponse(

	/*@field:SerializedName("data")
	val data: List<String?>? = null,*/

	@field:SerializedName("success")
	val success: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null
)
