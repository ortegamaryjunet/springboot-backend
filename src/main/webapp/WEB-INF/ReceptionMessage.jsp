<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Receptionist Message</title>
	<link rel="stylesheet" href="/css/recepMessage.css">
    <link rel="stylesheet" href="https://cdn-uicons.flaticon.com/2.6.0/uicons-regular-rounded/css/uicons-regular-rounded.css">
</head>
<body>

<div class="chat-container">
    <aside class="chat-sidebar">
        <div class="logo">
            <img src="/images/clinic-logo.png" alt="Clinic Logo">
        </div>

        <div class="sidebar-header">
            <div>
                <h2>Messages</h2>
            </div>

            <button class="new-chat-btn" id="newMessageBtn" title="Create new message">
                <img src="/images/new-msg.png" alt="New Message">
            </button>
        </div>

        <div class="search-box">
            <img src="/images/search-icon.png" alt="Search">
            <input type="text" id="searchInput" placeholder="Search patient">
        </div>

        <div class="filter-buttons">
            <button class="filter-btn active" id="allBtn">All</button>
            <button class="filter-btn" id="unreadBtn">Unread</button>
        </div>

        <div class="chat-list" id="chatList">

			<div class="chat-item unread" data-name="Deborah Mendez">
			    <div class="avatar">D</div>
			
			    <div class="chat-info">
			        <div class="chat-row">
			            <h4>Deborah Mendez</h4>
			            <span class="chat-time">8:45 AM</span>
			        </div>
			
			        <div class="chat-row">
			            <p id="last-Deborah">Good morning, may available slot po ba today?</p>
			            <span class="unread-badge" id="badge-Deborah">1</span>
			        </div>
			    </div>
			</div>
			
			<div class="chat-item unread" data-name="Mia Fernandez">
			    <div class="avatar">M</div>
			
			    <div class="chat-info">
			        <div class="chat-row">
			            <h4>Mia Fernandez</h4>
			            <span class="chat-time">9:20 AM</span>
			        </div>
			
			        <div class="chat-row">
			            <p id="last-Mia">Pwede po bang i-reschedule ang appointment ko?</p>
			            <span class="unread-badge" id="badge-Mia">1</span>
			        </div>
			    </div>
			</div>
			
			<div class="chat-item unread" data-name="Rhein Alvarez">
			    <div class="avatar">R</div>
			
			    <div class="chat-info">
			        <div class="chat-row">
			            <h4>Rhein Alvarez</h4>
			            <span class="chat-time">10:10 AM</span>
			        </div>
			
			        <div class="chat-row">
			            <p id="last-Rhein">Magkano po ang cleaning service?</p>
			            <span class="unread-badge" id="badge-Rhein">1</span>
			        </div>
			    </div>
			</div>

        </div>
        
        <div class="back-btn">
            <a href="#" onclick="history.back(); return false;">
                <img src="/images/back.png" alt="Back">
                Back
            </a>
        </div>
    </aside>

    <main class="chat-main">

        <div class="empty-state" id="emptyState">
            <div class="empty-icon">💬</div>
            <h2>Select a patient</h2>
            <p>Choose a conversation to start messaging.</p>
        </div>

        <div class="conversation" id="conversation">

            <div class="conversation-header">
                <div class="patient-profile">
                    <div class="avatar large" id="headerAvatar">J</div>
                    <div>
                        <h3 id="patientName">Patient Name</h3>
                        <p>Online</p>
                    </div>
                </div>
            </div>

            <div class="chat-messages" id="chatMessages"></div>

            <div class="message-input-container">
                <input type="text" id="messageInput" placeholder="Type your message">
                <button id="sendBtn">
                    <img src="/images/send-btn.png" alt="Send">
                </button>
            </div>

        </div>

    </main>
</div>

<div class="new-message-modal" id="newMessageModal">
    <div class="new-message-box">
        <h3>Create New Message</h3>
        <p>Enter patient name</p>

        <input type="text" id="newPatientInput" placeholder="Patient name">

        <div class="new-message-actions">
            <button type="button" class="cancel-new-message" id="cancelNewMessage">Cancel</button>
            <button type="button" class="create-new-message" id="createNewMessage">Create</button>
        </div>
    </div>
</div>

<script src="/scripts/recepMessage.js"></script>

</body>
</html>