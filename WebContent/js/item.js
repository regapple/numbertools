function reload() {
  $('#mainform').submit();
}
function toAIOptimize(nick, campaignid, adgroudid, days) {
  var auto = this.event.srcElement.getAttribute('auto');
  if(auto == '1'){
	  confirm('宝贝已经开启自动优化功能，滴滴会对宝贝进行自动调整，您确定要手工干预吗',function(){
      hRequest('toOptimize.action', {
        n: 'adgroupId',
        v: adgroudid
      }, {
        n: 'selectedNick',
        v: nick
      }, {
        n: 'campaignId',
        v: campaignid
      }, {
        n: 'pastDays',
        v: days
      });
	  });
  }else{
    hRequest('toOptimize.action', {
      n: 'adgroupId',
      v: adgroudid
    }, {
      n: 'selectedNick',
      v: nick
    }, {
      n: 'campaignId',
      v: campaignid
    }, {
      n: 'pastDays',
      v: days
    });
  }
}
function toQuickAddKeyword(nick, campaignId, adgroudId, days) {
  var auto = this.event.srcElement.getAttribute('auto');
  if(auto == '1'){
    confirm('宝贝已经开启自动优化功能，滴滴会对宝贝进行自动调整，您确定要手工干预吗',function(){
      hRequest('quickAddKeyword.action', {
        n: 'nick',
        v: nick
      }, {
        n: 'campaignId',
        v: campaignId
      }, {
        n: 'adgroupId',
        v: adgroudId
      }, {
          n: 'pastDays',
          v: days||'1'
      });
    });
  }else{
    hRequest('quickAddKeyword.action', {
      n: 'nick',
      v: nick
    }, {
      n: 'campaignId',
      v: campaignId
    }, {
      n: 'adgroupId',
      v: adgroudId
    }, {
        n: 'pastDays',
        v: days||'1'
    });
  }
}
function showDfp(id, price) {
    $(".pop_input_div").hide();
    var a = $('#dfp_' + id);
    $('#newdfp').val(price);
    $("#dfpId").attr("value", id);
    $("#dfp-div").css({
        'top': $(a).offset().top - 4,
        'left': $(a).offset().left,
        'position': 'absolute'
    }).show();
    $('#newdfp').focus();
}
function showBidLimit(id, price, adgroupid) {
    $(".pop_input_div").hide();
    var a = $('#dfp_' + id);
    $('#limit-price').val(price);
    $('#limit-adgroup').val(adgroupid);
    $("#bidLimt-div").css({
        'top': $(a).offset().top - 4,
        'left': $(a).offset().left,
        'position': 'absolute'
    }).show();
    $('#limit-price').focus();
}
function toggleAttention(o, id) {
  var type = 'adgroup', data = $(o).data(), isFollowed = data.isfollowed, flag = (0 === isFollowed);
  $.ajax({
    url: 'toggleAttention.action',
    data: {
      attentionType: type,
      targetId: id,
      flag: flag
    },
    success: function(data, status) {
      if ('success' == status) {
        if (data.success) {
          $(o).text(flag ? '取消关注' : '我要关注').data({
            isfollowed: flag ? 1 : 0
          });
        } else {
          alert((flag ? '添加关注' : '取消关注') + '失败');
        }
      }
    }
  });
}
function toOptimizeKeyword(nick, campaignId, adgroudId, days) {
  var auto = this.event.srcElement.getAttribute('auto');
  if(auto == '1'){
    confirm('宝贝已经开启自动优化功能，滴滴会对宝贝进行自动调整，您确定要手工干预吗',function(){
      hRequestOpenNew("itemSingle.action", {
        n: 'selectedNick',
        v: nick
      }, {
        n: 'campaignId',
        v: campaignId
      }, {
        n: 'adgroupId',
        v: adgroudId
      }, {
        n: 'pastDays',
        v: days
      });
    });
  }else{
    hRequestOpenNew("itemSingle.action", {
      n: 'selectedNick',
      v: nick
    }, {
      n: 'campaignId',
      v: campaignId
    }, {
      n: 'adgroupId',
      v: adgroudId
    }, {
      n: 'pastDays',
      v: days
    });
  }
}
function modifyLimit() {
  var a = $("#limit-price").val(), adgroupid = $('#limit-adgroup').val();
  var s = /^\d+\.\d{1,2}$|^\d+$/;
  if (s.test(a)) {
    if(parseFloat(a) <= 0){
      alert('请输入大于0的数');
      return;
    }else if(parseFloat(a)<0.1){
   	 alert('关键词的最低价格不能低于0.1元');
     return;
    }else{
    	 confirm("提示", "亲，修改宝贝限价会将宝贝下所有高于【"+a+"】元的关键词出价调整为【"+a+"】元，确认修改吗？", function(status){
             if ($(this).hasClass('yes')) {
            	 $.ajax({
            	      url:'updateLimit.action',
            	      type:'POST',
            	      data:{
            	        adgroupId:adgroupid,
            	        price: a,
            	        nick:$('input[name=selectedNick]').val()
            	      },
            	      success:function(d,status){
            	        if(status=='success'){
            	          alert(d.msg,function(){
            	        	// 刷新页面
              	            hRequest("item.action", {
              	              n: 'selectedNick',
              	              v: $('input[name=selectedNick]').val()
              	            }, {
              	              n: 'pastDays',
              	              v: $('input[name=pastDays]').val()
              	            },{
              	              n: 'campaignId',
              	              v: $('input[name=campaignId]').val()
              	            });
            	          });
            	         // confirm("",d.msg,function(){});
            	          if (d.success) {
            	            
            	          }
            	        }
            	      }
            	    });
             }
             
    	 });
    }
  } else {
    alert('请输入正确的金额！');
  }
}

