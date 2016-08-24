$(document).ready(function() {
    // carousel body background
    var data = JSON.parse($("#carousel-data").text());
    var images = data.images;
    var pointer = 1; // set to 1 because the body background is set to images[0] by default
    setInterval(function() {
        var image = images[pointer];
        $("body").css("background-image", "url(" + image + ")");
        pointer++;
        if (pointer >= images.length) {
            pointer = 0;
        }
    }, 5000); // 5s delay between carousel images
});