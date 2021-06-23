package com.cts.teamplayer.models

import com.google.gson.annotations.SerializedName

data class CountryListResponse(

	@field:SerializedName("data")
	val data: List<CountryList?>? = null,

	@field:SerializedName("success")
	val success: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class CountryList(

	@field:SerializedName("capital")
	val capital: String? = null,

	@field:SerializedName("emojiU")
	val emojiU: String? = null,

	@field:SerializedName("wikiDataId")
	val wikiDataId: String? = null,

	@field:SerializedName("flag")
	val flag: String? = null,

	@field:SerializedName("emoji")
	val emoji: String? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("phonecode")
	val phonecode: String? = null,

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("native")
	val jsonMemberNative: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("currency")
	val currency: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("text")
	val text: String? = null,

	@field:SerializedName("iso2")
	val iso2: String? = null,

	@field:SerializedName("iso3")
	val iso3: String? = null
)
