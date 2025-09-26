const socket = new SockJS("/ws");
stompClient = Stomp.over(socket);
const lastMsgTime= document.querySelectorAll(".last-msg-time");
lastMsgTime.forEach(t=>{
	
	
	t.textContent=timeformat(t.getAttribute("data-lastMessageTime"));
})
function createLi(chatRoom) {
	// 새로운 li만들어서 화면에 삽입
	const chatList = document.querySelector(".chatList .simplebar-content");

	//li
	const newli = document.createElement("li");

	newli.className = "mb-4 px-5 py-2 chatListOne";
	newli.setAttribute("data-roomId", chatRoom.chatRoomId);
	newli.setAttribute("data-unreadCount", 0);
	newli.addEventListener("dblclick", () => {
		const pop = window.open("/chat/room/detail/" + chatRoom.chatRoomId, "room_no_" + chatRoom.chatRoomId, "width=500,height=600 ,left=600, top=100");
		if (pop) {
			pop.focus();
		}
		fetch("/chat/participant/lastMessage/" + chatRoom.chatRoomId, {
			method: "POST"
		});
		list.querySelector(".unreadCount").textContent = "";
		list.setAttribute("data-unreadCount", 0);
	});

	//a
	const a = document.createElement("div");
	a.className = "media media-message";

	//프로필영역

	const divImg = document.createElement("div");
	divImg.className = "position-relative mr-3";

	const img = document.createElement("img");
	img.className = "rounded-circle";
	//추후에 연결될 이미지url로 바꿔주면됨
	img.src = "/images/default_profile.jpg";
	img.alt = "User Image";
	img.style.width = "70px";

	divImg.appendChild(img);

	//메세지 바디
	const mediaBody = document.createElement("div");
	mediaBody.className = "media-body";

	//messageContent
	const msgContents = document.createElement("div");
	msgContents.className = "message-contents";

	//사람이름+읽음표시+최근메세지 시간

	const topSpan = document.createElement("span");
	topSpan.className = "d-flex justify-content-between align-items-center mb-1";

	const usernameSpan = document.createElement("span");
	usernameSpan.className = "username text-dark";
	usernameSpan.textContent = chatRoom.chatRoomTitle;


	const rightSpan = document.createElement("span");


	//unreadCount	
	const badge = document.createElement("span");
	badge.className = "unreadCount";
	rightSpan.appendChild(badge);

	// time
	const stateSpan = document.createElement("span");
	stateSpan.className = "state text-smoke last-msg-time";

	rightSpan.appendChild(stateSpan);

	// 조립
	topSpan.appendChild(usernameSpan);
	topSpan.appendChild(rightSpan);

	const lastMsg = document.createElement("p");
	lastMsg.className = "last-msg text-smoke";

	msgContents.appendChild(topSpan);
	msgContents.appendChild(lastMsg);
	mediaBody.appendChild(msgContents);

	a.appendChild(divImg);
	a.appendChild(mediaBody);
	newli.appendChild(a);

	// 최종적으로 ul.chatList에 붙이기
	chatList.appendChild(newli);
}
function timeformat(lastMessagTime){
	msgTime = new Date(lastMessagTime.substring(0,23));
	const nowTime = new Date();
	
	//차이를 하루 단위로 저장
	const diffDay = (nowTime.getTime()-msgTime.getTime())/(1000*60*60*24);
	
	if(diffDay<1){
		
	const hour =msgTime.getHours();
	const minute = msgTime.getMinutes();
	const ampm = hour>12 ? "오후" : "오전";
	const displayHour = hour % 12 === 0 ? 12 : hour % 12;
		
		return ampm+" "+displayHour+":"+minute.toString().padStart(2,"0");
	}else if(diffDay<2){
		return "어제";
	}else{
		const year = date.getFullYear();
		const month = String(date.getMonth() + 1).padStart(2, "0"); // 월은 0~11
		const day = String(date.getDate()).padStart(2, "0");
		return year+"-"+month+"-"+day;
	}
	
		
}
stompClient.connect({}, function(frame) {
	// 기본적으로 본인 개인큐 구독 (알림/DM 수신)
	stompClient.subscribe("/user/queue/notifications", (message) => {
		const notification = JSON.parse(message.body)

		//알림이 채팅관련 인경우
		if (notification.chatRoomId != null) {
			// 구독 알림이 오면 어떤 채팅방인지 확인하고 안읽음 값을 올려줌 이때 포커스중일때는 올리지 않음
			const chatList = document.querySelectorAll(".chatListOne");
			let existing = false;
			chatList.forEach(chat => {
				const chatRoomId = chat.getAttribute("data-roomId");
				const unreadCount = chat.querySelector(".unreadCount");
				const count = parseInt(chat.getAttribute("data-unreadCount"), 10);
				const lastMsg = chat.querySelector(".last-msg");
				const lastMsgTime=chat.querySelector(".last-msg-time");
				
				
				if (chatRoomId == notification.chatRoomId) {
					//중복임을 flag로 저장
					existing = true;
					if (notification.notificationType == "room") {
						
					}
					if (notification.notificationType == "message") {
						//그 창을 보고 있을때
						if (notification.focused) {
							lastMsg.textContent=notification.messageContent;
							lastMsgTime.textContent=timeformat(notification.sent_at);
							
						} else {
							
							if (count == 0) {
								unreadCount.className = "badge badge-secondary unreadCount";
							}
							unreadCount.textContent = (count + 1);
							chat.setAttribute("data-unreadCount", count + 1);
							lastMsg.textContent=notification.messageContent;
							lastMsgTime.textContent=timeformat(notification.sent_at);
						}


					}
					//포커스할 때 목록에서 숫자를 0 을 바꾸기위한 알림이 옴 
					if (notification.notificationType == "read") {
						unreadCount.className = "unreadCount";
						unreadCount.textContent = "";
						chat.setAttribute("data-unreadCount", 0);
					}



				}
			})

			//채팅방을 연락처 목록에서 열 때 DB를 조회해서 해당 채팅방 번호가 있으면 열지 않는데 
			//이때 서버에서 상대에게 채팅방 구독 알림이 날라와서 중복 체크를 함		
			if (!existing) {
				createLi(notification);
			}
		}



		/////알림이 결재 일경우


	});
});
//1대1 채팅 생성
const employeeListOne = document.querySelectorAll(".employeeListOne");
employeeListOne.forEach(list => {
	list.addEventListener("dblclick", () => {
		const username = [list.getAttribute("data-employee")];
		const roomdata = {
			usernames: username,
			chatRoomType: "direct"
		};
		//서버로 요청을 보내서 구독 알림을 보냄
		fetch("/chat/room/createRoom", {
			method: "POST",
			headers: { "Content-Type": "application/json" },
			body: JSON.stringify(roomdata)
		})
			.then(res => res.json())
			.then(chatRoom => {
				//해당 채팅방을 열어줌
				const pop = window.open("/chat/room/detail/" + chatRoom.chatRoomId, "room_no_" + chatRoom.chatRoomId, "width=500,height=600 ,left=600, top=100");
				if (pop) {
					pop.focus();
				}
			});
	})

})


