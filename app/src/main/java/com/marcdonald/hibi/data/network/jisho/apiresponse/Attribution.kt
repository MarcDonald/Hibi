/*
 * Copyright 2020 Marc Donald
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
package com.marcdonald.hibi.data.network.jisho.apiresponse

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Attribution(
	// API either returns boolean false or the String url of the dbpedia entry
	val dbpedia: Any,
	val jmdict: Boolean,
	val jmnedict: Boolean
)