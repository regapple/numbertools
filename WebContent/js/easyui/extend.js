/**
 * easyui覆盖 by guoj
 */

/* 覆盖window */
$.extend($.fn.window.defaults, {
	collapsible : false,
	minimizable : false,
	maximizable : false
});

$(function() {
	/* 覆盖combobox样式 */
	$(".combo:not(.datebox)> span > .textbox-icon").addClass("fa fa-sort-down");
	$(".datebox > span > .textbox-icon").addClass("fa fa-calendar");
});

/* 重写combobox */
$.extend($.fn.combobox.defaults, {
	panelHeight : 'auto',
	panelMaxHeight : 200,
	editable : false
});

/* 重写datagrid填充 */
var gridView = $.extend({}, $.fn.datagrid.defaults.view, {
	onAfterRender : function(target) {
		$.fn.datagrid.defaults.view.onAfterRender.call(this, target);
		var opts = $(target).datagrid('options');
		var vc = $(target).datagrid('getPanel').children('div.datagrid-view');
		vc.children('div.datagrid-empty').remove();
		if (!$(target).datagrid('getRows').length) {
			var d = $('<div class="datagrid-empty"></div>').html(
					opts.emptyMsg || 'o(╯□╰)o  没有相关记录').appendTo(vc);
			d.css({
				position : 'absolute',
				left : 0,
				top : 100,
				width : '100%',
				textAlign : 'center',
				color : '#666'
			});
		}
	}
});

/* 合并指定列 */
function mergeCellsByField(tableID, colList) {
	var ColArray = colList.split(",");
	var tTable = $("#" + tableID);
	var TableRowCnts = tTable.datagrid("getRows").length;
	var tmpA;
	var tmpB;
	var PerTxt = "";
	var CurTxt = "";
	for ( var j = ColArray.length - 1; j >= 0; j--) {
		PerTxt = "";
		tmpA = 1;
		tmpB = 0;
		for ( var i = 0; i <= TableRowCnts; i++) {
			if (i == TableRowCnts) {
				CurTxt = "";
			} else {
				CurTxt = tTable.datagrid("getRows")[i][ColArray[j]];
			}
			if (PerTxt == CurTxt) {
				tmpA += 1;
			} else {
				tmpB += tmpA;
				tTable.datagrid("mergeCells", {
					index : i - tmpA,
					field : ColArray[j],// 合并字段
					rowspan : tmpA,
					colspan : null
				});
				tTable.datagrid("mergeCells", { // 根据ColArray[j]进行合并
					index : i - tmpA,
					field : "Ideparture",
					rowspan : tmpA,
					colspan : null
				});
				tmpA = 1;
			}
			PerTxt = CurTxt;
		}
	}
}