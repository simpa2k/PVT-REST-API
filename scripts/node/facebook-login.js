let functions = require('./functions');

let server = 'http://localhost:9000';
let facebookToken = 'EAAa2D7XPp6oBANnYLx3fM1ZBeCPuZCP0Q8749cWiTWPKyAgCrdDQ5ZBtr7JC0HmwbdopnHzYH3ZAZC9MMhBAmim9JQFABrtWtUxqWtETYuJrh0TRQinhuZC1GF0bUMkVhLMsDM8tO8jjqxWhb3LhOmJgblfgKc1yz6f6ZCXO1VsE8aTQCH7LxVv6K5mZA5iZAlHEZD';

functions.facebookLogin(server, facebookToken, function(responseObject) {
        console.log(responseObject);
    }
);
