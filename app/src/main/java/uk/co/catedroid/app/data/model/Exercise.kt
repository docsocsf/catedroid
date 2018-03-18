package uk.co.catedroid.app.data.model

import com.google.gson.annotations.SerializedName

class Exercise(val code: String?, val name: String?, val start: String?, val end: String?,
               @field:SerializedName("assessed_status") val assessedStatus: String?,
               @field:SerializedName("submission_status") val submissionStatus: String?,
               @field:SerializedName("module_number") val moduleNumber: String?,
               @field:SerializedName("module_name") val moduleName: String?,
               @field:SerializedName("spec_key") val specKey: String?,
               val links: Map<String, String>?)