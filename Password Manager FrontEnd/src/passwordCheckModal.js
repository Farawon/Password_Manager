let passwordInput = document.querySelector('.passwordInput')
let barsContainer = document.querySelector('.bars')
let barsStrength = document.querySelectorAll('.barStrength')
let strengthInfo = document.querySelector('.strengthInfo')
let strength = document.querySelector('.describeStrength')
let charsLength = document.querySelector('.charsLength')
export function passwordCheck(){
    let password = passwordInput.value
    let score = 0
    strengthInfo.style.display = 'block'
    if(password.length >= 8){
    score++
    }
    if (/[A-Z]/.test(password)) {
        score++
    }
    if (/[1-9]/.test(password)){
        score++
    }
    if (/[^\w\s]/.test(password)){
        score++
    }
    if(password.length < 8){
        score = 1
    }
    let charsCount = password.length
    if(charsCount > 0){
        charsLength.textContent = charsCount + ' chars'
    }
    if(score == 1){
        strength.textContent = 'Weak'
        strength.style.color = 'orange'
    }else if(score == 2 || score == 3){
        strength.textContent = 'Medium'
        strength.style.color = 'rgb(32, 49, 229)'
    }else if(score == 4){
        strength.textContent = 'Very Strong'
        strength.style.color ='rgb(16, 169, 86)'
    }
    barsStrength.forEach((bar, i) => {
    bar.style.backgroundColor = 'rgb(30, 30, 90)'
    if(i <= 0 && score == 1){
        bar.style.backgroundColor = 'rgb(245, 158, 11)'
    }if(i <= 1 && score == 2){
            bar.style.backgroundColor = 'rgb(79, 70, 229)'
    }if(i <= 2 && score == 3){
        bar.style.backgroundColor = 'rgb(79, 70, 229)'
    }
    if(i <= 3 && score == 4){
            bar.style.backgroundColor = 'rgb(16, 185, 129)'
    }
    if(password.length == 0){
    strengthInfo.style.display = 'none'
    }
    
})
}
passwordInput.addEventListener('input', passwordCheck)