package org.jetbrains.hyperskill.network

sealed class ResponseResult {
    class Success(val info: String): ResponseResult()
    class Failure(val errorMessage: String): ResponseResult()
}
