/*
Copyright (c) 2003-2011, CKSource - Frederico Knabben. All rights reserved.
For licensing, see LICENSE.html or http://ckeditor.com/license
*/

CKEDITOR.editorConfig = function( config )
{
	// Define changes to default configuration here. For example:
	// config.language = 'fr';
	// config.uiColor = '#AADC6E';
	
	/* Toolbar definitions */
	config.toolbar_WebRatioDefault = [
		['Bold','Italic','-','NumberedList','BulletedList','-','About']
	];
	config.toolbar_Basic = config.toolbar_WebRatioDefault;
	config.toolbar_Full = [
		['Source'],
		['Cut','Copy','Paste','PasteText','PasteFromWord'],
		['Undo','Redo','-','Find','Replace','-','SelectAll','RemoveFormat'],
		'/',
		['Bold','Italic','Underline','Strike','-','Subscript','Superscript'],
		['NumberedList','BulletedList','-','Table'],
		['About']
	];
	config.toolbar_Default = config.toolbar_Full;
	
	/* File Manager configuration */
	config.filebrowserBrowseUrl = "WRResources/CKEditor/filemanager/browser/default/browser.html?Connector=../../connectors/java";
	config.filebrowserImageBrowseUrl = "WRResources/CKEditor/filemanager/browser/default/browser.html?Type=Image&Connector=../../connectors/java";
	config.filebrowserFlashBrowseUrl = "WRResources/CKEditor/filemanager/browser/default/browser.html?Type=Flash&Connector=../../connectors/java";
	config.filebrowserUploadUrl = "WRResources/CKEditor/filemanager/connectors/java?Type=File";
	config.filebrowserImageUploadUrl = "WRResources/CKEditor/filemanager/connectors/java?Type=Image";
	config.filebrowserFlashUploadUrl = "WRResources/CKEditor/filemanager/connectors/java?Type=Flash";
	
	/* Other settings */
	config.baseFloatZIndex = 20000;
};
