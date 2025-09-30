//메세지 연결 송수신
// 메세지 수신 박스
const messageContainer = document.querySelector(".messageContainer")
const listChat = document.getElementById('chat-list');
const chatRoomId = document.querySelector(".roomId").value;
const username = document.querySelector(".username").value;
const socket = new SockJS("/ws");
const stompClient = Stomp.over(socket);
const sendBtn = document.getElementById("sendBtn");
//직전 발신자 저장
let lastSender = null;
// 에러메세지가 두번 호출되는걸 막음 
let errorHandled = false;
function timeformat(lastMessageTime) {
	if (!lastMessageTime || lastMessageTime.trim() === "") {
		return " ";  // 값이 없으면 그냥 공백 반환
	}
	msgTime = new Date(lastMessageTime.substring(0, 23));

		const hour = msgTime.getHours();
		const minute = msgTime.getMinutes();
		const ampm = hour > 12 ? "오후" : "오전";
		const displayHour = hour % 12 === 0 ? 12 : hour % 12;

		return ampm + " " + displayHour + ":" + minute.toString().padStart(2, "0");
	


}

function appendMessage(msg, prepend = false, isSameSender = null) {
	//발신자가 나인경우
	const isMe = msg.employeeUsername === username;
	//발신자가 직전 발신자와 같은경우
	//프로필이미지 등을 하나만 보여주기 위해
	if (isSameSender === null) {
		isSameSender = msg.employeeUsername === lastSender;
	}

	//왼쪽인지 오른쪽인지 껍데기
	const divMedia = document.createElement("div");
	if (isMe) {
		divMedia.className = "media media-chat media-chat-right";
	} else {
		divMedia.className = "media media-chat";
	}

	//상대방의 메세지 왼쪽으로 
	if (!isMe) {
		//직전에 보내지 않았으면 전부다 만듦
		if (!isSameSender) {
			//이미지
			const img = document.createElement("img");
			img.className = "rounded-circle profileImg";
			img.src = msg.imgPath;
			img.alt = "Image";
			img.style.width = "50px";
			divMedia.append(img);

			//media-body
			const divBody = document.createElement("div");
			divBody.className = "media-body";


			//이름
			const name = document.createElement("h6");
			name.className = "name font-weight-bold mb-1";
			name.textContent = msg.viewName;
			divBody.appendChild(name);

			//메시지 껍데기
			const divText = document.createElement("div");
			divText.className = "text-content";
			divText.setAttribute("data-no", msg.messageId);

			//메세지 내용
			const spanMessage = document.createElement("span");
			spanMessage.className = "message";
			spanMessage.textContent = msg.messageContent;
			divText.appendChild(spanMessage);

			//시간
			const timeMessage = document.createElement("time");
			timeMessage.className = "time";
			timeMessage.textContent =timeformat( msg.sentAt);
			divText.appendChild(timeMessage);

			//조립
			divBody.appendChild(divText);
			divMedia.appendChild(divBody);

		}
		//직전에 보낸사람이면
		else {
			const divBody = document.createElement("div");
			divBody.className = "media-body";
			

			const divText = document.createElement("div");
			divText.className = "text-content";
			divText.setAttribute("data-no", msg.messageId);
			//메세지 내용
			const spanMessage = document.createElement("span");
			spanMessage.className = "message";
			spanMessage.textContent = msg.messageContent;
			divText.appendChild(spanMessage);

			//시간
			const timeMessage = document.createElement("time");
			timeMessage.className = "time";
			timeMessage.textContent = timeformat( msg.sentAt);
			divText.appendChild(timeMessage);
			//조립
			divBody.appendChild(divText);
			divMedia.appendChild(divBody);


		}


	}
	// 보낸사람이 나인 경우
	else {

		const divBody = document.createElement("div");
		divBody.className = "media-body";

		const divText = document.createElement("div");
		divText.className = "text-content";
		divText.setAttribute("data-no", msg.messageId);

		//메세지 내용
		const spanMessage = document.createElement("span");
		spanMessage.className = "message";
		spanMessage.textContent = msg.messageContent;
		divText.appendChild(spanMessage);

		//시간
		const timeMessage = document.createElement("time");
		timeMessage.className = "time";
		timeMessage.textContent = timeformat( msg.sentAt);
		divText.appendChild(timeMessage);

		//조립
		divBody.appendChild(divText);
		divMedia.appendChild(divBody);


	}

	if(!prepend) {
		listChat.appendChild(divMedia);
		messageContainer.scrollTop = messageContainer.scrollHeight;
		lastSender = msg.employeeUsername;
	}else{
		// 항상 리스트 맨 앞에 붙이기 (prepend)
		listChat.insertBefore(divMedia, listChat.firstChild);
	}
	return divMedia;
}



