function post(){
    var question = $("#question_id").val();
    var comment = $("#comment_content").val();
    $.ajax({
        type:"POST",
        url:"/comment",
        contentType:'application/json',
        data: JSON.stringify({
            "parentId":question,
            "content":comment,
            "type":0
        }),
        success:function (response){

            if(response.code == 200){
                $("#comment_content").hide();
            }else {
                if (response.code == 203){
                    var isAccepted = confirm(response.message);
                    if (isAccepted){
                        window.open("https://gitee.com/oauth/authorize?client_id=266407dfe065073d7c2b9b75183a9558ee8b6661b6587fc88e20277cbe2b6d3a&redirect_uri=http://localhost:8888/callback&response_type=code&state=1");
                        window.localStorage.setItem("closable", true);
                    }
                }else {
                    alert(response.message);
                }
            }
            console.log(response)
        },
        dataType:"json"
    });

}