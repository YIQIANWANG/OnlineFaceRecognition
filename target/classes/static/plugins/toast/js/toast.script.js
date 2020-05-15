/**
 * Toaster.js是一款Material Design风格jquery toast插件。Toaster.js提供5种主题，以及众多配置参数，用于显示toast类型的消息提示框。
		Toaster.js的特点有：
		提供5种主题风格。
		可以设置Toast自动消失。
		可以显示关闭按钮。
		可以显示关闭进度条。
		支持从右向左显示。
		
		
		$.Toast()对象需要接收4个参数，分别为：标题、消息、类型和参数。
		$.Toast(title, message, type, options);    
		title：Toast的标题。
		message：Toast的消息体。
		type：Toast的类型。
		options：配置参数。
		
		
		默认的配置参数如下：
		
		{
		    // append to body
		    appendTo: "body",
		    // is stackable?
		    stack: false,
		    // 'toast-top-left'
		    // 'toast-top-right'
		    // 'toast-top-center'
		    // 'toast-bottom-left'
		    // 'toast-bottom-right'
		    // 'toast-bottom-center'
		    position_class: "toast-bottom-right",
		    // true = snackbar
		    fullscreen: false,
		    // width
		    width: 250,
		    // space between toasts
		    spacing: 20,
		    // in milliseconds
		    timeout: 4000,
		    // has close button
		    has_close_btn: true,
		    // has icon
		    has_icon: true,
		    // is sticky
		    sticky: false,
		    // border radius in pixels
		    border_radius: 6,
		    // has progress bar
		    has_progress: false,
		    // RTL support
		    rtl: false
		}

 */

(function(){
    "use strict";
    $.Toast = function(title, message, type, options){
        var defaultOptions = {
            appendTo: "body",
            stack: false,
            position_class: "toast-bottom-right",
            fullscreen:false,
            width: 250,
            spacing:20,
            timeout: 4000,
            has_close_btn:true,
            has_icon:true,
            sticky:false,
            border_radius:6,
            has_progress:false,
            rtl:false
        }

        var $element = null;

        var $options = $.extend(true, {}, defaultOptions, options);

        var spacing = $options.spacing;

        var css = {
            "position":($options.appendTo == "body") ? "fixed" : "absolute",
            "width":$options.width,
            "display":"none",
            "border-radius":$options.border_radius,
            "z-index":999999999
        }

        $element = $('<div class="toast-item-wrapper ' + type + ' ' + $options.position_class + '"></div>');
        $('<p class="toast-title">' + title + '</p>').appendTo($element);
        $('<p class="toast-message">' + message + '</p>').appendTo($element);

        if($options.appendTo == "body" && ( $options.position_class == "toast-top-center" || $options.position_class == "toast-bottom-center" )) {
        	var left0 = ($(window).width() - $options.width) / 2;
        	css['left'] = left0;
        }
        
        if($options.fullscreen){
            $element.addClass( "fullscreen" );
        }

        if($options.rtl){
            $element.addClass( "rtl" );
        }

        if($options.has_close_btn){
            $('<span class="toast-close">&times;</span>').appendTo($element);
            if( $options.rtl){
                css["padding-left"] = 20;
            } else {
                css["padding-right"] = 20;
            }
        }

        if($options.has_icon){
            $('<i class="i toast-icon toast-icon-' + type + '"></i>').appendTo($element);
            if( $options.rtl){
                css["padding-right"] = 50;
            } else {
                css["padding-left"] = 50;
            }            
        }

        if($options.has_progress && $options.timeout > 0){
            $('<div class="toast-progress"></div>').appendTo($element);
        }

        if($options.sticky){
            $options.spacing = 0;
            spacing = 0;

            switch($options.position_class){
                case "toast-top-left" : {
                    css["top"] = 0;
                    css["left"] = 0;
                    break;
                }
                case "toast-top-right" : {
                    css["top"] = 0;
                    css["left"] = 0;                    
                    break;
                }
                case "toast-top-center" : {
                    css["top"] = 0;
                    css["left"] = css["right"] = 0;  
                    css["width"] = "100%";                  
                    break;
                }
                case "toast-bottom-left" : {
                    css["bottom"] = 0;
                    css["left"] = 0;                     
                    break;
                }
                case "toast-bottom-right" : {
                    css["bottom"] = 0;
                    css["right"] = 0;                     
                    break;
                }
                case "toast-bottom-center" : {
                    css["bottom"] = 0;
                    css["left"] = css["right"] = 0;  
                    css["width"] = "100%";                     
                    break;
                }
                default : {
                    break;
                }                                                                        
            }
        }

        if($options.stack){
            if($options.position_class.indexOf("toast-top") !== -1 ){
                $($options.appendTo).find('.toast-item-wrapper').each(function(){
                    css["top"] = parseInt($(this).css("top")) + this.offsetHeight + spacing;
                });
            } else if($options.position_class.indexOf("toast-bottom") !== -1 ){
                $($options.appendTo).find('.toast-item-wrapper').each(function(){
                    css["bottom"] = parseInt($(this).css("bottom")) + this.offsetHeight + spacing;
                });
            }
        }        

        $element.css(css);

        $element.appendTo($options.appendTo);

		if($element.fadeIn) {
            $element.fadeIn();
        }else {
            $alert.css({display: 'block', opacity: 1});
        }

		function removeToast(){          
			$.Toast.remove( $element );
		}

		if($options.timeout > 0){
			setTimeout(removeToast, $options.timeout);
            if($options.has_progress){
                $(".toast-progress", $element).animate({"width":"100%"}, $options.timeout);
            }
		}        

        $(".toast-close", $element).click(removeToast)

        return $element;
    }

    $.Toast.remove = function( $element ){
        "use strict";        
		if($element.fadeOut)
		{
			$element.fadeOut(function(){
				return $element.remove();
			});
		}
		else{
			$element.remove();
		}        
    }
})();