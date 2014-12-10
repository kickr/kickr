function unwrapResponse(result) {
  return result.data;
}

function EndPoint(http, uri) {

  this.create = function(data) {
    return http.post(uri, data).then(unwrapResponse);
  };

  this.list = function(page) {
    var query = (page !== undefined ? '?page=' + page : '');

    return http.get(uri + query).then(unwrapResponse);
  };

  this.get = function(id) {
    return http.get(uri + '/' + id).then(unwrapResponse);
  };
}

function MatchApi(http) {
  EndPoint.call(this, http, '/api/match');
}

function TableApi(http) {
  EndPoint.call(this, http, '/api/table');
}

function PlayerApi(http) {
  var uri = '/api/player';

  EndPoint.call(this, http, uri);

  this.find = function(namePart) {
    return http.get(uri + '?namePart=' + namePart).then(unwrapResponse);
  };
}

function ApiService(http) {

  var matches = new MatchApi(http),
      players = new PlayerApi(http),
      tables = new TableApi(http);

  this.matches = function() {
    return matches;
  };

  this.players = function() {
    return players;
  };

  this.tables = function() {
    return tables;
  };
}


ApiService.$inject = [ '$http' ];

module.exports = ApiService;