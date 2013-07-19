morph
=========
[![Build Status](https://travis-ci.org/anishathalye/morph.png?branch=master)](https://travis-ci.org/anishathalye/morph)

A framework and domain-specific language (DSL) that helps parse and transform
(*morph*!) structured documents. It currently supports several file formats
including XML, JSON, and CSV, and custom formats are usable as well.

using custom parsers
--------------------
To use custom file formats, you will need to specify the grammar and write a
parser to transform data to a *morph* format abstract syntax tree (AST), which
is specified in `Ast.scala`. Your parser must implement the trait `AstBuilder`.
The easiest way to write a parser is to use the *parboiled* library. You can
refer to `CsvParser.scala` to see what a *parboiled* parser looks like.

**Note: The DSL and the rest of the framework is still in development! The API
may change dramatically before the final release.**
