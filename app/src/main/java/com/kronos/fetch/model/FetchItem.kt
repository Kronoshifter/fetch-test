package com.kronos.fetch.model

data class FetchItem(
  val id: Long,
  val listId: Long,
  val name: String? = null
)
