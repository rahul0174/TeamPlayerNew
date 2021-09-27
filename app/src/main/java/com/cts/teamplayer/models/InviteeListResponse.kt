package com.cts.teamplayer.models

import com.google.gson.annotations.SerializedName

data class InviteeListResponse(

	@field:SerializedName("data")
	val data: List<InviteeListDataItem?>? = null,

	@field:SerializedName("success")
	val success: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class InviteeListDataItem(

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("group_id")
	val groupId: String? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("invited_by")
	val invitedBy: String? = null,

	@field:SerializedName("on_date")
	val onDate: String? = null,

	@field:SerializedName("email")
	val email: String? = null,

	@field:SerializedName("status")
	val status: String? = null
)
