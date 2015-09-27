String.prototype.trim = function() {
  return this.replace(/^\s*(.*?)\s*$/, '$1');
};
String.prototype.parseTpl =function(o){
  return this.replace(/{([^{}]*)}/g, function(a, b){
    var r =o[b];
    return (typeof r === 'number' || typeof r === 'string' || typeof r === 'boolean') ? r : '';
  });
};
function dateFormat(date) {
  if (date instanceof Date) {
    var year = date.getFullYear(), month = date.getMonth() + 1, day = date
            .getDate();
    if (parseInt(day) < 10) {
      day = "0" + day;
    }
    if (parseInt(month) < 10) {
      month = "0" + month;
    }
    return year + '-' + month + '-' + day;
  }
  return '';
}

(function($) {
  // 居中
  $.fn.extend({
    center: function() {
      return this.each(function() {
        var position = $(this).parent().offset();
        var top = ($(window).height() - $(this).height()) / 2 + position.top;
        var left = ($(window).width() - $(this).width()) / 2 + position.left;
        if($(document.body).css('position') === 'relative'){
          var pos = $(document.body).offset();
          top = top - pos.top;
          left = left - pos.left;
        }
        
        if ($(this).css('position') === 'absolute') {
          top += $(window).scrollTop();
          left += $(window).scrollLeft();
        }
        $(this).css({/* position:'absolute', */
          margin: 0,
          top: (top > 0 ? top : 0) + 'px',
          left: (left > 0 ? left : 0) + 'px'
        });
      });
    },
    loading: function(options) {
      var opts = $.extend({}, $.fn.loading.defaults, options), array = [];
      this.each(function() {
        var container = $(this),
            loadingPic = '<div class="loader ' + opts.loadingPicClass + '">' +
                '<div class="pic"><img src="' + opts.url + '"/></div>' +
                '<div class="text">数据正在加载中，请稍候...</div>' +
                '</div>',
            overLay = '<div class="' + opts.overLayClass + '"></div>',
            loader = $(overLay).append(loadingPic);
        container.attr('origPos', container.css('position'));
        container.css('position', 'relative');
        var o = loader.appendTo(container);
        array.push(o);
      });
      return array;
    }
  });
  $.fn.loading.defaults = {
    url: 'image/loading.gif',
    overLayClass: 'loader-overlayer',
    loadingPicClass: 'normal'
  };
})(jQuery);
// alert
window.alert = function(message, runDespiteClose, fnc) {
  fnc = typeof runDespiteClose === 'function'?runDespiteClose:fnc;
  runDespiteClose = typeof runDespiteClose === 'boolean'?runDespiteClose: false;
  $('.m-message').remove();
  if (message && $.trim(message)) {
    var msg_div = $(
            '<div class="m-message">' + '<div class="m-message-head">'
                    + '<span class="pull-left">提示</span>'
                    + '<button type="button" class="close">×</button>'
                    + '</div>' + '<div class="m-message-body">' + message
                    + '</div>' + '</div>')

    .appendTo('body').center().show(100,function(){runDespiteClose && fnc && (setTimeout(fnc, 3000))}).delay(3000).queue(
            function() {
              $(this).remove();
              !runDespiteClose && fnc && fnc();
            });
    $('.close', msg_div).bind('click', function() {
      msg_div.remove();
      fnc?fnc():false;
    });
  }
};
// confirm
window.confirm = function(title, message, fn, modal) {
  $('.m-message').remove();
  var msg_div, backdrop = null, a, b, c, d;
  if (arguments[0] && typeof arguments[0] === 'string') {
    if (arguments[1] && typeof arguments[1] === 'string') {
      b = arguments[1];
      a = arguments[0];
      c = (arguments[2] && typeof arguments[2] === 'function') ? arguments[2]
              : null;
      d = (arguments[3] && typeof arguments[3] === 'boolean');
    } else {
      b = arguments[0];
      a = '提示';
      c = (arguments[1] && typeof arguments[1] === 'function') ? arguments[1]
              : null;
      d = (arguments[2] && typeof arguments[2] === 'boolean');
    }
    msg_div = $('<div class="m-message">' + '<div class="m-message-head">'
            + '<span class="pull-left">' + a + '</span>'
            + '<button type="button" class="close">×</button>' + '</div>'
            + '<div class="m-message-body">' + b + '		</div>'
            + '<div class="m-message-foot">'
            + '<input type="button" class="button yes" value="确定"/>'
            + '<input type="button" class="button cancel" value="取消"/>'
            + '</div>' + '</div>');
    if (d) {
      backdrop = $('<div class="overlayer"></div>').appendTo('body');
    }
    msg_div.appendTo('body').center().show(100);
    $('.close,.cancel', msg_div).bind('click', function() {
      backdrop && backdrop.remove();
      msg_div.remove();
    });
    $('.m-message-foot .yes', msg_div).bind('click', function() {
      c && c.call(this);
      backdrop && backdrop.remove();
      msg_div.remove();
    });
  }
};

