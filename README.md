# Questions and Answers

At a recent project a questionnaire was built for customers to open bank accounts. 
It supported a number of different types of questions, validation rules that depended on anmswers to other questions, a mechanism to establish
how _complete_ the questionnaire was, visibility of questions and sections of the questionnaire depending on answers to other questions.

It became apparent that this is not as straightforward as it first seems _if you want a type-safe, extensible model_.
Everyone felt certain there was a better way.

This project is an exploration of different ways to model the problem, and also has a more complete and usable implementation.

For details of the problem see the README.md in the _experimental_ module. 

For the more usable library see the _questionnaire_ module.

# Contributions

I, and several others, would be very interested to see solutions to the general problem. So please send pull requests, just add a new package to the code in the
_experimental_ module, and some documentation.

-- Channing
