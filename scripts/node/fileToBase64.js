const fs = require('fs');

// From: https://stackoverflow.com/questions/24523532/how-do-i-convert-an-image-to-a-base64-encoded-data-url-in-sails-js-or-generally
const toBase64 = function(file) {

    let bitmap = fs.readFileSync(file);
    return new Buffer(bitmap).toString('base64');

};

module.exports = {
    toBase64: toBase64
};