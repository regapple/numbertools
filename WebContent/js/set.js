/**
 * author mpc
 * 功能：Map实现，以键值方式存储对象，相同的key会覆盖掉之前的value
 * 说明：使用时可以提供一个获取key的方法
 * 例子：var map = new Map(function(){return this.id;});
 * map.size 返回存储对象个数
 * map.add({id:1,……});
 * map.keys();
 * map.values();
 * map.values(function(){return this.id == 1;});
 * map.containsKey(1);
 * map.containsValue({id:1,……}); 只要通过获取key的方法得到的key存在map中，则认为value存在
 * map.remove();
 * map.remove(function(){return this.id == 1;});
 */
var Map = function(fn){
	var o = {};
	var size = 0;
	this.keyFn = fn;
	this.entry = o;
	this.size = size;
};
Map.prototype.add = function(array){
	var _array = [].concat(array),  i = 0;
	for(; i < _array.length; i ++){
		var key = (this.keyFn?this.keyFn.call(_array[i]):_array[i]);
		if(!(key in this.entry)){
			this.size ++;
		}
		this.entry[key] = _array[i];//覆盖
	}
};
Map.prototype.remove = function(filterFn){
	if(!filterFn){
		this.entry = {};
		this.size = 0;
	}else{
		for(var o in this.entry){
			if(this.entry.hasOwnProperty(o)){
				if(filterFn && filterFn.call(this.entry[o])){
					delete this.entry[o];
					this.size --;
				}
			}
		}
	}
};
Map.prototype.keys = function(){
	var array = [];
	for(var o in this.entry){
		if(this.entry.hasOwnProperty(o))
		array.push(o);
	}
	return array;
};
Map.prototype.values = function(filterFn){
	var array = [];
	for(var o in this.entry){
		if(this.entry.hasOwnProperty(o)){
			if(!filterFn){
				array.push(this.entry[o]);
			}else if(filterFn.call(this.entry[o])){
				array.push(this.entry[o]);
			}
		}
	}
	return array;
};
Map.prototype.containsKey = function(key){
	for(var o in this.entry){
		if(key === o){
			return true;
		}
	}
	return false;
};
Map.prototype.containsValue = function(value){
	var key = (this.keyFn?this.keyFn.call(value):value);
	for(var o in this.entry){
		if(key === o){
			return true;
		}
	}
	return false;
};