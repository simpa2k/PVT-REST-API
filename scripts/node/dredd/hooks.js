let hooks = require('hooks');
let functions = require('../functions');

let server = "http://localhost:8080";
let stash = {};

let facebookLogin = "Logga in och skapa hyresgästsprofil > 1. Logga in via facebook > POST mot /facebook/login";

hooks.before(facebookLogin, (transaction) => {

    let requestBody = JSON.parse(transaction.request.body);
    requestBody['facebookAuthToken'] = 'EAAa2D7XPp6oBANiMSPjQExKkBOHFsPGWb4giRh1OSaJC5B1GweZBAgos4zZB8xcEXEZCj6pYk4ITO4oYEadMAEJKSe40JBqxu6KGcRgi9ZCIHbb453O5sSQYmfbZBaZArxU3IBBZATWhkdx0ZAuMlWGS6j5ZCgIvt30vGp5HbCsrcXub781Tng9PTVrQ7ZAI8vdWkZD';

    transaction.request.body = JSON.stringify(requestBody);

});

hooks.after(facebookLogin, (transaction) => {

     stash['authToken'] = JSON.parse(transaction.real.body)['authToken'];
     stash['facebookAuthToken'] = JSON.parse(transaction.real.body)['authToken'];

 });

 hooks.beforeEach((transaction) => {

     if (typeof(stash['authToken']) !== 'undefined') {
         transaction.request['headers']['X-AUTH-TOKEN'] = stash['authToken'];
     }

 });

hooks.before("Logga in och skapa hyresgästsprofil > 2. Skapa en hyresgästprofil > POST mot /users/profiles", (transaction) => {

});

hooks.before("Logga in och skapa hyresobjekt > 1. Logga in via facebook > POST mot /facebook/login", (transaction) => {
    transaction.skip = true;
});

hooks.before("Logga in och skapa hyresobjekt > 2. Skapa ett hyresobjekt > POST mot /accommodation", (transaction, done) => {

    // Logging in as another user.
    functions.localLogin(server, "user1@demo.com", "password", (responseObject) => {

        transaction.request['headers']['X-AUTH-TOKEN'] = responseObject.authToken;
        stash['authToken'] = responseObject.authToken;

        done();

    });
});

hooks.before("Anmäl intresse för hyresgäster > 2. Skicka valda hyresgäster > POST mot /interests", (transaction) => {

    let requestBody = JSON.parse(transaction.request.body);
    requestBody = [4];

    transaction.request.body = JSON.stringify(requestBody);

});

hooks.before("Hämta hyresobjekt vars hyresvärd visat intresse för en > 1. Hämta intresseanmälningar > GET mot /interests", (transaction) => {

    // Resetting user.
    stash['authToken'] = stash['facebookAuthToken'];

});