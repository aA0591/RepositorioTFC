/*----------------------------------------------------------------------*/
/* wl_Fileexplorer v 1.0
/* description: makes a Fileexplorer
/* dependency: elFinder plugin (elfinder.js)
/*----------------------------------------------------------------------*/


jQuery.fn.wl_Fileexplorer = function (method) {

	var args = arguments;
	return this.each(function () {

		var $this = jQuery(this);


		if (jQuery.fn.wl_Fileexplorer.methods[method]) {
			return jQuery.fn.wl_Fileexplorer.methods[method].apply(this, Array.prototype.slice.call(args, 1));
		} else if (typeof method === 'object' || !method) {
			if ($this.data('wl_Fileexplorer')) {
				var opts = jQuery.extend({}, $this.data('wl_Fileexplorer'), method);
			} else {
				var opts = jQuery.extend({}, jQuery.fn.wl_Fileexplorer.defaults, method, $this.data());
			}
		} else {
			jQuery.error('Method "' + method + '" does not exist');
		}


		if (!$this.data('wl_Fileexplorer')) {

			$this.data('wl_Fileexplorer', {});

			//simple: call the plugin!
			//this has potential. maybe there are some options in future updates
			$this.elfinder(opts);

		} else {

		}

		if (opts) jQuery.extend($this.data('wl_Fileexplorer'), opts);
	});

};

jQuery.fn.wl_Fileexplorer.defaults = {
	url: 'elfinder/php/connector.php',
	toolbar: [
		['back', 'reload', 'open', 'select', 'quicklook', 'info', 'rename', 'copy', 'cut', 'paste', 'rm', 'mkdir', 'mkfile', 'upload', 'duplicate', 'edit', 'archive', 'extract', 'resize', 'icons', 'list', 'help']
	]
};
jQuery.fn.wl_Fileexplorer.version = '1.0';


jQuery.fn.wl_Fileexplorer.methods = {
	set: function () {
		var $this = jQuery(this),
			options = {};
		if (typeof arguments[0] === 'object') {
			options = arguments[0];
		} else if (arguments[0] && arguments[1] !== undefined) {
			options[arguments[0]] = arguments[1];
		}
		jQuery.each(options, function (key, value) {
			if (jQuery.fn.wl_Fileexplorer.defaults[key] !== undefined || jQuery.fn.wl_Fileexplorer.defaults[key] == null) {
				$this.data('wl_Fileexplorer')[key] = value;
			} else {
				jQuery.error('Key "' + key + '" is not defined');
			}
		});

	}
};