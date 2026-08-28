# Console UI Test Plan

## Test environment

- **Java version:** 25
- **Build command:** `javac --release 25 -d out/date-time-verification (Get-ChildItem -Recurse -Filter *.java -Path src/main/java | ForEach-Object FullName)`
- **Launch convention:** Run from the repository root with `java -cp out/date-time-verification bobby.Bobby`.
- **Comparison rule:** Exact output match after line-ending normalization, unless a test case explicitly states another deterministic rule.

## Test cases

Each case starts a fresh program process.

## T01 — task and mark errors preserve state

**Aim:** A valid to-do remains unchanged after invalid to-do, unknown-command, and invalid mark/unmark inputs.

**Inputs:**

```text
todo read book
todo
list
blah
list
mark 1
mark 0
list
unmark 1
unmark 2
list
bye
```

**Command:**

```powershell
Remove-Item data/duke.txt -ErrorAction Ignore; javac --release 25 -d out/date-time-verification (Get-ChildItem -Recurse -Filter *.java -Path src/main/java | ForEach-Object FullName); "todo read book", "todo", "list", "blah", "list", "mark 1", "mark 0", "list", "unmark 1", "unmark 2", "list", "bye" | java -cp out/date-time-verification bobby.Bobby
```

**Expected output:**

```text
____________________________________________________________
BBBB   OOO   BBBB  BBBB  Y   Y
B   B O   O  B   B B   B  Y Y
BBBB  O   O  BBBB  BBBB    Y
B   B O   O  B   B B   B   Y
BBBB   OOO   BBBB  BBBB    Y
____________________________________________________________
     Hello, I'm Bobby.
     What can I do for you?
____________________________________________________________
____________________________________________________________
     Got it. I've added this task:
       [T][ ] read book
     Now you have 1 tasks in the list.
____________________________________________________________
____________________________________________________________
     You don't have a task after the todo.
____________________________________________________________
____________________________________________________________
Here are the tasks in your list:
     1.[T][ ] read book
____________________________________________________________
____________________________________________________________
     I don't understand what you said. Please use the correct commands
____________________________________________________________
____________________________________________________________
Here are the tasks in your list:
     1.[T][ ] read book
____________________________________________________________
____________________________________________________________
     Nice! I've marked this task as done:
       [T][X] read book
____________________________________________________________
____________________________________________________________
     Invalid task number.
____________________________________________________________
____________________________________________________________
Here are the tasks in your list:
     1.[T][X] read book
____________________________________________________________
____________________________________________________________
     OK, I've marked this task as not done yet:
       [T][ ] read book
____________________________________________________________
____________________________________________________________
     Invalid task number.
____________________________________________________________
____________________________________________________________
Here are the tasks in your list:
     1.[T][ ] read book
____________________________________________________________
____________________________________________________________
     Bye! Hope to see you again soon.
____________________________________________________________
```

## T02 — deadline and event errors preserve state

**Aim:** Malformed date input and incomplete deadline/event commands do not alter the tasks added by valid commands.

**Inputs:**

```text
deadline submit /by Friday
deadline
list
event meeting /from 2019-10-15 0900 /to 2019-10-15 1000
event coffee /from
list
bye
```

**Command:**

```powershell
Remove-Item data/duke.txt -ErrorAction Ignore; javac --release 25 -d out/date-time-verification (Get-ChildItem -Recurse -Filter *.java -Path src/main/java | ForEach-Object FullName); "deadline submit /by Friday", "deadline", "list", "event meeting /from 2019-10-15 0900 /to 2019-10-15 1000", "event coffee /from", "list", "bye" | java -cp out/date-time-verification bobby.Bobby
```

**Expected output:**

