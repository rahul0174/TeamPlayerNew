package com.cts.teamplayer.models

import com.google.gson.annotations.SerializedName

data class DemoQuestioneResponse(

	@field:SerializedName("data")
	val data: DemoQuestionData? = null,

	@field:SerializedName("success")
	val success: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class AnswerGivenItem(

	@field:SerializedName("image")
	val image: String? = null,

	@field:SerializedName("questionid")
	val questionid: String? = null,

	@field:SerializedName("answer")
	val answer: String? = null,

	@field:SerializedName("updated_at")
	val updatedAt: Any? = null,

	@field:SerializedName("created_at")
	val createdAt: Any? = null,

	@field:SerializedName("sortorder")
	val sortorder: String? = null,

	@field:SerializedName("answer_id")
	val answerId: String? = null,

	@field:SerializedName("status")
	val status: Boolean? = null
)

data class SectionDetail(

	@field:SerializedName("questionnaire")
	val questionnaire: String? = null,

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("detail")
	val detail: String? = null
)

data class QuestionsItem(

	@field:SerializedName("question")
	val question: String? = null,

	@field:SerializedName("answer_given")
	val answerGiven: List<AnswerGivenItem?>? = null,

	@field:SerializedName("section_detail")
	val sectionDetail: SectionDetail? = null,

	@field:SerializedName("answers")
	val answers: List<AnswersItem?>? = null,

	@field:SerializedName("created_at")
	val createdAt: Any? = null,

	@field:SerializedName("sectionid")
	val sectionid: String? = null,

	@field:SerializedName("maxanswers")
	val maxanswers: String? = null,

	@field:SerializedName("timelimit")
	val timelimit: String? = null,

	@field:SerializedName("updated_at")
	val updatedAt: Any? = null,

	@field:SerializedName("minanswers")
	val minanswers: String? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("savenegativeanswers")
	val savenegativeanswers: String? = null,

	@field:SerializedName("answer_saved")
	val answerSaved: Boolean? = null,

	@field:SerializedName("employerquestion")
	val employerquestion: String? = null,

	@field:SerializedName("subpart")
	val subpart: String? = null
)

data class AnswersItem(

	@field:SerializedName("image")
	val image: String? = null,

	@field:SerializedName("questionid")
	val questionid: String? = null,

	@field:SerializedName("answer")
	val answer: String? = null,

	@field:SerializedName("updated_at")
	val updatedAt: Any? = null,

	@field:SerializedName("created_at")
	val createdAt: Any? = null,

	@field:SerializedName("sortorder")
	val sortorder: String? = null,

	@field:SerializedName("answer_id")
	val answerId: String? = null
)

data class DemoQuestionData(

	@field:SerializedName("questions")
	val questions: List<QuestionsItem?>? = null,

	@field:SerializedName("sections")
	val sections: List<SectionsItem?>? = null
)

data class SectionsItem(

	@field:SerializedName("questionnaire")
	val questionnaire: String? = null,

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("detail")
	val detail: String? = null
)
