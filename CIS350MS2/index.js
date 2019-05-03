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
//var Habit = require('./Habit.js');

var Survey = require('./Survey.js')

/***************************************/
class Habit {
	constructor(name, type, unit) {
		this.habitName = name;
		this.type = type;
		this.unit = unit;
		this.tags = [];
		this.dailyEntries = [];
	}
}

//daily entries
class InfoPoint {
	constructor(timestamp, amount, isDone, happiness) {
		this.time = timestamp;
		if (amount) {
			this.amount = parseFloat(amount);
		}
		if (isDone) {
			this.isDone = ("true" == isDone.toLowerCase() || "yes" == isDone.toLowerCase() || "y" == isDone.toLowerCase());
		}
		this.happiness = parseInt(happiness);
	}
}

class userResponse {
	constructor(username, response) {
		this.username = username;
		this.response = response;
	}
}

/* Index page with links to add user,
view all users, add survey, and
view all surveys
*/
app.use('/public', express.static('public'));

/* route for creating a new user
username and password are input
this is the action of the addUserForm */
app.use('/addUser', (req, res) => {
	var newUser = new User({
		userName: req.body.username, //requesting the body to have a username
		password: req.body.password,
		habits: []
	});
	// save the person to the database
	newUser.save((err) => {
		if (err) {
			res.render('addUserFailed');
		} else {
			// display the "successfully created" page using EJS
			res.render('created', { user: newUser }); //key is user; data is newUser; render is like print
		}
	});
});

/* Displays the info about the user with username ':name' 
including username, password, habits link, and delete user
username and password can also be updated here */
app.use('/user/:name', (req, res) => {
	User.findOne({ userName: req.params.name }, (err, user) => { //response is from the database, not the user/client
		if (err) { 
			res.type('html').status(500); res.send('Error: ' + err); 
		} else if (user == null) {
			res.send(req.params.name);
		} else {
			res.render('viewUserInfo', { user: user })
		}
	});
});

//view list of all users (with links to their info)
app.use('/view', (req, res) =>
	User.find((err, allUsers) => {
		if (err) {
			res.type('html').status(500); res.send('Error: ' + err);
		} else if (allUsers.length == 0) {
			res.type('html').status(200); res.send('There are no users');
		} else { res.render('viewAll', { user: allUsers }) };
	}
));

/* page that appears after a user is deleted and
removed from the database */
app.use('/deleteUser/:name', (req, res) => {
	var query = { userName: req.params.name };
	var userDeleted = req.params.name;
	User.deleteOne(query, (err, user) => {
		if (err) throw err;
		else { res.render('deleteUserFinished', { userDeleted }); }
	})
});

/* updates user with username :name to have
new username based on the request body */
app.use('/updateUsername/:name', (req, res) => {
	User.findOne({ userName: req.params.name }, (err, user) => {
		if (err) { 
			res.type('html').status(500); res.send('Error: ' + err); 
		} else if (user == null) {
			res.send('cannot find the user with this name');
		} else {
			var oldName = user.userName;
			var newName = req.body.newUsername;
			if (user != null) user.userName = newName;
			user.save((err) => {
				if (err) {
					user.userName = oldName;
					res.render('updateNameFailed', { user: user });
				} else {
					res.render('updateUsername', { user: user });
				}
			});
		}
	});
})

/* updates user with username :name to have
new password based on the request body */
app.use('/updatePassword/:name', (req, res) => {
	User.findOne({ userName: req.params.name }, (err, user) => {
		if (err) { 
			res.type('html').status(500); res.send('Error: ' + err); 
		} else if (user == null) {
			res.send('cannot find the user with this name');
		} else {
			var oldPassword = user.password;
			var newPassword = req.body.newPassword;
			if (user != null) user.password = newPassword;
			user.save((err) => {
				if (err) {
					user.password = oldPassword;
					res.render('updatePasswordFailed', { user: user });
				} else {
					res.render('updatePassword', { user: user });
				}
			});
		}
	});
});

