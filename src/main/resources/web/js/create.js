(function() {

/**
 * Global functions
 */

FAST.showCreate = function(stateData) {

	var html = '<legend>Create League</legend>';
	html += buildCreatePage(stateData);

	$('#mainContainer').html(html);

};

/**
 * Local functions
 */

var categoryTypes = {
	P: 'Pitching',
	B: 'Batting'
};

var buildCreatePage = function(stateData) {

	var html = buildSupportedList();

	return html;

};

var buildSupportedList = function() {

	var html = '' +
	'<div class="inline-block-top">' +
		'<ul class="nav nav-list pull-left supported-leagues">' +
			'<li class="nav-header">Supported Leagues</li>' +
			'<li>ESPN H2H Points</li>' +
			'<li class="nav-header">League Info</li>' +
			buildInput() +
		'</ul>' +
	'</div>' +
	'<div id="progress" class="inline-block-top">' +
		'Please enter a league ID and year that you want to retrieve league data for.' +
	'</div>' +
	'<div id="progressSummary" class="inline-block-top pull-right progress-summary">' +
		buildSummarySteps() +
	'</div>';

	return html;

};

var buildInput = function() {

	var html = '' +
	'<div class="form-horizontal create-input">' +
		'<div class="control-group">' +
			'<label class="control-label" for="inputLeagueID">League ID</label>' +
			'<div class="controls">' +
				'<input type="text" id="inputLeagueID" class="input-small">' +
			'</div>' +
		'</div>' +
		'<div class="control-group">' +
			'<label class="control-label" for="inputYear">Year</label>' +
			'<div class="controls">' +
				'<input type="text" id="inputYear" class="input-small">' +
			'</div>' +
		'</div>' +
		'<div class="control-group">' +
			'<div class="controls">' +
				'<button class="btn btn-primary" id="createLeague">Create</button>' +
			'</div>' +
		'</div>' +
	'</div>';

	return html;

};

var buildSummarySteps = function() {

	var html = '' +
		buildSummaryStep(1, 'Basic League Info') +
		buildSummaryStep(2, 'Create Categories') +
		buildSummaryStep(3, 'Create Teams') +
		buildSummaryStep(4, 'Determine Schedule') +
		buildSummaryStep(5, 'Setting Lineups') +
		buildSummaryStep(6, 'Retrieve Stats') +
		'<div class="step-summary">' +
			'<p><h3>READ THIS:</h3></p>' +
			'<p>Step 5 and 6 take a long time. This is because it must scrape all the data from ESPN\'s website.</p>' +
			'<p>This means roster data for each team on each day (step 5) and statistics for <i>all</i> players for each day.</p>' +
			'<p>When it\'s scraped, it also must be stored in a database. This process can take time, so grab a drink.</p>' +
			'<p>For a completed league, I\'d estimate step 5 will take 2 minutes, and around 5 minutes for step 6. Pro-rate that if the league is not over yet.</p>' +
			'<p>You can monitor progress by clicking a link for the given step.</p>' +
			'<p style="color: #C83026">Do <strong>NOT</strong> navigate away while processing or your league data may be incomplete and cause errors.</p>' +
			'<p>If you do so accidentally, you can re-create it and purge what\'s there.</p>' +
		'</div>';

	return html;

};

var buildSummaryStep = function(stepNum, stepTitle) {

	var html = '' +
	'<div class="step-summary" id="stepSummary' + stepNum + '">' +
		'<span class="step-summary-label">' +
			'<a href="#step' + stepNum + '"><strong>Step ' + stepNum + ':</strong> ' + stepTitle + '</a>' +
		'</span>' +
		'<div class="step-checkbox incomplete">' +
			'<i class="icon-check-empty"></i>' +
		'</div>' +
	'</div>';

	return html;

};

var buildCreateSteps = function() {

	var html = '' +
		buildStep(1, 'Basic League Info') +
		buildStep(2, 'Create Categories') +
		buildStep(3, 'Create Teams') +
		buildStep(4, 'Determine Schedule') +
		buildStep(5, 'Setting Lineups') +
		buildStep(6, 'Retrieve Stats');

	return html;

};

var buildStep = function(stepNum, stepTitle) {

	var html = '' +
	'<section id="step' + stepNum + '">' +
		'<div class="create-step" id="step' + stepNum + '">' +
			'<div class="step-title">' +
				'<span class="step-collapse">&#x25BC</span> Step ' + stepNum + ': ' + stepTitle +
			'</div>' +
			'<div class="step-details"></div>' +
		'</div>' +
	'</section>';

	return html;

};

var checkLeague = function() {

	var fantasyID = parseInt($('#inputLeagueID').val(), 10);
	var year = parseInt($('#inputYear').val(), 10);

	// Step 1: Check if there's a league
	$.ajax({
		url: '/services/league',
		data: {
			fantasyID: fantasyID,
			year: year
		},
		success: function(data) {
			if (typeof data !== 'undefined') {
				confirmDelete(data);
			}
			else {
				createLeague(fantasyID, year);
			}
		}
	});

};

var createLeague = function(fantasyID, year) {

	$('#progress').html(buildCreateSteps());

	var args = {
		fantasyLeagueID: fantasyID,
		year: year,
		fantasyService: 'ESPN'
	};

	$.ajax({
		url: '/services/league' + FAST.createURLParams(args),
		type: 'POST',
		beforeSend: function() {
			stepLoading(1);
		},
		success: showLeague,
		error: FAST.ajaxError
	});

};

var showLeague = function(league) {

	var html = '' +
	'<b>Name:</b> ' + league.name + '<br>' +
	'<b>Year:</b> ' + league.year + '<br>' +
	'<b>Service:</b> ' + league.service;

	$('#step1').find('.step-details').html(html);

	doneLoading(1);

	createCategories(league);

};

var createCategories = function(league) {

	var args = {
		leagueID: league.id
	};

	$.ajax({
		url: '/services/category' + FAST.createURLParams(args),
		type: 'POST',
		beforeSend: function() {
			stepLoading(2);
		},
		success: function(categories) {
			showCategories(league, categories);
		},
		error: FAST.ajaxError
	});

};

var showCategories = function(league, categories) {

	var html = '' +
	'<table class="step-table">' +
		'<thead>' +
			'<tr>' +
				'<th>Type</th>' +
				'<th>Category</th>' +
				'<th>Points</th>' +
				'<th>Type</th>' +
				'<th>Category</th>' +
				'<th>Points</th>' +
			'</tr>' +
		'</thead>' +
		'<tbody>';

	var col = 1;
	for (var i = 0; i < categories.length; i++) {

		var category = categories[i];

		if (col === 1) html += '<tr>';

		html += '' +
		'<td>' + categoryTypes[category.categoryType] + '</td>' +
		'<td>' + category.category + '</td>' +
		'<td>' + category.points + '</td>';

		if (col === 2) {
			html += '</tr>';
			col = 0;
		}

		col++;
	}

	html += '</tbody></table>';

	$('#step2').find('.step-details').html(html);

	doneLoading(2);

	league.categories = categories;
	createTeams(league);

};

var createTeams = function(league) {

	var args = {
		leagueID: league.id
	};

	$.ajax({
		url: '/services/team' + FAST.createURLParams(args),
		type: 'POST',
		beforeSend: function() {
			stepLoading(3);
		},
		success: function(teams) {
			showTeams(league, teams);
		},
		error: FAST.ajaxError
	});

};

var showTeams = function(league, teams) {

	var html = '' +
	'<table class="step-table">' +
		'<thead>' +
			'<tr>' +
				'<th>Name</th>' +
				'<th>Owner</th>' +
			'</tr>' +
		'</thead>' +
		'<tbody>';

	for (var i = 0; i < teams.length; i++) {

		var team = teams[i];

		html += '' +
		'<tr>' +
			'<td>' + team.name + '</td>' +
			'<td>' + team.owner + '</td>' +
		'</tr>';

	}

	html += '</tbody></table>';

	$('#step3').find('.step-details').html(html);

	doneLoading(3);

	league.teams = teams;
	createSchedule(league);

};

var createSchedule = function(league) {

	var args = {
		leagueID: league.id
	};

	$.ajax({
		url: '/services/week' + FAST.createURLParams(args),
		type: 'POST',
		beforeSend: function() {
			stepLoading(4);
		},
		success: function(weeks) {
			showSchedule(league, weeks);
		},
		error: FAST.ajaxError
	});

};

var showSchedule = function(league, weeks) {

	var html = '';

	var teams = getTeamArray(league);

	for (var i = 0; i < weeks.length; i++) {

		var week = weeks[i];

		html += '' +
		'<div class="week">' +
			'<div class="week-title">' +
				'<i class="icon-caret-right"></i> ' + 
				FAST.getFormattedDate(week.startPeriod.date) + ' to ' + FAST.getFormattedDate(week.endPeriod.date) +
			'</div>' +
			'<div class="week-matchups">';

		for (var j = 0; j < week.matchups.length; j++) {
			var matchup = week.matchups[j];
			html += teams[matchup.awayTeamID].name + ' @ ' + teams[matchup.homeTeamID].name + '<br>';
		}

		if (week.matchups.length === 0) {
			html += 'Playoffs!';
		}

		html += '</div></div>';

	}

	$('#step4').find('.step-details').html(html);

	doneLoading(4);

	league.weeks = weeks;

	createLineups(league);

};

var getTeamArray = function(league) {

	var teams = {};
	for (var k = 0; k < league.teams.length; k++) {
		var team = league.teams[k];
		teams[team.fantasyTeamID] = team;
	}

	return teams;

};

var createLineups = function(league) {

	var args = {
		leagueID: league.id
	};

	$.ajax({
		url: '/services/period/max',
		type: 'GET',
		data: args,
		beforeSend: function() {
			stepLoading(5, true);
		},
		error: FAST.ajaxError
	})
	.then(function(period) {

		var html = '';

		var weeks = [];
		for (var i = 0; i < league.weeks.length; i++) {

			var week = league.weeks[i];
			if (week.startPeriod.date > period.date) break;
			weeks.push(week);

			html +=
			'<div class="week">' +
				'<div class="week-title">' +
					'<i class="icon-check-empty unavailable" id="roster-week-' + week.id + '"></i> ' +
					FAST.getFormattedDate(week.startPeriod.date) + ' to ' + FAST.getFormattedDate(week.endPeriod.date) +
				'</div>' +
				'<div class="week-matchups" style="display: none">' +
				'</div>' +
			'</div>';

		}

		$('#step5').find('.step-details').html(html);

		var weekNum = 0;
		var createRoster = function() {

			var args = {
				weekID: weeks[weekNum].id,
				maxPeriodID: period.periodID
			};

			$.ajax({
				url: '/services/week/roster' + FAST.createURLParams(args),
				type: 'POST',
				beforeSend: function() {

					$('i#roster-week-' + args.weekID).removeClass();
					$('i#roster-week-' + args.weekID).addClass('icon-refresh');
					$('i#roster-week-' + args.weekID).addClass('icon-spin');

				},
				success: function() {

					$('i#roster-week-' + args.weekID).removeClass();
					$('i#roster-week-' + args.weekID).addClass('icon-check');

					weekNum++;
					if (weekNum >= weeks.length) {
						doneLoading(5);
						createStats(league, weeks, period);
					}
					else {
						createRoster();
					}

				},
				error: FAST.ajaxError
			});
		};

		createRoster();

	});

};

var createStats = function(league, weeks, maxPeriod) {

	stepLoading(6, true);

	var html = '';
	for (var i = 0; i < weeks.length; i++) {

		html += '' +
		'<div class="week" id="stats-week-' + weeks[i].id + '">' +
			'<div class="week-title">' +
				'<i class="icon-caret-right unavailable"></i> ' +
				FAST.getFormattedDate(weeks[i].startPeriod.date) + ' to ' + FAST.getFormattedDate(weeks[i].endPeriod.date) +
			'</div>' +
			'<div class="week-matchups" style="display: none">' +
			'</div>' +
		'</div>';

	}

	$('#step6').find('.step-details').html(html);

	var teams = getTeamArray(league);
	var weekNum = 0;
	var retrieveStats = function() {

		var args = {
			weekID: weeks[weekNum].id,
			maxPeriodID: maxPeriod.periodID
		};

		$.ajax({
			url: '/services/player/results/week' + FAST.createURLParams(args),
			type: 'POST',
			beforeSend: function() {

				var collapse = $('#stats-week-' + args.weekID).find('i');
				$(collapse).removeClass();
				$(collapse).addClass('icon-refresh');
				$(collapse).addClass('icon-spin');
				$(collapse).addClass('unavailable');

			},
			success: function(week) {

				var detailHTML = '';
				for (var j = 0; j < week.matchups.length; j++) {
					var matchup = week.matchups[j];
					detailHTML += '' +
						teams[matchup.awayTeamID].name + ' <strong>(' + matchup.awayTeamScore + ')</strong> @ ' +
						teams[matchup.homeTeamID].name + ' <strong>(' + matchup.homeTeamScore + ')</strong><br>';
				}
				$('#stats-week-' + args.weekID).find('.week-matchups').html(detailHTML);

				var collapse = $('#stats-week-' + args.weekID).find('i');
				$(collapse).removeClass();
				$(collapse).addClass('icon-caret-right');

				weekNum++;
				if (weekNum >= weeks.length) {
					doneLoading(6);
				}
				else {
					retrieveStats();
				}

			},
			error: FAST.ajaxError
		});

	};

	retrieveStats();

};

var stepLoading = function(stepNum, isLong) {

	var longStep = (typeof isLong !== 'undefined' && isLong) ? ' (this step takes a while)' : '';

	$('#step' + stepNum).find('.step-details').html('<img src="img/loading.gif"> Retrieving...' + longStep);

	var icon = $('#stepSummary' + stepNum).find('i').first();
	$(icon).removeClass();
	$(icon).addClass('icon-refresh');
	$(icon).addClass('icon-spin');
	$(icon).parent().removeClass('incomplete');
	$(icon).parent().addClass('in-progress');

};

var doneLoading = function(stepNum) {

	var icon = $('#stepSummary' + stepNum).find('i').first();
	$(icon).removeClass();
	$(icon).addClass('icon-check');
	$(icon).parent().removeClass('in-progress');
	$(icon).parent().addClass('complete');

};

var confirmDelete = function(league) {

	var html = '' +
	'<div class="modal-header">' +
		'<button type="button" class="close" data-dismiss="modal">&times;</button>' +
		'<h3>League already exists!</h3>' +
	'</div>' +
	'<div class="modal-body">' +
		'The name of this league is ' + league.name + '.' +
		'<p>Would you like to delete what\'s there and re-create the league?' +
	'</div>' +
	'<div class="modal-footer">' +
		'<button class="btn btn-danger">Nevermind...</button>' +
		'<button class="btn btn-success" data-league="' + JSON.stringify(league).replace(/"/g, '&quot;') + '">Re-create!</button>' +
	'</div>';

	$('#confirmModal').html(html);
	$('#confirmModal').modal('show');

};

var reCreateLeague = function() {

	clearModal();
	var league = JSON.parse($(this).attr('data-league'));

	$.ajax({
		url: '/services/league/leagueID/' + league.id,
		type: 'DELETE',
		dataType: 'text',
		beforeSend: function() {
			$('#progress').html('<img src="img/loading.gif"> Deleting "' + league.name + '" ' + league.year + '...');
		},
		data: {
			leagueID: league.id
		},
		success: function() {
			createLeague(league.fantasyID, league.year);
		}
	});

};

var clearModal = function() {
	$('#confirmModal').modal('hide');
};

var toggleWeekDisplay = function(event) {

	var details = $(this).parent().find('.week-matchups').first();
	var collapse = $(this).find('i');

	if ($(collapse).hasClass('unavailable')) return;

	if ($(details).is(':visible')) {
		$(collapse).removeClass();
		$(collapse).addClass('icon-caret-right');
		$(details).slideUp();
		return;
	}

	$(collapse).removeClass();
	$(collapse).addClass('icon-caret-down');
	$(details).slideDown();

};

var toggleStepDisplay = function(event) {

	var details = $(this).parent().find('.step-details').first();
	var collapse = $(this).find('.step-collapse');

	if ($(details).is(':visible')) {
		$(this).find('.step-collapse').html('&#x25BA');
		$(details).slideUp();
		return;
	}

	$(this).find('.step-collapse').html('&#x25BC');
	$(details).slideDown();

};

var addEvents = function() {

	$('#mainContainer').on('click', 'button#createLeague', checkLeague);
	$('#mainContainer').on('click', '.step-title', toggleStepDisplay);
	$('#mainContainer').on('click', '.week-title', toggleWeekDisplay);

	$('#confirmModal').on('click', 'button.btn-danger', clearModal);
	$('#confirmModal').on('click', 'button.btn-success', reCreateLeague);
	$('#confirmModal').on('hidden', $('#confirmModal').empty);

};

var init = function() {

	addEvents();

};

$(document).ready(init);

})();