let functions = require('./functions');

let server = 'http://localhost:9000';
let facebookToken = 'EAAa2D7XPp6oBAIuqPZAyZCQMLLE8xFjEUrHj2meZBZB61cqcD5IAYrodvLV0xxyNoFMJicbJCQCggEUHmE0DZCxOu3DZBtGOX581iZBYzxObVT45Gx6FKdkJfJNVhBHUjf4htlUAH01SZBqet5UKaph4MSKYwtZAh9lLVcAFZBVKZC0bIpTEbTtKWET';

functions.facebookLogin(server, facebookToken, function(responseObject) {
        console.log(responseObject);
    }
);
