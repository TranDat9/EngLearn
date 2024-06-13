package com.example.sel.screen.user.blog.adapter

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context.MODE_PRIVATE
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.sel.R
import com.example.sel.databinding.ItemCommentBinding
import com.example.sel.screen.user.blog.model.CommentItem
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import org.w3c.dom.Comment


class CommentAdapter(private val itemCmts : MutableList<CommentItem>, private val postId: String,private val userId :String) : RecyclerView.Adapter<CommentAdapter.CommetViewHolder>()
{
    private val databaseReference: DatabaseReference =
        FirebaseDatabase.getInstance("https://blogapp-46ef8-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("blogs")

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommetViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemCommentBinding.inflate(inflater,parent,false)
        return CommetViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return  itemCmts.size
    }

    override fun onBindViewHolder(holder: CommetViewHolder, position: Int) {
        val cmtItem =itemCmts[position]
        holder.bind(cmtItem,postId,userId)
    }


    inner class CommetViewHolder(private val binding: ItemCommentBinding):
        RecyclerView.ViewHolder(binding.root) {
        fun bind(commentModel: CommentItem,postId: String,userId:String) {
            binding.txtCmtText.text = commentModel.comment.toString()
            binding.txtCmtDate.text = commentModel.date.toString()
            binding.txtCmtname.text = commentModel.username.toString()
            Glide.with(binding.imgComtProfile.context)
                .load(commentModel.profileImage)
                .into(binding.imgComtProfile)

            binding.btnCmtOption.setOnClickListener {

                val builder = AlertDialog.Builder(it.context)

                // Thiết lập tiêu đề cho AlertDialog
                //builder.setTitle("Chọn một hành động")
                // Thiết lập các tùy chọn cho AlertDialog
                val options = arrayOf("Delete","Edit")
                val context =binding.root.context
                builder.setItems(options) { dialog, which ->
                    when (which) {
                        0 -> {
                            val commentId = commentModel.commentId
                            val CmtReference: DatabaseReference =databaseReference.child(postId.toString()).child("comments").child(commentId)

                            CmtReference.get().addOnSuccessListener { dataSnapshot ->
                                if (dataSnapshot.exists()) {
                                    val userIdFromDB = dataSnapshot.child("userId") .getValue(String::class.java)
                                    Toast.makeText(context , userIdFromDB,Toast.LENGTH_LONG).show()
                                    // Do something with the userId
                                    if(userId == userIdFromDB)
                                    {
                                        CmtReference.removeValue().addOnCompleteListener { task ->
                                    if (task.isSuccessful) {
                                        // Xóa thành công
                                    } else {
                                        // Xử lý lỗi
                                    }
                                }
                                    }

                                } else {
                                    Toast.makeText(context , "nodata",Toast.LENGTH_LONG).show()
                                }
                            }.addOnFailureListener { exception ->
                                // Handle possible errors
                                Log.w("FirebaseData", "Error getting data", exception)
                            }


                        }
                        1 -> {
                            val commentId = commentModel.commentId
                            val CmtReference: DatabaseReference = databaseReference.child(postId.toString()).child("comments").child(commentId)
//                            CmtReference.child("comment").setValue("hello").addOnCompleteListener { task ->
//                                if (task.isSuccessful) {
//                                    // Chỉnh sửa thành công
//                                } else {
//                                    // Xử lý lỗi
//                                }
//                            }
                            // Tạo một EditText
                            val input = EditText(context)

                            // Tạo một AlertDialog để nhập nội dung comment mới
//                            val dialog = AlertDialog.Builder(context)
//                                .setTitle("Edit comment")
//                                .setMessage("Nhập nội dung comment mới:")
//                                .setView(input)
//                                .setPositiveButton("OK") { dialog, _ ->
//                                    val newComment = input.text.toString()
//                                    // Cập nhật comment
//                                    CmtReference.child("comment").setValue(newComment)
//                                        .addOnCompleteListener { task ->
//                                        if (task.isSuccessful) {
//                                            // Chỉnh sửa thành công
//                                            Toast.makeText(context , "ok",Toast.LENGTH_LONG).show()
//                                        } else {
//                                            // Xử lý lỗi
//                                            Toast.makeText(context , "khong thanh cong",Toast.LENGTH_LONG).show()
//                                        }
//                                    }
//                                }
//                                .setNegativeButton("Cancel") { dialog, _ ->
//                                    dialog.cancel()
//                                }
//                                .create()
                            // Tạo một Dialog mới
                            val dialog = Dialog(context)
                            dialog.setContentView(R.layout.custom_dialog_layout)


                            val okButton = dialog.findViewById<Button>(R.id.btnEditCmt)
                            okButton.setOnClickListener {
                              val edt = dialog.findViewById<EditText>(R.id.edtEditCmt).text
                                CmtReference.child("comment").setValue(edt.toString())
                                        .addOnCompleteListener { task ->
                                        if (task.isSuccessful) {
                                            // Chỉnh sửa thành công
                                            Toast.makeText(context , "ok",Toast.LENGTH_LONG).show()
                                        } else {
                                            // Xử lý lỗi
                                            Toast.makeText(context , "khong thanh cong",Toast.LENGTH_LONG).show()
                                        }
                                    }
                            }

                            dialog.show()

                        }


                    }
                }

                // Hiển thị AlertDialog
                builder.show()
            }

        }
    }

}