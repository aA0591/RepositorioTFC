/************************************************************************
 *									      	INIT SCRIPTS					                      *
 ************************************************************************/

jQuery(document).ready(function($) {
  init($,$(document));

  if (typeof wr != "undefined") {
    /* Init for ajax update */
    wr.LegacyAjaxPlugin.addContentUpdateListener(function(element) {
      init($,$(element));
    });
  }
});


function wl_removeRow( unitID, rowID ) {
  var $ = jQuery;
  var lastIndex = multiEntryMap[unitID + "LastIndex"];

  var lastID = unitID + '[' + lastIndex + ']'; 
  var $lastID = '#'+unitID + '\\[' + lastIndex + '\\]';

  //Extract row id
  var rowData = /(\w+)\[(\d+)\]/.exec(rowID);
  var $rowID = '#'+rowData[1] + '\\[' + rowData[2] + '\\]'; 
  var $row = $($rowID);

  var $ds = $('#'+unitID + 'DataSize');
  var size = parseInt( $ds.val() ); 
  if(size > 1){
    // Remove the button
    $row.next().remove();
    // Remove the row
    $row.remove()

    size--;
    $ds.val( size );

    if(lastID == rowID) {
      lastIndex--;
      $lastID = '#'+unitID + '\\[' + lastIndex + '\\]';

      while( $( $lastID ).length==0 && lastIndex>=0 ) {
        lastIndex--;
        $lastID = '#'+unitID + '\\[' + lastIndex + '\\]';
      }
    }
  } 
  multiEntryMap[unitID + "LastIndex"] = lastIndex;
}
function wl_addRow( unitID ) {
  var $ = jQuery;

  var $unitID = "#"+unitID;
  var lastIndex = multiEntryMap[unitID + "LastIndex"];
  var lastRowId =  unitID + '[' + lastIndex + ']';
  var $lastRowId =  '#'+unitID + '\\[' + lastIndex + '\\]';
  var $lastRow = $( $lastRowId );

  if( $lastRow.length===0 ) {
    lastIndex = 0
    lastRowId =  unitID + '[0]';
    $lastRowId =  '#'+unitID + '\\[0\\]';
    $lastRow = $( $lastRowId );
  }

  var $container = $( $unitID );
  lastIndex++;
  var newRowId = unitID + '[' + (lastIndex) + ']';
  var $newRowId = '#'+unitID + '\\[' + (lastIndex) + '\\]';

  // Clone the button
  var $deleteBtn = $lastRow.next().clone();
  var html = $deleteBtn.html();
  while( html.indexOf( lastRowId )>-1) {
    html = html.replace( lastRowId, newRowId );
  }
  $deleteBtn.html( html );


  // Clone the row
  var $newRow = $( "<fieldset />", {
    id: newRowId,
    "class": $lastRow[0].className,
    html: $lastRow.html()
  } );

  // Replace the old ID
  html = $newRow.html();
  while( html.indexOf( lastRowId )>-1) {
    html = html.replace( lastRowId, newRowId );
  }
  $newRow.html( html );


  // Reset the fields
  $( 'input, textarea', $newRow ).not( 'input[type="checkbox"],input[type="radio"]' ).each( function() {
    $( this ).val( '' );
  });
  $( 'input[type="checkbox"],input[type="radio"]', $newRow ).each( function() {
    $( this ).next( '.jquery-checkbox' ).remove();
  });
  $( 'input.date, input.time, input.timestamp', $newRow ).each( function() {
    $( this ).removeClass( 'hasDatepicker' ).removeData('datepicker');
  });
  $("select option", $newRow).filter(':selected').each( function() {
    $( this ).removeAttr( 'selected' );
  });
  $( 'textarea', $newRow ).each( function() {
    $( this ).val( '' );
    if ($( this ).is('.html')) {
      $( this ).show().prev().remove();
    }
  });
  $( 'input[type="file"]', $newRow ).each( function() {
    $( this ).nextAll().remove();
    $( this ).unwrap().css('opacity', '');
  });

  // Add the row
  var $ds =  $( '#'+unitID+'DataSize' );
  var size = parseInt( $ds.val() );
  size++;
  $ds.val( size );
  $container.append( $newRow );
  // Add the delete button
  $container.append( $deleteBtn );

  // init the added tags
  init( $, $newRow );

  multiEntryMap[unitID + "LastIndex"] = lastIndex;
}