```text
____________________________________________________________
BBBB   OOO   BBBB  BBBB  Y   Y
B   B O   O  B   B B   B  Y Y
BBBB  O   O  BBBB  BBBB    Y
B   B O   O  B   B B   B   Y
BBBB   OOO   BBBB  BBBB    Y
____________________________________________________________
     Hello, I'm Bobby.
     What can I do for you?
____________________________________________________________
____________________________________________________________
     Please use dates and times in YYYY-MM-DD HHMM format.
____________________________________________________________
____________________________________________________________
     Please use: deadline DESCRIPTION /by YYYY-MM-DD HHMM
____________________________________________________________
____________________________________________________________
Here are the tasks in your list:
No tasks added yet.
____________________________________________________________
____________________________________________________________
     Got it. I've added this task:
       [E][ ] meeting (from: Oct 15 2019 9:00 AM to: Oct 15 2019 10:00 AM)
     Now you have 1 tasks in the list.
____________________________________________________________
____________________________________________________________
     Please use: event DESCRIPTION /from YYYY-MM-DD HHMM /to YYYY-MM-DD HHMM
____________________________________________________________
____________________________________________________________
Here are the tasks in your list:
     1.[E][ ] meeting (from: Oct 15 2019 9:00 AM to: Oct 15 2019 10:00 AM)
____________________________________________________________
____________________________________________________________
     Bye! Hope to see you again soon.
____________________________________________________________
```

## T03 — mixed valid workflow and blank input

**Aim:** Whitespace and case-normalised valid commands work, while a blank input does not affect subsequent task state changes.

**Inputs:**

```text
  TODO Walk dog  
   
list
deadline pay bills /by 2019-10-15 1800
event project /from 2019-10-16 0900 /to 2019-10-16 1000
unmark 1
mark 3
list
bye
```

**Command:**

```powershell
Remove-Item data/duke.txt -ErrorAction Ignore; javac --release 25 -d out/date-time-verification (Get-ChildItem -Recurse -Filter *.java -Path src/main/java | ForEach-Object FullName); "  TODO Walk dog  ", "   ", "list", "deadline pay bills /by 2019-10-15 1800", "event project /from 2019-10-16 0900 /to 2019-10-16 1000", "unmark 1", "mark 3", "list", "bye" | java -cp out/date-time-verification bobby.Bobby
```

**Expected output:**

```text
____________________________________________________________
BBBB   OOO   BBBB  BBBB  Y   Y
B   B O   O  B   B B   B  Y Y
BBBB  O   O  BBBB  BBBB    Y
B   B O   O  B   B B   B   Y
BBBB   OOO   BBBB  BBBB    Y
____________________________________________________________
     Hello, I'm Bobby.
     What can I do for you?
____________________________________________________________
____________________________________________________________
     Got it. I've added this task:
       [T][ ] Walk dog
     Now you have 1 tasks in the list.
____________________________________________________________
____________________________________________________________
     I don't understand what you said. Please use the correct commands
____________________________________________________________
____________________________________________________________
Here are the tasks in your list:
     1.[T][ ] Walk dog
____________________________________________________________
____________________________________________________________
     Got it. I've added this task:
       [D][ ] pay bills (by: Oct 15 2019 6:00 PM)
     Now you have 2 tasks in the list.
____________________________________________________________
____________________________________________________________
     Got it. I've added this task:
       [E][ ] project (from: Oct 16 2019 9:00 AM to: Oct 16 2019 10:00 AM)
     Now you have 3 tasks in the list.
____________________________________________________________
____________________________________________________________
     OK, I've marked this task as not done yet:
       [T][ ] Walk dog
____________________________________________________________
____________________________________________________________
     Nice! I've marked this task as done:
       [E][X] project (from: Oct 16 2019 9:00 AM to: Oct 16 2019 10:00 AM)
____________________________________________________________
____________________________________________________________
Here are the tasks in your list:
     1.[T][ ] Walk dog
     2.[D][ ] pay bills (by: Oct 15 2019 6:00 PM)
     3.[E][X] project (from: Oct 16 2019 9:00 AM to: Oct 16 2019 10:00 AM)
____________________________________________________________
____________________________________________________________
     Bye! Hope to see you again soon.
____________________________________________________________
```

## T04 — delete task and reindex remaining tasks

**Aim:** Deleting a valid task removes it, preserves the other tasks, and reindexes the displayed list.

**Inputs:**

```text
todo read book
deadline return book /by 2019-06-06 1800
event project meeting /from 2019-08-06 1400 /to 2019-08-06 1600
mark 1
mark 2
delete 3
list
bye
```

**Command:**

