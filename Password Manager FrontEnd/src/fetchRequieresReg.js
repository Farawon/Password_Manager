let full_name = document.querySelector('.nameInput')
let email = document.querySelector('.emailInput')
let password = document.querySelector('#passwordCheck')
let password_confirm = document.querySelector('#masterPasswordConfirm')
let btn_create = document.querySelector('.btnCreate')
let checkBox = document.querySelector('.inputCheckBox')
let parts = full_name.split(' ')
const firstName = parts[0]
const lastName = parts[1]
btn_create.addEventListener('click', async () => {
    const user_date = {
        firstName: firstName,
        lastName: lastName,
        email: email,
        rawPassword: password,
        confirmPassword: password_confirm,
        

    }
    const response = await fetch ('', {
        method:'POST',
        headers: {
            'Content-Type': "application/json"
            ,
        },
        body: JSON.stringify(user_date)
    })
    const date = response.json();
})
