const patients = {
    "Deborah Mendez": [
        {
            text: "Good morning, may available slot po ba today?",
            type: "received",
            time: "8:45 AM",
            status: ""
        }
    ],

    "Mia Fernandez": [
        {
            text: "Pwede po bang i-reschedule ang appointment ko?",
            type: "received",
            time: "9:20 AM",
            status: ""
        }
    ],

    "Rhein Alvarez": [
        {
            text: "Magkano po ang cleaning service?",
            type: "received",
            time: "10:10 AM",
            status: ""
        }
    ],
};

let currentPatient = "";

const patientName = document.getElementById("patientName");
const headerAvatar = document.getElementById("headerAvatar");
const chatMessages = document.getElementById("chatMessages");
const messageInput = document.getElementById("messageInput");
const sendBtn = document.getElementById("sendBtn");
const emptyState = document.getElementById("emptyState");
const conversation = document.getElementById("conversation");
const searchInput = document.getElementById("searchInput");
const allBtn = document.getElementById("allBtn");
const unreadBtn = document.getElementById("unreadBtn");
const chatList = document.getElementById("chatList");
const newMessageBtn = document.getElementById("newMessageBtn");

const newMessageModal = document.getElementById("newMessageModal");
const newPatientInput = document.getElementById("newPatientInput");
const cancelNewMessage = document.getElementById("cancelNewMessage");
const createNewMessage = document.getElementById("createNewMessage");

function getChatItems() {
    return document.querySelectorAll(".chat-item");
}

getChatItems().forEach(function(item) {
    item.addEventListener("click", function() {
        const name = item.getAttribute("data-name");
        selectPatient(name, item);
    });
});

function selectPatient(name, selectedItem) {
    currentPatient = name;

    emptyState.style.display = "none";
    conversation.style.display = "flex";

    patientName.textContent = name;
    headerAvatar.textContent = name.charAt(0).toUpperCase();

    getChatItems().forEach(function(item) {
        item.classList.remove("active");
    });

    selectedItem.classList.add("active");
    selectedItem.classList.remove("unread");

    const firstName = name.split(" ")[0];
    const badge = document.getElementById("badge-" + firstName);

    if (badge) {
        badge.style.display = "none";
        badge.textContent = "";
    }

    renderMessages(name);
}

function renderMessages(name) {
    chatMessages.innerHTML = "";

    const dateDivider = document.createElement("div");
    dateDivider.className = "date-divider";
    dateDivider.textContent = "Today";
    chatMessages.appendChild(dateDivider);

    patients[name].forEach(function(message, index) {
        const nextMessage = patients[name][index + 1];

        const isGrouped =
            nextMessage &&
            nextMessage.type === message.type &&
            nextMessage.time === message.time;

        createMessageBubble(
            message.text,
            message.type,
            message.time,
            message.status,
            isGrouped
        );
    });

    scrollToBottom();
}

function createMessageBubble(text, type, time, status, isGrouped) {
    const messageRow = document.createElement("div");

    if (isGrouped) {
        messageRow.className = "message-row " + type + " grouped";
    } else {
        messageRow.className = "message-row " + type;
    }

    const bubble = document.createElement("div");
    bubble.className = "message-bubble";
    bubble.textContent = text;

    messageRow.appendChild(bubble);

    if (type === "sent") {
        const messageStatus = document.createElement("span");
        messageStatus.className = "message-status";
        messageStatus.textContent = time + " • " + status;
        messageRow.appendChild(messageStatus);
    } else {
        const messageTime = document.createElement("span");
        messageTime.className = "message-time";
        messageTime.textContent = time;
        messageRow.appendChild(messageTime);
    }

    chatMessages.appendChild(messageRow);
}

function sendMessage() {
    if (!currentPatient) {
        return;
    }

    const text = messageInput.value.trim();

    if (text === "") {
        return;
    }

    const time = getCurrentTime();

    patients[currentPatient].push({
        text: text,
        type: "sent",
        time: time,
        status: "Sent"
    });

    renderMessages(currentPatient);
    updateLastMessage(currentPatient, text, time);

    messageInput.value = "";

    const patientToUpdate = currentPatient;

    setTimeout(function() {
        markLatestMessageDelivered(patientToUpdate);
    }, 1200);
}

function markLatestMessageDelivered(name) {
    if (!name || !patients[name]) {
        return;
    }

    const messages = patients[name];

    for (let i = messages.length - 1; i >= 0; i--) {
        if (messages[i].type === "sent") {
            messages[i].status = "Delivered";
            break;
        }
    }

    if (currentPatient === name) {
        renderMessages(name);
    }
}

