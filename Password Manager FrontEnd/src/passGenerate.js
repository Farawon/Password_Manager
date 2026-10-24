import { passwordCheck } from "./passwordCheckModal.js"
let passwordInputGenerate = document.querySelector('.passwordInput')
let btnGenerate = document.querySelector('.btnGenerate')
let lengthSlider = document.querySelector('#lengthSlider')
let amountChars = document.querySelector('.amountChars')
let characters = 'abcdefghijzklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890!@#$%^&*()_+{}":?><'
lengthSlider.addEventListener('input',() => {
    amountChars.textContent = `${lengthSlider.value} chars`;
})
btnGenerate.addEventListener('click',() => {
    let password = ''
    for (let i = 0; i < lengthSlider.value; i++) {
        let randomIndex = Math.floor(Math.random() * characters.length)
        password += characters[randomIndex]
        
    }
    console.log(password);
    passwordInputGenerate.value = password
    passwordCheck()
})
