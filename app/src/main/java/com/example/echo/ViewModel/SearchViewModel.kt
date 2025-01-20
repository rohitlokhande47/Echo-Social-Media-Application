import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.echo.models.ThreadModel
import com.example.echo.models.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class SearchViewModel : ViewModel() {

    private val db = FirebaseDatabase.getInstance()
    val users = db.getReference("users")

    // to get both data thread and user use pair
    private var _users = MutableLiveData<List<UserModel>>()
    var usersList: LiveData<List<UserModel>> = _users

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    init {
        fetchUsers {
            _users.value =it
        }
    }

    private fun fetchUsers(onResult: (List<UserModel>) -> Unit) {
        users.addValueEventListener(
            object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val result = mutableListOf<UserModel>()
                    for (threadSanpshot in snapshot.children) {
                        val thread = threadSanpshot.getValue(UserModel::class.java)
                     result.add(thread!!)
                    }
                    onResult(result)
                    Log.d("result",onResult.toString())
                }

                override fun onCancelled(error: DatabaseError) {
                    _error.postValue("Failed to fetch threads: ${error.message}")
                }
            },
        )
    }

    private fun fetchUserFromThread(
        thread: ThreadModel,
        onResult: (UserModel) -> Unit,
    ) {
        db
            .getReference("users")
            .child(thread.userId)
            .addListenerForSingleValueEvent(
                object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val user = snapshot.getValue(UserModel::class.java)
                        user?.let(onResult) // this will the value of user
                    }

                    override fun onCancelled(error: DatabaseError) {
                        _error.postValue("Failed to fetch user: ${error.message}")
                    }
                },
            )
    }
}
