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

data class Author(
	val avatar_url: String,
	val events_url: String,
	val followers_url: String,
	val following_url: String,
	val gists_url: String,
	val gravatar_id: String,
	val html_url: String,
	val id: Int,
	val login: String,
	val node_id: String,
	val organizations_url: String,
	val received_events_url: String,
	val repos_url: String,
	val site_admin: Boolean,
	val starred_url: String,
	val subscriptions_url: String,
	val type: String,
	val url: String
)