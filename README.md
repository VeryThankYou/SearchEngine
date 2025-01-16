# Guide to using the scripts

# Search engine usage:
In the application folders, the 6 java files, index1-index7, are search engines. They can be run with a file path to the dataset as the argument, and a text-based interface allows you to search in the dataset.
Index 1 is the naive datastructure, index3 is the linked list of unique terms, index4 is the hash-table solution, index5 implements and-or search, index6 has single-term ranked searching, index 7 has ranked and-or search.

Using and-or search: To make an and search, the following syntax is used:

term1&&term2

Or search is as follows:

term1||term2

These can be combined using nested parentheses, but only use parenthesis for more than 2 terms, and remember that the placement changes the query. Here is an example using the example query from the report:

((duck||goose)&&(roast||recipe))||((state||country)&&(president||minister))

You cannot query terms with |, &, or parentheses in them, this will cause errors.

# Testing files:
The Testing java file tests build-time, memory, and searchtime. In the main method, you can select which datastructure/search algorithm to use. In writeTests you can set the number of testfiles to test on. A commented block can be uncommented for specific tests for and-or search.
Results are saved in the Testing folder.

HashTest and HashTest2 is used for testing hash-distribution and hash time. Hasshtest takes a path to a test-file as an argument, and tests distribution for that dataset.

# Python scripts
RandomStuff was used to count different measures of the test files, Plotting and Plotting2 is used to plot different test results.
