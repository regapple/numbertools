;(function ($) {
    $.fn.simpleGrid = function (options) {
        var defaults = {
            url:'',
            data:[],
            useExistsData: false,
            params: {},
            defaultRenderer:function(value,item){
                return value;
            },
            columns:[{
                id:'',
                name:'',
                style:'',
                renderer:function(value,item){
                	return value || '';
                },
                action:function(){alert(this.id);}
            }], //列名
            isPagination: false,//是否分页
            showSearch: false, //是否显示查询条件
            rowProperty:'items',
            totalProperty:'total',
            currentInfo: {
                rowPerPage:8,//每一页有多少条数据
                pageIndex:1//第几页
            },
            searchFields:[]//查询条件,接收dom对象的字符串表示形式
        };
        var opts = $.extend(defaults, options);
        var me =$(this);
        this.each(function(i,item){
            //$(me.selector + ' .pagination').delegate('.pagination ul li','click',function(){alert(1);});
            preInit($(item));
            if(opts.url){
            	getData(1);
            }else if(opts.data){
            	loadData(opts.data);
            }
        });
        //only pure dom
        function render(htmlArray,ele){
            if(!ele){return;}
            var text = htmlArray.join('');
            ele.innerHTML = text;
//            $(ele).html(htmlArray.join(''));
        }
        
        function preInit(ele){
            var fragments = generateSearchCondition();
            fragments.push('<div class="data-table"></div>');
            //fragments.push('<div class="pagination pagination-right"></div>');
            $(ele).html(fragments.join(''));
            $('.input-append a',ele).bind('click',function(){
                if(opts.url){
                	getData(1);
                }
                else if(opts.data){
                	loadData(opts.data);
                };
            });
        }
        
        //加载已有数据
        function loadData(data){
        	processData(data);
        }
        
        //事件处理
        function renderTableArea(data, sortClm, sortOrder){
//        	var dataTable  = generateDataTable(opts.isPagination?data[opts.rowProperty]:data, sortClm, sortOrder);
        	var dataTable  = generateDataTable(data[opts.rowProperty]||data, sortClm, sortOrder);
            render(dataTable, me.children('.data-table')[0]);
        }
        
        function processData(data){
        	renderTableArea(data);
            //事件绑定
            if(opts.isPagination){
            	var pagination = generatePagination(data);
            	  me.children('.pagination').remove();
            	  me.append(pagination.join(''));
                
                var $p = $('.pagination ul li',me);
                var $first = $p.first(),$last = $p.last(),$else = $p.not($first).not($last);
                $p.first().bind('click',function(){
                    if(!$(this).hasClass('disabled')){
                        getData(parseInt(opts.currentInfo.pageIndex) - 1);
                    }
                });
                $p.last().bind('click',function(){
                    if(!$(this).hasClass('disabled')){
                        getData(parseInt(opts.currentInfo.pageIndex ) + 1);
                    }
                });
                $else.bind('click',function(){
                    if(!$(this).hasClass('disabled')){
                        var i = $('a',this).html();
                        getData(i);
                    }
                });
            }
            opts.callback && opts.callback(data,opts);
        }
        
        //异步获取数据
        function getData(n,callback){
            var searchString = $('.input-append input,.input-append select,.input-append textarea',me).serialize();
            opts.currentInfo.pageIndex  = arguments[0] || 1;
            var paginationString = $.param(opts.currentInfo);
            var paramToPass = $.param(opts.params);
            var temp = (paginationString?encodeURI(paginationString):'');
            if(searchString){
            	if(temp){
            		temp += '&';
            	}
            	temp += searchString;
            }
            if(paramToPass){
            	if(temp){
            		temp += '&';
            	}
            	temp += encodeURI(paramToPass);
            }
            $.ajax({
                url: encodeURI(opts.url),
                contentType:"application/x-www-form-urlencoded; charset=UTF-8",
                data: temp,
                //overLayTarget: me.selector,
                success: function (data, status) {
                    if (status == 'success') {
                    	processData(data);
                    }
                }
            });
        }

        function generateSearchCondition(){
            var fragments = [];
            if(opts.showSearch && opts.searchFields){
                fragments.push('<div class="input-append">');
                fragments.push(opts.searchFields.join(''));
                fragments.push('<a class="btn"><i class="icon-search"></i></a></div>');
            }
            return fragments;
        }

        function generateDataTable(o, clm, order){
            var fragments = [];
            fragments.push('<table class="table table-striped  table-condensed table-hover table-bordered">');
            fragments.push("<thead>");
            if(opts.columns && opts.columns.length > 0){
                fragments.push("<tr>");
                $.each(opts.columns||[],function(i,item){
                	var text = item.name, 
                	    attribute = generateAttr.call(item['thAttribute'] || {});
                    fragments.push("<th" + attribute + ">" + text + "</th>");
                });
                fragments.push("</tr>");
            }
            fragments.push("</thead>");
            fragments.push("<tbody>");
            $.each(o, function (i,item) {
                var trAttribute = opts.trAttribute || {};
                //console.log(JSON.stringify(trAttribute, function(k,v){if(typeof(v) == 'function'){v=v.call(this,i,item);}return v;}));
                var att = []; 
                for(at in trAttribute){att.push(at + "='" + (typeof(trAttribute[at]) == 'function'?trAttribute[at].call(this,i,item):trAttribute[at]) + "'");}
                fragments.push("<tr " + att.join(" ") + ">");
                $.each(opts.columns,function(j,column){
                    var renderer = column.renderer || opts.defaultRenderer;
                    var v = renderer(item[column.id],item,i), 
                        attribute = generateAttr.call(column['cellAttribute'] || {}, i, j, item);
                    fragments.push("<td" + attribute + ">" +
                    		(v == null?'':v) + 
                    		"</td>");
                });
                fragments.push("</tr>");
            });
            fragments.push("</tbody>");
            fragments.push('</table>'); 
            return fragments;
        }
        function generatePagination(o){
            var totalPage = 0,pageLabel = 0,min = 0, max = 0;
            var totalkey = opts.totalProperty || 'total';
            if(o && o[totalkey]){
                totalPage = Math.ceil(o[totalkey]/opts.currentInfo.rowPerPage);
                pageLabel = (totalPage > 5?5:totalPage);
                if(opts.currentInfo.pageIndex <= 3){
                    min = 1;
                    max = (totalPage >pageLabel?pageLabel:totalPage);
                }else{
                    min = (parseInt(opts.currentInfo.pageIndex) - 2);
                    max = ((parseInt(opts.currentInfo.pageIndex ) + 2 > totalPage)?totalPage:(parseInt(opts.currentInfo.pageIndex) + 2));
                }
            }
            var fragments = [];
            fragments.push('<div class="pagination pagination-right">');
            fragments.push('<div class="pagination-tip">' + getPaginationInfo(o) + '</div>');
            if(pageLabel > 0){
                fragments.push('<ul>');
                fragments.push('<li ' + ((opts.currentInfo.pageIndex <= 1)?'class="disabled"':'') + '><a href="javascript:;">上一页</a></li>');
                for(;min <= max; min++){
                    fragments.push('<li ' + (opts.currentInfo.pageIndex == min?'class="disabled"':'') + '><a href="javascript:;">' + min + "</a></li>");
                }
                fragments.push('<li ' + (opts.currentInfo.pageIndex >= totalPage?'class="disabled"':'') + '><a href="javascript:;">下一页</a></li>');
                fragments.push('</ul>');
            }
            fragments.push('</div>');
            return fragments;
        }
        function getPaginationInfo(o){
            var pageInfo = '';
            var totalkey = opts.totalProperty || 'total',
            	itemKey = opts.rowProperty || 'items';
            if(o){
                var total = o[totalkey], items = o[itemKey];
                if(total <= 0){
                    pageInfo = '没有数据哦 !';
                }else{
                    //pageInfo = '{total}条记录，共{totalPage}页，第{index}页';
                    pageInfo = '记录数：' + items.length + '/{total}，页数：{index}/{totalPage}.';
                    pageInfo = pageInfo.replace(/\{total\}/g,total);
                    pageInfo = pageInfo.replace(/\{capacity\}/g,opts.currentInfo.rowPerPage);
                    pageInfo = pageInfo.replace(/\{totalPage\}/g,Math.ceil(total/opts.currentInfo.rowPerPage));
                    pageInfo = pageInfo.replace(/\{index\}/g,opts.currentInfo.pageIndex);
                }                
            }
            return pageInfo;
        }
        
        function generateAttr(){
          if(!this){return '';}
          var array = [];
          for(key in this){
            var v = (typeof(this[key]) === 'function'?this[key].apply(me,arguments): this[key]); 
            array.push((key === 'className'?'class':key) + '="' + v + '"');
          }
          array = array.join(' ');
          return array? (' ' + array) : '';
        }
        
        return opts;
    };
})(jQuery);
