import { emailCheck } from "./emailCheck.js"
import { getPasswordScore } from "./getPasswordScore.js"
import { API_BASE, setAccessToken } from "./api.js"

const form = document.querySelector("#loginForm")

form.addEventListener("submit", async (event) => {
    event.preventDefault()
    if (getPasswordScore(form.masterpass.value) !== 4 || !emailCheck(form.email.value)) {
        form.masterpass.style.borderColor = "red"
        form.email.style.borderColor = "red"
        return
    }
    const user_date = {
        email: form.email.value,
        rawPassword: form.masterpass.value,
    }
    const response = await fetch(`${API_BASE}/auth/login`, {
        method: "POST",
        credentials: "include",
        headers: {
            "Content-Type": "application/json",
            "ngrok-skip-browser-warning": "true",
        },
        body: JSON.stringify(user_date),
    })
    if (!response.ok) {
        form.masterpass.style.borderColor = "red"
        form.email.style.borderColor = "red"
        console.error("Login failed", response.status, await response.text())
        return
    }
    const date = await response.json()
    setAccessToken(date.accessToken)
    window.location.href = "dashboard.html"
})
