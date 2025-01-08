package org.hyperskill.secretdiary
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import android.app.AlertDialog
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import kotlinx.datetime.Clock
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainActivity : AppCompatActivity() {

    lateinit var sharedPreferences: SharedPreferences
    val notes = mutableListOf<Note>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val etNewWriting: EditText = findViewById(R.id.etNewWriting)
        val btnSave: Button = findViewById(R.id.btnSave)
        val btnUndo: Button = findViewById(R.id.btnUndo)
        val tvDiary: TextView = findViewById(R.id.tvDiary)

        sharedPreferences = getSharedPreferences("PREF_DIARY", MODE_PRIVATE)
        if (sharedPreferences.contains("KEY_DIARY_TEXT")) {
            val myNotesText = sharedPreferences.getString("KEY_DIARY_TEXT", "")
            val splitedNotes = myNotesText?.split("\n\n")
            if (splitedNotes != null) {
                for (n in splitedNotes) {
                    val element = n.split("\n", limit = 2)
                    notes.add(Note(element[0], element[1]))
                }
            }
            tvDiary.text = notes.joinToString("\n\n")
        }


        btnSave.setOnClickListener {
            if (etNewWriting.text.isEmpty() || etNewWriting.text.isBlank()) {
                Toast.makeText(this, "Empty or blank input cannot be saved", Toast.LENGTH_SHORT).show()
            } else {
                val noteText = etNewWriting.text.toString()
                val note = Note(getCurrentTime(), noteText)
                notes.add(0, note)
                tvDiary.text = notes.joinToString("\n\n")
                etNewWriting.setText("")
                //Saving notes
                val editor = sharedPreferences.edit()
                editor.putString("KEY_DIARY_TEXT", tvDiary.text.toString()).apply()
            }
        }

        btnUndo.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle(R.string.adTitle)
                .setMessage(R.string.adMessage)
                .setPositiveButton(R.string.adYes) {_, _ ->
                    if (notes.size > 0) {
                        notes.removeAt(0)
                        tvDiary.text = notes.joinToString("\n\n")
                    }
                }
                .setNegativeButton(R.string.adNo) {_, _ ->
                }
                .setCancelable(false)
                .show()
        }
    }


    fun getCurrentTime(): String {
        val currentTime = Clock.System.now().toEpochMilliseconds()
        val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val formattedTime = formatter.format(Date(currentTime))
        return formattedTime
    }
}