$.ajaxSetup({
  type: "POST",
  global: true,
  //cache: false,
  dataType: "json",
  statusCode:{
    404: function(){
      alert('无法找到请求页面');
    }
  },
  error: function(XMLHttpRequest, textStatus, errorThrown) {
    if('parsererror' == textStatus){
      alert('返回数据格式不正确');
    }else if('timeout' == textStatus){
      alert('连接已超时');
    }else if('abort' == textStatus){
      alert('请求已取消');
    }else if('error'){
      alert('小二好像出错了，建议您再试一遍');
    }
  }
});
$(document).ajaxSend(function(e, jqXHR, opts) {
  var overLayTarget = $(opts.overLayTarget), config = opts.loadingConfig, overLayer;
  if(config && config.overLay !== undefined && !config.overLay){
    return;
  }
  if (overLayTarget.length == 0) {
    overLayTarget = $(document.body);
  }
  
  overLayer = overLayTarget.loading(config);
  opts.overLayer = overLayer;
});
$(document).ajaxComplete(function(e, jqXHR, opts) {
  if (opts.overLayer) {
    $.each(opts.overLayer, function(i, item) {
      var p = $(item).parent(), q = p.attr('origPos');
      if(q){
        p.css('position', q);
      }
      item.remove();
    });
  }
});
function debounce(func, wait, immediate) {
  var timeout;
  return function() {
      var context = this, args = arguments;
      var later = function() {
          timeout = null;
          if (!immediate) func.apply(context, args);
      };
      var callNow = immediate && !timeout;
      clearTimeout(timeout);
      timeout = setTimeout(later, wait);
      if (callNow) func.apply(context, args);
  };
}
function adjustCenter(){
  $('.m-message').each(function(i, item){
    var p = $(item).css('position');
    if(p === 'absolute'){
      var top = ($(window).height() - $(item).height()) / 2;
      var left = ($(window).width() - $(item).width()) / 2;
      if($(document.body).css('position') === 'relative'){
        var pos = $(document.body).offset();
        top = top - pos.top;
        left = left - pos.left;
      }
      
      top += $(window).scrollTop();
      left += $(window).scrollLeft();
      
      $(item).css({/* position:'absolute', */
        margin: 0,
        top: (top > 0 ? top : 0) + 'px',
        left: (left > 0 ? left : 0) + 'px'
      });
    }
  });
}
$.fn.shiftSelectable = function() {
  var lastChecked;

  $(document.body).delegate(this.selector, 'click', {selector: this.selector}, function(evt) {
      if(!lastChecked) {
          lastChecked = this;
          return;
      }
      
      var $boxes = $(evt.data.selector) ;
      if(evt.shiftKey) {
          var start = $boxes.index(this),
              end = $boxes.index(lastChecked);
          $boxes.slice(Math.min(start, end), Math.max(start, end) + 1)
          .each(function(i, box){
            box.checked = lastChecked.checked;
            $(box).trigger('change');
          });
      }

      lastChecked = this;
  });
};
$(function() {
  $(document.body).tooltip({selector: '[data-toggle=tooltip]'});
  // 弹出式操作菜单
  $('body').delegate('.oper-area', 'mouseenter', function() {
    $('.dropdown-menu', this).show();
  }).delegate('.oper-area', 'mouseout', function() {
    $('.dropdown-menu', this).hide();
  });
//  $(window).bind('scroll', debounce(adjustCenter, 100));
  // 下拉列表
  // 下拉列表点击弹出列表项和失去焦点事件
  $('body').delegate('div.dropdown', 'mouseover', function(event) {
      var hd = $('.dropdown-hd', this);
      if(!hd.hasClass('dropdown-hd-active')){
        this.focus();
        hd.addClass('dropdown-hd-active');
        $('.dropdown-list', this).show();
      }
  }).delegate('div.dropdown', 'mouseleave', function() {
    $('.dropdown-hd', this).removeClass('dropdown-hd-active');
    $('.dropdown-list', this).hide();
  });
  // 下拉列表项事件
  $('body').delegate('div.dropdown .dropdown-item', 'mouseover', function() {
    $(this).addClass('dropdown-itemover');
  }).delegate('div.dropdown .dropdown-item', 'mouseout', function() {
    $(this).removeClass('dropdown-itemover');
  }).delegate(
          'div.dropdown .dropdown-item',
          'mousedown',
          function() {
            $(this).parent().children().removeClass('dropdown-itemselected');
            $(this).addClass('dropdown-itemselected').parent('.dropdown-list')
                    .prev('.dropdown-hd').children('.dropdown-text').text(
                            $('span', this).text());
            // 隐藏域赋值
            var o = $(this).parent('ul').parent('div').children(
                    'input[type=hidden]'), n = $('span[value]', this);
            if (o.val() != n.attr('value')) {
              o.val(n.attr('value'));
              o.trigger('click', arguments);
            }
            $(this).parent().parent().trigger('blur');
          });
  // 切换账户
  $('input[type=hidden][name=selectedNick]').on('click', function() {
    var me = this;
    updateLast(this.value, function() {
      $(me).trigger('customClick');
    });
  });
  // 列表全选框
  $('body').delegate('#checkall', 'change', function() {
    var a = this.checked;
    $(":checkbox[name='rowid']:visible").each(function() {
      var changeFlag = false;
      if(this.checked != a){
        changeFlag = true;
      }
      this.checked = a;
      if(changeFlag){
        $(this).trigger('change');
      }
    });
  });
    $("#campaign-manage").on("hidden.bs.modal", function () {
        location.replace(location);
    });
});
function updateLast(nick, callback) {
	callback && callback();
	
	/*
  $.ajax({
    url: 'accountTimeLog.action',
    type: 'post',
    data: {
      nick: nick
    },
    success: function() {
      callback && callback();
    }
  });
  */
	
}
function getURLBySize(url, size){
  if(url && typeof url == 'string'){
    if(typeof size == 'number' && size > 0){
      return url.replace(/.*\.(\w+)$/, function(){return arguments[1]?(arguments[0] + '_' + size + 'x' + size + '.jpg'):'';});
    }else{
      return url;
    }
  }
}
function getOrigURL(url){
  return (url||'').replace(/_\d+x\d+\.\w+/, '');
}
function toAdgroup() {
  var days = $('input[name=pastDays]').val() || 1, nick = $(
          'input[name=selectedNick]').val()
          || '';
  location.assign('item.action?pastDays=' + days + "&selectedNick="
          + encodeURIComponent(nick));
}
function toAutoCampaign(){
  var days = $('input[name=pastDays]').val() || 1, nick = $(
    'input[name=selectedNick]').val()
    || '';
  location.assign('toCampaigns.action?queryDay=' + days + "&nick="
    + encodeURIComponent(nick));
}
function toCampaign() {
  var days = $('input[name=pastDays]').val() || 1, nick = $(
          'input[name=selectedNick]').val()
          || '';
  location.assign('queryCampaigns.action?queryDay=' + days + "&nick="
          + encodeURIComponent(nick));
}
function toHome() {
  var days = $('input[name=pastDays]').val() || 1, nick = $(
          'input[name=selectedNick]').val()
          || '';
  location.assign('home.action?queryDay=' + days + "&nick="
          + encodeURIComponent(nick));
}
function toManage() {
  var days = $('input[name=pastDays]').val() || 1, nick = $(
        'input[name=selectedNick]').val()
        || '';
//  location.assign('gotoCampaignManage.action?queryDay=' + days + "&nick="
//          + encodeURIComponent(nick));
    $("#campaign-manage").modal({
        remote: "gotoCampaignManage.action?queryDay=" + days + "&nick=" + encodeURIComponent(nick),
        show: true
    });
}

