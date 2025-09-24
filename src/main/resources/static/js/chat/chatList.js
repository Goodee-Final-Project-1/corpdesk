const socket = new SockJS("/ws");
stompClient = Stomp.over(socket);

stompClient.connect({}, function(frame) {
	// 기본적으로 본인 개인큐 구독 (알림/DM 수신)
	stompClient.subscribe("/user/queue/notifications", (message) => {

		const chatRoom = JSON.parse(message.body)
		const table = document.querySelector("table");
		const newRow = document.createElement("tr");

		newRow.classList.add("chatListOne");
		newRow.dataset.roomId = chatRoom.chatRoomId;
		newRow.style.cursor = "pointer";
		newRow.innerHTML = "<td>" + chatRoom.chatRoomId + "</td><td>" + chatRoom.chatRoomTitle + "</td>" + "<td>" + chatRoom.unreadCount + "</td>";
		newRow.addEventListener("click", () => {
			window.open("/chat/room/detail/" + chatRoom.chatRoomId, "", "width=500,height=600 ,left=600, top=100");
		});
		table.appendChild(newRow);


	});


});
//1대1 채팅 생성
const employeeListOne = document.querySelectorAll(".employeeListOne");
employeeListOne.forEach(list=>{
	list.addEventListener("dblclick",()=>{
		const username = [list.getAttribute("data-employee")];
			const roomdata = {
				usernames: username,
				chatRoomType: "direct"
			};
			fetch("/chat/room/createRoom", {
				method: "POST",
				headers: { "Content-Type": "application/json" },
				body: JSON.stringify(roomdata)
			})
				.then(res => res.json())
				.then(chatRoom => {
					console.log(chatRoom.chatRoomId)
					stompClient.subscribe("/sub/chat/room/" + chatRoom.chatRoomId, (message) => {

					})
					//방생성 후 화면에서도 바로 목록을 보여줌
					let existing = false;
					document.querySelectorAll(".chatListOne").forEach(tr => {
						if (tr.getAttribute("data-roomId") == chatRoom.chatRoomId) {
							existing = true;
						}
					});


					// 중복 확인
					if (!existing) {
						const table = document.querySelector("table");
						const newRow = document.createElement("tr");

						newRow.classList.add("chatListOne");
						newRow.dataset.roomId = chatRoom.chatRoomId;
						newRow.style.cursor = "pointer";
						newRow.innerHTML = "<td>" + chatRoom.chatRoomId + "</td><td>" + chatRoom.chatRoomTitle + "</td>";
						newRow.addEventListener("click", () => {
							window.open("/chat/room/detail/" + chatRoom.chatRoomId, "", "width=500,height=600 ,left=600, top=100");

						});
						table.appendChild(newRow);
						console.log("1");
					} else {
						window.open("/chat/room/detail/" + chatRoom.chatRoomId, "", "width=500,height=600 ,left=600, top=100");
					}



				});
		
	})
	
})


//채팅방 클릭시 해당 채팅방을 띄움 , 동시에 마지막 확인 메세지를 변경
const chatListOne = document.querySelectorAll(".chatListOne").forEach(list => {
	const roomId = list.getAttribute('data-roomId');
	list.addEventListener("click", () => {
		window.open("/chat/room/detail/" + roomId, "", "width=500,height=600 ,left=600, top=100");
		fetch("/chat/participant/lastMessage/" + roomId, {
			method: "POST"
		});
		list.querySelector(".unreadCount").innerText = "";
		list.setAttribute("data-unreadCount", "0");
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