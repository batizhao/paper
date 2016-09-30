$(document).on('hidden.bs.modal', '.modal', function () {
    $(this).removeData('bs.modal');
});

$('.modal').on('click', '[data-toggle=form-submit]', function(e) {
    e.preventDefault();
    $($(this).data('target')).submit();
});

$(document).on('click', 'button[data-loading-text]', function(e) {
    var btn = $(this).button('loading');
    setTimeout(function () {
        btn.button('reset');
    }, 2000);
});

var showMessage = function(type, message) {
    $.gritter.add({
        title: '通知',
        text: message,
        class_name: type
    });
};

var Notify = {
    success: function(message) {
        showMessage('gritter-success', message);
    },

    warning: function(message) {
        showMessage('gritter-warning', message);
    },

    error: function(message) {
        showMessage('gritter-error', message);
    },

    info: function(message) {
        showMessage('gritter-info', message);
    }
};