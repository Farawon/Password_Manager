import { apiFetch, ensureAccessToken, logout } from "./api.js"
import {passwordCheck} from './passwordCheckModal.js'
import { emailCheck } from "./emailCheck.js"
let containerPass = document.querySelector(".containerPass")
let btnAddPass = document.querySelector(".BtnaddVault")
let nameSite = document.querySelector("#siteNameInput")
let urlSite = document.querySelector("#urlInput")
let userEmail = document.querySelector("#userEmailInput")
let passModal = document.querySelector("#passInputModal")
let categoryList = document.querySelector("#categorySelect")
let signOut = document.querySelector(".signOut")
let strengthBars = document.querySelector('.strengthBars')
let bars = document.querySelectorAll('.barRowPass')
let totalPass = document.querySelector('.totalPassword')
let strongPass = document.querySelector('.strongPasswordAmount')
let weakPass = document.querySelector('.weakPasswordAmount')
let passEntries = document.querySelector('.amountPasswords')
let modalWindow = document.querySelector('.modalWindow')
let modalBkg = document.querySelector('.modalBackground')
let selectionGroup = document.querySelector('.selectionGroup')
let selectsType = document.querySelectorAll('.selectType')
let activeCategory = "ALL";
let searchInput = document.querySelector('.searchInputHeader')
let passRow
let threePoint
let modalEditTool
let modalBkgEdit
let visitSite
let responseCredential = []
let btnSync = document.querySelector('.btnSync')


if (signOut) {
    signOut.addEventListener("click", () => logout())
}

async function loadCredentials() {
    await ensureAccessToken()
    const response = await apiFetch("/credentials", { method: "GET" })
    if (!response.ok) {
        console.error("Load credentials failed", response.status)
        return
    }
    const items = await response.json()
    responseCredential = items
    containerPass.innerHTML = ""
    items.forEach((item) => htmlRowPass(item))
    updateTotalPass()
    filterPasswords();
    updateTotalPass();  
    
}

btnAddPass.addEventListener("click", async () => {
    if (nameSite.value.length < 3) {
        nameSite.style.borderColor = "red";
    } else {
        nameSite.style.borderColor = "";
    }
    if (!urlSite.value.startsWith("https://")) {
        urlSite.style.borderColor = "red";
    } else {
        urlSite.style.borderColor = "";
    }
    if (!emailCheck(userEmail.value)) {
        userEmail.style.borderColor = "red";
    } else {
        userEmail.style.borderColor = "";
    }
    if (passModal.value.length < 8) {
        passModal.style.borderColor = "red";
    } else {
        passModal.style.borderColor = "";
    }
    if (nameSite.value.length < 3 || !urlSite.value.startsWith("https://") ||   !emailCheck(userEmail.value) || passModal.value.length < 8) {
        return;
    }
    let obj = {
        name: nameSite.value,
        website: urlSite.value,
        email: userEmail.value,
        rawPassword: passModal.value,
        credentialCategories: categoryList.value,
    }

    const requries = await apiFetch("/credentials", {
        method: "POST",
        body: JSON.stringify(obj),
    })

    if (!requries.ok) {
        console.error("Create credential failed", requries.status, await requries.text())
        return
    }
    let data = await requries.json()
    
    htmlRowPass({ ...data, rawPassword: passModal.value })
    filterPasswords();
})

