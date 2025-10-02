const socket = new SockJS("/ws");
stompClient = Stomp.over(socket);
const lastMsgTime = document.querySelectorAll(".last-msg-time");
const popup = new Map();
lastMsgTime.forEach(t => {


	t.textContent = timeformat(t.getAttribute("data-lastMessageTime"));
})
function createLi(chatRoom) {
	// 새로운 li만들어서 화면에 삽입
	const chatList = document.querySelector(".chatList .simplebar-content");

	//li
	const newli = document.createElement("li");

	newli.className = "mb-4 px-5 py-2 chatListOne list-group-item-action";
	newli.setAttribute("data-roomId", chatRoom.chatRoomId);
	newli.setAttribute("data-unreadCount", 0);
	newli.addEventListener("dblclick", (e) => {
		if (e.target.classList.contains("kebab-menu")) {
			return;
		}
		const pop = window.open("/chat/room/detail/" + chatRoom.chatRoomId, "room_no_" + chatRoom.chatRoomId, "width=700,height=650 ,left=600, top=100");
		if (pop) {
			pop.focus();
		}
		fetch("/chat/participant/lastMessage/" + chatRoom.chatRoomId, {
			method: "POST"
		});
		newli.querySelector(".unreadCount").textContent = "";
		newli.setAttribute("data-unreadCount", 0);
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
	img.src = chatRoom.imgPath;
	img.alt = "User Image";
	img.style.width = "70px";
	img.style.height = "70px";

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




	const rightSpan = document.createElement("span");


	//unreadCount	
	const badge = document.createElement("span");
	badge.className = "unreadCount";
	rightSpan.appendChild(badge);

	// time
	const stateSpan = document.createElement("span");
	stateSpan.className = "state text-smoke last-msg-time";
	stateSpan.textContent = timeformat(chatRoom.sentAt);
	rightSpan.appendChild(stateSpan);

	// dropdown-item
	// kebab-menu div
	const kebabDiv = document.createElement("div");
	kebabDiv.className = "dropdown kebab-menu";
	kebabDiv.style.marginLeft = "10px";

	// a 태그 (토글 버튼)
	const toggleA = document.createElement("a");
	toggleA.className = "dropdown-toggle icon-burger-mini kebab-menu";
	toggleA.href = "#";
	toggleA.setAttribute("role", "button");
	toggleA.setAttribute("id", "dropdownMenuLink");
	toggleA.setAttribute("data-toggle", "dropdown");
	toggleA.setAttribute("aria-haspopup", "true");
	toggleA.setAttribute("aria-expanded", "false");

	// dropdown-menu div
	const menuDiv = document.createElement("div");
	menuDiv.className = "dropdown-menu dropdown-menu-right";
	menuDiv.setAttribute("aria-labelledby", "dropdownMenuLink");

	// dropdown-item a
	const leaveA = document.createElement("a");
	leaveA.className = "dropdown-item room-out";
	leaveA.href = "javascript:void(0)";
	leaveA.textContent = "채팅방 나가기";


	// 조립
	menuDiv.appendChild(leaveA);
	kebabDiv.appendChild(toggleA);
	kebabDiv.appendChild(menuDiv);

	topSpan.appendChild(usernameSpan);
	topSpan.appendChild(rightSpan);

	const lastMsg = document.createElement("p");
	lastMsg.className = "last-msg text-smoke";

	msgContents.appendChild(topSpan);
	msgContents.appendChild(lastMsg);
	mediaBody.appendChild(msgContents);

	a.appendChild(divImg);
	a.appendChild(mediaBody);
	a.appendChild(kebabDiv);
	newli.appendChild(a);
	// 최종적으로 ul.chatList에 붙이기
	chatList.insertBefore(newli, chatList.firstChild);
}
function timeformat(lastMessageTime) {
	if (!lastMessageTime || lastMessageTime.trim() === "") {
		return " ";  // 값이 없으면 그냥 공백 반환
	}
	msgTime = new Date(lastMessageTime.substring(0, 23));
	const nowTime = new Date();

	//차이를 하루 단위로 저장
	const diffDay = (nowTime.getTime() - msgTime.getTime()) / (1000 * 60 * 60 * 24);

	if (diffDay < 1) {

		const hour = msgTime.getHours();
		const minute = msgTime.getMinutes();
		const ampm = hour > 12 ? "오후" : "오전";
		const displayHour = hour % 12 === 0 ? 12 : hour % 12;

		return ampm + " " + displayHour + ":" + minute.toString().padStart(2, "0");
	} else if (diffDay < 2) {
		return "어제";
	} else {
		const year = msgTime.getFullYear();
		const month = String(msgTime.getMonth() + 1).padStart(2, "0"); // 월은 0~11
		const day = String(msgTime.getDate()).padStart(2, "0");
		return year + "-" + month + "-" + day;
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
				const lastMsgTime = chat.querySelector(".last-msg-time");



				if (chatRoomId == notification.chatRoomId) {
					//중복임을 flag로 저장
					existing = true;

					
					//초대할 경우
					if (notification.notificationType == "invite") {
						const chatRoomTitle = chat.querySelector(".username");
						const img = chat.querySelector("img");
						if (notification.focused) {
							lastMsg.textContent = notification.messageContent;
							lastMsgTime.textContent = timeformat(notification.sentAt);
							if (notification.viewName != null) {
								chatRoomTitle.textContent = notification.viewName;
							}
							img.src = notification.imgPath;

						} else {

							if (count == 0) {
								unreadCount.className = "badge badge-secondary unreadCount";
							}
							unreadCount.textContent = (count + 1);
							chat.setAttribute("data-unreadCount", count + 1);
							lastMsg.textContent = notification.messageContent;
							lastMsgTime.textContent = timeformat(notification.sentAt);
							if (notification.viewName != null) {
								chatRoomTitle.textContent = notification.viewName;
							}
							img.src = notification.imgPath;
						}


					}

					// 메세지나 퇴장 일경우 
					if (notification.notificationType == "message" ||notification.notificationType == "out")  {
						//그 창을 보고 있을때
						if (notification.focused) {
							lastMsg.textContent = notification.messageContent;
							lastMsgTime.textContent = timeformat(notification.sentAt);

						} else {

							if (count == 0) {
								unreadCount.className = "badge badge-secondary unreadCount";
							}
							unreadCount.textContent = (count + 1);
							chat.setAttribute("data-unreadCount", count + 1);
							lastMsg.textContent = notification.messageContent;
							lastMsgTime.textContent = timeformat(notification.sentAt);
						}


					}
					//포커스할 때 목록에서 숫자를 0 을 바꾸기위한 알림이 옴 
					if (notification.notificationType == "read") {

						if (unreadCount) {	
							unreadCount.className = "unreadCount";
							unreadCount.textContent = "";
						}
						chat.setAttribute("data-unreadCount", 0);
					} 
					//다른 알림일경우 리스트에서 제일 위로 올림
					else {
						const chatListContainer = document.querySelector(".chatList .simplebar-content");
						chatListContainer.insertBefore(chat, chatListContainer.firstChild);
					}



				}


			})

			//채팅방을 연락처 목록에서 열 때 DB를 조회해서 해당 채팅방 번호가 있으면 열지 않는데 
			//이때 서버에서 상대에게 채팅방 구독 알림이 날라와서 중복 체크를 함		
			if (!existing&& notification.notificationType !== "read") {
				createLi(notification);
				// 방금 만든 li 가져오기
				const chatList = document.querySelector(".chatList .simplebar-content");
				const newChat = chatList.querySelector(`.chatListOne[data-roomId="${notification.chatRoomId}"]`);

				if (newChat) {
					const lastMsg = newChat.querySelector(".last-msg");
					const lastMsgTime = newChat.querySelector(".last-msg-time");
					const chatRoomTitle = newChat.querySelector(".username");
					const img = newChat.querySelector("img");
					const unreadCount = newChat.querySelector(".unreadCount");

					// 미리보기 채우기
					lastMsg.textContent = notification.messageContent || "";
					lastMsgTime.textContent = timeformat(notification.sentAt);
					if (notification.viewName) {
						chatRoomTitle.textContent = notification.viewName;
					}
					if (notification.imgPath) {
						img.src = notification.imgPath;
					}

					// 안읽음 숫자
					if (!notification.focused) {
						unreadCount.className = "badge badge-secondary unreadCount";
						unreadCount.textContent = 1;
						newChat.setAttribute("data-unreadCount", 1);
					}

				}


			}
		}


			/////알림이 결재 일경우


		});
});
//1대1 채팅 생성
const employeeListOne = document.querySelectorAll(".employeeListOne");
employeeListOne.forEach(list => {
	list.addEventListener("dblclick", (e) => {

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
				const pop = window.open("/chat/room/detail/" + chatRoom.chatRoomId, "room_no_" + chatRoom.chatRoomId, "width=700,height=650 ,left=600, top=100");
				if (pop) {
					pop.focus();
				}
			});
	})

})


