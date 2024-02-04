import com.google.gson.Gson
import com.google.gson.internal.LinkedTreeMap
import it.unilix.json.JsonFile
import it.unilix.json.JsonObject

fun main(args: Array<String>) {
    val jsonFile = JsonFile("/home/giovanni/IdeaProjects/JSONlib/src/main/resources/test4.json")
    jsonFile.createIfNotExists()
    jsonFile.load()
}

fun Map<*,*>.toLinkedTreeMap(): LinkedTreeMap<String, Any?> {
    val map = LinkedTreeMap<String, Any?>()
    this.forEach { (key, value) ->
        map[key.toString()] = value
    }
    return map
}

fun Any?.print() {
    println(this)
}

