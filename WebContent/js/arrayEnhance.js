/**
 * 数组过滤
 *  function isBigEnough(element) {
 *	  return element >= 10;
 *	}
 *	var filtered = [12, 5, 8, 130, 44].filter(isBigEnough);
 */
if (!Array.prototype.filter) {
	Array.prototype.filter = function(fun /* , thisp */) {
		'use strict';

		if (!this) {
			throw new TypeError();
		}

		var objects = Object(this);
		//var len = objects.length >>> 0;
		if (typeof fun !== 'function') {
			throw new TypeError();
		}

		var res = [];
		var thisp = arguments[1];
		for ( var i in objects) {
			if (objects.hasOwnProperty(i)) {
				if (fun.call(thisp, objects[i], i, objects)) {
					res.push(objects[i]);
				}
			}
		}

		return res;
	};
}
/**
 * 数组遍历
 */
if (!Array.prototype.each) {
    Array.prototype.each = function (fn, scope) {
        'use strict';
        var i, len;
        for (i = 0, len = this.length; i < len; ++i) {
            if (i in this) {
                fn.call(scope, this[i], i, this);
            }
        }
    };
}

/**
 * 数组删除值
 */
if (!Array.prototype.removeValue) {
    Array.prototype.removeValue = function (value) {
    	var me = this;
    	me.each(function(item, i){
        	if(item === value){
        		me.splice(i, 1);
        	}
        });
    };
}
