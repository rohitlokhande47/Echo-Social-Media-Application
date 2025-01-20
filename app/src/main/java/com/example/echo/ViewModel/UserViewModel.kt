import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.echo.models.ThreadModel
import com.example.echo.models.UserModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class UserViewModel : ViewModel() {
    private val db = FirebaseDatabase.getInstance()
    val threadRef = db.getReference("Thread")
    val userRef = db.getReference("users")

    private val _threads = MutableLiveData(listOf<ThreadModel>())

    val threads: LiveData<List<ThreadModel>> get() = _threads

    private val _followerList = MutableLiveData(listOf<String>())

    val followerList: LiveData<List<String>> get() = _followerList

    private val _followingList = MutableLiveData(listOf<String>())

    val followingList: LiveData<List<String>> get() = _followingList

    private var _users = MutableLiveData(UserModel())

    val users: LiveData<UserModel> get() = _users

    fun fetchUser(userId: String) {
        userRef.child(userId).addListenerForSingleValueEvent(
            object : ValueEventListener {
                @SuppressLint("SuspiciousIndentation")
                override fun onDataChange(snapshot: DataSnapshot) {
                    val user = snapshot.getValue(UserModel::class.java)
                    _users.postValue(user)
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            },
        )
    }

    fun fetchThreads(userId: String) {
        threadRef.orderByChild("userId").equalTo(userId).addListenerForSingleValueEvent(
            object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val threadList =
                        snapshot.children.mapNotNull {
                            it.getValue(ThreadModel::class.java)
                        }
                    _threads.postValue(threadList)
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            },
        )
    }

    val firestoredb = Firebase.firestore

    fun followUsers(
        userId: String,
        currenUsertId: String,
    ) {
        val ref = firestoredb.collection("following").document(currenUsertId)
        val followref = firestoredb.collection("followers").document(userId)

        ref.update("followingIds", FieldValue.arrayUnion(userId))
        followref.update("followerIds", FieldValue.arrayUnion(currenUsertId))
    }

    fun getFollwers(userId: String) {
        firestoredb
            .collection("followers")
            .document(userId)
            .addSnapshotListener { value, error ->

                val followerIds = value?.get("followerIds")as? List<String> ?: listOf()
                _followerList.postValue(followerIds)
            }
    }

    fun getFollowing(userId: String) {
        firestoredb
            .collection("following")
            .document(userId)
            .addSnapshotListener { value, error ->

                val followerIds = value?.get("followingIds")as? List<String> ?: listOf()
                _followingList.postValue(followerIds)
            }
    }

    fun unfollowUsers(userId: String, currentUserId: String) {
        val ref = firestoredb.collection("following").document(currentUserId)
        val followRef = firestoredb.collection("followers").document(userId)

        ref.update("followingIds", FieldValue.arrayRemove(userId))
        followRef.update("followerIds", FieldValue.arrayRemove(currentUserId))
    }
}
