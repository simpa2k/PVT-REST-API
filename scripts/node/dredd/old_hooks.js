"use strict";

let server = "http://localhost:8080";
let hooks = require('hooks');
let functions = require('../functions');

let stash = {};

let facebookLogin = "Inloggning > Facebook-inloggning > Logga in en användare via Facebook.";

hooks.before(facebookLogin, (transaction) => {

    transaction.skip = true;
    let requestBody = JSON.parse(transaction.request.body);
    requestBody['facebookAuthToken'] = '';

    transaction.request.body = JSON.stringify(requestBody);

});

/*hooks.after(facebookLogin, (transaction) => {

    stash['authToken'] = JSON.parse(transaction.real.body)['authToken'];

});



hooks.beforeEach((transaction) => {

    if (typeof(stash['authToken']) !== 'undefined') {
        transaction.request['headers']['X-AUTH-TOKEN'] = stash['authToken'];
    }

});*/

hooks.before("Användare > Användare > Hämta en användares profil.", (transaction) => {
    transaction.skip = true;
});

hooks.before("Användare > Hyresgäst > Hämta en hyresgästs profil.", (transaction, done) => {

    functions.localLogin(server, 'kalle@example.com', 'password', (responseObject) => {

        transaction.request['headers']['X-AUTH-TOKEN'] = responseObject.authToken;
        done();

    });
});

hooks.before("Användare > Hyresgäst > Uppdatera en hyresgästs profil.", (transaction) => {
    transaction.skip = true;
});

hooks.before("Användare > Hyresvärd > Hämta en hyresvärds profil.", (transaction) => {
    transaction.skip = true;
});

hooks.before("Användare > Hyresvärd > Uppdatera en hyresvärds profil.", (transaction) => {
    transaction.skip = true;
});

hooks.before("Hyresobjekt > Hyresobjekt > Hämta hyresobjekt.", (transaction) => {
    transaction.skip = true;
});

hooks.before("Intresseanmälningar > Intresseanmälningar > Hämta intresseanmälningar.", (transaction, done) => {

    functions.localLogin(server, 'kalle@example.com', 'password', (responseObject) => {

        transaction.request['headers']['X-AUTH-TOKEN'] = responseObject.authToken;
        done();

    });
});
