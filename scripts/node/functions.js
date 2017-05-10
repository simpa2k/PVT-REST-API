let fetch = require('node-fetch');
let FormData = require('form-data');

/*
 * Login
 */

let facebookLogin = function(server, facebookToken, successCallback) {

    let options = {
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify({
            facebookAuthToken: facebookToken
        })
    };

    performPostRequest(server, '/facebook/login', options, successCallback);

};

let localLogin = function(server, email, password, successCallback) {

	let formData = new FormData();
	formData.append('emailAddress', email);
	formData.append('password', password);

	performPostRequest(server, '/login', {body: formData}, successCallback)

};

/*
 * Interests
 */

let getInterests = function(server, authToken, parameters) {
	performAuthenticatedGetRequest(server, '/interests', authToken, parameters, console.log);
};

let chooseTenants = function(server, authToken, tenants, successCallback) {
    performAuthenticatedPostRequest(server, '/interests', authToken, tenants, successCallback);
};

/*
 * Accommodation
 */

let getAccommodation = function(server, authToken, parameters) {
    performAuthenticatedGetRequest(server, '/accommodation', authToken, parameters, console.log);
};

let createAccommodation = function(server, authToken, accommodation, successCallback) {
    performAuthenticatedPostRequest(server, '/accommodation', authToken, accommodation, successCallback)
};

/*
 * Users
 */

let getTenantProfile = function(server, authToken) {
    performAuthenticatedGetRequest(server, '/users/tenants', authToken, {}, console.log);
};

let createTenantProfile = function(server, authToken, profile, successCallback) {
    performAuthenticatedPostRequest(server, '/users/profiles', authToken, profile, successCallback);
};

/*
 * Utilities
 */

let performAuthenticatedPostRequest = function(server, endpoint, authToken, body, successCallback) {

    let options = {
        headers: {
            'Content-Type': 'application/json',
            'X-AUTH-TOKEN': authToken
        },
        body: JSON.stringify(body)
    };

    performPostRequest(server, endpoint, options, successCallback);

};

let performPostRequest = function(server, endpoint, options, successCallback) {

    options.method = 'POST';
    options.credentials = 'include';

    performRequest(server, endpoint, {}, options, successCallback);

};

let performAuthenticatedGetRequest = function(server, endpoint, authToken, parameters, successCallback) {

    let options = {
        method: 'GET',
        headers: {"X-AUTH-TOKEN": authToken}
    };

    performRequest(server, endpoint, parameters, options, successCallback);

};

let performRequest = function(server, endpoint, parameters, options, successCallback) {

    let uri = buildUri(server + endpoint, parameters);

    fetch(uri, options).then(function(response) {

        return response.json();

    }).then(function(responseObject) {

        successCallback(responseObject);

    }).catch(function(error) {

        console.error(error);

    });
};

let buildUri = function(endpoint, requestObject) {

    let uri = endpoint + '?';
    let first = true;

    for (let key in requestObject) {

        if (first) {
            first = false;
        } else {
            uri += '&';
        }

        if (requestObject.hasOwnProperty(key)) {
            uri += (key + '=' + requestObject[key]);
        }
    }

    return uri;

};

module.exports = {
    facebookLogin: facebookLogin,
    localLogin: localLogin,
	getInterests: getInterests,
    chooseTenants: chooseTenants,
    getAccommodation: getAccommodation,
    createAccommodation: createAccommodation,
    getTenantProfile: getTenantProfile,
    createTenantProfile: createTenantProfile,
    performPostRequest: performPostRequest,
    performAuthenticatedGetRequest: performAuthenticatedGetRequest
};

