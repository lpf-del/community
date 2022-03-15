$(function () {
    helangSearch.init();
})
var new_label = "#"; // 选择标签的值
var label_index = 0; // 选择标签的下标
/**
 * 动态改变选择标签的值，将要选择的标签变为红色
 */
function label_new(label_val) {
    new_label = document.getElementsByName("labels")[label_val].getAttribute('value');
    document.getElementsByName("labels")[label_index].style = "";
    label_index = label_val;
    document.getElementsByName("labels")[label_val].style = "color : red;";
}

var new_time = 0; //选择时间的值
var time_index = 0;

function time_new(time) {
    new_time = document.getElementsByName("times")[time].getAttribute('value');
    document.getElementsByName("times")[time_index].style = "";
    time_index = time;
    document.getElementsByName("times")[time].style = "color : red;"
}

/**
 * 搜索： 输入搜索内容，选择搜索类型（评论、用户、文章），进行搜索。
 *  当搜索内容为空时提示搜素内容不能为空。
 */
function search() {
    let searchInput = document.getElementById("search-input").value;
    if (searchInput == null || searchInput == '' || searchInput == ' ') {
        window.wxc.xcConfirm("搜索内容不能为空", window.wxc.xcConfirm.typeEnum.error);
        return;
    }
    let searchType = document.getElementById("search_type").innerText;
    if (searchType == "文章搜索") {
        $.get("/articleLuceneSearch",
            {
                'str': searchInput,
                'time': new_time,
                'label': new_label
            },
            function (data) {
                show_article(data);
            }
        );
    } else if (searchType == "用户搜索") {
        $.get("/userSearchIndex",
            {
                'str': searchInput,
            },
            function (data) {
                show_user(data);
            }
        );
    } else if (searchType == "评论搜索") {
        $.get("/commentSearchIndex",
            {
                'str': searchInput,
                'time': 0,
                'articleId': 0
            },
            function (data) {
                show_comment(data);
            }
        );
    }
}

/**
 * 点击下拉框改变搜索筛选内容
 * 文章筛选：时间、标签
 * 评论筛选：时间
 * 用户筛选：无
 * @param id
 */
function li_value(id) {
    let li_var = document.getElementById(id).getAttribute('value');
    document.getElementById("search_type").innerText = li_var;
    if (id == "li_v_1") {
        document.getElementById("label_id").style = ""
        document.getElementById("time_id").style = "";
    } else if (id == "li_v_2") {
        document.getElementById("label_id").style = "display: none;"
        document.getElementById("time_id").style = "display: none;";
    } else if (id == "li_v_3") {
        document.getElementById("label_id").style = "display: none;"
        document.getElementById("time_id").style = "";
    }
}

/**
 * 循环获取文章json的数据字段
 * @param data
 */
function show_article(data) {
    //展示前先删除标签的子元素
    $('#comment_ul li').remove();
    $('#show_ul li').remove();
    let jsonData = JSON.parse(data);
    let xs = 0;
    let ys = 0;
    let articleId = "";
    let content = "";
    let picturePath = "";
    let releaseTime = "";
    let title = "";
    for (xs in jsonData) {
        for (ys in jsonData[xs]) {
            let values = jsonData[xs][ys];
            if (ys == 'articleId') articleId = values;
            if (ys == 'content') content = values;
            if (ys == 'picturePath') picturePath = values;
            if (ys == 'releaseTime') releaseTime = values;
            if (ys == 'title') title = values;
        }
        create_article(articleId, content, picturePath, releaseTime, title);
    }
}

/**
 * 将文章的数据展现到界面上
 * @param articleId
 * @param content
 * @param picturePath
 * @param releaseTime
 * @param title
 */
function create_article(articleId, content, picturePath, releaseTime, title) {
    let time = new Date(releaseTime).toLocaleString().replace(/:\d{1,2}$/, ' ');
    $('#show_ul').append(
        '<li class="blogs_list">' +
        '<a href="/profile?articleId=' + articleId + '" target="_blank">' +
        '<i>' +
        '<img src="' + picturePath + '" alt="">' +
        '</i>' +
        '<h2>' + title + '</h2>' +
        '</a>' +
        '<p>' + content + '</p>' +
        '<div class="blogs_base"><span class="blogs_time">' + time + '</span><span class="blogs_onclick">xxx</span></div>' +
        '<a href="/profile?articleId=' + articleId + '" target="_blank" class="read_more">阅读更多</a>' +
        '</li>');
}

