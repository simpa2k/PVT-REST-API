let functions = require('./functions');
let server = 'http://localhost:8080';

let tenantToken = '258bcdc0-aa7f-4b52-8689-5041bae06006';
let renterToken = '0b7a7625-b8d5-4fac-a009-f2ebbfb524ae';

let prettyPrint = function(object) {
    return JSON.stringify(object, null, 4);
};

let printMessage = function(message) {

    console.log("################################################################################");
    console.log(message);

};

let body = [
    4
];

printMessage("Posting the following to /accommodation, as renter: " + prettyPrint(body));

functions.chooseTenants(server, renterToken, body, function() {

    printMessage("Tenant getting renters who has shown interest.");

    /*functions.performAuthenticatedGetRequest(server, '/interests', tenantToken, {}, function(responseObject) {

     printMessage("Result of getting the renters interested in the logged in tenant: " + prettyPrint(responseObject));

     })*/

    functions.getInterests(server, tenantToken, {}, function(responseObject) {

        printMessage("Result of getting the renters interested in the logged in tenant: " + prettyPrint(responseObject));

    })

});

