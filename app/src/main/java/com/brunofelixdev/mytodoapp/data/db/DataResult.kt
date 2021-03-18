package com.brunofelixdev.mytodoapp.data.db

sealed class DataResult<T>(val data: T?, val message: String?) {
    class Success<T>(data: T) : DataResult<T>(data, null)
    class Error<T>(message: String) : DataResult<T>(null, message)
}