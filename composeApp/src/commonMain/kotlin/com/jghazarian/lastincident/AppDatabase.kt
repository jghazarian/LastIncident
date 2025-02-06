package com.jghazarian.lastincident

//import androidx.room.ConstructedBy
//import androidx.room.Dao
//import androidx.room.Database
//import androidx.room.Entity
//import androidx.room.Insert
//import androidx.room.PrimaryKey
//import androidx.room.Query
//import androidx.room.RoomDatabase
//import androidx.room.RoomDatabaseConstructor
import androidx.room.*
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDateTime
import androidx.compose.runtime.setValue

@Suppress("NO_ACTUAL_FOR_EXPECT")
internal expect object AppDatabaseConstructor : RoomDatabaseConstructor<AppDatabase> {
    override fun initialize(): AppDatabase
}

@Database(entities = [IncidentEntity::class], version = 1)
@ConstructedBy(AppDatabaseConstructor::class)
@TypeConverters(LocalDateTimeConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getDao(): IncidentDao
}

class LocalDateTimeConverter {
    @TypeConverter
    fun fromTimestamp(value: String?): LocalDateTime? {
        return value?.let { LocalDateTime.parse(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: LocalDateTime?): String? {
        return date?.toString()
    }
}

@Dao
interface IncidentDao {
    @Insert
    suspend fun insert(item: IncidentEntity)

    @Query("SELECT count(*) FROM IncidentEntity")
    suspend fun count(): Int

    @Query("SELECT * FROM IncidentEntity")
    fun getAllAsFlow(): Flow<List<IncidentEntity>>

    @Query("SELECT * FROM IncidentEntity WHERE id = :id")
    fun getIncidentFlow(id: Long): Flow<IncidentEntity>

    //TODO: get unique incident titles
    @Query("SELECT DISTINCT title From IncidentEntity")
    fun getUniqueTitlesFlow(): Flow<List<String>>

    @Query("SELECT DISTINCT title From IncidentEntity")
    suspend fun getUniqueTitles(): List<String>
}

@Entity
data class IncidentEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val content: String,
    val timeStamp: Long
)

//fun getRoomDatabase(
//    builder: RoomDatabase.Builder<AppDatabase>
//): AppDatabase {
//    return builder
////        .addMigrations(MIGRATIONS)
////        .fallbackToDestructiveMigrationOnDowngrade()
//        .setDriver(BundledSQLiteDriver())
//        .setQueryCoroutineContext(Dispatchers.IO)
//        .build()
//}

internal const val dbFileName = "my_room.db"