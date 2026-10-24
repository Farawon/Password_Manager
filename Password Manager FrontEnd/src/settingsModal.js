let settingsBtn = document.querySelector('.gear')
let modalSettings = document.querySelector('.modalSettings')
let modalBkgSettings = document.querySelector('.settingsBkg')
let settingsCloseIcon = document.querySelector('.settingsCloseIcon')
let settingsCloseBtn = document.querySelector('.settingsCloseBtn')
let btnProfile = document.querySelector('.profile')
let btnBilling = document.querySelector('.billing')
let profileSection = document.querySelector('.profileSection')
let billingSection = document.querySelector('.billingSection')
settingsBtn.addEventListener('click', () => {
    modalSettings.style.toggle = 'flex'
    modalBkgSettings.style.display = 'block'
})
modalBkgSettings.addEventListener('click',() => {
    modalSettings.style.display = 'none'
    modalBkgSettings.style.display = 'none'
})
settingsCloseBtn.addEventListener('click',() => {
    modalSettings.style.display = 'none'
    modalBkgSettings.style.display = 'none'
})
settingsCloseIcon.addEventListener('click',() => {
    modalSettings.style.display = 'none'
    modalBkgSettings.style.display = 'none'
})
settingsBtn.addEventListener('click', () => {
    modalSettings.style.display = 'flex';
    modalBkgSettings.style.display = 'block';
});
btnProfile.addEventListener('click', () => {
    profileSection.style.display = 'block';
    billingSection.style.display = 'none';

    btnProfile.classList.add('active');
    btnBilling.classList.remove('active');
});

btnBilling.addEventListener('click', () => {
    profileSection.style.display = 'none';
    billingSection.style.display = 'block';

    btnBilling.classList.add('active');
    btnProfile.classList.remove('active');
});