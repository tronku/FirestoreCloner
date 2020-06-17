package tronku.project.firebasecloner.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [CollectionModel::class], version = 2)
abstract class RoomDB : RoomDatabase() {

    abstract fun collectionDao(): CollectionDao

    companion object {
        @Volatile private var instance: RoomDB? = null

        operator fun invoke(context: Context) = instance ?: synchronized(this) {
            instance ?: buildDatabase(context).also { instance = it }
        }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(context, RoomDB::class.java, "collections.db")
                .fallbackToDestructiveMigration().build()
    }

}