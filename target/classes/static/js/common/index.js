/**
 * 系统首页
 * @author Lee
 * @date 2017-12-12
 */
$(function() {

    fixLayout();
    $(window).bind("resize", function() {
        fixLayout();
    });

    // 左侧菜单
    var sideMenuObj = $('#side-menu');
    sideMenuObj.metisMenu();
    sideMenuObj.find("li:first a:first").click();

    // 左侧伸缩
    $("#btn-side-outdent").click(function() {
        var leftObj = $(".ls-layout-left").toggleClass("mini-sidebar");
        var itemObj = $(".sidebar .nav>li>a");
        var sideObj = $(this);
        var headerObj = $(".ls-layout-header");
        if (leftObj.hasClass("mini-sidebar")) {
            itemObj.addClass("center").find("span").hide();
            sideObj.find("i").attr("class", "fa fa-indent");
            headerObj.css("padding-left", "60px");
        } else {
            itemObj.removeClass("center").find("span").show();
            doAnimated(itemObj.find("span"), "animated fadeInLeft");
            sideObj.find("i").attr("class", "fa fa-outdent");
            headerObj.css("padding-left", "220px");
        }
        fixLayout();
    });
    $(".sidebar .nav>li>a").hover(function() {
        if ($(".ls-layout-left").hasClass("mini-sidebar")) {
            var item = $(this);
            layer.tips(item.text(), item, {tips: [2, '#0FA6D8']});
        }
    }, function() {
        layer.closeAll("tips");
    });

    // 菜单点击事件
    $(".metismenu a").click(function() {
        menuClickEvent($(this));
        $(".metismenu a.active").removeClass("active");
        $(this).addClass("active");
    });

    // 绑定Header菜单的hover事件
    $(".ls-header-menu-item.item-dropdown").hover(function() {
        closeTabOpts();
        $(this).find(".dropdown-info").show();
    }, function() {
        $(this).find(".dropdown-info").hide();
    });

    window.setTimeout(function() {
//		doAnimated($(".ls-page-loading"),"animated zoomOut",function(){
        $(".ls-page-loading").data("init", "true").hide();
        fixLayout();
//		});
    }, 1000);

    showTime();
});

// 触发左侧菜单的显示/隐藏事件
function toggleMenu() {
    $("#btn-side-outdent").click();
}

// 菜单点击事件
function menuClickEvent(aObj) {
    var url = aObj.data("url");
    if (url !== "undefined" && $.trim(url) !== "") {
        var id = aObj.attr("id");
        var text = aObj.text();
        var iconClass = aObj.find("i:first").attr("class");
        var open = aObj.data("open");
        if (open === "dialog") {
            dialog.open({
                width: $(window).width() + "px",
                height: $(window).height() + 'px',
                content: url,
                title: text,
                showOkBtn: false
            });
        } else if (open === "blank") {
            window.open(url, text);
        } else {
            addTab(id, url, text, iconClass);
        }
    }
}

// 固定页面布局
function fixLayout() {
    var wHeight = $(window).height();
    var wWidth = $(window).width();
    $(".ls-layout-left-menu").height(wHeight - 50);

    var left = $(".ls-layout-left");
    var center = $(".ls-layout-center");
    var loading = $(".ls-page-loading");
    var centerPadding = 0;

    var leftWidth = left.width();
    var centerHeight = wHeight - 50 - 30 - centerPadding * 2;
    var centerWidth = wWidth - leftWidth - centerPadding * 2;

    var centerCss = {
        "height": centerHeight + "px",
        "width": centerWidth + "px",
        "right": centerPadding + "px",
        "bottom": (centerPadding + 30) + "px"
    };
    center.css(centerCss);
    var tabContentHeight = centerHeight - 40;
    $(".tab-content").height(tabContentHeight);

    $(".ls-layout-bottom").width(wWidth - leftWidth);

    if (loading.data("init") === "true") {
        loading.css(centerCss).height(centerHeight - 40 - 1);
    }
}

function pageLoading() {
    $(".ls-page-loading").show();
}

function pageLoaded() {
    $(".ls-page-loading").hide();
}

// 回到首页按钮事件
function toHomePage() {
    closeAllTab();
}

// 系统注销操作
function logout() {
    layer.confirm('您确定要注销系统登录吗?', {icon: 3, title: '提示'}, function(index) {
        layer.close(index);
        window.location.href = ctx + "admin/logout";
    });
}

// 通用动画方法
function doAnimated(obj, clazz, callback) {
    obj.addClass(clazz).one('webkitAnimationEnd mozAnimationEnd MSAnimationEnd oanimationend animationend', function() {
        $(this).removeClass(clazz);
        if (callback) callback();
    });
}

function showTime() {
    var currentDate = new Date();
    var hour = currentDate.getHours();
    var minute = currentDate.getMinutes();
    var second = currentDate.getSeconds();
    var time = (hour < 10 ? "0" + hour : hour);
    time = time + ":" + (minute < 10 ? "0" + minute : minute);
    time = time + ":" + (second < 10 ? "0" + second : second);
    $(".ls-header-menu-btn.clock").find("span").html(time);
    window.setTimeout(showTime, 1000);
}
