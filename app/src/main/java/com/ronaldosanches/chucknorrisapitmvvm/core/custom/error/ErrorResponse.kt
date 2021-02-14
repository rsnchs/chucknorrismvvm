package com.ronaldosanches.chucknorrisapitmvvm.core.custom.error

data class ErrorResponse(
    val error_description: String,
    val causes: Map<String, String> = emptyMap()
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ErrorResponse

        if (error_description != other.error_description) return false
        if (causes != other.causes) return false

        return true
    }

    override fun hashCode(): Int {
        var result = error_description.hashCode()
        result = 31 * result + causes.hashCode()
        return result
    }
}