/**
 * 循环获取评论json的数据字段
 * @param data
 */
function show_comment(data) {
    //展示前先删除标签的子元素
    $('#comment_ul li').remove();
    $('#show_ul li').remove();
    let jsonData = JSON.parse(data);
    let xs = 0;
    let ys = 0;
    let articleId = "";
    let commentContent = "";
    let commentId = "";
    let commentTime = "";
    let commentator = "";
    let username = "";
    let title = "";
    let commentType = "";
    for (xs in jsonData) {
        for (ys in jsonData[xs]) {
            let values = jsonData[xs][ys];
            if (ys == 'articleId') articleId = values;
            if (ys == 'commentContent') commentContent = values;
            if (ys == 'commentId') commentId = values;
            if (ys == 'commentTime') commentTime = values;
            if (ys == 'commentator') commentator = values;
            if (ys == 'username') username = values;
            if (ys == 'articleTitle') title = values;
            if (ys == 'commentType') commentType = values;
        }
        create_comment(articleId, commentContent, commentId, commentTime, commentator, username, title, commentType)
    }
}

/**
 * 将评论的数据展现到界面上
 * @param articleId
 * @param commentContent
 * @param commentId
 * @param commentTime
 * @param commentator
 * @param username
 * @param title
 */
function create_comment(articleId, commentContent, commentId, commentTime, commentator, username, title, commentType) {
    let time = new Date(commentTime).toLocaleString().replace(/:\d{1,2}$/, ' ');
    let label = "";
    if (commentType == 1) {
        label = "回复评论";
    } else if (commentType == 0) {
        label = "回复文章";
    }
    $('#comment_ul').append(
        '<li class="li_comment">' +
        '<div class="comment_li_1 comment_li"><a href="/profile?articleId=' + articleId + '">' + '回复:' + title + '</a></div>' +
        '<div class="comment_li_2 comment_li"><span class="comment_span">' + label + '</span>' + commentContent + '</div>' +
        '<div class="comment_li_3 comment_li"><span>' + username + '</span><span>' + time + '</span></div>' +
        '</li>'
    );
}

/**
 * 循环获取用户json的数据字段
 * @param data
 */
function show_user(data) {
    //展示前先删除标签的子元素
    $('#comment_ul li').remove();
    $('#show_ul li').remove();
    let jsonData = JSON.parse(data);
    let xs = 0;
    let ys = 0;
    let userId = "";
    let introduction = "";
    let postCount = "";
    let username = "";
    let user_tou_xiang = "";
    for (xs in jsonData) {
        for (ys in jsonData[xs]) {
            let values = jsonData[xs][ys];
            if (ys == 'userId') userId = values;
            if (ys == 'introduction') introduction = values;
            if (ys == 'postCount') postCount = values;
            if (ys == 'username') username = values;
            if (ys == 'touXiangUrl') user_tou_xiang = values;
        }
        create_user(userId, introduction, postCount, username, user_tou_xiang);
    }
}

/**
 * 将用户的数据展现到界面上
 * @param userId
 * @param introduction
 * @param postCount
 * @param username
 */
function create_user(userId, introduction, postCount, username, user_tou_xiang) {
    $('#comment_ul').append(
        '<li class="user_li">' +
        '<div class="user_left"><img src="' + user_tou_xiang + '" class="user_img"></div>' +
        '<div class="user_midden">' +
        '<div class="user_mid_1"><a href="/homePageById?userId=' + userId + '">' + username + '</a></div>' +
        '<div class="user_mid_2">发帖数：' + postCount + '</div>' +
        '<div class="user_mid_3">' + introduction + '</div>' +
        '</div>' +
        '<div class="user_right">' +
        '<div onclick="guangZhu(' + userId + ')" class="user_guangzhu">+关注</div>' +
        '</div>' +
        '</li>'
    );
}
