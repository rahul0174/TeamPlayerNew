package com.cts.teamplayer.models

import com.google.gson.annotations.SerializedName

data class StateListResponse(

	@field:SerializedName("data")
	val data: List<StateList?>? = null,

	@field:SerializedName("success")
	val success: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class StateList(

	@field:SerializedName("country_code")
	val countryCode: String? = null,

	@field:SerializedName("wikiDataId")
	val wikiDataId: String? = null,

	@field:SerializedName("flag")
	val flag: String? = null,

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("fips_code")
	val fipsCode: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("text")
	val text: String? = null,

	@field:SerializedName("iso2")
	val iso2: String? = null,

	@field:SerializedName("country_id")
	val countryId: String? = null
)
