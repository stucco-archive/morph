The Model
---------

Morph uses a two step process to transform structured documents. First, a
structured document (in a format such as XML or JSON) is parsed and turned
into an abstract syntax tree. Then, the syntax tree is transformed using
an extractor written in the Morph DSL.

Separating parsing from extraction allows for greater flexibility and makes
it easier to perform transformations. This is because parsing and extraction
are logically two separate tasks that have different functions.

Using parsers to homogenize data in different formats allows users of Morph to
focus on the task of extracting data from various sources without being
concerned with the specifics of parsing and dealing with specific file formats.

This way, Morph users can focus on the overall structure of documents rather
than the specifics of a particular syntax. Homogenizing data and turning
it into a Morph abstract syntax tree makes it easy to express transformations
using the Morph DSL.
