package io.gnosis.data.models

data class Page<T>(
    val count: Int?,
    val next: String?,
    val previous: String?,
    val results: List<T>
)

inline fun <T, R> Page<T>.foldInner(folder: (item: T, MutableList<R>) -> MutableList<R>): Page<R> =
    Page(count, next, previous, results.fold(mutableListOf(), { acc, item -> folder(item, acc) }))
