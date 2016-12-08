jQuery(document).ready(function ($) {
  "use strict";

  //Enable sidebar toggle
  $("[data-toggle='offcanvas']").click(function (e) {
    e.preventDefault();

    //If window is small enough, enable sidebar push menu
    if ($(window).width() <= 992) {
      $('.row-offcanvas').toggleClass('active');
      $('.left-side').removeClass("collapse-left");
      $(".right-side").removeClass("strech");
      $('.row-offcanvas').toggleClass("relative");
    } else {
      //Else, enable content streching
      $('.left-side').toggleClass("collapse-left");
      $(".right-side").toggleClass("strech");
    }
  });

  //Add hover support for touch devices
  $('body').on('touchstart', '.btn', function () {
    $(this).addClass('hover');
  }).on('touchend', '.btn', function () {
    $(this).removeClass('hover');
  });

  /*     
   * Add collapse and remove events to boxes
   */
  $('body').on("click", ".box-tools [data-widget='collapse']", function (e) {
    e.preventDefault();
    //Find the box parent        
    var box = $(this).parents(".box").first();
    //Find the body and the footer
    var bf = box.find(".box-body, .box-footer");
    if (!box.hasClass("collapsed-box")) {
      box.addClass("collapsed-box");
      bf.slideUp();
    } else {
      box.removeClass("collapsed-box");
      bf.slideDown();
    }
  });

  /* Sidebar tree view */
  $(".sidebar .treeview").sidebarTree();

  /* 
   * Make sure that the sidebar is streched full height
   * ---------------------------------------------
   * We are gonna assign a min-height value every time the
   * wrapper gets resized and upon page load. We will use
   * Ben Alman's method for detecting the resize event.
   *
   **/
  function fix_sidebar() {
    //Get window height and the wrapper height
    var height = $(window).height() - $("body header.header").height();
    $(".wrapper").css("min-height", height + "px");
    var content = $(".wrapper").height();
    //If the wrapper height is greater than the window
    if (content > height)
      //then set sidebar height to the wrapper
      $(".left-side, html, body").css("min-height", content + "px");
    else {
      //Otherwise, set the sidebar to the height of the window
      $(".left-side, html, body").css("min-height", height + "px");
    }
  }

  //Save tab status
  var tabStatus = {};
  $('body').on('shown.bs.tab', 'a[data-toggle="tab"]', function (e) {
    var tabId = $(e.target).closest('.nav').attr('id');
    tabStatus[tabId] = $(e.target).attr('href');
  });

  //Save collapse status
  var collapseStatus = {};
  $('body').on('hidden.bs.collapse shown.bs.collapse', '.collapse, .collapsing', function (e) {
    var collapseId = $(this).attr('id');
    if (collapseId) {
      collapseStatus[collapseId] = $(this).hasClass("in"); 
    }
  });


  //Fire when wrapper is resized
  $(window).resize(function () {
    fix_sidebar();
  });

  //Fix the fixed layout sidebar scroll bug
  fix_sidebar();


  /*
   * @param {Element} container Block to init
   */
  function init(container) {
    var $container = $(container),
        active;


    $("input:text[alt], input:password[alt], textarea.nockeditor[alt]", $container).each(function (index) {
      var $this = $(this);
      $this.attr("placeholder", $this.attr("alt"));
    });
    if (!$.support.placeholder) {
      active = document.activeElement;
      $('input:text[placeholder], input:password[placeholder], textarea.nockeditor[placeholder]', $container).blur();
      if (document.activeElement !== active) {
        $(active).focus();
      }
    }

    $('.MultiEntryUnit.dynamic', $container).each(function () {
      dynamicMultipleForm(this);
    });

    if ($.fn.filestyle) {
        $(":file + .bootstrap-filestyle", $container).remove();
        $(":file", $container).each(function(){
      	 $(this).filestyle({buttonText: $(this).prev().attr('data-buttonText')}); 
        });
    }

    //Reset Tab
    $('.nav-tabs, .nav-pills', $container).each(function () {
      var tabId = $(this).attr('id');
      if (tabId && tabId in tabStatus) {
        $('a[href="' + tabStatus[tabId] + '"]', this).tab('show');
      }
    });

    //Reset Accordion
    $('.collapse', $container).each(function () {
      var collapseId = $(this).attr('id');
      if (collapseId && collapseId in collapseStatus) {
        $(this)[(collapseStatus[collapseId]) ? 'addClass' : 'removeClass']('in');
      }
    });
  }

  /*
   * @param {Element} dmf Multiple Form Container
   */
  function dynamicMultipleForm(dmf) {
    var $table = $('> .table', dmf),
        $lastRow = $('> tbody > tr:last', $table),
        lastRowId = $lastRow.attr('id'),
        newRowId = $table.attr("id") + "[INDEX]",
        newRow = $lastRow.get(0).outerHTML;
    while (newRow.indexOf(lastRowId) > 0) {
      newRow = newRow.replace(lastRowId, newRowId);
    }
    while (newRow.indexOf("selected") > 0) {
      newRow = newRow.replace("selected", "");
    }
    while (newRow.indexOf("checked") > 0){
        newRow = newRow.replace("checked", "");
      }    
    if (jQuery.datepicker && jQuery.datepicker.markerClassName) {
      while (newRow.indexOf(jQuery.datepicker.markerClassName) > 0) {
        newRow = newRow.replace(jQuery.datepicker.markerClassName, "");
      }
    }
   
    $table.data('multiEntryRow', newRow);

    function checkRowLength($table) {
      var $removeRowLinks = $('.remove-row', $table);
      if ($removeRowLinks.length > 1) {
        $removeRowLinks.removeClass('disabled');
      } else if ($removeRowLinks.length < 2) {
        $removeRowLinks.addClass('disabled');
      }
    }

    $(dmf).on('click', '.add-row', function (event) {
      event.preventDefault();

      var $table = $(this).closest(".MultiEntryUnit.dynamic").find('> .table'),
          $lastRow = $('> tbody > tr:last', $table),
          lastRowId = $lastRow.attr('id'),
          rowIndex = 1 + Number(lastRowId.replace($table.attr("id"), "").replace("[", "").replace("]", "")),
          newRowId = $table.attr("id") + "[" + (rowIndex) + "]",
          newRow = $table.data('multiEntryRow'),
          rowId = $table.attr("id") + "[INDEX]";

      while (newRow.indexOf(rowId) > 0) {
        newRow = newRow.replace(rowId, newRowId);
      }

      var $newRow = $(newRow).insertAfter($lastRow);
      $(":input", $newRow).not(':button, :submit').val("");
      init($newRow);

      checkRowLength($table);

      return false;

    }).on('click', '.remove-row', function (event) {
      event.preventDefault();

      var $row = $(this).closest("tr"),
          $table = $(this).closest("table");
      if (!$row.is(":only-child") && !$(this).is(".disabled")) {
        $row.remove();
      }

      checkRowLength($table);

      return false;
    });

    checkRowLength(dmf);
  }

  $.support.placeholder = ('placeholder' in document.createElement('input'));
  if (!$.support.placeholder) {
    $(document).on('focus', 'input:text[placeholder], input:password[placeholder], textarea.nockeditor[placeholder]', function (e) {
      var $this = $(this),
          placeholder = $this.attr('placeholder');
      if ($this.is('[readonly]')) {
        return false;
      }
      if ($.trim(placeholder) !== '' && $this.val() === placeholder) {
        $this.val('').removeClass('has-placeholder');
      }
    }).on('blur', 'input:text[placeholder], input:password[placeholder], textarea.nockeditor[placeholder]', function (e) {
      var $this = $(this),
          placeholder = $this.attr('placeholder');
      if ($this.is('[readonly]')) {
        return false;
      }
      if ($.trim(placeholder) !== '' && ($this.val() === '' || $this.val() === placeholder)) {
        $this.val(placeholder).addClass('has-placeholder');
      }
    }).on('submit.wr', 'form', function (e) {
      $('.has-placeholder', $(this)).each(function () {
        $(this).val('');
      });
    }).on('ajaxbegin', function (e) {
      $('.has-placeholder', $(this)).each(function () {
        $(this).val('');
      });
    }).on('ajaxcomplete', function (e) {
      var active = document.activeElement;
      $('input:text[placeholder], input:password[placeholder], textarea.nockeditor[placeholder]').blur();
      if (document.activeElement !== active) {
        $(active).focus();
      }
    });
  }




  if (typeof wr !== "undefined") {

    /* Init for ajax update */
    wr.LegacyAjaxPlugin.addContentUpdateListener(function (element) {
      init(element);
    });

    /* Trigger custom event on ajax events */
    wr.logic.AjaxRequestActor.prototype.execute = (function () {
      var _execute = wr.logic.AjaxRequestActor.prototype.execute;
      return function (input, output, context) {
        $(document).trigger($.Event("ajaxbegin", {
          ajaxAction: input
        }));
        var promise = _execute.call(this, input, output, context);
        promise.onComplete(function () {
          $(document).trigger($.Event("ajaxcomplete", {
            ajaxAction: input
          }));
        });
        return promise;
      };
    })();

  }

  init(document);


});







