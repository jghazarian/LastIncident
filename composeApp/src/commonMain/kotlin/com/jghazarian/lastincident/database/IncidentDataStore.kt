package com.jghazarian.lastincident.database

import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.core.okio.OkioSerializer
import androidx.datastore.core.okio.OkioStorage
import co.touchlab.kermit.Logger
import com.jghazarian.lastincident.IncidentEntity
import com.jghazarian.lastincident.json
import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.Serializable
import okio.BufferedSink
import okio.BufferedSource
import okio.FileSystem
import okio.Path.Companion.toPath
import okio.SYSTEM
import okio.use


@Serializable
data class IncidentGroup(
    val items: List<GroupItem>
)

@Serializable
data class GroupItem(
    val id: Long,
    val count: Int,
)

internal object GroupJsonSerializer : OkioSerializer<IncidentGroup> {
    override val defaultValue: IncidentGroup = IncidentGroup(emptyList())
    override suspend fun readFrom(source: BufferedSource): IncidentGroup {
        return json.decodeFromString<IncidentGroup>(source.readUtf8())
    }

    override suspend fun writeTo(t: IncidentGroup, sink: BufferedSink) {
        sink.use {
            it.writeUtf8(json.encodeToString(IncidentGroup.serializer(), t))
        }
    }
}

class IncidentDataStore(
    private val produceFilePath: () -> String,
) {
    private val db: DataStore<IncidentGroup> = DataStoreFactory.create(
        storage = OkioStorage<IncidentGroup>(
            fileSystem = FileSystem.SYSTEM,
            serializer = GroupJsonSerializer,
            producePath = {
                produceFilePath().toPath()
            }
        )
    )

    val incidentGroup: Flow<IncidentGroup>
        get() = db.data
    suspend fun add(incidentEntity: IncidentEntity) = update(incidentEntity, 1)
    suspend fun remove(incidentEntity: IncidentEntity) = update(incidentEntity, -1)
    suspend fun update(incidentEntity: IncidentEntity, diff: Int) {
        db.updateData { prevGroup ->
            Logger.d("!!!!!!!!!start of updatedData from data store")
            val newItems = mutableListOf<GroupItem>()
            var found = false
            prevGroup.items.forEach {
                if (it.id == incidentEntity.id) {
                    found = true
                    Logger.d("Found1")
                    newItems.add(
                        it.copy(
                            count = it.count + diff,
                        ),
                    )
                } else {
                    Logger.d("Not Found1")
                    newItems.add(it)
                }
            }
            if (!found) {
                Logger.d("Not Found2")
                newItems.add(
                    GroupItem(id = incidentEntity.id, count = diff),
                )
            }
            Logger.d("Remove all")
            newItems.removeAll {
                it.count <= 0
            }
            IncidentGroup(
                items = newItems,
            )
        }
    }
}