/*----------------------------------------------------------------------*/
/* wl_Autocomplete v 1.0
/* description: extends the jQuery Autocomplete Function
/* dependency: jQUery UI Autocomplete, parseData function
/*----------------------------------------------------------------------*/


jQuery.fn.wl_Autocomplete = function (method) {

	var args = arguments;
	return this.each(function () {

		var $this = jQuery(this);


		if (jQuery.fn.wl_Autocomplete.methods[method]) {
			return jQuery.fn.wl_Autocomplete.methods[method].apply(this, Array.prototype.slice.call(args, 1));
		} else if (typeof method === 'object' || !method) {
			if ($this.data('wl_Autocomplete')) {
				var opts = jQuery.extend({}, $this.data('wl_Autocomplete'), method);
			} else {
				var opts = jQuery.extend({}, jQuery.fn.wl_Autocomplete.defaults, method, $this.data());
			}
		} else {
			try {
				return $this.autocomplete(method, args[1], args[2]);
			} catch (e) {
				jQuery.error('Method "' + method + '" does not exist');
			}
		}


		if (!$this.data('wl_Autocomplete')) {

			$this.data('wl_Autocomplete', {});

			//if source is a function use the returning value of that function
			if (jQuery.isFunction(window[opts.source])) {
				opts.source = window[opts.source].call(this);
			}
			//if it is an string it must be an array to parse (typeof '[1,2,3]' === 'string') 
			if (typeof opts.source === 'string') {
				opts.source = jQuery.parseData(opts.source, true);
			}

			//call the jQueryUI autocomplete plugin
			$this.autocomplete(opts);


		} else {

		}

		if (opts) jQuery.extend($this.data('wl_Autocomplete'), opts);
	});

};

jQuery.fn.wl_Autocomplete.defaults = {
	//check http://jqueryui.com/demos/autocomplete/ for all options
};
jQuery.fn.wl_Autocomplete.version = '1.0';


jQuery.fn.wl_Autocomplete.methods = {
	set: function () {
		var $this = jQuery(this),
			options = {};
		if (typeof arguments[0] === 'object') {
			options = arguments[0];
		} else if (arguments[0] && arguments[1] !== undefined) {
			options[arguments[0]] = arguments[1];
		}
		jQuery.each(options, function (key, value) {
			if (jQuery.fn.wl_Autocomplete.defaults[key] !== undefined || jQuery.fn.wl_Autocomplete.defaults[key] == null) {
				$this.data('wl_Autocomplete')[key] = value;
			} else {
				jQuery.error('Key "' + key + '" is not defined');
			}
		});

	}
};