jQuery(document).ready(function () {
	var scroll_timer;
	var displayed = false;
	var $message = jQuery('#scrollToTopMultiMessageUnit a');
	var $window = jQuery(window);
	var top = jQuery(document.body).children(0).position().top;
	$window.scroll(function () {
		window.clearTimeout(scroll_timer);
		scroll_timer = window.setTimeout(function () {
			if($window.scrollTop() <= top)
			{
				displayed = false;
				$message.fadeOut(500);
			}
			else if(displayed == false)
			{
				displayed = true;
				$message.stop(true, true).show().click(function () { $message.fadeOut(500); });
			}
		}, 100);
	});
});




