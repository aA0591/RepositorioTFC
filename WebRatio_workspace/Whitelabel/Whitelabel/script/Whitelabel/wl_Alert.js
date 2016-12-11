/*----------------------------------------------------------------------*/
/* wl_Alert v 1.1
/* description: Handles alert boxes
/* dependency: jquery UI Slider, fadeOutSlide plugin
/*----------------------------------------------------------------------*/


jQuery.fn.wl_Alert = function (method) {
	var args = arguments;
	return this.each(function () {

		var $this = jQuery(this);


		if (jQuery.fn.wl_Alert.methods[method]) {
			return jQuery.fn.wl_Alert.methods[method].apply(this, Array.prototype.slice.call(args, 1));
		} else if (typeof method === 'object' || !method) {
			if ($this.data('wl_Alert')) {
				var opts = jQuery.extend({}, $this.data('wl_Alert'), method);
			} else {

				var opts = jQuery.extend({}, jQuery.fn.wl_Alert.defaults, method, $this.data());
			}
		} else {
			jQuery.error('Method "' + method + '" does not exist');
		}


		if (!$this.data('wl_Alert')) {

			$this.data('wl_Alert', {});

			//bind click events to hide alert box
			$this.bind('click.wl_Alert', function (event) {
				event.preventDefault();

				//Don't hide if it is sticky
				if (!$this.data('wl_Alert').sticky) {
					jQuery.fn.wl_Alert.methods.close.call($this[0]);
				}

				//prevent hiding the box if an inline link is clicked
			}).find('a').bind('click.wl_Alert', function (event) {
				event.stopPropagation();
			});
		} else {

		}
		//show it if it is hidden
		if ($this.is(':hidden')) {
			$this.slideDown(opts.speed / 2);
		}

		if (opts) jQuery.extend($this.data('wl_Alert'), opts);
	});

};

jQuery.fn.wl_Alert.defaults = {
	speed: 500,
	sticky: false,
	onBeforeClose: function (element) {},
	onClose: function (element) {}
};
jQuery.fn.wl_Alert.version = '1.1';


jQuery.fn.wl_Alert.methods = {
	close: function () {
		var $this = jQuery(this),
			opts = $this.data('wl_Alert');
		//call callback and stop if it returns false
		if (opts.onBeforeClose.call(this, $this) === false) {
			return false;
		};
		//fadeout and call an callback
		$this.fadeOutSlide(opts.speed, function () {
			opts.onClose.call($this[0], $this);
		});
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
			if (jQuery.fn.wl_Alert.defaults[key] !== undefined || jQuery.fn.wl_Alert.defaults[key] == null) {
				$this.data('wl_Alert')[key] = value;
			} else {
				jQuery.error('Key "' + key + '" is not defined');
			}
		});

	}
};

//to create an alert box on the fly
jQuery.wl_Alert = function (text, cssclass, insert, after, options) {
	//go thru all
	jQuery('div.alert').each(function () {
		var _this = jQuery(this);
		//...and hide if one with the same text is allready set
		if (_this.text() == text) {
			_this.slideUp(jQuery.fn.wl_Alert.defaults.speed);
		}
	});

	//create a new DOM element and inject it
	var al = jQuery('<div class="alert ' + cssclass + '">' + text + '</div>').hide();
	(after) ? al.appendTo(insert).wl_Alert(options) : al.prependTo(insert).wl_Alert(options);
	
	//return the element
	return al;
};