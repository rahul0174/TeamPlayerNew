package com.cts.teamplayer.models

data class CompatibilityReportResponse(
	val data: CompatibilityReportData? = null,
	val success: Boolean? = null,
	val message: String? = null
)

data class CompatibilityReportUserListItem(
	val score: String? = null,
	val im: String? = null,
	val sectionResult: List<Any?>? = null,
	val maxScore: String? = null,
	val name: String? = null,
	val id: String? = null
)

data class CompatibilityReportData(
	val sectionList: List<CompatibilityReportSectionListItemItem?>? = null,
	val userList: List<CompatibilityReportUserListItem?>? = null,
	val team: CompatibilityReportTeam? = null,
	val user: CompatibilityReportUser? = null
)

data class CompatibilityReportSectionListItemItem(
	val score: String? = null,
	val sectionName: String? = null,
	val name: String? = null
)

data class CompatibilityReportTeam(
	val name: String? = null
)

data class CompatibilityReportUser(
	val zip: String? = null,
	val image: String? = null,
	val country: String? = null,
	val occupation: String? = null,
	val im: String? = null,
	val city: String? = null,
	val lastName: String? = null,
	val noOfEmployees: Any? = null,
	val createdAt: String? = null,
	val organizationName: Any? = null,
	val title: String? = null,
	val userRole: Any? = null,
	val cv: String? = null,
	val updatedAt: String? = null,
	val userId: String? = null,
	val phone: String? = null,
	val addressLine1: String? = null,
	val id: String? = null,
	val addressLine2: String? = null,
	val state: String? = null,
	val firstName: String? = null,
	val sector: Any? = null,
	val email: String? = null
)

