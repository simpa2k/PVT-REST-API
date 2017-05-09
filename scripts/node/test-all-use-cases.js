let functions = require('./functions');

let server = 'http://localhost:9000';

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

    let profileOptions = {
        headers: {
            'Content-Type': 'application/json',
            'X-AUTH-TOKEN': tenantToken
        },
        body: {

            description: "Hej, jag heter Kalle och behöver någonstans att bo.",
            maxRent: 5000,
            maxDeposit: 8000,
            rentalPeriod: {
                start: "2017-05-01",
                end: "2018-05-01"
            }

        }
    };

    printMessage("Posting the following to /users/profiles, as tenant: " + prettyPrint(profileOptions));

    profileOptions.body = JSON.stringify(profileOptions.body);

    functions.performPostRequest(server, '/users/profiles', profileOptions, function() {

        functions.facebookLogin(server, renterFacebookToken, function(responseObject) {

            printMessage("Response object after logging in renter via Facebook: " + prettyPrint(responseObject));
            renterToken = responseObject.authToken;

            let accommodationOptions = {
                headers: {
                    'Content-Type': 'application/json',
                    'X-AUTH-TOKEN': renterToken
                },
                body: {

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

                }
            };

            printMessage("Posting the following to /accommodation, as renter: " + prettyPrint(accommodationOptions));

            accommodationOptions.body = JSON.stringify(accommodationOptions.body);

            functions.performPostRequest(server, '/accommodation', accommodationOptions, function() {

                functions.performAuthenticatedGetRequest(server, '/users', renterToken, {

                    maxRent: 5000

                }, function(responseObject) {

                    printMessage("Result of getting tenants filtered by max rent: " + prettyPrint(responseObject));

                    let chooseTenantOptions = {
                        headers: {
                            'Content-Type': 'application/json',
                            'X-AUTH-TOKEN': renterToken
                        },
                        body: [
                            responseObject[0].id
                        ]
                    };

                    printMessage("Posting the following to /accommodation, as renter: " + prettyPrint(chooseTenantOptions));

                    chooseTenantOptions.body = JSON.stringify(chooseTenantOptions.body);

                    functions.performPostRequest(server, '/interests', chooseTenantOptions, function() {

                        functions.performAuthenticatedGetRequest(server, '/interests', tenantToken, {}, function(responseObject) {

                            printMessage("Result of getting the renters interested in the logged in tenant: " + prettyPrint(responseObject));

                        })

                    })

                });

            })

        });

    })

});
