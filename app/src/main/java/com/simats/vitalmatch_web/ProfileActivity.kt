package com.simats.vitalmatch_web

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.simats.vitalmatch_web.databinding.ActivityProfileBinding
import com.simats.vitalmatch_web.models.Profile
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.launch

class ProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding
    private var currentProfile: Profile? = null

    private var selectedState: String = ""
    private var selectedDistrict: String = ""
    private var selectedCity: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Bottom Navigation
        binding.bottomNav.selectedItemId = R.id.nav_profile
        binding.bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    startActivity(Intent(this, DashboardActivity::class.java))
                    finish()
                    true
                }
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
                R.id.nav_profile -> true
                else -> false
            }
        }

        setupLocationSpinners()

        binding.btnSaveProfile.setOnClickListener {
            saveProfile()
        }

        fetchProfile()
    }

    private fun setupLocationSpinners() {
        // State Spinner
        val states = mutableListOf("Select State") + IndiaLocations.getStates()
        val stateAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, states)
        stateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerState.adapter = stateAdapter

        // District Spinner (initially empty)
        resetDistrictSpinner()

        // City Spinner (initially empty)
        resetCitySpinner()

        // State selection listener
        binding.spinnerState.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (position == 0) {
                    selectedState = ""
                    resetDistrictSpinner()
                    resetCitySpinner()
                    return
                }
                selectedState = states[position]
                val districts = mutableListOf("Select District") + IndiaLocations.getDistricts(selectedState)
                val districtAdapter = ArrayAdapter(this@ProfileActivity, android.R.layout.simple_spinner_item, districts)
                districtAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                binding.spinnerDistrict.adapter = districtAdapter
                resetCitySpinner()
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        // District selection listener
        binding.spinnerDistrict.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (position == 0) {
                    selectedDistrict = ""
                    resetCitySpinner()
                    return
                }
                selectedDistrict = parent?.getItemAtPosition(position).toString()
                val cities = mutableListOf("Select City") + IndiaLocations.getCities(selectedState, selectedDistrict)
                val cityAdapter = ArrayAdapter(this@ProfileActivity, android.R.layout.simple_spinner_item, cities)
                cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                binding.spinnerCity.adapter = cityAdapter
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        // City selection listener
        binding.spinnerCity.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                selectedCity = if (position == 0) "" else parent?.getItemAtPosition(position).toString()
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun resetDistrictSpinner() {
        val districtAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, listOf("Select District"))
        districtAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerDistrict.adapter = districtAdapter
    }

    private fun resetCitySpinner() {
        val cityAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, listOf("Select City"))
        cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerCity.adapter = cityAdapter
    }

    private fun setSpinnerValue(spinnerItems: List<String>, value: String?): Int {
        if (value.isNullOrEmpty()) return 0
        return spinnerItems.indexOf(value).takeIf { it >= 0 } ?: 0
    }

    private fun fetchProfile() {
        lifecycleScope.launch {
            try {
                val user = SupabaseClient.client.auth.currentUserOrNull()
                if (user == null) {
                    Toast.makeText(this@ProfileActivity, "Not logged in", Toast.LENGTH_SHORT).show()
                    finish()
                    return@launch
                }

                currentProfile = SupabaseClient.client.postgrest["profiles"]
                    .select {
                        filter {
                            eq("id", user.id)
                        }
                    }
                    .decodeSingleOrNull<Profile>()

                currentProfile?.let { profile ->
                    binding.etFullName.setText(profile.fullName)
                    binding.etMobile.setText(profile.mobile)
                    binding.etBloodGroup.setText(profile.bloodGroup)
                    binding.switchDonor.isChecked = profile.isDonor
                    binding.switchAvailable.isChecked = profile.isAvailable

                    // Pre-fill location spinners
                    if (!profile.state.isNullOrEmpty()) {
                        val states = mutableListOf("Select State") + IndiaLocations.getStates()
                        val statePos = setSpinnerValue(states, profile.state)
                        binding.spinnerState.setSelection(statePos)

                        // Wait for state spinner to trigger, then set district
                        binding.spinnerState.post {
                            if (!profile.district.isNullOrEmpty()) {
                                val districts = mutableListOf("Select District") + IndiaLocations.getDistricts(profile.state)
                                val districtPos = setSpinnerValue(districts, profile.district)
                                binding.spinnerDistrict.setSelection(districtPos)

                                binding.spinnerDistrict.post {
                                    if (!profile.city.isNullOrEmpty()) {
                                        val cities = mutableListOf("Select City") + IndiaLocations.getCities(profile.state, profile.district)
                                        val cityPos = setSpinnerValue(cities, profile.city)
                                        binding.spinnerCity.setSelection(cityPos)
                                    }
                                }
                            }
                        }
                    }
                }
            } catch (e: Exception) {
                Toast.makeText(this@ProfileActivity, "Error loading profile: ${e.message}", Toast.LENGTH_SHORT).show()
                e.printStackTrace()
            }
        }
    }

    private fun saveProfile() {
        val name = binding.etFullName.text.toString().trim()
        val mobile = binding.etMobile.text.toString().trim()
        val bloodGroup = binding.etBloodGroup.text.toString().trim()
        val isDonor = binding.switchDonor.isChecked
        val isAvailable = binding.switchAvailable.isChecked

        binding.btnSaveProfile.isEnabled = false
        binding.btnSaveProfile.text = "Saving..."

        lifecycleScope.launch {
            try {
                val user = SupabaseClient.client.auth.currentUserOrNull()
                if (user == null) return@launch

                val updatedProfile = Profile(
                    id = user.id,
                    fullName = name,
                    mobile = mobile,
                    bloodGroup = bloodGroup,
                    state = selectedState,
                    district = selectedDistrict,
                    city = selectedCity,
                    isDonor = isDonor,
                    isAvailable = isAvailable
                )

                SupabaseClient.client.postgrest["profiles"]
                    .update(updatedProfile) {
                        filter {
                            eq("id", user.id)
                        }
                    }

                Toast.makeText(this@ProfileActivity, "Profile updated successfully!", Toast.LENGTH_SHORT).show()
                binding.btnSaveProfile.isEnabled = true
                binding.btnSaveProfile.text = "Save Changes"
            } catch (e: Exception) {
                Toast.makeText(this@ProfileActivity, "Error saving profile: ${e.message}", Toast.LENGTH_LONG).show()
                e.printStackTrace()
                binding.btnSaveProfile.isEnabled = true
                binding.btnSaveProfile.text = "Save Changes"
            }
        }
    }
}