function updateLastMessage(name, text, time) {
    const firstName = name.split(" ")[0];
    const lastMessage = document.getElementById("last-" + firstName);

    if (lastMessage) {
        lastMessage.textContent = text;
    }

    const activeItem = document.querySelector('.chat-item[data-name="' + name + '"]');

    if (activeItem) {
        const chatTime = activeItem.querySelector(".chat-time");
        const preview = activeItem.querySelector(".chat-row p");

        if (chatTime) {
            chatTime.textContent = time;
        }

        if (preview) {
            preview.textContent = text;
        }
    }
}

function getCurrentTime() {
    const now = new Date();

    return now.toLocaleTimeString([], {
        hour: "numeric",
        minute: "2-digit"
    });
}

function scrollToBottom() {
    chatMessages.scrollTop = chatMessages.scrollHeight;
}

sendBtn.addEventListener("click", sendMessage);

messageInput.addEventListener("keypress", function(e) {
    if (e.key === "Enter") {
        sendMessage();
    }
});

allBtn.addEventListener("click", function() {
    allBtn.classList.add("active");
    unreadBtn.classList.remove("active");

    searchInput.value = "";

    getChatItems().forEach(function(item) {
        item.style.display = "flex";
    });

    removeNoResult();
});

unreadBtn.addEventListener("click", function() {
    unreadBtn.classList.add("active");
    allBtn.classList.remove("active");

    let count = 0;

    getChatItems().forEach(function(item) {
        const badge = item.querySelector(".unread-badge");

        if (badge && badge.style.display !== "none" && badge.textContent.trim() !== "") {
            item.style.display = "flex";
            count++;
        } else {
            item.style.display = "none";
        }
    });

    showNoResult(count, "No unread messages");
});

searchInput.addEventListener("keyup", function() {
    const value = searchInput.value.toLowerCase();
    let count = 0;

    getChatItems().forEach(function(item) {
        const name = item.getAttribute("data-name").toLowerCase();

        if (name.includes(value)) {
            item.style.display = "flex";
            count++;
        } else {
            item.style.display = "none";
        }
    });

    showNoResult(count, "No patient found");
});

function showNoResult(count, message) {
    removeNoResult();

    if (count === 0) {
        const noResult = document.createElement("div");
        noResult.className = "no-result";
        noResult.id = "noResult";
        noResult.textContent = message;

        chatList.appendChild(noResult);
    }
}

function removeNoResult() {
    const noResult = document.getElementById("noResult");

    if (noResult) {
        noResult.remove();
    }
}

newMessageBtn.addEventListener("click", function() {
    newMessageModal.classList.add("show");
    newPatientInput.value = "";
    setTimeout(function() {
        newPatientInput.focus();
    }, 100);
});

cancelNewMessage.addEventListener("click", function() {
    newMessageModal.classList.remove("show");
});

newMessageModal.addEventListener("click", function(e) {
    if (e.target === newMessageModal) {
        newMessageModal.classList.remove("show");
    }
});

createNewMessage.addEventListener("click", createNewPatientChat);

newPatientInput.addEventListener("keypress", function(e) {
    if (e.key === "Enter") {
        createNewPatientChat();
    }
});

function createNewPatientChat() {
    const patient = newPatientInput.value.trim();

    if (patient === "") {
        newPatientInput.focus();
        return;
    }

    const name = patient;

    if (patients[name]) {
        const existingItem = document.querySelector('.chat-item[data-name="' + name + '"]');

        if (existingItem) {
            selectPatient(name, existingItem);
        }

        newMessageModal.classList.remove("show");
        return;
    }

    patients[name] = [];

    const firstLetter = name.charAt(0).toUpperCase();
    const firstName = name.split(" ")[0];

    const item = document.createElement("div");
    item.className = "chat-item";
    item.setAttribute("data-name", name);

    item.innerHTML =
        '<div class="avatar">' + firstLetter + '</div>' +
        '<div class="chat-info">' +
            '<div class="chat-row">' +
                '<h4>' + name + '</h4>' +
                '<span class="chat-time">New</span>' +
            '</div>' +
            '<div class="chat-row">' +
                '<p id="last-' + firstName + '">No messages yet</p>' +
                '<span class="unread-badge" id="badge-' + firstName + '" style="display:none;"></span>' +
            '</div>' +
        '</div>';

    item.addEventListener("click", function() {
        selectPatient(name, item);
    });

    chatList.prepend(item);
    newMessageModal.classList.remove("show");
    selectPatient(name, item);
}