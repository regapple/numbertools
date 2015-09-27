var chartsPop = null;

var optionsPop = {
	chart : {
		renderTo : 'chart_aPop',
		zoomType : 'xy'
	},
	title : {
		text : ''
	},
	subtitle : {
		text : ''
	},
	xAxis : {},
	yAxis : [ {
		gridLineWidth : 1,
		title : {
			text : '',
			style : {
				color : '#E84B00'
			}
		},
		labels : {
			formatter : function() {
				return this.value + '次';
			},
			style : {
				color : '#2489c5'
			}
		},
		opposite : false,
		minRange : 1,
		min : 0
	}, {
		gridLineWidth : 1,
		title : {
			text : '',
			style : {
				color : '#FF0B85'
			}
		},
		labels : {
			formatter : function() {
				return this.value + '元';
			},
			style : {
				color : '#FF0B85'
			}
		},
		opposite : false,
		minRange : 0.1,
		min : 0
	}, {
		gridLineWidth : 1,
		title : {
			text : '',
			style : {
				color : '#f70'
			}
		},
		labels : {
			formatter : function() {
				return this.value + '%';
			},
			style : {
				color : '#f70'
			}
		},
		opposite : false,
		minRange : 0.01,
		min : 0
	}, {
		gridLineWidth : 1,
		title : {
			text : '',
			style : {
				color : '#1b7e5a'
			}
		},
		labels : {
			formatter : function() {
				return this.value + '元';
			},
			style : {
				color : '#1b7e5a'
			}
		},
		opposite : true,
		minRange : 0.1,
		min : 0
	}, {
		gridLineWidth : 1,
		title : {
			text : '',
			style : {
				color : '#FF0B0B'
			}
		},
		labels : {
			formatter : function() {
				return this.value + '笔';
			},
			style : {
				color : '#a77a94'
			}
		},
		opposite : true,
		min : 0
	}, {
		gridLineWidth : 1,
		title : {
			text : '',
			style : {
				color : '#73716e'
			}
		},
		labels : {
			formatter : function() {
				return this.value + '';
			},
			style : {
				color : '#73716e'
			}
		},
		opposite : true,
		minRange : 0.01,
		min : 0
	} ],
	legend : {
		backgroundColor : '#FFFFFF'
	},
	tooltip : {
		formatter : function() {
			var unit = {
				'花费' : '元',
				'成交额' : '元',
				'点击量' : '次',
				'展现量' : '次',
				'投资回报率' : '',
				'点击率' : '%',
				'平均点击花费' : '元',
				'成交笔数' : '笔',
				'收藏次数' : '次',
				'点击转化率' : '%'
			};
			var obj_list = chartsPop.series;
			var result = this.x + '日 ' + "<br/>";
			for ( var i = 0; i < obj_list.length; i++) {
				if (obj_list[i].visible) {
					result = result + (obj_list[i].name) + " "
							+ obj_list[i].data[this.point.x].y
							+ (unit[obj_list[i].name]) + "<br/>";
				}
			}
			return result;
		}
	},
	// cost pay click impressions roi ctr cpc paycount favcount pct
	// 次 元 % 元 笔 默认
	series : [ {
		name : '花费',
		color : '#d54e21',
		type : 'spline',
		yAxis : 1,
		data : [],
		visible : true
	}, {
		name : '成交额',
		color : '#dae500',
		type : 'spline',
		yAxis : 1,
		data : [],
		visible : true
	}, {
		name : '点击量',
		color : '#2489c5',
		type : 'spline',
		yAxis : 0,
		data : [],
		visible : false
	}, {
		name : '展现量',
		color : '#9cc2cb',
		type : 'spline',
		yAxis : 0,
		data : [],
		visible : false
	}, {
		name : '投资回报率',
		color : '#73716e',
		type : 'spline',
		yAxis : 5,
		data : [],
		visible : false
	}, {
		name : '点击率',
		color : '#f70',
		type : 'spline',
		yAxis : 2,
		data : [],
		visible : false
	}, {
		name : '平均点击花费',
		color : '#1b7e5a',
		type : 'spline',
		yAxis : 3,
		data : [],
		visible : false
	}, {
		name : '成交笔数',
		color : '#a77a94',
		type : 'spline',
		yAxis : 4,
		data : [],
		visible : false
	}, {
		name : '收藏次数',
		color : '#6a5a8c',
		type : 'spline',
		yAxis : 4,
		data : [],
		visible : false
	}, {
		name : '点击转化率',
		color : '#ee8080',
		type : 'spline',
		yAxis : 2,
		data : [],
		visible : false
	} ]
};

function rendererChart(url,nickName,id,title){
	$.ajax({
		url:url,
		data:{
			nick:nickName,
			id:id
		},
		success:function(d,status){
			if(status=='success'){
				rendererHighChartPop(d,title);
			}
		}
	});
}
//映射走势图
function rendererHighChartPop(d,title){
//	$('#chart-div').removeClass('hide');
	//$('#chart-div .modal-header button.close').on('click',function(){$("#chart-div").addClass('hide');$('.modal-backdrop').hide();});
	$('#myModalLabelPop').html(title+"<span class='myModalLabelTip'>( 最近七天趋势 )</span>");
//	$('.modal-backdrop').modal('');
	
	var days=7;
	if(parseInt(days)<7){
		days=7;
	}
	var  date=[];//getxAxisArray(days);
	
	var  cost=new Array();
	var  impressions=new Array();
	var  click=new Array();
	var  ctr=new Array();
	var  cpc=new Array();
	var  totalPay=new Array();
	var  totalFavCount=new Array();
	var  totalPayCount=new Array();
	var  pct=new Array();
	var  roi=new Array();
	
	$.each(d,function(i,item){
		date.push(item.date);
		cost.push(item.cost);
		impressions.push(item.impressions);
		click.push(item.click);
		ctr.push(item.ctr);
		cpc.push(item.cpc);
		totalPay.push(item.totalPay);
		totalFavCount.push(item.totalFavCount);
		totalPayCount.push(item.totalPayCount);
		pct.push(item.pct);
		roi.push(item.roi);
	});
	
	optionsPop.xAxis.categories =date;
	//cost  pay click impressions roi  ctr  cpc  paycount  favcount pct
	optionsPop.series[0].data=cost;
	optionsPop.series[1].data=totalPay;
	optionsPop.series[2].data=click;
	optionsPop.series[3].data=impressions;
	optionsPop.series[4].data=roi;
	optionsPop.series[5].data=ctr;
	optionsPop.series[6].data=cpc;
	optionsPop.series[7].data=totalPayCount;
	optionsPop.series[8].data=totalFavCount;
	optionsPop.series[9].data=pct;
	chartsPop = new Highcharts.Chart(optionsPop);
	$('#chart-div').modal({backdrop:true,show:true});
}
//x轴日期数据
function getxAxisArray(days){
	var arr=new Array();
	var now = new Date();
	var timestamp=now.getTime();
	
	if(days>=1){
		for (var i=days;i>=1;i--){
			var t=timestamp-24*3600*1000*i;
			var d=new Date(t);
			arr.push((d.getMonth()+1)+"-"+d.getDate());
		}
		
		return arr;
	}else{
		alert("参数错误");
	}
}