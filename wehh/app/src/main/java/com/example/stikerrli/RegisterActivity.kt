package com.example.stikerrli

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.stikerrli.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.TextRegis.setOnClickListener {
            finish()
        }

        binding.ButtonRegis.setOnClickListener {
            val user = binding.UsernameRegis.text.toString().trim()
            val pass = binding.PasswordRegis.text.toString().trim()

            if (user.isEmpty() || pass.isEmpty()) {
                Toast.makeText(this, "Lengkapi Username dan Password!", Toast.LENGTH_SHORT).show()
            } else {
                registerUser(user, pass)
            }
        }
    }

    private fun registerUser(user: String, pass: String) {

        Thread {
            try {
                val params = HashMap<String, String>()
                params["name"] = user
                params["password"] = pass

                val rh = RequestHandler()
                val result = rh.sendPostRequest(Konfigurasi.URL_REGISTER, params)

                runOnUiThread {
                    if (result.contains("success")) {
                        Toast.makeText(this, "Registration successful!", Toast.LENGTH_LONG).show()
                        finish()
                    } else {
                        Toast.makeText(this, "Registration failed!", Toast.LENGTH_LONG).show()
                    }
                }

            } catch (e: Exception) {
                runOnUiThread {
                    Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }.start()
    }
}