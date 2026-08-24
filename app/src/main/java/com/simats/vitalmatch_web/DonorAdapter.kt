package com.simats.vitalmatch_web

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.simats.vitalmatch_web.models.Profile

class DonorAdapter(private val donors: List<Profile>) :
    RecyclerView.Adapter<DonorAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvDonorName: TextView = view.findViewById(R.id.tvDonorName)
        val tvDonorBloodGroup: TextView = view.findViewById(R.id.tvDonorBloodGroup)
        val tvDonorLocation: TextView = view.findViewById(R.id.tvDonorLocation)
        val tvDonorContact: TextView = view.findViewById(R.id.tvDonorContact)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_donor, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val donor = donors[position]
        holder.tvDonorName.text = donor.fullName.takeIf { it.isNotBlank() } ?: "Unknown Donor"
        holder.tvDonorBloodGroup.text = "Blood Group: ${donor.bloodGroup}"
        holder.tvDonorLocation.text = "Location: ${donor.city}, ${donor.state}"
        holder.tvDonorContact.text = "Contact: ${donor.mobile}"
    }

    override fun getItemCount() = donors.size
}
