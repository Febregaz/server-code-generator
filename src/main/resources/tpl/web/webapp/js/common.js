(function($) {
    $.getUrlParam = function(name) {
        var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
        var r = window.location.search.substr(1).match(reg);
        if (r != null) return unescape(r[2]);
        return null;
    }
})(jQuery);
// 如果是主页addPathPrefix为false，否则，需要true，用来处理相对路径
function initLeft(addPathPrefix, focusPath) {
    var pathPrefix = '';
    if (addPathPrefix && addPathPrefix == true) {
        pathPrefix = '../';
    }
    //	$.getJSON(pathPrefix + 'user/listLeft.do', function(result) {
    $.getJSON(pathPrefix + 'permission/listLeft.do', function(result) {
        var html = '';
        for (var i = 0; i < result.length; i++) {
            if (result[i]['leaf'] == 1) { // 如果是叶子节点，
                var activeCls = (result[i]['path'] == focusPath) ? ' class="active"' : '';
                html = html + '<li ' + activeCls + '><a class="ajax-link" href="' + pathPrefix + result[i]['path'] + '"><i class="' +
                    result[i]['css'] + '"></i><span> ' + result[i]['name'] + '</span></a></li>';
            } else {
                html = html + '<li class="accordion"><a href="#"><i class="glyphicon glyphicon-chevron-down"></i><span> ' + result[
                    i]['name'] + '</span></a><ul class="nav nav-pills nav-stacked">';
                if (result[i]['children'] && result[i]['children'].length > 0) {
                    for (var j = 0; j < result[i]['children'].length; j++) {
                        var tmpNode = result[i]['children'][j];
                        var activeCls = (tmpNode['path'] == focusPath) ? ' class="active"' : '';
                        html = html + '<li ' + activeCls + '><a class="ajax-link" href="' + pathPrefix + tmpNode['path'] +
                            '"><i class="' + tmpNode['css'] + '"></i><span> ' + tmpNode['name'] + '</span></a></li>';
                    }
                }
                html = html + '</ul></li>';
            }
        }
        $('.sidebar-nav').find('.main-menu').eq(0).append(html);
        $('.accordion > a').click(function(e) {
            e.preventDefault();
            var $ul = $(this).siblings('ul');
            var $li = $(this).parent();
            if ($ul.is(':visible')) $li.removeClass('active');
            else $li.addClass('active');
            $ul.slideToggle();
        });
        $('.accordion li.active:first').parents('ul').slideDown();
    });
}
// 初始化导航栏的用户名称
function initNavUser(addPathPrefix) {
    var pathPrefix = '';
    if (addPathPrefix && addPathPrefix == true) {
        pathPrefix = '../';
    }
    //	$.get(pathPrefix + 'user/getName.do', null, function(data) {
    //		if(data) {
    //			data = data.replace(/\"/g, "");
    //			$('.navbar-user').append(data);
    //		}
    //	});
    $('.navbar-user').append('--');
}