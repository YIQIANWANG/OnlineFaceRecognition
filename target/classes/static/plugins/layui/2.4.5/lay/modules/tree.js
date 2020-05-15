layui.define("jquery",function(a){var d=layui.$,f=layui.hint();var b="layui-tree-enter",e=function(g){this.options=g};var c={arrow:["&#xe623;","&#xe625;"],checkbox:["&#xe626;","&#xe627;"],radio:["&#xe62b;","&#xe62a;"],branch:["&#xe622;","&#xe624;"],leaf:"&#xe621;"};e.prototype.init=function(h){var g=this;h.addClass("layui-box layui-tree");if(g.options.skin){h.addClass("layui-tree-skin-"+g.options.skin)}g.tree(h);g.on(h)};e.prototype.tree=function(k,i){var j=this,h=j.options;var g=i||h.nodes;layui.each(g,function(m,o){var p=o.children&&o.children.length>0;var n=d('<ul class="'+(o.spread?"layui-show":"")+'"></ul>');var l=d(["<li "+(o.spread?'data-spread="'+o.spread+'"':"")+">",function(){return p?'<i class="layui-icon layui-tree-spread">'+(o.spread?c.arrow[1]:c.arrow[0])+"</i>":""}(),function(){return h.check?('<i class="layui-icon layui-tree-check">'+(h.check==="checkbox"?c.checkbox[0]:(h.check==="radio"?c.radio[0]:""))+"</i>"):""}(),function(){return'<a href="'+(o.href||"javascript:;")+'" '+(h.target&&o.href?'target="'+h.target+'"':"")+">"+('<i class="'+o.icon+'"></i>')+("<cite>"+(o.name||"未命名")+"</cite></a>")}(),"</li>"].join(""));if(p){l.append(n);j.tree(n,o.children)}k.append(l);typeof h.click==="function"&&j.click(l,o);j.spread(l,o);h.drag&&j.drag(l,o)})};e.prototype.click=function(j,i){var h=this,g=h.options;j.children("a").on("click",function(k){layui.stope(k);g.click(i)})};e.prototype.spread=function(m,l){var k=this,i=k.options;var n=m.children(".layui-tree-spread");var j=m.children("ul"),g=m.children("a");var h=function(){if(m.data("spread")){m.data("spread",null);j.removeClass("layui-show");n.html(c.arrow[0]);g.find(".layui-icon").html(c.branch[0])}else{m.data("spread",true);j.addClass("layui-show");n.html(c.arrow[1]);g.find(".layui-icon").html(c.branch[1])}};if(!j[0]){return}n.on("click",h);g.on("dblclick",h)};e.prototype.on=function(i){var h=this,g=h.options;var j="layui-tree-drag";i.find("i").on("selectstart",function(k){return false});if(g.drag){d(document).on("mousemove",function(n){var k=h.move;if(k.from){var o=k.to,l=d('<div class="layui-box '+j+'"></div>');n.preventDefault();d("."+j)[0]||d("body").append(l);var m=d("."+j)[0]?d("."+j):l;(m).addClass("layui-show").html(k.from.elem.children("a").html());m.css({left:n.pageX+10,top:n.pageY+10})}}).on("mouseup",function(){var k=h.move;if(k.from){k.from.elem.children("a").removeClass(b);k.to&&k.to.elem.children("a").removeClass(b);h.move={};d("."+j).remove()}})}};e.prototype.move={};e.prototype.drag=function(l,k){var j=this,h=j.options;var g=l.children("a"),i=function(){var n=d(this),m=j.move;if(m.from){m.to={item:k,elem:l};n.addClass(b)}};g.on("mousedown",function(){var m=j.move;m.from={item:k,elem:l}});g.on("mouseenter",i).on("mousemove",i).on("mouseleave",function(){var n=d(this),m=j.move;if(m.from){delete m.to;n.removeClass(b)}})};a("tree",function(h){var g=new e(h=h||{});var i=d(h.elem);if(!i[0]){return f.error("layui.tree 没有找到"+h.elem+"元素")}g.init(i)})});