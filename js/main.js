// ============================================
// VitalMatch - Main Application Logic
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

// Immediate Cached User UI (prevents "User Name" flash)
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

// Core init — runs on every protected page
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
    if (session && isAuth) {
        window.location.href = 'dashboard.html';
        return false;
    }
    if (session && typeof DB !== 'undefined') {
        currentProfile = await DB.getProfile(session.user.id);
        updateUI();
    }
    setupLogout();
    setupMobileSidebar();
    setupGlobalHeaderLinks();
    return true;
}

function setupGlobalHeaderLinks() {
    // 1. Profile Avatar/Name Click -> profile.html
    document.querySelectorAll('.user-avatar-display, .user-name-display').forEach(el => {
        const container = el.closest('.cursor-pointer') || el;
        container.style.cursor = 'pointer';
        container.onclick = (e) => {
            e.preventDefault();
            window.location.href = 'profile.html';
        };
    });

    // 2. Notification Bell Click -> notifications.html
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

    // Dynamic location header
    const locText = document.getElementById('userLocationText');
    if (locText) {
        const userLoc = [currentProfile.city, currentProfile.district, currentProfile.state].filter(Boolean).join(', ');
        locText.innerHTML = `<i class="fas fa-map-marker-alt text-primary me-1"></i> ${userLoc || 'Location not set'}`;
    }
    setupGlobalHeaderLinks();
}

function setupLogout() {
    document.querySelectorAll('a').forEach(link => {
        if (link.classList.contains('text-danger') &&
            (link.textContent.trim().includes('Logout') || link.getAttribute('href') === 'index.html')) {
            link.addEventListener('click', async (e) => {
                e.preventDefault();
                localStorage.removeItem('vm_user_name');
                await supabaseClient.auth.signOut();
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

function formatDate(d) {
    if (!d) return 'N/A';
    return new Date(d).toLocaleDateString('en-IN', { year:'numeric', month:'short', day:'numeric' });
}

function showToast(message, type = 'success') {
    const t = document.createElement('div');
    t.className = `alert alert-${type} alert-dismissible fade show position-fixed top-0 start-50 translate-middle-x mt-3 shadow`;
    t.style.zIndex = '9999'; t.style.minWidth = '300px';
    t.innerHTML = `${message}<button type="button" class="btn-close" data-bs-dismiss="alert"></button>`;
    document.body.appendChild(t);
    setTimeout(() => { if (t.parentNode) t.remove(); }, 4000);
}

async function toggleAvailability(checkbox) {
    const s = document.getElementById('availabilityStatus');
    if (s) {
        s.innerText = checkbox.checked ? 'Available' : 'Unavailable';
        s.className = 'form-check-label fw-bold small ms-2' + (checkbox.checked ? ' text-success' : ' text-danger');
    }
    if (currentProfile) await DB.updateProfile(currentProfile.id, { is_available: checkbox.checked });
}

// Auto-init on DOMContentLoaded
document.addEventListener('DOMContentLoaded', async () => {
    const ok = await initApp();
    if (!ok) return;
    if (typeof pageInit === 'function') await pageInit();
});
