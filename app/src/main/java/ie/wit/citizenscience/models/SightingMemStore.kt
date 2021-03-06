package ie.wit.citizenscience.models

import timber.log.Timber.i

var lastId = 0L

internal fun getId(): Long {
    return lastId++
}

class SightingMemStore : SightingStore {
    val sightings = ArrayList<SightingModel>()

    override fun findAll(): List<SightingModel> {
        return sightings
    }

    override fun create(sighting: SightingModel) {
        sighting.id = getId()
        sightings.add(sighting)
        logAll()
    }

    override fun delete(sighting: SightingModel) {
        var foundSighting: SightingModel? = sightings.find { s -> s.id == sighting.id }
        if (foundSighting != null) {
            sightings.remove(foundSighting)
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

    fun logAll() {
        sightings.forEach{ i("${it}") }
    }
}