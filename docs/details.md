# Details for P2: Classifying Markov, Fall 2026

You should have already read the [README](../README.md) file to get an overview of the project. The details here are needed to explain the classes you're given, the code you'll write, and the implementation details.

## Starter Code and Using Git
**_You should have installed all software (Java, Git, VS Code) before completing this project._** You can find 
the [directions for installation here](https://coursework.cs.duke.edu/201fall26/resources-201/-/blob/main/installingSoftware.md) (including workarounds for submitting without Git if needed).

We'll be using Git and the installation of GitLab at [coursework.cs.duke.edu](https://coursework.cs.duke.edu). All code for classwork will be kept here. Git is software used for version control, and GitLab is an online repository to store code in the cloud using Git.



**You will need to copy/paste the output of running the driver program `Classifier201` before and after memoizing as described
below.**


## Coding in Project P2: Markov Classifier

For this project, you [**start with the URL linked to course calendar**](https://coursework.cs.duke.edu/201fall26/p2-markov-classifier).

**[This document details the workflow](https://coursework.cs.duke.edu/201fall26/resources-201/-/blob/main/projectWorkflow.md) for downloading the starter code for the project, updating your code on coursework using Git, and ultimately submitting to Gradescope for autograding.** We recommend that you read and follow the directions carefully this first time working on a project! While coding, we recommend that you periodically (perhaps when completing a method or small section) push your changes.

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


## Java Background 

As with P1, you'll be creating a new class `ClassifyingModel` 
that extends `BaseMarkovModel`. As described below, 
you'll use concepts from the class `HashMarkovModel` that you previously wrote. If you didn't complete that assignment you'll likely benefit from the code in `ClassifyingModel` which includes a partially complete implementation that uses a `HashMap`. 

  > You'll need to complete `processTraining` based on the coding details you'll find below. However, the code you're given initializes the instance variable `myMap` correctly.

## Programming and Testing

This document has complete information on the code you must write. As described above, you'll run the program `Classifier201` which creates several `ClassifyingModel` objects, trains
them on 11 different author/folders, then tries to match "unknown" works against these models using
the maximum likelihood estimate code you write. You'll complete the following methods
in `ClassifyingModel` (each is described in detail below.

  - `processTraining` partially complete, you must update vocabulary, stored in a `HashSet` instance variable.
  - `vocabularySize` uses the `HashSet` instance variable and returns its size.
  - `calculateMatchProbability` which uses the instance variables, a local variable, and the logic
  described below.

After implementing these, you will likely be able to run `Classifier201` and see if it matches the expected output. **Then you'll need to modify `tokenInContextCount` using memoization to make your program more efficient.**

See the expected output below. Copy that output to a document you'll turn in as part of the analysis questions, and to which you can compare the more efficient, memoized version of your program.

*Note: when a `Classifier201` object is created in the `main` method, the second parameter to the constructor determines if debugging/copious output
is printed. Changing the value of that parameter from `true` to `false` will generate less output.*

### Constructors

You'll need to implement two constructors that correspond to the two constructors in `BaseMarkovModel`. These constructors are partially completed
in the code you fork/clone, but the instance variable provided is *not* initialized and there are more instance variables needed that must be initialized in the constructor as well. You'll see that there is a boolean instance variable `myUseMemo` that defaults to `false`. When you implement _memoizing_ as a performance/speed enhancement described below, you'll construct `ClassifyingModel` objects with an explicit parameter of `true` so that memoizing is engaged when the program runs.

Additional  instance variables are described next.

### instance variables

Some instance vaiables will allow you to meet correctness and
performance criteria. In the [P1:Markov](https://coursework.cs.duke.edu/201fall26/p1-markov/) assignment you were advised to use `HashMap<List<String>,List<String>>` as an instance variable. The keys in this map are each a _context_, or an N-gram of N-tokens/words where `N` is the order of the model. The corresponding value is a list of the words/tokens that follow the key _context_. See that assignment for
more details. In the code you fork/clone, this instance variable `myMap` is defined, **and is** given a value in the constructor. **You'll need additional instance variables as well** -- details both above and in what follows. 

You will need a `HashSet<String>` to store the unique words/tokens
that constitute a model's vocabulary (described below). To meet performance criteria after you know your model
is correct, you'll likely need `HashMap<List<String>, Map<String,Integer>` in which each different 
_context_ is a key, and the corresponding value is a map of each following word/token and the number of times
the following word/token occurs. Details for this are described below. This instance variable is used when _memoizing_ and is *not* needed for correctness, but is used for efficiency.

*Note that your code also has access to the `protected` instance variables
in `BaseMarkovModel`, including `myWordSequence`.*

## The processTraining() method

You'll need to complete the `processTraining` method that was described in implementing `HashMarkovModel`. This method
is called in `BaseMarkovModel` as the last line of both `trainDirectory` and `trainText`. In `HashMarkovModel` the code you wrote populated the instance variable that was termed `myMap` (which exists in the code you fork/clone). 

You may assume that `processTraining` is
only called from `trainDirectory` so that the inherited instance variable `myWordSequence` has been
filled with words/tokens. *You must add each of these words/tokens to the instance variable `HashSet<String>` that represents the model's vocabulary* and you must initialize the instance variable used for memoizing. The instance variable `myMap` is, however, initialized in the code you're given as the last two lines of `processTraining`. 

### Relevant instructions from `HashMarkovModel`

The code you fork/clone loops over every possible context (based on `myModelSize`) and updates that context in instance
variable `myMap` by adding the token/word that follows the context to the `ArrayList` that's the corresponding value in `myMap`.

See the [details document](https://coursework.cs.duke.edu/201fall26/p1-markov/-/blob/main/docs/details.md) for P1:HashMarkovModel for more information.

## Required public and private helper methods

You are given method headers and some code for:
  - `vocabularySize` that returns the number of unique words/tokens in the trained model (you must implement this -- it should return the size of a set you create and populate.)
  - `tokenInContextCount` for a given _context_ and _next_ word/token, returns the number of times `next` follows `context. You are given _slow code_ for this method, after you know it works you'll make it more efficient.
  - `calculateMatchProbability` - returns the maximum likelihood estimate  (MLE) that some text matches this model. This is the key method in determining the MLE for a given text.

Information on these helper methods can be found in the comments included
for them in the `ClassifyingModel.java` code you fork/clone and then modify.


## Method calculateMatchProbability

The method `calculateMatchProbability` is called from `Classifier201` to obtain the maximal likelihood
estimate (MLE) that an unknown text matches the trained model. The 
method `calculateLogLikelihood` 
from [`AuthorShip.java`](../src/AuthorShip.java) is
similar and will prove useful as a model. The math behind the code you write, and
the general flow of control are described below.

The `text` parameter is the unkown text for which this method calculates
the MLE for `text` compared to this trained model. The general steps are

  - Convert the text to a `List` of words/tokens by calling the `createTokenizedText`
    helper method (done in the code you fork/clone)
  - loop over every possible _context_ (a `List<String>` with `myOrder` values) and the following word/token from the unknown text. For each of these:
    - add the context to a local `HashSet` that stores the number of unique contexts in the unknown text.
    - calculate the number of times the `context` occurs in the trained model, store in an appropriaely named local variable, e.g., `contextCount` (this value can be obtained from the map instance variable of the trained model).
    - calculate the number of times `next` follows `context` in the trained model by calling helper method `tokenInContextCount`, store in an appropriately named local variable, e.g., `nextCount`
    - use this equation to calculate an MLE probability for one context and one token: 
    $$
    (nextCount + smoother)/(contextCount + smoother*vocabSize)
    $$
    See the similar value in [`AuthorShip.java`](../src/AuthorShip.java) for example. Note that `nextCount` is calculated by calling `tokenInContextCount` and `contextCount` is the number of times `context` 
    occurs in the trained model, which can be determined directly from `myMap`.
    - as you loop, accumulate the sum of the log of each probability. Again see [`AuthorShip.java`](../src/AuthorShip.java) for similar code.
  - After the loop, return the _context normalized_ sum of all log-probabilities. For this you'll divide the log-sum by the number of unique contexts in the unknown text. You'll need to track that number in the loop, e.g., using a local `HashSet` to store all the (unique) contexts from the unknown texts.

### Testing and Verifying Results

After you've implemented `calculateMatchProbability` you can test it to see if it matches expected results using the JUnit tests
in `TestClassifyingModel` and in matching printed results to what's expected as shown below.

## Efficiency Considerations in calculateMatchProbabilities

### Performance Criteria

You will need to add an instance variable
that _caches_ or _memoizes_ the number of times each follow word/token occurs for each _context_ to meet performance criteria for how long
it takes `calculateMatchProbability` to execute, because the code in that
method will call `tokenInContextCount` as described next.  The code you'd 
write for this without the _cache_/instance variable is provided in the code you fork/clone and
is shown below:

```
  private int tokenInContextCount(List<String> context, String token) {
      int count = 0;
      for(String s : myMap.get(context)) {
            if (s.equals(token)) {
                count += 1;
            }
      }
      return count;
  }
```

The method `tokenInContextCount` that is part of the code you fork 
loops over all tokens that follow parameter `context` and thus has complexity $O(N)$ where $N$ is the total number of
tokens in the list that follow `context`. For some authors the average
length of these lists across all contexts is small, but for other authors
it is large, e.g., the average length ranges from 12 to 56. When an unknown
text has contexts that "hit" these longer lists, the time across all
matches can be excessive. On ola's (very new Mac-pro) laptop, timings for one runt to match the file `old-fashioned-girl.txt` against all authors is shown below:
```
    time: 0.82 for proust
    time: 0.57 for hesse
    time: 4.25 for alcott
    time: 2.11 for verne
    time: 1.77 for dumas
    time: 0.41 for kafka
    time: 1.20 for shakespeare
    time: 3.88 for twain
    time: 10.08 for dostoevsky
    time: 3.07 for cbronte
    time: 2.99 for melville
```
With the optimization described next, the timings change as follows:
```
    time: 0.08 for proust
    time: 0.06 for hesse
    time: 0.37 for alcott
    time: 0.29 for verne
    time: 0.15 for dumas
    time: 0.05 for kafka
    time: 0.16 for shakespeare
    time: 0.40 for twain
    time: 1.32 for dostoevsky
    time: 0.42 for cbronte
    time: 0.51 for melville
```

### Caching/Memoizing

If `tokenInContextCount` is called several times with the
same `context` and `token` parameters, the code calculates the same
return value each time by looping, checking for equality, and incrementing a count. 
If the value for each pair of `context` and `token` pairs
is _stored_, it can be returned in $O(1)$ time without the loop. This
kind of optimization is called _caching_ or _memoizing_ as explained
in [this Wikipedia article](https://en.wikipedia.org/wiki/Memoization).

There are several ways to _memoize_ these results for each _context_ and _token_ pair. We suggest one method, using an instance variable `Map<List<String>, Map<String,Integer>> myCache`. Each unique `context` is a key in this map. The corresponding
value is a map of each `token` that follows the `context` key to to the integer
value of how many times the `token` follows `context. You'll likely need to think carefully about what this means and how it works.

When `tokenInContextCount` is called with `context`, `token` parameters for the
first time, the loop that counts the number of occurrences of `token` that follows
`context` executes. Before the count is returned, you must store the value in `myCache`, e.g.,
with code similar to (conceptually at least)
```
    myCache.get(context).put(token,count);
```
Note that the value of `myCache.get(context)` is a map that now has
the value of how many times `token` follows `context` stored in the map.

Modify the method `tokenInContextCount` so that before
the loop in the body of `tokenInContextCount` the map `myCache` is
checked, e.g., using `myCache.get(context).containsKey(token)`. If this is true? The
value of how many times `token` follows `context` was previously calculated and stored. This value can be obtained from `myCache` and returned --- the loop doesn't execute at all! By storing the result
the first time the method is called for each `context` and `token` pair, the
result can be retrieved rather than recalculated on the next calls with the 
same pair.

This improvement makes each model faster and faster when used to identify
more and more unknown texts since some of those texts may share the
same `context` and `token` pairs. Runs of `Classsifier201` should be much faster when you've implemented memoization.

Note: You should use the instance variable `myUseCache` and only store/retrieve values from the map `myCache` if 
`myUseCache` is true. This value is set when a `ClassifyingModel` object is created, that's near line 83 of `Classifier201` in
the method `trainAllAuthors`.  For example, using the cache before looping over an unseen context might look like this:

```
    if (myUseMemo && myCache.get(context).containsKey(token)){
        return myCache.get(context).get(token);
    }
```

## Correct Results

If you implement `calculateMatchProbability` as described above, you should get _best matches_ with probabilities similar to
those shown below. Please post to ED if yours are drastically different from those shown.

```

*** -54.07      shakespeare for caesar.txt
*** -26.24      kafka for urteil.txt
*** -66.73      hesse for gertrude.txt
*** -104.23     alcott for old-fashioned-girl.txt
*** -66.57      shakespeare for othello.txt
*** -89.98      melville for white-jacket.txt
*** -121.22     cbronte for shirley.tx
*** -109.57     twain for innocents.txt
*** -91.32      verne for fiveweeks-balloon.txt
*** -115.96     dumas for dumas-story.txt
*** -80.63      dostoevsky for gambler.txt
*** -99.74      proust for prisonniere.txt

```


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
