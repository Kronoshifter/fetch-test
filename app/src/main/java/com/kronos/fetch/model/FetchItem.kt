package com.kronos.fetch.model

import kotlinx.serialization.Serializable

@Serializable
data class FetchItem(
  val id: Long,
  val listId: Long,
  val name: String? = null
)
