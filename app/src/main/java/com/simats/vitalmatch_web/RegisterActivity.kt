package com.simats.vitalmatch_web

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.simats.vitalmatch_web.databinding.ActivityRegisterBinding
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import kotlinx.coroutines.launch

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.tvLogin.setOnClickListener {
            finish()
        }

        binding.btnRegister.setOnClickListener {
            val name = binding.etFullName.text.toString().trim()
            val mobile = binding.etMobile.text.toString().trim()
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString()

            if (name.isEmpty() || mobile.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please enter all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            binding.btnRegister.isEnabled = false
            binding.btnRegister.text = "Creating Account..."

            lifecycleScope.launch {
                try {
                    SupabaseClient.client.auth.signUpWith(Email) {
                        this.email = email
                        this.password = password
                        this.data = buildJsonObject {
                            put("full_name", name)
                            put("mobile", mobile)
                        }
                    }
                    SupabaseClient.client.auth.signOut() // Sign out to force manual login
                    Toast.makeText(this@RegisterActivity, "Registration Successful. Please Login.", Toast.LENGTH_LONG).show()
                    startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
                    finishAffinity()
                } catch (e: Exception) {
                    Toast.makeText(this@RegisterActivity, "Registration Failed: ${e.localizedMessage}", Toast.LENGTH_LONG).show()
                    binding.btnRegister.isEnabled = true
                    binding.btnRegister.text = "Create Account"
                }
            }
        }
    }
}
