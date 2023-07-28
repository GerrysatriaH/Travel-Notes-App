package pnj.ti4a.uas_gerrysatriahalim

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText

class Login : AppCompatActivity() {
    private lateinit var emailEditText: TextInputEditText
    private lateinit var passwordEditText: TextInputEditText
    private lateinit var loginBtn: MaterialButton

    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        emailEditText = findViewById(R.id.emailEditText)
        passwordEditText = findViewById(R.id.passwordEditText)
        loginBtn = findViewById(R.id.loginButton)

        sharedPreferences = getSharedPreferences("my_pref", Context.MODE_PRIVATE)

        loginBtn.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()

            if (isValid(email, password)) {
                saveCredentials(email, password)
                Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show()

                var toMainMenu = Intent(this@Login, MainActivity::class.java)
                startActivity(toMainMenu)
            } else {
                Toast.makeText(this, "Invalid credentials", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun isValid(email: String, password: String): Boolean {
        return email == "gerry@gmail.com" && password == "12345"
    }

    private fun saveCredentials(email: String, password: String) {
        val editor = sharedPreferences.edit()
        editor.putString("email", email)
        editor.putString("password", password)
        editor.putString("Nim", "2107411028")
        editor.putString("Nama", "Gerry Satria Halim")
        editor.putString("Kelas", "TI 4A")
        editor.apply()
    }
}