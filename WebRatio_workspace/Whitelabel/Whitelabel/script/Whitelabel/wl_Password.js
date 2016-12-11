/*----------------------------------------------------------------------*/
/* wl_Password v 1.0.1
/* description: handles password fields
/* dependency: none
/*----------------------------------------------------------------------*/
jQuery.fn.wl_Password = function (method) {

	var args = arguments;
	return this.each(function () {

		var $this = jQuery(this);

		if ($this.data('wl_Password') && $this.data('wl_Password').confirmfield) {
			return;
		}
		if (jQuery.fn.wl_Password.methods[method]) {
			return jQuery.fn.wl_Password.methods[method].apply(this, Array.prototype.slice.call(args, 1));
		} else if (typeof method === 'object' || !method) {
			if ($this.data('wl_Password')) {
				var opts = jQuery.extend({}, $this.data('wl_Password'), method);
			} else {
				var opts = jQuery.extend({}, jQuery.fn.wl_Password.defaults, method, $this.data());
			}
		} else {
			jQuery.error('Method "' + method + '" does not exist');
		}


		if (!$this.data('wl_Password')) {

			$this.data('wl_Password', {});


			//The strnghtmeter
			var $strength = jQuery('<div/>', {
				'class': 'passwordstrength'
			}).appendTo($this.parent()).hide();

			//if confirm field
			if (opts.confirm) {
				//clone the password field and append it after the field or after the strengthmeter. Hide it
				opts.connect = this.id + '_confirm';
				var $confirm = jQuery($this.addClass('password').clone()).attr({
					'id': opts.connect,
					'name': opts.connect
				}).appendTo($this.parent()).removeAttr('required').hide();
				$confirm.data('wl_Password', {
					confirmfield: true
				});
			}


			$this.bind({

				//focus triggers a keyup
				'focus.wl_Password': function () {
					$this.trigger('keyup.wl_Password');
				},

				//blur sets the strengthmeter
				'blur.wl_Password': function () {
					var opts = $this.data('wl_Password') || opts;
					if ($this.val()) {
						if (opts.confirm && !$confirm.val()) $strength.text(opts.text.confirm);
					} else {
						$strength.hide();
						if (opts.confirm) $confirm.hide();
					}
				},

				'keyup.wl_Password': function () {
					var opts = $this.data('wl_Password') || opts;
					
					//if value is set
					if ($this.val()) {
						
						//show optional confirm field
						if (opts.confirm) $confirm.show();

						//get the strength of the current value
						var _strength = getStrength($this.val(), opts.minLength);
						
						//show optional strengthmeter
						if (opts.showStrength) {
							$strength.show();
							$strength.attr('class', 'passwordstrength').addClass('s_' + _strength).text(opts.words[_strength]);
						}
						
						//add strength to the DOM element
						$this.data('wl_Password').strength = _strength;
					
					//hide siblings if no value is set
					} else {
						if (opts.showStrength) $strength.hide();
						if (opts.confirm) $confirm.val('').hide();
					}
				}
			});
			
			//bind only when confirmation and strengthmeter is active
			if (opts.confirm && opts.showStrength) {
				$confirm.bind('keyup.wl_Password', function () {
					var opts = $this.data('wl_Password') || opts;
					if (!$confirm.val()) {
						//confirm text
						$strength.text(opts.text.confirm);
					} else if ($confirm.val() != $this.val()) {
						//password doesn't match
						$strength.text(opts.text.nomatch);
					} else {
						//password match => show strength
						$strength.text(opts.words[$this.data('wl_Password').strength]);
					}
				});
			}

			//calculates the strenght of a password
			//must return a value between 0 and 5
			//value is the password
			function getStrength(value, minLength) {
				var score = 0;
				if (value.length < minLength) {
					return score
				} else {
					score = Math.min(15, (score + (value.length) * 2));
				}
				if (value.match(/[a-z]/)) score += 1;
				if (value.match(/[A-Z]/)) score += 5;
				if (value.match(/\d+/)) score += 5;
				if (value.match(/(.*[0-9].*[0-9].*[0-9])/)) score += 7;
				if (value.match(/.[!,@,#,$,%,^,&,*,?,_,~]/)) score += 5;
				if (value.match(/(.*[!,@,#,$,%,^,&,*,?,_,~].*[!,@,#,$,%,^,&,*,?,_,~])/)) score += 7;
				if (value.match(/([a-z].*[A-Z])|([A-Z].*[a-z])/)) score += 2;
				if (value.match(/([a-zA-Z])/) && value.match(/([0-9])/)) score += 3;
				if (value.match(/([a-zA-Z0-9].*[!,@,#,$,%,^,&,*,?,_,~])|([!,@,#,$,%,^,&,*,?,_,~].*[a-zA-Z0-9])/)) score += 3;
				return Math.min(5, Math.ceil(score / 10));
			}

		} else {

		}

		if (opts) jQuery.extend($this.data('wl_Password'), opts);
	});

};

jQuery.fn.wl_Password.defaults = {
	confirm: true,
	showStrength: true,
	words: ['too short', 'bad', 'medium', 'good', 'very good', 'excellent'],
	minLength: 3,
	text: {
		confirm: 'please confirm',
		nomatch: 'password doesn\'t match'
	}
};

jQuery.fn.wl_Password.version = '1.0.1';

jQuery.fn.wl_Password.methods = {
	set: function () {
		var $this = jQuery(this),
			options = {};
		if (typeof arguments[0] === 'object') {
			options = arguments[0];
		} else if (arguments[0] && arguments[1] !== undefined) {
			options[arguments[0]] = arguments[1];
		}
		jQuery.each(options, function (key, value) {
			if (jQuery.fn.wl_Password.defaults[key] !== undefined || jQuery.fn.wl_Password.defaults[key] == null) {
				$this.data('wl_Password')[key] = value;
			} else {
				jQuery.error('Key "' + key + '" is not defined');
			}
		});

	}
};