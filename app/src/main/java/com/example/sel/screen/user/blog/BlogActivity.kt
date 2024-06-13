package com.example.sel.screen.user.blog

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.sel.base.model.HistoryExam
import com.example.sel.databinding.ActivityBlogBinding
import com.example.sel.screen.user.HistoryActivity
import com.example.sel.screen.user.blog.adapter.BlogAdapter
import com.example.sel.screen.user.blog.model.BlogItemModel
import com.example.sel.screen.user.home.HomeActivity
import com.example.sel.screen.user.profile.ProfileActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class BlogActivity : AppCompatActivity() {
    private val  binding : ActivityBlogBinding by lazy {
        ActivityBlogBinding.inflate(layoutInflater)
    }

    private  lateinit var databaseReference: DatabaseReference
    private val blogItems = mutableListOf<BlogItemModel>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        //

        val userId = getSharedPreferences("MyPrefs", MODE_PRIVATE).getInt("USER_ID", 0)
        val userName = getSharedPreferences("MyPrefs", MODE_PRIVATE).getString("USER_NAME", "")

        binding.profileImage.setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
        }
        binding.navimgBlog.setOnClickListener {
            startActivity(Intent(this, HistoryActivity::class.java))
        }
        binding.navimgHome.setOnClickListener {
            startActivity(Intent(this, HomeActivity::class.java))
        }
//
//        //go to save acticity
//        binding.saveArticleButton.setOnClickListener {
//            startActivity(Intent(this,SaveArticlesActivity::class.java))
//        }

        databaseReference = FirebaseDatabase
            .getInstance("https://blogapp-46ef8-default-rtdb.asia-southeast1.firebasedatabase.app")
            .reference.child("blogs")

        if(userId!=null)
        {
            loadUserProfileImage(userId.toString());
        }
        // set blogs post in to recycle view

        //initialize the recycle view and adapter
        val recyclerView = binding.blogRecycleview
        val blogAdapter = BlogAdapter(blogItems,userId.toString())
        recyclerView.adapter = blogAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)
        // fetch data from data base

        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                blogItems.clear()
                for(snapshot in snapshot.children)
                {
                    val blogItem = snapshot.getValue(BlogItemModel::class.java)
                    if(blogItem !=null)
                    {
                        blogItems.add(blogItem)
                    }
                }
                //revers blog
                blogItems.reverse()
                // notify data change
                blogAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@BlogActivity,"Blog Data load failed", Toast.LENGTH_LONG).show()
            }
        })

        binding.floatingAddArticleButton.setOnClickListener {

            Toast.makeText(this ,userId.toString()+userName.toString(),Toast.LENGTH_LONG).show()
           startActivity(Intent(this,AddArticleActivity::class.java))

        }
    }

    private fun loadUserProfileImage(userId: String) {
        val userReference = FirebaseDatabase.getInstance("https://blogapp-46ef8-default-rtdb.asia-southeast1.firebasedatabase.app").reference.child("users").child(userId)
        userReference.child("profileImage").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val profileImageUrl = snapshot.getValue(String::class.java)
                if(profileImageUrl!=null)
                {
                    Glide.with(this@BlogActivity)
                        .load(profileImageUrl)
                        .into(binding.profile)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@BlogActivity,"Error loading profile image", Toast.LENGTH_LONG).show()
            }
        })

    }

}