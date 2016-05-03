var exec = require('cordova/exec');

exports.initSession = function(success, error) {
    exec(success, error, "BranchSDK", "initSession", []);
};

exports.getFirstReferringParams = function(callback) {
    exec(callback, false, "BranchSDK", "getFirstReferringParams", []);
};

exports.getLatestReferringParams = function(callback) {
    exec(callback, false, "BranchSDK", "getLatestReferringParams", []);
};

exports.setIdentity = function(arg0, success, error) {
    exec(success, error, "BranchSDK", "setIdentity", [arg0]);
};

exports.userCompletedAction = function(arg0, callback) {
    exec(callback, false,  "BranchSDK", "userCompletedAction", [arg0]);
};
