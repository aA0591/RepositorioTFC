/*----------------------------------------------------------------------*/
/* wl_Calendar v 1.0
/* description: makes a Calendar
/* dependency: fullcalendar plugin (calendar.js)
/*----------------------------------------------------------------------*/


jQuery.fn.wl_Calendar = function (method) {

	var args = arguments;
	return this.each(function () {

		var $this = jQuery(this);


		if (jQuery.fn.wl_Calendar.methods[method]) {
			return jQuery.fn.wl_Calendar.methods[method].apply(this, Array.prototype.slice.call(args, 1));
		} else if (typeof method === 'object' || !method) {
			if ($this.data('wl_Calendar')) {
				var opts = jQuery.extend({}, $this.data('wl_Calendar'), method);
			} else {
				var opts = jQuery.extend({}, jQuery.fn.wl_Calendar.defaults, method, $this.data());
			}
		} else {
			try {
				return $this.fullCalendar(method, args[1], args[2], args[3], args[4]);
			} catch (e) {
				jQuery.error('Method "' + method + '" does not exist');
			}
		}


		if (!$this.data('wl_Calendar')) {

			$this.data('wl_Calendar', {});

			//we need to use the jquery UI Theme
			opts.theme = true;

			//some shorties for the header, you can add more easily
			switch (opts.header) {
			case 'small':
				opts.header = {
					left: 'title',
					right: 'prev,next'
				};
				break;
			case 'small-today':
				opts.header = {
					left: 'title',
					right: 'prev,today,next'
				};
				break;
			case 'small-today-views':
				opts.header = {
					left: 'title',
					center: 'month,agendaWeek',
					right: 'prev,today,next'
				};
				break;
			default:
			}

			//call the fullCalendar plugin
			$this.fullCalendar(opts);


		} else {

		}

		if (opts) jQuery.extend($this.data('wl_Calendar'), opts);

	});

};

jQuery.fn.wl_Calendar.defaults = {};
jQuery.fn.wl_Calendar.version = '1.0';


jQuery.fn.wl_Calendar.methods = {
	set: function () {
		var $this = jQuery(this),
			options = {};
		if (typeof arguments[0] === 'object') {

			options = arguments[0];
		} else if (arguments[0] && arguments[1] !== undefined) {
			options[arguments[0]] = arguments[1];
		}
		jQuery.each(options, function (key, value) {
			if (jQuery.fn.wl_Calendar.defaults[key] !== undefined || jQuery.fn.wl_Calendar.defaults[key] == null) {
				$this.data('wl_Calendar')[key] = value;
			} else {
				jQuery.error('Key "' + key + '" is not defined');
			}
		});

	}
};