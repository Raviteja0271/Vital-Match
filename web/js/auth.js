/**
 * VitalMatch Authentication Service
 * Handles user login, registration, and session management.
 */

const AuthService = {
    // Simulate user database in localStorage
    getUsers: function() {
        const users = localStorage.getItem('vm_users');
        return users ? JSON.parse(users) : [];
    },

    saveUser: function(user) {
        const users = this.getUsers();
        users.push(user);
        localStorage.setItem('vm_users', JSON.stringify(users));
    },

    register: function(userData) {
        const users = this.getUsers();
        if (users.find(u => u.email === userData.email)) {
            return { success: false, message: 'Email already exists.' };
        }

        this.saveUser(userData);
        return { success: true, message: 'Account created successfully!' };
    },

    login: function(email, password) {
        const users = this.getUsers();
        const user = users.find(u => u.email === email && u.password === password);

        if (user) {
            // Set session
            localStorage.setItem('vm_session', JSON.stringify({
                email: user.email,
                firstName: user.firstName,
                lastName: user.lastName,
                loginTime: new Date().getTime()
            }));
            return { success: true };
        }

        return { success: false, message: 'Invalid email or password.' };
    },

    logout: function() {
        localStorage.removeItem('vm_session');
        window.location.href = 'index.html';
    },

    checkSession: function() {
        const session = localStorage.getItem('vm_session');
        if (!session && !window.location.pathname.includes('index.html') && !window.location.pathname.includes('register.html')) {
            window.location.href = 'index.html';
        }
        return session ? JSON.parse(session) : null;
    }
};

// Initialize listeners if on auth pages
document.addEventListener('DOMContentLoaded', () => {
    const loginForm = document.getElementById('loginForm');
    if (loginForm) {
        loginForm.addEventListener('submit', (e) => {
            e.preventDefault();
            const email = loginForm.querySelector('input[type="email"]').value;
            const password = loginForm.querySelector('input[type="password"]').value;

            const result = AuthService.login(email, password);
            if (result.success) {
                window.location.href = 'dashboard.html';
            } else {
                alert(result.message);
            }
        });
    }

    const registerForm = document.getElementById('registerForm');
    if (registerForm) {
        registerForm.addEventListener('submit', (e) => {
            e.preventDefault();
            const firstName = registerForm.querySelectorAll('input[type="text"]')[0].value;
            const lastName = registerForm.querySelectorAll('input[type="text"]')[1].value;
            const email = registerForm.querySelector('input[type="email"]').value;
            const password = registerForm.querySelector('input[type="password"]').value;

            const result = AuthService.register({ firstName, lastName, email, password });
            if (result.success) {
                alert(result.message);
                window.location.href = 'index.html';
            } else {
                alert(result.message);
            }
        });
    }

    const logoutBtn = document.getElementById('logoutBtn');
    if (logoutBtn) {
        logoutBtn.addEventListener('click', (e) => {
            e.preventDefault();
            AuthService.logout();
        });
    }
});
