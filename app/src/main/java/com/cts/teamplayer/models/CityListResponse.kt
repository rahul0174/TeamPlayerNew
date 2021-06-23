package com.cts.teamplayer.models

import com.google.gson.annotations.SerializedName

data class CityListResponse(

	@field:SerializedName("data")
	val data: List<CityList?>? = null,

	@field:SerializedName("success")
	val success: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class CityList(

	@field:SerializedName("updated_on")
	val updatedOn: String? = null,

	@field:SerializedName("wikiDataId")
	val wikiDataId: String? = null,

	@field:SerializedName("flag")
	val flag: String? = null,

	@field:SerializedName("latitude")
	val latitude: String? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("country_code")
	val countryCode: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("state_id")
	val stateId: String? = null,

	@field:SerializedName("text")
	val text: String? = null,

	@field:SerializedName("state_code")
	val stateCode: String? = null,

	@field:SerializedName("country_id")
	val countryId: String? = null,

	@field:SerializedName("longitude")
	val longitude: String? = null
)
