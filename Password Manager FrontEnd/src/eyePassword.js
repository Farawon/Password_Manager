let passwordInput = document.querySelectorAll('.passwordInput')
let openEyes = document.querySelectorAll('.fa-eye')
let closeEyes = document.querySelectorAll('.fa-eye-slash')
openEyes.forEach((openEye, index) => {
    openEye.addEventListener('click', function() {
        openEye.style.visibility = 'hidden';
        if (closeEyes[index]) closeEyes[index].style.visibility = 'visible';
        if (passwordInput[index]) passwordInput[index].type = 'text';
    });
});
closeEyes.forEach((closeEye, index) => {
    closeEye.addEventListener('click',function(){
        closeEye.style.visibility = 'hidden'
        if(openEyes[index]) openEyes[index].style.visibility = 
        'visible';
        if(passwordInput[index]) passwordInput[index].type =
        'password'
    } )
})