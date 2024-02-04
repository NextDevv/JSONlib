package it.unilix.json

import com.google.gson.GsonBuilder
import com.google.gson.internal.LinkedTreeMap
import toLinkedTreeMap
import java.util.HashMap

class JsonObject(private var value: Any? = null) {
    private val gson = GsonBuilder().setPrettyPrinting().create()

    private fun isMap(): Boolean {
        return value is Map<*, *> || (value is JsonObject && (value as JsonObject).value is Map<*, *>)
    }

    /**
     * Get the value of the key
     * @param key the key
     * @return the value of the key
     */
    operator fun get(key: String): JsonObject {
        if(!isMap()) {
            val map = linkedMapOf(key to linkedMapOf<String, Any?>())
            this.value = map
            return JsonObject(map)
        }
        var v = this.value
        if(v is JsonObject)
            v = (this.value as JsonObject).get()
        val map = try { (v as HashMap<String, Any?>).toLinkedTreeMap() } catch (_: Exception) { v as LinkedTreeMap<String, Any?> }
        if(!map.containsKey(key)) {
            map[key] = linkedMapOf<String, Any?>().toLinkedTreeMap()
        }
        this.value = map
        return JsonObject(map[key] ?: linkedMapOf<String, Any?>().toLinkedTreeMap())
    }

    /**
     * Get the value of the index
     * @param index the index
     * @return the value of the index
     */
    operator fun get(index: Int): JsonObject {
        if(value is List<*>) {
            if((value as List<*>).isEmpty())
                value = arrayListOf<Any?>()
            if((value as List<*>).size <= index)
                (value as ArrayList<Any?>).add(index, linkedMapOf<String, Any?>().toLinkedTreeMap())
            return JsonObject((value as List<*>)[index])
        }
        return JsonObject(null)
    }

    operator fun set(key: String, value: Any?) {
        if(isMap()) {
            var v = this.value
            if(v is JsonObject)
                v = (this.value as JsonObject).get()
            (v as LinkedTreeMap<String, Any>)[key] = value
            this.value = v
        }else {
            val map = linkedMapOf(key to value)
            this.value = map
        }
    }

    private fun isStringAMap(string: String): Boolean {
        return string.startsWith("{") && string.endsWith("}")
    }

    operator fun set(index: Int, value: Any) {
        if(this.value is List<*>) {
            (this.value as ArrayList<Any>)[index] = value
        }
    }

    fun get(key: String, default: Any?): Any? {
        return (value as Map<*, *>).getOrDefault(key, default)
    }

    fun get(): Any? {
        return value
    }

    fun isNull(): Boolean {
        return value == null
    }

    operator fun plus(other: JsonObject): JsonObject {
        if(isMap() && other.isMap()) {
            val map = value as Map<*, *>
            val otherMap = other.value as Map<*, *>
            val newMap = LinkedHashMap(map)
            newMap.putAll(otherMap)
            return JsonObject(newMap)
        }
        return JsonObject(null)
    }

    operator fun minus(other: JsonObject): JsonObject {
        if(isMap() && other.isMap()) {
            val map = value as Map<*, *>
            val otherMap = other.value as Map<*, *>
            val newMap = LinkedHashMap(map)
            newMap.keys.removeAll(otherMap.keys)
            return JsonObject(newMap)
        }
        return JsonObject(null)
    }

    operator fun plus(number: Number): JsonObject {
        if(value is Number) {
            return JsonObject((value as Number).toDouble() + number.toDouble())
        }
        return JsonObject(null)
    }

    operator fun minus(number: Number): JsonObject {
        if(value is Number) {
            return JsonObject((value as Number).toDouble() - number.toDouble())
        }
        return JsonObject(null)
    }

    operator fun times(number: Number): JsonObject {
        if(value is Number) {
            return JsonObject((value as Number).toDouble() * number.toDouble())
        }
        return JsonObject(null)
    }

    operator fun div(number: Number): JsonObject {
        if(value is Number) {
            return JsonObject((value as Number).toDouble() / number.toDouble())
        }
        return JsonObject(null)
    }

    fun toJsonString(): String {
        return gson.toJson(value)
    }

    override fun toString(): String {
        return gson.toJson(value)
    }

    operator fun plus(hashMapOf: Map<String, String>): JsonObject {
        if(isMap()) {
            val map = value as Map<*, *>
            val newMap = LinkedHashMap(map)
            newMap.putAll(hashMapOf)
            return JsonObject(newMap)
        }
        return JsonObject(null)
    }

    fun <T> to(type: Class<T>): T {
        return gson.fromJson(gson.toJson(value), type)
    }

    operator fun plus(listOf: List<Int>): JsonObject {
        if(value is List<*>) {
            val list = value as List<*>
            val newList = ArrayList(list)
            newList.addAll(listOf)
            return JsonObject(newList)
        }
        return JsonObject(null)
    }

    override fun hashCode(): Int {
        return value.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as JsonObject

        return value == other.value
    }
}