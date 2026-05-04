document.getElementById("patientName").innerText = "Select a Patient";

let currentPatient = "";

function selectPatient(name) {
    const patientNameElem = document.getElementById("patientName");
    const chatArea = document.querySelector(".chat-area");
    const chatMessages = chatArea.querySelector(".chat-messages");
    const inputContainer = chatArea.querySelector(".message-input-container");

    patientNameElem.innerText = name;
    currentPatient = name;

    const firstName = name.split(" ")[0];

    chatMessages.innerHTML = "";

    const lastMessageElem = document.getElementById("last-" + firstName);
    const badge = document.getElementById("badge-" + firstName);

    patientNameElem.style.fontWeight = "normal";

    if (lastMessageElem && lastMessageElem.style.fontWeight === "bold") {
        patientNameElem.style.fontWeight = "bold";
    }

    if (lastMessageElem) {
        lastMessageElem.style.fontWeight = "normal";
    }

    if (badge) {
        badge.style.display = "none";
        badge.textContent = "";
    }

    inputContainer.style.display = "flex";

    if (name === "Juan Dela Cruz") {
        addMessage("Doctor, masakit po yung ngipin ko.", "received", firstName);
    } 
    else if (name === "Estella Santos") {
        addMessage("Salamat po sa treatment.", "received", firstName);
    } 
    else if (name === "Pedro Reyes") {
        addMessage("Hi Doc! Anong oras ka po available?", "received", firstName);
    }
}

function addMessage(message, type, firstName) {
    const chatMessages = document.querySelector(".chat-messages");
    const msgDiv = document.createElement("div");
    msgDiv.innerText = message;

    const patientNameElem = document.getElementById("patientName");
    const activeFirstName = patientNameElem.innerText.split(" ")[0];

    if (type === "sent") {
        msgDiv.className = "sent-message";
    } else {
        msgDiv.className = "received-message";

        const lastMessageElem = document.getElementById("last-" + firstName);
        const badge = document.getElementById("badge-" + firstName);

        if (firstName !== activeFirstName) {
            if (lastMessageElem) {
                lastMessageElem.style.fontWeight = "bold";
            }

            if (badge) {
                let count = parseInt(badge.textContent || "0");
                count += 1;
                badge.textContent = count;
                badge.style.display = "inline";
            }
        } else {
            if (lastMessageElem) {
                lastMessageElem.style.fontWeight = "normal";
            }
        }
    }

    chatMessages.insertBefore(msgDiv, chatMessages.firstChild);
}

function sendMessage() {
    if (!currentPatient) return;

    const input = document.getElementById("messageInput");
    const message = input.value.trim();
    const inputContainer = document.querySelector(".message-input-container");

    if (message && inputContainer.style.display !== "none") {
        const firstName = currentPatient.split(" ")[0];

        addMessage(message, "sent", firstName);
        input.value = "";
    }
}

const sendBtn = document.getElementById("sendBtn");
if (sendBtn) {
    sendBtn.addEventListener("click", sendMessage);
}

const messageInput = document.getElementById("messageInput");
if (messageInput) {
    messageInput.addEventListener("keypress", function (e) {
        if (e.key === "Enter") {
            sendMessage();
        }
    });
}

const chatAreaInit = document.querySelector(".chat-area");
const inputContainerInit = chatAreaInit.querySelector(".message-input-container");

if (inputContainerInit) {
    inputContainerInit.style.display = "none";
}

const allBtn = document.querySelector(".all-btn");
const unreadBtn = document.querySelector(".unread-btn");
const chatItems = document.querySelectorAll(".chat-item");

function showAllChats() {
    chatItems.forEach(function (item) {
        item.style.display = "block";
    });

    const listContainer = document.querySelector(".chat-list") || document.body;
    const emptyMsg = document.getElementById("no-unread-msg");
    if (emptyMsg) emptyMsg.remove();

    allBtn.classList.add("active");
    unreadBtn.classList.remove("active");
}

function showUnreadChats() {
    let visibleCount = 0;

    chatItems.forEach(function (item) {
        const badge = item.querySelector(".unread-badge");

        if (badge && parseInt(badge.textContent || "0") > 0) {
            item.style.display = "block";
            visibleCount++;
        } else {
            item.style.display = "none";
        }
    });

    const listContainer = document.querySelector(".chat-list") || document.body;

    let emptyMsg = document.getElementById("no-unread-msg");

    if (visibleCount === 0) {
        if (!emptyMsg) {
            emptyMsg = document.createElement("div");
            emptyMsg.id = "no-unread-msg";
            emptyMsg.textContent = "No unread chats";
            emptyMsg.style.padding = "10px";
            emptyMsg.style.textAlign = "center";
            emptyMsg.style.color = "#888";
            listContainer.appendChild(emptyMsg);
        }
    } else {
        if (emptyMsg) emptyMsg.remove();
    }

    unreadBtn.classList.add("active");
    allBtn.classList.remove("active");
}

if (allBtn) allBtn.addEventListener("click", showAllChats);
if (unreadBtn) unreadBtn.addEventListener("click", showUnreadChats);

showAllChats();

const searchInput = document.getElementById("searchInput");

if (searchInput) {
    searchInput.addEventListener("keyup", function () {
        const searchValue = this.value.toLowerCase();
        let visibleCount = 0;

        chatItems.forEach(function (item) {
            const patientName = item.querySelector(".patient-name").innerText.toLowerCase();

            if (patientName.includes(searchValue)) {
                item.style.display = "block";
                visibleCount++;
            } else {
                item.style.display = "none";
            }
        });

        let noSearchMsg = document.getElementById("no-search-msg");

        if (visibleCount === 0) {
            if (!noSearchMsg) {
                noSearchMsg = document.createElement("div");
                noSearchMsg.id = "no-search-msg";
                noSearchMsg.textContent = "No matching results found";
                noSearchMsg.style.padding = "10px";
                noSearchMsg.style.textAlign = "center";
                noSearchMsg.style.color = "#888";
                document.querySelector(".chat-list").appendChild(noSearchMsg);
            }
        } else {
            if (noSearchMsg) noSearchMsg.remove();
        }
    });
}