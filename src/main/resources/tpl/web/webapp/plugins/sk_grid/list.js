$(function() {
    $('#out_grid').sk_grid({
        url: '../${modelVarName}/list.do',
        showColTool: true,
        showNumber: true,
        customToolHtml: '${customToolHtml}',
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
        columns: [{
            field: "name",
            header: "名称",
            width: "100px",
            formatter: function(rowIndex, value, rowData) {
               return value;
            }
        }, {
            field: "",
            exculdeVisible: true,
            header: "",
            formatter: function(rowIndex, value, rowData) {
                var editBtn = '<button title="修改" onclick=edit(this,"' + rowData['id'] +
                    '") type="button" class="btn btn-primary btn-sm" style="padding:0px;padding-left:3px;padding-right:2px;margin-left:5px;">' +
                    '<i class="glyphicon glyphicon-edit ")></i></button>';
                var showNameBtn = '<button title="删除" onclick=del(this,"' + rowData['id'] +
                    '") type="button" class="btn btn-primary btn-sm" style="padding:0px;padding-left:3px;padding-right:2px;margin-left:5px;">' +
                    '<i class="glyphicon glyphicon-info-sign")></i></button>';
                return editBtn + showNameBtn;
            }
        }]
    });
});
// 修改记录
function edit(e, id) {
    window.location = './edit.html?id=' + id;
}
// 删除记录
function del(e, id) {
    bootbox.confirm('确认删除 ？', function(result) {
        if (result) {
            $.post('../${modelVarName}/delete.do', {
                id: id
            }, function(jsonData) {
                bootbox.alert(jsonData.message);
                $('#out_grid').sk_grid({
                    method: 'reload'
                });
            });
        }
    });
}
// 查询
function search() {
    $('#out_grid').sk_grid({
        method: 'reload',
        postData: { //ajax post body
            $ {
                postData
            }
        }
    });
}