package it.unilix.json

class JsonString {
    companion object {
        @JvmStatic
        fun fromString(string: String): JsonObject {
            return JsonObject(string)
        }
    }
}