function init($,element) {

  //for Caching
  var $content = $(element);

  /*----------------------------------------------------------------------*/
  /* preload images
	/*----------------------------------------------------------------------*/

  //$.preload();

  /*----------------------------------------------------------------------*/
  /* Widgets
	/*----------------------------------------------------------------------*/
  var $widgets = $.merge($content.find('div.widgets'), $content.parents('div.widgets'));
  //$widgets.removeData('wl_Widget');
  //$widgets.wl_Widget();
  if( $.fn.wl_Widget ) {
    $content.find("div.widget,section.widget").wl_Widget();
  }
  /*----------------------------------------------------------------------*/
  /* All Form Plugins
	/*----------------------------------------------------------------------*/


  //Integers and decimal
  //if( $.fn.wl_Number ) {
  //	$content.find('input.integer').wl_Number();	
  //	$content.find('input.decimal').wl_Number({decimals:2,step:0.1});
  //	$content.find('input.float').wl_Number({decimals:2,step:0.1});
  //}

  //Date and Time fields
  var agent=navigator.userAgent.toLowerCase();
  //if( !(/iphone|ipad|ipod|opera/i.test(agent)) ) {
  if ( $.fn.datepicker ) {
    $content.find('input.date:not(:disabled, [readonly])').each(function(index, el) {
      var dateOptions = $(this).data('dateOptions');
      var dateConfig = $.wr.calendar.dateConfigFromJava(dateOptions.datePattern);
      $(this).datepicker({
        dateFormat: dateConfig.dateFormat,
        firstDay: dateOptions.firstDay
      });
    });
  }
  
  if ( $.fn.timepicker ) {
    $content.find('input.time:not(:disabled, [readonly])').each(function(index, el) {
      var timeOptions = $(this).data('timeOptions');
      var timeConfig = $.wr.calendar.timeConfigFromJava(timeOptions.timePattern);
      $(this).timepicker({
        timeFormat: timeConfig.timeFormat,
        ampm: timeConfig.ampm
      });
    });
    $content.find('input.timestamp:not(:disabled, [readonly])').each(function(index, el) {
      var datetimeOptions = $(this).data('timestampOptions');
      var datetimeConfig = $.wr.calendar.timestampConfigFromJava(datetimeOptions.timestampPattern);
      $(this).datetimepicker({
        dateFormat: datetimeConfig.dateFormat,
        firstDay: datetimeOptions.firstDay,
        timeFormat: datetimeConfig.timeFormat,
        ampm: datetimeConfig.ampm,
        separator: datetimeConfig.separator
      });
    });
  }
  //}
  // Remove the .no-iphone items
  if ( /iphone|ipad|ipod/i.test(agent) ) {
    $( '.no-iphone' ).remove();
  }

  //Autocompletes (source is required)
  /*
	$content.find('input.autocomplete').wl_Autocomplete({
		source: ["ActionScript","AppleScript","Asp","BASIC","C","C++","Clojure","COBOL","ColdFusion","Erlang","Fortran","Groovy","Haskell","Java","JavaScript","Lisp","Perl","PHP","Python","Ruby","Scala","Scheme"]
	});
	*/

  //Elastic textareas (autogrow)
  if( $.fn.elastic )
    $content.find('textarea[data-autogrow]').elastic();
  //WYSIWYG Editor
  if( $.fn.wl_Editor )
    $content.find('textarea.html:not(:disabled, [readonly])').wl_Editor();

  //Validation
  if( $.fn.wl_Valid )
    $content.find('input[data-regex]').wl_Valid();
  if( $.fn.wl_Mail )
    $content.find('input.email:not(:disabled, [readonly])').wl_Mail();
  if( $.fn.wl_URL )
    $content.find('input.url:not(:disabled, [readonly])').wl_URL();

  //File Upload
  /*
	if( $.fn.wl_File )
		$content.find('input[type=file]').wl_File();
	*/

  //Password and Color
  if( $.fn.wl_Password )
    $content.find('input[type=password]').wl_Password();
  if( $.fn.wl_Color )
    $content.find('input.color').wl_Color();

  //Sliders
  if( $.fn.wl_Slider )
    $content.find('div.slider').wl_Slider();

  //Multiselects
  if( $.fn.wl_Multiselect )
    $content.find('select[multiple]').wl_Multiselect();

  //The Form itself
  /*
	if( $.fn.wl_Form )
		$content.find('form').wl_Form();
	*/

  /*----------------------------------------------------------------------*/
  /* Alert boxes
	/*----------------------------------------------------------------------*/

  if( $.fn.wl_Alert )
    $content.find('div.alert').wl_Alert();

  /*----------------------------------------------------------------------*/
  /* Breadcrumb
	/*----------------------------------------------------------------------*/

  if( $.fn.wl_Breadcrumb )
    $content.find('ul.breadcrumb').wl_Breadcrumb();

  /*----------------------------------------------------------------------*/
  /* datatable plugin
	/*----------------------------------------------------------------------*/

  if( $.fn.dataTable ) {
    $content.find(".DataTableIndexUnit table.datatable").each(function(index) {
      var opts = {
        bAutoWidth : false,
        aoColumnDefs : [{ "bSortable": false, "bSearchable": false, "aTargets": ["link-h"], "sWidth": "1px" }]
      }
      opts = $.extend(opts, $(this).data());

      $(this).dataTable( opts );
    });
  }

  /*----------------------------------------------------------------------*/
  /* uniform plugin && checkbox plugin (since 1.3.2)
	/* uniform plugin causes some issues on checkboxes and radios
	/*----------------------------------------------------------------------*/

  if( $.fn.uniform )
    $content.find("input[type=file]").uniform();
  if( $.fn.checkbox )
    $content.find('input:checkbox, input:radio').checkbox();

  /*----------------------------------------------------------------------*/
  /* Charts
	/*----------------------------------------------------------------------*/

  if( $.fn.wl_Chart ) {
    $content.find('table.chart').wl_Chart({
      onClick: function(value, legend, label, id){
        $.msg("value is "+value+" from "+legend+" at "+label+" ("+id+")",{header:'Custom Callback'});
      }
    });
  }


  /*----------------------------------------------------------------------*/
  /* Calendar
	/*----------------------------------------------------------------------*/
  if($.fn.wl_Calendar) {
  }

  /*----------------------------------------------------------------------*/
  /* Gallery
	/*----------------------------------------------------------------------*/

  //$content.find('ul.gallery').wl_Gallery();


  /*----------------------------------------------------------------------*/
  /* Tipsy Tooltip
	/*----------------------------------------------------------------------*/

  if( $.fn.tipsy ) {
    var config = {
      tooltip :{
        gravity: 'nw',
        fade: false,
        opacity: 1,
        offset: 0
      }
    };
    $content.find('input[title]').tipsy({
      gravity: function(){return ($(this).data('tooltip-gravity') || config.tooltip.gravity); },
      fade: config.tooltip.fade,
      opacity: config.tooltip.opacity,
      color: config.tooltip.color,
      offset: config.tooltip.offset
    });
  }


  /*----------------------------------------------------------------------*/
  /* Accordions
	/*----------------------------------------------------------------------*/

  if( $.fn.accordion ) {
    $content.find('div.accordion').accordion({
      collapsible:true,
      autoHeight:false
    });
  }

  /*----------------------------------------------------------------------*/
  /* Tabs
	/*----------------------------------------------------------------------*/
  if( $.fn.tabs ) {
    $content.find('div.tab').tabs({
      selected: $content.find('div.tab').data( 'selected' ),
      fx: {
        opacity: 'toggle',
        duration: 'fast'
      }	  
    });
  }


  /*----------------------------------------------------------------------*/
  /* Helpers
	/*----------------------------------------------------------------------*/

  //placholder in inputs is not implemented well in all browsers, so we need to trick this		
  /*	$content.find("[placeholder]").bind('focus.placeholder',function() {
		var el = $(this);
		if (el.val() == el.attr("placeholder") && !el.data('uservalue')) {
			el.val("");
			el.removeClass("placeholder");
		}
	}).bind('blur.placeholder',function() {
		var el = $(this);
		if (el.val() == "" || el.val() == el.attr("placeholder") && !el.data('uservalue')) {
			el.addClass("placeholder");
			el.val(el.attr("placeholder"));
			el.data("uservalue",false);
		}else{

		}
	}).bind('keyup.placeholder',function() {
		var el = $(this);
		if (el.val() == "") {
			el.data("uservalue",false);
		}else{
			el.data("uservalue",true);
		}
	}).trigger('blur.placeholder');
	*/









  /*
	 * Now the custom functionality
	 */


  //Header navigation for smaller screens
  var $headernav = $content.find('ul#headernav').not('.landmark');

  $headernav.bind('click',function(){
    //if(window.innerWidth > 800) return false;
    var ul = $headernav.find('ul').eq(0);
    (ul.is(':hidden')) ? ul.addClass('shown') : ul.removeClass('shown');
  });

  $headernav.find('ul > li').bind('click',function(event){
    event.stopPropagation();
    var children = $(this).children('ul');

    if(children.length){
      (children.is(':hidden')) ? children.addClass('shown') : children.removeClass('shown');
      return false;
    }
  });

  /* Custom menu for landmark pages */
  var $headernavLandmark = $content.find('ul#headernav.landmark');

  // Landmarks
  $headernavLandmark.bind('click',function(){
    //if(window.innerWidth > 800) return false;
    var ul = $headernavLandmark.find('ul').eq(0);
    (ul.is(':hidden')) ? ul.addClass('shown') : ul.removeClass('shown');
  });
  // Page links
  $pagelinks = $content.find('ul#pagelinks.landmark');
  $pagelinks.bind('click',function(){
    //if(window.innerWidth > 800) return false;
    var ul = $pagelinks.find('ul').eq(0);
    (ul.is(':hidden')) ? ul.addClass('shown') : ul.removeClass('shown');
  });

  $headernavLandmark.find('ul > li').bind('mouseenter mouseleave',function(event){
    //event.stopPropagation();
    var children = $(this).children('ul');

    if(children.length && children.children().length){
      if( event.type=='mouseenter' ) {
        if( children.is(':hidden') ) {
          children.addClass('shown')
          $(this).addClass( 'active' );
        }
      } else if( event.type=='mouseleave' ) {
        if( !children.is(':hidden') )
          children.removeClass('shown')
          $(this).removeClass( 'active' );
      }
      return false;
    }
  });



  //Main Navigation		
  var $nav = $content.find('#nav');

  $nav.delegate('li','click.wl', function(event){
    var _this = $(this),
        _parent = _this.closest( 'ul' ),
        a = _parent.find('a');
    _parent.find('ul').slideUp('fast');
    a.removeClass('active');
    _this.find('ul:hidden').slideDown('fast');
    _this.find('a').eq(0).addClass('active');
    event.stopPropagation();
  });

  // Landmark navigation
  var $navLandmark = $content.find('nav ul.landmark').closest( 'nav' );

  $navLandmark.delegate('li','mouseenter.wl mouseleave.wl', function(event){
    var _this = $(this),
        _parent = _this.closest( 'ul' ),
        a = _parent.find('a');
    _parent.find('ul').slideUp('fast');
    a.removeClass('active');
    _this.find('ul:hidden').slideDown('fast');
    _this.find('a').eq(0).addClass('active');
    event.stopPropagation();
  });

}


