let hooks = require('hooks');
let functions = require('../functions');

let server = "http://localhost:8080";
let stash = {};

let facebookLogin = "Logga in och skapa hyresgästsprofil > 1. Logga in via facebook > POST mot /facebook/login";

hooks.before(facebookLogin, (transaction) => {

    let requestBody = JSON.parse(transaction.request.body);
    requestBody['facebookAuthToken'] = 'EAAa2D7XPp6oBAG5EJwM0zmFzKUz8ZCoIZAaIo2SjVjIdyowcLrDOxC0vxZBcu3RE3HNe1PIZCyw6lqQJREp1jqKIp1OL3xBo3k4BAUZC2PpeeCciz2iaWzFcbXxeB8lF04gCAtyQJQ0BOjiu3dI9s3Q3BEsFGHsLHmOyusA3x4hI1LHjsRWfaEglxGR0xP0UZD';

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