function htmlRowPass(data) {
    let datesAmount = data.updatedAt
    let newData = new Date()
    let minutesTime = (Math.floor((new Date() - new Date(datesAmount)) / ( 1000 * 60) ));
    let hoursAgo = Math.floor((new Date() - new Date(datesAmount)) / ( 1000 * 60 * 60))
    let daysAgo = Math.floor((new Date() - new Date(datesAmount)) / ( 1000 * 60 * 60 * 24))
    if(hoursAgo > 24){
        datesAmount = daysAgo + ' days ago'
    }else if(minutesTime < 60){
        datesAmount = minutesTime + ' minutes ago'
    }else if(minutesTime >= 60){
        datesAmount = hoursAgo + ' hours ago'
    }
    containerPass.innerHTML += `
     <div class="passwordRow" data-id = '${data.id}'>  
        <div class="siteInfo">
            <div style = "background-color: ${randomColor()} " class="siteIcon">${data.name[0].toUpperCase()}</div>
            <div class="nameAndCategory">
                <p class = 'passwordName'>${data.name}</p>
                <span class = 'crdCategory'>${data.credentialCategories}</span>
            </div>
        </div>
        <span class="userEmail">${data.email}</span>
        <div class="credentialPassword">
            <input type="password" class="passwordCopy" readonly disabled value="*********">
        </div>
        <div class = 'strengthContainer'>
            <div class = 'iconCopyEye'>
                <i class="fa-regular fa-eye eyeRowPass"></i>
                <i class="fa-regular fa-clone fa-rotate-90 copyBoard"></i>
            </div>
            <div class="strength">
                <div class="strengthBars">
                    <span class = 'barRowPass'></span>
                    <span class = 'barRowPass'></span>
                    <span class = 'barRowPass'></span>
                </div>
                <p class="passStrength">${data.passwordStrength || "WEAK"}</p>
            </div>
        </div>
        <span class="dayAmount">${datesAmount || data.updatedAt}</span>
        <i class="fa-solid fa-ellipsis fa-rotate-90"></i>
        <div class = 'bkgModalRow'></div>
            <div class="modalEditTool">
                <div class="tools">
                    <div class = 'toolType editPass'>
                        <i class="fa-solid fa-pen"></i>
                        <p>Edit</p>
                    </div>
                    <div class = 'toolType visitSite' data-url = '${data.website}'>
                        <i class="fa-solid fa-arrow-up-right-from-square"></i>
                        <p>Visit Site</p>
                    </div>
                    <span class="divider"></span>
                    <div class = 'toolTypeDelete deleteBtn'>
                        <i class="fa-solid fa-trash-can"></i>
                        <p>Delete</p>
                    </div>
                </div>
            </div>
    </div>
    `
        threePoint = document.querySelectorAll('.fa-ellipsis')
        modalBkgEdit = containerPass.querySelectorAll('.bkgModalRow')
        visitSite = containerPass.querySelectorAll('.visitSite')

        searchInput.addEventListener('input', () => {
        const searchValue = searchInput.value.toLowerCase();
        const filterPass = responseCredential.filter(data => 
            data.name.toLowerCase().includes(searchValue)
        );
        const rows = containerPass.querySelectorAll(".passwordRow");
        rows.forEach(row => {
            const namePass = row.querySelector('.passwordName').textContent.toLowerCase()
        filterPass.forEach(data => {
            if(namePass.includes(searchValue)){
                row.style.display = ''
            }else{
                row.style.display = 'none'
            }
        })
    })
    })
        threePoint.forEach(i => {
            i.addEventListener('click', (event) => {
                let modalActive = event.target.parentElement.querySelector('.modalEditTool')
                let modalBkgEditActive = event.target.parentElement.querySelector('.bkgModalRow')
                modalActive.style.display = 'block'
                modalBkgEditActive.style.display = 'block'
            })
        })
        modalBkgEdit.forEach(item => {
            item.addEventListener('click', (event) => {
                let modalBkgEditActive = event.target.parentElement.querySelector('.bkgModalRow')
                let bkgClosest = event.target.closest('.passwordRow')
                let bkgStyle = bkgClosest.querySelector('.modalEditTool')
                bkgStyle.style.display = 'none'
                modalBkgEditActive.style.display = 'none'
            })
        })
        let row = containerPass.lastElementChild
        let bars = row.querySelectorAll(".barRowPass");
        let passwordStrength = data.passwordStrength;
        if (passwordStrength === "WEAK") {
            bars[0].style.backgroundColor = "orange";

        }
        if (passwordStrength === "MEDIUM") {
            bars[0].style.backgroundColor = "blue";
            bars[1].style.backgroundColor = "blue";
        }
        if (passwordStrength === "STRONG") {
            bars[0].style.backgroundColor = "rgb(16, 185, 129)";
            bars[1].style.backgroundColor = "rgb(16, 185, 129)";
            bars[2].style.backgroundColor = "rgb(16, 185, 129)";
        }
        containerPass.addEventListener("click", async (event) => {
            if (event.target.closest(".eyeRowPass")) {
            const passRow = event.target.closest(".passwordRow")
            const password = passRow.querySelector(".passwordCopy")
            if (password.value === "*********") {
                const response = await apiFetch(
                    `/credentials/password/${passRow.dataset.id}`,
                    { method: "GET" }
                )
                if (!response.ok) {
                    console.error("Get password failed:", response.status)
                    return
                }
                password.value = await response.text()
            }
            password.type = password.type === "password" ? "text" : "password"
        }
            const visitButton = event.target.closest(".visitSite");

            if (visitButton) {
                const url = visitButton.dataset.url;
                window.open(url, "_blank");
            }
            const editPass = event.target.closest('.editPass')
            if(!editPass) return;
            editPass.addEventListener('click', () => {
                modalWindow.style.display = 'block'
                modalBkg.style.display = 'block'
            })
        })
        updateTotalPass()  
 }
 selectionGroup.addEventListener("click", (event) => {
    const selectAside = event.target.closest(".selectType");
    if (!selectAside) return;
    const categoryName = selectAside.querySelector(".textDescription");
    if (!categoryName) return;
    const category = categoryName.textContent.trim().toUpperCase();
    if (category === "ALL ITEMS") {
        activeCategory = "ALL";
    } else {
        activeCategory = category;
    }
    filterPasswords();
});
function filterPasswords() {
    const rows = containerPass.querySelectorAll(".passwordRow");
    rows.forEach(row => {
        const category = row.querySelector(".crdCategory")
            .textContent.trim().toUpperCase();
        if (activeCategory === "ALL" || category === activeCategory) {
            row.style.display = "";
        } else {
            row.style.display = "none";
        }
    });
}
function updateTotalPass() {
    const rows = containerPass.querySelectorAll(".passwordRow");
    totalPass.textContent = rows.length;
    passEntries.textContent = rows.length + ' entries'
    let strong = 0;
    let weak = 0;
    rows.forEach((row) => {
        const strength = row.querySelector(".passStrength").textContent;
        if (strength === "STRONG") {
            strong++;
        }
        if (strength === "WEAK") {
            weak++;
        }
    });
    strongPass.textContent = strong;
    weakPass.textContent = weak;
}
function randomColor(){
    const r = Math.floor(Math.random() * 256)
    const g = Math.floor(Math.random() * 256)
    const b = Math.floor(Math.random() * 256)
    return `rgb(${r}, ${g}, ${b})`
}

containerPass.addEventListener("click", async (event) => {
    const deleteButton = event.target.closest(".deleteBtn");
    if (!deleteButton) return;
    const passRow = deleteButton.closest(".passwordRow");
    if (!passRow) return;
    deleteButton.dataset.loading = "true";
    try {
        const response = await apiFetch(
            `/credentials/${passRow.dataset.id}`,
            { method: "DELETE" }
        );
        if (!response.ok) {
            console.error("Delete failed:", response.status);
            return;
        }
        passRow.remove();
        updateTotalPass();

    } catch (error) {
        console.error("Delete error:", error);

    } finally {
        deleteButton.dataset.loading = "false";
    }
});

loadCredentials().catch((error) => {
    console.error(error)
    window.location.href = "login.html"
})

btnSync.addEventListener('click', async () => {
    window.location.reload();
})