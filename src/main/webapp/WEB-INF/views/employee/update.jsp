<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
	
	<div>
		<form method="post" action="/employee/update/email">
            <input type="text" name="externalEmail" placeholder="이메일">
			<button>이메일 변경</button>
		</form>
        <form method="post" action="/employee/update/password">
            <input type="password" name="password" placeholder="비밀번호">
            <button>비밀번호 변경</button>
        </form>
	</div>
	
</body>
</html>