# Bobby User Guide

// Update the title above to match the actual product name

// Product screenshot goes here

// Product intro goes here

## Viewing task statistics

Use `stats` to see a count-only summary of the tasks currently managed by Bobby.

```text
stats
```

Bobby shows the current Monday-to-Sunday period, how many currently completed tasks were
completed during that period, and the overall completed, pending, and total counts:

```text
Here are your task statistics:
     Period: Sep 07 2026 to Sep 13 2026
     Currently completed this week: 3
     Completed overall: 5
     Pending: 2
     Total: 7
```

Deleted and unmarked tasks are not included in completed counts. Completed tasks loaded from
an older Bobby save file remain completed, but do not count as completed during the current
week because their completion time is unknown. Unmark and mark such a task again to record a
new completion time.

The command does not accept additional arguments. For example, `stats week` is invalid.

## Adding deadlines

// Describe the action and its outcome.

// Give examples of usage

Example: `keyword (optional arguments)`

// A description of the expected outcome goes here

```
expected output
```

## Feature ABC

// Feature details


## Feature XYZ

// Feature details
