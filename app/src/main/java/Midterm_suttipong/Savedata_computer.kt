package Midterm_suttipong

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.os.StrictMode
import android.provider.MediaStore
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.myapplication.R
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import org.json.JSONObject
import java.io.File
import java.io.IOException

class Savedata_computer : AppCompatActivity() {

    private val PICK_IMAGE_REQUEST = 1
    private var imageUri: Uri? = null
    private lateinit var imageView: ImageView

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            if (granted) {
                // Permission granted, proceed with operations
            } else {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
            }
        }

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_savedata_computer)

        // Request necessary permissions
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }

        supportActionBar?.hide()
        //For an synchronous task
        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        val editText_Namebrand = findViewById<EditText>(R.id.editText_Namebrand)
        val editText_Model = findViewById<EditText>(R.id.editText_Model)
        val editText_SerialNumber = findViewById<EditText>(R.id.editText_SerialNumber)
        val editText_Quantity = findViewById<EditText>(R.id.editText_Quantity)
        val editText_Price = findViewById<TextView>(R.id.editText_Price)
        val editText_CPUspeed = findViewById<TextView>(R.id.editText_CPUspeed)
        val editText_Memory_Ram = findViewById<TextView>(R.id.editText_Memory_Ram)
        val editText_Memory_Harddisk = findViewById<TextView>(R.id.editText_Memory_Harddisk)

        imageView = findViewById(R.id.imageView)

        val btn_save = findViewById<Button>(R.id.btn_save)
        val btn_chooseImage = findViewById<Button>(R.id.btn_choose_image)

        btn_chooseImage.setOnClickListener {
            openImageChooser()
        }

        btn_save.setOnClickListener {
            if (editText_Namebrand.text.toString().isEmpty()) {
                editText_Namebrand.error = "กรุณาระบุชื่อแบรนด์"
                return@setOnClickListener
            }

            if (editText_Model.text.toString().isEmpty()) {
                editText_Model.error = "กรุณาระบุชื่อรุ่น"
                return@setOnClickListener
            }

            if (editText_SerialNumber.text.toString().isEmpty()) {
                editText_SerialNumber.error = "กรุณาระบุซีเรียลนัมเบอร์"
                return@setOnClickListener
            }

            if (editText_Quantity.text.toString().isEmpty()) {
                editText_Quantity.error = "กรุณาระบุจำนวน"
                return@setOnClickListener
            }
            if (editText_Price.text.toString().isEmpty()) {
                editText_Price.error = "กรุณาระบุราคา"
                return@setOnClickListener
            }
            if (editText_CPUspeed.text.toString().isEmpty()) {
                editText_CPUspeed.error = "กรุณาระบุความเร็วซีพียู"
                return@setOnClickListener
            }
            if (editText_Memory_Ram.text.toString().isEmpty()) {
                editText_Memory_Ram.error = "กรุณาระบุความจุหน่วยความจำ"
                return@setOnClickListener
            }
            if (editText_Memory_Harddisk.text.toString().isEmpty()) {
                editText_Memory_Harddisk.error = "กรุณาระบุความจุฮาร์ดดิสก์"
                return@setOnClickListener
            }
            if (imageUri == null) {
                Toast.makeText(this, "กรุณาเลือกรูปภาพ", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Convert URI to File
            val filePath = getRealPathFromURI(imageUri!!)
            val file = File(filePath)

            var url = getString(R.string.root_url) + getString(R.string.add_product_url)
            val okHttpClient = OkHttpClient()

            // Create RequestBody for file
            val fileBody = RequestBody.create("image/jpeg".toMediaTypeOrNull(), file)

            // Create MultipartBody for form data and file
            val builder = MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("computer_Namebrand", editText_Namebrand.text.toString())
                .addFormDataPart("computer_Model", editText_Model.text.toString())
                .addFormDataPart("computer_Serialnumber", editText_SerialNumber.text.toString())
                .addFormDataPart("computer_Quantity", editText_Quantity.text.toString())
                .addFormDataPart("computer_Price", editText_Price.text.toString())
                .addFormDataPart("computer_CPUspeed", editText_CPUspeed.text.toString())
                .addFormDataPart("computer_Memory", editText_Memory_Ram.text.toString())
                .addFormDataPart("computer_Harddisk", editText_Memory_Harddisk.text.toString())
                .addFormDataPart("computer_Image", file.name, fileBody)

            val requestBody = builder.build()
            val request: Request = Request.Builder()
                .url(url)
                .post(requestBody)
                .build()

            val response = okHttpClient.newCall(request).execute()

            if (response.isSuccessful) {
                val obj = JSONObject(response.body!!.string())
                val status = obj.getString("status")
                val message = obj.getString("message")

                if (status == "true") {
                    Toast.makeText(applicationContext, message, Toast.LENGTH_LONG).show()

                    // Redirect to main page
                    val intent = Intent(this@Savedata_computer, Search_computer::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(applicationContext, message, Toast.LENGTH_LONG).show()
                }
            } else {
                Toast.makeText(
                    applicationContext,
                    "ไม่สามารถเชื่อมต่อกับเซิร์ฟเวอร์ได้",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun openImageChooser() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
            imageUri = data?.data
            imageUri?.let { uri ->
                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, uri)
                imageView.setImageBitmap(bitmap)
                saveImageToGallery(bitmap)  // Save the chosen image to the gallery
            }
        }
    }

    private fun getRealPathFromURI(uri: Uri): String {
        val cursor = contentResolver.query(uri, null, null, null, null)
        cursor?.use {
            it.moveToFirst()
            val idx = it.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
            return it.getString(idx)
        }
        return ""
    }

    private fun saveImageToGallery(bitmap: Bitmap) {
        val filename = "IMG_${System.currentTimeMillis()}.jpg"
        val values = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, filename)
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
            put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES) // For API 29 and above
        }

        val resolver = contentResolver
        val uri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)

        uri?.let {
            resolver.openOutputStream(it)?.use { outputStream ->
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
                outputStream.flush()
            }
        } ?: run {
            Toast.makeText(this, "ไม่สามารถบันทึกภาพได้", Toast.LENGTH_SHORT).show()
        }
    }
}
