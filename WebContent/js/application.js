// NOTICE!! 
// IT'S TOOLTIP PREFERENCES!
// ++++++++++++++++++++++++++++++++++++++++++


$(function(){
    ////////// tooltip
	// 下面的语句移到common.js中，以便异步加载也生效
    $('[data-toggle=tooltip]').tooltip();
	
	// Test ------------
	$("i[data-original-title]").attr("data-original-title","Test");
	
	// 总花费
	$("#totalCost i[data-original-title]").attr("data-original-title","推广单元在淘宝直通车展示位上被用户点击所消耗的费用。");	
	// 展现量
	$("#impressionstd i[data-original-title]").attr("data-original-title","推广单元在淘宝直通车展示位上被买家看到的次数。");
	// 点击量
	$("#clickRate i[data-original-title]").attr("data-original-title","推广单元在淘宝直通车展示位上被点击的次数。注意，虚假点击会被直通车反作弊体系过滤，该数据为反作弊系统过滤后的数据。");
	// 点击率
	$("#clicksRatio i[data-original-title]").attr("data-original-title","点击率 = 点击量 / 展现量，可直观表示宝贝的吸引程度，点击率越高，说明宝贝对买家的吸引力越大。");
	// 平均点击花费
	$("#averageClickCost i[data-original-title]").attr("data-original-title","平均点击花费 = 花费 / 点击量，即推广单元每一次点击产生的平均花费金额");
	// 成交额
	$("#turnover i[data-original-title]").attr("data-original-title","成交额 = 直接成交金额 + 间接成交金额，即推广单元在淘宝直通车展示位被点击后，买家在15天有效时间内，所有通过支付宝交易的成交金额（含运费）。");
	// 收藏量
	$("#collections i[data-original-title]").attr("data-original-title","收藏量 = 收藏宝贝数 + 收藏店铺数，即推广单元在淘宝直通车展示位被点击后，买家在15天有效时间内，所有发生收藏的次数。");
	// 成交数
	$("#numberOfTransactions i[data-original-title]").attr("data-original-title","成交数 = 直接成交笔数 + 间接成交笔数，即推广单元在淘宝直通车展示位被点击后，买家在15天有效时间内，所有通过支付宝交易的成交笔数。");
	// 点击转化率
	$("#clickTheConversionRate i[data-original-title]").attr("data-original-title","点击转化率 = 总成交笔数 / 点击量，反应淘宝直通车点击在15天内转化支付宝成交的比例。");
	// 投资回报率
	$("#theReturnOnInvestment i[data-original-title]").attr("data-original-title","投资回报率 = 总成交金额 / 花费，反应淘宝直通车点击花费在15天内带来支付宝成交金额的比例。");


	
	// 管理我的委托
	$("#elegate i[data-original-title]").attr("data-original-title","通过该功能，您可以从您所有的推广计划中，选择哪些推广计划委托本软件进行管理。如果是第一次使用本软件，必须使用该功能进行设置，否则会影响其他功能的使用。当您选择了推广计划然后点击“保存”按钮时，软件会自动下载所选推广计划的相关数据，您稍等片刻刷新一下页面就能看到数据了。");
	// 操作记录
	$("#record i[data-original-title]").attr("data-original-title","通过本软件所作操作的日志记录。");

	
	// 推广状态过滤
	$("#filter i[data-original-title]").attr("data-original-title","根据所选宝贝状态，过滤本页面显示的推广宝贝。");

	
	// 自定义删除
	$("#customDel i[data-original-title]").attr("data-original-title","自定义删除：按照您在右侧选择的条件进行删除。<br/>手工删除：删除您手动勾选的关键词。<br/>智能删除：删除全店所有无展现词，有展现的词即使重复也保留。");	
	// 删词级别
	$("#delLevel i[data-original-title]").attr("data-original-title","对于某个重复的关键词，按照点击量，展现量等数据降序排列，排名越往后，删词级别越高，也就越建议您删除。");


	// 策略类型
	$("#policyType i[data-original-title]").attr("data-original-title","1.ROI优先：以提高宝贝的ROI为目标的优化策略。<br/>2.流量优先：以提高宝贝的访问流量为目标的优化策略。<br/>3.低PPC优先：以降低宝贝的整体花费为目标的优化策略。<br/>4.当您变换优化策略时，软件会重新计算建议出价。");
	// 保存策略类型
	$("#savePolicyType i[data-original-title]").attr("data-original-title","当您选择了优化策略和加价降价幅度后，可以点击“保存”按钮为当前宝贝设置默认的优化策略，下次智能优化时将按照您保存的策略进行计算。");




    ////////// Message
    
    // Test ------------
	// $(" .alert .alertMessage").html("<strong>Holy guacamole! </strong>Text!请选择要操作的账户！");
		
	// AccountList  
	$("#accountList .alert .alertMessage").html("<strong>欢迎来到洪海直通车 ! </strong>现在开始新的旅程吧！");    
    // Home  
	// $("#home .alert .alertMessage").html("<strong>Hello！</strong> 双12将至，淘宝为了减轻服务器压力，已开始限制软件的接口调用量，为了您能顺利使用软件，麻烦亲做如下调整： <p>1、尽早添加推广宝贝，尽早选词和改价，以免因接口限制而无法提交，进而错过推广良机。</p><p>2、部分功能接口可能出现不稳定，如预估排名、报表下载、质量得分等，请亲错过高峰期。</p><p>3、接淘宝通知，将下线店铺总流量、店铺访客数、店铺转化率的接口，我们也将予以隐藏。</p>");
    
	
});


		

