/*
 * Copyright 2019 Marc Donald
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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