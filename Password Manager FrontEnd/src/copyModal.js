let copyModal = document.querySelector('.fa-copy')
let passCopy = document.querySelector('.passwordInput')
copyModal.addEventListener('click', async () => {
    await navigator.clipboard.writeText(passCopy.value)
})