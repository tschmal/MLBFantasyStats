(function() {

/**
 * Local variables
 */

var pageFunctions = {
	navView: FAST.showView,
	navCreate: FAST.showCreate
};

/**
 * Global functions
 */

FAST.navChanged = function(event) {

	// Update the selected item.

	var clickedPageID = $(this).attr('id');
	var isNewPage = updateNav(clickedPageID);

	if (isNewPage) {
		var stateData = {pageID: clickedPageID};
		FAST.History.pushState(stateData, FAST.title, createURLForState(stateData));

		if (pageFunctions.hasOwnProperty(clickedPageID)) {
			pageFunctions[clickedPageID](stateData);
		}
	}

};

FAST.showPage = function(state) {

	var pageID = state.data.pageID;

	updateNav(pageID);

};

/**
 * Local functions
 */

var createURLForState = function(stateData) {

	var url = '?';

	for (var param in stateData) {
		if (url.length > 1) url += '&';
		url += param + '=' + stateData[param];
	}

	return url;

};

var updateNav = function(clickedPageID) {

	var isNewPage = false;

	$('ul.nav').children().each(function(i, listItem) {

		var pageID = $(listItem).children().first().attr('id');

		if (pageID === clickedPageID && !$(listItem).hasClass('active')) {
			$(listItem).addClass('active');
			isNewPage = true;
		}
		else if (pageID !== clickedPageID) {
			$(listItem).removeClass('active');
		}

	});

	return isNewPage;

};

var getURLParameters = function() {

	var search = window.location.search.substring(1);
	var params = search.split('&');
	var hash = {};

	for (var i = 0; i < params.length; i++) {
		if (params[i].length === 0) continue;
		var keyVal = params[i].split('=');
		hash[unescape(keyVal[0])] = unescape(keyVal[1]);
	}

	if (!hash.hasOwnProperty('pageID')) hash.pageID = 'navHome';

	return hash;

};

var beginHistory = function() {

	FAST.History = window.History;
	if (!FAST.History.enabled) {
		return;
	}

	FAST.History.Adapter.bind(window, 'statechange', function() {
		var State = FAST.History.getState();
		FAST.showPage(State);
	});

};

var setInitialState = function() {

	var stateData = getURLParameters();
	FAST.History.pushState(stateData, FAST.title, createURLForState(stateData));	

};

var init = function() {

	beginHistory();
	setInitialState();

};

$(document).ready(init);

})();