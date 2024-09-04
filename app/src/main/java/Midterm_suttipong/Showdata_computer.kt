package Midterm_suttipong

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.StrictMode
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.myapplication.R
import com.bumptech.glide.Glide
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject

class Showdata_computer : AppCompatActivity() {
    private var ImageName: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_showdata_computer)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.showdata_activity)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets

        }
        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        supportActionBar?.hide()

        val TextView_Namebrand = findViewById<TextView>(R.id.textViewNamebrand)
        val TextView_Model = findViewById<TextView>(R.id.textViewModel)
        val TextView_SerialNumber = findViewById<TextView>(R.id.textViewSerialnumber)
        val TextView_Quantity = findViewById<TextView>(R.id.textViewQuantity)
        val TextView_Price = findViewById<TextView>(R.id.textViewPrice)
        val TextView_CPUspeed = findViewById<TextView>(R.id.textViewCPUspeed)
        val TextView_Memory_Ram = findViewById<TextView>(R.id.textViewMemory)
        val TextView_Memory_Harddisk = findViewById<TextView>(R.id.textViewHarddisk)
        val Imageview = findViewById<ImageView>(R.id.imageView)

        val statusActivity = intent.getStringExtra("computer_ID")

        var url = getString(R.string.root_url) + getString(R.string.show_product_url) + statusActivity.toString()

        val okHttpClient = OkHttpClient()
        var request: Request = Request.Builder()
            .url(url)
            .get()
            .build()
        var response = okHttpClient.newCall(request).execute()

        if (response.isSuccessful) {
            var obj = JSONObject(response.body!!.string())
            TextView_Namebrand.text = obj["computer_Namebrand"].toString()
            TextView_Model.text = obj["computer_Model"].toString()
            TextView_SerialNumber.text = obj["computer_Serialnumber"].toString()
            TextView_Quantity.text = obj["computer_Quantity"].toString()
            TextView_Price.text = obj["computer_Price"].toString()
            TextView_CPUspeed.text = obj["computer_CPUspeed"].toString()
            TextView_Memory_Ram.text = obj["computer_Memory"].toString()
            TextView_Memory_Harddisk.text = obj["computer_Harddisk"].toString()
            ImageName = obj["computer_Image"].toString()
        }
        if (ImageName != null) {
            url = getString(R.string.root_url) + ImageName.toString()
            Glide.with(this)
                .load(url)
                .into(Imageview)
        }
    }

}
