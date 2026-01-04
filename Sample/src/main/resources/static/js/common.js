function updateHistory() {
	$.ajax({
		url: '/api/production/history', // 履歴取得API
		method: 'GET',
		success: function(data) {
			renderHistory(data.rows);
		}
	});
}

function renderHistory(rows) {
	const tbody = $('#history-body');
	tbody.empty();
	
	rows.forEach(row => {
		const tr = $('<tr>');
		tr.append($('<td>').text(row.sizeNo));
		tr.append($('<td>').text(row.thickness));
		tr.append($('<td>').text(row.width));
		tr.append($('<td>').text(row.length));
		tr.append($('<td>').text(row.material));
		tr.append($('<td>').text(row.count));
		tr.append($('<td>').text(row.productivity).attr('data-flg', row.flg));
		tbody.append(tr);
	});
}
