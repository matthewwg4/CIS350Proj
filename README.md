# 350S19-22

HOW TO RUN

To run the app, start up MongoDB in your command terminal with: .\bin\mongod --dbpath [path
to database]

Then, start up Node in your command terminal by changing the directory to the one that
contains index.js and running: node index.js.

Then, run the android app from the android studio emulator.

DOCUMENT OF CLASSES

The HabitTracker class is an abstract class that is extended by the BinaryHabitTracker and
NumericalHabitTracker classes. These classes store the methods and data of habits.
HabitTracker stores a list of DateInfo. The HabitTracker class also uses the HabitType enum to
get the habit type when it is unknown.

The DateInfo class stores the input within a day for a data point within a habit.
HabitType is an enum that differentiates between binary and numerical habits.

The Resource class defines an object that stores information about a real-life resource (the
name, tags, and infomation).

The Survey class defines an object that stores information about a survey (the question,
options, and responses)

The FakeSurveyDatabase stores the surveys from the database. It also holds methods to
access an instance of that FakeSurveyDatabase or a specific survey, populate it with
information from the database, or update the mongo survey database with information from the
app.

The FakeResourceDatabase holds the resources hardcoded into the app. Currently, when
adding a new resource, the information is hardcoded directly into this class by calling the
Resource constructor. It also holds methods to access an instance of FakeResourceDatabase
or a specific resource.

The FakeTemplateDatabase holds the templates hardcoded into the app. Currently, when
adding a new habit template, the information is hardcoded directly into this class by adding a
“template” (name and enum) to the FakeTemplateDatabase. This class has methods to get an
instance of FakeTemplateDatabase or a specific template.

The UserEntry class stores information about a user, as well as all of the habit information
associated with that user, which is stored in a set of HabitTrackers.

The DataSource class stores UserEntry information. When a getUser() call is made, the cache
is checked for the UserEntry being searched for, and if the UserEntry is not in the cache, the
UserEntry information is retrieved (if it exists) from the mongo database and added to the
cache.

The first activity that begins when the app starts is the LoginActivity. The LoginActivity allows
the user to input a username and password and either attempt to create a new account or
attempt to login. If the user tries to create a new account, the app checks the mongo user
database. If the username already exists, a user will not be created. Otherwise, a new user will
be added to the database. If the user tries to login, the app will check to see if the username is
in the database, and if that user has the inputted password. If not, the app will not let the user
log in. If so, the login will be a success and the LoginActivity will launch MenuActivity and
initialize the user that logged in by calling DataSource and creating a UserEntry. Every activity
launched from now on will pass on the user’s name in the intent.

When MenuActivity begins, the app gets the surveys from the mongo survey database, creates
new Surveys with that information, and populates FakeSurveyDatabase with those surveys.
From MenuActivity, the user can click on “Habits”, “Visualization”, “Resources”, or “Surveys”.
These will launch HabitsActivity, TrendviewerActivity, ResourceActivity, and SurveyActivity
respectively.

HabitsActivity allows the user to add a new habit (launching AddExistingHabitActivity) or view
the user’s current habits (launching CurrentHabitsActivity).
HabitForm shows all of the data for a specific habit by accessing the cached user (UserEntry)
and allows the user to add data to the habit (by launching AddData). It uses the HabitTracker
and DataSource classes.

AddData is an activity class that allows the user to add a data point to a habit. It uses the
classes: HabitTracker, DataSource, DateInfo, and UserEntry. It updates the cached user with
the new data point.

AddExistingHabitActivity adds a habit to the cached user’s set of habits. It uses the classes:
HabitType, HabitTracker, FakeTemplateDatabase, and DataSource.

CurrentHabitsActivity displays a list of habits that a user has. It uses the HabitTracker and
DataSource classes.

TrendViewerActivity accesses the UserEntry to show a list of Habits and generates a graph
showing the data points of the selected habits

SurveyActivity shows a list of all current surveys from the FakeSurveyDatabase. Clicking on any
survey will launch CustomSurveyActivity. In addition to passing the user’s name, this launch will
also pass the survey’s name.

CustomSurveyActivity allows you to enter a response to a specific survey in the

FakeSurveyDatabase (the one passed by SurveyActivity). This activity accesses the mongo
survey database to update the survey there with the new response.

ResourceActivity shows a list of all current resources from the FakeResourceDatabase and
allows the user to search for resources by tag or name. Clicking on a resource launches

CustomResourceDatabase, passing it the name of the resource clicked on. Searching for
something launches ResourceSearchResultActivity, passing it the string entered in the search
bar.

CustomResourceActivity allows the user to view information about the resource (from the
FakeResourceDatabase) that the user has clicked on.

ResourcesSearchResultActivity displays all the resources in the FakeResourceDatabase whose
name contains the string passed to the activity or which contain a tag matching that string

HabitViewAdapter helps TrendViewerActivity display a list of habits in a recycler view

TrendViewerActivity allows the user to see a graph of a habit and choose a habit to be
displayed on the graph
