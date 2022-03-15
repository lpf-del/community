function SelectTag(t) {
    this.child = t.child || ".default",
        this.over = t.over || "on",
        this.all = t.all || ".all",
        this.init()
}
$.extend(SelectTag.prototype, {
    init: function() {
        var t = this;
        t._bindEvent(),
            t._select()
    },
    _changeURLPar: function(t, e, i) {
        var n = this
            , r = e + "=([^&]*)"
            , a = e + "=" + i
            , s = encodeURI(n._getQueryString(e));
        return t.match(r) ? t = t.replace(s, i) : t.match("[?]") ? t + "&" + a : t + "?" + a
    },
    _getQueryString: function(t) {
        var e = new RegExp("(^|&)" + t + "=([^&]*)(&|$)","i")
            , i = window.location.search.substr(1).match(e);
        return null != i ? decodeURI(i[2]) : null
    },
    _select: function() {
        var t = this
            , e = window.location.href;
        $(t.child).each(function() {
            var i = $(this).attr("name") + "=" + $(this).attr("rel")
                , n = new RegExp(encodeURI(i),"g");
            if (n.test(e)) {
                $(this).addClass(t.over);
                var r = $(this).attr("name");
                $("[name=" + r + "]").eq(0).removeClass(t.over)
            } else
                $(this).removeClass(t.over)
        })
    }
});