function toEditAll() {
  var days = $('input[name=pastDays]').val() || 1, nick = $(
          'input[name=selectedNick]').val()
          || '';
  location.assign('editAll.action?queryDay=' + days + "&nick="
          + encodeURIComponent(nick));
}

function toAttention() {
  var days = $('input[name=pastDays]').val() || 1, nick = $(
          'input[name=selectedNick]').val()
          || '';
  hRequestOpenNew('toAttention.action', {
    n: 'queryDay',
    v: days
  }, {
    n: 'nick',
    v: nick
  });
}
function toKeywordDuplicate() {
  var days = $('input[name=pastDays]').val() || 1, nick = $(
          'input[name=selectedNick]').val()
          || '';
  location.assign('toKeywordDuplicate.action?pastDays=' + days
          + "&selectedNick=" + encodeURIComponent(nick));
}
function toLog(type, id){
  hRequestOpenNew('toLog.action', {
    n: 'type', v: type
  },{
    n: 'nick', v: $('input[name=selectedNick]').val()||''
  },{
    n: 'id', v: id
  });
}

//手工加词链接拼装
function toCAK(type, id){
  hRequestOpenNew('customAddKeyword.action', {
    n: 'nickname', v: $('input[name=selectedNick]').val()||''
  },{
    n: 'adgroupid', v: id
  });
}

