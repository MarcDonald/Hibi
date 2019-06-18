package app.marcdev.hibi.data.network.jisho.apiresponse

import com.google.gson.annotations.SerializedName

data class Sense(
  val antonyms: List<Any>,
  @SerializedName("english_definitions")
  val englishDefinitions: List<String>,
  val info: List<Any>,
  val links: List<Any>,
  @SerializedName("parts_of_speech")
  val partsOfSpeech: List<String>,
  val restrictions: List<Any>,
  @SerializedName("see_also")
  val seeAlso: List<Any>,
  val source: List<Any>,
  val tags: List<Any>
)