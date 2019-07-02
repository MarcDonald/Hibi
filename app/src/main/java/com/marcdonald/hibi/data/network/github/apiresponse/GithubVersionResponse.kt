package com.marcdonald.hibi.data.network.github.apiresponse

data class GithubVersionResponse(
	val assets: List<Asset>,
	val assets_url: String,
	val author: Author,
	val body: String,
	val created_at: String,
	val draft: Boolean,
	val html_url: String,
	val id: Int,
	val name: String,
	val node_id: String,
	val prerelease: Boolean,
	val published_at: String,
	val tag_name: String,
	val tarball_url: String,
	val target_commitish: String,
	val upload_url: String,
	val url: String,
	val zipball_url: String
)