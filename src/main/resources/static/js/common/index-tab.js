/**
 * 系统首页动态标签
 * @author Lee 
 * @date 2017-12-12
 */

var tabOptPanelId = "tab-opts-panel"; // 选项卡操作面板id
$(function(){
	
	// 页面选项卡中的按钮事件处理：左右滚动和标签操作
	$(".ls-layout-center .tabs-roll").click(function(){
		var obj = $(this);
		if(obj.hasClass("tab-dropdown")) { 
			var isShow = $("body").data("tab-opts-panel");
			if("true" === isShow) {
				closeTabOpts();
			} else {
				openTabOpts();
			}
		} else {
			var direction = "";
			if(obj.hasClass("roll-left")) {
				direction = "right";
			} else if(obj.hasClass("roll-right")) {
				direction = "left";
			}
			tabScroll({direction:direction});
		}
	});
});

// 选项卡页滚动处理
function tabScroll(options) {
	window.setTimeout(function(){
	
		var direction = options ? options['direction'] : "";
		if(direction && direction != "") {
			var lsTabs = $(".ls-tabs");
			var lsTabsContainer = $(".tabs-item-container");
			var lsTabsWidth = lsTabs.width();
			var currentTabWidth = 0;
			$(".ls-tabs .nav-tabs li").each(function(){
				currentTabWidth += $(this).width();
			});
			
			var scrollLeft = 0;
			var step = 300;
			var lsTabsContainerLeft = parseInt(lsTabsContainer.css("left"),10);
			if(currentTabWidth > lsTabsWidth) {
				if(direction == "left") {
					var maxScrollLeft = currentTabWidth - lsTabsWidth;
					scrollLeft = lsTabsContainerLeft - step;
					if(Math.abs(scrollLeft) >= maxScrollLeft) {
						scrollLeft = -maxScrollLeft;
					}
				} else if(direction == "right") {
					var maxScrollLeft = 0;
					scrollLeft = lsTabsContainerLeft + step;
					if(scrollLeft >= maxScrollLeft) {
						scrollLeft = maxScrollLeft;
					}
				} else if(direction == "active") {
					var liObj = options.tab;
					var liLeftTabWidth = 0;
					var lis = $(".ls-tabs .nav-tabs li");
					for(var i = 0;i<lis.length;i++) {
						var li = $(lis[i]);
						if(li.attr("id") == liObj.attr("id")) break;
						liLeftTabWidth += li.width();
					}
					var liLeft = lsTabsContainerLeft + liLeftTabWidth;
					var liWidth = liObj.width();
					var liLeft2 = liLeft + liWidth;
					
					var viewWidth = lsTabsWidth;
					if(liLeft >= 0 && liLeft <= viewWidth && liLeft2 >=0 && liLeft2 <= viewWidth) { // 当前选项卡在可视范围内,不处理
						return ;
					}
					if(liLeft < 0) {
						scrollLeft = - liLeftTabWidth;
					} else if(liLeft >= viewWidth || liLeft2 >= viewWidth) {
						scrollLeft = viewWidth - (liLeftTabWidth + liWidth);
					}
				}
			}
			
			// 控制连续点击滚动按钮
			if($("body").data("tab-scroll-finished") != "false" ){
				$("body").data("tab-scroll-finished","false");
				lsTabsContainer.animate({left : scrollLeft}, 500 ,function(){
					$("body").data("tab-scroll-finished","true");
				});
			}
		}
		
	},10);
}

// 打开选项卡的的操作面板
function openTabOpts() {
	var panel = $("#" + tabOptPanelId);
	if(panel.length <= 0) {
		var tabDrop = $("<div id='"+tabOptPanelId+"' class='dropdown-info' style='top:-250px;display:block;z-index:98;'></div>").appendTo($(".ls-layout-center"));
		var tabUl = $("<ul><ul>").appendTo(tabDrop);
		tabUl.append($("<li><a href='javascript:;' data-method='locCurrentTab'><i class='fa fa-location-arrow'></i>定位当前选项卡</a></li>"));
		tabUl.append($("<li><a href='javascript:;'  data-method='closeCurrentTab'><i class='fa fa-close'></i>关闭当前选项卡</a></li>"));
		tabUl.append($("<li><a href='javascript:;' data-method='refreshCurrentTab'><i class='fa fa-refresh'></i>刷新当前选项卡</a></li>"));
		tabUl.append($("<li class='divider'></li>"));
		tabUl.append($("<li><a href='javascript:;' data-method='closeOtherTab'><i class='fa fa-window-close'></i>关闭其他选项卡</a></li>"));
		tabUl.append($("<li><a href='javascript:;' data-method='closeAllTab'><i class='fa fa-window-close-o'></i>关闭全部选项卡</a></li>"));
		tabUl.append($("<li class='divider'></li>"));
		tabUl.append($("<li><a href='javascript:;'><i class='fa fa-angle-double-up'></i>关闭当前操作面板</a></li>"));
		tabDrop.animate({top : 40}, 100 ,function(){
			$("body").data("tab-opts-panel","true");
			$(".ls-tabs .tab-dropdown").find("i").attr("class","fa fa-chevron-up");
		});
		tabDrop.find("a").click(function(){
			closeTabOpts();
			var method = $(this).data("method");
			if(method && method != "") {
				eval(method + "()");
			}
		});
	}
}

