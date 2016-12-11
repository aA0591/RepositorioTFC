/*----------------------------------------------------------------------*/
/* wl_Store v 2.0
/* description: Uses LocalStorage to save information within the Browser
/*				enviroment
/* dependency:  jStorage
/*----------------------------------------------------------------------*/


jQuery.wl_Store = function (namespace) {
	
    if(typeof jQuery.jStorage != 'object') jQuery.error('wl_Store requires the jStorage library');
	
		var namespace = namespace || 'wl_store',

	
		save = function (key, value) {
			return jQuery.jStorage.set(namespace+'_'+key, value);
		},

		get = function (key) {
			return jQuery.jStorage.get(namespace+'_'+key);
		};
		
		remove = function (key) {
			return jQuery.jStorage.deleteKey(namespace+'_'+key);
		},

		flush = function () {
			return jQuery.jStorage.flush();
		},
		
		index = function () {
			return jQuery.jStorage.index();
		};


	//public methods
	return {
		save: function (key, value) {
			return save(key, value);
		},
		get: function (key) {
			return get(key);
		},
		remove: function (key) {
			return remove(key);
		},
		flush: function () {
			return flush();
		},
		index: function () {
			return index();
		}

	}


};