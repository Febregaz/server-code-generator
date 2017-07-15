/**
 * @author songkun
 * @date 2017-05-05
 * Required: object-to-formdata,object-traverse,paging(js+css)
	url: '../ad/list.do',//请求url
	showColTool: true,//显示或者隐藏“列”按钮
	showNumber: true,//是否显示行号
	showPagination: true,//是否分页
	customToolHtml:''//自定的html元素，会在表格左上方显示，比如，自定义一些查询功能的输入框、按钮
	buttons: [{//右边自定义按钮，其中，“列”按钮是系统自带的，可以通过showColTool=false来隐藏
            iconCls: 'glyphicon glyphicon-plus',//按钮显示的icon
            title: '新增',//鼠标移上去显示的名字
            handler: function() {//点击按钮的事件
                window.location = './add.html';
            }
        }],
    onRowClick: function(rowIndex,rowData) {},// 单击行事件
    onCellClick: function(rowIndex,colIndex,field,rowData) {},// 单击单元格事件
	columns: [{//列信息
            field: "name",// 字段名字
            header: "名称",//显示的名字
            orderable: true,//是否需要点击“列头”排序，默认不能点击排序，点击列头，改变排序的顺序：none->asc->desc->none
            orderType: 'desc', //排序类型，orderable=true才有用，包含：asc(正序)/desc(倒序)/none(不排序)，默认none
            width: "100px",//列宽度,默认是自动宽
            hidden:false,//是否隐藏，true:隐藏，false:显示，默认显示
            exculdeVisible: true,//是否受“列”按钮控制，是否需要隐藏、显示的功能，true:表示永远显示/隐藏，默认false
            formatter: function(rowIndex, value, rowData) {//自定义显示的值
                return value;
            }
        }]
 */
