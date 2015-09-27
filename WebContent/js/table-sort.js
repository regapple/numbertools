Array.prototype.diff = function(a) {
    return this.filter(function(i) {return !(a.indexOf(i) > -1);});
};
(function($) {
  $.fn.tableSort = function(options) {
    // columns:[1,2,3...],
    // conditionGenerator: function(indexes, index, ctrl){}
    var me = $(this), opts = $.extend($.fn.tableSort.defaults, options), currentIndexes = [], currentConditions = [], orders = [], currentColumn, order;
    var _sort = function(e) {
      var loader = me.parent().loading();
      var data = e.data, area = data.renderArea, rows = data.rows, sortThs = data.columns, colIndex = data.i, generator = data.generator;
      var n = ("[object Object]" === Object.prototype.toString.call(colIndex)?colIndex.i:"[object Number]" === Object.prototype.toString.call(colIndex)?colIndex:-1);
      var nextOrder, currOrder = $(this).attr('class');

      if ('sort-both' == currOrder) {
        nextOrder = 'sort-desc';
      } else {
        if ('sort-asc' == currOrder) {
          nextOrder = 'sort-desc';
        } else if ('sort-desc' == currOrder) {
          nextOrder = 'sort-asc';
        }
      }

      var co = nextOrder.split('-').pop(), order = co, obj = generator(
              currentIndexes, colIndex, orders, co, e.ctrlKey);
      currentColumn = n;
      currentIndexes = obj.currentColumns;
      currentConditions = obj.currentConditions;
      orders = obj.currentOrders;

      var array = quickSort.call(rows, currentConditions, 0, orders);

      var oFragment = document.createDocumentFragment();
      array.each(function(o) {
        oFragment.appendChild(o);
      });
      $('tbody', area)[0].appendChild(oFragment);

      $.each(currentIndexes, function(x, y){
        me.find('thead > tr > th').eq(y).children('div').eq(0).attr('class', 'sort-' + orders[x + 1]);
      });
      $.each(sortThs.diff(currentIndexes), function(x, y){
        me.find('thead > tr > th').eq(y).children('div').eq(0).attr('class', 'sort-both');
      });

      loader.each(function(item) {
        item.remove()
      });
    };
    // 快速排序
    function quickSort(sortFn, depth, orders) {
      if (this.length <= 1) { return this; }
      if (depth >= sortFn.length) { return this; }
      var order = orders[depth], pivotIndex = Math.floor(this.length / 2), sortedArray = [], pivot = this[0], left = [], right = [], center = [];
      for ( var i = 0; i < this.length; i++) {
        var temp = this[i], n = sortFn[depth](temp, pivot);
        if (n < 0) {
          left.push(this[i]);
        } else if (n > 0) {
          right.push(this[i]);
        } else if (n == 0) {
          center.push(this[i]);
        }
      }
      if (sortFn.length <= (depth + 1)) {
        if ('desc' == order) {
          sortedArray = quickSort.call(left, sortFn, depth, orders).concat(
                  center, quickSort.call(right, sortFn, depth, orders));
        } else if ('asc' == order) {
          sortedArray = quickSort.call(right, sortFn, depth, orders).concat(
                  center, quickSort.call(left, sortFn, depth, orders));
        }
      } else {
        if ('desc' == order) {
          sortedArray = quickSort.call(left, sortFn, depth, orders).concat(
                  quickSort.call(center, sortFn, depth + 1, orders),
                  quickSort.call(right, sortFn, depth, orders));
        } else if ('asc' == order) {
          sortedArray = quickSort.call(right, sortFn, depth, orders).concat(
                  quickSort.call(center, sortFn, depth + 1, orders),
                  quickSort.call(left, sortFn, depth, orders));
        }
      }
      return sortedArray;
    }
    $.fn.tableSort.defaults = {
      columns: [],
      conditionGenerator: $.fn.empty()
    };
    this.each(function(i, item) {
      var headers = $('thead > tr > th', item), rows = $('tbody > tr', item);
      opts.columns.each(function(i) {
        var j = ("[object Object]" === Object.prototype.toString.call(i)?i.i:"[object Number]" === Object.prototype.toString.call(i)?i:-1);
        // 添加排序样式
        $(headers[j]).html(
                '<div class="sort-both" title="按下ctrl键多列排序">' + $(headers[j]).text() + '</div>');
        // 添加事件支持
        $('div[class^=sort-]', headers[j]).bind('click', {
          i: i,
          columns: opts.columns,
          renderArea: item,
          rows: rows.get(),
          generator: opts.conditionGenerator
        }, _sort);
      });
    });
    return $.extend(opts, {
      currentColumn: currentColumn,
      currentColumnOrder: order,
      currentConditions: currentConditions
    });
  };

})(jQuery);
