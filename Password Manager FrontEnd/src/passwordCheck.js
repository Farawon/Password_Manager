let masterPassword = document.getElementById('masterPasswordInput')
let masterPasswordConfirm = document.querySelector('#masterPasswordConfirm')
let inputBox = document.getElementById('passwordCheck')
let strength = document.getElementById('strength')
let symbolsReqirements = document.getElementById('symbolsReqirements')
let length = document.getElementById('length')
let uppercase = document.getElementById('uppercase')
let number = document.getElementById('number')
let special = document.getElementById('special')
let bars = document.querySelectorAll('.bar')
let btnCreate = document.querySelector('.btnCreate')
masterPassword.addEventListener('input', function(){
    inputBox.style.display = 'block'
    let password = masterPassword.value
    let score = 0
    if(password.length >= 8){
    length.style.color = 'rgb(16, 185, 129)'
    score++
    }else{
        length.style.color = 'rgb(142, 182, 212)'
    }
    if (/[A-Z]/.test(password)) {
        uppercase.style.color = 'rgb(16, 185, 129)'
        score++
    }else{
        uppercase.style.color = 'rgb(142, 182, 212)'
    }
    if (/[1-9]/.test(password)){
        number.style.color = 'rgb(16, 185, 129)'
        score++
    }else{
        number.style.color = 'rgb(142, 182, 212)'
    }
    if (/[^\w\s]/.test(password)){
        special.style.color = 'rgb(16, 185, 129)'
        score++
    }else{
        special.style.color = 'rgb(142, 182, 212)'
    }
    let SymbLeft = 8 - password.length 
    if(SymbLeft > 0){
        symbolsReqirements.textContent = SymbLeft + ' more chars needed'
    }
    else{
        symbolsReqirements.style.display = 'none'
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
    bars.forEach((bar, i) => {
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

    })
    if(password.length == 0){
        inputBox.style.display = 'none'
    }
})