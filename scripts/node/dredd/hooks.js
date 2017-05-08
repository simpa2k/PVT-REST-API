let hooks = require('hooks');
let functions = require('../functions');

let server = "http://localhost:9000";
let stash = {};

let facebookLogin = "Logga in och skapa hyresgästsprofil > 1. Logga in via Facebook > POST mot /facebook/login";

hooks.before(facebookLogin, (transaction) => {

    transaction.skip = true;
    let requestBody = JSON.parse(transaction.request.body);
    requestBody['facebookAuthToken'] = '';

    transaction.request.body = JSON.stringify(requestBody);

});

hooks.after(facebookLogin, (transaction) => {

     stash['authToken'] = JSON.parse(transaction.real.body)['authToken'];

 });

 hooks.beforeEach((transaction) => {

     if (typeof(stash['authToken']) !== 'undefined') {
         transaction.request['headers']['X-AUTH-TOKEN'] = stash['authToken'];
     }

 });

hooks.before("Logga in och skapa hyresgästsprofil > 2. Skapa en hyresgästsprofil > POST mot /users/profiles", (transaction) => {
   transaction.skip = true;
});

hooks.before("Logga in och skapa hyresobjekt > 1. Logga in via facebook > POST mot /facebook/login", (transaction) => {
    transaction.skip = true;
});

hooks.before("Anmäl intresse för hyresgäster > 2. Skicka valda hyresgäster > POST mot /users", (transaction) => {
    transaction.skip = true;
});
