let btnAdd = document.querySelector('.btnAdd')
let modalWindow = document.querySelector('.modalWindow')
let modalBkg = document.querySelector('.modalBackground')
const crossIcon = document.querySelector('.fa-xmark')
let cancelBtn  = document.querySelector('.cancelBtn')
btnAdd.addEventListener('click', () => {
    modalWindow.style.display = 'block'
    modalBkg.style.display = 'block'
})
crossIcon.addEventListener('click', () => {
    modalWindow.style.display = 'none'
    modalBkg.style.display = 'none'
})
modalBkg.addEventListener('click', () => {
    modalWindow.style.display = 'none'
    modalBkg.style.display = 'none'
})
cancelBtn.addEventListener('click', () => {
    modalWindow.style.display = 'none'
    modalBkg.style.display = 'none'
})