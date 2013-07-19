Functional Paradigm
-------------------

Morph adopts several principles from functional programming. Morph avoids state
and mutable data - all Morph data structures are immutable.

All Morph data and expressions are referentially transparent. This means that
an expression can be replaced with its value without changing the behavior of a
program. This makes it easier to reason about program behavior. Referential
transparency makes it easy to write correct programs.

Morph can also be treated as if it uses the substitution model for evaluation
(even though it does not internally).
