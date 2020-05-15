/**
 * 页面通用js脚本
 * @author Lee
 * @date 2018-05-21
 */

(function(win,ls){
	'use strict';
	
	// 页面初始化操作
	ls.init = function() {
		var _this = this;
		_this.ajaxSetting();
		_this.fitLayout();
		$(window).bind("resize", function () {
			_this.fitLayout();
	    });
	};
	
	ls.size = function() {
		return { width : $(win).width(), height : $(win).height() };
	};
	
	/**
	 * 初始化页面中的layui表单
	 * mods : 需要注册的组件,默认只注册['form']
	 */
	ls.initLayForm = function(options, mods) {
		var this_ = this;
		var defaultOptions = {
				initFun : function(layo){},
				sucCallback : function(rs){},
				errorCallback : function(rs){}
		};
		var defaultMods = ['form'], treeSelectMod = "treeSelect", hasTreeSelect = false ;
		if(typeof mods=='string') {
			if(mods === treeSelectMod) hasTreeSelect = true;
			defaultMods.push(mods);
		} else if((typeof mods == 'object') && mods.constructor === Array) {
			for(var i = 0;i < mods.length; i++) {
				var mod = mods[i]; 
				if(mod === treeSelectMod) hasTreeSelect = true;
				defaultMods.push(mod);
			}
		}
		
		if(hasTreeSelect) {
			layui.config({
			    base:  ctx 
			}).extend({
			    treeSelect: "js/plugin-layui-treeSelect"
			});
		}
		
		var $options = $.extend(true, {}, defaultOptions, options);
		layui.use(defaultMods, function(){
			var form = layui.form; 
			if($options.initFun) {
				var initObj = {form : form};
				for(var i = 0;i<defaultMods.length;i++) {
					var mod = defaultMods[i];
					if(mod !== 'form') {
						initObj[mod] = layui[mod];
					}
				}
				$options.initFun(initObj);
			}
			
			// 注册自定义验证规则: layui form 默认验证： required, phone, email, url, number, date, identity
			// 覆盖默认验证规则(email和phone),在单个验证的时候,如果不输入,则不验证,否则进行验证
			form.verify({
				email : function(value, item) {
					if($.trim(value) !== "") {
						if(!/^([a-zA-Z0-9_\.\-])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/.test(value)){
					      return '邮箱格式不正确';
					    }
					}
				},
				phone : function(value, item) {
					if($.trim(value) !== "") {
						if(!/^1\d{10}$/.test(value)){
					      return '请输入正确的手机号码';
					    }
					}
				},
				valid : function(value, item) { // 同名验证
					var input = $(item);
					var oldValue = input.attr("lay-oldValue") || "";
					if(value !== oldValue) {
						var msg ;
						$.ajax( {
							url : input.attr("lay-validUrl"),  type : "POST", global : false,
							data : {value : value , property : input.attr("name") }, dataType : "json", async : false, 
							success : function(rs) {
								if(rs.code === 400) msg = "已经存在,不可重复录入";
							}
						});
						return msg;
					}
				}
			});
			
			// 监听表单提交事件： 表单提交按钮的lay-filter="doSubmit"
			form.on('submit(doSubmit)', function(data){
	  		  var formData = data.field;
	  		  
	  		  // 解决checkbox如果多选只能提交一个值的问题,多个值用英文的逗号分隔
	  		  // 同时解决多个相同的name的问题
	  		  var checkBoxValues = {};
	  		  $("form").find("input[type='checkbox']:checked, input[type='text'], input[type='hidden'], input[type='number'] ").each(function(){
	  			   var name = $(this).attr("name"), value = $(this).val();
	  			   var values = checkBoxValues[name];
	  			   if(values) {
	  				   values.push(value);
	  			   } else {
	  				   values = [value];
	  			   }
	  			   checkBoxValues[name] = values;
	  		  });
	  		  for(var key in checkBoxValues) {
	  			  formData[key] = checkBoxValues[key].join(",");
	  		  }
	  		  
	  		  // 处理值为true|false的checkbox未被选中的情况
	  		  $("form").find("input[type='checkbox']").each(function(){
	  			   var name = $(this).attr("name"), value = $(this).val(), checked = $(this).prop("checked");
	  			   if(name && (value == "true" || value == "false") && !checked) {
	  				  formData[name] = value == "true" ? "false" : "true";
	  			   }
	  		  });
	  		  
	  		  this_.formSubmitSingle($("form").attr("action"), formData, $options.sucCallback, $options.errorCallback );
	  		  return false; 
	  	  });
			
		});
	};

	/**
	 * 通过dailog.open打开的对话框中包含表单数据提交的时候,可以用此方法提交数据请求
	 */
	ls.formSubmitSingle = function(url,params,sucCallback,errorCallback) {
		var this_ = this;
		this_.formSubmit(url, params, function(rs){
			  if(sucCallback) sucCallback(rs);
			  var _callback = top.formSubmitCallback;
			  if(_callback) _callback(rs);
		  },function(rs){
			  if(errorCallback) errorCallback(rs);
		  });
	};
	
	/**
	 * 表单提交-ajax请求处理
	 */
	ls.formSubmit = function(url, params,sucCallback,errorCallback) {
		var this_ = this;
		this_.ajax(url, params, function(rs){
			if(rs.code === 0) { // 出现错误信息
				top.toast.error(rs.msg);
				if(errorCallback) errorCallback(rs);
			} else if(rs.code === 1){ // 提交成功
				top.toast.success(rs.msg);
				if(sucCallback) sucCallback(rs);
			} else if(rs.code === 400){ // 数据校验失败
				var attributes = rs.datas;
				for(var field in attributes) {
					var input = $("#" + field);
					layer.tips(attributes[field], function(){
		                if(typeof input.attr('lay-ignore') !== 'string'){
		                    if(input[0].tagName.toLowerCase() === 'select' || /^checkbox|radio$/.test(input[0].type)){
		                      return input.next();
		                    }
		                  }
		                  return input;
		                }() ,  { tips: [3, 'red'], tipsMore: false });
				}
			} else {
				top.toast.warn("提交失败,出现未知错误");
			}
		});
	};
	
	/**
	 * 数据修改
	 */
	ls.updateTableData = function(options) {
		var this_ = this;
		var table = $("#" + options.tableId);
		var selectRows = table.bootstrapTable("getSelections");
		var len = selectRows.length;
		if(len === 0) {
			top.toast.warn("请选择需要处理的数据");
		} else {
			if(len > 1) {
				top.toast.warn("只能选择一条记录进行处理");
			} else {
				var row = selectRows[0];
				var validate = options.validate;
				var validateResult = true;
				if(validate) validateResult = validate(row);
				if(validateResult === true) {
					var id = row.id;
					options.url = this_.appendUrlPara(options.url, "id", id);
					this_.openFormDialog(options);
				} else {
					top.toast.warn(validateResult);
				}
			}
		}
	};
	
	/**
	 * 通用的打开表单页面处理方法
	 * options参数： tableId, url, callback, width, height
	 */
	ls.openFormDialog = function(options) {
		var defaultOptions = {
				title : "新增数据", 
				tableId : '',
				url : '',
				width : "750px",
				showOkBtn : true, 
				okBtnText : "确定", 
				success : function(rs){},
				cancel : function(){}
		};
		var $options = $.extend(true, {}, defaultOptions, options);
		
		// 打开对话框表单提交成功之后的回调方法: 如果有table,强制刷新数据, 然后再回调传入的callback
		var sucCallback = function(rs) {
			var table = $("#" + $options.tableId);
			if(table.length > 0) {
				try {
					table.bootstrapTable("refresh");
				} catch (e) {}
			}
			if($options.success) $options.success(rs);
		};
		
		var dialogOptions = {
				title :$options.title,
				width : $options.width,
				height : $options.height,
				content : $options.url,
				showOkBtn : $options.showOkBtn,
				okBtnText : $options.okBtnText,
				callback : sucCallback ,
				cancelCallback : $options.cancel
		};
		top.dialog.open(dialogOptions);
	};
	
	ls.deleteTableData = function(options) {
		var this_ = this;
		var table = $("#" + options.tableId);
		var selectRows = table.bootstrapTable("getSelections");
		if(selectRows.length === 0) {
			top.toast.warn("请选择需要处理的数据");
		} else {
			var msg = options.msg;
			if(!msg || $.trim(msg) === "") {
				msg = "您确定要删除选中的数据吗?";
			}
			top.dialog.confirm(msg, function(){
				var ids = [];
				for(var i = 0, len = selectRows.length; i < len; i++) {
					var id = selectRows[i].id;
					if(id) ids.push(id);
				}
				this_.ajax(options.url, {ids : ids}, function(rs){
					if(rs.code === 0) {
						var errorCallback = options.errorCallback;
						if(!errorCallback || errorCallback(rs)) {
							top.toast.error(rs.msg);
						}
					} else if(rs.code === 1){
						var succCallback = options.succCallback;
						if(!succCallback || succCallback(rs)) {
							top.toast.success(rs.msg);
							table.bootstrapTable("refresh");
						}
					} else {
						top.toast.warn("删除失败,出现未知错误");
					}
				});
			});
		}
	}
	
	
	ls.fitLayout = function() {
		var _this = this;
		var winSize = _this.size(), winHeight = winSize.height, winWidth = winSize.width;
		var container = $(".b-container");
		if(container.length === 0 || container.length > 1) return ;
		
		var containerHeight = winHeight - 2;
		container.css({height : containerHeight + "px"});
		
		var calBodyHeight = function(height_, fluidObj, cardObj) {
			var body = cardObj.find(".layui-card-body"), header = cardObj.find(".layui-card-header");
			var fluidPadTop = parseInt(fluidObj.css("padding-top"), 10), fluidPadBottom = parseInt(fluidObj.css("padding-bottom"), 10), 
			fluidMagTop = parseInt(fluidObj.css("margin-top"), 10), fluidMagBottom = parseInt(fluidObj.css("margin-bottom"), 10);
			var bodyPadTop = parseInt(body.css("padding-top"), 10), bodyPadBottom = parseInt(body.css("padding-bottom"), 10), 
			bodyMagTop = parseInt(body.css("margin-top"), 10), bodyMagBottom = parseInt(body.css("margin-bottom"), 10);
			var h1 = fluidPadTop + fluidPadBottom + fluidMagTop + fluidMagBottom;
			var h2 = bodyPadTop + bodyPadBottom + bodyMagTop + bodyMagBottom;
			var h3 = 0;
			if(header.length > 0 && header.css("display") !== "none") {
				h3 = header.outerHeight(true)
			}
			body.height(height_ - h1 - h2 - h3);
		}
		
		var fitTable = function(tableBody) {
			if(tableBody.find("table").length === 0) return ;
			var tableHeight = tableBody.innerHeight();
			var timeId = window.setInterval(function(){
				var table = tableBody.find(".fixed-table-body table");
				if(table.data("bootstrap-table-loaded") === "loaded" ) {
				//	var options = table.bootstrapTable("getOptions");
					var pagination = false;// options.pagination;
					table.bootstrapTable("resetView",{height : (pagination ? tableHeight : (tableHeight - 5) ) });
					var bootstrapTable = tableBody.find(".bootstrap-table");
					if(bootstrapTable.length !== 0) {
						var loading = bootstrapTable.find(".fixed-table-loading");
						var loadingWidth = loading.width(), loadingHeight = loading.height();
						var loadingMsg = loading.find(".fixed-table-loading-msg");
						var msgWidth = loadingMsg.outerWidth(true), msgHeight = loadingMsg.outerHeight(true);
						var msgLeft = (loadingWidth - msgWidth) / 2, msgTop = (loadingHeight - msgHeight) / 2;
						loadingMsg.css({left : msgLeft + "px", top : msgTop + "px"});
					}
					
					// 绑定bootstrap-table中自定的按钮[name='showToggleQuery']事件
					var queryToggleBtn = $("button[name='toggleQuery']");
					if(queryToggleBtn.length > 0) {
						if( queryToggleBtn.data("bindEvent") !== "1" ) {
							queryToggleBtn.bind("click",function(){
								var queryCon = tableBody.parents(".layui-card.ls-card").find(".layui-form.layui-card-header");
								if(queryCon.length > 0) {
									if(queryCon.css("display") === "none") queryCon.show(100);
									else queryCon.hide(100);
									window.setTimeout(function(){ _this.fitLayout(); }, 100);
								}
							});
							queryToggleBtn.data("bindEvent","1");
						}
					}
					
					window.clearInterval(timeId);
				}
			}, 10);
		}
		
		var sLeft = $(".b-layout.b-layout-left"), sRight = $(".b-layout.b-layout-right"), lrObj = {} ;
		if(sLeft.length > 0) lrObj['left'] = sLeft;
		if(sRight.length > 0) lrObj['right'] = sRight;
		for(var key in lrObj) {
			(function(side, sideObj){
				sideObj.css({height : containerHeight + "px"});
				var lFluid = sideObj.find(".layui-fluid.ls-card-fluid");
				var lCard = lFluid.find(".layui-card.ls-card");
				if(lCard.length > 0) {
					calBodyHeight(containerHeight, lFluid, lCard);
				}
				if(container.find(".b-layout-fold." + side).length <= 0) {
					var _fLeftClass = "fa-angle-left", _fRightClass="fa-angle-right";
					var fold = $("<div class='b-layout-fold "+side+"' title='隐藏/显示'><i class='fa "+ (side === "left" ? _fLeftClass : _fRightClass) +"'></i></div>").appendTo(container);
					var resizer = $("<div class='b-layout-resizer "+side+"'></div>").appendTo(container);
					var foldCss = { "top" : ( containerHeight - fold.outerHeight() ) / 2 + "px" };
					foldCss[side] = sideObj.outerWidth(true) + "px"
					fold.css(foldCss);
					var resizerCss = { height : lCard.outerHeight(true) + "px"};
					resizerCss[side] = sideObj.outerWidth(true) + "px"
					resizer.css(resizerCss);

					fold.click(function(){
						var i = fold.find("i");
						if(side === "left") {
							if(i.hasClass(_fLeftClass)) {
								sideObj.animate({ left : ( 0 - sideObj.outerWidth(true)) + "px"}, 50 , function(){
									i.removeClass(_fLeftClass).addClass(_fRightClass);
									fold.css({ left : "0px"});
									$(".b-layout-resizer.left").hide();
									_this.fitLayout();
								});
							} else {
								sideObj.animate({ left : "0px" }, 50 , function(){
									i.removeClass(_fRightClass).addClass(_fLeftClass);
									fold.css({ left : sideObj.outerWidth(true) + "px"});
									$(".b-layout-resizer.left").show();
									_this.fitLayout();
								});
							}
						} else {
							if(i.hasClass(_fRightClass)) {
								sideObj.animate({ right : ( 0 - sideObj.outerWidth(true)) + "px"}, 50 , function(){
									i.removeClass(_fRightClass).addClass(_fLeftClass);
									fold.css({ right : "0px"});
									$(".b-layout-resizer.right").hide();
									_this.fitLayout();
								});
							} else {
								sideObj.animate({ right : "0px" }, 50 , function(){
									i.removeClass(_fLeftClass).addClass(_fRightClass);
									fold.css({ right : sideObj.outerWidth(true) + "px"});
									$(".b-layout-resizer.right").show();
									_this.fitLayout();
								});
							}
						}
					});
					resizer.mousedown(function(){
						$(this).addClass("hover");
					}).mouseup(function(){
						$(this).removeClass("hover");
					});
					resizer.Tdrag({
					    scope:".b-container",
					    axis:"x",
					    cbMove : function(){
					    	if(side === "left") {
					    		var rLeft = resizer.css("left");
					    		sideObj.css({width : rLeft});
						    	fold.css({ left : rLeft});
					    	} else {
					    		var rLeft2 = parseInt(resizer.css("left"), 10);
					    		var rWidth = winWidth - rLeft2 - resizer.outerWidth(true);
					    		sideObj.css({width : rWidth + "px"});
						    	fold.css({ right : rWidth + "px"});
					    	}
					    	_this.fitLayout();
					    }
					});
				}
			})(key, lrObj[key]);
		}
		
		/*
		if(lLeft.length > 0) {
			lLeft.css({height : containerHeight + "px"});
			var lFluid = lLeft.find(".layui-fluid.ls-card-fluid");
			var lCard = lFluid.find(".layui-card.ls-card");
			if(lCard.length > 0) {
				calBodyHeight(containerHeight, lFluid, lCard);
			}
			
			if(container.find(".b-layout-fold").length <= 0) {
				var _fLeftClass = "fa-angle-left", _fRightClass="fa-angle-right";
				var fold = $("<div class='b-layout-fold' title='隐藏/显示'><i class='fa "+_fLeftClass+"'></i></div>").appendTo(container);
				var resizer = $("<div class='b-layout-resizer'></div>").appendTo(container);
				fold.css({ top : ( containerHeight - fold.outerHeight() ) / 2 + "px", left : lLeft.outerWidth(true) + "px"});
				resizer.css({ height : lCard.outerHeight(true) + "px", left : lLeft.outerWidth(true) + "px"});
				
				fold.click(function(){
					var i = fold.find("i");
					if(i.hasClass(_fLeftClass)) {
						lLeft.animate({ left : ( 0 - lLeft.outerWidth(true)) + "px"}, 50 , function(){
							i.removeClass(_fLeftClass).addClass(_fRightClass);
							fold.css({ left : "0px"});
							$(".b-layout-resizer").hide();
							_this.fitLayout();
						});
					} else {
						lLeft.animate({ left : "0px" }, 50 , function(){
							i.removeClass(_fRightClass).addClass(_fLeftClass);
							fold.css({ left : lLeft.outerWidth(true) + "px"});
							$(".b-layout-resizer").show();
							_this.fitLayout();
						});
					}
				});
				resizer.mousedown(function(){
					$(this).addClass("hover");
				}).mouseup(function(){
					$(this).removeClass("hover");
				});
				resizer.Tdrag({
				    scope:".b-container",
				    axis:"x", 
				    cbMove : function(){
				    	var rLeft = resizer.css("left");
				    	lLeft.css({width : rLeft});
				    	fold.css({ left : rLeft});
				    	_this.fitLayout();
				    }
				});
			}
		}
		*/
		
		var lCenter = $(".b-layout.b-layout-center");
		if(lCenter.length > 0) {
			var lLeftSize = 0, rRightSize = 0, lWidth = 0, rWidth = 0;
			var leftObj = lrObj['left'], rightObj = lrObj['right'];
			if(leftObj) {
				lLeftSize = parseInt(leftObj.css('left'), 10);
				lWidth = leftObj.outerWidth(true);
			}
			if(rightObj) {
				rRightSize = parseInt(rightObj.css('right'), 10);
				rWidth = rightObj.outerWidth(true);
			}
			var cWidth = winWidth - lLeftSize - rRightSize - lWidth - rWidth;
			lCenter.css({width : cWidth + "px", height : containerHeight + "px", right: (rWidth + rRightSize) + "px"});
			var cFluid = lCenter.find(".layui-fluid.ls-card-fluid");
			var cCard = cFluid.find(".layui-card.ls-card");
			if(cCard.length > 0) {
				calBodyHeight(containerHeight, cFluid, cCard);
				var bodyTable = cCard.find(".layui-card-body.ls-body-table");
				if(bodyTable.length > 0) {
					fitTable(bodyTable);
				}
			}
		}
	}
	
	ls.bootstrapTable = function(id, options) {
		var h_ = 400, tableContainer = $(".layui-card-body.ls-body-table");
		if(tableContainer.length > 0) {
			var tableContainerH = tableContainer.outerHeight();
			if(tableContainerH > h_) h_ = tableContainerH;
		}
		var defaultOptions = {
				classes : "table table-hover table-condensed",
				method : "post",
				striped : false,
				cache : false,
				height : h_,
				contentType : "application/x-www-form-urlencoded",
				queryParams : function(sParams) {
					return sParams || {};
				},
				search : true, 
				searchOnEnterKey : true, 
				searchTimeOut: 10, 
				queryParamsType : "noLimit",
				pagination : true,
				pageSize : 30,
				pageList : [30, 60, 100, 200, 500],
				paginationLoop : false, 
				sidePagination : "server",
				silentSort : false,
				sortable : true,
				idField: "id",
				uniqueId : "id", 
				sortOrder : "asc",
				clickToSelect : true, 
				searchAlign : 'right',
				buttonsAlign : "right",
				toolbarAlign : 'left',
				showPaginationSwitch: false,
				showRefresh : true,
				showToggleQuery: false, // 扩展属性，显示/异常查询条件
				showColumns : true,
				showToggle: false,
				showFullscreen : false,
				responseHandler: function(res) {
		            return { "total": res.total, "rows": res.rows };
			    },
			    onPostHeader : function() {
					$("#" + id).data("bootstrap-table-loaded", "loaded" );
				}
		};
		var $options = $.extend(true, {}, defaultOptions, options);
		var pagination = $options.pagination;
		if(!pagination) {
			$options['responseHandler'] = function(res) {
				return res;
			}
		}
		$("#" + id).bootstrapTable($options);
	};
	
	ls.treegrid = function(id, options) {
		var defaultOptions = {
				pagination : false,
				search: false,
				striped: false, 
				clickToSelect : false, 
				showColumns: false,
				treeShowField: 'name',
			    parentIdField: 'parentId',
				columns : [],
				onLoadSuccess : function(){
					$("#" + id).treegrid({
						treeColumn: 1,
			          	onChange: function() {
			        	  $("#" + id).bootstrapTable('resetWidth')
			          	}
				    });
				}
		};
		
		var $options = $.extend(true, {}, defaultOptions, options);
		this.bootstrapTable(id, $options);
	};
	
	ls.appendUrlPara = function(url, name, value) {  
		if(url.indexOf("?") === -1) url += "?";
	    else url += "&";
	    url += encodeURIComponent(name)+"="+encodeURIComponent(value);
	    return url;
	};
	
	ls.exportExcelByUrl = function(url, params) {
		var $form = $("<form id='excel-export-form' method='post' action='"+url+"' style='display:none;'></form>").appendTo("body");
		for(var key in params) {
			$("<input type='hidden' name='"+key+"' value='" + params[key] + "'>").appendTo($form);
		}
	    $form.submit();
	    window.setTimeout(function(){ $form.remove();}, 100);
	};
	
	ls.ajax = function(url, params, callback) {
		$.ajax( {
			url : url,
			type : "POST",
			cache : false,
			global : true,
			traditional : true,
			data : params,
			dataType : "json",
			success : function(rs) {
				if (callback) callback(rs);
			},
			beforeSend : function() {
				top.dialog.loading();
			}
		});
	}
	
	ls.ajaxUpload = function(url, formData, callback) {
		$.ajax({
			url : url,
			type : "POST",
			cache : false,
			global : true,
			traditional : true,
			processData : false,
			contentType : false,
			data : formData,
			dataType : "json",
			success : function(rs) {
				if (callback) callback(rs);
			},
			beforeSend : function(XMLHttpRequest) {
				top.dialog.loading();
			}
		});
	}
	
	ls.ajaxSetting = function() {
		$(document).ajaxStop(function(event, request, settings) {
			top.dialog.loaded();
		});
		$(document).ajaxComplete(function(event, request, settings){
			top.dialog.loaded();
			var responseJSON = request.responseJSON;
			if(responseJSON) {
				if(responseJSON.code === 500) {
					top.toast.error(responseJSON.msg);
				}
			}
		});
		$(document).ajaxSend(function(evt, request, settings) {
		});
		$(document).ajaxError( function(event, XMLHttpRequest, ajaxOptions, thrownError) {
			top.dialog.loaded();
			var status = XMLHttpRequest.status;
			if(status === 401) {
				top.location.reload();
			} else if(status === 403) {
				top.toast.warn("对不起,您没有访问权限");
			} else if(status === 404) {
				top.toast.error("对不起,您访问的地址不存在");
			} else if(status === 500) {
				top.toast.error("对不起,服务器出现异常");
			} else {
				top.toast.error("系统出现未知错误,请联系系统管理员处理");
			}
		});
	};

	// 获取url参数,键值不重复
    ls.getQueryVariable = function(name) {
    	var query = window.location.search.substring(1);
    	var vars = query.split("&");
    	for (var i=0; i<vars.length; i++) {
            var pair = vars[i].split("=");
            if(pair[0] === name){
            	return pair[1];
            }
    	}
    	return "";
    };
	
	ls.getObjectURL = function(file) {
    	var url = null;
    	if (window.createObjectURL !== undefined) {
    		url = window.createObjectURL(file);
    	} else if (window.URL !== undefined) {
    		url = window.URL.createObjectURL(file);
    	} else if (window.webkitURL !== undefined) {
    		url = window.webkitURL.createObjectURL(file);
    	}
    	return url;
    };
	
    ls.getBase64Image = function(img) {  
        var canvas = document.createElement("canvas");  
        canvas.width = img.width;  
        canvas.height = img.height;  
        var ctx = canvas.getContext("2d");  
        ctx.drawImage(img, 0, 0, img.width, img.height);  
        var ext = img.src.substring(img.src.lastIndexOf(".")+1).toLowerCase();
		return canvas.toDataURL("image/"+ext);
    };
    
    ls.scrollTop = function(callback) {
    	var topElem = $("<div class='ls-scroll-top' title='回到顶部'><i class='fa fa-angle-double-up'></i></div>").appendTo( $("body") );
    	topElem.click(function() {
    		if(callback) callback();
    	});
    };
    
    // 数据标签在数据表格中的显示
    ls.getDictLabel = function(map, value) {
    	var valueObj = map[value] || {};
    	var text = valueObj['text'] || "";
    	if(text === "") return "<div class='td-state none'>未知</div>";
    	return "<div class='" + valueObj.cssClass + "' style='" + valueObj['cssStyle'] + "'>" + text + "</div>";
    };
    
    // 数据表格中的排序字段显示标签
    ls.getSortLabel = function(value, rowData) {
    	return "<input type='number' id='"+ rowData.id +"'  class='layui-input inner-input' value='"+value+"'><span class='inner-label'>"+value+"</span>"
    };
    
    // 数据表格的排序按钮事件
    ls.doSortEvent = function(target, url) {
    	var btn = $(target), step = btn.data("_step_");
    	var inputCon = $(".fixed-table-container");
    	var innerInputs = inputCon.find(".layui-input.inner-input"), innerLabel = inputCon.find(".inner-label");
    	if(step === "save") {
    		var params = {};
    		innerInputs.each(function(){
    			var input = $(this);
    			params[input.attr("id")] = input.val();
    		});
    		this.formSubmit(url, params , function(){
    			innerInputs.hide();
    			innerLabel.show();
        		btn.html("<i class='fa fa-sort-numeric-asc'></i> 设置排序");
    			btn.data("_step_", "start");
        		$("button[name='refresh']").click();
    		});
    	} else {
    		innerInputs.show();
    		innerLabel.hide();
    		btn.html("<i class='fa fa-sort-numeric-asc'></i> 保存排序");
    		btn.data("_step_", "save");
    	}
    }
	
})(window, window.LS = {});

$(function() {
	LS.init();
});
