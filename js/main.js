// ============================================
// VitalMatch - Main Application Logic
// GPS-Based Real-Time Location Tracking
// Requires: supabase-config.js + database.js
// ============================================

let currentProfile = null;

// Theme Management
function applyTheme(theme) {
    if (theme === 'dark') {
        document.documentElement.classList.add('dark-mode');
        if (document.body) document.body.classList.add('dark-mode');
    } else {
        document.documentElement.classList.remove('dark-mode');
        if (document.body) document.body.classList.remove('dark-mode');
    }
    localStorage.setItem('vm_theme', theme);
}

function initTheme() {
    const savedTheme = localStorage.getItem('vm_theme') || 'light';
    applyTheme(savedTheme);
}
initTheme();

function initCachedUserUI() {
    const cachedName = localStorage.getItem('vm_user_name');
    if (cachedName) {
        document.querySelectorAll('.user-name-display').forEach(el => { el.innerText = cachedName; });
        const welcomeEl = document.getElementById('userWelcomeName');
        if (welcomeEl) welcomeEl.innerText = cachedName;
        document.querySelectorAll('.user-avatar-display').forEach(img => {
            img.src = `https://ui-avatars.com/api/?name=${encodeURIComponent(cachedName)}&background=E53935&color=fff`;
        });
    }
}
initCachedUserUI();

// Core init
async function initApp() {
    initTheme();
    initCachedUserUI();
    if (typeof applyPageLanguage === 'function') applyPageLanguage();

    const session = await supabaseGetSession();
    const path = window.location.pathname;
    const isAuth = ['index.html','register.html','otp-verify.html'].some(p => path.endsWith(p))
                   || path.endsWith('/') || path === '';
    const isSuccess = path.endsWith('success.html');

    if (!session && !isAuth && !isSuccess) {
        window.location.href = 'index.html';
        return false;
    }
    if (session && typeof DB !== 'undefined') {
        currentProfile = await DB.getProfile(session.user.id);

        if (!currentProfile) {
            console.warn('Profile deleted from database. Terminating session...');
            await supabaseSignOut();
            localStorage.clear();
            alert('Your account data was cleared from the database. Please register a new account.');
            window.location.href = 'index.html';
            return false;
        }
    }

    updateUI();
    setupLogout();
    setupMobileSidebar();
    setupGlobalHeaderLinks();

    // Request permissions (Notifications & Location) on login
    requestAllPermissionsOnLogin();
    return true;
}

async function requestAllPermissionsOnLogin() {
    if ('Notification' in window && Notification.permission !== 'granted' && Notification.permission !== 'denied') {
        try {
            await Notification.requestPermission();
        } catch (e) {
            console.warn('Notification permission error:', e);
        }
    }
    trackLiveGPSLocation();
}

function setupGlobalHeaderLinks() {
    document.querySelectorAll('.user-avatar-display, .user-name-display').forEach(el => {
        const container = el.closest('.cursor-pointer') || el;
        container.style.cursor = 'pointer';
        container.onclick = (e) => {
            e.preventDefault();
            window.location.href = 'profile.html';
        };
    });
    document.querySelectorAll('.fa-bell').forEach(el => {
        if (el.closest('.sidebar-link')) return;
        const container = el.closest('.position-relative') || el.parentElement || el;
        container.style.cursor = 'pointer';
        container.onclick = (e) => {
            e.preventDefault();
            window.location.href = 'notifications.html';
        };
    });
}

function updateUI() {
    if (!currentProfile) return;
    const name = currentProfile.full_name || 'User';
    localStorage.setItem('vm_user_name', name);
    document.querySelectorAll('.user-name-display').forEach(el => { el.innerText = name; });
    const welcomeEl = document.getElementById('userWelcomeName');
    if (welcomeEl) welcomeEl.innerText = name;
    document.querySelectorAll('.user-avatar-display').forEach(img => {
        img.src = `https://ui-avatars.com/api/?name=${encodeURIComponent(name)}&background=E53935&color=fff`;
    });
    setupGlobalHeaderLinks();
    // DO NOT set location from profile here — GPS handles it
}

