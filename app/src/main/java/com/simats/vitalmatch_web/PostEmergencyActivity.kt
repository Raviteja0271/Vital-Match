package com.simats.vitalmatch_web

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.simats.vitalmatch_web.databinding.ActivityPostEmergencyBinding
import com.simats.vitalmatch_web.models.EmergencyRequest
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.launch

class PostEmergencyActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPostEmergencyBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPostEmergencyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Bottom Navigation
        binding.bottomNav.selectedItemId = R.id.nav_emergencies
        binding.bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    startActivity(Intent(this, DashboardActivity::class.java))
                    finish()
                    true
                }
                R.id.nav_emergencies -> true // Already on this page
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

        binding.btnSubmitEmergency.setOnClickListener {
            val patientName = binding.etPatientName.text.toString().trim()
            val bloodGroup = binding.etBloodGroup.text.toString().trim()
            val hospital = binding.etHospital.text.toString().trim()
            val contact = binding.etContact.text.toString().trim()
            val location = binding.etLocation.text.toString().trim()
            val notes = binding.etNotes.text.toString().trim()

            if (patientName.isEmpty() || bloodGroup.isEmpty() || hospital.isEmpty() || contact.isEmpty() || location.isEmpty()) {
                Toast.makeText(this, "Please fill in all required fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            binding.btnSubmitEmergency.isEnabled = false
            binding.btnSubmitEmergency.text = "Submitting..."

            lifecycleScope.launch {
                try {
                    val user = SupabaseClient.client.auth.currentUserOrNull()
                    if (user == null) {
                        Toast.makeText(this@PostEmergencyActivity, "You must be logged in.", Toast.LENGTH_SHORT).show()
                        return@launch
                    }

                    val request = EmergencyRequest(
                        userId = user.id,
                        patientName = patientName,
                        bloodGroup = bloodGroup,
                        hospitalName = hospital,
                        contactNumber = contact,
                        location = location,
                        notes = notes
                    )

                    SupabaseClient.client.postgrest["emergency_requests"].insert(request)

                    Toast.makeText(this@PostEmergencyActivity, "Emergency request posted successfully", Toast.LENGTH_SHORT).show()
                    // Navigate back to dashboard
                    startActivity(Intent(this@PostEmergencyActivity, DashboardActivity::class.java))
                    finish()
                } catch (e: Exception) {
                    Toast.makeText(this@PostEmergencyActivity, "Error: ${e.message}", Toast.LENGTH_LONG).show()
                    e.printStackTrace()
                    binding.btnSubmitEmergency.isEnabled = true
                    binding.btnSubmitEmergency.text = "Submit Request"
                }
            }
        }
    }
}
