if ('serviceWorker' in navigator) {
  window.addEventListener('load', () => {
    navigator.serviceWorker.register('/service-worker.js')
      .then(registration => {
        console.log(`ServiceWorker registration successful with scope: ${registration.scope}`);
      })
      .catch(function(error) {
        console.error(`ServiceWorker registration failed: ${error}`);
      });
  });
}