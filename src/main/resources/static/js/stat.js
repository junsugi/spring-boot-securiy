window.onload = function(){
    $("#searchBtn").on("click", function(e){
        e.preventDefault();
        var startDate = $("#startDate").val();
        var endDate = $("#endDate").val();
        $.ajax({
            url: '/api/user/stat?startDate='+startDate+'&endDate='+endDate,
            type: "GET",
            dataType: 'json',
            success: function(ret){
                if (ret !== undefined){
                    var html = "";
                    // 요약 정보
                    var summary = ret.startDate + "~" + ret.endDate + "<br/>" + "총 " + ret.userList.length +"명이 가입하였습니다."
                    $("#user-stat-summary").html(summary);
                    // 테이블 tr 추가
                    for(var i = 0; i < ret.userList.length; i++){
                        var user = ret.userList[i];
                        html += "<tr><td style='text-align: center'>"+ (ret.userList.length - i ) +"</td>";
                        for(var j = 0; j < user.length; j++){
                            html += "<td style='text-align: center'>" + user[j] + "</td>";
                        }
                        html += "</tr>";
                    }
                    $('#user-table-list').html(html);
                }
            },
            error: function(e){
                console.log(e);
                alert(e.statusText);
            }
        })
    });
}