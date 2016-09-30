$(function(){

    var showNotify = function(response) {
        window.location.reload();
    };

    var $table = $('#account-table');

    $table.on('click', 'a[data-role=delete-account]', function() {
        var $trigger = $(this);
        var target = $trigger.data('url');
        var title = $trigger.attr("title");

        if (confirm('确定' + title + ' 吗？')) {
            $.post(target).success(showNotify)
        };

    });

});