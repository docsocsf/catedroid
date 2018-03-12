package uk.co.catedroid.app.data.model

import com.google.gson.annotations.SerializedName

class Note(val number: String, val title: String, val type: String, val size: String,
           val loaded: String, val owner: String, val hits: String, val url: String,
           val filekey: String,
           @field:SerializedName("module_number") val moduleNumber: String,
           @field:SerializedName("module_name") val moduleName: String) {

    override fun toString(): String {
        return "Note{" +
                "moduleNumber='" + moduleNumber + '\''.toString() +
                ", moduleName='" + moduleName + '\''.toString() +
                ", title='" + title + '\''.toString() +
                '}'.toString()
    }
}
