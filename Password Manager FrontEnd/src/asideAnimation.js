let selectType = document.querySelectorAll('.selectType')
selectType.forEach(element => {
    element.addEventListener('click', () => {
        selectType.forEach(item => {
            item.classList.remove('selectTypeActive')
        })
        element.classList.add('selectTypeActive')
    })
});