/*
 * SIDEBAR MENU
 * ------------
 * This is a custom plugin for the sidebar menu. It provides a tree view.
 *
 * Usage:
 * $(".sidebar).tree();
 *
 * Note: This plugin does not accept any options. Instead, it only requires a class
 *       added to the element that contains a sub-menu.
 *
 * When used with the sidebar, for example, it would look something like this:
 * <ul class='sidebar-menu'>
 *      <li class="treeview active">
 *          <a href="#>Menu</a>
 *          <ul class='treeview-menu'>
 *              <li class='active'><a href=#>Level 1</a></li>
 *          </ul>
 *      </li>
 * </ul>
 *
 * Add .active class to <li> elements if you want the menu to be open automatically
 * on page load. See above for an example.
 */
(function ($) {
  "use strict";

  $.fn.sidebarTree = function () {

    return this.each(function () {
      var btn = $(this).children("a").first();
      var menu = $(this).children(".treeview-menu").first();
      var isActive = $(this).hasClass('active');

      //initialize already active menus
      if (isActive) {
        menu.show();
        //btn.children(".fa-angle-left").first().removeClass("fa-angle-left").addClass("fa-angle-down");
      }
      //Slide open or close the menu on link click
      btn.click(function (e) {
        e.preventDefault();
        if (isActive) {
          //Slide up to close menu
          menu.slideUp(function () { //close also sub-menu elements
            menu.find(".treeview-menu").hide()
          });
          isActive = false;
          //btn.children(".fa-angle-down").first().removeClass("fa-angle-down").addClass("fa-angle-left");
          btn.parent("li").removeClass("active");
        } else {
          //Slide down to open menu
          menu.slideDown();
          isActive = true;
          //btn.children(".fa-angle-left").first().removeClass("fa-angle-left").addClass("fa-angle-down");
          btn.parent("li").addClass("active");
        }
      });

      /* Add margins to submenu elements to give it a tree look 
      menu.find("li > a").each(function () {
        var pad = parseInt($(this).css("margin-left")) + 10;

        $(this).css({
          "margin-left": pad + "px"
        });
      });*/

    });

  };


}(jQuery));