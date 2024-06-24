package com.example.sel.screen.user.blog

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.sel.R
import com.example.sel.databinding.ActivityReadMoreBinding
import com.example.sel.screen.user.blog.adapter.CommentAdapter
import com.example.sel.screen.user.blog.model.BlogItemModel
import com.example.sel.screen.user.blog.model.CommentItem

import com.example.sel.screen.user.blog.model.UserData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

import java.text.SimpleDateFormat
import java.util.Date


class ReadMoreActivity : AppCompatActivity() {
    private val binding : ActivityReadMoreBinding by lazy {
        ActivityReadMoreBinding.inflate(layoutInflater)
    }

    private val databaseReference: DatabaseReference =
        FirebaseDatabase.getInstance("https://blogapp-46ef8-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("blogs")



    private val userReference: DatabaseReference =
        FirebaseDatabase.getInstance("https://blogapp-46ef8-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("users")
    private val auth = FirebaseAuth.getInstance()

   private val cmtitems = mutableListOf<CommentItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val blogItem = intent.getParcelableExtra<BlogItemModel>("blogItem")
        val postId = intent.getStringExtra("postId")
         //Toast.makeText(this,postId.toString(),Toast.LENGTH_LONG ).show()
        if (blogItem!=null)
        {
            binding.txtRMheading.text = blogItem.heading.toString()
            binding.txtRMpost.text = blogItem.post.toString()
            if (blogItem.profileImage !=null)
            {
                Glide.with(this@ReadMoreActivity)
                    .load(blogItem.profileImage)
                    .into(binding.imgRMProfile)
            }
        }

        //initialize the recycle view and adapter
        val userId = getSharedPreferences("MyPrefs", MODE_PRIVATE).getInt("USER_ID", 0)
        val recyclerView = binding.rcvCmt
        val cmtAdapter = CommentAdapter(cmtitems,postId.toString(),userId.toString())
        recyclerView.adapter = cmtAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        // fetch data from data base
        val CmtReference: DatabaseReference =databaseReference.child(postId.toString()).child("comments")
        CmtReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                cmtitems.clear()
                for(snapshot in snapshot.children)
                {
                    val cmtItem = snapshot.getValue(CommentItem::class.java)
                    if(cmtItem !=null)
                    {
                        cmtitems.add(cmtItem)
                    }
                }
                //revers blog
                cmtitems.reverse()
                // notify data change
                cmtAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@ReadMoreActivity," load failed", Toast.LENGTH_LONG).show()
            }
        })

        binding.backButton.setOnClickListener {
            finish()
        }
        binding.btnCancelCmt.setOnClickListener {
            binding.txtAddCmt.setText("")

        }
        binding.btnAddCmt.setOnClickListener {
            val contentCmt :String = binding.txtAddCmt.text.toString().trim()

            val userId = getSharedPreferences("MyPrefs", MODE_PRIVATE).getInt("USER_ID", 0)
            val userName = getSharedPreferences("MyPrefs", MODE_PRIVATE).getString("USER_NAME", "")

            if(userId!=null) {
                userReference.child(userId.toString()).addListenerForSingleValueEvent(object :
                    ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val userData = snapshot.getValue(UserData::class.java)
                        if(userData !=null)
                        {
                            val userNameFromDB = userData.name
                            val userImageURLfromDB = userData.profileImage
                            val currentDate = SimpleDateFormat("yyy-MM-dd").format(Date())
                            //create a cmt item
                            val cmtItem =CommentItem (
                                contentCmt,
                                currentDate,
                                userImageURLfromDB,
                                userNameFromDB,
                                userId.toString()

                            )
                            //generate a unique key for the blog post
                            val key = CmtReference.push().key
                            if(key !=null)
                            {
                                cmtItem.commentId =key
                                val blogReference =CmtReference.child(key)
                                blogReference.setValue(cmtItem).addOnCompleteListener {
                                    if(it.isSuccessful)
                                    {
                                        binding.txtAddCmt.setText("")
                                    }else{
                                        Toast.makeText(this@ReadMoreActivity,"failed add cmt",
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
}