// 关闭选项卡的操作面板
function closeTabOpts() {
	var panel = $("#" + tabOptPanelId);
	if(panel.length > 0) {
		panel.animate({top : -250}, 100 ,function(){
			panel.remove();
			$("body").data("tab-opts-panel","false");
			$(".ls-tabs .tab-dropdown").find("i").attr("class","fa fa-chevron-down");
		});
	}
}

// 添加选项卡
function addTab(idSuffix,url,text,iconClass) {
	var tabs = $(".ls-tabs .nav-tabs");
	var content = $(".tab-content");
	
	var liId = "li-" + idSuffix;
	var tabId = "tab-" + idSuffix;
	var frameId = "frame-" + idSuffix;
	
	if($("#" + tabId).length > 0) { // 选项卡已经存在
		refreshCurrentTab(idSuffix, false, url);
	} else {
		pageLoading();
		var li = $("<li id='"+liId+"'><a href='#"+tabId+"' data-toggle='tab'><i class='"+iconClass+"'></i>"+text+"<span class='tab-close'><i class='fa fa-close' title='关闭'></i></span></a></li>").appendTo(tabs);
		$("<div class='tab-pane fade' id='"+tabId+"'><iframe id='"+frameId+"' name='"+frameId+"' frameborder='' src='"+url+"'></iframe></div>").appendTo(content);
		$("#" + frameId).bind("load",pageLoaded);
		li.find(".tab-close").click(function(){
			closeCurrentTab(idSuffix);
		});
	}
	
	tabs.find("li").removeClass("active");
	content.find(".tab-pane").removeClass("in active");
	var liObj = $("#" + liId).addClass("active");
	$("#" + tabId).addClass("in active");
	
	locCurrentTab(liObj);
}

//定位到当前选中的选项卡,如果当前选项卡标签不在可视范围内,则移动到可视范围内
//1. 添加菜单定位; 2. 主动点击定位按钮定位; 3. 刷新当前选项卡定位; 4. 关闭选项卡定位
function locCurrentTab(liObj) {
	if(liObj == null || typeof liObj == "undefined") {
		liObj = getCurrentActiveTab()['tab'];
	}
	tabScroll({direction:"active", tab : liObj});
}


//刷新当前选中的选项卡: 如果指定idSuffix,则关闭指定的选项卡, isLoc : 刷新的同时是否需要定位
function refreshCurrentTab(idSuffix, isLoc, url) {
	var liObj = null;
	var frame = null;
	var isLocUrl = false;
	if(typeof idSuffix == "undefined") {
		var p = getCurrentActiveTab();
		liObj = p.tab;
		paneObj = p.content;
		isLocUrl = true;
	} else {
		liObj = $("#li-" + idSuffix);
		paneObj = $("#tab-" + idSuffix);
	}
	if(liObj != null && paneObj != null) {
		pageLoading();
		var frame = paneObj.find("iframe");
		frame.attr("src", isLocUrl ? frame.attr("src") : url );
		var events = $._data(frame[0],'events');
		if( !(events && events["load"]) ){
			frame.bind("load",pageLoaded);
		}
		if(isLoc != false) {
			locCurrentTab(liObj);
		}
	}
}

// 关闭当前选中的选项卡: 如果指定idSuffix,则关闭指定的选项卡
function closeCurrentTab(idSuffix) {
	var liObj = null;
	var paneObj = null;
	if(typeof idSuffix == "undefined") {
		var p = getCurrentActiveTab();
		liObj = p.tab;
		paneObj = p.content;
	} else {
		liObj = $("#li-" + idSuffix);
		paneObj = $("#tab-" + idSuffix);
	}
	if(liObj != null && paneObj != null) {
		var closable = liObj.data("closable") + "";
		if(closable != "false") {
			if(liObj.hasClass("active")) {
				var preLi = liObj.prev();
				preLi.addClass("active");
				$("#tab" + preLi.attr("id").replace("li","")).addClass("in active");
			}
			liObj.remove();
			paneObj.remove();
			locCurrentTab();
		}
	}
}

// 关闭当前未选中的选项卡
function closeOtherTab(){
	$(".ls-tabs .nav-tabs").find("li").each(function(){
		var li = $(this);
		var closable = li.data("closable") + "";
		if(closable != "false" && !li.hasClass("active") ) {
			$("#tab" + li.attr("id").replace("li","")).remove();
			li.remove();
			$(".tabs-item-container").css("left","0");
		}
	});
}

// 关闭所有的选项卡
function closeAllTab() {
	$(".ls-tabs .nav-tabs").find("li").each(function(){
		var li = $(this);
		var closable = li.data("closable") + "";
		if(closable != "false" ) {
			$("#tab" + li.attr("id").replace("li","")).remove();
			li.remove();
			$("#li-ls-home").addClass("active");
			$("#tab-ls-home").addClass("in active");
			$(".tabs-item-container").css("left","0");
		}
	});
}

/*
 * 获取当前选中的选项卡
 */
function getCurrentActiveTab() {
	var liObj = $(".ls-tabs .nav-tabs").find("li.active");
	var paneObj = $(".tab-content").find(".tab-pane.active");
	if(liObj.length <= 0) {
		liObj = $("#li-ls-home");
		paneObj = $("#tab-ls-home");
	}
	if(liObj.length <= 0) {
		liObj = null;
		paneObj = null;
	}
	return {tab : liObj, content : paneObj}
}
