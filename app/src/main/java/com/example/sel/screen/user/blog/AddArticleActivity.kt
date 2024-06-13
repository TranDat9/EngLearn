package com.example.sel.screen.user.blog

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.viewModelScope
import com.example.sel.R
import com.example.sel.base.ApiService
import com.example.sel.base.model.ResponsePost
import com.example.sel.databinding.ActivityAddArticleBinding
import com.example.sel.screen.user.blog.model.BlogItemModel
import com.example.sel.screen.user.blog.model.UserData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date

class AddArticleActivity : AppCompatActivity() {
    private val binding : ActivityAddArticleBinding by lazy {
        ActivityAddArticleBinding.inflate(layoutInflater)
    }

        private lateinit var database: FirebaseDatabase

    private val databaseReference: DatabaseReference =
        FirebaseDatabase.getInstance("https://blogapp-46ef8-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("blogs")
    private val userReference: DatabaseReference =
        FirebaseDatabase.getInstance("https://blogapp-46ef8-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("users")
    private val auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        database = FirebaseDatabase.getInstance("https://blogapp-46ef8-default-rtdb.asia-southeast1.firebasedatabase.app")
        binding.imageButton.setOnClickListener {
            finish()
        }
        binding.addBlogbutton.setOnClickListener {
            // sử dụng add thì thong tin collection trên firebase

            val userReference = database.getReference("users")

            val userId = getSharedPreferences("MyPrefs", MODE_PRIVATE).getInt("USER_ID", 0)
            val userName = getSharedPreferences("MyPrefs", MODE_PRIVATE).getString("USER_NAME", "")
            val userEmail = getSharedPreferences("MyPrefs", MODE_PRIVATE).getString("USER_EMAIL", "")

            val userData = UserData(
                userName.toString(),
                userEmail.toString(),
                "https://firebasestorage.googleapis.com/v0/b/blogapp-46ef8.appspot.com/o/profile_image%2Fk%20(3).png?alt=media&token=5cb7bdae-4437-4913-8e3a-8f744f18714b"
            )
            userReference.child(userId.toString()).setValue(userData)


            val title :String = binding.blogTiltle.editText?.text.toString().trim()
            val description :String = binding.blogDescription.editText?.text.toString().trim()
            if(title.isEmpty() || description.isEmpty() )
            {
                Toast.makeText(this,"Fill all the fields", Toast.LENGTH_LONG).show()
            }

                userReference.child(userId.toString()).addListenerForSingleValueEvent(object :
                    ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val userData = snapshot.getValue(UserData::class.java)
                        if(userData !=null)
                        {
                            val userNameFromDB = userData.name
                            val userImageURLfromDB = userData.profileImage

                            val currentDate = SimpleDateFormat("yyy-MM-dd").format(Date())
                            //create a blogitemmodel
                            val blogItem = BlogItemModel(
                                title,
                                userNameFromDB,
                                currentDate,
                                description,
                                0,
                                userImageURLfromDB
                            )
                            //generate a unique key for the blog post
                            val key = databaseReference.push().key
                            if(key !=null)
                            {
                                blogItem.postId =key
                                val blogReference =databaseReference.child(key)
                                blogReference.setValue(blogItem).addOnCompleteListener {
                                    if(it.isSuccessful)
                                    {
                                        finish()
                                    }else{
                                        Toast.makeText(this@AddArticleActivity,"failed add blog",
                                            Toast.LENGTH_LONG).show()
                                    }
                                }
                            }


                        }
                    }

                    override fun onCancelled(error: DatabaseError) {

                    }

                })


        }
    }


}