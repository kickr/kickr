var SimpleEndPoint = require('./simple-end-point'),
    BaseEndPoint = require('./end-point');


function MatchApi(http, uri) {
  SimpleEndPoint.call(this, http, uri);
}

function TableApi(http, uri) {
  SimpleEndPoint.call(this, http, uri);
}

function PlayerApi(http, uri) {
  SimpleEndPoint.call(this, http, uri);

  this.find = function(namePart) {
    return http.get(uri + '?namePart=' + namePart).then(this.unwrapResponse);
  };
}

function TournamentApi(http, uri) {
  SimpleEndPoint.call(this, http, uri);
}


function ScoresApi(http, uri) {
  BaseEndPoint.call(this, http, uri);

  this.get = function() {
    return http.get(uri).then(this.unwrapResponse);
  };
}

function UserApi(http, uri) {

  this.fetchStatus = function() {
    return http.get(uri + '/status').then(this.unwrapResponse);
  };
}


function Api(http) {

  var baseUri = '/api';

  this.create = function(ApiModule, uri) {
    return new ApiModule(http, baseUri + '/' + uri);
  };

  var matches = this.create(MatchApi, 'match'),
      players = this.create(PlayerApi, 'player'),
      scores = this.create(ScoresApi, 'score'),
      tables = this.create(TableApi, 'table'),
      tournaments = this.create(TournamentApi, 'tournament'),
      user = this.create(UserApi, 'user');

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

  this.tournaments = function() {
    return tournaments;
  };

  this.user = function() {
    return user;
  };

  this.tournaments = function() {
    return tournaments;
  };
}


Api.$inject = [ '$http' ];

module.exports = Api;