let functions = require('./functions');

let server = 'http://localhost:8080';

let tenantFacebookToken = 'EAAa2D7XPp6oBAId1IIQjj2iMoZBxz4yWYriYnxXSZAHssNT8Qpsg2QlDZAzdtuw3qfcfNjH8HvHaDKqALAcXMyOAev1iUTyvgVwOLP3calNZC30TnHMiOBZBOzvOU1Hwmhc5FgmWvlIU1wZBffkWpeZAz5bJkcsyIgZD';
let renterFacebookToken = 'EAAa2D7XPp6oBAKRKHXPKOJ1Upvm1je5zl7SiMxnnujkYRDxGZB2X8UpTl6dZCDBZApKDKdawdiBvgayezVSM96p6ZCZBYropI7dkZAnSJ71K7WJWP4GVGUWekzHstgIaRQ7NPzVO1uIlo9lRQUlNXwqZCwoy6SvgMPt4rgN7rKS6AZDZD';

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

                    printMessage("Posting the following to /accommodation, as renter: " + prettyPrint(body));

                    functions.chooseTenants(server, renterToken, body, function() {

                        printMessage("Tenant getting renters who has shown interest.");

                        functions.performAuthenticatedGetRequest(server, '/interests', tenantToken, {}, function(responseObject) {

                            printMessage("Result of getting the renters interested in the logged in tenant: " + prettyPrint(responseObject));

                        })

                    })

                });

            })

        });

    })

});
