$(document).ready(function() {
    init('../${modelVarName}/edit.do');
    $.getJSON('../${modelVarName}/getByKey.do?id=' + ($.getUrlParam('id')), function(result) {
        if (result) {${jsFieldSetters}
        } else {
            goBack();
        }
    });
});