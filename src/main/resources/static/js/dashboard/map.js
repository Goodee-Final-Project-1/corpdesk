/**
 *
 */
window.onload = function() {
  var mapContainer = document.getElementById('map'), // 지도를 표시할 div
    mapOption = {
      center: new kakao.maps.LatLng(33.450701, 126.570667), // 지도의 중심좌표
      level: 3 // 지도의 확대 레벨
    };

  var map = new kakao.maps.Map(mapContainer, mapOption); // 지도를 생성합니다


// 마커를 표시할 위치와 title 객체 배열입니다
  var positions = [
    // {
    //   title: '카카오',
    //   latlng: new kakao.maps.LatLng(33.450705, 126.570677)
    // },
    // {
    //   title: '생태연못',
    //   latlng: new kakao.maps.LatLng(33.450936, 126.569477)
    // },
    // {
    //   title: '텃밭',
    //   latlng: new kakao.maps.LatLng(33.450879, 126.569940)
    // },
    // {
    //   title: '근린공원',
    //   latlng: new kakao.maps.LatLng(33.451393, 126.570738)
    // }
  ];

  // 오늘의 일정 목록을 가져옴
  fetch('/personal-schedule/today', {
    method: 'GET'
  })
    .then(r => r.json())
    .then(r => {
      r.forEach(item => {
        console.log(item.personalScheduleId);
        console.log(item.scheduleName);
        console.log(item.scheduleDateTime);

        let scheduleDateTime = item.scheduleDateTime;

        // positions 배열에 새로운 객체 추가
        positions.push({
          title: `${scheduleDateTime.substring(11, 16)} ${item.scheduleName}\n${item.address}`,
          latlng: new kakao.maps.LatLng(item.latitude, item.longitude)
        });
      });

      console.log("positions: ", positions);

      // 마커 생성
      var imageSrc = "https://t1.daumcdn.net/localimg/localimages/07/mapapidoc/markerStar.png"; // 마커 이미지의 이미지 주소

      for (var i = 0; i < positions.length; i++) {

        // 마커 이미지의 이미지 크기 입니다
        var imageSize = new kakao.maps.Size(24, 35);

        // 마커 이미지를 생성합니다
        var markerImage = new kakao.maps.MarkerImage(imageSrc, imageSize);

        // 마커를 생성합니다
        var marker = new kakao.maps.Marker({
          map: map, // 마커를 표시할 지도
          position: positions[i].latlng, // 마커를 표시할 위치
          title: positions[i].title, // 마커의 타이틀, 마커에 마우스를 올리면 타이틀이 표시됩니다
          image: markerImage // 마커 이미지
        });

        // // 커스텀 오버레이에 표출될 내용으로 HTML 문자열이나 document element가 가능합니다
        // var content = '<div class="customoverlay">' +
        //     `    <span class="title">${positions[i].title}</span>` +
        //     '</div>';
        //
        // // 커스텀 오버레이가 표시될 위치입니다
        // var position = new kakao.maps.LatLng(positions[i].latlng.Ma, positions[i].latlng.La);
        //
        // // 커스텀 오버레이를 생성합니다
        // var customOverlay = new kakao.maps.CustomOverlay({
        //   map: map,
        //   position: positions[i].latlng,
        //   content: content,
        //   yAnchor: 1
        // });
      }

      // 모든 마커가 보이도록 지도 범위 재설정
      if (positions.length > 0) {
        var bounds = new kakao.maps.LatLngBounds();

        for (var i = 0; i < positions.length; i++) {
          bounds.extend(positions[i].latlng);
        }

        map.setBounds(bounds);
      }
    })
    .catch(error => {
      console.error('Error:', error);
    })
  ;
}