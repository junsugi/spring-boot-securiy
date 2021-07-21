window.onload = function(){
    $("#removeUser").on("click", function(e){
        e.preventDefault();
        if(confirm("정말로 탈퇴하시겠습니까?")){
            $.ajax({
                url: "/user",
                type: "DELETE",
                data: {email: $("#email").val()},
                success: function(ret){
                    if(ret === 'success'){
                        alert("탈퇴가 완료되었습니다. 이용해주셔서 감사합니다.");
                        location.href="/index";
                    }
                },
                error: function(e){
                    alert(e.statusText);
                    console.log(e);
                }
            })
        }
    });
}