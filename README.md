# Project 2: Classifying Markov Models, Spring 2026

This is the high-level project document for Project P2-Markov-Classifying in CompSci 201 at Duke University, Spring 2026.

See [the details document](docs/details.md) for information on using Git, starting the project, 
and more details about the project including information about the classes and concepts 
that are outlined briefly below. 

*You'll absolutely need to read the information* in the [details document](docs/details.md) to understand how the classes 
in this project work independently and together. 
 
This project builds conceptually on P1: Markov Text Generation in using the same 
class `BaseMarkovModel`.


## Introduction

In Project P1: Markov you were asked to implement two different Markov Model classes
to _generate_ text probabilistically after training on different authors. Those models,
`SimpleMarkovModel` and `HashMarkovModel` are *generative AI Markov Models*. 

In this project you'll implement a new subclass of `BaseMarkovModel` that is
conceptually a variant of `HashMarkovModel`. 

This new `ClassifyingModel` is used
to *classify* unknown texts after training on many different authors. Your class, `ClassifyingModel`, will extend the `BaseMarkovModel` class (as classes in P1 did). You'll train multiple models on the
works of say `N` authors, each model representing a Markov Model for one of the authors, say
M<sub>1</sub>, M<sub>2</sub>, ..., M<sub>N</sub>. Then, for a set of unknown files, 
say U<sub>1</sub>, U<sub>2</sub>,..., U<sub>k</sub>, you'll find the maximum likelyhood estimate (MLE)
for each of the `k` files that it's authored by each of the `N` authors (represented by a trained model). The largest of these MLE values will "predict" or "classify" the author of the unknown text.

**You will need to copy/paste the output of running the driver program `Classifier201` before and after memoizing as described
below.**

### Similar code in AuthorShip.java

We discussed the code in [AuthorShip.java](src/AuthorShip.java) in class. That code
trains a single order-zero Markov Model on the works of one author. Then it
tries to identify which one of `k` unknown files U<sub>1</sub>, U<sub>2</sub>,..., U<sub>k</sub> is most
likely authored by the single author used to train the model.

In this assignment you'll create `N` Markov Models and then
identify which of each of the `k` unknown files is most likely written by each of the `N` authors. However,
you'll do this using an order-1 or order-2 Markov Model rather than the order zero model in [Authorship.java](src/AuthorShip.java).


## General Work for this project

Your goal is to create the class `ClassifyingModel` and use it to train ten
or more different models, and then use these models to classify
texts whose authorship is "unknown" in the sense that these texts weren't part
of the training process.

Summary:
    - Fork/Clone the project
    - Complete the implementation of `ClassifyingModel` with and without memoizing.
    - Test your implementation with the JUnit tests in `TestClassifyingModel`.
    - Veryify results for each unknown text compared to correct results in the [details document](cods/details.md).
    - Run the class `Classifier201` as described in this write-up to predict the best
    (maximal MLE) for unknown texts to answer the analysis questions.

Details about `ClassifyingModel` can be found 
in the [details document](docs/details.md) and more information is included
below for determining correctness.

### Reading/Commenting at Start (optional, Engagement points)

This assignment is clearly an extension of Project P1, but the classifying aspect
is part of a new emphasize on AI/ML in Compsci 201 that started in academic year 2025-2026.

This new version is built for 201 in the Spring 2026 course offering and differs in *several details* from previous versions. This
means that using an LLM or previous solutions may require work, though LLMs are very good.

## Running Driver Code

The primary driver code for this assignment is located in `Classifier201.java`. You should be able to run 
the `public static void main` method of the program `Classifer201.java` 
immediately after cloning the starter code, and should see something like the output shown below. The timings are not relevant, the vocabulary sizes 
are incorrect, and the probabilities are not based on actual calculations

```
data has 11 subdirs
training          dumas order 1 with 2 unique tokens, 530842 tokens
training     dostoevsky order 1 with 2 unique tokens, 955589 tokens
training          hesse order 1 with 2 unique tokens, 186468 tokens
training    shakespeare order 1 with 2 unique tokens, 223930 tokens
training       melville order 1 with 2 unique tokens, 493511 tokens
training          twain order 1 with 2 unique tokens, 607487 tokens
training          kafka order 1 with 2 unique tokens, 137797 tokens
training         proust order 1 with 2 unique tokens, 368899 tokens
training         alcott order 1 with 2 unique tokens, 539696 tokens
training          verne order 1 with 2 unique tokens, 402366 tokens
training        cbronte order 1 with 2 unique tokens, 556642 tokens
time: 0.00 for proust
time: 0.00 for hesse
time: 0.00 for alcott
(more not shown)
...
```

