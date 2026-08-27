/**
 * VitalMatch API Service
 * Replace BASE_URL with your actual mobile app's backend endpoint.
 */

const API_CONFIG = {
    BASE_URL: 'https://api.vitalmatch.com/v1', // Replace with your logic's URL
    TIMEOUT: 5000
};

const ApiService = {
    async request(endpoint, options = {}) {
        const session = JSON.parse(localStorage.getItem('vm_session'));
        const headers = {
            'Content-Type': 'application/json',
            ...(session ? { 'Authorization': `Bearer ${session.token}` } : {}),
            ...options.headers
        };

        // For demonstration, we simulate the API response
        // In production, uncomment the fetch block below
        console.log(`[API Call] ${options.method || 'GET'} to ${endpoint}`);

        return new Promise((resolve) => {
            setTimeout(() => {
                resolve({ ok: true, json: async () => ({ success: true }) });
            }, 500);
        });

        /*
        try {
            const response = await fetch(`${API_CONFIG.BASE_URL}${endpoint}`, {
                ...options,
                headers
            });
            if (!response.ok) throw new Error('Network response was not ok');
            return await response.json();
        } catch (error) {
            console.error('API Error:', error);
            throw error;
        }
        */
    },

    getVitals() { return this.request('/vitals'); },
    updateVitals(data) { return this.request('/vitals', { method: 'POST', body: JSON.stringify(data) }); },
    getMatches() { return this.request('/matches'); }
};
