//批量优化
function toOptimizeKeyword(nick, campaignId, adgroudId, days) {
    toUrl('itemSingle.action', nick, campaignId, adgroudId, days);
}


//智能优化
function toAIOptimize(nick, campaignid, adgroudid, days) {
    toUrl('toOptimize.action', nick, campaignid, adgroudid, days);
}

//快速选词
function toQuickAddKeyword(nick, campaignId, adgroudId, days) {
    toUrlWithNick('quickAddKeyword.action', nick, campaignId, adgroudId, days);
}

//优化设置
function toOptimizeSetting(nick, campaignId, adgroudId, days) {
    toUrl('toOptimizeSetting.action', nick, campaignId, adgroudId, days);
}

//通用链接拼装
function toUrl(url, nick, campaignId, adgroudId, days) {
    hRequest(url, {
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

//使用nick作为参数连接拼装
function toUrlWithNick(url, nick, campaignId, adgroudId, days) {
    hRequest(url, {
        n: 'nick', v: nick||''
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


//已删词管理链接拼装
function toDeletedKeyword(nick, campaignId, adgroudId, days){
    toUrlWithNick('getKeywordDelInfo.action', nick, campaignId, adgroudId, days);
}


//手工加词链接拼装
function toCustomAddKeyword(nick, campaignId, adgroudId, days){
    hRequest('customAddKeyword.action', {
        n: 'nickname', v: nick||''
    }, {
        n: 'campaignid',
        v: campaignId
    }, {
        n: 'adgroupid',
        v: adgroudId
    }, {
        n: 'pastDays',
        v: days
    });
}

