package app.marcdev.nikki.data.network.apiresponse

data class SearchResponse(
  val `data`: List<Data>,
  val meta: Meta
)