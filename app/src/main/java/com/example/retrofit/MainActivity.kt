package com.example.retrofit

import android.os.Binder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.example.retrofit.databinding.ActivityMainBinding
import com.example.retrofit.retrofit.AuthRequest
import com.example.retrofit.retrofit.MainAPI
import com.example.retrofit.retrofit.ProductAPI
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val retrofit = Retrofit.Builder()
            .baseUrl("https://dummyjson.com")
            .addConverterFactory(GsonConverterFactory.create()).build()
        val productAPI = retrofit.create(ProductAPI::class.java)
        val mainAPI = retrofit.create(MainAPI::class.java)

        val tv = findViewById<TextView>(R.id.tv)
        val b = findViewById<Button>(R.id.button)

        b.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                val product = productAPI.getProductById(2)
                runOnUiThread {
                    tv.text = product.title
                }
            }
        }

        binding.signin.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                val user = mainAPI.auth(
                    AuthRequest(
                        binding.username.text.toString(),
                        binding.password.text.toString()
                    )
                )
                runOnUiThread {
                    binding.apply {
                        Picasso.get().load(user.image).into(iv)
                        firstName.text = user.firstName
                        lastName.text = user.lastName
                    }
                }
            }
        }
    }
}