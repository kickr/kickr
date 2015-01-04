var BaseEndPoint = require('./simple-end-point');


function MatchApi(http, uri) {
  BaseEndPoint.call(this, http, uri);
}

function TableApi(http, uri) {
  BaseEndPoint.call(this, http, uri);
}

function PlayerApi(http, uri) {
  BaseEndPoint.call(this, http, uri);

  this.find = function(namePart) {
    return http.get(uri + '?namePart=' + namePart).then(this.unwrapResponse);
  };
}

function ScoresApi(http, uri) {
  BaseEndPoint.call(this, http, uri);
}

function TournamentApi(http, uri) {
  BaseEndPoint.call(this, http, uri);
}

function UserApi(http, uri) {

  this.fetchStatus = function() {
    return http.get(uri + '/status').then(this.unwrapResponse);
  };
};


function Api(http) {

  var baseUri = '/api';

  this.create = function(ApiModule, uri) {
    return new ApiModule(http, baseUri + '/' + uri);
  };

  var matches = this.create(MatchApi, 'match'),
      players = this.create(PlayerApi, 'player'),
      scores = this.create(ScoresApi, 'score'),
      tables = this.create(TableApi, 'table'),
      tournaments = this.create(ScoresApi, 'tournament'),
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
}


Api.$inject = [ '$http' ];

module.exports = Api;