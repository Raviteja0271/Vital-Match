package com.simats.vitalmatch_web

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.simats.vitalmatch_web.databinding.ActivitySearchDonorsBinding
import com.simats.vitalmatch_web.models.Profile
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.launch

class SearchDonorsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchDonorsBinding

    private var filterState: String = ""
    private var filterDistrict: String = ""
    private var filterCity: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchDonorsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.rvDonors.layoutManager = LinearLayoutManager(this)

        // Bottom Navigation
        binding.bottomNav.selectedItemId = R.id.nav_donors
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
                R.id.nav_donors -> true
                R.id.nav_profile -> {
                    startActivity(Intent(this, ProfileActivity::class.java))
                    finish()
                    true
                }
                else -> false
            }
        }

        setupFilterSpinners()

        binding.btnSearch.setOnClickListener {
            fetchDonors()
        }

        // Initial load (all donors)
        fetchDonors()
    }

    private fun setupFilterSpinners() {
        // State Spinner
        val states = mutableListOf("All States") + IndiaLocations.getStates()
        val stateAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, states)
        stateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerFilterState.adapter = stateAdapter

        // District Spinner (initially empty)
        resetDistrictSpinner()

        // City Spinner (initially empty)
        resetCitySpinner()

        // State selection listener
        binding.spinnerFilterState.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (position == 0) {
                    filterState = ""
                    resetDistrictSpinner()
                    resetCitySpinner()
                    return
                }
                filterState = states[position]
                val districts = mutableListOf("All Districts") + IndiaLocations.getDistricts(filterState)
                val districtAdapter = ArrayAdapter(this@SearchDonorsActivity, android.R.layout.simple_spinner_item, districts)
                districtAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                binding.spinnerFilterDistrict.adapter = districtAdapter
                resetCitySpinner()
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        // District selection listener
        binding.spinnerFilterDistrict.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (position == 0) {
                    filterDistrict = ""
                    resetCitySpinner()
                    return
                }
                filterDistrict = parent?.getItemAtPosition(position).toString()
                val cities = mutableListOf("All Cities") + IndiaLocations.getCities(filterState, filterDistrict)
                val cityAdapter = ArrayAdapter(this@SearchDonorsActivity, android.R.layout.simple_spinner_item, cities)
                cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                binding.spinnerFilterCity.adapter = cityAdapter
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        // City selection listener
        binding.spinnerFilterCity.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                filterCity = if (position == 0) "" else parent?.getItemAtPosition(position).toString()
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun resetDistrictSpinner() {
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, listOf("All Districts"))
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerFilterDistrict.adapter = adapter
    }

    private fun resetCitySpinner() {
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, listOf("All Cities"))
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerFilterCity.adapter = adapter
    }

    private fun fetchDonors() {
        binding.btnSearch.isEnabled = false
        binding.btnSearch.text = "Searching..."

        lifecycleScope.launch {
            try {
                var query = SupabaseClient.client.postgrest["profiles"]
                    .select {
                        filter {
                            eq("is_donor", true)
                            eq("is_available", true)
                            if (filterState.isNotEmpty()) {
                                eq("state", filterState)
                            }
                            if (filterDistrict.isNotEmpty()) {
                                eq("district", filterDistrict)
                            }
                            if (filterCity.isNotEmpty()) {
                                eq("city", filterCity)
                            }
                        }
                    }
                    .decodeList<Profile>()

                binding.rvDonors.adapter = DonorAdapter(query)
                binding.btnSearch.isEnabled = true
                binding.btnSearch.text = "Search Donors"

                if (query.isEmpty()) {
                    Toast.makeText(this@SearchDonorsActivity, "No donors found for the selected location", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@SearchDonorsActivity, "Error: ${e.message}", Toast.LENGTH_LONG).show()
                e.printStackTrace()
                binding.btnSearch.isEnabled = true
                binding.btnSearch.text = "Search Donors"
            }
        }
    }
}