//채팅방 클릭시 해당 채팅방을 띄움 , 동시에 마지막 확인 메세지를 변경
const chatListOne = document.querySelectorAll(".chatListOne").forEach(list => {
	const roomId = list.getAttribute('data-roomId');
	list.addEventListener("dblclick", (e) => {
		if (e.target.classList.contains("kebab-menu")) {
			return;
		}
		const pop = window.open("/chat/room/detail/" + roomId, "room_no_" + roomId, "width=700,height=650 ,left=600, top=100");
		if (pop) {
			pop.focus();
		}
		fetch("/chat/participant/lastMessage/" + roomId, {
			method: "POST"
		});
		list.querySelector(".unreadCount").textContent = "";
		list.setAttribute("data-unreadCount", 0);
	})

});

//채팅방 나가기
document.querySelector(".chatList").addEventListener("click", (e) => {
	if (e.target.classList.contains("room-out")) {
		const li = e.target.closest(".chatListOne");
		const roomId = li.getAttribute("data-roomId");
		fetch("/chat/room/out/" + roomId, {

		}).then(res => res.json())
			.then(res => {
				if (res) {
					li.remove();

				}
				else {
				}
			})

	}
});

//그룹 채팅 생성
let selectedParticipants = [];

document.getElementById("nextStepBtn").addEventListener("click", () => {
	selectedParticipants = Array.from(document.querySelectorAll(".participant-checkbox:checked"))
		.map(cb => cb.value);
	if (selectedParticipants.length < 2) {
		alert("참여자를 최소 2명 이상 선택하세요.");
		return;
	}
	$("#createRoomStep1").modal("hide");
	$("#createRoomStep2").modal("show");

});

