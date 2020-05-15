/**
 * 数据选择器
 * by Lee on 2019-02-10
 */

var Selector = window.Selector || {};

// 组织结构选择器
Selector.org = function(callback) {
	top.dialog.openSelectDialog("选择组织机构", ctx + "admin/system/origanization/select", 700, $(top.window).height() - 150, function(datas){
		if(callback) callback(datas);
	});
};

