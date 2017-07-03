window.PatientPortal = window.PatientPortal || {};

window.PatientPortal.http = function () {

  function getHeaders() {
    const headers = new Headers();
    const token = localStorage.getItem('authToken');
    if (token) {
      headers.append('Authorization', `Bearer ${token}`);
    }

    headers.append('content-type', 'application/json');

    return headers;
  }

  function getRequest(method, path, body) {
    return new Request(`${PatientPortal.config.apiUrl}/${path}`, {
      method: method,
      headers: getHeaders(),
      body: JSON.stringify(body)
    });
  }

  function checkStatus(response) {
    if (response.ok) {
      return response;
    } else if (response.status === 401 || response.status === 403) {
      localStorage.removeItem('authToken');
      document.dispatchEvent(new CustomEvent('log-out', {bubbles: true}));
      return;
    }
    throw new Error('Things went horribly wrong', response);
  }

  return {
    get: function (path) {
      return fetch(getRequest('GET', path)).then(checkStatus).then(res => res.json());
    },
    post: function (path, body) {
      return fetch(getRequest('POST', path, body)).then(checkStatus).then(res => res.json());
    },
    put: function (path, body) {
      return fetch(getRequest('PUT', path, body)).then(checkStatus);
    },
    delete: function (path) {
      return fetch(getRequest('DELETE', path)).then(checkStatus).then(res => res.json());
    },
    login: function (loginDetails) {
      return new Promise((resolve, reject) => {
        fetch(PatientPortal.config.apiUrl + '/login', {method: 'POST', body: JSON.stringify(loginDetails)})
          .then(res => {
            if (res.headers.has('Authorization')) {
              const token = res.headers.get('Authorization').substring(7);
              localStorage.setItem('authToken', token);
              resolve(token);
            } else {
              reject(`Well that didn't work.`);
            }
          })
          .catch(err => reject(err));
      });
    },
    logout: function () {
      localStorage.removeItem('authToken');
    }
  }
}();
