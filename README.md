# Questions and Answers

At a recent project, my team needed to produce a questionnaire customers needed to fill in to open bank accounts. 
It had to support a number of different types of questions, with validation rules and a mechanism to establish
how _complete_ the questionnaire was.

It became apparent that this is not as straight forward as it first seems, _if you want a type-safe, extensible model_.
It also is a fun playground to experiment with a number of modelling ideas.

## The Problem

The model must support the following:

1. Questions whose answers are of different types: Strings, Dates, Booleans, etc.
2. Given a list of questions of different types, produce different representations of the questionnaire:
  * pretty print
  * ui components (as html)
  * json
  * etc.
3. Questions can be optional
4. A measure of how complete the questionnaire is - all non-optional questions must be answered
5. Validation rules
  * string longer than a certain length
  * individual questions such as dates before/after some date
  * inter question validation, eg
    * current address does not match the address you gave on your identity document
    * You haven't given at least 3 years address history
  * essentially an arbitrarily complex expression for a question using its current answer _and_ any other answer provided to far 

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