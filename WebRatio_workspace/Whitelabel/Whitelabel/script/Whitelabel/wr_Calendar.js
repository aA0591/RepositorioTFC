jQuery.fn.wr_Calendar = function (method) {

	var args = arguments;
	return this.each(function () {
		var $ = jQuery;
		var $this = jQuery(this);


		if (jQuery.fn.wr_Calendar.methods[method]) {
			return jQuery.fn.wr_Calendar.methods[method].apply(this, Array.prototype.slice.call(args, 1));
		} else if (typeof method === 'object' || !method) {
			if ($this.data('wr_Calendar')) {
				var opts = jQuery.extend({}, $this.data('wr_Calendar'), method);
			} else {
				var opts = jQuery.extend({}, jQuery.fn.wr_Calendar.defaults, method, $this.data());
			}
		} else {
			jQuery.error('Method "' + method + '" does not exist');
		}
		
		if (!$this.data('wr_Calendar')) {

			$this.data('wr_Calendar', {});

			var $calendar = $this.find('section table');
			var $details = $this.find('footer');
			var $anchorName = $details.find( 'a[name]' ).attr( 'name' );
			
			
			//$(window).resize(function() {
			
			if ($(window).width()<900) {
				
				$calendar.find( 'tbody td' ).click( function() {
					
					var $header = $( this ).find( '.date-header' );
					var $events = $( this ).find( '.date-events' );
					
					
					var $headerCloned = $header.clone();
					// Display full date
					var $dayOfMonth = $headerCloned.find( '.dayOfMonth' );
					$dayOfMonth.text( $dayOfMonth.data( 'fullDate' ) );
					var $eventsCloned = $events.clone();
					$eventsCloned.find('div.g6').removeClass( 'g6 omega alpha' )
					
					// Remove previous details
					$details.find( '.date-header' ).remove();
					$details.find( '.date-events' ).remove();
					$details.append( $headerCloned );
					$details.append( $eventsCloned );
					
					// Point to the details
					location.href = "#"+$anchorName;
				});
			}
				
			//});
		} else {
		}
		
		if (opts) jQuery.extend($this.data('wr_Calendar'), opts);

	});

};

jQuery.fn.wr_Calendar.defaults = {};
jQuery.fn.wr_Calendar.version = '1.0';


jQuery.fn.wr_Calendar.methods = {
};