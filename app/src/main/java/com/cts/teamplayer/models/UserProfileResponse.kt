package com.cts.teamplayer.models

import com.google.gson.annotations.SerializedName

data class UserProfileResponse(

	@field:SerializedName("data")
	val data: UserProfileData? = null,

	@field:SerializedName("success")
	val success: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class OccupationData(

	@field:SerializedName("code")
	val code: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("id")
	val id: Int? = null
)

data class SectorData(

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("id")
	val id: Int? = null
)

data class UserProfileData(

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
	val sectorData: SectorData? = null,

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
	val occupationData: OccupationData? = null,

	@field:SerializedName("last_name")
	val lastName: String? = null,

	@field:SerializedName("no_of_employees")
	val noOfEmployees: Any? = null,

	@field:SerializedName("organization_name")
	val organizationName: Any? = null,

	@field:SerializedName("cv")
	val cv: String? = null,

	@field:SerializedName("user_id")
	val userId: String? = null,

	@field:SerializedName("phone")
	val phone: String? = null
)
