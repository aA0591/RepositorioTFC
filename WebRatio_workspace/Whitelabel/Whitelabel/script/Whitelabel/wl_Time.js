/*----------------------------------------------------------------------*/
/* wl_Time v 1.1
/* description: makes a timefield out of an input field
/* dependency: jQuery Datepicker, mousewheel plugin, jQuery.leadingZero
/*----------------------------------------------------------------------*/


jQuery.fn.wl_Time = function (method) {

	var args = arguments;
	return this.each(function () {

		var $this = jQuery(this);

		if (jQuery.fn.wl_Time.methods[method]) {
			return jQuery.fn.wl_Time.methods[method].apply(this, Array.prototype.slice.call(args, 1));
		} else if (typeof method === 'object' || !method) {
			if ($this.data('wl_Time')) {
				var opts = jQuery.extend({}, $this.data('wl_Time'), method);
			} else {
				var opts = jQuery.extend({}, jQuery.fn.wl_Time.defaults, method, $this.data());
			}
		} else {
			jQuery.error('Method "' + method + '" does not exist');
		}


		if (!$this.data('wl_Time')) {

			$this.data('wl_Time', {});

			$this.bind({

				'mousewheel.wl_Time': function (event, delta) {
					var opts = $this.data('wl_Time') || opts;
					if (opts.mousewheel) {
						event.preventDefault();
						
						//delta must be 1 or -1 (different on macs and with shiftkey pressed)
						delta = (delta < 0) ? -1 : 1;
						
						//scroll thru hours if shift key is pressed
						if (event.shiftKey) delta *= 60 / opts.step;
						jQuery.fn.wl_Time.methods.change.call($this[0], delta);
					}
				},

				'change.wl_Time': function () {
					var opts = $this.data('wl_Time') || opts;
					
					//correct input value
					jQuery.fn.wl_Time.methods.correct.call($this[0]);
					//print it
					jQuery.fn.wl_Time.methods.printTime.call($this[0]);
					//callback
					opts.onChange.call($this[0], opts.time);
				}
				
				//for 12h format ad a little span after the input and set length to 5 (hh:mm)
			}).after('<span class="timeformat"/>').attr('maxlength', 5);


			//is connected to a datepicker
			if (opts.connect !== null) {
				var _date = jQuery('#' + opts.connect),
					_oldcallback = opts.onDateChange;

				//set a callback if the time reaches another date
				_callback = function (offset) {
					var current = new Date(_date.datepicker('getDate')).getTime();
					if (current) _date.datepicker('setDate', new Date(current + (864e5 * offset)));
					_oldcallback.call($this[0], offset);
				};
				opts.onDateChange = _callback;
			}
			

			//value is set and has to get translated (self-explanatory) 
			if (opts.value) {
				var now = new Date().getTime(),
					date;
				switch (opts.value) {
				case 'now':
					date = new Date(now);
					break;
				default:
					//if a valid number add them as days to the date field
					if (!isNaN(opts.value)) date = new Date(now + (60000 * (opts.value % 60)));
				}
				
				//set the time (hh:mm)
				opts.time = jQuery.leadingZero(date.getHours()) + ':' + jQuery.leadingZero(date.getMinutes());
				//write it into the input
				$this.val(opts.time);
			}

		} else {

		}

		if (opts) jQuery.extend($this.data('wl_Time'), opts);
		
		//save value if already set
		if($this.val().match(/\d\d:\d\d/)){
			$this.data('wl_Time').time = $this.val();
			//correct input value
			jQuery.fn.wl_Time.methods.printTime.call($this[0]);
		}
	});

};

jQuery.fn.wl_Time.defaults = {
	step: 5,
	timeformat: 24,
	roundtime: true,
	time: null,
	value: null,
	mousewheel: true,
	onDateChange: function (offset) {},
	onHourChange: function (offset) {},
	onChange: function (value) {}
};
jQuery.fn.wl_Time.version = '1.1';



jQuery.fn.wl_Time.methods = {
	change: function (delta) {
		var $this = jQuery(this),
			opts = $this.data('wl_Time'),
			_current = new Date('2010/01/01 ' + ($this.data('wl_Time').time || '00:00')),
			_new = new Date(_current.getTime() + (delta * $this.data('wl_Time').step * 60000)),
			_hours = _new.getHours(),
			_minutes = _new.getMinutes();

		//round the time on a mousewheel
		if (opts.roundtime) {
			_minutes -= (_minutes % $this.data('wl_Time').step);
		}

		//save time
		$this.data('wl_Time').time = jQuery.leadingZero(_hours) + ':' + jQuery.leadingZero(_minutes);
		
		//and print it
		jQuery.fn.wl_Time.methods.printTime.call(this);

		//callbacks
		opts.onChange.call(this, $this.data('wl_Time').time);
		if (Math.abs(_current.getMinutes() - _minutes) == (60 - opts.step)) {
			opts.onHourChange.call(this, (_hours - _current.getHours()));
		}
		if (Math.abs(_current.getHours() - _hours) == (23)) {
			opts.onDateChange.call(this, (_hours) ? -1 : 1);
		}
	},
	printTime: function () {
		var $this = jQuery(this),
			opts = $this.data('wl_Time'),
			time = opts.time;
	
		if (time) {
			time = time.split(':');
			
			//calculate the 12h format
			if (opts.timeformat == 12) {
				$this.val(jQuery.leadingZero(((time[0] % 12 == 0) ? 12 : time[0] % 12)) + ':' + jQuery.leadingZero(time[1])).next().html((time[0] / 12 >= 1) ? 'pm' : 'am');
			
			//or set the 24h format
			} else {
				$this.val(jQuery.leadingZero(time[0]) + ':' + jQuery.leadingZero(time[1]));
			}
		}
	},
	correct: function () {
		var $this = jQuery(this),
			val = $this.val(),
			time;
		
		//no value => stop
		if (val == '') return;
		
		//it is not hh:mm format
		if (!/^\d+:\d+$/.test(val)) {
			
			//convert the input (read the docs for more details)
			if (val.length == 1) {
				val = "0" + val + ":00";
			} else if (val.length == 2) {
				val = val + ":00";
			} else if (val.length == 3) {
				val = val.substr(0, 2) + ":" + val.substr(2, 3) + "0";
			} else if (val.length == 4) {
				val = val.substr(0, 2) + ":" + val.substr(2, 4);
			}
		}
		time = val.split(':');
		
		//value is wrong or out of range
		if (!/\d\d:\d\d$/.test(val) && val != "" || time[0] > 23 || time[1] > 59) {
			$this.val('00:00').focus().select();
			$this.data('wl_Time').time = '00:00';
			jQuery.fn.wl_Time.methods.printTime.call(this);
		
		//value is a time
		} else {
			//save it
			$this.data('wl_Time').time = val;
			//print it
			jQuery.fn.wl_Time.methods.printTime.call(this);
		}
	},
	set: function () {
		var $this = jQuery(this),
			options = {};
		if (typeof arguments[0] === 'object') {
			options = arguments[0];
		} else if (arguments[0] && arguments[1] !== undefined) {
			options[arguments[0]] = arguments[1];
		}
		jQuery.each(options, function (key, value) {
			if (jQuery.fn.wl_Time.defaults[key] !== undefined || jQuery.fn.wl_Time.defaults[key] == null) {
				$this.data('wl_Time')[key] = value;
				$this.trigger('change.wl_Time');
			} else {
				jQuery.error('Key "' + key + '" is not defined');
			}
		});

	}
};