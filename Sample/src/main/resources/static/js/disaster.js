$(function () {

    let active = false;

    $('.mode-card').click(function () {

        if (active) return;

        active = true;

        const selected = $(this);

        $('.mode-card').addClass('disabled');
        selected.removeClass('disabled');

        selected.find('.status').show();

        $('#releaseBtn').show();
        $('#notice').hide();
    });

    $('#releaseBtn').click(function () {

        active = false;

        $('.mode-card').removeClass('disabled');
        $('.status').hide();

        $('#releaseBtn').hide();
        $('#notice').show();
    });

});