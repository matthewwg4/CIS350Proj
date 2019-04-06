// set up Express
var express = require('express');
var app = express();

// set up EJS
app.set('view engine', 'ejs');

// set up BodyParser
var bodyParser = require('body-parser');
app.use(bodyParser.urlencoded({ extended: true }));

// import the User class from User.js
var User = require('./User.js');

/***************************************/
app.use('/public', express.static('public'));
// route for showing all the people

// route for creating a new user
// this is the action of the "create new person" form

app.use('/addUser', (req, res) => {
	var newUser = new User({
		userName: req.body.username, //requesting the body to have a username
		password: req.body.password,
	});

	// save the person to the database
	newUser.save((err) => {
		if (err) {

			//Jialin's original code
			// res.type('html').status(200);
			// res.write('uh oh: ' + err);
			// console.log(err);
			// res.end();

			// Tea's code:
			res.render('addUserFailed');
		}
		else {
			// display the "successfull created" page using EJS
			res.render('created', { user: newUser }); //key is user; data is newUser; render is like print
		}
	});
});

// Displays the user with username ':name'
app.use('/user/:name', (req, res) => {
	User.findOne({ userName: req.params.name }, (err, user) => { //response is from the database, not the user/client
		if (err) { res.type('html').status(500); res.send('Error: ' + err); }
		else if (user == null) {
			res.send(req.params.name);
		}
		else {
			res.render('viewUserInfo', { user: user })
		}
	});
});

app.use('/view', (req, res) =>
	User.find((err, allUsers) => {
		if (err) {
			res.type('html').status(500); res.send('Error: ' + err);
		} else if (allUsers.length == 0) {
			res.type('html').status(200); res.send('There are no users');
		}
		else { res.render('viewAll', { user: allUsers }) };
		//res.status(400).send();
	}
		//	}
	));

app.use('/deleteUser/:name', (req, res) => {
	var query = { userName: req.params.name };
	var userDeleted = req.params.name;
	User.deleteOne(query, (err, user) => {
		if (err) throw err;
		else { res.render('deleteUserFinished', { userDeleted }); }
	})
});

app.use('/updateUsername/:name', (req, res) => {
	User.findOne({ userName: req.params.name }, (err, user) => {
		if (err) { res.type('html').status(500); res.send('Error: ' + err); }
		else if (user == null) {
			res.send('cannot find the user with this name');
		} else {
			var oldName = user.userName;
			var newName = req.body.newUsername;
			if (user != null) user.userName = newName;
			user.save((err) => {
				if (err) {
					user.userName = oldName;
					res.render('updateNameFailed', { user: user });
				}
				else {
					res.render('updateUsername', { user: user });
				}
			});
		}
	});
})

app.use('/updatePassword/:name', (req, res) => {
	User.findOne({ userName: req.params.name }, (err, user) => {
		if (err) { res.type('html').status(500); res.send('Error: ' + err); }
		else if (user == null) {
			res.send('cannot find the user with this name');
		} else {
			var oldPassword = user.password;
			var newPassword = req.body.newPassword;
			if (user != null) user.password = newPassword;
			user.save((err) => {
				if (err) {
					user.password = oldPassword;
					res.render('updatePasswordFailed', { user: user });
				}
				else {
					res.render('updatePassword', { user: user });
				}
			});
		}
	});
});

app.use('/goToUserHabits/:name', (req, res) => {
	User.findOne({ userName: req.params.name }, (err, user) => { //response is from the database, not the user/client
		if (err) {
			res.type('html').status(500); res.send('Error: ');
		} else {
			res.render('goToUserHabits', { user: user });
		}
	});
});

app.use('/addHabit/:name', (req, res) => {
	User.findOne({ userName: req.params.name }, (err, user) => {
		if (err) { res.type('html').status(500); res.send('Error: ' + err); }
		else if (user == null) {
			res.send('cannot find the user with this name');
		} else {
			user.habits[user.habits.length] = req.body.habitName;
			res.send(req.body.habitName);
		}
	});
});

app.use( /*default*/(req, res) => { res.status(404).send('Not found!'); });

/*************************************************/

app.listen(3000, () => {
	console.log('Listening on port 3000');
});