"use strict";
(function($) {
    var pluginName = 'sk_grid';
    var btnIdPrefix = 'sk_grid_button_id_'; // 工具栏中按钮的ID前缀
    var cellBtnId = 'sk_grid_button_id_cell'; //工具栏中“列”按钮的ID
    var defauteSettings = {
        method: 'init', // 包含：init/reload,init:初始化grid,reload:重新加载grid
        showColTool: true, // 是否显示“列”控件
        showNumber: true, // 是否显示行号
        onRowClick: function(rowIndex, rowData) {}, // 单击行事件
        onCellClick: function(rowIndex, colIndex, field, rowData) {}, // 单击单元格事件
        showPagination: true, // 显示分页信息
        postData: { // ajax post body
            pageSize: 10,
            pageNumber: 1,
            orderBies: [] // 排序
        },
        noDataMsg: '数据为空'
    };
    //初始化
    var init = function(thiz) {
        var params = thiz.data(pluginName);
        if (!params.columns) {
            return;
        }
        if (!params.url && params.url == '') { // 如果没有url则报错
            return;
        }
        $.ajax({
            type: 'post', // post请求
            async: true,
            cache: false,
            contentType: false,
            processData: false,
            url: params.url,
            data: Object.toFormData(params.postData),
            success: function(datas) {
                params = thiz.data(pluginName);
                var tools = buildToolBar(params.customToolHtml, params.showColTool, params.buttons, params.columns);
                var dataGrid = buildDataGrid(params.columns, datas.rows, params.showNumber, params.noDataMsg)
                var pagination = '';
                var paginationId = generatePaginationId();
                if (params.showPagination) {
                    pagination = buildPagination(paginationId);
                }
                $(thiz).html('<div class="sk-grid">' + tools + dataGrid + pagination + '</div>');
                if (params.showPagination) {
                    initPagination(thiz, datas.totalRows, paginationId);
                }
                bindToolButtonsEvent(params.buttons);
                bindColToolEvent(thiz);
                bindColHeaderEvent(thiz);
                bindRowEvent(thiz, datas.rows);
                bindCellEvent(thiz, datas.rows);
                if (typeof(eval(params.callback)) == "function") {
                    params.callback(datas, 'init');
                }
            }
        });
    };
    // 重新加载
    var reload = function(thiz) {
        var params = thiz.data(pluginName);
        $.ajax({
            type: 'post', // post请求
            async: true,
            cache: false,
            contentType: false,
            processData: false,
            url: params.url,
            data: Object.toFormData(params.postData),
            success: function(datas) {
                params = thiz.data(pluginName);
                var dataGrid = buildDataGrid(params.columns, datas.rows, params.showNumber, params.noDataMsg)
                $(thiz).find('div.sk-grid-container-outer').html(dataGrid);
                bindColHeaderEvent(thiz)
                bindRowEvent(thiz, datas.rows);
                bindCellEvent(thiz, datas.rows);
                if (params.showPagination) {
                    params.pagination.render({
                        count: datas.totalRows,
                        pagesize: params.postData.pageSize,
                        current: params.postData.pageNumber
                    });
                }
                if (typeof(eval(params.callback)) == "function") {
                    params.callback(datas, 'reload');
                }
            }
        });
    };
    // 点击某行事件
    var bindRowEvent = function(thiz, rows) {
        $(thiz).find('div.sk-grid-container-outer').find('tr').off('click').on('click', function(e) {
            var params = thiz.data(pluginName);
            var rowIndex = $(this).index();
            if (typeof(eval(params.onRowClick)) == "function") {
                params.onRowClick(rowIndex, rows[rowIndex]);
            }
        });
    };
    // 点击某行事件
    var bindCellEvent = function(thiz, rows) {
        $(thiz).find('div.sk-grid-container-outer').find('tr').each(function(rowIndex) {
            var params = thiz.data(pluginName);
            $(this).find('td').off('click').on('click', function(e) {
                var params = thiz.data(pluginName);
                var cellIndex = $(this).index();
                if (typeof(eval(params.onCellClick)) == "function") {
                    params.onCellClick(rowIndex, cellIndex, getFieldName(cellIndex, params.showNumber, params.columns),
                        rows[rowIndex]);
                }
            });
        });
    };
    // 列头排序事件
    var bindColHeaderEvent = function(thiz) {
        $(thiz).find('div.sk-grid-container-outer').find('th').off('click').on('click', function(e) {
            var params = thiz.data(pluginName);
            var colIndex = $(this).index();
            colIndex = params.showNumber ? colIndex - 1 : colIndex;
            var showIndex = 0;
            var doReload = false; // 是否需要重新加载
            for (var tmpIndex = 0; tmpIndex < params.columns.length; tmpIndex++) {
                if (params.columns[tmpIndex].hidden) {
                    continue;
                }
                if (showIndex == colIndex) {
                    if (params.columns[tmpIndex].orderable) {
                        doReload = true; //需要重新加载
                        params.columns[tmpIndex].orderType = getNextOrderType(params.columns[tmpIndex].orderType);
                        resetOrderBies(params); // 重置排序列表
                    }
                    break;
                }
                showIndex = showIndex + 1;
            }
            if (doReload) {
                reload(thiz);
            }
        });
    };
    // 工具栏“列”按钮
    var bindColToolEvent = function(thiz) {
        $('.sk-grid-tool-bar-col input[type="checkbox"]').off('click').on('click', function(e) {
            var params = thiz.data(pluginName);
            var field = $(this).val();
            if ($.isArray(params.columns)) {
                for (var colIndex = 0; colIndex < params.columns.length; colIndex++) {
                    var col = params.columns[colIndex];
                    if (col.field == field) {
                        col.hidden = !this.checked;
                        if (!this.checked) { // 如果隐藏某列、则去掉排序
                            resetOrderBies(params);
                        }
                        break;
                    }
                }
            }
            reload(thiz);
        });
    };
    // 绑定事件
    var bindToolButtonsEvent = function(buttons) {
        if ($.isArray(buttons)) {
            for (var i = 0; i < buttons.length; i++) {
                var btn = buttons[i];
                if (btn.handler) {
                    $('#' + btnIdPrefix + i).click(btn.handler);
                } else {
                    $('#' + btnIdPrefix + i).click(function(e) {});
                }
            }
        }
    };
    // 初始化分页控件
    var initPagination = function(thiz, totalRows, paginationId) {
        var params = thiz.data(pluginName);
        var p = new Paging();
        p.init({
            target: '#' + paginationId,
            count: totalRows,
            pagesize: params.postData.pageSize,
            current: params.postData.pageNumber,
            toolbar: true,
            callback: function(page, size, count) {
                var params = thiz.data(pluginName);
                params.postData.pageNumber = page;
                params.postData.pageSize = size;
                thiz.data(pluginName, params);
                reload(thiz);
            }
        });
        params.pagination = p;
    };
    // 构造分页控件
    var buildPagination = function(paginationId) {
        return '<div class="sk-grid-pagination" id="' + paginationId + '"></div>';
    };
    // 构造分页控件
    var generatePaginationId = function() {
        var existCount = $('.sk-grid-pagination').length;
        if (existCount === 0 || existCount == null) {
            return 'sk-grid-pagination_1' + (existCount + 1);
        }
        return 'sk-grid-pagination_' + (existCount + 1);
    };
    // 构造数据表格
    var buildDataGrid = function(columns, rows, showNumber, noDataMsg) {
        var dataGrid = '<div class="sk-grid-container-outer"><table class="sk-grid-container" style="word-break:break-all">';
        dataGrid += buildGridHeader(columns, showNumber);
        if (rows && rows.length > 0) {
            dataGrid += buildGridBody(columns, rows, showNumber, noDataMsg);
        }
        dataGrid += '</table></div>';
        return dataGrid;
    };
    // 构造表内容
    var buildGridBody = function(columns, rows, showNumber, noDataMsg) {
        if (!columns || columns.length <= 0) {
            return '';
        }
        var bodyHtml = '<tbody class="sk-grid-content-body">';
        if (rows && rows.length > 0) {
            var showCount = calcShowColCount(columns, showNumber);
            for (var rowIndex = 0; rowIndex < rows.length; rowIndex++) {
                var row = rows[rowIndex];
                bodyHtml = bodyHtml + '<tr class="sk-grid-content-row">';
                var colIndex = showNumber ? 1 : 0;
                if (showNumber) { // 是否显示行号
                    bodyHtml += '<td class = "' + getCellClass(rows.length, rowIndex, showCount, 0) + '" > ' + (rowIndex + 1) +
                        ' </td>';
                }
                for (var colIndex = 0; colIndex < columns.length; colIndex++) {
                    var col = columns[colIndex];
                    var width = (rowIndex == 0 && col.width) ? ' style="width:' + col.width + '"' : '';
                    if (col.hidden) {
                        continue;
                    }
                    var formatterResult = '';
                    if (typeof(eval(col.formatter)) == "function") {
                        formatterResult = col.formatter(rowIndex, row[col.field], row);
                    } else if (row[col.field] != null) {
                        formatterResult = row[col.field];
                    }
                    if (formatterResult === undefined || formatterResult == null || formatterResult == '') {
                        formatterResult = '&nbsp;';
                    }
                    bodyHtml += '<td class="' + getCellClass(rows.length, rowIndex, showCount, showNumber ? colIndex + 1 : colIndex) +
                        '"' + width + '>' + formatterResult + '</td>';
                }
                bodyHtml = bodyHtml + '</tr>';
            }
        } else {
            bodyHtml = bodyHtml + '<tr><td>' + noDataMsg + '</td></tr>'
        }
        bodyHtml = bodyHtml + '</tbody>';
        return bodyHtml;
    };
    // 获取某单元格的class
    var getCellClass = function(totalRow, rowIndex, totalCol, colIndex) {
        var clazz = 'sk-grid-content-col-common';
        if (colIndex == 0) {
            clazz += ' sk-grid-content-col-left';
        }
        if (totalRow == rowIndex + 1) { // 如果是最末尾一行
            if (colIndex == 0) {
                clazz += ' sk-grid-content-col-bottom-left';
            }
            if (totalCol == colIndex + 1) {
                clazz += ' sk-grid-content-col-bottom-right';
            }
        }
        return clazz;
    };
    // 构造表头
    var buildGridHeader = function(columns, showNumber) {
        if (!columns || columns.length <= 0) {
            return '';
        }
        var headerHtml = '<thead><tr>';
        if (showNumber) {
            headerHtml += '<th class="sk-grid-content-header-left">NO.</th>';
        }
        var showCount = calcShowColCount(columns, showNumber);
        var index = showNumber ? 1 : 0;
        for (var i = 0; i < columns.length; i++) {
            var col = columns[i];
            if (col.hidden) {
                continue;
            }
            var orderByFragment = getOrderByFragment(col);
            if (showCount == 1) { // 只有一列
                headerHtml += '<th class="sk-grid-content-header-left sk-grid-content-header-right">' + col.header +
                    orderByFragment + '</th>';
            } else { // 多列
                if (index == 0) {
                    headerHtml += '<th class="sk-grid-content-header-left">' + col.header + orderByFragment + '</th>';
                } else if (index == showCount - 1) {
                    headerHtml += '<th class="sk-grid-content-header-right">' + col.header + orderByFragment + '</th>';
                } else {
                    headerHtml += '<th class="sk-grid-content-header-common">' + col.header + orderByFragment + '</th>';
                }
            }
            index++;
        }
        headerHtml = headerHtml + '</tr></thead>';
        return headerHtml;
    };
    // 获取排序代码片段
    var getOrderByFragment = function(col) {
        if (!col || !col.orderable || !col.orderType) {
            return '';
        }
        if ('asc' == col.orderType) {
            return '<span class="glyphicon glyphicon-chevron-up"></span>';
        } else if ('desc' == col.orderType) {
            return '<span class="glyphicon glyphicon-chevron-down"></span>';
        }
        return '';
    };
    // 获取下一个排序类型
    var getNextOrderType = function(type) {
        // 'none' -> 'asc' -> 'desc' -> 'none'
        if (!type || type == 'none') {
            return 'asc';
        }
        if (type == 'asc') {
            return 'desc';
        }
        if (type == 'desc') {
            return 'none';
        }
    };
    // 计算需要显示的列的数量
    var calcShowColCount = function(columns, showNumber) {
        var showCount = showNumber ? 1 : 0;
        if (columns && columns.length > 0) {
            for (var i = 0; i < columns.length; i++) {
                if (columns[i].hidden) {
                    continue;
                }
                showCount = showCount + 1;
            }
        }
        return showCount;
    };
    // 获取某一列的field名
    var getFieldName = function(colIndex, showNumber, columns) {
        if (showNumber && colIndex <= 0) { // 如果是点击
            return '';
        }
        var colIndex = showNumber ? colIndex - 1 : colIndex;
        var field = '';
        if (columns && columns.length > 0) {
            for (var i = 0; i < columns.length; i++) {
                if (columns[i].hidden) {
                    continue;
                }
                if (colIndex == i) {
                    field = columns[i].field;
                    break;
                }
            }
        }
        return field;
    };
    // 构造工具栏
    var buildToolBar = function(customToolHtml, showColTool, buttons, columns) {
        if (!showColTool && (!buttons || buttons.length <= 0) && !customToolHtml) {
            return '';
        }
        if (!customToolHtml) {
            customToolHtml = '';
        }
        var toolBar = '<div class="sk-grid-tool-bar">';
        // custom html bar
        toolBar += '<div class="sk-grid-tool-bar-custom pull-left">' + customToolHtml + '</div>';
        // buttons + columns button begin
        toolBar += '<div class="sk-grid-tool-bar-system">';
        // columns button
        if (showColTool) {
            toolBar += buildColToolButton(columns);
        }
        // buttons 
        toolBar += buildButtons(buttons);
        // buttons + columns button end
        toolBar += '</div>';
        // custom buttons end
        toolBar += '<div class="clear-both"></div></div>';
        return toolBar;
    };
    // 构造工具栏按钮
    var buildButtons = function(buttons) {
        var toolButtons = '';
        if ($.isArray(buttons)) {
            for (var i = 0; i < buttons.length; i++) {
                var btn = buttons[i];
                toolButtons += '<div class="sk-grid-tool-bar-system-wrapper pull-right">';
                toolButtons += '<button type="button" id="' + (btnIdPrefix + i) + '" class="sk-grid-tool-bar-tool" title="' + btn.title +
                    '">';
                toolButtons += "<i class=\"" + btn.iconCls + "\"></i>";
                if (btn.text && btn.text.length > 0) {
                    toolButtons += '<span>' + btn.text + '</span>';
                }
                toolButtons += '</button> ';
                toolButtons += '</div>';
            }
        }
        return toolButtons;
    };
    // 构造“列”按钮
    var buildColToolButton = function(columns) {
        var toolBar = '<div class="sk-grid-tool-bar-system-wrapper dropdown pull-right">';
        toolBar += '<button type="button" id="' + cellBtnId +
            '" class="sk-grid-tool-bar-tool dropdown-toggle" data-toggle="dropdown" title="列">';
        toolBar += '<i class="glyphicon glyphicon-align-justify"></i>';
        toolBar += '</button>';
        toolBar += '<ul class="dropdown-menu" aria-labelledby="dropdownMenu1">';
        for (var colIndex = 0; colIndex < columns.length; colIndex++) {
            var col = columns[colIndex];
            if (col.exculdeVisible) { // 如果不受【隐藏/显示】下拉按钮控制
                continue;
            }
            toolBar += '<li class="sk-grid-tool-bar-col"><label><input ' + (col.hidden ? '' : 'checked="checked"') +
                ' type="checkbox" value="' + col.field + '"/>' + col.header + '</label></li>';
        }
        toolBar += '</ul>';
        toolBar += '</div>';
        return toolBar;
    };
    // 重置orderBy数组
    var resetOrderBies = function(params) {
        var orderBies = [];
        var columns = params.columns;
        if (columns && $.isArray(columns)) {
            for (var colIndex = 0; colIndex < columns.length; colIndex++) {
                var col = columns[colIndex];
                if (col.hidden || !col.orderable) { // 隐藏或者禁止排序，跳过
                    continue;
                }
                if (col.orderType && (col.orderType == 'asc' || col.orderType == 'desc')) {
                    var orderBy = {};
                    orderBy.field = col.field;
                    orderBy.type = col.orderType == 'asc' ? 0 : 1;
                    orderBies.push(orderBy);
                }
            }
        }
        if (params.postData.orderBies && params.postData.orderBies.length > 0) {
            var oldOrders = [];
            for (var indexI = 0; indexI < params.postData.orderBies.length; indexI++) { // 为了保留顺序
                for (var indexJ = 0; indexJ < orderBies.length; indexJ++) {
                    if (params.postData.orderBies[indexI].field == orderBies[indexJ].field) {
                        oldOrders.push(orderBies[indexJ]);
                        break;
                    }
                }
            }
            var newOrders = [];
            for (var indexI = 0; indexI < orderBies.length; indexI++) { // 追加新的排序
                var has = false; //是否已经处理过了，如果是老的排序对象，则跳过
                for (var indexJ = 0; indexJ < oldOrders.length; indexJ++) {
                    if (orderBies[indexI].field == oldOrders[indexJ].field) {
                        has = true;
                        break;
                    }
                }
                if (!has) { //如果是老的排序对象，则跳过
                    newOrders.push(orderBies[indexI]);
                }
            }
            params.postData.orderBies = oldOrders.concat(newOrders); // 老的排序与新的排序合并
        } else {
            params.postData.orderBies = orderBies;
        }
    };
    // 初始化参数
    var initParams = function(thiz, params) {
        var oldParams = thiz.data(pluginName);
        if (typeof oldParams === "undefined") {
            params = $.extend(true, {}, defauteSettings, params);
        } else {
            params = $.extend(true, {}, oldParams, params);
        }
        resetOrderBies(params);
        thiz.data(pluginName, params);
    };
    $.fn.sk_grid = function(params) {
        var thiz = this;
        initParams(thiz, params);
        var params = thiz.data(pluginName);
        // Method calling logic
        if (params.method == 'reload') {
            return reload(thiz);
        } else if (params.method == 'init') {
            return this.each(function() {
                init(thiz);
            });
        } else {
            $.error("Method " + params + " does not exist on jQuery." + pluginName);
        }
    };
})(jQuery);