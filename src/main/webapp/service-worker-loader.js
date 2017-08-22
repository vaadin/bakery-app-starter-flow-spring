if ('serviceWorker' in navigator) {
  window.addEventListener('load', () => {
    navigator.serviceWorker.register('/service-worker.js')
      .then(registration => {
        window.console.log(`ServiceWorker registration successful with scope: ${registration.scope}`);
      })
      .catch(function(error) {
        window.console.error(`ServiceWorker registration failed: ${error}`);
      });
  });
}