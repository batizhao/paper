$(function(){

    var $form = $('#create-form');
    var $modal = $form.parents('.modal');

    var showNotify = function(response) {
        $modal.modal('hide');
        window.location.reload();
    };

    $form.validate({
        rules: {
            accountName: {
                required: true
            },
            course: {
                required: true
            },
            score: {
                required: true
            }
        },
        submitHandler: function(form, event) {
            event.preventDefault();
            $(form).ajaxSubmit({
                dataType : 'json',
                success: showNotify,
                error: function(data,textstatus){
                    $modal.modal('hide');
                    Notify.error("服务器内部错误，请稍后再试。");
                }
            });
        },
        errorPlacement: function(error, element) {
            error.appendTo(element.parent());
        },
        errorElement: "span",
        errorClass: "error"
    });
});