```powershell
Remove-Item data/duke.txt -ErrorAction Ignore; javac --release 25 -d out/date-time-verification (Get-ChildItem -Recurse -Filter *.java -Path src/main/java | ForEach-Object FullName); "todo read book", "deadline return book /by 2019-06-06 1800", "event project meeting /from 2019-08-06 1400 /to 2019-08-06 1600", "mark 1", "mark 2", "delete 3", "list", "bye" | java -cp out/date-time-verification bobby.Bobby
```

**Expected output:**

```text
____________________________________________________________
BBBB   OOO   BBBB  BBBB  Y   Y
B   B O   O  B   B B   B  Y Y
BBBB  O   O  BBBB  BBBB    Y
B   B O   O  B   B B   B   Y
BBBB   OOO   BBBB  BBBB    Y
____________________________________________________________
     Hello, I'm Bobby.
     What can I do for you?
____________________________________________________________
____________________________________________________________
     Got it. I've added this task:
       [T][ ] read book
     Now you have 1 tasks in the list.
____________________________________________________________
____________________________________________________________
     Got it. I've added this task:
       [D][ ] return book (by: Jun 06 2019 6:00 PM)
     Now you have 2 tasks in the list.
____________________________________________________________
____________________________________________________________
     Got it. I've added this task:
       [E][ ] project meeting (from: Aug 06 2019 2:00 PM to: Aug 06 2019 4:00 PM)
     Now you have 3 tasks in the list.
____________________________________________________________
____________________________________________________________
     Nice! I've marked this task as done:
       [T][X] read book
____________________________________________________________
____________________________________________________________
     Nice! I've marked this task as done:
       [D][X] return book (by: Jun 06 2019 6:00 PM)
____________________________________________________________
____________________________________________________________
     Noted. I've removed this task:
       [E][ ] project meeting (from: Aug 06 2019 2:00 PM to: Aug 06 2019 4:00 PM)
     Now you have 2 tasks in the list.
____________________________________________________________
____________________________________________________________
Here are the tasks in your list:
     1.[T][X] read book
     2.[D][X] return book (by: Jun 06 2019 6:00 PM)
____________________________________________________________
____________________________________________________________
     Bye! Hope to see you again soon.
____________________________________________________________
```

## T05 — delete errors preserve tasks between valid deletions

**Aim:** Valid deletions reindex the task list, while non-numeric, out-of-range, and zero task numbers leave the current tasks unchanged.

**Inputs:**

```text
todo alpha
todo beta
todo gamma
delete 2
list
delete 3
list
delete two
list
delete 2
list
delete 0
list
bye
```

**Command:**

```powershell
Remove-Item data/duke.txt -ErrorAction Ignore; javac --release 25 -d out/date-time-verification (Get-ChildItem -Recurse -Filter *.java -Path src/main/java | ForEach-Object FullName); "todo alpha", "todo beta", "todo gamma", "delete 2", "list", "delete 3", "list", "delete two", "list", "delete 2", "list", "delete 0", "list", "bye" | java -cp out/date-time-verification bobby.Bobby
```

**Expected output:**

```text
____________________________________________________________
BBBB   OOO   BBBB  BBBB  Y   Y
B   B O   O  B   B B   B  Y Y
BBBB  O   O  BBBB  BBBB    Y
B   B O   O  B   B B   B   Y
BBBB   OOO   BBBB  BBBB    Y
____________________________________________________________
     Hello, I'm Bobby.
     What can I do for you?
____________________________________________________________
____________________________________________________________
     Got it. I've added this task:
       [T][ ] alpha
     Now you have 1 tasks in the list.
____________________________________________________________
____________________________________________________________
     Got it. I've added this task:
       [T][ ] beta
     Now you have 2 tasks in the list.
____________________________________________________________
____________________________________________________________
     Got it. I've added this task:
       [T][ ] gamma
     Now you have 3 tasks in the list.
____________________________________________________________
____________________________________________________________
     Noted. I've removed this task:
       [T][ ] beta
     Now you have 2 tasks in the list.
____________________________________________________________
____________________________________________________________
Here are the tasks in your list:
     1.[T][ ] alpha
     2.[T][ ] gamma
____________________________________________________________
____________________________________________________________
     Invalid task number.
____________________________________________________________
____________________________________________________________
Here are the tasks in your list:
     1.[T][ ] alpha
     2.[T][ ] gamma
____________________________________________________________
____________________________________________________________
     Invalid task number.
____________________________________________________________
____________________________________________________________
Here are the tasks in your list:
     1.[T][ ] alpha
     2.[T][ ] gamma
____________________________________________________________
____________________________________________________________
     Noted. I've removed this task:
       [T][ ] gamma
     Now you have 1 tasks in the list.
____________________________________________________________
____________________________________________________________
Here are the tasks in your list:
     1.[T][ ] alpha
____________________________________________________________
____________________________________________________________
     Invalid task number.
____________________________________________________________
____________________________________________________________
Here are the tasks in your list:
     1.[T][ ] alpha
____________________________________________________________
____________________________________________________________
     Bye! Hope to see you again soon.
____________________________________________________________
```