/* goes to page that displays the user's habits
(clicking on a habit will go to a new page with the habit info)
a new habit can also be added */
app.use('/goToUserHabits/:name', (req, res) => {
	User.findOne({ userName: req.params.name }, (err, user) => { 
		if (err) {
			res.type('html').status(500); res.send('Error: ');
		} else {
			res.render('goToUserHabits', { user: user });
		}
	});
});

/* adds the habit to the user's list of habits
shows page with list of user's updated habits 
a new habit can again be added */
app.use('/addHabit/:name', (req, res) => {
	User.findOne({ userName: req.params.name }, (err, user) => {
		if (err) { 
			res.type('html').status(500); res.send('Error: ' + err); 
		} else if (user == null) {
			res.send('cannot find the user with this name');
		} else {
			var unique = true;
			user.habits.forEach(hab => {
				if (req.body.habitName == hab.habitName) {
					unique = false;
				}
			});
			if (unique) {
				var newHabit = new Habit(req.body.habitName, req.body.type, req.body.unit);
				if (user != null) {
					user.habits.push(newHabit);
				}
				user.save((err) => {
					if (err) {
						res.type('html').status(500); res.send(err);
					} else {
						res.render('goToUserHabits', { user: user });
					}
				})
			} else {
				res.render('addHabitFailed', { user: user });
			}
		}
	});
});

/* page that displays the name, type, tags, and link to info points
of the habit with name :habit of user with username :name  
habit can also be deleted on this page */
app.use('/habit/:name/:habit', (req, res) => {
	User.findOne({userName: req.params.name}, (err, user) => {
		if (err) { 
			res.type('html').status(500); res.send('Error: ' + err); 
		} else if (user == null) {
			res.send("Couldn't find user with that name");
		} else {
			var h = null;
			user.habits.forEach(hab => {
				if (req.params.habit == hab.habitName) {
					h = hab;
				}
			});
			res.render('viewHabitInfo', {habit: h, username: req.params.name});
		}
	});
});

//updates the name of the habit with original name :habit
app.use('/updateHabitName/:user/:habit', (req, res) => {
	User.findOne({ userName: req.params.user }, (err, user) => {
		if (err) { 
			res.type('html').status(500); res.send('Error: ' + err); 
		}	else if (user == null) {
			res.send('Cannot find the user with that name');
		} else {
			var holdHabit = null;
			var replicateName = false;
			user.habits.forEach(hab => {
				if (req.params.habit == hab.habitName) {
					holdHabit = hab;
				}
			});
			user.habits.forEach(hab => {
				if (req.body.newHabitname == hab.habitName) {
					replicateName = true;
				}
			});
			if (!replicateName) {
				holdHabit.habitName = req.body.newHabitname;
			}
			user.save((err) => {
				if (err || replicateName) {
					res.render('updateHabitNameFailed', { habit: holdHabit, user: req.params.user }); 
				} else {
					res.render('updateHabitName', { habit: holdHabit, user: req.params.user });
				}
			})
		}
	});
});

/*adds a tag to the user's habit's tags
the tag will appear in the updated list of tags on that page */
app.use('/addTag/:name/:habit', (req, res) => {
	User.findOne({ userName: req.params.name }, (err, user) => {
		if (err) { 
			res.type('html').status(500); res.send('Error: ' + err); 
		}	else if (user == null) {
			res.send('cannot find the user with this name');
		} else {
			var habit = undefined;
			user.habits.forEach(hab => {
				if (hab.habitName == req.params.habit) {
					hab.tags.push(req.body.newTag);
					habit = hab;
				}
			});
			User.update({ userName: user.userName}, { habits : user.habits}, (err) => {
				if (err) {
					res.type('html').status(500); res.send('Error: ');
				} else {
					res.render('viewHabitInfo', { habit: habit, username: req.params.name })
				}
			})
		}
	});
});

//page that appears and removes the habit with name :habit from the database
app.use('/deleteHabit/:name/:habit', (req, res) => {
	var habitDeleted = req.params.habit;
	var successfulDelete = false;
	User.findOne({ userName: req.params.name }, (err, user) => {
		if (err) {
			res.type('html').status(500); res.send('Error: ' + err);
		} else if (user == null) {
			res.send('cannot find the user with that name');
		} else {
			for (let index = 0; index < user.habits.length; index++) {
				const element = user.habits[index];
				if (habitDeleted == element.habitName) {
					user.habits.splice(index, 1);
					successfulDelete = true;
				}
			}
			user.save((err) => {
				if (err || !successfulDelete) {
					res.type('html').status(500); res.send('Error:' + err);
				} else {
					res.render('deleteHabitFinished', {habitDeleted, name: req.params.name }); 
				}
			})
		}
	})
});

