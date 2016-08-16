$(document).ready(function() {
    $(".modal-item").click(function() {
        var item = $(this);
        var modal = $(item.attr("data-toggle"));
        $('html').addClass('is-clipped');
        modal.addClass("is-active");
    });

    $('.modal-background, .modal-close').click(function() {
        $('html').removeClass('is-clipped');
        $(this).parent().removeClass('is-active');
    });
});