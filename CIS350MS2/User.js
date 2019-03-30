var mongoose = require('mongoose');

// the host:port must match the location where you are running MongoDB
// the "myDatabase" part can be anything you like
mongoose.connect('mongodb://localhost:27017/myDatabase');

var Schema = mongoose.Schema;

var userSchema = new Schema({
	userName: {type: String, required: true, unique: true},
	password: String
	habits: []
    });

// export userSchema as a class called User
module.exports = mongoose.model('User', userSchema);

// Tea: the part belows follow Person.js, but I think it is
// not necessary so temporarily it is commented out

// userSchema.methods.standardizeName = function() {
//     this.name = this.name.toLowerCase();
//     return this.name;
// }
