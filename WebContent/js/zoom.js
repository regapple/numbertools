
	function imgZoom (){
		$('.imgBox').hover(function() {
		    $(this).addClass('on');
		    var image = $(this).find('img');
		    image.attr('src', getURLBySize(image.attr('orc'), 200));
		    var wl = image.attr('width');
		    if (wl < 190) {
		        $(this).find('.in').css('left', '0');
		    } else {
		        $(this).find('.in').css('left', -wl / 4);
		    }
		    
		},function(){
			 $(this).animate({
		    	  width: "32px",
		        height: "32px"
		    },
		    400).removeClass('on');
        var image = $(this).find('img');
        image.attr('src', getURLBySize(image.attr('orc'), 30));
		    $(this).find('.in').css('left', '0');
		});		
	}