//채팅방 클릭시 해당 채팅방을 띄움 , 동시에 마지막 확인 메세지를 변경
const chatListOne = document.querySelectorAll(".chatListOne").forEach(list => {
	const roomId = list.getAttribute('data-roomId');
	list.addEventListener("dblclick", () => {
		const pop = window.open("/chat/room/detail/" + roomId, "room_no_" + roomId, "width=500,height=600 ,left=600, top=100");
		if (pop) {
			pop.focus();
		}
		fetch("/chat/participant/lastMessage/" + roomId, {
			method: "POST"
		});
		list.querySelector(".unreadCount").TtextContent ="";
		list.setAttribute("data-unreadCount", 0);
	})

});


//그룹 채팅 생성 모달 
const modal = document.getElementById("createRoomModal");
const btn = document.getElementById("createRoomBtn");
const span = document.querySelector(".close");

// 열기
btn.onclick = function() {
	modal.style.display = "block";
};

// 닫기
span.onclick = function() {
	modal.style.display = "none";
};

// 바깥 클릭 시 닫기
window.onclick = function(event) {
	if (event.target == modal) {
		modal.style.display = "none";
	}
};


// 생성버튼 
document.getElementById("createRoomConfirmBtn").addEventListener("click", () => {
	const roomTitle = document.getElementById("roomTitle").value;
	const selected = Array.from(document.querySelectorAll("#participantList input:checked"))
		.map(cb => cb.value);

	const payload = {
		roomTitle: roomTitle,
		participants: selected
	};

	fetch("/chat/room/createRoom", {
		method: "POST",
		headers: { "Content-Type": "application/json" },
		body: JSON.stringify(payload)
	})
		.then(res => res.json())
		.then(chatRoom => {
			// 목록에 새 방 추가
			console.log("방 생성됨:", chatRoom);
			modal.style.display = "none";
		});
});