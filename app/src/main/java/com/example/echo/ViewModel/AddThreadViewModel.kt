import android.content.Context
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cloudinary.android.MediaManager
import com.cloudinary.android.callback.ErrorInfo
import com.cloudinary.android.callback.UploadCallback
import com.example.echo.models.ThreadModel
import com.example.echo.models.UserModel
import com.example.echo.utils.SharedPref
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class AddThreadViewModel : ViewModel() {
    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseDatabase.getInstance()
    private val userRef = db.getReference("Thread")

    private val _isPosted = MutableLiveData<Boolean>()
    val isPosted: LiveData<Boolean> = _isPosted

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

     fun saveImageToCloudinary(
        thread: String,
        userId: String?,
        imageUri: Uri
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
                        saveThreadData(thread, imageUrl, userId)
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

     fun saveThreadData(
        thread: String,
        imageUrl: String,
        userId: String,
    ) {
        val threadData = ThreadModel(thread,imageUrl,userId,System.currentTimeMillis().toString())
        userRef
            .child(userRef.push().key!!)
            .setValue(threadData)
            .addOnSuccessListener {
                _isPosted.postValue(true)
            }.addOnFailureListener {
                _isPosted.postValue(false)
            }
    }
}