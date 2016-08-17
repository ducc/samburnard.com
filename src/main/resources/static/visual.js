$(document).ready(function() {
    // navigation
    var navToggle = $('#nav-toggle');
    var navMenu = $('#nav-menu');

    navToggle.click(function() {
        $(this).toggleClass('is-active');
        navMenu.toggleClass('is-active');
    });

    // modals
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

    // add another image button
    $("#add-image").click(function() {
        $("#item").clone().appendTo("#thumbnail-inputs");
    });
});