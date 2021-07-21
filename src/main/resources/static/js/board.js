window.onload = function(){

    $("#logout").on("click", function(e){
        e.preventDefault();
        if(confirm("로그아웃 하시겠습니까?")){
            $.ajax({
                url: "/loggedout",
                type: "GET",
                dataType: 'text',
                success: function(ret){
                    console.log(ret);
                    if(ret === "success"){
                        alert("로그아웃 되었습니다.");
                        location.href="/index";
                    }
                },
                error: function(e){
                    alert(e.statusText);
                    console.log(e);
                }
            });
        }
    });
}