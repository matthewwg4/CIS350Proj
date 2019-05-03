/* User.js */

var mongoose = require('mongoose');

// the host:port must match the location where you are running MongoDB
// the "myDatabase" part can be anything you like
mongoose.connect('mongodb://localhost:27017/myDatabase');

var Schema = mongoose.Schema;

var userSchema = new Schema({
	userName: {type: String, required: true, unique: true},
	password: {type: String, required: true},
	habits: {type: Array}
});
	
// export userSchema as a class called User
module.exports = mongoose.model('User', userSchema);