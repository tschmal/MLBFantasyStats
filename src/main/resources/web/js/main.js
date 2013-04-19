(function() {

var logAjaxError = function(jqXHR, textStatus, errorThrown) {
	console.log('onoes! an error! ' + textStatus + ': ' + errorThrown);
};

var buildLeagueMenu = function(leagues) {
	var html = '';
	var seenLeagues = {};
	var leagueNames = {};
	for (var i = 0; i < leagues.length; i++) {
		var league = leagues[i];

		// Avoid putting the same league twice. We only need one record per fantasyID, and it should be the latest year.
		var seenLeague = seenLeagues[league.fantasyID];
		if (seenLeague && seenLeague.year > league.year)
			continue;
		else
			leagueNames[league.fantasyID] = league.name;
	}

	for (var fantasyID in leagueNames) {
		html += '<option data-fantasyID="' + fantasyID + '">' + leagueNames[fantasyID] + '</option>';
	}
	return html;
};

var createLeagueMenu = function(service) {
	$.ajax({
		url: '/services/league/service/' + service,
		type: 'GET',
		success: function(leagues) {
			$('#leagueMenu').html(buildLeagueMenu(leagues));
			if (leagues.length > 0)
				getYears();
		},
		error: logAjaxError
	});
};

var buildYearMenu = function(leagues) {
	var html = '';
	for (var i = 0; i < leagues.length; i++) {
		html += '<option data-leagueID="' + leagues[i].id + '">' + leagues[i].year + '</option>';
	}
	return html;
};

var createYearMenu = function(fantasyID) {
	$.ajax({
		url: '/services/league/fantasyID/' + fantasyID,
		type: 'GET',
		success: function(leagues) {
			$('#yearMenu').html(buildYearMenu(leagues));
		},
		error: logAjaxError
	});
};

var getLeagues = function() {
	$('#leagueMenu').empty();
	$('#yearMenu').empty();
	var service = $('#serviceMenu').find(":selected").text();
	createLeagueMenu(service);
};

var getYears = function() {
	var selectedFantasyID = $('#leagueMenu').find(':selected').first().attr('data-fantasyID');
	createYearMenu(selectedFantasyID);
};

var addEvents = function() {
	$('.league-menu').on('change', '#serviceMenu', getLeagues);
	$('.league-menu').on('change', '#leagueMenu', getYears);
};

var init = function() {
	addEvents();
	getLeagues();
};

$(document).ready(init);
	
})();