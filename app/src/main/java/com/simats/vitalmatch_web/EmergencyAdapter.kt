package com.simats.vitalmatch_web

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.simats.vitalmatch_web.models.EmergencyRequest

class EmergencyAdapter(private val emergencies: List<EmergencyRequest>) :
    RecyclerView.Adapter<EmergencyAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvPatientName: TextView = view.findViewById(R.id.tvPatientName)
        val tvBloodGroup: TextView = view.findViewById(R.id.tvBloodGroup)
        val tvHospital: TextView = view.findViewById(R.id.tvHospital)
        val tvLocation: TextView = view.findViewById(R.id.tvLocation)
        val tvContact: TextView = view.findViewById(R.id.tvContact)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_emergency, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val req = emergencies[position]
        holder.tvPatientName.text = req.patientName
        holder.tvBloodGroup.text = "Blood Group: ${req.bloodGroup}"
        holder.tvHospital.text = "Hospital: ${req.hospitalName}"
        holder.tvLocation.text = "Location: ${req.location}"
        holder.tvContact.text = "Contact: ${req.contactNumber}"
    }

    override fun getItemCount() = emergencies.size
}
