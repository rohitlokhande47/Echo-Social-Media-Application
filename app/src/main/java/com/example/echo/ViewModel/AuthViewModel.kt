import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cloudinary.android.MediaManager
import com.cloudinary.android.callback.ErrorInfo
import com.cloudinary.android.callback.UploadCallback
import com.example.echo.models.UserModel
import com.example.echo.utils.SharedPref
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class AuthViewModel : ViewModel() {
    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseDatabase.getInstance()
    private val userRef = db.getReference("users")

    private val _firebaseUser = MutableLiveData<FirebaseUser>()
    val firebaseUser: LiveData<FirebaseUser> = _firebaseUser

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    init {
        _firebaseUser.value = auth.currentUser
    }

    fun login(
        email: String,
        password: String,
        context: Context,
    ) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
            if (it.isSuccessful) {
                _firebaseUser.postValue(auth.currentUser)

                getData(auth.currentUser!!.uid, context)
            } else {
                _error.postValue(it.exception!!.message)
            }
        }
    }

    private fun getData(
        uid: String,
        context: Context,
    ) {

        userRef.child(uid).addListenerForSingleValueEvent(
            object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val userData = snapshot.getValue(UserModel::class.java)
                    val imageUrl = userData?.imageUrl ?: ""
                    SharedPref.storeData(userData!!.name, userData.email, userData.bio, userData.userName, imageUrl, context)
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            },
        )
    }

    fun register(
        email: String,
        name: String,
        bio: String,
        password: String,
        userName: String,
        imageUri: Uri,
        context: Context,
    ) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
            if (it.isSuccessful) {
                val userId = auth.currentUser?.uid ?: return@addOnCompleteListener
                _firebaseUser.postValue(auth.currentUser)
                saveImageToCloudinary(email, name, bio, userName, imageUri, userId, context)
                Log.d("savUserId",userId)
            } else {
                _error.postValue("Something went wrong")
            }
        }
    }

    private fun saveImageToCloudinary(
        email: String,
        name: String,
        bio: String,
        userName: String,
        imageUri: Uri,
        userId: String?,
        context: Context,
    ) {
        if (userId == null) {
            _error.postValue("User ID is null")
            return
        }

        MediaManager
            .get()
            .upload(imageUri)
            .callback(
                object : UploadCallback {
                    override fun onStart(requestId: String) {
                        // Upload started
                    }

                    override fun onProgress(
                        requestId: String,
                        bytes: Long,
                        totalBytes: Long,
                    ) {
                        // Upload in progress
                    }

                    override fun onSuccess(
                        requestId: String,
                        resultData: Map<*, *>,
                    ) {
                        val imageUrl = resultData["url"] as String
                        saveUserData(email, name, bio, userName, imageUrl, userId, context)
                    }

                    override fun onError(
                        requestId: String,
                        error: ErrorInfo,
                    ) {
                        _error.postValue("Image upload failed: ${error.description}")
                    }

                    override fun onReschedule(
                        requestId: String,
                        error: ErrorInfo,
                    ) {
                        _error.postValue("Image upload rescheduled: ${error.description}")
                    }
                },
            ).dispatch()
    }

    private fun saveUserData(
        email: String,
        name: String,
        bio: String,
        userName: String,
        imageUrl: String,
        userId: String,
        context: Context,
    ) {


        val firestoredb= Firebase.firestore
        val followerRef = firestoredb.collection("followers").document(userId)
        val followeingRef = firestoredb.collection("following").document(userId)

        followeingRef.set(mapOf("followingID's" to listOf<String>()))
        followerRef.set(mapOf("followersID's" to listOf<String>()))
        val userData = UserModel(email, name, bio, userName, imageUrl, userId)
        userRef
            .child(userId)
            .setValue(userData)
            .addOnSuccessListener {
                SharedPref.storeData(
                    name,
                    email,
                    bio,
                    userName,
                    imageUrl,
                    context,
                )
            }.addOnFailureListener {}
    }

    fun Logout() {
        auth.signOut()
        _firebaseUser.postValue(null as FirebaseUser?)
    }
}