/* goes to page with daily entry info
shows the list of entries and options to add an entry */
app.use('/goToInfoPoints/:name/:habit', (req, res) => {
	User.findOne({userName: req.params.name}, (err, user) => {
		if (err) { 
			res.type('html').status(500); res.send('Error: ' + err); 
		}	else if (user == null) {
			res.send("Couldn't find user with that name");
		} else {
			var h = null;
			user.habits.forEach(hab => {
				if (req.params.habit == hab.habitName) {
					h = hab;
				}
			});
			res.render('viewInfoPoints', {habit: h, username: req.params.name});
		}
	});
});

app.use('/addInfoPoint/:name/:habit', (req, res) => {
	User.findOne({ userName: req.params.name }, (err, user) => {
		if (err) { res.type('html').status(500); res.send('Error: ' + err); }
		else if (user == null) {
			res.send('cannot find the user with this name');
		} else {
			var habit = undefined;
			user.habits.forEach(hab => {
				if (hab.habitName == req.params.habit) {
					var info = new InfoPoint(req.body.timestamp, req.body.amount, req.body.isDone, req.body.happiness);
					hab.dailyEntries.push(info);
					habit = hab;
				}
			});
			User.update({ userName: user.userName}, { habits : user.habits}, (err) => {
				if (err) {
					res.type('html').status(500); res.send('Error: ');
				} else {
					res.render('viewInfoPoints', { habit: habit, username: req.params.name })
				}
			})
		}
	});
});

app.use('/surveys', (req, res) =>
	Survey.find((err, allSurveys) => {
		if (err) {
			res.type('html').status(500); res.send('Error: ' + err);
		} else if (allSurveys.length == 0) {
			res.type('html').status(200); res.send('There are no surveys');
		}
		else { res.render('viewSurveys', { survey: allSurveys }) };
	}
	));
	
app.use('/addSurvey', (req, res) => {
	if(req.body.option1.length == 0 || req.body.option2.length == 0) {
			res.render('addSurveyFailed');
	} else {
		var newSurvey = new Survey({
		surveyName: req.body.surveyName,
		question: req.body.question,
		options: [req.body.option1, req.body.option2],
		userResponses: []
	});

	// save the survey to the database
	newSurvey.save((err) => {
		if (err) {
			res.render('addSurveyFailed');
		}
		else {
			// display the "successfull created" page using EJS
			res.render('surveyCreated', { survey: newSurvey });
		}
	});
	}

});	

app.use('/survey/:name', (req, res) => {
	Survey.findOne({ surveyName: req.params.name }, (err, survey) => {
		if (err) { res.type('html').status(500); res.send('Error: ' + err); }
		else {
			res.render('viewSurveyInfo', { survey: survey })
		}
	});
});

app.use('/deleteSurvey/:name', (req, res) => {
	var query = {surveyName: req.params.name};
	var surveyDeleted = req.params.name;
	Survey.deleteOne(query, (err, survey) => {
		if (err) throw err;
		else {res.render('deleteSurveyFinished', {surveyDeleted});}
	})
});

app.use('/updateQuestion/:name', (req, res) => {
	Survey.findOne({ surveyName: req.params.name }, (err, survey) => {
		if (err) { res.type('html').status(500); res.send('Error: ' + err); }
		else if (survey == null) {
			res.send('cannot find the survey with this name');
		} else {
			var oldQuestion = survey.question;
			var newQuestion = req.body.newQuestion;
			if (survey != null) survey.question = newQuestion;
			survey.save((err) => {
				if (err) {
					survey.question = oldQuestion;
					res.render('updateQuestionFailed', {survey : survey});
				}
				else {
					res.render('updateQuestion', { survey: survey });
				}
			});
		}
	});
})

