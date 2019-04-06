var mongoose = require('mongoose');

// the host:port must match the location where you are running MongoDB
// the "myDatabase" part can be anything you like
mongoose.connect('mongodb://localhost:27017/myDatabase');

var Schema = mongoose.Schema;

var habitSchema = new Schema({
	habitId: {type: String, required: true, unique: true},
	habitName: {type: String, required: true},
	type: {type: String, required: true},
	tags: [{
		type: String
	}],
	infoPoints: [{
		type: String
	}]
    });

// export habitSchema as a class called Habit
module.exports = mongoose.model('Habit', habitSchema);
