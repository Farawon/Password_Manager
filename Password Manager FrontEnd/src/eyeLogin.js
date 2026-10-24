let passwordInputEye = document.querySelector('.passwordInput')
let openEyesModal = document.querySelector('.fa-eye')
let closeEyesModal = document.querySelector('.fa-eye-slash')

openEyesModal.addEventListener('click',() => {
    openEyesModal.style.display = 'none'
    closeEyesModal.style.display = 'block'
    passwordInputEye.type = 'text'
})
closeEyesModal.addEventListener('click',() => {
    openEyesModal.style.display = 'block'
    closeEyesModal.style.display = 'none'
    passwordInputEye.type = 'password'
})
