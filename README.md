Morph
=========

master: [![Build Status]
(https://travis-ci.org/stucco/morph.png?branch=master)]
(https://travis-ci.org/stucco/morph)
dev: [![Build Status]
(https://travis-ci.org/stucco/morph.png?branch=dev)]
(https://travis-ci.org/stucco/morph)

*Morph* is a framework and domain-specific language (DSL) that helps parse and
transform structured data. It currently supports several file formats
including XML, JSON, and CSV, and custom formats are usable as well.

Documentation
-------------

You can read the [user guide](http://stucco.github.io/morph) or go
through the [Scaladoc](http://stucco.github.io/morph/latest/api). I
suggest going through the user guide. The Scaladoc can get a little scary.

Updating User Guide
-------------

The user guide is created with [pamflet.](https://github.com/n8han/pamflet)

All files to generate the user guide are in `/src/pamflet`

After modifying the user guide, any changes can be previewed with `pf src/pamflet/`

To publish the user guide, you will need to run `sbt make-site` to build, and then `sbt ghpages-push-site` to update the site.
