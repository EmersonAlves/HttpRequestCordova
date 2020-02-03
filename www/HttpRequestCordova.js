module.exports.post = function(data, successCallback, errorCallback) {
  cordova.exec(
    result => {
      successCallback(result);
    },
    error => {
      errorCallback(error);
    },
    "HttpRequestCordova",
    "requestPost",
    [data]
  );
};

module.exports.get = function(data, successCallback, errorCallback) {
  cordova.exec(
    result => {
      successCallback(result);
    },
    error => {
      errorCallback(error);
    },
    "HttpRequestCordova",
    "requestGet",
    [data]
  );
};
