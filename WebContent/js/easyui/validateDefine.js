$(function() {
	/* textbox 添加验证*/
	$(".easyui-textbox[data-options*=missingMessage]").each(function(){
	    var that = this;
	    $(this).parent().find(".textbox-text").bind('blur',function(){
	        $(that).textbox('enableValidation');
	        $(that).textbox('validate');
	    });
	});
	$(".easyui-datebox[data-options*=missingMessage]").each(function(){
	    var that = this;
	    $(this).parent().find(".textbox-text").bind('blur',function(){
	        $(that).textbox('enableValidation');
	        $(that).textbox('validate');
	    });
	});
});