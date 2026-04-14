/**
 * Tomcat自動復帰＆通信制御スクリプト（jQuery版）
 */
$(function() {
	window._monitorTimerId = null; // 各画面のタイマーIDを格納（共通で管理）
	let isMonitoring = false;      // 監視二重起動防止フラグ
	const CHECK_INTERVAL = 5000;   // 復帰確認の間隔（5秒）

	// --- 待機画面の表示 ---
	function showRecoveryOverlay(title, message) {
		if ($('#recovery-overlay').length > 0) return;
        
		const $overlay = $('<div>', { id: 'recovery-overlay' })
			.html(`
				<div class="recovery-content">
					<h1>${title}</h1>
					<p>${message}</p>
					<div class="loader"></div>
				</div>
			`);
		$('body').append($overlay);
	}
    
	// --- 一時的なエラーメッセージの表示 ---
	function showTemporaryMessage(message) {
		const id = "temp-error-msg";
		if ($('#' + id).length > 0) return; // 重複防止

		const $msg = $('<div>', { id: id }).text(message);	
		$('body').append($msg);

		// 2秒後に自動で消す（0.5秒かけてフェードアウト）
		$msg.delay(2000).fadeOut(500, () => $msg.remove());
	}

	// --- サーバー復帰の監視 ---
	function startHealthCheck() {
		if (isMonitoring) return;
		isMonitoring = true;

		// 各画面のタイマーをここで確実に止める
		if (window._monitorTimerId !== null) {
			clearInterval(window._monitorTimerId);
			window._monitorTimerId = null;
			console.warn("Monitor: Ajax通信タイマーを停止しました。");
		}

		console.warn("Monitor: サーバー停止を検知。監視を開始します。");

		const timer = setInterval(function() {
			console.log("Monitor: 復帰確認中...");

			$.ajax({
				url: '/', 
				type: 'HEAD',
				cache: false,
				global: false,
				// success だけでなく、完了(complete)時の中身で判定する
				complete: function(xhr) {
					// statusが0以外なら、サーバーは「応答」している（200OKや404など）
					if (xhr.status > 0) {
						console.log("Monitor: 復帰を検知しました！ (Status: " + xhr.status + ")");
						clearInterval(timer);
						location.reload();
					} else {
						console.log("Monitor: まだ応答がありません (Status: " + xhr.status + ")");
					}
				}
			});
		}, CHECK_INTERVAL);
	}

	// --- Ajaxエラーハンドラ ---
	$(document).ajaxError(function(event, xhr, settings) {
		if (xhr.status === 0) {
			// Tomcat停止時（接続拒否）
			showRecoveryOverlay("サーバー再起動中", "通信エラーを検知しました。<br>復帰後、自動的に画面を更新します。");
			startHealthCheck();
		} else if (xhr.status >= 500) {
			// サーバー内部エラー時（Java側の例外など）
			showTemporaryMessage("システムエラー：一時的なエラーが発生しました。システム管理者へお知らせください。");

			// サーバーから返されたJSON
			const errorData = xhr.responseJSON;
			if (errorData) {
				// JSONの中身をすべてコンソールに出力
				console.dir(errorData);

				// 特定のメッセージを表示したい場合
				if (errorData.message) {
					console.warn("Error Message:", errorData.message);
				}
			} else {
				// JSONが返ってこなかった場合のフォールバック
				console.log("Response Text:", xhr.responseText);
				console.log("HTTP Status:", xhr.status);
			}
			return;
		}
	});
	console.log("Monitor: 起動完了（正常稼働中）");
});

function furyoinshi_ajax() {

	$.ajax({
		url: '/furyoinshi_ajax',
		method: 'GET',
		success: function(res) {
		
			// evacuationActiveフラグがtrueならSweetAlertを表示
			if (res.evacuationActive) {
				showEvacuationAlert(data.imagePath, data.message);
				return; // 処理終了
			}
			
			$("#totalQuality").text(res.totalQuality);
			$("#generalQuality").text(res.generalQuality);
			$("#floorQuality").text(res.floorQuality);
			
			// current
			$(".current-status")
				.text(res.current.setting)
				.attr("class", "current-status " + res.current.settingClass);
			
			$(".current-code").text(res.current.material);
			$(".current-size").text(res.current.size);
			$(".stat-value").eq(0).text(res.current.production);
			$(".stat-value").eq(1).text(res.current.quality);
			
			// 不良因子
			renderDefects(res.defects);
			
			renderHistory(res.history);
		}
	});
}

// 不良因子
function renderDefects(data) {
    const $root = $("#defect-root").empty();

    let col = 1, row = 1, prevType = null;

    data.forEach(item => {
        if (row > 3 || (prevType && item.type !== prevType)) {
            col++;
            row = 1;
        }
        if (col > 12) return;

        $root.append(`
        <div class="defect-card" style="grid-column:${col};grid-row:${row}">
            <div class="defect-label ${item.type === 'B' ? 'b-label' : 'd-label'}">
            ${item.name}
            </div>
            <div class="defect-count">${item.count}</div>
        </div>
        `);

        prevType = item.type;
        row++;
    });
}

function renderHistory(rows) {
	const tbody = $('#daily-table-body');
	tbody.empty();
	
	rows.forEach(row => {
		const tr = $('<tr>');
		tr.append($('<td>').text(row.setting));
		tr.append($('<td>').text(row.material));
		tr.append($('<td>').text(row.size));
		tr.append($('<td>').text(row.count));
		tr.append($('<td>').text(row.quality).attr('data-flg', row.flg));
		tbody.append(tr);
	});
}

function sendEmail() {
	$.ajax({
		url: '/sendEmail',
		method: 'GET',
		success: function() {
			return false;
		}
	});
}