## T06 — delete command errors on an empty list

**Aim:** Missing and invalid task numbers, including deletion from an empty list, do not change the list before or after a valid deletion.

**Inputs:**

```text
delete 1
list
todo only task
delete
list
delete 2
list
DELETE 1
list
delete 1
list
bye
```

**Command:**

```powershell
Remove-Item data/duke.txt -ErrorAction Ignore; javac --release 25 -d out/date-time-verification (Get-ChildItem -Recurse -Filter *.java -Path src/main/java | ForEach-Object FullName); "delete 1", "list", "todo only task", "delete", "list", "delete 2", "list", "DELETE 1", "list", "delete 1", "list", "bye" | java -cp out/date-time-verification bobby.Bobby
```

**Expected output:**

```text
____________________________________________________________
BBBB   OOO   BBBB  BBBB  Y   Y
B   B O   O  B   B B   B  Y Y
BBBB  O   O  BBBB  BBBB    Y
B   B O   O  B   B B   B   Y
BBBB   OOO   BBBB  BBBB    Y
____________________________________________________________
     Hello, I'm Bobby.
     What can I do for you?
____________________________________________________________
____________________________________________________________
     Invalid task number.
____________________________________________________________
____________________________________________________________
Here are the tasks in your list:
No tasks added yet.
____________________________________________________________
____________________________________________________________
     Got it. I've added this task:
       [T][ ] only task
     Now you have 1 tasks in the list.
____________________________________________________________
____________________________________________________________
     Invalid task number.
____________________________________________________________
____________________________________________________________
Here are the tasks in your list:
     1.[T][ ] only task
____________________________________________________________
____________________________________________________________
     Invalid task number.
____________________________________________________________
____________________________________________________________
Here are the tasks in your list:
     1.[T][ ] only task
____________________________________________________________
____________________________________________________________
     Noted. I've removed this task:
       [T][ ] only task
     Now you have 0 tasks in the list.
____________________________________________________________
____________________________________________________________
Here are the tasks in your list:
No tasks added yet.
____________________________________________________________
____________________________________________________________
     Invalid task number.
____________________________________________________________
____________________________________________________________
Here are the tasks in your list:
No tasks added yet.
____________________________________________________________
____________________________________________________________
     Bye! Hope to see you again soon.
____________________________________________________________
```

## T07 — task-list changes are saved to disk

**Aim:** Adding, marking, unmarking, and deleting tasks automatically replaces `data/duke.txt` with the current task list in the specified save format.

**Inputs:**

```text
todo read book
deadline return book /by 2019-06-06 1800
event project meeting /from 2019-08-06 1400 /to 2019-08-06 1600
mark 2
unmark 2
delete 1
bye
```

**Command:**

```powershell
Remove-Item data/duke.txt -ErrorAction Ignore; javac --release 25 -d out/date-time-verification (Get-ChildItem -Recurse -Filter *.java -Path src/main/java | ForEach-Object FullName); "todo read book", "deadline return book /by 2019-06-06 1800", "event project meeting /from 2019-08-06 1400 /to 2019-08-06 1600", "mark 2", "unmark 2", "delete 1", "bye" | java -cp out/date-time-verification bobby.Bobby; Get-Content data/duke.txt
```

**Expected output:**