As you can see from the output the `data` folder has 11 sub-folders, one for each of 11 authors. The code in `Classifier201` then tries to match the twelve "unknown" files in the folder named `identify` using the maximum likelihood estimate code you'll complete in `ClassifyingModel`.

## Programming and Testing

Please see [the details document](docs/details.md) for complete information on the code you must write. As described above, you'll run the program `Classifier201` which creates many `ClassifyingModel` objects, trains
them on 11 different author/folders, then tries to match "unknown" works against these models using
the maximum likelihood estimate code you write. You'll complete the following methods
in `ClassifyingModel` (each is described in detail in [the details document](docs/details.md)).

  - `processTraining` partially complete, you must update vocabulary, stored in a `HashSet` instance variable.
  - `vocabularySize` uses the `HashSet` instance variable and returns its size.
  - `calculateMatchProbability` which uses the instance variables, a local variable, and the logic
  described in [the details document](docs/details.md).

After implementing these, you will likely be able to run `Classifier201` and see if it matches the expected output. Then you'll need to modify `tokenInContextCount` using memoization to make your program more efficient.

See the expected output in [the details document](docs/details.md). Copy that output to a document you'll turn in as part of the analysis questions, and to which you can compare the more efficient, memoized version of your program.

*Note: when a `Classifier201` object is created in the `main` method of that class, the second parameter determines if debugging/copious output
is printed. Changing the value of that parameter from `true` to `false` will generate less output.*


## Analysis Questions

Answer the following questions in your analysis. You'll submit your analysis as a separate PDF as an 
assignment to Gradescope. Answering these questions will require you to run the driver code to 
generate timing data and to reason about the algorithms and data structures you have implemented. 
We will include a template file for submitting your answers.

### Working Together for Analysis

You're *stronlgy encouraged* to work with others in 201 in completing the analysis section for this project. 
In future projects you'll work on an entire project in pairs, and submit once for the pair. For this project, however, 
each person should submit independently. If you actively work with one or more people in 201, *please make sure* you list each other 
in the analysis document you turn in. 

**For your analysis repsonses submit a PDF 
with the answer to each of the questions below on a separate page.** (for copy/paste, some of the results may take more than a page. Start the answer to each question on a separate page.)

### Question 1

Copy/paste the results from running `Classifier201` on your model **before** you implement memoizing, e.g., when the
value of `myUseCache` is `false`. Before that
output, explain why you think your program is correct. This should include comparisions to the expected
results as described in the [details document](docs/details.md).

### Question 2

Copy/paste the results of running `Classifier201` **after** memoizing, e.g., when the value of `myUseCache` is `true`. In text you write before
the output explain why you think your program is correct. Explain (in your own words) why memoizing
makes your program faster.

### Question 3

The "unknown" documents were actually each created by one of the authors used when
training models, e.g., the author has works in the `data` folder. In the main method
of `Classifier201` change the value of variable `identify`
from the string `"identify"` to the string `"newauthors"` and re-run the program (it will be faster
if you use caching). This will show results for five works *none of which* was authored by someone
in the `data` folder on which models were trained. Look at the output of the top three models/authors
for each of these unknown texts and then explain why the output makes sense or doesn't make sense
based on what models generate the best MLE values. You may need to search online to help
in answering this question. In general, you should provide reasons why the best MLE values
make sense for each of the five unknown text, or why these MLE values are problematic.

### Question 4 (counts twice)

Find the work of another author online that you think might be similar (or different) from
the authors used for training (folders in `data`). Works out of copyright and the source
of all the data in this project can be found at [Project Gutenberg](https://www.gutenberg.org/).
You can search, use your own knowledge, or ask an LLM for what authors might be appropriate.

Download a work by one such author in the format: `Plain Text UTF-8` from Project Gutenberg, add this
to the `newauthors` folder, run `Classifier201` and paste results into your document. 

In your answer you must include something about the process you used to identify an author,
the name of the author, the name of the work, and your analysis of the results.

### Extra Challenge/Engagement

Look at the folder `potus`, see the file `guide` that describes whose speeches are 
in each of the .txt files. Create new folders as necessary to train models for presidents Clinton,
both Bushes, Lyndon Johnson, Reagan, Obama, and Trump. Then try to identify how well the models
do at predicting each of the _unknown_ texts. Submit your analysis as a separate
extra credit/engagement in Gradescope.


## Submitting and Grading

The autograder will use the same tests that are in `TestClassifyingModel.java`. The autograder
will *NOT* test for speed based on memoizing, but your MLE results should be the same
regardless of whether memoizing is used.



### Grading

| Section.  | points |
|-----------|--------|
|Analysis   |     15 |
|Code       |      8 |  
