var DEFAULT_PAGE_SIZE = require('./const').DEFAULT_PAGE_SIZE;

var EndPoint = require('./end-point');

function SimpleEndPoint(http, uri) {
  EndPoint.call(this, http, uri);

  this.create = function(data) {
    return http.post(uri, data).then(this.unwrapResponse);
  };

  this.list = function(page, pageSize) {
    page = page || 0;
    pageSize = pageSize || DEFAULT_PAGE_SIZE;

    var firstResult = page * pageSize,
        maxResults = firstResult + pageSize;

    return http.get(uri + '?firstResult=' + firstResult + '&maxResults=' + maxResults).then(this.unwrapResponse);
  };

  this.get = function(id) {
    return http.get(uri + '/' + id).then(this.unwrapResponse);
  };

  this.remove = function(data) {
    return http.delete(uri + '/' + data.id).then(this.unwrapResponse);
  };
}

module.exports = SimpleEndPoint;