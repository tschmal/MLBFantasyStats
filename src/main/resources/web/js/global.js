(function() {

window.FAST = window.FAST || {};

FAST.title = 'Project FAST - Fantasy Stats and Analytics';

FAST.ajaxError = function(xhr, status, error) {
    console.log(xhr);
    console.log(status);
    console.log(error);
};

FAST.createURLParams = function(obj) {

	var params = '';

	for (var param in obj) {

		if (params.length === 0)
			params += '?';
		else
			params += '&';

		params += param + '=' + obj[param];
	}

	return params;

};

var months = [
	'January',
	'February',
	'March',
	'April',
	'May',
	'June',
	'July',
	'August',
	'September',
	'October',
	'November',
	'December'
];

FAST.getFormattedDate = function(time) {
	var date = new Date(time);
	return months[date.getMonth()] + ' ' + date.getDate();
};

})();