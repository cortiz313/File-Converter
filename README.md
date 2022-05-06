# File-Converter
This program will convert information from .csv to .txt and vice versa, as well as properly formatting and normalizing it.

Note: For .csv files, if a content of a cell has one or more commas (e.g. 12,345), then that cell’s content needs to be enclosed in double-quotations (e.g. “12,345”)

The program commands are as follows:

Command #1: convert source.xxx destination.yyy 
This command converts source.xxx to destination.yyy where source.xxx is the name and extension of the file that user wants to convert and destination.yyy is the name 
and extension of the file in which the user wants to store the result of format conversion. Please note that xxx and yyy can either be csv or txt. 
Also, the file names may or may not include the path to the file in the file system.

Command #2: normalize source.xxx 
This command reads the content of source.xxx, normalizes the content of each cell, and writes the normalized content back to the same file. 
Normalizing a cell is an operation that depends on the current content of the cell:
– if cell is empty: writes N/A instead
– if cell contains an integer: normalization explicitly shows the sign (+ for positive and - for negative). Also, if the integer
representation is shorter than 10 characters, it adds some leading zeros to make the representation 10 character long.
– if cell contains a float/double: normalization shows two digit after decimal point. Also, it uses scientific notation if the number is greater than 100 or 
less than 0.01.
– if cell contains a string longer than 13 characters, normalization shows the first 10 characters of the string followed by an ellipsis (three dots like this . . . )
– otherwise, normalization causes no change.

Command #3: quit
This command ends the program.
