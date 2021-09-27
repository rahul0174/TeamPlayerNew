package com.cts.teamplayer.models

import com.google.gson.annotations.SerializedName

data class QuestionnaireGroupDetailResponse(

	@field:SerializedName("data")
	val data: QuestionnaireGroupData? = null,

	@field:SerializedName("success")
	val success: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class SurveyGroup(

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

data class QuestionnaireGroupData(

	@field:SerializedName("is_admin")
	val isAdmin: Boolean? = null,

	@field:SerializedName("survey_group")
	val surveyGroup: SurveyGroup? = null,

	@field:SerializedName("survey_participants")
	val surveyParticipants: List<SurveyParticipantsItem?>? = null
)

data class SurveyParticipantsItem(

	@field:SerializedName("survey_progress")
	val surveyProgress: String? = null,

	@field:SerializedName("access")
	val access: String? = null,

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("profile_id")
	val profileId: String? = null,

	@field:SerializedName("user_name")
	val userName: String? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("survey_group_id")
	val surveyGroupId: String? = null,

	@field:SerializedName("email")
	val email: String? = null
)
