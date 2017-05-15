let functions = require('./functions');
let server = 'http://localhost:8080';

let tenantToken = '42f692cd-2e7f-45fa-87f0-4d545b20c6bd';
let renterToken = '9edbb770-4377-49d1-bd90-9ca15ef269e7';

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

    printMessage("Tenant getting renters who have shown interest.");

    /*functions.performAuthenticatedGetRequest(server, '/interests', tenantToken, {}, function(responseObject) {

     printMessage("Result of getting the renters interested in the logged in tenant: " + prettyPrint(responseObject));

     })*/

    functions.getInterests(server, tenantToken, {}, function(responseObject) {

        printMessage("Result of getting the renters interested in the logged in tenant: " + prettyPrint(responseObject));

    })

});

