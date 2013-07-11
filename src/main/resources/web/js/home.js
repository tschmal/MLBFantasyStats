(function() {

/**
 * Local functions
 */

var addEvents = function() {

    $('ul.nav').on('click', 'a', FAST.navChanged);

};

var init = function() {

    addEvents();

};

$(document).ready(init);

})();