// 메세지 수신
stompClient.connect({}, function(frame) {
	stompClient.subscribe("/sub/chat/room/" + chatRoomId, (message) => {
		const msg = JSON.parse(message.body);
		const isSameSender = msg.employeeUsername == lastSender;

		appendMessage(msg, false, isSameSender);
		lastSender = msg.employeeUsername;
		if (document.hasFocus()) {
			fetch("/chat/participant/lastMessage/" + chatRoomId, { method: "POST" });
		}
	})

}, function(error) {
	if (errorHandled) return;
	errorHandled = true;

	alert("참여되지 않은 사용자입니다.");
	location.href = "/chat/room/list";
}
)

//메세지 전송
sendBtn.addEventListener("click", () => {
	const inputText = document.getElementById("messageInput");
	const msg = {
		chatRoomId: chatRoomId,
		employeeUsername: username,
		messageContent: inputText.value
	};
	stompClient.send("/pub/chat/message", {}, JSON.stringify(msg));
	inputText.value = "";

});


// 메세지 스크롤 이벤트 처리

// 이전 기록을 가져오기전에 스크롤을 상단으로 올릴 경우 채팅내역 로딩을 기다리게함 
let isScrolled = false;
let lastMessageId = null;
let isEnd = false;


fetchList();

//스크롤 이벤트 발생 시 호출 
messageContainer.addEventListener("scroll", () => {
	if (messageContainer.scrollTop < 1 && isScrolled === false) {
		isScrolled = true;
		fetchList();
	}
});

//서버에서 이전 메시지 가져오기
function fetchList() {
	//처음 데이터까지 다 가져온 경우면 db접근안하고 리턴
	if (isEnd == true) {
		return;
	}
	//채팅 리스트를 가져올 시작 번호
	const firstLi = document.querySelector("#chat-list .text-content[data-no]");
	const endNo = firstLi ? Number(firstLi.getAttribute("data-no")) : 0;

	//채팅 리스트 가져오기
	fetch("/chat/message/list/" + chatRoomId + "/" + endNo + "/" + 20, {
		method: "GET"
	}).then(res => res.json())
		.then(res => {
			const messages = res.messages;
			const size = res.size;

			if (size < 20) {
				isEnd = true;
			}
			// 리스트를 가져오기전 스크롤 높이를 저장
			const oldScrollHeight = messageContainer.scrollHeight;

			// 이미 화면에 있는 최상단 메시지의 보낸 사람과 이어지는지도 확인

			messages.forEach((msg, idx) => {
			  const nextMsg = messages[idx + 1]; // 나보다 오래된(밑에 붙을) 메시지
			  let isSameSender = false;

			  if (nextMsg && nextMsg.employeeUsername === msg.employeeUsername) {
			    // 나 바로 밑 메시지가 같은 사람이면 이번 건 연속
			    isSameSender = true;
			  }
			  appendMessage(msg, true, isSameSender);
			});

			// 스크롤 위치 보정
			const newScrollHeight = messageContainer.scrollHeight;
			messageContainer.scrollTop = newScrollHeight - oldScrollHeight;


		})
		.finally(() => {
			isScrolled = false;
		});
}
const last = document.querySelector("#chat-list .text-content[data-no]:last-child");

//마지막으로 확인한 메세지 저장
window.addEventListener("beforeunload", () => {
	const last = document.querySelector("#chat-list .text-content[data-no]:last-child").getAttribute("data-no");
	if (last) {
		navigator.sendBeacon("/chat/participant/lastMessage/" + chatRoomId);
	}
});
//해당 창이 포커스 되었을 때
window.addEventListener("focus", () => {
	const last = document.querySelector("#chat-list .text-content[data-no]:last-child");
	if (last) {
		fetch("/chat/participant/lastMessage/" + chatRoomId, { method: "POST" });
	}
	stompClient.send("/pub/chat/focus", {}, JSON.stringify({
		chatRoomId: chatRoomId,
		focused: true
	}));
});

window.addEventListener("blur", () => {
	stompClient.send("/pub/chat/focus", {}, JSON.stringify({
		chatRoomId: chatRoomId,
		focused: false
	}));
});