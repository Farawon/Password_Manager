import { apiFetch } from "./api.js"
const profileName = document.querySelector('.profileName')
const profilePlan = document.querySelector('.profilePlan')
const profileFullName = document.querySelector('.profileFullName')
const profileEmail = document.querySelector('.profileEmail')
async function loadProfile (){
    const response = await apiFetch('/user', {
        method: 'GET'
    })
    if(!response.ok) {
        console.log('failed' , response.status);
        
    }
    const user = await response.json()
    const fullName = `${user.firstName} ${user.lastName}`
    profileName.textContent = fullName
    profilePlan.textContent = `${user.plan || 'FREE'} Plan`
    profileFullName.value = fullName
    profileEmail.value = user.email
} 
loadProfile()