package com.mritunjay.talkdesk.data.remote

enum class HttpError(val errorCode: Int, val errorString: String) {
    BAD_REQUEST(400, "Bad request"),
    NOT_FOUND(404, "Not found"),
    INTERNAL_SERVER_ERROR(500, "Internal server error")
}

sealed interface ApiResponse<out T> {
    data class Error(val httpError: HttpError) : ApiResponse<Nothing>
    data class Success<T>(val data: T) : ApiResponse<T>
}