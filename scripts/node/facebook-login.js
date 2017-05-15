let functions = require('./functions');

let server = 'http://localhost:8080';
let facebookToken = 'EAAa2D7XPp6oBAId1IIQjj2iMoZBxz4yWYriYnxXSZAHssNT8Qpsg2QlDZAzdtuw3qfcfNjH8HvHaDKqALAcXMyOAev1iUTyvgVwOLP3calNZC30TnHMiOBZBOzvOU1Hwmhc5FgmWvlIU1wZBffkWpeZAz5bJkcsyIgZD';

functions.facebookLogin(server, facebookToken, function(responseObject) {
        console.log(responseObject);
    }
);