Event.simulate = function(element, eventName) {

  var options = $.extend({
    pointerX: 0,
    pointerY: 0,
    button: 0,
    ctrlKey: false,
    altKey: false,
    shiftKey: false,
    metaKey: false,
    bubbles: true,
    cancelable: true
  }, arguments[2] || { } );

  var eventMatchers = {
    'HTMLEvents': /load|unload|abort|error|select|change|submit|reset|focus|blur|resize|scroll/,
    'MouseEvents': /click|mousedown|mouseup|mouseover|mousemove|mouseout/
  };

  var oEvent, eventType = null;

  for (var name in eventMatchers) {
    if (eventMatchers[name].test(eventName)) eventType = name;
  }

  if (!eventType) throw new SyntaxError('Only HTMLEvents and MouseEvents interfaces are supported');

  if (document.createEvent) {
    oEvent = document.createEvent(eventType);
    if (eventType == 'HTMLEvents') {
      oEvent.initEvent(eventName, options.bubbles, options.cancelable);
    }
    else {
      oEvent.initMouseEvent(eventName, options.bubbles, options.cancelable, document.defaultView, 
                            options.button, options.pointerX, options.pointerY, options.pointerX, options.pointerY,
                            options.ctrlKey, options.altKey, options.shiftKey, options.metaKey, options.button, $(element));
    }
    $(element).trigger(oEvent);
  }
  else {
    options.clientX = options.pointerX;
    options.clientY = options.pointerY;
    oEvent = $.extend(document.createEventObject(), options);
    $(element).trigger(/*'on' +*/ eventName, oEvent);
  }
}


/****************************************************
 *					UTILS							*
 ****************************************************/
/*
 *  Disable double click on links and buttons
 */ 
function disableOnClick(link){
  if (link.previouslyClicked) { 
    return true; 
  } 
  link.previouslyClicked = true;
  try{
    link.onclick=null;
    link.style.cursor='wait';
    link.style.color='#DCD9CC';
  }catch(e){
  }  
  return false;
}



