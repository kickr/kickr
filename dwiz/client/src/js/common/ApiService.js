function MatchApi(http) {

  this.create = function(match) {
    return http.post('/api/match', match).then(function(result) {

      console.log(result);

      return result.data;
    });
  };

  this.get = function(id) {
    return http.get('/api/match/' + id).then(function(result) {
      return result.data;
    });
  };

  this.list = function(page) {
    var query = (page !== undefined ? '?page=' + page : '');

    return http.get('/api/match' + query).then(function(result) {
      return result.data;
    });
  };
}

function PlayerApi(http) {
  this.find = function(namePart) {
    return http.get('/api/player?namePart=' + namePart).then(function(result) {
      return result.data;
    });
  };
}

function ApiService(http) {
  var matches = new MatchApi(http),
      players = new PlayerApi(http);

  this.matches = function() {
    return matches;
  };

  this.players = function() {
    return players;
  };
}


ApiService.$inject = [ '$http' ];

module.exports = ApiService;