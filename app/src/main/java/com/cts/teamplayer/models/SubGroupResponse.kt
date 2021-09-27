package com.cts.teamplayer.models

import com.google.gson.annotations.SerializedName

data class SubGroupResponse(

	@field:SerializedName("data")
	val data: List<SubGroupList?>? = null,

	@field:SerializedName("success")
	val success: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class SubGroupList(

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("group_id")
	val groupId: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("user_list")
	val userList: List<UserListItem?>? = null
)

data class UserListItem(

	@field:SerializedName("survey_progress")
	val surveyProgress: String? = null,

	@field:SerializedName("user_type")
	val userType: String? = null,

	@field:SerializedName("updated_at")
	val updatedAt: Any? = null,

	@field:SerializedName("group_id")
	val groupId: String? = null,

	@field:SerializedName("user_id")
	val userId: String? = null,

	@field:SerializedName("subgroup_id")
	val subgroupId: String? = null,

	@field:SerializedName("user_name")
	val userName: String? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("email")
	val email: String? = null
)
