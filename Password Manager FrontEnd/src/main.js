let burgerMenu = document.getElementById('burger')
let closeMenu = document.getElementById('cross')
const navigation = document.querySelector('nav')
burgerMenu.addEventListener('click' , function(){
   burgerMenu.style.display = "none" 
   closeMenu.style.display = "block"
   navigation.classList.add('activeBurger')
})
closeMenu.addEventListener('click' ,() => {
   closeMenu.style.display = "none" 
   burgerMenu.style.display = "block"
   navigation.classList.remove('activeBurger')
})
