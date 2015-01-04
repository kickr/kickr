function EndPoint(http, uri) {

  this.http = http;
  this.uri = uri;

  this.unwrapResponse = function(result) {
    return result.data;
  };
}

module.exports = EndPoint;