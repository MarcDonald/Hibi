package com.marcdonald.hibi.data.network.jisho.apiresponse

import com.google.gson.annotations.SerializedName

data class Data(
	val attribution: Attribution,
	@SerializedName("is_common")
	val isCommon: Boolean,
	val japanese: List<Japanese>,
	val senses: List<Sense>,
	val tags: List<String>
)