function post(type,e ){
    var data = e.getAttribute("id");
    console.log(type)

    if (type == 1){
        var comment = $("#comment_content1").val();
    }else {
        var comment = $("#comment_content").val();
    }
    console.log(comment)
    $.ajax({
        type:"POST",
        url:"/comment",
        contentType:'application/json',
        data: JSON.stringify({
            "parentId":data,
            "content":comment,
            "type":type
        }),
        success:function (response){
            console.log(response)
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
function collapseComments(e){
   var data1 = e.getAttribute("data-id");
   var comments = $("#comment-" + data1);
   //获取二级评论的展开状态
    var collapse = e.getAttribute("data-collapse");
    if (collapse){
        //折叠二级评论
        comments.removeClass("in");
        e.removeAttribute("data-collapse");
    }else {
        var subCommentContainer = $("#comment-" + data1);
        if (subCommentContainer.children().length != 1) {
            //展开二级评论
            comments.addClass("in");
            // 标记二级评论展开状态
            e.setAttribute("data-collapse", "in");
            e.classList.add("active");
        }else {


        $.getJSON("/comment/" + data1, function (data) {
            $.each(data.data.reverse(), function (index, comment) {
                console.log(comment)
                var mediaLeftElement = $("<div/>", {
                    "class": "media-left"
                }).append($("<img/>", {
                    "class": "media-object img-rounded he",
                    "src": comment.user.avatarUrl
                }));

                var mediaBodyElement = $("<div/>", {
                    "class": "media-body"
                }).append($("<h5/>", {
                    "class": "media-heading",
                    "html": comment.user.name
                  })).append($("<div/>", {
                    "html": comment.content
                })).append($("<div/>", {
                    "class": "menu"
                }).append($("<span/>", {
                    "class": "pull-right",
                    "html": moment(comment.gmtCreate).format('MM-DD')
                })));

                var mediaElement = $("<div/>", {
                    "class": "media"
                }).append(mediaLeftElement).append(mediaBodyElement);

                var commentElement = $("<div/>", {
                    "class": "col-lg-12 col-md-12 col-sm-12 col-xs-12 comments"
                }).append(mediaElement);
                subCommentContainer.prepend(commentElement);
            });
            //展开二级评论
            comments.addClass("in");
            // 标记二级评论展开状态
            e.setAttribute("data-collapse", "in");
            e.classList.add("active");
        });}
    }
}