// =====================================================
// LIVE GPS LOCATION TRACKER — uses device GPS only
// =====================================================
function trackLiveGPSLocation() {
    const locEl = document.getElementById('userLocationText');
    if (!locEl) return;

    // Step 1: Show loading spinner
    locEl.innerHTML = '<i class="fas fa-spinner fa-spin text-danger me-1"></i> <span class="fw-bold text-muted">Detecting your GPS location...</span>';

    // Step 2: Check if browser supports geolocation
    if (!navigator.geolocation) {
        locEl.innerHTML = '<i class="fas fa-exclamation-triangle text-warning me-1"></i> <span class="fw-bold text-dark">GPS not supported by this browser</span>';
        return;
    }

    // Step 3: Request real-time GPS position from device
    navigator.geolocation.getCurrentPosition(
        // SUCCESS — got GPS coordinates
        async function(position) {
            const lat = position.coords.latitude;
            const lng = position.coords.longitude;

            locEl.innerHTML = '<i class="fas fa-spinner fa-spin text-danger me-1"></i> <span class="fw-bold text-muted">Resolving address from GPS...</span>';

            let locationName = '';

            // Method 1: Use Google Geocoding API with LOCATION_API_KEY
            if (typeof LOCATION_API_KEY !== 'undefined' && LOCATION_API_KEY && LOCATION_API_KEY.length > 10) {
                try {
                    const resp = await fetch('https://maps.googleapis.com/maps/api/geocode/json?latlng=' + lat + ',' + lng + '&key=' + LOCATION_API_KEY);
                    const gdata = await resp.json();
                    if (gdata && gdata.results && gdata.results.length > 0) {
                        const comps = gdata.results[0].address_components;
                        const city = comps.find(c => c.types.includes('locality'));
                        const district = comps.find(c => c.types.includes('administrative_area_level_2'));
                        const state = comps.find(c => c.types.includes('administrative_area_level_1'));
                        const parts = [
                            city ? city.long_name : null,
                            district ? district.long_name : null,
                            state ? state.long_name : null
                        ].filter((v, i, a) => v && a.indexOf(v) === i);
                        locationName = parts.join(', ');
                    }
                } catch (e) {
                    console.warn('Google Geocoding failed, trying fallback:', e);
                }
            }

            // Method 2: Free reverse geocoding API as fallback
            if (!locationName) {
                try {
                    const resp2 = await fetch('https://api.bigdatacloud.net/data/reverse-geocode-client?latitude=' + lat + '&longitude=' + lng + '&localityLanguage=en');
                    const fdata = await resp2.json();
                    if (fdata) {
                        const city = fdata.locality || fdata.city || '';
                        const state = fdata.principalSubdivision || '';
                        locationName = [city, state].filter(Boolean).join(', ');
                    }
                } catch (e2) {
                    console.warn('Fallback geocoding also failed:', e2);
                }
            }

            // Method 3: Raw coordinates if everything fails
            if (!locationName) {
                locationName = 'Lat: ' + lat.toFixed(4) + ', Lng: ' + lng.toFixed(4);
            }

            // Display the live GPS location
            locEl.innerHTML = '<i class="fas fa-crosshairs text-danger me-1"></i> <span class="fw-bold text-dark">' + locationName + '</span> <span class="badge bg-success bg-opacity-10 text-success rounded-pill ms-1" style="font-size:10px;">● Live GPS</span>';

            // Also save coordinates to user profile in Supabase
            if (currentProfile && currentProfile.id && typeof supabaseClient !== 'undefined') {
                try {
                    await supabaseClient.from('profiles').update({
                        latitude: lat,
                        longitude: lng
                    }).eq('id', currentProfile.id);
                } catch (e) {}
            }
        },

        // ERROR — GPS denied or unavailable
        function(error) {
            console.warn('GPS Error:', error.code, error.message);
            let msg = 'Location access denied';
            if (error.code === 1) msg = 'Please allow location access in your browser';
            if (error.code === 2) msg = 'GPS position unavailable';
            if (error.code === 3) msg = 'GPS request timed out';
            locEl.innerHTML = '<i class="fas fa-exclamation-circle text-warning me-1"></i> <span class="fw-bold text-dark">' + msg + '</span>';
        },

        // OPTIONS — high accuracy, no cache
        {
            enableHighAccuracy: true,
            timeout: 15000,
            maximumAge: 0
        }
    );
}

function setupLogout() {
    document.querySelectorAll('a').forEach(link => {
        if (link.classList.contains('text-danger') &&
            (link.textContent.trim().includes('Logout') || link.getAttribute('href') === 'index.html')) {
            link.addEventListener('click', async (e) => {
                e.preventDefault();
                localStorage.clear();
                try {
                    await supabaseClient.auth.signOut();
                } catch (err) {}
                window.location.href = 'index.html';
            });
        }
    });
}

function setupMobileSidebar() {
    const toggleBtn = document.querySelector('.btn-mobile-sidebar');
    const sidebar = document.querySelector('.sidebar');
    if (toggleBtn && sidebar) {
        toggleBtn.addEventListener('click', () => {
            sidebar.classList.toggle('show');
        });
    }
}

// === UTILITIES ===
function formatDate(dateString) {
    if (!dateString) return 'Never';
    try {
        const d = new Date(dateString);
        if (isNaN(d.getTime())) return dateString;
        return d.toLocaleDateString('en-IN', { year: 'numeric', month: 'short', day: 'numeric' });
    } catch (e) {
        return dateString;
    }
}

function timeAgo(dateString) {
    const now = new Date(), date = new Date(dateString);
    const mins = Math.floor((now - date) / 60000);
    if (mins < 1) return 'Just now';
    if (mins < 60) return `${mins} mins ago`;
    const hrs = Math.floor(mins / 60);
    if (hrs < 24) return `${hrs} hours ago`;
    const days = Math.floor(hrs / 24);
    if (days < 30) return `${days} days ago`;
    return date.toLocaleDateString('en-IN', { year:'numeric', month:'short', day:'numeric' });
}

// Auto-run initialization on DOM ready
document.addEventListener('DOMContentLoaded', async () => {
    await initApp();
    if (typeof pageInit === 'function') {
        try {
            await pageInit();
        } catch (e) {
            console.error('pageInit execution warning:', e);
        }
    }
});
