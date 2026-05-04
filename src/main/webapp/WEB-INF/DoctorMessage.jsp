<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Doctor Message</title>
	<link rel="stylesheet" href="/css/doctorMessage.css">
</head>
<body>

    <div class="container">
        <div class="sidebar">
            <div class="logo">
                <img src="/images/clinic-logo.png" alt="Clinic Logo">
            </div>

            <div class="up-divider"></div>

            <div class="chat-header">
                <h2>Chats</h2>
                <img src="/images/new-msg.png" alt="New Message">
            </div>
            
			<div class="search-box">
			     <img src="/images/search-icon.png" alt="Search">
			     <input type="text" id="searchInput" placeholder="Search...">
			</div>  
			    
			<div class="filter-buttons">
			    <button class="all-btn">All</button>
			    <button class="unread-btn">Unread</button>
			</div>
			
            <div class="chat-list">
                <div class="chat-item" onclick="selectPatient('Juan Dela Cruz')">
                    <div class="patient-name">Juan Dela Cruz</div>
                    <div class="chat-preview">
                        <span class="last-message" id="last-Juan">Doctor, masakit po yung ngipin ko.</span>
                        <span class="unread-badge" id="badge-Juan">1</span>
                    </div>
                </div>

                <div class="chat-item" onclick="selectPatient('Estella Santos')">
                    <div class="patient-name">Estella Santos</div>
                    <div class="chat-preview">
                        <span class="last-message" id="last-Estella">Salamat po sa treatment.</span>
                        <span class="unread-badge" id="badge-Estella">1</span>
                    </div>
                </div>

                <div class="chat-item" onclick="selectPatient('Pedro Reyes')">
                    <div class="patient-name">Pedro Reyes</div>
                    <div class="chat-preview">
                        <span class="last-message" id="last-Pedro">Hi Doc! Anong oras ka po available?</span>
                        <span class="unread-badge" id="badge-Pedro">1</span>
                    </div>
                </div>
            </div>

            <div class="down-divider"></div>

            <div class="back-btn">
                <a href="#" onclick="history.back(); return false;">
                    <img src="/images/back.png" alt="Back">
                    Back
                </a>
            </div>
        </div>

        <div class="main">
            <div class="top-header">
                <h2 id="patientName"> </h2>
            </div>

            <div class="chat-area">
                <div class="chat-messages">
                    <!-- Sample messages will appear here from patient -->
                </div>

                <div class="message-input-container">
                    <input type="text" id="messageInput" placeholder="Type a message...">
                    <img src="/images/send-btn.png" id="sendBtn" alt="Send" class="send-icon">
                </div>
            </div>
        </div>
    </div>

<script src="/scripts/doctorMessage.js"></script>

</body>
</html>