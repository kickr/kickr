var PAGE_SIZE = 5;

function unwrapResponse(result) {
  return result.data;
}

function EndPoint(http, uri) {

  this.create = function(data) {
    return http.post(uri, data).then(unwrapResponse);
  };

  this.list = function(page, pageSize) {
    page = page || 0;
    pageSize = pageSize || PAGE_SIZE;

    var firstResult = page * pageSize,
        maxResults = firstResult + pageSize;

    return http.get(uri + '?firstResult=' + firstResult + '&maxResults=' + maxResults).then(unwrapResponse);
  };

  this.get = function(id) {
    return http.get(uri + '/' + id).then(unwrapResponse);
  };

  this.remove = function(data) {
    return http.delete(uri + '/' + data.id).then(unwrapResponse);
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

function ScoresApi(http) {
  EndPoint.call(this, http, '/api/score');
}

function ApiService(http) {

  var matches = new MatchApi(http),
      players = new PlayerApi(http),
      scores = new ScoresApi(http),
      tables = new TableApi(http);

  this.matches = function() {
    return matches;
  };

  this.players = function() {
    return players;
  };

  this.scores = function() {
    return scores;
  };

  this.tables = function() {
    return tables;
  };
}


ApiService.$inject = [ '$http' ];

module.exports = ApiService;