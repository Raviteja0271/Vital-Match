// ============================================
// VitalMatch - Database Operations
// Requires: supabase-config.js loaded first
// ============================================

const DB = {

    // ========== PROFILES ==========

    async getProfile(userId) {
        const { data, error } = await supabaseClient
            .from('profiles')
            .select('*')
            .eq('id', userId)
            .single();
        if (error) console.error('getProfile:', error.message);
        return data;
    },

    async updateProfile(userId, updates) {
        updates.updated_at = new Date().toISOString();
        const { data, error } = await supabaseClient
            .from('profiles')
            .update(updates)
            .eq('id', userId)
            .select()
            .single();
        if (error) console.error('updateProfile:', error.message);
        return data;
    },

    async searchDonors(filters) {
        let query = supabaseClient
            .from('profiles')
            .select('*')
            .eq('is_donor', true);

        if (filters.blood_group && filters.blood_group !== 'all') {
            query = query.eq('blood_group', filters.blood_group);
        }
        if (filters.state) {
            query = query.eq('state', filters.state);
        }
        if (filters.district) {
            query = query.eq('district', filters.district);
        }
        if (filters.city) {
            query = query.eq('city', filters.city);
        }

        const { data, error } = await query.order('is_available', { ascending: false });
        if (error) console.error('searchDonors:', error.message);
        return data || [];
    },

    // ========== EMERGENCY REQUESTS ==========

    async createEmergency(emergency) {
        // Sanitize payload to match emergency_requests table schema
        const fullLoc = emergency.location || [emergency.city, emergency.district, emergency.state].filter(Boolean).join(', ') || 'Not specified';
        
        const cleanPayload = {
            user_id: emergency.user_id,
            patient_name: emergency.patient_name,
            blood_group: emergency.blood_group,
            hospital_name: emergency.hospital_name,
            contact_number: emergency.contact_number,
            location: fullLoc,
            notes: emergency.notes || '',
            priority: emergency.priority || 'High',
            status: emergency.status || 'Active'
        };

        const { data, error } = await supabaseClient
            .from('emergency_requests')
            .insert(cleanPayload)
            .select()
            .single();
        if (error) console.error('createEmergency:', error.message);
        return { data, error };
    },

    async getEmergencies(limit = 50) {
        const { data, error } = await supabaseClient
            .from('emergency_requests')
            .select('*')
            .order('created_at', { ascending: false })
            .limit(limit);
        if (error) console.error('getEmergencies:', error.message);
        return data || [];
    },

    async getActiveEmergencies() {
        const { data, error } = await supabaseClient
            .from('emergency_requests')
            .select('*')
            .eq('status', 'Active')
            .order('created_at', { ascending: false });
        if (error) console.error('getActiveEmergencies:', error.message);
        return data || [];
    },

    async markAsConnected(emergencyId) {
        const { data, error } = await supabaseClient
            .from('emergency_requests')
            .update({ status: 'Connected' })
            .eq('id', emergencyId)
            .select()
            .single();
        if (error) console.error('markAsConnected:', error.message);
        return data;
    },

    // ========== NOTIFICATIONS ==========

    async getNotifications(userId) {
        const { data, error } = await supabaseClient
            .from('notifications')
            .select('*')
            .eq('user_id', userId)
            .order('created_at', { ascending: false });
        if (error) console.error('getNotifications:', error.message);
        return data || [];
    },

    async markNotificationRead(notifId) {
        const { data, error } = await supabaseClient
            .from('notifications')
            .update({ is_read: true })
            .eq('id', notifId);
        if (error) console.error('markNotificationRead:', error.message);
        return data;
    },

    async createNotification(notification) {
        const { data, error } = await supabaseClient
            .from('notifications')
            .insert(notification);
        if (error) console.error('createNotification:', error.message);
        return data;
    },

    // ========== REPORTS ==========

    async createReport(report) {
        const { data, error } = await supabaseClient
            .from('reports')
            .insert(report)
            .select()
            .single();
        if (error) console.error('createReport:', error.message);
        return { data, error };
    },

    async getMyReports(userId) {
        const { data, error } = await supabaseClient
            .from('reports')
            .select('*')
            .eq('reporter_id', userId)
            .order('created_at', { ascending: false });
        if (error) console.error('getMyReports:', error.message);
        return data || [];
    },

    // ========== DONATIONS ==========

    async getDonationHistory(userId) {
        const { data, error } = await supabaseClient
            .from('donations')
            .select('*')
            .eq('donor_id', userId)
            .order('donation_date', { ascending: false });
        if (error) console.error('getDonationHistory:', error.message);
        return data || [];
    },

    async addDonation(donation) {
        const { data, error } = await supabaseClient
            .from('donations')
            .insert(donation)
            .select()
            .single();
        if (error) console.error('addDonation:', error.message);
        return { data, error };
    },

    // ========== BLOOD REQUESTS ==========

    async createBloodRequest(request) {
        const { data, error } = await supabaseClient
            .from('blood_requests')
            .insert(request)
            .select()
            .single();
        if (error) console.error('createBloodRequest:', error.message);
        return { data, error };
    },

    async getMyBloodRequests(donorUserId) {
        const { data, error } = await supabaseClient
            .from('blood_requests')
            .select('*')
            .eq('donor_user_id', donorUserId)
            .order('created_at', { ascending: false });
        if (error) console.error('getMyBloodRequests:', error.message);
        return data || [];
    },

    async updateBloodRequestStatus(requestId, status) {
        const { data, error } = await supabaseClient
            .from('blood_requests')
            .update({ status: status })
            .eq('id', requestId);
        if (error) console.error('updateBloodRequestStatus:', error.message);
        return data;
    },

    async markEmergencyCompletedAndRecordDonation(emergencyId, donorPhoneOrName) {
        const todayDate = new Date().toISOString().split('T')[0];
        
        if (emergencyId) {
            await supabaseClient
                .from('emergency_requests')
                .update({ status: 'Completed' })
                .eq('id', emergencyId);
        }

        let cleanInput = donorPhoneOrName.replace(/\D/g, '');
        let matchedDonor = donors ? donors.find(d => {
            let cleanMobile = (d.mobile || '').replace(/\D/g, '');
            return (cleanMobile && cleanInput.includes(cleanMobile)) ||
                   (cleanMobile.length >= 10 && cleanInput.endsWith(cleanMobile.slice(-10))) ||
                   (d.full_name && d.full_name.toLowerCase().includes(donorPhoneOrName.toLowerCase()));
        }) : null;

        if (!matchedDonor && donors && donors.length > 0) {
            matchedDonor = donors[0];
        }

        if (matchedDonor) {
            await supabaseClient
                .from('profiles')
                .update({ 
                    last_donation_date: todayDate,
                    is_available: false 
                })
                .eq('id', matchedDonor.id);

            const { data: previousNotifs } = await supabaseClient
                .from('notifications')
                .select('*')
                .eq('user_id', matchedDonor.id)
                .eq('type', 'DONATION_SUCCESS');

            const count = (previousNotifs ? previousNotifs.length : 0) + 1;
            const ordinal = count === 1 ? '1st' : count === 2 ? '2nd' : count === 3 ? '3rd' : `${count}th`;

            await supabaseClient
                .from('notifications')
                .insert({
                    user_id: matchedDonor.id,
                    title: `${ordinal} Donation Completed!`,
                    message: `${ordinal} Donation - Donated Blood on ${todayDate}`,
                    type: 'DONATION_SUCCESS'
                });
        }
        return true;
    },

    // ========== DASHBOARD STATS ==========

    async getDashboardStats(userDistrict = null, userId = null) {
        let donorsQuery = supabaseClient.from('profiles').select('id', { count: 'exact', head: true }).eq('is_donor', true);
        let availableQuery = supabaseClient.from('profiles').select('id', { count: 'exact', head: true }).eq('is_donor', true).eq('is_available', true);
        let emergenciesQuery = supabaseClient.from('emergency_requests').select('id', { count: 'exact', head: true }).eq('status', 'Active');

        if (userDistrict) {
            donorsQuery = donorsQuery.ilike('district', userDistrict);
            availableQuery = availableQuery.ilike('district', userDistrict);
            emergenciesQuery = emergenciesQuery.ilike('district', userDistrict);
        }

        let userConnectionsCount = 0;
        if (userId) {
            const [myEmergencies, myDonations, myBloodRequests] = await Promise.all([
                supabaseClient.from('emergency_requests').select('id', { count: 'exact', head: true }).eq('user_id', userId).in('status', ['Connected', 'Completed']),
                supabaseClient.from('notifications').select('id', { count: 'exact', head: true }).eq('user_id', userId).eq('type', 'DONATION_SUCCESS'),
                supabaseClient.from('blood_requests').select('id', { count: 'exact', head: true }).eq('donor_user_id', userId).in('status', ['Accepted', 'Completed'])
            ]);
            userConnectionsCount = (myEmergencies.count || 0) + (myDonations.count || 0) + (myBloodRequests.count || 0);
        }

        const [donors, available, emergencies] = await Promise.all([
            donorsQuery,
            availableQuery,
            emergenciesQuery
        ]);

        return {
            totalDonors: donors.count || 0,
            availableDonors: available.count || 0,
            activeEmergencies: emergencies.count || 0,
            successfulConnections: userConnectionsCount,
        };
    }
};
