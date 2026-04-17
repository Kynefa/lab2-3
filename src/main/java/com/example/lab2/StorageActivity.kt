package com.example.lab2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast

class StorageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_storage)

        val txtStorage = findViewById<TextView>(R.id.txtStorage)
        val btnClear = findViewById<Button>(R.id.btnClearStorage)
        val btnBack = findViewById<Button>(R.id.btnBack)

        fun loadData() {
            val data = StorageHelper.read(this)
            txtStorage.text = if (data.isBlank()) "Сховище порожнє" else data
        }

        loadData()

        btnClear.setOnClickListener {
            val ok = StorageHelper.clear(this)
            Toast.makeText(
                this,
                if (ok) "Сховище очищено" else "Помилка очищення",
                Toast.LENGTH_SHORT
            ).show()
            loadData()
        }

        btnBack.setOnClickListener {
            finish()
        }
    }
}
