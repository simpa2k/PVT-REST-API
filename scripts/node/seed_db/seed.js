const fs = require('fs');
const functions = require('../functions.js');

let host = 'https://protected-gorge-44302.herokuapp.com';
//let host = 'http://localhost:8080';

/*
 Christel
 Henrik
 Håkan
 Iris
 Niklas
 Pat
 Simon O
 Simon S
 Tobias
 Viktor
 */

const readUsers = function(callback) {

    fs.readFile('./users.json', 'utf8', (err, data) => {

        if (err) {
            return console.error(err);
        }

        let users = JSON.parse(data);

        console.log("Read from users.json. Contents of array: " + JSON.stringify(users, null, 4));

        callback(users);

    });
};

const loginAll = function(users, successCallback) {

    for (let i = 0; i < users.length; i++) {

        functions.facebookLogin(host, users[i].facebookToken, (responseObject) => {
            successCallback(users[i], responseObject.authToken);
        });
    }
};

const createAccommodation = function(user, authToken) {

    functions.createAccommodation(host, authToken, user.accommodation, () => {
        console.log("Successfully created accommodation for user with authentication token " + authToken);
    });
};

const createTenantProfile = function(user, authToken) {

    functions.createTenantProfile(host, authToken, user.tenantProfile, () => {
        console.log("Successfully created tenant profile for user with authentication token " + authToken);
    });
};

const run = function() {

    readUsers((users) => {

        loginAll(users, (user, authToken) => {

            if (user.accommodation !== null) {

                createAccommodation(user, authToken);

            } /*else if (user.tenantProfile !== null) {

                createTenantProfile(user, authToken);

            }*/
        });
    });
};

run();