function modifyDfp() {
  var a = $("#newdfp").val();
  var s = /^\d+\.\d{1,2}$|^\d+$/;
  if (s.test(a)) {
    $("#defaultPrice").attr("value", a);
    $.ajax({
      url: 'updateItem.action',
      data: $("form").serialize(),
      success: function(data, status) {
        if ('success' == status) {
          alert(data.message);
          if (data.success) {
            // 刷新页面
            hRequest("item.action", {
              n: 'selectedNick',
              v: $('input[name=selectedNick]').val()
            }, {
              n: 'pastDays',
              v: $('input[name=pastDays]').val()
            },{
              n: 'campaignId',
              v: $('input[name=campaignId]').val()
            });
          }
        }
      }
    });
  } else {
    alert('请输入正确的金额！');
  }
}

function showCt() {
  //var e    = $.Event('show.bs.modal', { relatedTarget: e.relatedTarget});
  var relatedTarget = $(this), 
      id = relatedTarget.attr('data-id'), 
      nick = relatedTarget.attr('data-nickname'), 
      adgroupid = relatedTarget.attr('data-adgroupid'), 
      creative1Id = relatedTarget.attr('data-creative1id') || '', 
      creative2Id = relatedTarget.attr('data-creative2id') || '', 
      creative1Title = relatedTarget.attr('data-creative1title') || '', 
      creative2Title = relatedTarget.attr('data-creative2title') || '', 
      creative1Img = (relatedTarget.attr('data-creative1imgurl') || '').replace(/_sum\.jpg$/, ''), 
      creative2Img = (relatedTarget.attr('data-creative2imgurl') || '').replace(/_sum\.jpg$/, ''),
      addBtnStyle = creative2Id ? "none" : "block";
  $('#btnSave,.add-creative').removeAttr('data');
  var modalHtml = [], rowsHtml = [], imagesHtml = [];
  modalHtml.push('<input type="hidden" name="oldTitle1" value="{creative1Title}"/>');//creative1Title||''
  modalHtml.push('<input type="hidden" name="oldTitle2" value="{creative2Title}"/>');//creative2Title||''
  modalHtml.push('<input type="hidden" name="oldUrl1" value="{creative1Img}"/>');//(creative1Img.replace(/_sum\.jpg$/, '') || '')
  modalHtml.push('<input type="hidden" name="oldUrl2" value="{creative2Img}"/>');//(creative2Img.replace(/_sum\.jpg$/, '') || '')
  modalHtml.push('<form>');
  modalHtml.push('<input type="hidden" name="nick"  value="{nick}"/>');
  modalHtml.push('<input type="hidden" name="id"  value="{id}"/>');
  modalHtml.push('<input type="hidden" name="adgroupid"  value="{adgroupid}"/>');
  modalHtml.push('<input type="hidden" name="type1"  value=""/>');
  modalHtml.push('<input type="hidden" name="type2"  value=""/>');
  modalHtml.push('<input type="hidden" name="id1"  value="{creative1Id}"/>');//creative1Id||''
  modalHtml.push('<input type="hidden" name="id2"  value="{creative2Id}"/>');//creative2Id||''
  modalHtml.push("<table class='table table-strip table-bordered'><thead><tr><th style='min-width:40px;'></th><th style='min-width:161px;'>创意图片</th><th>创意标题</th></tr></thead><tbody>");
  modalHtml.push('{rows}');
  modalHtml.push('</table>');
  modalHtml.push("<a class='add-creative btn btn-large' href='javascript:;' style='width: 785px;display:{addBtnStyle};'>添加创意</a>");//(creative2Id ? "none" : "block")
  modalHtml.push('</form>');
  
  imagesHtml.push('<img class="creative-thumbnail" src="{url}"/>');//url   //data[0].itemImgs[i].url
  
  rowsHtml.push("<tr>");
  rowsHtml.push('<td>{creativeName}</td>');
  rowsHtml.push('<td class="creativeImg"><div style="position:relative">');
  rowsHtml.push('<div class="inner-minus" title="删除创意"><div class="inner-slider"></div></div>');
  rowsHtml.push('<img src="{creativeImg}"/>');//creative1Img.replace(/_sum\.jpg$/, '')
  rowsHtml.push('<input type="hidden" name="{creativeUrl}" value="{sizedCreativeImg}"/>');//creativeUrl[1 or 2]   //creative1Img.replace(/_sum\.jpg$/, '')
  rowsHtml.push('</div></td>');
  rowsHtml.push('<td class="creativeEditTd">');
  rowsHtml.push('<input type="text" name="{creativeTitleName}" value="{creativeTitleValue}"/><span>已有字符数:{charCount}</span>');//creativeTitleName[1 or 2]    //creativeTitleValue    //getCharLength(creative1Title)
  rowsHtml.push('<div class="options">{images}</div>');
  rowsHtml.push("</td>");
  rowsHtml.push("</tr>");
  
  var a = '#ct_' + id;
  $("#ctId").attr("value", id);
  // 异步获取可选图片
  $.ajax({
      url: 'getCreatives.action',
      cache: false,
      data: {
        adgroupid: adgroupid
      },
      success: function(data, status) {
        if (status == 'success') {
          //保存创意
          var imgs = (data[0] || [])['itemImgs'] || [], 
              images = [],//可选图
              rows = [];
          //初始化可选创意图片
          if(imgs && imgs.length > 0){
            imgs.each(function(item){
              images.push(imagesHtml.join('').parseTpl({url: getURLBySize(item.url, 100)}));
            });
          }
          images = images.join('');
          
          //初始化行
          if(creative1Id){
            var rowdata = {
                    creativeName: '创意一',
                    creativeImg: getURLBySize(creative1Img, 160),
                    sizedCreativeImg: creative1Img,//
                    creativeUrl: 'creativeUrl1',
                    creativeTitleName: 'creativeTitle1',
                    creativeTitleValue: creative1Title,
                    charCount: getCharLength(creative1Title) || 0,
                    images: images
            };
            rows.push(rowsHtml.join('').parseTpl(rowdata));
          }
          if(creative2Id){
            var rowdata = {
                    creativeName: '创意二',
                    creativeImg: getURLBySize(creative2Img, 160),
                    sizedCreativeImg: creative2Img,//
                    creativeUrl: 'creativeUrl2',
                    creativeTitleName: 'creativeTitle2',
                    creativeTitleValue: creative2Title,
                    charCount: getCharLength(creative2Title) || 0,
                    images: images
            };
            rows.push(rowsHtml.join('').parseTpl(rowdata));
          }
          
          //初始化modal
          var tab = modalHtml.join('').parseTpl({
            nick: nick,
            id: id,
            adgroupid: adgroupid,
            creative1Id: creative1Id,
            creative2Id: creative2Id,
            creative1Title: creative1Title,
            creative2Title: creative2Title,
            creative1Img: creative1Img,
            creative2Img: creative2Img,
            addBtnStyle: addBtnStyle,
            rows :rows.join('')
          });
          var div = $('#ct-div');
          var fragment = div.find('.m-content > .panel > .panel-body').empty().append(tab);
          $('#btnSave,.add-creative').attr('data', JSON.stringify(data));
          div.modal({backdrop: true, show: true});
      }
    }
  });
}
function addCreative(){
  var dataAtt = this.getAttribute('data');
  if(!dataAtt){return;}
  var data = JSON.parse(dataAtt);
  if(data && data.length > 0){

    var l = $(this).prev().children('tbody').children('tr').length, c = l + 1, title = $(
            this).prev().children('tbody').children('tr').children('').eq(0)
            .text();
    var newTitle = '';
    if (title == '创意一') {
      newTitle = '创意二';
    } else if (title == '创意二' || !title) {
      newTitle = '创意一';
    }
    if (c <= 2) {
      var creative2 = '';
      creative2 += "<tr>";
      creative2 += ('<td>' + newTitle + '</td>');
      creative2 += ('<td class="creativeImg"><div style="position:relative">');
      creative2 += '<div class="inner-minus" title="删除创意"><div class="inner-slider"></div></div>';
      creative2 += ('<img src="'
              + (getURLBySize(data[0].itemImgs[0].url,160) || 'data:image/png;base64,') + '"/>');
      creative2 += ('<input type="hidden" name="creativeUrl' + c + '" value="'
              + (data[0].itemImgs[0].url || '') + '"/>');
      creative2 += ('</div></td>');

      creative2 += ('<td class="creativeEditTd"><input type="text" name="creativeTitle'
              + c + '" value=""/><span>已有字符数:0</span>');
      creative2 += '<div class="options">';
      for ( var i = 0; i < data[0].itemImgs.length; i++) {
        creative2 += ('<img class="creative-thumbnail selected" src="'
                + getURLBySize(data[0].itemImgs[i].url,100) + '"/>');
      }
      creative2 += '</div>';
      creative2 += "</td>";
      creative2 += "</tr>";
      if (title == '创意一' || !title) {
        $(this).prev('table').append(creative2);
      } else if (title == '创意二') {
        $(this).prev('table').prepend(creative2);
      }

      // 处理参数

      if ($('#ct-div input[type=hidden][name=id' + (parseInt(c)) + ']').val()) {
        $('#ct-div input[type=hidden][name=type' + (parseInt(c)) + ']').val(
                'update');
      } else {
        $('#ct-div input[type=hidden][name=type' + (parseInt(c)) + ']')
                .val('new');
      }

      if (c == 2) {
        $(this).hide();
      }
    }
  }
}
function validateCreative(){
  var dataAtt = this.getAttribute('data');
  if(!dataAtt){return;}
  var data = JSON.parse(dataAtt);
  if(data && data.length > 0){

// 验证图片地址
//       var u1 = $.trim($('#ct-div input[name=creativeUrl1]').val()), u2 = $.trim($(
//               '#ct-div input[name=creativeUrl2]').val()), uArray = [], preFix = /http:\/\/img\d*\.taobaocdn\.com/, r1 = false, r2 = false;
//       $.each(data[0].itemImgs, function(i, item) {
//         uArray.push(item.url);
//       });
//       for ( var z = 0; z < uArray.length; z++) {
//         if ((u1.replace(preFix, '') == uArray[z].replace(preFix, '')) || !u1) {
//           r1 = true;
//         }
//         if ((u2.replace(preFix, '') == uArray[z].replace(preFix, '')) || !u2) {
//           r2 = true;
//         }
//       }
//       if (!r1) {
//         alert('创意一图片不是推广组的图片');
//         return;
//       }
//       if (!r2) {
//         alert('创意二图片不是推广组的图片');
//         return;
//       }
     // 验证标题
     if (validateTitle($('#ct-div input[name=creativeTitle1]')[0])
             && validateTitle($('#ct-div input[name=creativeTitle2]')[0])) {
       // 判断是否修改
       if (($('#ct-div input[name=creativeTitle1]').length > 0)
               && ($('#ct-div input[name=creativeUrl1]').length > 0)
               && (($('#ct-div input[name=oldTitle1]').val() && $(
                       '#ct-div input[name=oldTitle1]').val() != $.trim($(
                       '#ct-div input[name=creativeTitle1]').val())) || ($(
                       '#ct-div input[name=oldUrl1]').val() && $(
                       '#ct-div input[name=oldUrl1]').val() != $.trim($(
                       '#ct-div input[name=creativeUrl1]').val())))) {
         $('#ct-div input[name=type1]').val('update');
       }
       if (($('#ct-div input[name=creativeTitle2]').length > 0)
               && ($('#ct-div input[name=creativeUrl2]').length > 0)
               && (($('#ct-div input[name=oldTitle2]').val() && $(
                       '#ct-div input[name=oldTitle2]').val() != $.trim($(
                       '#ct-div input[name=creativeTitle2]').val())) || ($(
                       '#ct-div input[name=oldUrl2]').val() && $(
                       '#ct-div input[name=oldUrl2]').val() != $.trim($(
                       '#ct-div input[name=creativeUrl2]').val())))) {
         $('#ct-div input[name=type2]').val('update');
       }
       var params = $('#ct-div form').serialize();
       editCreative(params);
     } else {
       return;
     }
  }
}

