function getAnnouncement(){
	$.ajax({
		url:'announcement.action',
		type:'POST',
		success:function(d,status){
			if(status=='success'){
				if(d.length>0){
					var html='';
					for(var i=0;i<d.length;i++){
						html=html+"<p>"+d[i].content+"</p>";
					}
					$("#home .alert .alertMessage").html(html);
				}
			}
		}
	});
}
