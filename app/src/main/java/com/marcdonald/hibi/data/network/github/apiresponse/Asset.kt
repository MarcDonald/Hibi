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

data class Asset(
	val browser_download_url: String,
	val content_type: String,
	val created_at: String,
	val download_count: Int,
	val id: Int,
	val label: Any,
	val name: String,
	val node_id: String,
	val size: Int,
	val state: String,
	val updated_at: String,
	val uploader: Uploader,
	val url: String
)