var e="返回顶部",	
	t=$('<div class="back_to_top"></div>').appendTo($("body")).text(e).attr("title",e).click(function(){
		$("html, body").animate({scrollTop:0},120);
	});

function scroll(){
	var e=$(document).scrollTop(),n=$(window).height();
		e>300?t.fadeIn(500):t.fadeOut(500),window.XMLHttpRequest||t.css("top",e+n-166);
}

$(window).bind('scroll', scroll);		
