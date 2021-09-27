package com.cts.teamplayer.models

import com.google.gson.annotations.SerializedName

data class GroupListResponse(

	@field:SerializedName("data")
	val data: List<GroupListItem?>? = null,

	@field:SerializedName("success")
	val success: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class GroupListItem(

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
	val id: String? = null,

	@field:SerializedName("max_size")
	val maxSize: String? = null
)
