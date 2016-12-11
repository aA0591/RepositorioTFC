/*----------------------------------------------------------------------*/
/* wl_Breadcrumb v 1.0
/* description: Makes and handles a Breadcrumb navigation
/* dependency: 
/*----------------------------------------------------------------------*/


jQuery.fn.wl_Breadcrumb = function (method) {

	var args = arguments;
	return this.each(function () {

		var $this = jQuery(this),
			$li = $this.find('li');
			$a = $this.find('a');


		if (jQuery.fn.wl_Breadcrumb.methods[method]) {
			return jQuery.fn.wl_Breadcrumb.methods[method].apply(this, Array.prototype.slice.call(args, 1));
		} else if (typeof method === 'object' || !method) {
			if ($this.data('wl_Breadcrumb')) {
				var opts = jQuery.extend({}, $this.data('wl_Breadcrumb'), method);
			} else {
				var opts = jQuery.extend({}, jQuery.fn.wl_Breadcrumb.defaults, method, $this.data());
			}
		} else {
			jQuery.error('Method "' + method + '" does not exist');
		}
		
		//get the current field or use the first if not set
		var $current = $this.find('a.active').eq(0);
		if (!$current.length) $current = $a.eq(opts.start);


		if (!$this.data('wl_Breadcrumb')) {

			$this.data('wl_Breadcrumb', {});

			//each anchor
			$a.each(function (i) {
				var _this = jQuery(this);
				//save the id
				_this.data('id', i);

				//prepend numbers if set
				if (opts.numbers) _this.text((i + 1) + '. ' + _this.text());
			});

			//each listelement
			$li.each(function (i) {
				var _this = jQuery(this);
				
				//if has a class (must be an icon)
				if (_this.attr('class')) {
					//innerwrap the anchor to attach the icon
					$a.eq(i).wrapInner('<span class="' + _this.attr('class') + '"/>');
					//remove the class from the list element
					_this.removeAttr('class');
				}
			});

			//add a 'last' class to the last element for IE :(
			if(jQuery.browser.msie)$li.filter(':last').addClass('last');

			//Bind the click handler
			$this.delegate('a', 'click.wl_Breadcrumb', function () {
				var opts = $this.data('wl_Breadcrumb') || opts;

				//if disabled stop
				if (opts.disabled) return false;
				var _this = jQuery(this);
				
				//if not allownextonly or data is current+1 or current-x
				if (!opts.allownextonly || _this.data('id') - $this.find('a.active').data('id') <= 1) {
					
					//activate and trigger callback
					jQuery.fn.wl_Breadcrumb.methods.activate.call($this[0], _this);
					opts.onChange.call($this[0], _this, _this.data('id'));
				}
				return true;
			});

			//connected breadcrumb
			if (opts.connect) {
				var $connect = jQuery('#' + opts.connect),
					$pages = $connect.children();

				//bind event to all 'next' class elements
				$connect.find('.next').bind('click.wl_Breadcrumb', function () {
					$this.wl_Breadcrumb('next');
					return false;
				});
				//bind event to all 'prev' class elements
				$connect.find('.prev').bind('click.wl_Breadcrumb', function () {
					$this.wl_Breadcrumb('prev');
					return false;
				});
				
				//hide all and show the starting one
				$pages.hide().eq(opts.start).show();

			}
			
			//disable if set
			if (opts.disabled) {
				$this.wl_Breadcrumb('disable');
			}
		} else {

		}

		if (opts) jQuery.extend($this.data('wl_Breadcrumb'), opts);

		//activate the current part
		jQuery.fn.wl_Breadcrumb.methods.activate.call(this, $current);
	});

};

jQuery.fn.wl_Breadcrumb.defaults = {
	start: 0,
	numbers: false,
	allownextonly: false,
	disabled: false,
	connect: null,
	onChange: function () {}
};
jQuery.fn.wl_Breadcrumb.version = '1.0';


jQuery.fn.wl_Breadcrumb.methods = {
	activate: function (element) {
		var $this = jQuery(this);
		
		//element is a number so we mean the id
		if (typeof element === 'number') {
			element = $this.find('li').eq(element).find('a');
			element.trigger('click.wl_Breadcrumb');
			return false;
		}
		var _opts = $this.data('wl_Breadcrumb');
		//remove classes
		$this.find('a').removeClass('active previous');
		//find all previous tabs and add a class
		element.parent().prevAll().find('a').addClass('previous');
		//add and active class to the current tab
		element.addClass('active');
		//connected breadcrumb
		if (_opts.connect) {
			var $connect = jQuery('#' + _opts.connect),
				$pages = $connect.children();
			//hide all and show selected
			$pages.hide().eq(element.data('id')).show();
		}
	},
	disable: function () {
		var $this = jQuery(this);
		//disable and add class disable
		$this.wl_Breadcrumb('set', 'disabled', true);
		$this.addClass('disabled');
	},
	enable: function () {
		var $this = jQuery(this);
		//enable and remove class disable
		$this.wl_Breadcrumb('set', 'disabled', false);
		$this.removeClass('disabled');
	},
	next: function () {
		var $this = jQuery(this);
		//click next tab
		$this.find('a.active').parent().next().find('a').trigger('click.wl_Breadcrumb');
	},
	prev: function () {
		var $this = jQuery(this);
		//click prev tab
		$this.find('a.active').parent().prev().find('a').trigger('click.wl_Breadcrumb');
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
			if (jQuery.fn.wl_Breadcrumb.defaults[key] !== undefined || jQuery.fn.wl_Breadcrumb.defaults[key] == null) {
				$this.data('wl_Breadcrumb')[key] = value;
			} else {
				jQuery.error('Key "' + key + '" is not defined');
			}
		});

	}
};