function init(addOrEditUrl) {
    initLeft(true, '../${modelVarNameLowerCase}/list.html'); // 初始化左边树
    initNavUser(true); // 初始化导航栏的用户名
    $('form').bootstrapValidator({
        // Only disabled elements are excluded
        // The invisible elements belonging to inactive tabs must be validated
        excluded: [':disabled'],
        feedbackIcons: {
            valid: 'glyphicon glyphicon-ok',
            invalid: 'glyphicon glyphicon-remove',
            validating: 'glyphicon glyphicon-refresh'
        },
        fields: {${jsFields}
        }
    });
    $('form').bootstrapValidator().off('success.form.bv').on('success.form.bv', function(e) {
        // Prevent form submission
        e.preventDefault();
        $('form').ajaxSubmit({
            type: "post",
            url: addOrEditUrl,
            success: function(jsonData, statusText) {
                alert(jsonData.message);
                if (jsonData.success == true) {
                    goBack();
                }
            },
            error: function() {
                alert('网络异常，请刷新再试!');
            }
        });
    });
}

function goBack() {
    window.location = './list.html';
}