$(document).ready(function () {
    $('.table #delete_button_contact_form').on('click', function (event) {
        event.preventDefault();
        var href = $(this).attr('href');
        $('#modal_deletion_confirmation #button_submit_modal_window_deletion_confirmation').attr('href', href);
        $("#modal_deletion_confirmation").fadeIn("slow");
        $("#button_submit_modal_window_deletion_confirmation").click(function () {
            $(".overlay_deletion_confirmation").fadeOut("slow");
        });
        $(".modal_window_deletion_confirmation__close").click(function () {
            $(".overlay_deletion_confirmation").fadeOut("slow");
        });
    });
});
