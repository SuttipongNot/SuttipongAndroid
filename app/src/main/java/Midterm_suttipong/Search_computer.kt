package Midterm_suttipong

import android.content.Intent
import android.os.Bundle
import android.os.StrictMode
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.myapplication.R

class Search_computer : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_search_computer2)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.seaherch_activity)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        supportActionBar?.hide()
        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        val button = findViewById<Button>(R.id.button)
        val buttonSearch = findViewById<Button>(R.id.buttonSearch)
        val editTextResearch = findViewById<EditText>(R.id.editTextResearch)

        button.setOnClickListener {
            intent = Intent(this, Savedata_computer::class.java)
            startActivity(intent)
        }

        buttonSearch.setOnClickListener {
            if (editTextResearch.text.toString().isEmpty()) {
                editTextResearch.error = "กรุณาระบุรหัสสินค้า"
                return@setOnClickListener
            }
            intent = Intent(this, Showdata_computer::class.java)
            startActivity(intent)
            intent.putExtra( "computer_ID", editTextResearch.text.toString())
            startActivity(intent)


        }
    }
}