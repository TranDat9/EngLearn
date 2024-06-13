package com.example.sel.base

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import androidx.activity.ComponentActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.example.sel.R
import javax.sql.DataSource

class Utils {
    companion object{
        fun Activity.hideKeyboard() {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(findViewById<ViewGroup>(android.R.id.content).windowToken, 0)
        }

        fun finishActivityWithAnim(activity: Activity, intent: Intent?) {
            activity.setResult(Activity.RESULT_OK, intent)
            activity.finish()
            activity.overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        }
        @SuppressWarnings("deprecation")
        fun dpToPx(context: Context?, dp: Int): Int {
            if (context != null){
                val displayMetrics = context.resources.displayMetrics
                return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT))
            }
            return 0
        }
//        fun dpToPx(context: Context, dp: Int): Int {
//            return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp.toFloat(), context.resources.displayMetrics).toInt()
//        }

        /**
         * Use LocalDensity instead
         */
        @SuppressWarnings("deprecation")
        fun pxToDp(context: Context?, px: Int): Int {
            if (context != null){
                val displayMetrics = context.resources.displayMetrics
                return Math.round(px * (DisplayMetrics.DENSITY_DEFAULT / displayMetrics.xdpi))
            }
            return 0
        }

        fun loadImageWithGlide(
            context: Context?,
            url: String?,
            imageView: ImageView?,
            width: Int? = null,
            height: Int? = null
        ) {
            try {
                if (context != null && imageView != null) {
                    val options = RequestOptions()
//                        .centerCrop()
                        .disallowHardwareConfig()

                    if (width != null && height != null && context != null) {
                        var widthValue = Utils.dpToPx(context, width)
                        var heightValue = Utils.dpToPx(context, height)
                        options.override(widthValue, heightValue)
                    }
                    Glide.with(context).load(url).apply(options).listener(
                        object : RequestListener<Drawable> {


                            override fun onResourceReady(
                                resource: Drawable?,
                                model: Any?,
                                target: com.bumptech.glide.request.target.Target<Drawable>?,
                                dataSource: com.bumptech.glide.load.DataSource?,
                                isFirstResource: Boolean
                            ): Boolean {
                                return false

                            }

                            override fun onLoadFailed(
                                e: GlideException?,
                                model: Any?,
                                target: Target<Drawable>?,
                                isFirstResource: Boolean
                            ): Boolean {
                                return false
                            }

                        }).into(imageView)
                }
            } catch (e: java.lang.Exception) {

            }
        }

        fun startActivityWithResultCode(
//                beginActivity: AppCompatActivity,
//                finishActivity: AppCompatActivity, resultCode: Int, bundle: Bundle?
            beginActivity: ComponentActivity,
            finishActivity: ComponentActivity, resultCode: Int, bundle: Bundle?
        ) {
            val intentAcitivity = Intent(beginActivity, finishActivity::class.java)
            bundle?.let {
                intentAcitivity.putExtras(it)
            }
            beginActivity.startActivityForResult(intentAcitivity, resultCode)
            beginActivity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }

    }
}