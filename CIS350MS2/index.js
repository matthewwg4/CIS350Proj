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
	// construct the Person from the form data which is in the request body
	var newUser = new User ({
		userName: req.body.username,
		password: req.body.password,
	});

	// save the person to the database
	newUser.save( (err) => { 
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
		    res.render('created', {user : newUser});
		}
	} ); 	
});

// Displays the user with username ':name'
app.use('/user/:name', (req, res) => {
	// console.log("reached");
	// res.send(req.params.name);
	User.findOne({userName: req.params.name}, (err, user) => {
		if (err) {res.type('html').status(500);res.send('Error: ' + err);}
		else {
			res.render('viewUserInfo', {user : user})
		}
	});
});

app.use('/view', (req, res) => 
	User.find( (err, allUsers) => {
		if (err) {res.type('html').status(500);res.send('Error: ' + err); 
	}
	else if (allUsers.length == 0) {res.type('html').status(200);res.send('There are no users');
}
else {res.render('viewAll', { user: allUsers})};
}
));


app.use( /*default*/ (req, res) => {  res.status(404).send('Not found!');});


// route for accessing data via the web api
// to use this, make a request for /api to get an array of all Person objects
// or /api?name=[whatever] to get a single object
// app.use('/api', (req, res) => {
	// console.log("LOOKING FOR SOMETHING?");

	// // construct the query object
	// var queryObject = {};
	// if (req.query.name) {
	    // // if there's a name in the query parameter, use it here
	    // queryObject = { "name" : req.query.name };
	// }

	// Person.find( queryObject, (err, persons) => {
		// console.log(persons);
		// if (err) {
		    // console.log('uh oh' + err);
		    // res.json({});
		// }
		// else if (persons.length == 0) {
		    // // no objects found, so send back empty json
		    // res.json({});
		// }
		// else if (persons.length == 1 ) {
		    // var person = persons[0];
		    // // send back a single JSON object
		    // res.json( { "name" : person.name , "age" : person.age } );
		// }
		// else {
		    // // construct an array out of the result
		    // var returnArray = [];
		    // persons.forEach( (person) => {
			    // returnArray.push( { "name" : person.name, "age" : person.age } );
			// });
		    // // send it back as JSON Array
		    // res.json(returnArray); 
		// }
		
	    // });
    // });




    /*************************************************/




    app.listen(3000,  () => {
    	console.log('Listening on port 3000');
    });