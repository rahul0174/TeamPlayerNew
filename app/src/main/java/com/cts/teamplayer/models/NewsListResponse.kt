package com.cts.teamplayer.models

import com.google.gson.annotations.SerializedName

data class NewsListResponse(

	@field:SerializedName("data")
	val data: List<NewsDataItem?>? = null,

	@field:SerializedName("success")
	val success: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class NewsDataItem(

	@field:SerializedName("feature_image")
	val featureImage: String? = null,

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("description")
	val description: String? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("title")
	val title: String? = null,

	@field:SerializedName("keyword")
	val keyword: String? = null,

	@field:SerializedName("slug")
	val slug: String? = null,

	@field:SerializedName("content")
	val content: String? = null
)
