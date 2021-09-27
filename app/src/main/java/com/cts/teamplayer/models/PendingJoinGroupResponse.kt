package com.cts.teamplayer.models

import com.google.gson.annotations.SerializedName

data class PendingJoinGroupResponse(

	@field:SerializedName("data")
	val data: List<PendingJoinGroupDataItem?>? = null,

	@field:SerializedName("success")
	val success: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class PendingJoinGroupDataItem(

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

	@field:SerializedName("email")
	val email: String? = null,

	@field:SerializedName("status")
	val status: String? = null,

	@field:SerializedName("group")
	val group: Group? = null
)

data class Group(

	@field:SerializedName("code")
	val code: String? = null,

	@field:SerializedName("test")
	val test: String? = null,

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("max_size")
	val maxSize: String? = null
)