//已删词管理链接拼装
function toKDI(type, id){
	  hRequestOpenNew('getKeywordDelInfo.action', {
	    n: 'nick', v: $('input[name=selectedNick]').val()||''
	  },{
	    n: 'adgroupId', v: id
	  });
	}

// 选中后排序规则函数
function checkedSort(a, b) {
  var ckA = $(a).find('td:first input[type=checkbox]'),
      ckB = $(b).find('td:first input[type=checkbox]');
  if(ckA.length > 0 && ckB.length > 0){
    var checkedA = $(a).find('td:first input[type=checkbox]')[0].checked;
    var checkedB = $(b).find('td:first input[type=checkbox]')[0].checked;
    if (checkedA == checkedB) {
      return 0;
    } else if (checkedA) {
      return -1;
    } else if (checkedB) { return 1; }
  }else{
    return 0;
  }
}
// 单列排序规则函数
function getSingleColSortRule(colIndex, txtFn) {
  return function(a, b) {
    var textA = (txtFn && Object.prototype.toString.call(txtFn) == "[object Function]"?txtFn.call($(a).children('td').eq(colIndex)):$(a).children('td').eq(colIndex).text());
    var textB = (txtFn && Object.prototype.toString.call(txtFn) == "[object Function]"?txtFn.call($(b).children('td').eq(colIndex)):$(b).children('td').eq(colIndex).text());
    if (!isNaN(textA) && !isNaN(textB)) {
      return parseFloat(textB) - parseFloat(textA);
    } else {
      return (textB + '').localeCompare(textA + '');
    }
  };
}
/**
 * 排序控制逻辑
 * 
 * @param indexes
 *          已经参与排序的列
 * @param index
 *          当前点击的列
 * @param ctrl
 *          是否按下ctrl
 * @returns {准备排序的列，根据准备排序列生成的排序方法}
 */
function columnSortOrder(indexes, index, orders, order, ctrl){
  var firstExists = false;
  var z = ("[object Object]" === Object.prototype.toString.call(index)?index.i:"[object Number]" === Object.prototype.toString.call(index)?index:-1);
  if (ctrl) {
    var exists = false, i = 0, l = indexes.length;
    for (; i < l; i++) {
      if (indexes[i] == z) {
        exists = true;
        break;
      }
    }
    if (exists) {
      indexes.splice(i, 1);
      orders.splice(i + i, 1);
    }
    if (orders.length == 0) {
      firstExists = true;
    }
    indexes.push(z);
    orders.push(order);
  } else {
    firstExists = true;
    indexes = [z];
    orders = [order];
  }
  if (firstExists) {
    orders.unshift('desc');
  }
  var args = [indexes, getSingleColSortRule];
  if("[object Object]" === Object.prototype.toString.call(index)){
    args.push(index.fn);
  }
  var conditions = createSortConditions.apply(this, args);
  return {
    currentColumns: indexes,
    currentConditions: conditions,
    currentOrders: orders
  };
}
// 根据可排序的列生成排序函数
function createSortConditions(array, fn, txtFn) {
  var o = [];
  $.each(array, function(i, item) {
    o.push(fn(item, txtFn));
  });
  o.unshift(checkedSort);
  return o;
}
function hRequestOpenNew(url) {
  var i = 1, j = arguments.length, p = [];
  for (; i < j; i++) {
    var item = arguments[i];
    p.push(item.n + '=' + item.v);
  }
  window.open(encodeURI(p.length > 0 ? (url + '?' + p.join('&')) : url));
}

function hRequest(url) {
  var i = 1, j = arguments.length, p = [];
  for (; i < j; i++) {
    var item = arguments[i];
    p.push(item.n + '=' + item.v);
  }
  location.assign(encodeURI(p.length > 0 ? (url + '?' + p.join('&')) : url));
}

