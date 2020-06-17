package tronku.project.firebasecloner.activity

import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.add_collection_layout.*
import tronku.project.firebasecloner.CollectionAdapter
import tronku.project.firebasecloner.R
import tronku.project.firebasecloner.db.CollectionModel
import tronku.project.firebasecloner.db.RoomDB
import tronku.project.firebasecloner.isUpdate
import tronku.project.firebasecloner.setUpdateValue
import tronku.project.firebasecloner.viewmodel.CollectionViewModel


class MainActivity : AppCompatActivity(), CollectionAdapter.CollectionClickListener {

    private val firestore by lazy { FirebaseFirestore.getInstance() }

    private val db by lazy { RoomDB(this) }
    private var collectionList = listOf<CollectionModel>()

    private val viewModel by lazy { CollectionViewModel() }
    private val adapter by lazy { CollectionAdapter(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        init()
        setListeners()
    }

    private fun setListeners() {
        addButton.setOnClickListener {
            if (isUpdate) {
                setUpdateValue(false)
                Toast.makeText(this, "Restart the app", Toast.LENGTH_SHORT).show()
            } else {
                showAddDialog()
            }
        }

        updateButton.setOnClickListener {
            if (isUpdate) {
                viewModel.putCollection(firestore, collectionList)
            } else {
                setUpdateValue(true)
                Toast.makeText(this, "Restart the app", Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.isLoadingLiveData.observe(this, Observer {
            loadingLayout.visibility = if (it) View.VISIBLE else View.GONE
        })

        db.collectionDao().getAllCollections().observe(this, Observer {
            if (it.isNotEmpty()) {
                collectionList = it
                adapter.updateList(collectionList)
            }
        })
    }

    private fun showAddDialog() {
        val dialog = Dialog(this, R.style.DialogTheme)

        dialog.setContentView(R.layout.add_collection_layout)
        val layoutParams = dialog.window!!.attributes
        dialog.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT
        )
        dialog.window?.attributes = layoutParams

        dialog.fetchButton.setOnClickListener {
            val collectionName = dialog.collectionEditText.text.toString()
            if (collectionName.isNotEmpty()) {
                viewModel.insertCollection(firestore, db, collectionName)
            } else {
                Toast.makeText(this@MainActivity, "Enter a collection name", Toast.LENGTH_SHORT).show()
            }
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun init() {
        FirebaseApp.initializeApp(applicationContext)
        if (isUpdate) {
            val settings = FirebaseFirestoreSettings.Builder()
                .setHost("10.0.2.2:8080")
                .setSslEnabled(false)
                .setPersistenceEnabled(false)
                .build()
            firestore.firestoreSettings = settings
        }

        collectionRecyclerView.layoutManager = LinearLayoutManager(this)
        collectionRecyclerView.adapter = adapter
    }

    override fun onClicked(collectionModel: CollectionModel) {
        viewModel.deleteCollection(collectionModel, db)
    }
}