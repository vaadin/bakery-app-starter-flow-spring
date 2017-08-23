const CACHE_NAME = 'bakery-cache';

const urlsToCache = [
  '/login.html',
  '/manifest.json',
  '/icons/icon-96.png',
  '/icons/icon-192.png',
  '/icons/bg.jpg',
  '/icons/favicon.ico',
  '/fonts/OpenSans-Light-webfont.woff',
  '/fonts/OpenSans-Semibold-webfont.woff'
];

self.addEventListener('install', event => {
  event.waitUntil(
    caches.open(CACHE_NAME)
      .then(cache => cache.addAll(urlsToCache))
  );
});

self.addEventListener('fetch', event => {
  event.respondWith(
    caches.match(event.request)
      .then(function(response) {
        if (response) {
          return response;
        }
        return fetch(event.request)
          .then(function(response) {
            return response;
          });
      })
      .catch(function(error) {
      })
  );
});