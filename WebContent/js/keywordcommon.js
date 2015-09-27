/*
 * 设置修改词的价格后的颜色
 */
var todayString =dateFormat(new Date());
function getDataColor(value,item){
	var lastOperDate=item.lastOperDate;
	
	var ret=value;
	if(lastOperDate!=null&&lastOperDate!=""&&lastOperDate==todayString){
		var direction=item.direction;
		if(direction==1){
			ret="<font  color='red' title='今日已上调价格'>"+value+"</font>";
		}else if(direction==-1){
			ret="<font  color='green' title='今日已下调价格'>"+value+"</font>";
		}
	}
	
	return ret;	
}

function showChart(id,title){
	
	rendererChart('keywordAction.action?method=keywordDailyReportData',getNick(),id,"关键词【 "+title+" 】");
}