package com.example.sel.screen.user.dictionary

import android.annotation.SuppressLint
import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.webkit.URLUtil
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.sel.R
import com.example.sel.base.RetrofitClient
import com.example.sel.base.model.DictionaryResponse
import com.example.sel.base.model.Phonetics
import com.example.sel.screen.user.home.HomeActivity
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.TranslatorOptions
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

class DictionaryActivity : AppCompatActivity() {
    private lateinit var editTextWord: EditText

    // private lateinit var buttonLookup: Button
    private lateinit var textViewDefinition: TextView

    private lateinit var textViewDefinitionTranslate: TextView
    private lateinit var txtWord_tran: TextView
    private lateinit var txtPronounce: TextView
    private lateinit var definition: TextView
    private var audioUrl: String? = null
    private lateinit var btnAudio: ImageView
    private var mediaPlayer: MediaPlayer? = null
    private lateinit var btnBack:ImageView
    private lateinit var btnTranslate:ImageView


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dictionary)

        editTextWord = findViewById(R.id.editTextWord)
        textViewDefinition = findViewById(R.id.txt_translate)
        txtWord_tran = findViewById(R.id.txtword_tran)
        txtPronounce = findViewById(R.id.txtpronounce)
        definition = findViewById(R.id.txtdefinition)
        btnAudio = findViewById(R.id.btn_audio)
        btnBack = findViewById(R.id.imgVBack)
        btnTranslate = findViewById(R.id.imgtranslate)
        textViewDefinitionTranslate=findViewById(R.id.txtdefinitiontranslate)

        btnAudio.visibility = View.GONE
        btnTranslate.visibility = View.GONE

        btnBack.setOnClickListener {
            val intent = Intent(this@DictionaryActivity, HomeActivity::class.java)
            startActivity(intent)
        }

        editTextWord.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {

                btnAudio.visibility = View.VISIBLE
                btnTranslate.visibility = View.VISIBLE

                textViewDefinitionTranslate.text =""
                val word = editTextWord.text.toString()
                if (word.isNotEmpty()) {
                    txtWord_tran.text = word
                    Toast.makeText(v.context, "Load....", Toast.LENGTH_LONG).show()
                    translateText(word,textViewDefinition)
                    getWordMeaning(word)
                }
                true
            } else {
                false
            }
        }

        btnAudio.setOnClickListener {
            if (audioUrl != null && URLUtil.isValidUrl(audioUrl)) {
                playAudio(audioUrl!!)
            } else {
                Toast.makeText(this, "No valid audio URL available", Toast.LENGTH_SHORT).show()

            }
        }

        btnTranslate.setOnClickListener {
            val word = definition.text.toString()
            translateText(word,textViewDefinitionTranslate)
        }
    }

    private fun translateText(text: String,resultTextView: TextView) {
        val options = TranslatorOptions.Builder()
            .setSourceLanguage(TranslateLanguage.ENGLISH)
            .setTargetLanguage(TranslateLanguage.VIETNAMESE)
            .build()
        val englishVietNamTranslator = Translation.getClient(options)

        var conditions = DownloadConditions.Builder()
            .requireWifi()
            .build()

        englishVietNamTranslator.downloadModelIfNeeded(conditions)
            .addOnSuccessListener {
                englishVietNamTranslator.translate(text).addOnSuccessListener {
                    //textViewDefinition.text = it
                    resultTextView.text = it
                }
            }
            .addOnFailureListener { exception ->

            }

    }


    private fun getWordMeaning(word: String) {
        val call = RetrofitClient.instance.getWordMeaning(word)
        call.enqueue(object : Callback<List<DictionaryResponse>> {
            override fun onResponse(call: Call<List<DictionaryResponse>>, response: Response<List<DictionaryResponse>>) {
                if (response.isSuccessful && response.body() != null) {
                    val dictionaryResponse = response.body()!!.firstOrNull()
                    dictionaryResponse?.let {
                        var firstPhonetic: Phonetics? = null
                        for (phonetic in it.phonetics) {
                            if (phonetic != null) {
                                firstPhonetic = phonetic
                            }
                        }
                        val firstDefinition = it.meanings.firstOrNull()?.definitions?.firstOrNull()
                        txtPronounce.text = firstPhonetic?.text ?: "N/A"
                        definition.text = firstDefinition?.definition ?: "N/A"
                        audioUrl = firstPhonetic?.audio ?: run {
                            Toast.makeText(this@DictionaryActivity, "No audio URL available", Toast.LENGTH_SHORT).show()
                            null
                        }
                    } ?: run {
                        Toast.makeText(this@DictionaryActivity, "No data found", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@DictionaryActivity, "Failed to get definition", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<DictionaryResponse>>, t: Throwable) {
                Log.e("DictionaryError", "Error: ${t.message}")
                Toast.makeText(this@DictionaryActivity, "Error occurred", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun playAudio(url: String) {
        mediaPlayer?.release()
        mediaPlayer = MediaPlayer().apply {
            try {
                setDataSource(url)
                setOnPreparedListener { start() }
                setOnErrorListener { _, _, _ ->
                    Toast.makeText(this@DictionaryActivity, "Error playing audio", Toast.LENGTH_SHORT).show()
                    true
                }
                prepareAsync()
            } catch (e: IOException) {
                Toast.makeText(this@DictionaryActivity, "Error loading audio", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.release()
        mediaPlayer = null
    }

}