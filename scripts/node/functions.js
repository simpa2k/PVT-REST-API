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

const getInterests = function(server, authToken, parameters, successCallback) {
    performAuthenticatedGetRequest(server, '/interests', authToken, parameters, successCallback);
};

const showInterest = function(server, authToken, tenantId, successCallback) {
    addEdge(server, authToken, tenantId, true, successCallback);
};

const showDisinterest = function(server, authToken, userId, successCallback) {
    addEdge(server, authToken, userId, false, successCallback);
};

const addEdge = function(server, authToken, userId, active, successCallback) {

    let body = {
        user: userId,
        active: active
    };

    performAuthenticatedPostRequest(server, '/interests', authToken, body, successCallback);

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

let getUsers = function(server, authToken, parameters) {
    performAuthenticatedGetRequest(server, '/users', authToken, parameters, console.log);
};

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

    /*let options = {
        method: 'GET',
        headers: {"X-AUTH-TOKEN": authToken}
    };*/
    let options = {
        headers: {'X-AUTH-TOKEN': authToken}
    };

    performGetRequest(server, endpoint, parameters, options, successCallback);

};

let performGetRequest = function(server, endpoint, parameters, options, successCallback) {

    options.method = 'GET';

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
    showInterest: showInterest,
    showDisinterest: showDisinterest,
    chooseTenants: chooseTenants,
    getAccommodation: getAccommodation,
    createAccommodation: createAccommodation,
    getUsers: getUsers,
    getTenantProfile: getTenantProfile,
    createTenantProfile: createTenantProfile,
    performPostRequest: performPostRequest,
    performGetRequest: performGetRequest,
    performAuthenticatedGetRequest: performAuthenticatedGetRequest,
    performRequest: performRequest
};

