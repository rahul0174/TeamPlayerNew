package com.cts.teamplayer.models

import com.google.gson.annotations.SerializedName

data class QuestionWithAnswerResponse(

	@field:SerializedName("data")
	val data: QuestionWithAnswerData? = null,

	@field:SerializedName("success")
	val success: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class AnswerGivenItemNew(

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

data class SectionDetailNew(

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

data class QuestionsItemNew(

	@field:SerializedName("question")
	val question: String? = null,

	/*@field:SerializedName("answer_given")
	val answerGiven: List<AnswerGivenItemNew?>? = null,
*/
	@field:SerializedName("section_detail")
	val sectionDetail: SectionDetailNew? = null,

	@field:SerializedName("answers")
	val answers: List<AnswersItemNew?>? = null,

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
	val minanswers: Int? = null,

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

data class AnswersItemNew(

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

data class QuestionWithAnswerData(

	@field:SerializedName("questions")
	val questions: List<QuestionsItemNew?>? = null,

	@field:SerializedName("sections")
	val sections: List<SectionsItemNew?>? = null
)

data class SectionsItemNew(

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
