/**
 * VitalMatch Application Logic
 * Handles dashboard data, vital tracking, and matching algorithms.
 */

const App = {
    // Initial data for demonstration
    vitals: [
        { date: '2023-10-24', heartRate: 72, bp: '120/80', spo2: 98, status: 'Normal' },
        { date: '2023-10-23', heartRate: 75, bp: '122/82', spo2: 97, status: 'Normal' },
        { date: '2023-10-22', heartRate: 88, bp: '130/85', spo2: 96, status: 'Elevated' }
    ],

    matches: [
        { name: 'Sarah Miller', bio: 'Cardio & Yoga Enthusiast', tags: ['Morning Person', 'Healthy Eater'], score: 98, color: '#0052cc' },
        { name: 'Michael Chen', bio: 'Strength Training Specialist', tags: ['Evening Workouts', 'Marathon Runner'], score: 92, color: '#60a5fa' },
        { name: 'Emma Wilson', bio: 'Meditation & Mental Health', tags: ['Mindfulness', 'Vegetarian'], score: 89, color: '#a855f7' }
    ],

    init: function() {
        console.log('VitalMatch App Initialized');
        this.updateUserInfo();
        this.renderDashboardStats();
        this.handleVitalForm();
    },

    updateUserInfo: function() {
        const session = AuthService.checkSession();
        if (session) {
            const userNames = document.querySelectorAll('.user-name');
            userNames.forEach(el => el.textContent = `${session.firstName} ${session.lastName}`);

            const welcomeText = document.querySelector('.welcome-text');
            if (welcomeText) welcomeText.textContent = `Welcome back, ${session.firstName}!`;
        }
    },

    renderDashboardStats: function() {
        // In a real app, this would fetch from an API
        // For now, we use the hardcoded vitals array
        const latestVital = this.vitals[0];
        const heartRateEl = document.getElementById('heartRateVal');
        if (heartRateEl) heartRateEl.textContent = `${latestVital.heartRate} BPM`;
    },

    handleVitalForm: function() {
        const vitalForm = document.getElementById('vitalForm');
        if (vitalForm) {
            vitalForm.addEventListener('submit', (e) => {
                e.preventDefault();
                const newVital = {
                    date: new Date().toLocaleDateString('en-US', { month: 'short', day: 'numeric', year: 'numeric' }),
                    heartRate: parseInt(vitalForm.querySelector('input[placeholder*="Heart Rate"]').value),
                    bp: `${vitalForm.querySelectorAll('input')[1].value}/${vitalForm.querySelectorAll('input')[1].value - 40}`, // Simplified BP
                    spo2: parseInt(vitalForm.querySelector('input[placeholder*="Oxygen"]').value),
                    status: 'Normal'
                };

                this.vitals.unshift(newVital);
                alert('Vital record saved successfully!');
                window.location.reload(); // Refresh to see new data
            });
        }
    }
};

document.addEventListener('DOMContentLoaded', () => {
    App.init();
});
