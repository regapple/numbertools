var optimizeProfileModal;
function showOptimizeProfile() {
  var ckboxes = $('input[type=checkbox][name=rowid]:checked');
  if (ckboxes.length > 0) {
    $.ajax({
      url: 'getOptimizeParams.action',
      data: {
        opid: ckboxes.length > 1 ? ',' : ckboxes[0].value,
        type: $('#optimize-profile-type').val()
      },
      success: function(data, status) {
        if ('success' == status) {
          formInit(data);
          optimizeProfileModal.modal('show');
        }
      }
    });
  } else {
    alert('请选择要配置的推广计划');
  }
}

function formInit(json) {
  var form = $('#optimal-div form');
  form.find('input[type=radio][name=optimize_tactic]').each(function(i, item) {
    item.checked = (item.value == json.optimize_tactic);
  });

  form.find('#optimiz-profile-rankedRange1').val(parseInt(json.avgpos_level_1) || '');
  form.find('#optimiz-profile-rankedRange2').val(parseInt(json.avgpos_level_2) || '');
  form.find('#optimiz-profile-rankedRange3').val(parseInt(json.avgpos_level_3) || '');

  form.find('#optimiz-profile-bidonbase').val(parseFloat(json.base_range) || '');
  form.find('#optimiz-profile-bidondelta').val(parseFloat(json.change_scope) || '');
}

function formValidation() {
  var form = $('#optimal-div form'), radios = form
          .find('input[type=radio][name=optimize_tactic]:checked'), integerInputs = form
          .find('#optimiz-profile-rankedRange1,#optimiz-profile-rankedRange2,#optimiz-profile-rankedRange3,#optimiz-profile-bidonbase,#optimiz-profile-bidondelta');
  if (radios.length == 0) {
    alert('请选择优化策略');
    return false;
  }

  var i = 0, l = integerInputs.length;
  for (; i < 3; i++) {
    var value = integerInputs[i].value;
    if (!value || !value.trim() || !/^[1-9][0-9]*$/.test(value.trim())) {
      alert('必须输入大于0的整数');
      integerInputs[i].focus();
      integerInputs[i].select();
      return false;
    }
  }
  for (i = 3; i < l; i++) {
    var value = integerInputs[i].value;
    if (!value || !value.trim() || !/^[1-9][0-9]*$|^[1-9]$|[1-9]*[0-9]?\.[0-9]*$/.test(value.trim())) {
      alert('必须输入大于0的数');
      integerInputs[i].focus();
      integerInputs[i].select();
      return false;
    }
  }
  var a = parseInt(integerInputs[0].value),
      b = parseInt(integerInputs[1].value),
      c = parseInt(integerInputs[2].value);
  if(a > b || a > c || b > c){
    alert('顺序不对');
    return false;
  }

  return {
    opid: function(){
      var ckboxes = $('input[type=checkbox][name=rowid]:checked'),array = [];
      ckboxes.each(function(i, item){
        array.push(item.value);
      });
      return array;
    },
    type: $('#optimize-profile-type').val(),
    optimize_tactic: $('input[type=radio][name=optimize_tactic]:checked').val(),
    avgpos_level_1: integerInputs[0].value,
    avgpos_level_2: integerInputs[1].value,
    avgpos_level_3: integerInputs[2].value,
    base_range:     integerInputs[3].value,
    change_scope:   integerInputs[4].value,
  };
}
$(function() {
  optimizeProfileModal = $('#optimal-div').modal('hide');
  // 保存
  $('#optimal-divButton').on('click', function() {
    var flag = formValidation(), form = $('#optimal-div form');
    if (flag) {
      $.ajax({
        url: 'setOptimizeParams.action',
        data: flag,
        success: function(data, status){
          if ('success' == status) {
            alert(data.message);
          }
        },
        complete: function(){
          optimizeProfileModal.modal('hide');
        }
      });
    }
  });
});