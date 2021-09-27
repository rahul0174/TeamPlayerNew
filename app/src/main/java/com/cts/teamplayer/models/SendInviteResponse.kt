package com.cts.teamplayer.models

import com.google.gson.annotations.SerializedName

data class SendInviteResponse(

	@field:SerializedName("data")
	val data: SendInviteData? = null,

	@field:SerializedName("success")
	val success: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class SendInviteData(

	@field:SerializedName("email")
	val email: List<String?>? = null
)