function delCreative() {
  $(this).parent().parent().parent().parent().parent().parent().next().css(
          'display', 'block');
  $(this).parent().parent().parent().parent().remove();
  // 处理参数
  var name = $(this).parent().next().next().attr('name'), c = name
          .charAt(name.length - 1);
  // 存在ID才删除
  if ($('#ct-div input[type=hidden][name=id' + c + ']').val()) {
    $('#ct-div input[type=hidden][name=type' + c + ']').val('del');
  } else {
    $('#ct-div input[type=hidden][name=type' + c + ']').val('');
  }
}
function editCreative(param) {
  var editO = $.parseJSON(param.replace(/&$|$/, '"}').replace(/^|^&/, '{"')
          .replace(/=/g, '":"').replace(/&/g, '","'));
  if (editO.type1 || editO.type2) {
    $.ajax({
      url: 'operateCreatives.action',
      data: param,
      success: function(data, status) {
        if (status == 'success') {
          if(data){
            if(data.success){
              $('#ct-div').modal('hide').find('.modal-body').empty();
              alert('亲，创意修改成功了', function() {
                if (data && data.success) {
                  // 刷新
                  hRequest('item.action', {
                    n: 'selectedNick',
                    v: $('input[name=selectedNick]').val()
                  }, {
                    n: 'pastDays',
                    v: $('input[name=pastDays]').val()
                  }, {
                    n: 'campaignId',
                    v: $('input[name=campaignId]').val()
                  });
                }
              });
            }else{
              alert(data.message?data.message:'亲，修改创意操作失败了');
            }
          }
        }
      }
    });
  }
}
function showModal() {
  $('.modal-backdrop').removeClass('hide');
  $("#ct-div").removeClass('hide');
}
function hideModal() {
  $('.modal-backdrop').addClass('hide');
  $("#ct-div").addClass('hide');
}
function getCharLength(str) {
  if (!str) { return 0; }
  var hanReg = /[\u4e00-\u9fa5]/, charLength = 0, hanLength = 0;
  for ( var i = 0; i < str.length; i++) {
    if (hanReg.test(str[i])) {
      hanLength += 2;
    } else {
      charLength++;
    }
  }
  return charLength + hanLength;
}
function validateTitle(o) {
  if (!o) { return true; }
  var a = o.value;
  if (a.replace(/^\s*|\s*$/, '') == '') {
    alert('推广标题不能为空！');
    this.focus();
    return false;
  }
  var s1 = /\w/g;
  var s2 = /[\u4e00-\u9fa5]/g;
  var b1 = 0;
  var b2 = 0;
  while (s1.exec(a)) {
    b1++;
  }
  while (s2.exec(a)) {
    b2++;
  }
  if (b1 + b2 * 2 > 40) {
    alert('不能超过20个汉字！');
    this.focus();
    return false
  } else {
    return true;
  }
}
function changePic() {
  var origURL = getOrigURL($(this).attr('src'));
  $(this).parent().parent().prev().find('img').attr('src', getURLBySize(origURL, 160));
  $(this).parent().parent().prev().find('img').next('input').val(
          origURL);
}
function pauseCreative() {
  var a = $(":checkbox[name='rowid']:checked");
  if (a.length > 0) {
    confirm("您确定要暂停推广这些宝贝吗？", function(text) {
      if ($(this).hasClass('yes')) {
        $.ajax({
          url: 'updateItem.action?onlineStatus=offline',
          data: $('form').serialize(),
          success: function(data, status) {
            if ('success' == status) {
              alert(data.message, function() {
                // reload
                if (data.success) {
                    reload();
                }
              });
            }
          }
        });
      }
    });
  } else {
    alert("请至少选择一条记录！");
  }
}

