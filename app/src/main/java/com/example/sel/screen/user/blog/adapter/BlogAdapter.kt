package com.example.sel.screen.user.blog.adapter

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.sel.R
import com.example.sel.databinding.BlogItemBinding
import com.example.sel.screen.user.blog.ReadMoreActivity
import com.example.sel.screen.user.blog.model.BlogItemModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class BlogAdapter(private val items : MutableList<BlogItemModel>,private val userId: String):
RecyclerView.Adapter<BlogAdapter.BlogViewHolder>() {

    private val databaseReference: DatabaseReference = FirebaseDatabase
        .getInstance("https://blogapp-46ef8-default-rtdb.asia-southeast1.firebasedatabase.app").reference



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BlogAdapter.BlogViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = BlogItemBinding.inflate(inflater, parent, false)
        return BlogViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BlogAdapter.BlogViewHolder, position: Int) {
        val blogItem = items[position]
        holder.bind(blogItem,userId)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class BlogViewHolder(private val binding: BlogItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(blogItemModel: BlogItemModel ,userId: String) {
            val postId = blogItemModel.postId
            val context = binding.root.context
            binding.heading.text = blogItemModel.heading.toString()
            Glide.with(binding.profile.context)
                .load(blogItemModel.profileImage)
                .into(binding.profile)
            binding.username.text = blogItemModel.username.toString()
            binding.date.text = blogItemModel.date.toString()
            binding.post.text = blogItemModel.post.toString()
            binding.likecount.text = blogItemModel.likeCount.toString()

            binding.root.setOnClickListener {
                val context =binding.root.context
                val intent = Intent(context, ReadMoreActivity::class.java)
                intent.putExtra("postId",postId)
                intent.putExtra("blogItem",blogItemModel)
                context.startActivity(intent)
            }

            //check if the current user has liked the post and update image like
                val postLikeReference :DatabaseReference = databaseReference.child("blogs").child(postId).child("likes")

                postLikeReference.child(userId).addListenerForSingleValueEvent(object :
                    ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if(snapshot.exists())
                        {
                            binding.likebutton.setImageResource(R.drawable.heart_liked)
                        }else{
                            binding.likebutton.setImageResource(R.drawable.heart)
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {

                    }
                })


            binding.addBlogbutton.setOnClickListener {
                val context = it.context
                val intent = Intent(context,ReadMoreActivity::class.java)
                context.startActivity(intent)

            }

            //handles like button clicks
            binding.likebutton.setOnClickListener {
                if(userId!=null)
                {
                    handleLikeButtonClickd(postId,blogItemModel,binding)
                }
                else{
                    Toast.makeText(context,"You must login First", Toast.LENGTH_LONG).show()
                }
            }

        }
    }


    private fun handleLikeButtonClickd(postId: String, blogItemModel: BlogItemModel,binding: BlogItemBinding) {
        val userReference = databaseReference.child("users").child(userId)
        val postLikeReference = databaseReference.child("blogs").child(postId).child("likes")
        //User has already liked the post , so unlike it
        postLikeReference.child(userId)
            .addListenerForSingleValueEvent(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if(snapshot.exists())
                    {
                        userReference.child("likes").child(postId).removeValue()
                            .addOnSuccessListener {
                                postLikeReference.child(userId).removeValue()
                                blogItemModel.likedBy?.remove(userId)
                                updateLikeButton(binding,false)

                                //decrement the likes in data base
                                val newLikeCount = blogItemModel.likeCount-1
                                blogItemModel.likeCount = newLikeCount
                                databaseReference.child("blogs").child(postId).child("likeCount").setValue(newLikeCount)
                                notifyDataSetChanged()
                            }
                            .addOnFailureListener {e->
                                Log.e("LikedClick","Failed to unlike the blog $e", )

                            }
                    }
                    else{
                        //user not liked the post , so like it
                        userReference.child("likes").child(postId).setValue(true)
                            .addOnSuccessListener {
                                postLikeReference.child(userId).setValue(true)
                                blogItemModel.likedBy?.add(userId)
                                updateLikeButton(binding,true)

                                //increment the likes in data base
                                val newLikeCount = blogItemModel.likeCount+1
                                blogItemModel.likeCount = newLikeCount
                                databaseReference.child("blogs").child(postId).child("likeCount").setValue(newLikeCount)
                                notifyDataSetChanged()
                            }
                            .addOnFailureListener {e->
                                Log.e("LikedClick","Failed to like the blog $e", )

                            }

                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
    }
    private fun updateLikeButton(binding:BlogItemBinding,liked: Boolean) {
        if(liked )
        {
            binding.likebutton.setImageResource(R.drawable.heart)
        }else{
            binding.likebutton.setImageResource(R.drawable.heart_liked)
        }
    }

    fun updateData(saveBlogsArticles:List<BlogItemModel>) {
        items.clear()
        items.addAll(saveBlogsArticles)
        notifyDataSetChanged()
    }
}


