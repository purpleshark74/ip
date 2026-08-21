# Console UI Test Plan

## Test environment

- **Java version:** 25
- **Build command:** `javac --release 25 -d out src/main/java/*.java`
- **Launch convention:** Run from the repository root with `java -cp out Bobby`.
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
javac --release 25 -d out src/main/java/*.java; "todo read book", "todo", "list", "blah", "list", "mark 1", "mark 0", "list", "unmark 1", "unmark 2", "list", "bye" | java -cp out Bobby
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

**Aim:** Malformed deadline and event commands do not alter the tasks added by valid commands.

**Inputs:**

```text
deadline submit /by Friday
deadline
list
event meeting /from Monday /to Tuesday
event coffee /from
list
bye
```

**Command:**

```powershell
javac --release 25 -d out src/main/java/*.java; "deadline submit /by Friday", "deadline", "list", "event meeting /from Monday /to Tuesday", "event coffee /from", "list", "bye" | java -cp out Bobby
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
       [D][ ] submit (by: Friday)
     Now you have 1 tasks in the list.
____________________________________________________________
____________________________________________________________
     Please use: deadline DESCRIPTION /by DEADLINE
____________________________________________________________
____________________________________________________________
Here are the tasks in your list:
     1.[D][ ] submit (by: Friday)
____________________________________________________________
____________________________________________________________
     Got it. I've added this task:
       [E][ ] meeting (from: Monday to: Tuesday)
     Now you have 2 tasks in the list.
____________________________________________________________
____________________________________________________________
     Please use: event DESCRIPTION /from START /to END
____________________________________________________________
____________________________________________________________
Here are the tasks in your list:
     1.[D][ ] submit (by: Friday)
     2.[E][ ] meeting (from: Monday to: Tuesday)
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
deadline pay bills /by Friday
event project /from Monday /to Tuesday
unmark 1
mark 3
list
bye
```

**Command:**

```powershell
javac --release 25 -d out src/main/java/*.java; "  TODO Walk dog  ", "   ", "list", "deadline pay bills /by Friday", "event project /from Monday /to Tuesday", "unmark 1", "mark 3", "list", "bye" | java -cp out Bobby
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
       [D][ ] pay bills (by: Friday)
     Now you have 2 tasks in the list.
____________________________________________________________
____________________________________________________________
     Got it. I've added this task:
       [E][ ] project (from: Monday to: Tuesday)
     Now you have 3 tasks in the list.
____________________________________________________________
____________________________________________________________
     OK, I've marked this task as not done yet:
       [T][ ] Walk dog
____________________________________________________________
____________________________________________________________
     Nice! I've marked this task as done:
       [E][X] project (from: Monday to: Tuesday)
____________________________________________________________
____________________________________________________________
Here are the tasks in your list:
     1.[T][ ] Walk dog
     2.[D][ ] pay bills (by: Friday)
     3.[E][X] project (from: Monday to: Tuesday)
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
deadline return book /by June 6th
event project meeting /from Aug 6th 2pm /to 4pm
mark 1
mark 2
delete 3
list
bye
```

**Command:**

```powershell
javac --release 25 -d out src/main/java/*.java; "todo read book", "deadline return book /by June 6th", "event project meeting /from Aug 6th 2pm /to 4pm", "mark 1", "mark 2", "delete 3", "list", "bye" | java -cp out Bobby
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
       [D][ ] return book (by: June 6th)
     Now you have 2 tasks in the list.
____________________________________________________________
____________________________________________________________
     Got it. I've added this task:
       [E][ ] project meeting (from: Aug 6th 2pm to: 4pm)
     Now you have 3 tasks in the list.
____________________________________________________________
____________________________________________________________
     Nice! I've marked this task as done:
       [T][X] read book
____________________________________________________________
____________________________________________________________
     Nice! I've marked this task as done:
       [D][X] return book (by: June 6th)
____________________________________________________________
____________________________________________________________
     Noted. I've removed this task:
       [E][ ] project meeting (from: Aug 6th 2pm to: 4pm)
     Now you have 2 tasks in the list.
____________________________________________________________
____________________________________________________________
Here are the tasks in your list:
     1.[T][X] read book
     2.[D][X] return book (by: June 6th)
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
javac --release 25 -d out src/main/java/*.java; "todo alpha", "todo beta", "todo gamma", "delete 2", "list", "delete 3", "list", "delete two", "list", "delete 2", "list", "delete 0", "list", "bye" | java -cp out Bobby
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
javac --release 25 -d out src/main/java/*.java; "delete 1", "list", "todo only task", "delete", "list", "delete 2", "list", "DELETE 1", "list", "delete 1", "list", "bye" | java -cp out Bobby
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
No tasks added yet.
____________________________________________________________
____________________________________________________________
     Invalid task number.
____________________________________________________________
____________________________________________________________
No tasks added yet.
____________________________________________________________
____________________________________________________________
     Bye! Hope to see you again soon.
____________________________________________________________
```