```text
____________________________________________________________
BBBB   OOO   BBBB  BBBB  Y   Y
B   B O   O  B   B B   B  Y Y
BBBB  O   O  BBBB  BBBB    Y
B   B O   O  B   B B   B   Y
BBBB   OOO   BBBB  BBBB    Y
____________________________________________________________
     Hello, I'm Bobby.
     What can I do for you?
____________________________________________________________
____________________________________________________________
     Got it. I've added this task:
       [T][ ] read book
     Now you have 1 tasks in the list.
____________________________________________________________
____________________________________________________________
     Got it. I've added this task:
       [D][ ] return book (by: Jun 06 2019 6:00 PM)
     Now you have 2 tasks in the list.
____________________________________________________________
____________________________________________________________
     Got it. I've added this task:
       [E][ ] project meeting (from: Aug 06 2019 2:00 PM to: Aug 06 2019 4:00 PM)
     Now you have 3 tasks in the list.
____________________________________________________________
____________________________________________________________
     Nice! I've marked this task as done:
       [D][X] return book (by: Jun 06 2019 6:00 PM)
____________________________________________________________
____________________________________________________________
     OK, I've marked this task as not done yet:
       [D][ ] return book (by: Jun 06 2019 6:00 PM)
____________________________________________________________
____________________________________________________________
     Noted. I've removed this task:
       [T][ ] read book
     Now you have 2 tasks in the list.
____________________________________________________________
____________________________________________________________
     Bye! Hope to see you again soon.
____________________________________________________________
D | 0 | return book | 2019-06-06T18:00
E | 0 | project meeting | 2019-08-06T14:00 | 2019-08-06T16:00
```

## T08 — saved tasks load when the application starts

**Aim:** A valid save file restores to-do, deadline, and event tasks with their saved completion states before the first command is processed.

**Inputs:**

```text
list
bye
```

**Command:**

```powershell
$ProgressPreference = 'SilentlyContinue'; New-Item -ItemType Directory -Force data | Out-Null; "T | 1 | read book", "D | 0 | return book | 2019-06-06T18:00", "E | 1 | project meeting | 2019-08-06T14:00 | 2019-08-06T16:00" | Set-Content data/duke.txt; javac --release 25 -d out/date-time-verification (Get-ChildItem -Recurse -Filter *.java -Path src/main/java | ForEach-Object FullName); "list", "bye" | java -cp out/date-time-verification bobby.Bobby
```

**Expected output:**

```text
____________________________________________________________
BBBB   OOO   BBBB  BBBB  Y   Y
B   B O   O  B   B B   B  Y Y
BBBB  O   O  BBBB  BBBB    Y
B   B O   O  B   B B   B   Y
BBBB   OOO   BBBB  BBBB    Y
____________________________________________________________
     Hello, I'm Bobby.
     What can I do for you?
____________________________________________________________
____________________________________________________________
Here are the tasks in your list:
     1.[T][X] read book
     2.[D][ ] return book (by: Jun 06 2019 6:00 PM)
     3.[E][X] project meeting (from: Aug 06 2019 2:00 PM to: Aug 06 2019 4:00 PM)
____________________________________________________________
____________________________________________________________
     Bye! Hope to see you again soon.
____________________________________________________________
```

## T09 — invalid saved data starts with an empty list

**Aim:** A malformed save record produces a clear error and does not load a partial or invalid task list.

**Inputs:**

```text
list
bye
```

**Command:**

```powershell
$ProgressPreference = 'SilentlyContinue'; New-Item -ItemType Directory -Force data | Out-Null; "D | 1 | missing deadline" | Set-Content data/duke.txt; javac --release 25 -d out/date-time-verification (Get-ChildItem -Recurse -Filter *.java -Path src/main/java | ForEach-Object FullName); "list", "bye" | java -cp out/date-time-verification bobby.Bobby
```

**Expected output:**

```text
____________________________________________________________
BBBB   OOO   BBBB  BBBB  Y   Y
B   B O   O  B   B B   B  Y Y
BBBB  O   O  BBBB  BBBB    Y
B   B O   O  B   B B   B   Y
BBBB   OOO   BBBB  BBBB    Y
____________________________________________________________
     Hello, I'm Bobby.
     What can I do for you?
____________________________________________________________
____________________________________________________________
     Unable to load tasks from disk. Starting with an empty list.
____________________________________________________________
____________________________________________________________
Here are the tasks in your list:
No tasks added yet.
____________________________________________________________
____________________________________________________________
     Bye! Hope to see you again soon.
____________________________________________________________
```
