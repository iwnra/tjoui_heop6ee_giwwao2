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



$(function () {

    // =========================
    // 初期表示
    // =========================
    loadStatus();

    // =========================
    // ボタン押下イベント
    // =========================
    $('.disaster-btn').on('click', function () {
        const mode = $(this).data('mode');

        // 二重押下防止
        if ($(this).hasClass('disabled')) return;

        updateStatus(mode);
    });

    // 解除ボタン
    $('#btnRelease').on('click', function () {
        updateStatus('NONE');
    });

});


// =========================
// ステータス取得（初期＆更新後共通）
// =========================
function loadStatus() {

    $.ajax({
        url: '/getStatus',
        method: 'GET',
        cache: false
    })
    .done(function (res) {
        // res.status を想定
        renderUI(res.status);
    })
    .fail(function () {
        console.error('ステータス取得失敗');
    });
}


// =========================
// ステータス更新
// =========================
function updateStatus(mode) {

    $.ajax({
        url: '/updateStatus',
        method: 'POST',
        contentType: 'application/json',
        data: JSON.stringify({ status: mode })
    })
    .done(function () {
        // 更新後に再取得
        loadStatus();
    })
    .fail(function () {
        console.error('ステータス更新失敗');
    });
}


// =========================
// 画面描画
// =========================
function renderUI(status) {

    // 全ボタン初期化
    $('.disaster-btn')
        .removeClass('active disabled')
        .find('.alert-text').hide();

    // 解除ボタン非表示
    $('#btnRelease').hide();

    // 注意文表示
    $('#noticeText').show();

    if (status === 'NONE') {
        return;
    }

    // =========================
    // 災害モード時
    // =========================

    // 全ボタン非活性
    $('.disaster-btn').addClass('disabled');

    // 該当ボタン
    const $target = $('.disaster-btn[data-mode="' + status + '"]');

    $target.addClass('active');

    // 「! 災害モード中 !」表示（中央上）
    $target.find('.alert-text').show();

    // 解除ボタン表示
    $('#btnRelease').show();

    // 注意文非表示
    $('#noticeText').hide();
}