//이름으로 검색
const namesearchBtn = document.querySelector(".namesearchBtn")
namesearchBtn.addEventListener("click", () => {
	const searchUserInputValue = document.getElementById("searchUserInput").value.trim();
	const participantList = document.getElementById("participantList")
	const participantLi = participantList.querySelectorAll("li");

	participantLi.forEach(li => {
		const employeeName = li.querySelector(".employeeName").textContent.trim();
		if (searchUserInputValue == "" || employeeName == searchUserInputValue) {
			li.classList.remove("hidden");

		} else {
			li.classList.add("hidden");
		}

	})
})



//모달 닫힐때 원래 상태로 돌림

// Step1 모달: 취소 버튼 + 닫기 버튼(X) + ESC 초기화
// X 버튼 + 취소 버튼
document.querySelectorAll("#createRoomStep1 [data-dismiss='modal'], #createRoomStep1 .close , #createRoomStep1 #nextStepBtn")
	.forEach(btn => {
		btn.addEventListener("click", () => {

			// 검색 input 초기화
			document.getElementById("searchUserInput").value = "";

			// 체크박스 해제
			document.querySelectorAll("#participantList .participant-checkbox")
				.forEach(cb => cb.checked = false);

			// 숨겨진 li 복원
			document.querySelectorAll("#participantList li")
				.forEach(li => li.classList.remove("hidden"));

		});
	});

// Step2 모달: 취소 버튼 + 닫기 버튼(X) 초기화
document.querySelectorAll("#createRoomStep2 [data-dismiss='modal'], #createRoomStep2 .close #createRoomStep2 #createRoomConfirmBtn")
	.forEach(btn => {
		btn.addEventListener("click", () => {
			// 방 제목 input 초기화
			document.getElementById("roomTitle").value = "";
		});
	});

//esc 닫기
window.addEventListener("keydown", (e) => {
	/*const modal = document.querySelector("#createRoomStep1");
	const modal2 = document.querySelector("#createRoomStep2");*/
	if (e.key == 'Escape') {
		// 검색 input 초기화
		document.getElementById("searchUserInput").value = "";

		// 체크박스 해제
		document.querySelectorAll("#participantList .participant-checkbox")
			.forEach(cb => cb.checked = false);

		// 숨겨진 li 복원
		document.querySelectorAll("#participantList li")
			.forEach(li => li.classList.remove("hidden"));

		//step2 초기화	
		document.getElementById("roomTitle").value = "";
	}


});


// 최종 생성
document.getElementById("createRoomConfirmBtn").addEventListener("click", () => {
	const roomTitle = document.getElementById("roomTitle").value;
	console.log(selectedParticipants);
	if (!roomTitle.trim()) {
		alert("방 제목을 입력하세요.");
		return;
	}

	const data = {
		chatRoomTitle: roomTitle,
		usernames: selectedParticipants,
		chatRoomType: "room"
	};

	fetch("/chat/room/createRoom", {
		method: "POST",
		headers: { "Content-Type": "application/json" },
		body: JSON.stringify(data)
	})
		.then(res => res.json())
		.then(chatRoom => {
			$("#createRoomStep2").modal("hide");
			chatRoom.imgPath = "/images/group_profile.png";
			console.log("방아이디"+chatRoom.chatRoomId);
			//createLi(chatRoom);
			
				console.log("방아이디"+chatRoom.chatRoomId);
			// 방금 만든 li 가져오기  제목 설정 해줌
			/*	const chatList = document.querySelector(".chatList .simplebar-content");
				const newChat = chatList.querySelector(`.chatListOne[data-roomId="${chatRoom.chatRoomId}"]`);

				if (newChat) {
				 newChat.querySelector(".username").textContent=data.chatRoomTitle;
				}
*/

			const pop = window.open("/chat/room/detail/" + chatRoom.chatRoomId, "room_no_" + chatRoom.chatRoomId, "width=700,height=650 ,left=600, top=100");
			if (pop) {
				pop.focus();
			}

		});
});