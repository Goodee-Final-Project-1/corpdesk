<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>조직 설계</title>
	
	<!-- jQuery & JSTree (조직도 트리용) -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/jstree/3.3.12/themes/default/style.min.css"/>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.6.0/jquery.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/jstree/3.3.12/jstree.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/jstree/3.3.12/jstree.search.min.js"></script>
    
	
	<c:import url="/WEB-INF/views/include/head.jsp"/>
	
	
	<style>
	
	.card-default{
	
		height:700px;
	
	}
	
	</style>
</head>

<c:import url="/WEB-INF/views/include/body_wrapper_start.jsp"/> 

	<c:import url="/WEB-INF/views/include/sidebar.jsp"/>

	<c:import url="/WEB-INF/views/include/page_wrapper_start.jsp"/>

		<c:import url="/WEB-INF/views/include/header.jsp"/>
	
		<c:import url="/WEB-INF/views/include/content_wrapper_start.jsp"/>
			<!-- 내용 시작 -->
			<div class="card card-default">
				<div class="card-body">
					<h5 class="mb-0">조직 설계</h5>
				
						<div class="row">
                    
                    <!-- 좌측: 조직도 -->
                    <div class="col-md-4 border-end">
                        <h6 class="mb-3">조직도</h6>
                        <div id="orgTree"></div>
                        <div class="mt-3">
                            <button class="btn btn-sm btn-primary">부서 추가</button>
                            <button class="btn btn-sm btn-danger">부서 삭제</button>
                        </div>
                    </div>
                    
                    <!-- 우측: 상세 정보 -->
                    <div class="col-md-8">
                        <h6 class="mb-3">부서 정보</h6>
                        <table class="table table-bordered">
                            <tr>
                                <th style="width: 30%">부서명</th>
                                <td id="deptName">-</td>
                            </tr>
                            <tr>
                                <th>부서코드</th>
                                <td id="deptCode">-</td>
                            </tr>
                            <tr>
                                <th>부서아이디</th>
                                <td id="deptId">-</td>
                            </tr>
                            <tr>
                                <th>상위부서</th>
                                <td id="deptLeader">-</td>
                            </tr>
                            <tr>
                                <th>생성일자</th>
                                <td id="createDate">-</td>
                            </tr>
                        </table>
                    </div>
                </div>
            </div>
        </div>

			
			
			
			
			
			<!-- 내용 끝 -->
		<c:import url="/WEB-INF/views/include/content_wrapper_end.jsp"/>
	
	<c:import url="/WEB-INF/views/include/page_wrapper_end.jsp"/>
	
<c:import url="/WEB-INF/views/include/body_wrapper_end.jsp"/>

<script>
$(function(){
    // 1. 트리 데이터 불러오기
    $.getJSON("/organization/tree", function(data){
        $('#orgTree').jstree({
            'core' : {
                'data' : data
            }
        });
    });

    // 2. 노드 클릭 시 상세정보 불러오기
    $('#orgTree').on("select_node.jstree", function (e, data) {
        let deptId = data.node.id;
        $.getJSON("/organization/" + deptId, function(detail){
            $("#deptName").text(detail.departmentName);
            $("#deptCode").text(detail.departmentId);
            $("#deptId").text(detail.departmentId);
            $("#deptLeader").text(detail.departmentLeader || "-");
            $("#createDate").text(detail.createDate || "-");
        });
    });
});

</script>



</html>