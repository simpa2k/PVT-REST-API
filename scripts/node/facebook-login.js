let functions = require('./functions');

let server = 'http://localhost:9000';
let facebookToken = 'EAAa2D7XPp6oBAPsZC1uSmZA9M0t8NU1O1PyWKyYde61ijXj4EZCupt8XZCoVJveP7ZAEf7uNj72fniOS4K6xCHTAw9C6Vb8ZCzcs3On8OO3R2lDGpbgAZC7qRXCQbMvbVPk4HVKsW92GMJxOSL3nWdgzQvHZAUMUcvpDvSgWSZCOsRZBmsSZA4cPZAsG';

functions.facebookLogin(server, facebookToken, function(responseObject) {
        console.log(responseObject);
    }
);
