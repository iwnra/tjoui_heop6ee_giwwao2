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

function furyoinshi_ajax() {
	$.ajax({
		url: '/furyoinshi_ajax',
		method: 'GET',
		success: function(res) {
		  $("#totalQuality").text(res.totalQuality);
		  $("#generalQuality").text(res.generalQuality);
		  $("#floorQuality").text(res.floorQuality);
		
		  // current
		  $(".current-status")
		     .text(res.current.settingLabel)
		     .attr("class", "current-status " + res.current.settingClass);
		
		  $(".current-code").text(res.current.material);
		  $(".current-size").text(res.current.size);
		  $(".stat-value").eq(0).text(res.current.production);
		  $(".stat-value").eq(1).text(res.current.quality);
		
		  // 不良因子
		  renderDefects(res.defects);
		  
		  renderHistory2(res.history);
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

function renderHistory2(rows) {
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