app.use('/updateOption/:name/:option', (req, res) => {
	Survey.findOne({ surveyName: req.params.name }, (err, survey) => {
		if (err) { res.type('html').status(500); res.send('Error: ' + err); }
		else if (survey == null) {
			res.send('cannot find the survey with this name');
		} else {
			var oldOption = req.params.option;
			var newOption = req.body.newOption;
			function isSameStringOld(entry) {
				return entry == oldOption;
			}
			function isSameStringNew(entry) {
				return entry == newOption;
			}
			//set forces db to recognize change within array; just = will not do this
			if (survey != null) {
				survey.options.set(survey.options.findIndex(isSameStringOld), newOption);
			}
			if (newOption.length == 0) {
				survey.options.set(survey.options.findIndex(isSameStringNew), oldOption);
				res.render('updateOptionFailed', {survey : survey});
			} else {
				survey.save((err) => {
				if (err) {
					//YOU SHOULD NEVER GET HERE
					survey.options.set(survey.options.findIndex(isSameStringNew), oldOption);
					res.render('updateOptionFailed', {survey : survey});
				}
				else {
					res.render('updateOption', { survey: survey, option : newOption });
				}
			});
			}
		}
	});
});

app.use('/addOption/:name', (req, res) => {
	Survey.findOne({ surveyName: req.params.name }, (err, survey) => {
	if (err) { res.type('html').status(500); res.send('Error: ' + err); }
	else if (survey == null) {
			res.send('cannot find the survey with this name');
	} else {
			if(req.body.newOption.length == 0) {
			res.render('addOptionFailed', {survey : survey});
			} else {
				if (survey != null) {
				survey.options.push(req.body.newOption);
			}
			survey.save((err) => {
			if (err) {
				//YOU SHOULD NEVER GET HERE
				survey.options.pop();
				res.render('addOptionFailed', {survey : survey});
				}
				else {
					if (survey != null) {
					res.render('optionCreated', {survey : survey, option: req.body.newOption})
				}
				}

		});
	}
	}	
	});
});	

app.use('/userResponses/:name', (req, res) => {
	Survey.findOne({surveyName: req.params.name}, (err, survey) => {
		if (err) {
			res.type('html').status(500); res.send('Error: ' + err);
		}  else if (survey == null) {
			res.send('cannot find the survey with this name');
		}  else {
			if (survey != null) {
			if (survey.userResponses.length == 0) {
			res.type('html').status(200); res.send('There are no user responses');
			}
			else { 
			res.render('viewUserResponses', { survey : survey }); 
			}
			}
		}
	});
});

app.use('/updateResponse/:name/:user', (req, res) => {
	Survey.findOne({ surveyName: req.params.name }, (err, survey) => {
		if (err) { res.type('html').status(500); res.send('Error: ' + err); }
		else if (survey == null) {
			res.send('cannot find the survey with this name');
		} else {
			var oldResponse = req.params.response;
			var newResponse = req.body.newResponse;
			 if (newResponse.length == 0) {
				res.render('updateResponseFailed', {survey: survey, reason: "emptyFields"});
			 } else if(!survey.options.includes(newResponse)) {
				res.render('updateResponseFailed', {survey: survey, reason: "optionMatching"});
			 } else {
				if (survey != null) {
					let resp = survey.userResponses.find(x => x.username === req.params.user);
					let index = survey.userResponses.indexOf(resp);
					survey.userResponses.set(index, new userResponse(req.params.user, newResponse));
				}
				survey.save((err) => {
					if (err) {
					//YOU SHOULD NEVER GET HERE
						res.render('updateResponseFailed', {survey: survey, reason : "HOW DID YOU DO THIS?"});
					}
					else {
						res.render('updateResponse', { survey: survey, response : newResponse });
				}
				});
			}
			}
			});
});

app.use('/addUserResponse/:name', (req, res) => {
	Survey.findOne({ surveyName: req.params.name }, (err, survey) => {
	if (err) { res.type('html').status(500); res.send('Error: ' + err); }
	else if (survey == null) {
			res.send('cannot find the survey with this name');
	} else {
			res.render('addResponseForm', { survey : survey })
	}		
	});
});	

