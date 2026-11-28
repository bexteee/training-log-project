# Training Log Project

A rule-based classifier for sprint swimmers' training, built as a course project by Vitor Sa and Owen Bartee.
A Java core holds the data model and the effort calculation; a Python wrapper cleans the data collected with
Google Forms. The classifier itself (a decision tree) is planned but not built yet.

## Goal

Our research question: **can set-level data (distance, effort % and rest) put a swim set into an energy-system
category** (for example aerobic or anaerobic)?

We have not answered it yet. So far we built what the question needs: a format for logging sets, real data from
six swimmers on our team, and the calculation of how hard each rep was compared with the swimmer's best time.
The classifier is the next step, and its output will be checked against labels from our coach.

Why we care: we want an easy way for us and our team to see whether what we do at the pool matches what our
coach planned.

## What works now

| Part | Status |
|---|---|
| Python cleaner (Google Forms exports → clean CSVs) | works |
| Java data model and best-time lookup (push/dive) | works, tested |
| Java reader for best times | works, tested |
| Reading logged sets into Java, effort % and rest per set from the files | not yet |
| Decision tree (classifier) and agreement % | not yet (stretch goal) |

* **Python cleaner** (`wrapper/clean_data.py`): reads the CSV files we export from the Google Sheets linked to our
  forms (`data/raw/`), converts times like `1:02.35` to seconds and dates to ISO format, and writes clean files to
  `data/clean/` in the format the Java side expects. It skips sets swum with equipment (fins, paddles) and sets
  without a start (dive/push), and prints a message for each skipped set.

* **Java data model**: classes for what matters when analyzing a swimming practice: a set (stroke, start, rep
  distance, send-off interval, rep times), a workout (a swimmer's sets on one day, in order) and a best time
  (race or test best, dive or push). Every class checks its input and rejects impossible values.

* **Best-time lookup**: a symbol table (a Java `HashMap`) keyed by swimmer, stroke, distance, kind (race or test)
  and start (dive or push). For a logged set it finds the best time to compare with, in this order: a race best
  with the same start, then a test (practice) best with the same start; if neither exists, the other start (race,
  then test), and the set is flagged as a start mismatch. Example: a swimmer logs 3x50 free @ 4:00 from a push.
  Each rep is compared with the swimmer's 50 free race best from a push (or, if there is none, their push test
  best), and the effort of each rep is best time / rep time x 100.

* **Best-times reader** (`BestTimesReader`): reads `data/clean/best_times.csv` into the symbol table above.

## Requirements

* Java 21 or newer, and Maven
* Python 3 (we use 3.11); only the standard library, no extra packages

## How to run

Clone the repository and open a terminal in its main folder (`training-log-project`).

**1. Clean the data** (run from the main folder, because the script uses paths like `data/raw/...`):

```
python wrapper/clean_data.py
```

It writes `data/clean/best_times.csv` and `data/clean/sets.csv`, and prints a line for every skipped set
(equipment used or start missing).

**2. Run the Java tests:**

```
cd training-log
mvn clean test
```

Expected result: `Tests run: 60, Failures: 0` and `BUILD SUCCESS`.

## Data pipeline

Each stage writes a file that the next stage reads.

```
Google Forms → Google Sheets → data/raw/ (CSV exports)
            → wrapper/clean_data.py → data/clean/ (clean CSVs)
            → Java (training-log/): best times loaded into the symbol table
            → (planned) sets read → effort % and rest per set → decision tree → agreement %
```

## Repository layout

| Folder | Contents |
|---|---|
| `training-log/` | Java project (Maven): data model in `model/`, CSV readers in `io/`, JUnit tests |
| `wrapper/` | Python cleaner |
| `data/raw/` | exports from the Google Forms (best times, set log) |
| `data/clean/` | cleaned CSVs written by the Python cleaner |
| `data/reference-labels.csv` | reference categories for the test set |
| `docs/` | design notes |

## Data and privacy

The data comes from two Google Forms (best times and a set log) answered by six swimmers on our team about
practices in November 2026. Swimmers appear only as anonymous IDs (A01 to A06); the list that links IDs to names
is kept privately and is never in this repository.

## Team and AI use

* Vitor Sa: data model, best-time lookup, readers, Python cleaner, data collection.
* Owen Bartee: classification rules (decision tree).

Our team's rule is that AI does not write our code. We used Claude (Anthropic) to review code, explain concepts
and design test cases; every use is described, with dates, in the AI-use statement of our sprint reports.