function applyMarket() {
  var a = $(":checkbox[name='rowid']:checked");
  if (a.length > 0) {
    confirm("您确定要参与推广这些宝贝吗？", function(text) {
      if ($(this).hasClass('yes')) {
        $.ajax({
          url: 'updateItem.action?onlineStatus=online',
          data: $('form').serialize(),
          success: function(data, status) {
            if ('success' == status) {
              alert(data.message, function() {
                // reload
                if (data.success) {
                  reload();
                }
              });
            }
          }
        });
      }
    });
  } else {
    alert("请至少选择一条记录！");
  }
}
function delAdgroup() {
  var a = $(":checkbox[name='rowid']:checked");
  if (a.length > 0) {
    confirm("你确定要删除这些宝贝吗，直通车后台会同步删除？", function() {
      if($(this).hasClass('yes')){
        $("form").attr("action", "deleteItem.action");
        $("form").submit();
      }
    });
  } else {
    alert("请至少选择一条记录！");
  }
}
function applyNewAdgroup() {
  var campaign = $('input[type=hidden][name=campaignId]').val();
  var nick = $('input[type=hidden][name=selectedNick]').val();
  var campaignName = $('input[type=hidden][name=campaignId]').next(
          '.dropdown-hd').children('.dropdown-text').text();
  if (campaign && campaign != 'all') {
    hRequest("toPromoteItem.action", {
      n: 'nick',
      v: nick
    }, {
      n: 'campaignId',
      v: campaign
    }, {
      n: 'campaignName',
      v: campaignName
    });
  } else {
    alert('请选择推广计划');
  }
}
function toggleAutoAi(n, adgroupids, flag){
  $.ajax({
    url: 'setAutoOptimizeStatus.action',
    data: {nick: n, opid: adgroupids.join(','), status: flag},
    success: function(data, status){
      if('success' == status){
        alert(data.message);
        if(data.success){
          adgroupids.each(function(item){
              var innerHtml ='未开启全自动 <a id="startAutoAI_' + item + '" class="item_btn item_btn_hidden item_btn_startao" href="javascript:;">开启</a>';
              if (flag==1) {
                innerHtml = '已开启全自动 <a id="stopAutoAI_' + item + '" class="item_btn item_btn_hidden item_btn_stopao" href="javascript:;">关闭</a>';
              }
            $('#optimizeAutoAI_' + item).html(innerHtml);
          });
        }
      }
    }
  });
}

