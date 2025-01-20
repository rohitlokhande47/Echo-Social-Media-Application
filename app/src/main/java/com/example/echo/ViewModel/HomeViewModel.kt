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

class HomeViewModel : ViewModel() {
    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseDatabase.getInstance()
    val thread = db.getReference("Thread")

    // to get both data thread and user use pair
    private var _threadsandUsers = MutableLiveData<List<Pair<ThreadModel, UserModel>>>()
    var threadsandUsers: LiveData<List<Pair<ThreadModel, UserModel>>> = _threadsandUsers

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    init {
        fetchThreadsandUsers {
            _threadsandUsers.value = it
        }
    }

    private fun fetchThreadsandUsers(onResult: (List<Pair<ThreadModel, UserModel>>) -> Unit) {
        thread.addValueEventListener(
            object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val result = mutableListOf<Pair<ThreadModel, UserModel>>()
                    for (threadSanpshot in snapshot.children) {
                        val thread = threadSanpshot.getValue(ThreadModel::class.java)
                        thread.let {
                            fetchUserFromThread(it!!) { user ->
                                result.add(0, it to user)

                                if (result.size == snapshot.childrenCount.toInt()) {
                                    onResult(result)
                                }
                            }
                        }
                    }
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
