;(function($) {
  $.fn.tableSelect = function() {
    if($(this).length == 0){
      return;
    }
    $(this).addClass('drop');
    //拖拽
    $(this)
    .drag("start",function( ev, dd ){
      //取消所有选中
      if(!ev.ctrlKey){
        $(dd.available).each(function(i, item){
          var a = $(item).find('td:first input[type=checkbox]');
          if(a[0]){a[0].checked = false;$(item).removeClass('active');}
          
        });
      }
      return $('<div class="selection" />')
      .css({"background": "transparent"} )
      .appendTo( document.body );
    },{not: 'a,img,input,i,font', which: 1, click: false})
    .drag(function( ev, dd ){
      $( dd.proxy ).css({
        top: Math.min( ev.pageY, dd.startY ),
        left: Math.min( ev.pageX, dd.startX ),
        height: Math.abs( ev.pageY - dd.startY ),
        width: Math.abs( ev.pageX - dd.startX )
      });
    })
    .drag("end",function( ev, dd ){
      $( dd.proxy ).remove();
    });
    $('.drop')
      .drop("start",function(ev){
          if(!ev.ctrlKey){
          $('.drop').removeClass('dropped');
          }
        $( this ).addClass("active");
        $(this).find('td:first input[type=checkbox]')[0].checked = true;
      })
      .drop(function( ev, dd ){
        $( this ).toggleClass("dropped");
      })
      .drop("end",function(){
          if(!$(this).find('td:first input[type=checkbox]')[0].checked){
          $( this ).removeClass("active");
          }
          if($(this).find('td:first input[type=checkbox]')[0].checked){
          $( this ).addClass("dropped");
          }
        if('down' === arguments[1].mouseState){
          $(this).find('td:first input[type=checkbox]')[0].checked = false;
        }               
      });
    $.drop({ multi: true });
  }
})(jQuery);