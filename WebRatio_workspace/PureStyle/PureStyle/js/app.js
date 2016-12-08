jQuery(function($) {

    /* -- MENU MANAGEMENT ------------------------------------------------------------ */

    var layout = $('#layout'),
        menu = $('#menu'),
        menuLink = $('#menuLink'),
        copyright = $('.copyright');


    menuLink.on('click.wr.menu touchstart.wr.menu', function(e) {
        var active = 'active';

        e.preventDefault();
        layout.toggleClass(active);
        menu.toggleClass(active);
        menuLink.toggleClass(active);
        copyright.toggleClass(active);
    });

    if (menu.children().hasClass('pure-menu-horizontal')) {

        function _onresize() {
            var menu = $('#menu').children(),
                left = 'left-menu',
                horizontal = 'pure-menu-horizontal';

            if (window.innerWidth < 993) {
                if (!layout.hasClass(left)) {
                    layout.toggleClass(left);
                }
                if (menu.hasClass(horizontal)) {
                    menu.toggleClass(horizontal);
                }
            } else {
                if (layout.hasClass(left)) {
                    layout.toggleClass(left);
                }
                if (!menu.hasClass(horizontal)) {
                    menu.toggleClass(horizontal);
                    /* force redraw */
                    $('.user-location').hide().show(0);
                    $('html').trigger('clear.wr.menu');
                }

            }
        };

        $(window).on('resize', _onresize);
        _onresize();
    }

    if ($('.pure-menu-children', menu).length || $('.user-location ul', menu).length) {
        function toggleMenu(e) {
            e.preventDefault();
            e.stopPropagation();
            var menuItem = $(this).parent();
            menuItem.hasClass('pure-menu-open') ? menuItem.trigger('close.wr.menu') : menuItem.trigger('open.wr.menu');
        };

        function openMenu(e) {
            e.stopPropagation();
            $(this).parents('.pure-menu-horizontal').find('.pure-menu-open').not($(this).parents('.pure-menu-open')).trigger('close.wr.menu');
            $(this).addClass('pure-menu-open');
        };

        function closeMenu(e) {
            e.stopPropagation();
            $(this).removeClass('pure-menu-open');
            $('.pure-menu-has-children.pure-menu-open', $(this)).trigger('close.wr.menu');
        };

        function clearMenu(e) {
            $('.pure-menu-horizontal > ul > .pure-menu-has-children.pure-menu-open, .user-location .pure-menu-open', $(this)).trigger('close.wr.menu');

        };

        function touchstartMenu(e) {
            if (!$(e.target).parents('#menu').length) {
                $('html').trigger('clear.wr.menu');
            }
        };

        /* Menu events */
        $('html').on('click.wr.menu touchstart.wr.menu', '.pure-menu-has-children > .pure-menu-label, .pure-menu-horizontal .user-location p', toggleMenu)
            .on('open.wr.menu', '.pure-menu-has-children', openMenu)
            .on('close.wr.menu', '.pure-menu-has-children', closeMenu);
        $('html').on('click.wr.menu clear.wr.menu', clearMenu)
            .on('touchstart.wr', touchstartMenu);

        /* User Location events */
        $('.user-location p').parent().on('open.wr.menu', openMenu)
            .on('close.wr.menu', closeMenu);
    }


    /* -- Dismiss Management --------------------------------------------------------- */
    $(document).on('click', '.pure-dismiss', function(event) {
        $(this).parents(".pure-alert-dismissable").eq(0).slideUp();
    });
});