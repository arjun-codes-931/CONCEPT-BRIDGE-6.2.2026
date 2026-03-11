// js/auth.js
console.log("auth.js loaded");

// Check if loginBtn exists before adding event listener
if (document.getElementById("loginBtn")) {
    document.getElementById("loginBtn").addEventListener("click", login);
}

async function login() {
    console.log("Login clicked");

    const username = document.getElementById("username").value;
    const password = document.getElementById("password").value;

    try {
        const res = await fetch("http://localhost:8080/auth/login", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ username, password })
        });

        if (!res.ok) {
            alert("Login failed: Invalid credentials");
            return;
        }

        const data = await res.json();
        console.log("Backend response:", data);

        localStorage.setItem("token", data.token);
        localStorage.setItem("role", data.role);
        localStorage.setItem("username", username); // Store username

        redirectByRole(data.role);

    } catch (e) {
        console.error("Login error:", e);
        alert("Login error: " + e.message);
    }
}

function redirectByRole(role) {
    console.log("Redirecting, role =", role);

    if (!role) {
        alert("No role received");
        return;
    }

    if (role.includes("STUDENT")) {
        window.location.href = "student/dashboard.html";
    } 
    else if (role.includes("TEACHER")) {
        window.location.href = "teacher/dashboard.html";
    } 
    else if (role.includes("ADMIN")) {
        window.location.href = "admin/dashboard.html";
    } 
    else {
        alert("Unknown role: " + role);
    }
}

// Get username from token
function getUsernameFromToken() {
    const token = localStorage.getItem('token');
    if (!token) return null;
    
    try {
        const payload = JSON.parse(atob(token.split('.')[1]));
        return payload.sub; // Username is in 'sub' field
    } catch (error) {
        console.error('Token decode error:', error);
        return localStorage.getItem('username'); // Fallback
    }
}

// Check if user is logged in
function checkAuth() {
    const token = localStorage.getItem('token');
    if (!token) {
        window.location.href = 'login.html';
        return false;
    }
    return true;
}

// Logout
function logout() {
    localStorage.clear();
    window.location.href = 'login.html';
}

// Add to window
window.getUsernameFromToken = getUsernameFromToken;
window.checkAuth = checkAuth;
window.logout = logout;