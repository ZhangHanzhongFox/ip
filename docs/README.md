# Fox User Guide

// Update the title above to match the actual product name

![Fox GUI](Ui.png)

// Product intro goes here

## Adding deadlines

Use `deadline <description> /by <date>` to add a deadline. Dates must use
`yyyy-MM-dd`, such as `2026-12-02`.

Fox validates that the date exists and displays valid dates as `MMM dd yyyy`.
For example:

Example: `deadline submit report /by 2026-12-02`

The deadline is displayed as `(by: Dec 02 2026)` and is restored after Fox is
restarted.

## Adding events safely

Use `event <description> /from <start> /to <end>` to add an event. Fox accepts
times such as `10am`, `10:30am`, and `22:30`. The end time must be later than
the start time; parameters such as `/from` and `/to` can each appear only once.

Example: `event project meeting /from 2pm /to 3:30pm`

## Finding tasks

Use `find <keyword> [more keywords]` to find tasks whose descriptions contain
at least one keyword as a complete word. Matching is case-insensitive, and the
results keep their original task numbers.

Example: `find report meeting`

## Preventing duplicate tasks

Fox rejects a new task when an existing task has the same type and details.
For example, entering `todo read book` twice adds only the first task. A task
remains a duplicate after it is marked as done because its completion status
does not change its identity.

## Feature ABC

// Feature details


## Feature XYZ

// Feature details

## Acknowledgements

Parts of Fox's JavaFX GUI were adapted from the
[SE-EDU JavaFX tutorial](https://se-education.org/guides/tutorials/javaFx.html):

* [Part 1](https://se-education.org/guides/tutorials/javaFxPart1.html) guided the JavaFX launcher and
  cross-platform Gradle dependency setup.
* [Part 4](https://se-education.org/guides/tutorials/javaFxPart4.html) guided the FXML controller and dialog-box
  interaction structure.
* [Part 5](https://se-education.org/guides/tutorials/javaFxPart5.html) provided the basis for the initial CSS styling.

These parts were subsequently customized for Fox.
