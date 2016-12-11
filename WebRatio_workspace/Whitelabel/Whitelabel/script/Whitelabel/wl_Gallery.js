/*----------------------------------------------------------------------*/
/* wl_Gallery v 1.2
/* description: makes a sortable gallery
/* dependency: jQuery UI sortable
/*----------------------------------------------------------------------*/


jQuery.fn.wl_Gallery = function (method) {

	var args = arguments;
	return this.each(function () {

		var $this = jQuery(this);


		if (jQuery.fn.wl_Gallery.methods[method]) {
			return jQuery.fn.wl_Gallery.methods[method].apply(this, Array.prototype.slice.call(args, 1));
		} else if (typeof method === 'object' || !method) {
			if ($this.data('wl_Gallery')) {
				var opts = jQuery.extend({}, $this.data('wl_Gallery'), method);
			} else {
				var opts = jQuery.extend({}, jQuery.fn.wl_Gallery.defaults, method, $this.data());
			}
		} else {
			jQuery.error('Method "' + method + '" does not exist');
		}

		var $items = $this.find('a');
		if (!$this.data('wl_Gallery')) {

			$this.data('wl_Gallery', {});

			//make it sortable
			$this.sortable({
				containment: $this,
				opacity: 0.8,
				distance: 5,
				handle: 'img',
				forceHelperSize: true,
				placeholder: 'sortable_placeholder',
				forcePlaceholderSize: true,
				start: function (event, ui) {
					$this.dragging = true;
					ui.item.trigger('mouseleave');
				},
				stop: function (event, ui) {
					$this.dragging = false;
				},
				update: function (event, ui) {}
			});

			opts.images = [];

			//add the rel attribut for the fancybox
			$items.attr('rel', opts.group).fancybox(opts.fancybox);

			
			$items.each(function () {
				var _this = jQuery(this),
					_image = _this.find('img'),
					_append = jQuery('<span>');
					
				//add edit and delete buttons
				if(opts.editBtn)_append.append('<a class="edit">Edit</a>');
				if(opts.deleteBtn)_append.append('<a class="delete">Delete</a>');
				
				if(opts.deleteBtn || opts.editBtn)_this.append(_append);
				
				//store images within the DOM
				opts.images.push({
					image: _image.attr('rel') || _image.attr('src'),
					thumb: _image.attr('src'),
					title: _image.attr('title'),
					description: _image.attr('alt')
				});
			});
			
			if(opts.deleteBtn || opts.editBtn){
				//bind the mouseenter animation
				$this.delegate('li', 'mouseenter', function (event) {
					if (!$this.dragging) {
						var _this = jQuery(this),
							_img = _this.find('img'),
							_pan = _this.find('span');
	
						_img.animate({
							top: -20
						}, 200);
						_pan.animate({
							top: 80
						}, 200);
					}
				//bind the mouseleave animation
				}).delegate('li', 'mouseleave', function (event) {
					var _this = jQuery(this),
						_img = _this.find('img'),
						_pan = _this.find('span');
	
					_img.animate({
						top: 0
					}, 200);
					_pan.animate({
						top: 140
					}, 200);
				});
			}
			
			if(opts.editBtn){
				//bind the edit event to the button
				$this.find('a.edit').bind('click.wl_Gallery touchstart.wl_Gallery', function (event) {
					event.stopPropagation();
					event.preventDefault();
					var opts = $this.data('wl_Gallery') || opts,
						_this = jQuery(this),
						_element = _this.parent().parent().parent(),
						_href = _element.find('a')[0].href,
						_title = _element.find('a')[0].title;
						
					//callback action
					opts.onEdit.call($this[0], _element, _href, _title);
					return false;
	
				});
			}
			
			if(opts.deleteBtn){
				//bind the delete event to the button
				$this.find('a.delete').bind('click.wl_Gallery touchstart.wl_Gallery', function (event) {
					event.stopPropagation();
					event.preventDefault();
					var opts = $this.data('wl_Gallery') || opts,
						_this = jQuery(this),
						_element = _this.parent().parent().parent(),
						_href = _element.find('a')[0].href,
						_title = _element.find('a')[0].title;
					
					//callback action
					opts.onDelete.call($this[0], _element, _href, _title);
					return false;
				});
			}

		} else {

		}

		if (opts) jQuery.extend($this.data('wl_Gallery'), opts);
	});

};

jQuery.fn.wl_Gallery.defaults = {
	group: 'wl_gallery',
	editBtn: true,
	deleteBtn: true,
	fancybox: {},
	onEdit: function (element, href, title) {},
	onDelete: function (element, href, title) {}
};
jQuery.fn.wl_Gallery.version = '1.2';


jQuery.fn.wl_Gallery.methods = {
	set: function () {
		var $this = jQuery(this),
			options = {};
		if (typeof arguments[0] === 'object') {
			options = arguments[0];
		} else if (arguments[0] && arguments[1] !== undefined) {
			options[arguments[0]] = arguments[1];
		}
		jQuery.each(options, function (key, value) {
			if (jQuery.fn.wl_Gallery.defaults[key] !== undefined || jQuery.fn.wl_Gallery.defaults[key] == null) {
				$this.data('wl_Gallery')[key] = value;
			} else {
				jQuery.error('Key "' + key + '" is not defined');
			}
		});

	}
};