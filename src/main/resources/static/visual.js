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

    var images = -1;

    // add another image button
    $("#add-image").click(function() {
        if (images === -1) {
            if ($("#thumbnail-count").length) {
                images = $("#thumbnail-count").text();
            } else {
                images = 1;
            }
        }
        images++;
        var item = $("#item").clone();
        var elements = $(".add-image-input").toArray();
        $(elements[0]).attr("name", "image_thumbnail_" + images);
        $(elements[0]).val("");
        $(elements[1]).attr("name", "image_image_" + images);
        $(elements[1]).val("");
        item.appendTo("#thumbnail-inputs");
    });

    // about preview content
    $("#about-preview").click(function() {
        var text = $("#content").val();
        $("#preview").html(text);
    });
});