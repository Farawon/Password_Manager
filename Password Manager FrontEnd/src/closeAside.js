const collapseIcon = document.querySelector('.fa-chevron-left')
const expandIcon = document.querySelector('.fa-chevron-right')
const aside = document.querySelector('.mainAside')
const main = document.querySelector('main')
const menuToggle = document.querySelector('.menuToggle')
const asideOverlay = document.querySelector('.asideOverlay')
const navItems = document.querySelectorAll('.mainAside .selectType')
const text = document.querySelectorAll('.mainAside .textDescription')
const vault = document.querySelector('.vaultText')
const vaultGreen = document.querySelector('.vaultPassiveGreen')
const vaultStatus = document.querySelector('.vaultSecured')
const logoText = document.querySelector('.logoText')
const logo = document.querySelector('.logoBkg')
const profileCard = document.querySelector('.profileCard')
const profileSettings = document.querySelector('.profileSettings')
const mobileQuery = window.matchMedia('(max-width: 760px)')

function setMobileMenu(isOpen) {
    aside.classList.toggle('mobileOpen', isOpen)
    asideOverlay.classList.toggle('isVisible', isOpen)
    menuToggle.setAttribute('aria-expanded', String(isOpen))
    menuToggle.setAttribute('aria-label', isOpen ? 'Close navigation' : 'Open navigation')
    document.body.classList.toggle('menuOpen', isOpen)
}

function setCollapsed(isCollapsed) {
    aside.classList.toggle('asideClosed', isCollapsed)
    main.classList.toggle('mainWhenAsideClosed', isCollapsed)
    text.forEach((item) => item.style.display = isCollapsed ? 'none' : '')
    vault.style.display = isCollapsed ? 'none' : ''
    vaultGreen.style.display = isCollapsed ? 'none' : ''
    logoText.classList.toggle('logoTextOff', isCollapsed)
    logo.classList.toggle('logoBkgOff', isCollapsed)
    vaultStatus.classList.toggle('vaultSecuredOff', isCollapsed)
    profileCard.classList.toggle('profileCardOff', isCollapsed)
    profileSettings.classList.toggle('profileSettingsOff', isCollapsed)
    collapseIcon.style.display = isCollapsed ? 'none' : 'block'
    expandIcon.style.display = isCollapsed ? 'block' : 'none'
}

collapseIcon.addEventListener('click', () => setCollapsed(true))
expandIcon.addEventListener('click', () => setCollapsed(false))
menuToggle.addEventListener('click', () => {
    setMobileMenu(!aside.classList.contains('mobileOpen'))
})
asideOverlay.addEventListener('click', () => setMobileMenu(false))
navItems.forEach((item) => {
    item.addEventListener('click', () => {
        navItems.forEach((navItem) => navItem.classList.remove('selectTypeActive'))
        item.classList.add('selectTypeActive')
        if (mobileQuery.matches) setMobileMenu(false)
    })
})
document.addEventListener('keydown', (event) => {
    if (event.key === 'Escape') setMobileMenu(false)
})
mobileQuery.addEventListener('change', () => setMobileMenu(false))