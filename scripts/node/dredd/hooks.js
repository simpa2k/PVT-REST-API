let hooks = require('hooks');
let functions = require('../functions');

let stash = {};

let facebookLogin = "Inloggning > Facebook-inloggning > Logga in en användare via Facebook.";

hooks.before(facebookLogin, (transaction) => {

    let requestBody = JSON.parse(transaction.request.body);
    requestBody['facebookAuthToken'] = 'EAAa2D7XPp6oBAIC0aolIuCkUZBYqwm2vp3ZAOM2ZC2vXmoqyw7G298oC6gU2WgcKHZBcthRc0IuiP7Na74QKz8FHj7n9ZCWoBXuedNNtF5ZBL3cauD0wk9BTberrJ6tZAtPLbPohSTZBWBZBrZBdyucb85NSEVJMicrFGwvCZCNtskdf4lSPi0rM6GA';

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

    functions.localLogin('http://localhost:9000', 'kalle@example.com', 'password', (responseObject) => {

        transaction.request['headers']['X-AUTH-TOKEN'] = responseObject.authToken;
        done();

    });
    //transaction.skip = true;
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

hooks.before("Intresseanmälningar > Intresseanmälningar > Hämta intresseanmälningar.", function(transaction) {

    hooks.log("Intresseanmälningar");
    transaction.skip = true;

});
