import { emailCheck } from "./emailCheck.js"
import { getPasswordScore } from "./getPasswordScore.js"
import { API_BASE } from "./api.js"

let checkBox = document.querySelector(".inputCheckBox")
const form = document.querySelector("#loginForm")

form.addEventListener("submit", async (event) => {
    event.preventDefault()
    if (getPasswordScore(form.masterpass.value) !== 4) {
        form.masterpass.style.borderColor = "red"
        return
    }
    if (!emailCheck(form.email.value)) {
        form.email.style.borderColor = "red"
        return
    }
    let parts = form.fullName.value.split(" ")
    const firstName = parts[0]
    const lastName = parts[1]
    if (form.masterpass.value !== form.masterpassconfirm.value) {
        form.masterpass.style.borderColor = "red"
        form.masterpassconfirm.style.borderColor = "red"
        return
    }
    if (firstName == undefined || lastName == undefined) {
        form.fullName.style.borderColor = "red"
        return
    }
    if (!checkBox.checked) {
        return
    }
    const user_date = {
        firstName: firstName,
        lastName: lastName,
        email: form.email.value,
        rawPassword: form.masterpass.value,
        confirmPassword: form.masterpassconfirm.value,
        isTermsAccepted: checkBox.checked,
    }

    const response = await fetch(`${API_BASE}/auth/register`, {
        method: "POST",
        credentials: "include",
        headers: {
            "Content-Type": "application/json",
            "ngrok-skip-browser-warning": "true",
        },
        body: JSON.stringify(user_date),
    })
    if (!response.ok) {
        console.error("Register failed", response.status, await response.text())
        return
    }
    window.location.href = "login.html"
})
