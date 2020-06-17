package tronku.project.firebasecloner.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson
import kotlinx.coroutines.launch
import tronku.project.firebasecloner.db.CollectionModel
import tronku.project.firebasecloner.db.RoomDB

class CollectionViewModel : ViewModel() {

    private val gson by lazy { Gson() }

    private var isLoadingMutableLiveData = MutableLiveData(false)
    val isLoadingLiveData: LiveData<Boolean> = isLoadingMutableLiveData

    fun insertCollection(firestore: FirebaseFirestore, db: RoomDB, collectionName: String) {
        isLoadingMutableLiveData.postValue(true)
        firestore.collection("user/i4l80UF4KnX0En1x6TaWvGWXbew1/$collectionName").get().addOnCompleteListener {
            if (it.isSuccessful) {
                val map = HashMap<String, String>()
                val documents = it.result?.documents
                documents?.forEach { doc ->
                    map[doc.id] = gson.toJson(doc.data)
                }
                val collectionModel = CollectionModel(
                    collectionName = "user/i4l80UF4KnX0En1x6TaWvGWXbew1/$collectionName",
                    documentsData = gson.toJson(map)
                )
                viewModelScope.launch {
                    db.collectionDao().insertCollection(collectionModel)
                    isLoadingMutableLiveData.postValue(false)
                }
            } else {
                Log.e("ERROR", it.exception?.message.toString())
                isLoadingMutableLiveData.postValue(false)
            }
        }
    }

    fun putCollection(firestore: FirebaseFirestore, collectionList: List<CollectionModel>) {
        collectionList.forEach {
            val colName = it.collectionName
            val docs: HashMap<String, String> = gson.fromJson(it.documentsData, HashMap::class.java) as HashMap<String, String>
            docs.forEach {  doc ->
                val dataMap = gson.fromJson(doc.value, HashMap::class.java)
                firestore.collection(colName)
                    .document(doc.key)
                    .set(dataMap)
            }
        }

    }

    fun deleteCollection(collectionModel: CollectionModel, db: RoomDB) {
        isLoadingMutableLiveData.postValue(true)
        viewModelScope.launch {
            db.collectionDao().deleteCollection(collectionModel)
            isLoadingMutableLiveData.postValue(false)
        }
    }

}