# Console UI Test Plan

## Test environment

- **Java version:** 25
- **Build command:** _Record the command that builds the current application._
- **Launch convention:** _Record any required working directory, classpath, or JVM arguments._
- **Comparison rule:** Exact output match after line-ending normalization, unless a test case explicitly states another deterministic rule.

## Test cases

Add test cases in the order they should run. Each case starts a fresh program process.

No test cases are recorded yet. Copy the following template for each case, replacing every placeholder before running the suite.

## Test-case template — short name

**Aim:** Describe the behaviour being verified.

**Inputs:**

```text
<exact console input, one entry per line>
```

**Command:**

```powershell
<command that starts the program and supplies the inputs above>
```

**Expected output:**

```text
<complete expected console output>
```

> Every runnable test case must contain all four fields: aim, inputs, command, and expected output.
