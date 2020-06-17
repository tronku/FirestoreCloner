package tronku.project.firebasecloner.db

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface CollectionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCollection(collection: CollectionModel)

    @Delete
    suspend fun deleteCollection(collection: CollectionModel)

    @Query("SELECT * from collection_table")
    fun getAllCollections(): LiveData<List<CollectionModel>>
}