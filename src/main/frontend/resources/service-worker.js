const CACHE_NAME = 'bakery-cache';

const urlsToCache = [
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
    .then(function (response) {
      if (response) {
        console.log('Found response in cache:', response);
        return response;
      }
      console.log('No response found in cache. About to fetch from network...');
      return fetch(event.request)
        .then(function (response) {
          console.log('Response from network is:', response);
          return response;
        })
    })
    .catch(function (error) {
      console.error('Fetching failed:', error);
    })
  );
});
