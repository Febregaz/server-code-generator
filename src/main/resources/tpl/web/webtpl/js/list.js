$(function() {
	initLeft(true, '../${modelVarNameLowerCase}/list.html') ;
	initNavUser(true);
    $('#out_grid').sk_grid({
        url: '../${modelVarName}/list.do',
        showColTool: true,
        showNumber: true,
        customToolHtml:'${customToolHtml}',
        buttons: [{
            iconCls: 'glyphicon glyphicon-plus',
            title: '新增',
            handler: function() {
                window.location = './add.html';
            }
        }, {
            iconCls: 'glyphicon glyphicon-refresh',
            title: '刷新',
            handler: function() {
                $('#out_grid').sk_grid({
                    method: 'reload'
                });
            }
        }],
        columns: [${columns}]
    });
});
// 修改记录
function edit(e, id) {
    window.location = './edit.html?id=' + id;
}
// 删除记录
function del(e, id) {
	if (confirm('确认删除 ？')) {
		$.post('../${modelVarName}/delete.do', {
			id : id
		}, function(jsonData) {
			alert(jsonData.message);
			$('#out_grid').sk_grid({
				method : 'reload'
			});
		});
	}
}
// 查询
function search() {
	$('#out_grid').sk_grid({
		method: 'reload',
        url: '../${modelVarName}/search.do',
		postData: {//ajax post body
           ${postData}
        }
	});
}