function AccessToken($window, $cookies, $http) {

  var accessToken = 'accessToken',
      Authorization = 'Authorization';

  this.get = function() {
    return $cookies[accessToken] || $window.localStorage[accessToken];
  };

  this.set = function(token) {
    $cookies[accessToken] = $window.localStorage[accessToken] = token || '';

    var headers = $http.defaults.headers.common;

    if (token) {
      headers[Authorization] = 'Token ' + token;
    } else {
      delete headers[Authorization];
    }
  };
}

function Authentication($window, $cookies, $rootScope, $route, $http, $q) {

  var accessToken = new AccessToken($window, $cookies, $http);

  function clearAndReject(e) {
    clear();

    return $q.reject(e);
  }

  function clear() {
    $rootScope.currentUser = {
      permissions: 1
    };

    accessToken.set(null);
  }

  function reload(credentials) {
    $route.reload();

    return credentials;
  }

  function updateCredentials(response) {
    var user = response.data;
    $rootScope.currentUser = user;

    accessToken.set(user.token || accessToken.get());

    __loading = null;

    return user;
  }

  var __loading;

  this.load = function() {

    if (__loading) {
      return __loading;
    }

    var token = accessToken.get();

    if (!token) {
      return $q.reject(new Error('no session'));
    }

    if ($rootScope.currentUser) {
      return $q.when($rootScope.currentUser);
    }

    __loading = $http.get('/api/auth?token=' + token)
                     .then(updateCredentials, clearAndReject).then(function(e) {

      return e;
    });

    return __loading;
  };

  this.login = function(name, password) {
    __loading = $http.post('/api/auth', { name: name, password: password })
                         .then(updateCredentials, clearAndReject);

    return __loading;
  };

  this.logout = function() {
    return $http.delete('/api/auth').then(clear).then(reload);
  };

  this.userChange = function() {
    return $rootScope.currentUser;
  };
}


Authentication.$inject = [ '$window', '$cookies', '$rootScope', '$route', '$http', '$q' ];

module.exports = Authentication;