function toggleAutoAiHandler(e){
    var theone = e.id || this.id;
	var flag = theone.indexOf('startAutoAI')>-1?'1':'0';
	
    var nick = $("input[name='selectedNick']").val();
    var rows = $('input[name=rowid]:checked');
    if(rows.length > 0 ){
        var msg = '关闭全自动，滴滴将不再自动对宝贝增词，删词,关键字调价等，您确定要关闭吗？';
        if (flag==1) {
            msg = '开启全自动，滴滴会自动对宝贝增词，删词,关键字调价等，您确定要开启吗';
        }
        confirm(msg,function(){
            var adgroupids = [];
            rows.each(function(i, item){
              adgroupids.push(item.getAttribute('data-adgroupid'));
            });
            toggleAutoAi(nick, adgroupids, flag);
        });
    } else {
        alert('请选择操作宝贝');
    }
}

function setKeyAdgroup(nick, id, type, campaignId){
  function _(){
    $.ajax({
      url: 'setAdgroupType.action',
      data:{
    	nick: nick,
        adgroupid: id,
        adgroupType: type
      },
      success: function(d,s,x){
        if(s == 'success'){
          if(d.success){
            hRequest("item.action", {
              n: 'selectedNick',
              v: $('input[name=selectedNick]').val()
            }, {
              n: 'pastDays',
              v: $('input[name=pastDays]').val()
            },{
              n: 'campaignId',
              v: $('input[name=campaignId]').val()
            });
          }else{
            alert(d.msg);
          }
        }
      }
    });
  }
  if('' == type){
    confirm('亲，确定要取消重点宝贝吗？',function(){
      if($(this).hasClass('yes')){
        _(id, '');
      }
    });
  }else{
    if(itemCountValidate(campaignId)){
      _(id, type);
    }
  }
}
function itemCountValidate(campaignId) {
  var array = [];
  for(var i = 0, j = replication.length; i < j; i ++){
    var item = replication[i];
    if(item.campaignId == campaignId && 'key' == item.adgroupType){
      array.push(item);
    }
  }
  if(array.length >= keyAdgroupLimit){
    alert('亲，可设置的重点宝贝数量已满');
    return false;
  }
  return true;
}
