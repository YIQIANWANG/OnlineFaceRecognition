/**
 * @author Lee 
 * @date 2018-05-21
 */

/**
 * 土司
 */
var toast = window.toast || {};
$.extend(toast, {

	warn : function(msg) {
		this.open("警告", msg, "warning");
	},

	info : function(msg) {
		this.open("提示", msg, "info");
	},

	success : function(msg) {
		this.open("提示", msg, "success");
	},
	
	error : function(msg) {
		this.open("错误提示", msg, "error");
	},

	/**
	 * type : error/info/notice/success/warning
	 */
	open : function(title, message, type) {
		$.Toast(title, message, type, {
			position_class : "toast-top-center", // 顶部居中显示
			width : 300,
			stack : false,
			has_icon : true,
			has_close_btn : true,
			fullscreen : false,
			timeout : 3000,
			spacing : 10,
			sticky : false,
			has_progress : false,
			rtl : false
		});
	}
});

/**
 * 对话框
 */
var dialog = window.dialog || {};
$.extend(dialog, {
	
	alert : function(msg, callback) {
		layer.alert(msg, {icon : 0}, function(index){
			if(callback) callback();
			layer.close(index);
		})
	},
	
	confirm : function(msg, callback) {
		layer.confirm(msg, { icon : 3, title : '操作提示' /*,area: '300px'*/ }, function(index) {
			layer.close(index);
			if(callback) callback(); 
		});
	},
	
	/**
	 * 打开对话框(iframe层), 并指定一个页面
	 */
	open : function(options) {
		var windowHeight = $(window).height();
		var windowWidth = $(window).width();
		var defaultOptions = {
				width : "800px",
				height : (windowHeight - 50) + 'px', 
				content : window.location.href,
				title : "Dialog" ,
				type : 2,
				maxmin : false ,
				scrollbar : false ,
				okBtnText : "确定",
				showOkBtn : true,  // 是否显示确定按钮,默认显示,否则只显示取消按钮 , 如果需要显示确定按钮, 那么对话框中打开的页面中必须包含submit方法供按钮调用
				callback : function(rs) {}, // 如果显示确定按钮, 那么提交事件处理成功之后的回调
				cancelCallback : function(){} // 取消按钮的回调
		};

		var $options = $.extend(true, {}, defaultOptions, options);
		
		var w_ = parseInt($options.width,10), h_ = parseInt($options.height, 10);
		if(w_ > windowWidth) w_ = windowWidth;
		if(h_ > windowHeight) h_ = windowHeight;
		$options['area'] = [w_ + "px", h_ + "px"];
		if($options.showOkBtn) {
			$options['btn'] = [ $options.okBtnText, '关闭' ];
			$options['yes'] = function(index, layero) {
				var iframeWin = window[layero.find('iframe')[0]['name']];
				
				// 注册打开iframe对话框的窗体回调方法
				window.formSubmitCallback = function(rs) {
					layer.close(index);
					$options.callback(rs);
					window.formSubmitCallback = null;
				};

				iframeWin.submit();
				return false;
			};

			$options['btn2'] = function(index) {
				layer.close(index);
				if($options.cancelCallback) $options.cancelCallback();
			}
		} else {
			$options['btn'] = [ '关闭' ];
			$options['yes'] = function(index) {
				layer.close(index);
				if($options.cancelCallback) $options.cancelCallback();
			}
		}
		$options['cancel'] = function(index) {
			layer.close(index);
			if($options.cancelCallback) $options.cancelCallback();
		};

		layer.open($options);
	}, 
	
	/**
	 * 打开数据选择对话框
	 */
	openSelectDialog : function(title, url, width, height, callback ) {
		var dialogIndex = layer.open({
			title : title,
			type : 2,
			maxmin : false,
			area : [ width + 'px', height + 'px' ],
			content : url ,
			btn : [ '确定', '关闭' ],
			yes : function(index, layero) {
				var iframeWin = window[layero.find('iframe')[0]['name']];
				iframeWin.submit(function(datas){
					top.layer.close(dialogIndex);
					if(callback) {
						callback(datas);
					}
				});
				return false;
			},
			btn2 : function() {
				layer.close(dialogIndex);
			}
		});
	},
	
	loading : function() {
		layer.load(1, { shade: [0.1,'#000'] });
	}, 
	
	loaded : function() {
		layer.closeAll('loading'); 
	}
	
});

$(function(){
	layer.config({
		skin: 'custom-class'
	})
});
