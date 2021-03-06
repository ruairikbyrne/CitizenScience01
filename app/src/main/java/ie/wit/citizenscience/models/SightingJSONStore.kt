package ie.wit.citizenscience.models

import android.content.Context
import android.net.Uri
import com.google.gson.*
import com.google.gson.reflect.TypeToken
import ie.wit.citizenscience.helpers.*
import timber.log.Timber
import java.lang.reflect.Type
import java.util.*

const val JSON_FILE = "sightings.json"
val gsonBuilder: Gson = GsonBuilder().setPrettyPrinting()
    .registerTypeAdapter(Uri::class.java, UriParser())
    .create()
val listType: Type = object : TypeToken<ArrayList<SightingModel>>() {}.type

fun generateRandomId(): Long {
    return Random().nextLong()
}

class SightingJSONStore(private val context: Context) : SightingStore {

    var sightings = mutableListOf<SightingModel>()

    init {
        if (exists(context, JSON_FILE)) {
            deserialize()
        }
    }

    override fun findAll(): MutableList<SightingModel> {
        logAll()
        return sightings
    }

    override fun create(sighting: SightingModel) {
        sighting.id = generateRandomId()
        sightings.add(sighting)
        serialize()
    }

    override fun delete(sighting: SightingModel) {
        var foundSighting: SightingModel? = sightings.find { s -> s.id == sighting.id }
        if (foundSighting != null) {
            sightings.remove(foundSighting)
            serialize()
            logAll()
        }
    }


    override fun update(sighting: SightingModel) {
        var foundSighting: SightingModel? = sightings.find { s -> s.id == sighting.id }
        if (foundSighting != null) {
            foundSighting.classification = sighting.classification
            foundSighting.species = sighting.species
            foundSighting.image = sighting.image
            foundSighting.lat = sighting.lat
            foundSighting.lng = sighting.lng
            foundSighting.zoom = sighting.zoom
            logAll()
        }
    }

    private fun serialize() {
        val jsonString = gsonBuilder.toJson(sightings, listType)
        write(context, JSON_FILE, jsonString)
    }

    private fun deserialize() {
        val jsonString = read(context, JSON_FILE)
        sightings = gsonBuilder.fromJson(jsonString, listType)
    }

    private fun logAll() {
        sightings.forEach { Timber.i("$it") }
    }
}

class UriParser : JsonDeserializer<Uri>,JsonSerializer<Uri> {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): Uri {
        return Uri.parse(json?.asString)
    }

    override fun serialize(
        src: Uri?,
        typeOfSrc: Type?,
        context: JsonSerializationContext?
    ): JsonElement {
        return JsonPrimitive(src.toString())
    }
}