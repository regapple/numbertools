<%@page import="java.util.*,java.lang.*"%>
<%@page import="com.song.common.util.HttpPathUtil" language="java"%>
<%@ page contentType="text/html; charset=utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%
	String path = request.getContextPath();
	String basePath = HttpPathUtil.getBasePath(request);
%>
<!DOCTYPE html>
<html>
<head>
<base href="<%=basePath%>">
<title>查询数据</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">

<link rel="stylesheet" href="bootstrap/css/bootstrap.css" />
<link rel="stylesheet" type="text/css" href="./css/main.css" />
<link rel="stylesheet" href="js/uploadify/uploadify.css"/>
<link rel="stylesheet" href="css/item.css" />
<style type="text/css">
.ilabel,
.ivalue,
.tooltip-Tip,
.edit-part.icon-pencil{
	vertical-align: middle;

	display: inline-block;
	*display: inline;
	*zoom: 1;
}

.edit-part{
	position: relative;

}
.card_header>.dropdown,.card_header>.btn,.card_header .btn-group {
	margin-top: 5px;
}

.card_header .search .input-append {
	margin-left: 10px !important;
}

.card_header .btn-group .dropdown-menu {
	min-width: 30px;
}

.card_header .btn-group .dropdown-menu li {
	text-align: left;
}

.badgeDiv {
	height: 40px;
	padding-top: 5px;
	padding-left: 6px;
}
.ophandler{
	position: relative;
	height: 15px;
	cursor: pointer;
}
.ophandler .handlerinner{
  	position: absolute;
 	width: 0;
	border:10px solid transparent;
	border-top:10px solid #8ECBFF;
	left: 50%;
	transition:.2s ease-in;
}
.ophandler.slide .handlerinner{
 	-webkit-transform: rotate(180deg) translateY(10px);
	transition:.2s ease-in;
}
.ver-info{
	text-align: right;
	background: #D7DFFA;
	height: 22px;
	line-height: 22px;
    clear: both;
}
.oparea{
	border: 1px solid #D2D6FF;
	padding: 4px 20px;
}
.oparea .opgroup{
	margin-top: 2px;
	float: left;
	width: 395px;
}
.oparea .space{
	display: inline-block;
	*display: inline;
	*zoom: 1;
	width: 60px;
}

.oparea .opgroup.vinfo{
	width: 330px;
}
.oparea .opgroup.action{
	float: right;
	width: 380px;
}
.oparea .opgroup.state{
	width: 480px;
}
.oparea .opgroup.state .oplist > li{
	display: inline-block;
	*display: inline;
	*zoom: 1;
}
.oparea .opgroup .oplist{
	margin-left: 10px;
	margin-right: 10px;
}
.oparea .opgroup .oplist > li{
	display: inline-block;
}
.oparea .opgroup .optitle:after{
	content: ':';
}
.oparea .opgroup .optitle{
	display: inline-block;
	*display: inline;
	*zoom:1;
	min-width: 65px;
	font-weight: bold;
	vertical-align: top;
}
.oparea .badge{
	padding-top:0;
	padding-bottom:0;
}
.oparea .highlight{
	font-weight: bold;
	font-size: 1.2em;
}

.btn-group{
	border-radius: 3px;
	overflow: hidden;
}
.btn-group > .btn{
 	border-radius: none !important;
 	-webkit-border-radius: 0 !important;
 	font-size: 12px;
}
.btn-group > .btn.active{
	font-weight: bold;
}

    div.content-div table td {
        word-break: break-all;
    }
</style>

<script type="text/javascript" src="js/jquery.js"></script>
<script type="text/javascript" src="js/arrayEnhance.js"></script>
<script type="text/javascript" src="js/table-sort.js"></script>
<script type="text/javascript" src="bootstrap/js/bootstrap.js"></script>
<script type="text/javascript" src="js/common.js"></script>
<script type="text/javascript" src="js/simpleGrid.js"></script>
<script>
	var tourKey = 'item';
</script>
</head>

<body id="item">
	<div class="tour"><div class="inner"></div></div>

	<div class="wraper">
			<div class="content">
				<div class="card_total">
					<div class="card_header">
                        <div  class="card_left">
                            <div class="query-div search_group pull-right">
                                <input  type="text" id="nick" name="nick"
                                       value="" placeholder="淘宝ID" title="淘宝ID" />&nbsp;&nbsp;&nbsp;&nbsp;
                                <input  type="text" id="owner" name="owner"
                                       value="" placeholder="导入人" title="导入人" />
                                       
                                <button id="mainform-submit" class="search_button"></button>
                            </div>
                        </div>
					</div>
					<div class="speed_btn">
							<button type="button" class="btn btn_speed" id="importData">导入数据</button>
							<button type="button" class="btn btn_speed" id="deleteData">删除</button>
							<button type="button" class="btn btn_speed" id="exportData">导出所有数据</button>
						<div class="clearfix"></div>
					</div>
					<div>
						<div>
							<div class="content-div"></div>
						</div>
					</div>
				</div>
				
				<div id="campaign-div" class="m-modal modal fade" tabindex="-1"
		role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
		<div class="m-modal-table modal-dialog">
			<div class="m-modal-cell">
				<div class="m-content">
					<div class="panel panel-primary">
						<div class="panel-heading modal-header">
							<button type="button" class="close" data-dismiss="modal"
								aria-hidden="true">&times;</button>
							<h4>导入数据</h4>
						</div>
						<div class="panel-body">
							<form id="importForm" class="form-horizontal" role="form">
								<div class="form-group">
									<label for="inputEmail3" class="col-sm-2 control-label">导入人：</label>
									<div class="col-sm-10">
										<input type="text" name="campaignTitle" class="form-control"
											id="inputTitle" placeholder="导入人">
										<input type="hidden" id="path">
									</div>
								</div>
								<br />
								<div class="form-group">
									<label class="col-sm-2 control-label" >选择文件：</label>
									<div class="col-sm-10">
										<input type="file" 
											id="inputFile" placeholder="选择文件">
									</div>
									<div>
										<div style="float:right;width:79%;" align="left" id="fileQueue"></div>
									</div>
								</div>
							</form>
						</div>
						<div class="panel-footer">
							<div id="saveBtn" class="button">保存</div>
							<div class="button" data-dismiss="modal">取消</div>
						</div>

					</div>
				</div>
			</div>
		</div>
	</div>
			</div>

	</div>

