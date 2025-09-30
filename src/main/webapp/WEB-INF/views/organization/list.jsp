<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>조직 설계</title>
	
	<style>
	
	.card-default{
	
		
	
	}
	
	.col-lg-6{
	
	margin-top:50px;
	
	
	}
	

#orgTree .jstree-anchor {
  color: #333;
  font-weight: 500;
  font-size: 20px;
}


	</style>

<c:import url="/WEB-INF/views/include/head.jsp"/>
</head>

<c:import url="/WEB-INF/views/include/body_wrapper_start.jsp"/> 

	<c:import url="/WEB-INF/views/include/sidebar.jsp"/>

	<c:import url="/WEB-INF/views/include/page_wrapper_start.jsp"/>

		<c:import url="/WEB-INF/views/include/header.jsp"/>
	
		<c:import url="/WEB-INF/views/include/content_wrapper_start.jsp"/>
		
		
			<!-- 내용 시작 -->
			<div class="card card-default">
				<div class="card-body">
				
				 <h2>조직설계</h2>
				 
					<div class="col-lg-6 col-xl-3">
				    <div class="card mb-4 p-0">
				      <h5 class="card-title  pt-4 px-6">조직도</h5>
				       
						<div id="orgTree"></div>
				      
				      
				      <!-- 버튼 영역 -->
						  <div class="mt-3">
						    <button id="addBtn" class="btn btn-primary btn-sm">부서 추가</button>
						    <button id="deleteBtn" class="btn btn-danger btn-sm">부서 삭제</button>
						  </div>
						</div>
				    </div>
				  </div>
				  </div>
				  
				  <!-- 추가 모달 -->
			<div class="modal fade" id="addModal" tabindex="-1" aria-hidden="true">
			  <div class="modal-dialog">
			    <div class="modal-content">
			      <div class="modal-header">
			        <h5 class="modal-title">부서 추가</h5>
			        <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
			      </div>
			      <div class="modal-body">
			        <div class="mb-3">
			          <label class="form-label">부서명</label>
			          <input type="text" id="newDeptName" class="form-control" placeholder="예: 품질관리팀">
			        </div>
			        <div class="mb-3">
			          <label class="form-label">상위 부서명</label>
			          <input type="text" id="parentDeptName" class="form-control" placeholder="예: 개발팀 (없으면 루트)">
			        </div>
			      </div>
			      <div class="modal-footer">
			        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">취소</button>
			        <button type="button" class="btn btn-primary" id="saveDeptBtn">저장</button>
			      </div>
			    </div>
			  </div>
			</div>
			
			<!-- 삭제 모달 -->
			<div class="modal fade" id="deleteModal" tabindex="-1" aria-hidden="true">
			  <div class="modal-dialog">
			    <div class="modal-content">
			      <div class="modal-header">
			        <h5 class="modal-title">부서 삭제</h5>
			        <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
			      </div>
			      <div class="modal-body">
			        정말 삭제하시겠습니까? (하위 부서도 같이 삭제됩니다)
			      </div>
			      <div class="modal-footer">
			        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">취소</button>
			        <button type="button" class="btn btn-danger" id="confirmDeleteBtn">삭제</button>
			      </div>
			    </div>
			  </div>
			</div>
	            
	     

			
			
			
			
			
			<!-- 내용 끝 -->
		<c:import url="/WEB-INF/views/include/content_wrapper_end.jsp"/>
	
	<c:import url="/WEB-INF/views/include/page_wrapper_end.jsp"/>
	
<c:import url="/WEB-INF/views/include/body_wrapper_end.jsp"/>

<script>
const ctx = '${pageContext.request.contextPath}';

$(function(){
	  $.getJSON("/organization/tree", function(data){
	    $('#orgTree').jstree({
	      "core" : {
	        "data" : data
	      }
	    });
	  });

	  // 노드 클릭 이벤트
	  $('#orgTree').on("select_node.jstree", function (e, data) {
	    let deptId = data.node.id;
	    $.getJSON("/organization/" + deptId, function(detail){
	      alert("부서명: " + detail.departmentName);
	    });
	  });
	});



$(function(){
	  // 트리 초기화
	  $.getJSON("/organization/tree", function(data){
	    $('#orgTree').jstree({
	      "core": {
	        "check_callback": true,
	        "data": data
	      }
	    });
	  });

	  // 현재 선택된 노드 반환
	  function getSelectedNode() {
	    var tree = $('#orgTree').jstree(true);
	    var sel = tree.get_selected(true);
	    return sel.length > 0 ? sel[0] : null;
	  }

	  // ===========================
	  // 추가 버튼 → 모달 열기
	  // ===========================
	  $("#addBtn").on("click", function(){
	    $("#newDeptName").val("");
	    $("#parentDeptName").val("");
	    $("#addModal").modal("show");
	  });


	  // 저장 버튼 → DB 반영
		$("#saveDeptBtn").on("click", function(){
		  let name = $("#newDeptName").val().trim();
		  let parentName = $("#parentDeptName").val().trim();
		
		  if(!name) { 
		    alert("부서명을 입력하세요."); 
		    return; 
		  }
		
		  $.post("/organization/addByName", { parentName, name })
		    .done(function(newNode){
		      var tree = $('#orgTree').jstree(true);
		      tree.create_node(newNode.parentId == null ? "#" : newNode.parentId, {
		        id: newNode.id,
		        text: newNode.name
		      });
		
		      $("#addModal").modal("hide");
		      alert("부서가 추가되었습니다!");
		      location.reload();
		    })
		    .fail(function(xhr){
		      if(xhr.status === 409) {
		        alert("이미 존재하는 부서명입니다.");
		      } else {
		        alert("부서 추가 중 오류가 발생했습니다.");
		      }
		    });
		  
		});


	  // ===========================
	  // 삭제 버튼 → 모달 열기
	  // ===========================
	  $("#deleteBtn").on("click", function(){
	    var selected = getSelectedNode();
	    if(!selected) { alert("삭제할 부서를 선택하세요."); return; }
	    $("#deleteModal").modal("show");
	  });

	  // 삭제 확인
	  $("#confirmDeleteBtn").on("click", function(){
	    var selected = getSelectedNode();
	    if(!selected) return;

	    $.post("/organization/deleteCascade", { id: selected.id }, function(){
	      $('#orgTree').jstree(true).delete_node(selected);
	      $("#deleteModal").modal("hide");
	      alert("부서가 삭제되었습니다!");
	      location.reload();
	    });
	  });
	});

</script>



<!-- jsTree (로컬에 저장한 버전 사용) -->
<link rel="stylesheet" href="<c:url value='/css/themes/default/style.min.css'/>" />
<script src="<c:url value='/js/jstree.min.js'/>"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>

</html>