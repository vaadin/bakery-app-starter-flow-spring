// Important: update the version each time you change any of the files listed below
var version = 1;
// define your offline-page and assets used by it
var offlinePage = 'offline-page.html';
var offlineAssets = [offlinePage, 'manifest.json',
  'images/offline-login-banner.jpg'
]

const CACHE_NAME = 'static-v' + version;

function updateCache() {
  return caches.open(CACHE_NAME)
    .then(function(cache) {
      cache.addAll(offlineAssets);
    });
}

self.addEventListener('activate', event => {
  event.waitUntil(
    caches.keys().then(cacheNames => {
      return Promise.all(
        cacheNames.map(cacheName => {
          if (CACHE_NAME !== cacheName) {
            // If this cache name isn't present in the array of "expected" cache names,
            // then delete it.
            console.log('Deleting out of date cache:', cacheName);
            return caches.delete(cacheName);
          }
        })
      );
    })
  );
});

self.addEventListener('install', function(event) {
  event.waitUntil(updateCache());
});

var doesRequestAcceptHtml = function(request) {
  return request.headers.get('Accept')
    .split(',')
    .some(function(type) {
      return type === 'text/html';
    });
};

self.addEventListener('fetch', function(event) {
  var request = event.request;
  if (doesRequestAcceptHtml(request)) {
    // HTML pages fallback to offline page
    event.respondWith(
      fetch(request)
        .catch(function() {
          return caches.match(offlinePage);
        })
    );
  } else {
    if (request.cache === 'only-if-cached' && request.mode !== 'same-origin') {
        return;
    }
    event.respondWith(
      caches.match(request)
        .then(function(response) {
          return response || fetch(request);
        })
    );
  }
});
