package com.cts.teamplayer.models

import com.google.gson.annotations.SerializedName

data class UserProfileDetailsNResponse(

	@field:SerializedName("data")
	val data: UserProfileDetailsNData? = null,

	@field:SerializedName("success")
	val success: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class UserProfileDetailsNStateData(

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

	@field:SerializedName("iso2")
	val iso2: String? = null,

	@field:SerializedName("country_id")
	val countryId: String? = null
)

data class UserProfileDetailsNCityData(

	@field:SerializedName("updated_on")
	val updatedOn: String? = null,

	@field:SerializedName("country_code")
	val countryCode: String? = null,

	@field:SerializedName("wikiDataId")
	val wikiDataId: String? = null,

	@field:SerializedName("flag")
	val flag: String? = null,

	@field:SerializedName("latitude")
	val latitude: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("state_id")
	val stateId: String? = null,

	@field:SerializedName("state_code")
	val stateCode: String? = null,

	@field:SerializedName("country_id")
	val countryId: String? = null,

	@field:SerializedName("longitude")
	val longitude: String? = null
)

data class UserProfileDetailsNOccupationData(

	@field:SerializedName("code")
	val code: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("id")
	val id: Int? = null
)

data class UserProfileDetailsNSectorData(

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("id")
	val id: Int? = null
)

data class UserProfileDetailsNCountryData(

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

	@field:SerializedName("iso2")
	val iso2: String? = null,

	@field:SerializedName("iso3")
	val iso3: String? = null
)

data class UserProfileDetailsNData(

	@field:SerializedName("country")
	val country: String? = null,

	@field:SerializedName("occupation")
	val occupation: String? = null,

	@field:SerializedName("city")
	val city: String? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("title")
	val title: String? = null,

	@field:SerializedName("user_role")
	val userRole: Any? = null,

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("address_line_1")
	val addressLine1: String? = null,

	@field:SerializedName("sector_data")
	val sectorData: UserProfileDetailsNSectorData? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("address_line_2")
	val addressLine2: String? = null,

	@field:SerializedName("state")
	val state: String? = null,

	@field:SerializedName("first_name")
	val firstName: String? = null,

	@field:SerializedName("sector")
	val sector: String? = null,

	@field:SerializedName("email")
	val email: String? = null,

	@field:SerializedName("zip")
	val zip: String? = null,

	@field:SerializedName("image")
	val image: Any? = null,

	@field:SerializedName("im")
	val im: Any? = null,

	@field:SerializedName("occupation_data")
	val occupationData: UserProfileDetailsNOccupationData? = null,

	@field:SerializedName("last_name")
	val lastName: String? = null,

	@field:SerializedName("no_of_employees")
	val noOfEmployees: Any? = null,

	@field:SerializedName("organization_name")
	val organizationName: String? = null,

	@field:SerializedName("state_data")
	val stateData: UserProfileDetailsNStateData? = null,

	@field:SerializedName("cv")
	val cv: Any? = null,

	@field:SerializedName("country_data")
	val countryData: UserProfileDetailsNCountryData? = null,

	@field:SerializedName("user_id")
	val userId: String? = null,

	@field:SerializedName("phone")
	val phone: String? = null,

	@field:SerializedName("city_data")
	val cityData: UserProfileDetailsNCityData? = null
)