function shopDataSynch() {
  confirm('您在直通车后台进行的增删改操作，可以通过全店快速下载同步到本软件。<br/>由于淘宝接口限制，本功能暂时每天只允许使用三次，敬请谅解。<br/>同步耗时可能会比较长，点击确定开始后请耐心等待。', function(text) {
    if ($(this).hasClass('yes')) {
      $.ajax({
        url: 'synSingleShopStatus.action',
        data: {
          nick: $('input[name=selectedNick]').val()
        },
        success: function(data, status) {
          alert(data.message);
        }
      });
    }
  });
}
function synMoreStart(o) {
  var a = document.all.optionsRadios1, b = document.all.optionsRadios2, cancel = $(
          o).next();
  if (!a.checked) {
    alert('基础数据同步需要选中');
    return;
  }
  $.ajax({
    url: 'synSingleShopStatus.action',
    data: {
      nick: $('input[name=selectedNick]').val() || '',
      pastDays: $('synDays').val() || 1,
      reportFlag: b.checked ? 1 : 0
    },
    success: function(data, status) {
      if (status == 'success') {
        alert(data.message);
      }
    },
    complete: function() {
      cancel.trigger('click');
    }
  });
}

function setCookie(c_name, value, exdays) {
  var exdate = new Date();
  exdate.setDate(exdate.getDate() + exdays);
  var c_value = escape(value)
          + ((exdays == null) ? "" : "; expires=" + exdate.toUTCString());
  document.cookie = c_name + "=" + c_value;
}

function getCookie(c_name) {
  var c_value = document.cookie;
  var c_start = c_value.indexOf(" " + c_name + "=");
  if (c_start == -1) {
    c_start = c_value.indexOf(c_name + "=");
  }
  if (c_start == -1) {
    c_value = null;
  } else {
    c_start = c_value.indexOf("=", c_start) + 1;
    var c_end = c_value.indexOf(";", c_start);
    if (c_end == -1) {
      c_end = c_value.length;
    }
    c_value = unescape(c_value.substring(c_start, c_end));
  }
  return c_value;
}

function setColor(){
	var cid = getCookie('color');
	var chr = $("#"+cid).attr("href");            
    $("#mc").attr("href",chr);
}
setColor();

function getDateBefore(n){
	var d=new Date();
	
	d.setDate(d.getDate()-n);
	
	var year=d.getFullYear();
	var month=d.getMonth()+1;
	if(month<10){
		month="0"+month;
	}
	var day=d.getDate();
	if(day<10){
		day="0"+day;
	}
	return year+"-"+month+"-"+day;
}
function fixedhead_generate(selector, scrollHeight){
  return function fixedTHead(){
    var h_num = $(window).scrollTop(),                  //滚动高度
      head = $(selector).find(' thead').not('.headholder'); //实际表头
    
    if(h_num >= scrollHeight && head.css('position') != 'fixed'){
        var clonehead = $('.headholder'),               //表头替代
        trs = head.children('tr'),                      //实际表头内行
        ths = head.children('tr').children('th'),       //实际表头内列
        width2 = head.width();
      
        //加载替代
        if(clonehead.length == 0){
        clonehead = head.clone().addClass('headholder');
          head.after(clonehead);
        }
        
        //clonehead.show();
        
        head.attr('style')?head.attr('origstyle', head.attr('style')).removeAttr('style'):false;
        head.attr('style', 'position:fixed;z-index:5;width:' + width2 + 'px;top:0;');
        
        trs.attr('style', 'display:block;');
        ths.each(function(i, item){
          var display = $(item).css('display');
          var th = clonehead.find('th').eq(i),
          a = th.css('paddingLeft').replace('px', ''),
          b = th.css('paddingRight').replace('px', ''),
          c = th.css('borderLeftWidth').replace('px', ''),
          d = th.css('borderRightWidth').replace('px', ''),
          width = th.width() 
          + (isNaN(a)?0:parseFloat(a))
          + (isNaN(b)?0:parseFloat(b)) 
          + (isNaN(c)?0:parseFloat(c)) 
          + (isNaN(d)?0:parseFloat(d));
          $(item).removeClass('hide').css('display', display);
          $(item).attr('style')?$(item).attr('origstyle', $(item).attr('style')).removeAttr('style'):false;
          $(item).css('width', width);
          display == 'none'?($(item).css('display', 'none')):false;
          th.css('width', width);
          
      });
    }else if(h_num < 612 && head.css('position') == 'fixed'){
      var clonehead = $(selector).find('.headholder'),
      trs = head.children('tr'),
      ths = head.children('tr').children('th'),
      width2 = head.width();
      //取消
      clonehead.remove();
      
      head.removeAttr('style');
      trs.removeAttr('style');
      
      head.attr('origstyle')?head.attr('style', head.attr('origstyle')).removeAttr('origstyle'):false;
      ths.each(function(i, item){
        $(item).attr('origstyle')?$(item).attr('style', $(item).attr('origstyle')).removeAttr('origstyle'):false;
      });
    }
  };
}
//获取url参数
var queryString=function(key){
    return (document.location.search.match(new RegExp("(?:^\\?|&)"+key+"=(.*?)(?=&|$)"))||['',null])[1];
}
