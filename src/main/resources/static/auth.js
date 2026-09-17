const registerForm = document.getElementById('registerForm');
const loginForm = document.getElementById('loginForm');
const message = document.getElementById('message');

if (registerForm) {
    registerForm.addEventListener('submit', async (e) => {
        e.preventDefault();
        const body = {
            fullName: document.getElementById('fullName').value,
            email: document.getElementById('email').value,
            phone: document.getElementById('phone').value,
            password: document.getElementById('password').value,
            flatNumber: document.getElementById('flatNumber').value
        };
        const res = await fetch('/api/auth/register', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(body)
        });
        const data = await res.json();
        if (res.ok) {
            localStorage.setItem('token', data.token);
            message.style.color = 'green';
            message.textContent = 'Registered! Redirecting to login...';
            setTimeout(() => window.location.href = '/login', 1000);
        } else {
            message.textContent = data.error || Object.values(data)[0];
        }
    });
}

if (loginForm) {
    loginForm.addEventListener('submit', async (e) => {
        e.preventDefault();
        const body = {
            email: document.getElementById('email').value,
            password: document.getElementById('password').value
        };
        const res = await fetch('/api/auth/login', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(body)
        });
        const data = await res.json();
        if (res.ok) {
            localStorage.setItem('token', data.token);
            message.style.color = 'green';
            message.textContent = 'Login successful!';
        } else {
            message.textContent = data.error || 'Login failed';
        }
    });
}