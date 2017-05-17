let functions = require('./functions');

//let server = 'http://localhost:8080';
let server = 'https://protected-gorge-44302.herokuapp.com';

let tenantFacebookToken = '';
let renterFacebookToken = '';

let tenantToken = '';
let renterToken = '';

let prettyPrint = function(object) {
    return JSON.stringify(object, null, 4);
};

let printMessage = function(message) {

    console.log("################################################################################");
    console.log(message);

};

printMessage("Logging in tenant via Facebook");

functions.facebookLogin(server, tenantFacebookToken, function(responseObject) {

    printMessage("Response object after logging in tenant via Facebook: " + prettyPrint(responseObject));

    tenantToken = responseObject.authToken;

    let body = {

        description: "Hej, jag heter Kalle och behöver någonstans att bo.",
            maxRent: 5000,
            maxDeposit: 8000,
            rentalPeriod: {
            start: "2017-05-01",
                end: "2018-05-01"
        }

    };

    printMessage("Posting the following to /users/profiles, as tenant: " + prettyPrint(body));

    functions.createTenantProfile(server, tenantToken, body, function() {

        printMessage("Logging in tenant via Facebook");

        functions.facebookLogin(server, renterFacebookToken, function(responseObject) {

            printMessage("Response object after logging in renter via Facebook: " + prettyPrint(responseObject));
            renterToken = responseObject.authToken;

            let body = {

                rent: 5000,
                    size: 20,
                    rooms: 1,
                    deposit: 8000,
                    address: {
                    streetName: "Dymlingsgränd",
                        streetNumber: 3,
                        streetNumberLetter: "A"
                },
                rentalPeriod: {
                    start: "2017-05-01",
                        end: "2018-05-01"
                }

            };

            printMessage("Posting the following to /accommodation, as renter: " + prettyPrint(body));

            functions.createAccommodation(server, renterToken, body, function() {

                printMessage("Renter getting tenants whose max rent is greater than or equal to the rent of the accommodation.");

                functions.performAuthenticatedGetRequest(server, '/users', renterToken, {

                    maxRent: 5000,

                }, function(responseObject) {

                    printMessage("Result of getting tenants filtered by max rent: " + prettyPrint(responseObject));

                    let body = [
                        responseObject[0].id
                    ];

                    printMessage("Posting the following to /interests, as renter: " + prettyPrint(body));

                    functions.showInterest(server, renterToken, responseObject[0].id, function() {

                        printMessage("Tenant getting renters who have shown interest.");

                        functions.getInterests(server, tenantToken, {}, function(responseObject) {

                            printMessage("Result of getting the renters interested in the logged in tenant: " + prettyPrint(responseObject));

                            functions.showDisinterest(server, tenantToken, 5, function(responseObject) {

                                printMessage("Result of showing disinterest towards renter: " + prettyPrint(responseObject));

                                functions.getInterests(server, tenantToken, {}, function(responseObject) {

                                    printMessage("Result of getting the renters interested in the logged in tenant after showing disinterest: " + prettyPrint(responseObject));

                                })
                            })

                        })

                    })

                });

            })

        });

    })

});
