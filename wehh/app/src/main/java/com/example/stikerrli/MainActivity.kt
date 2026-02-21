package com.example.stikerrli

import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.stikerrli.databinding.ActivityMainBinding
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var session: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        session = SessionManager(this)

        // Kalau sudah login, langsung ke halaman yang sesuai
        if (session.isLoggedIn()) {
            if (session.getRole() == "admin") {
                startActivity(Intent(this, AdminDashboardActivity::class.java))
            } else {
                startActivity(Intent(this, HomePage::class.java))
            }
            finish()
            return
        }

        // NAVIGASI: Pindah ke Register
        binding.TextRegis.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        // LOGIC: Tombol Login
        binding.ButtonRegis.setOnClickListener {
            val username = binding.UsernameRegis.text.toString().trim()
            val password = binding.PasswordRegis.text.toString().trim()

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Isi semua data!", Toast.LENGTH_SHORT).show()
            } else {
                loginUser(username, password)
            }
        }
    }

    private fun loginUser(username: String, password: String) {
        class LoginProcess : AsyncTask<Void, Void, String>() {
            override fun doInBackground(vararg voids: Void?): String {
                val params = HashMap<String, String>()
                params["name"] = username
                params["password"] = password

                val rh = RequestHandler()
                return rh.sendPostRequest(Konfigurasi.URL_LOGIN, params)
            }

            override fun onPostExecute(s: String) {
                super.onPostExecute(s)
                try {
                    val jsonObject = JSONObject(s)
                    if (jsonObject.getString("status") == "success") {
                        val userId = jsonObject.getInt("user_id")
                        val role = jsonObject.getString("role")
                        session.saveLogin(userId, username, role)

                        // Logika Pengalihan Berdasarkan Peran
                        if (role == "admin") {
                            startActivity(Intent(this@MainActivity, AdminDashboardActivity::class.java))
                        } else {
                            startActivity(Intent(this@MainActivity, HomePage::class.java))
                        }
                        finish()
                    } else {
                        Toast.makeText(this@MainActivity, jsonObject.getString("message"), Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    Toast.makeText(this@MainActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
        LoginProcess().execute()
    }
}