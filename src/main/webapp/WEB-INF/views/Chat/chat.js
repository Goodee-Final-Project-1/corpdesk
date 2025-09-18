let stompClient = null;
let currentRoom = null;


let socket = new SockJS("/ws");
stompClient = Stomp.over(socket);

stompClient.connect({"Authorization": "Bearer ${cookie.accessToken.value}"},)