app.use('/createUserResponse/:name', (req, res) => {
	Survey.findOne({ surveyName: req.params.name }, (err, survey) => {
		if (err) { res.type('html').status(500); res.send('Error: ' + err); }
		else if (survey == null) {
			res.send('cannot find the survey with this name');
		} else {
			var newResponse = req.body.response;
			function isSameStringNew(entry) {
				return entry == newResponse;
			}
		    if (req.body.user.length == 0 || newResponse.length == 0) {
				res.render('createResponseFailed', {survey: survey, reason: "emptyFields"});
			 } 
			else if(!survey.options.includes(newResponse)) {
				res.render('createResponseFailed', {survey: survey, reason: "optionMatching"});
			} else  if (req.body.user in survey.userResponses) {
				res.render('createResponseFailed', {survey: survey, reason: "alreadyResponded"});
			}  else {
				if (survey != null) {
				var username = req.body.user;
				survey.userResponses.push(new userResponse(username, newResponse));
				}
				survey.save((err) => {
					if (err) {
					//YOU SHOULD NEVER GET HERE
						res.render('createResponseFailed', {survey: survey, reason : "HOW DID YOU DO THIS?"});
					}
					else {
						res.render('createResponse', { survey: survey, response : newResponse, user : req.body.user });
				}
				});
			}
			}
			});
});

app.use('/api/user', (req, res) => {
	User.findOne({ userName: req.query.name}, (err, user) => {
		if (err) { res.type('html').status(500); res.send('Error: ' + err); }
		else if (user == null) {
			res.send('cannot find the user with this name');
		} else {
			res.send(user);
		}
	});
});

app.use('/api/newuser', (req, res) => {
	var newUser = new User({
		userName: req.query.name, //requesting the body to have a username
		password: req.query.password,
		habits: []
	});

	// save the person to the database
	newUser.save((err) => {
		if (err) {
			res.send('failure');
		}
		else {
			// display the "successfull created" page using EJS
			res.send('success'); 
		}
	});
});

app.use('/api/addHabit', (req, res) => {
	User.findOne({ userName: req.query.name }, (err, user) => {
		if (err) { res.type('html').status(500); res.send('Error: ' + err); }
		else if (user == null) {
			res.send('cannot find the user with this name');
		} else {
			var unique = true;
			user.habits.forEach(hab => {
				if (req.body.habitName == hab.habitName) {
					unique = false;
				}
			});
			if (unique) {
				var newHabit = new Habit(req.query.habitName, req.query.type, req.query.unit);
				if (user != null) {
					user.habits.push(newHabit);
				}
				user.save((err) => {
					if (err) {
						res.send('failure');
					} else {
						res.send('success');
					}
				})
			} else {
				res.send('failure');
			}
		}
	});
});

app.use('/api/addInfoPoint/', (req, res) => {
	User.findOne({ userName: req.query.name }, (err, user) => {
		if (err) { res.type('html').status(500); res.send('Error: ' + err); }
		else if (user == null) {
			res.send('cannot find the user with this name');
		} else {
			var habit = undefined;
			user.habits.forEach(hab => {
				if (hab.habitName == req.query.habit) {
					var info = new InfoPoint(req.query.timestamp, req.query.amount, req.query.isDone, req.query.happiness);
					hab.dailyEntries.push(info);
					habit = hab;
				}
			});
			User.update({ userName: user.userName}, { habits : user.habits}, (err) => {
				if (err) {
					res.send('failure');
				} else {
					res.send('success');
				}
			})
		}
	});
});

//this is from android app
app.use('/android/:name/:user/:response', (req, res) => {
	Survey.findOne({ surveyName: req.params.name }, (err, survey) => {
		if (err) { res.type('html').status(500); res.send('Error: ' + err); }
		else if (survey == null) {
			res.send('cannot find');
		} else {
			var newResponse = req.params.response;
				survey.userResponses.set(req.params.user, newResponse);
				survey.save((err) => {
					if (err) {
					res.send("error");
					}
					else {
						res.send("success");
				}
				});
			}
			});
});
app.use(/*default*/(req, res) => { res.status(404).send('Not found!'); });

/*************************************************/

app.listen(3000, () => {
	console.log('Listening on port 3000');
});
