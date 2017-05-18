const fs = require('fs');
const functions = require('../functions.js');

const host = 'https://graph.facebook.com';
const appId = '';
const appToken = '';
const clientSecret = '';

let users = [];

const numUsers = 3;
let usersToGo = numUsers;

const createAndExtend = function(callback) {

    let options = {
        method: 'POST'
    };

    let parameters = {

        access_token: appToken,
        installed: true,
        permissions: 'email'

    };

    // Creating new test user.
    functions.performRequest(host, '/1889028451313578/accounts/test-users', parameters, options, (responseObject) => {

        // Extending test user's access token.
        functions.performGetRequest(host, '/oauth/access_token', {

            client_id: appId,
            client_secret: clientSecret,
            grant_type: 'fb_exchange_token',
            fb_exchange_token: responseObject.access_token

        }, {}, (responseObject) => {
            callback(responseObject);
        });
    });
};

const makeFacebookCall = function() {

    console.log("Making call to Facebook.");

    createAndExtend((responseObject) => {

        console.log("Got the following after creating user and extending access token: " + JSON.stringify(responseObject, null, 4));

        users.push(responseObject);

        if (--usersToGo === 0) {
            writeFile();
        }
    });
};

const populateUserArray = function() {

    fs.readFile('./users.json', 'utf8', (err, data) => {

        if (err) {
            return console.error(err);
        }

        users = JSON.parse(data);

        console.log("Read from users.json. Contents of array: " + JSON.stringify(users, null, 4));

        for (let i = 1; i <= numUsers; i++) {
            setTimeout(makeFacebookCall, 5000);
        }
    });
};

const writeFile = function() {

    fs.writeFile('./users.json', JSON.stringify(users, null, 4), (err) => {

        if (err) {
            return console.error(err);
        }

        console.log('Successfully saved array to users.json');
    });
};

populateUserArray();

