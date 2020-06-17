package tronku.project.firebasecloner.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "collection_table")
data class CollectionModel (
    @PrimaryKey(autoGenerate = true) var id: Int? = null,
    var collectionName: String,
    var documentsData: String
)