<script type="text/javascript">
document.write("<script type='text/javascript'"+
		" src='<%=path%>/js/uploadify/jquery.uploadify-3.1.min.js?f=<%=System.currentTimeMillis()%>'></s"+"cript>");
		//可排序序号，基于0
		var columns = [{
			id:'',
			name:'序号',
			renderer:function(value,item,index){
				return (index+1);
			}
		},{
			id: 'id',
			thAttribute: {
			  className: 'pictd',
			  style: 'min-width: 18px;width: 18px;vertical-align:middle; text-align: center'
			},
			cellAttribute: {
			  className: 'pictd',
			  style: 'vertical-align:middle; text-align: center'
			},
			name: '<input type="checkbox" id="checkall" name="checkall">',
			renderer: function(value, item){
				return '<input type="checkbox" name="rowid" value="' + value + '" data-id="' + item.id + '">';
			}
		},{
			id: 'name',
			name: '掌柜姓名'	,
			thAttribute: {
			  style: 'min-width:90px;'
			}
		},{
			id: 'tid',
			name: '淘宝ID',
			thAttribute: {
			  style: 'min-width:130px;'
			}
		},{
			id: 'mobile',
			name: '移动电话'	,
			thAttribute: {
			  style: 'min-width:110px'
			}
		},{
			id: 'shopName',
			name: '店铺名称'	,
			thAttribute: {
			  style: 'min-width:140px'
			},
			renderer: function(value, item){
				var url=item.shopLink;
				if(url){
					return '<a href="' + (url || '') + '" target="_blank">' + value + '</a><br/>' ;
				}else{
					return  value  ;
				}
				
			}
		},{
			id: 'shopLink',
			name: '店铺链接'	,
			thAttribute: {
			  style: 'min-width:140px'
			},
			renderer: function(value, item){
				var url=item.shopLink;
				if(url){
					return '<a href="' + (url || '') + '" target="_blank">' + url + '</a><br/>' ;
				}else{
					return  value  ;
				}
				
			}
		},{
			id: 'category',
			name: '类目',
			thAttribute: {
			  style: 'min-width:120px'
			}
		},{
			id: 'shopRank',
			name: '店铺级别',
			thAttribute: {
			  style: 'min-width: 80px'
			}
		},{
			id: 'shopType',
			name: 'B店C店',
			thAttribute: {
			  style: 'min-width: 80px; vertical-align: middle;'
			},
			cellAttribute: {
			  style: 'vertical-align: middle'
			}
		},{
			id: 'owner',
			name: '上传人',
			thAttribute: {
			  style: 'min-width: 80px; vertical-align: middle;'
			},
			cellAttribute: {
			  style: 'vertical-align: middle'
			}
		} ];
		
		function loadData(){
			$('.content-div').simpleGrid({
				url:'queryList',
				params:{
					tid:$("#nick").val(),
					owner:$("#owner").val()
				},
				columns:columns,
				currentInfo:{
					rowPerPage:30
				},
				isPagination:true
			});
		}
		function showModal(){
			$("#campaign-div").modal({
				backdrop : true,
				show : true
			});
		}
		function validate(){
			var length=$('input[name=rowid]:checked').length;
			if(length==0){
				return false;
			}else{
				return true;
			}
		}
		function deleteData(){
			if(validate()){
				 confirm("确定要删除这些数据吗？", function(text) {
				      if ($(this).hasClass('yes')) {
				    	  var d=[];
				    	 $('input[name=rowid]:checked').each(function(i,e){
				    		 d.push($(e).attr('data-id'));
				    	 });				   
				        $.ajax({
				          url: 'delete',
				          data: {
				        	 id:d.join('л')  
				          },
				          success: function(data, status) {
				            if ('success' == status) {
				              alert(data.msg, function() {
				                    loadData();
				              });
				            }
				          }
				        });
				      }
				    });
			}else{
				alert("请选择要删除的数据");
			}
		}
		function exportData(){
			window.open("download");
		}
		$(function() {
			//过滤状态
			//shift多选调用
			$('input[type=checkbox][name=rowid]').shiftSelectable();
			
			$('#mainform-submit').on('click',loadData);
			$('#importData').on('click',function(){location.assign('upload')});
			$('#deleteData').on('click',deleteData);
			$('#exportData').on('click',exportData);
			 //生成表格
			loadData();
		});
		
	</script>
</body>
</html>