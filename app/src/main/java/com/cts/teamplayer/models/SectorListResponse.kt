package com.cts.teamplayer.models

import com.google.gson.annotations.SerializedName

data class SectorListResponse(

	@field:SerializedName("data")
	val data: List<SectorList?>? = null,

	@field:SerializedName("success")
	val success: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class SectorList(

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("text")
	val text: String? = null
)
