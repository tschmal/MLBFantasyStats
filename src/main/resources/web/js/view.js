(function() {

/**
 * Global functions
 */

FAST.showView = function(stateData) {

	var html = '<legend>View League</legend>';

	$('#mainContainer').html(html);

	if (stateData.hasOwnProperty('leagueID') && stateData.hasOwnProperty('leagueYear')) {
		showLeagueView(state);
	}

	showLeagueSelect();

};

/**
 * Local functions
 */

var showLeagueView = function(state) {

	console.log('showing league view');

};

var showLeagueSelect = function() {

	console.log('showing league select');

};

var addEvents = function() {

};

var init = function() {

	addEvents();

};

$(document).ready(init);

})();