$(document).ready(function(){
    var mobileImage = $('.banner1')[0];
    var productImage = $('.banner2')[0];
    mobileImage.homePos = { x: mobileImage.offsetLeft, y: mobileImage.offsetTop };
    productImage.homePos = { x: productImage.offsetLeft, y: productImage.offsetTop };
    $('.banner2').mousemove(function (e) {
        parallax(e, mobileImage, 50);
        parallax(e, productImage, 50);
    });

    function parallax(e, target, layer) {
        var x = target.homePos.x - (e.pageX - target.homePos.x) / layer;
        var y = target.homePos.y - (e.pageY - target.homePos.y) / layer;
        $(mobileImage).css('position','relative');
        $(target).css({'top': y, 'left' : x });
    }
});
