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

var Survey = require('./Survey.js')

/***************************************/
app.use('/public', express.static('public'));
// route for showing all the people

// route for creating a new user
// this is the action of the "create new person" form

app.use('/addUser', (req, res) => {
	var newUser = new User({
		userName: req.body.username,
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
			res.render('created', { user: newUser });
		}
	});
});

// Displays the user with username ':name'
app.use('/user/:name', (req, res) => {
	User.findOne({ userName: req.params.name }, (err, user) => {
		if (err) { res.type('html').status(500); res.send('Error: ' + err); }
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
	}
	));

app.use('/deleteUser/:name', (req, res) => {
	var query = {userName: req.params.name};
	var userDeleted = req.params.name;
	User.deleteOne(query, (err, user) => {
		if (err) throw err;
		else {res.render('deleteUserFinished', {userDeleted});}
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
					res.render('updateNameFailed', {user : user});
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
					res.render('updatePasswordFailed', {user : user});
				}
				else {
					res.render('updatePassword', { user: user });
				}
			});
		}
	});
})

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
		userResponses: new Map()
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

// app.use('/updateSurveyname/:name', (req, res) => {
	// Survey.findOne({ surveyName: req.params.name }, (err, survey) => {
		// if (err) { res.type('html').status(500); res.send('Error: ' + err); }
		// else if (survey == null) {
			// res.send('cannot find the survey with this name');
		// } else {
			// var oldName = survey.surveyName;
			// var newName = req.body.newSurveyName;
			// if (survey != null) survey.surveyName = newName;
			// survey.update((err) => {
				// if (err) {
					// survey.surveyName = oldName;
					// res.render('updateSurveyNameFailed', {survey : survey});
				// }
				// else {
					// res.render('updateSurveyname', { survey: survey });
				// }
			// });
		// }
	// });
// })

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
			if (survey.userResponses.size == 0) {
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
			}    else {
				if (survey != null) {
				survey.userResponses.set(req.params.user, newResponse);
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
			} else  if (survey.userResponses.has(req.body.user)) {
				res.render('createResponseFailed', {survey: survey, reason: "alreadyResponded"});
			}  else {
				if (survey != null) {
				survey.userResponses.set(req.body.user, newResponse);
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

app.use( /*default*/(req, res) => { res.status(404).send('Not found!'); });

/*************************************************/

app.listen(3000, () => {
	console.log('Listening on port 3000');
});
