package com.simats.vitalmatch_web

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.simats.vitalmatch_web.databinding.ActivityDashboardBinding
import com.simats.vitalmatch_web.models.EmergencyRequest
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.launch

class DashboardActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDashboardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val user = SupabaseClient.client.auth.currentUserOrNull()
        if (user != null) {
            binding.tvUserEmail.text = "Logged in as:\n${user.email}"
        } else {
            binding.tvUserEmail.text = "Not logged in"
        }

        binding.rvEmergencies.layoutManager = LinearLayoutManager(this)

        binding.btnSearchDonors.setOnClickListener {
            startActivity(Intent(this, SearchDonorsActivity::class.java))
        }

        binding.btnPostEmergency.setOnClickListener {
            startActivity(Intent(this, PostEmergencyActivity::class.java))
        }

        binding.btnProfile.setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
        }

        binding.btnLogout.setOnClickListener {
            lifecycleScope.launch {
                SupabaseClient.client.auth.signOut()
                startActivity(Intent(this@DashboardActivity, LoginActivity::class.java))
                finishAffinity()
            }
        }

        // Bottom Navigation
        binding.bottomNav.selectedItemId = R.id.nav_home
        binding.bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> true // Already on this page
                R.id.nav_emergencies -> {
                    startActivity(Intent(this, PostEmergencyActivity::class.java))
                    finish()
                    true
                }
                R.id.nav_donors -> {
                    startActivity(Intent(this, SearchDonorsActivity::class.java))
                    finish()
                    true
                }
                R.id.nav_profile -> {
                    startActivity(Intent(this, ProfileActivity::class.java))
                    finish()
                    true
                }
                else -> false
            }
        }

        fetchEmergencies()
    }

    private fun fetchEmergencies() {
        lifecycleScope.launch {
            try {
                val requests = SupabaseClient.client.postgrest["emergency_requests"]
                    .select {
                        filter {
                            eq("status", "Active")
                        }
                    }
                    .decodeList<EmergencyRequest>()
                
                binding.rvEmergencies.adapter = EmergencyAdapter(requests)
            } catch (e: Exception) {
                Toast.makeText(this@DashboardActivity, "Error: ${e.message}", Toast.LENGTH_LONG).show()
                e.printStackTrace()
            }
        }
    }
}
