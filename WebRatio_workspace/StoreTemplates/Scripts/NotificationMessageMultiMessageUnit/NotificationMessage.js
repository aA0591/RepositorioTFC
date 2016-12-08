(function ($) {
    $.notificationMessage = function (el, options) {
        // To avoid scope issues, use 'base' instead of 'this'
        // to reference this class from internal events and functions.
        var base = this;

        // Access to jQuery and DOM versions of element
        base.$el = $(el);
        base.el = el;

        // Add a reverse reference to the DOM object
        base.$el.data("notificationMessage", base);

        base.init = function () {
            base.options = $.extend({}, $.notificationMessage.defaultOptions, options);
            base.$el.css({
                top: -base.$el.outerHeight()
            }).appendTo('body').animate({
                top: "0"
            }, 500, function() {
            	//remove old notifications
            	$('.notificationMessageMultiMessageUnit').not(this).remove();
            }).click(function () {
                base.hideMessage();
            });
            if (base.options.autoHiding) {
                setTimeout(function () {
                    base.hideMessage();
                }, 3000); //set delay to activate auto hiding message
            }
        };

        base.hideMessage = function () {
            base.$el.animate({
                top: -base.$el.outerHeight()
            }, 500, function () {
                $(this).remove();
            });
        };

        // Run initializer
        base.init();
    };

    $.notificationMessage.defaultOptions = {
        autoHiding: true
    };

    $.fn.notificationMessage = function (options) {
        return this.each(function () {
            (new $.notificationMessage(this, options));
        });
    };

})(jQuery);