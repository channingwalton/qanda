# Questions and Answers

At a recent project, my team produced a questionnaire customers needed to fill in to open bank accounts. 
It supported a number of different types of questions, with validation rules and a mechanism to establish
how _complete_ the questionnaire was.

It became apparent that this is not as straight forward as it first seems, _if you want a type-safe, extensible model_.
Everyone felt certain there was a better way. This project is an exploration of different ways to model the problem.

## The Problem

The model must support the following:

1. Questions whose answers are of different types: Strings, Dates, Booleans, Address, Phone Number, etc. Note that these are not all primitives.
2. Questions can be optional
3. Questions must support multiple Answers - eg. phone numbers
4. A measure of how complete the questionnaire is - all non-optional questions must be answered
5. Validation rules
  * string longer than a certain length
  * age greater than 16, etc.
  * inter question validation:
    * current address does not match the address you gave on your identity document
    * You haven't given at least 3 years address history
  * essentially an arbitrarily complex expression for a question using its current answer _and_ any other answer provided so far
6. Given a list of questions of different types, produce different representations of the questionnaire:
  * pretty print
  * ui components (as html)
  * json
  * all the protocols

## Oh, and one more thing …

Your model should be open to extension. That is, solve the [expression problem](http://homepages.inf.ed.ac.uk/wadler/papers/expression/expression.txt):
> The Expression Problem is a new name for an old problem.  The goal is
> to define a datatype by cases, where one can add new cases to the
> datatype and new functions over the datatype, without recompiling
> existing code, and while retaining static type safety (e.g., no
> casts).
> 
> Phil Wadler.

# Contributions

I, and several others, would be very interested to see solutions. So please send pull requests, just add a new package containing
code of course, and some documentation.

-- Channing
