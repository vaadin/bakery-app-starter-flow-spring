// Important: update the version each time you change any of the files listed below
var version = 4;
// define your offline-page and assets used by it
var manifest = 'manifest.json';
var offlinePage = 'offline-page.html';
var offlineAssets = [
  'images/offline-login-banner.jpg'
]

function updateCache() {
  return caches.open('static' + version)
    .then(function(cache) {
      cache.add(manifest);
      cache.add(offlinePage);
      cache.addAll(offlineAssets);
    });
}

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
