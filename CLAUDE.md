# Training Log Project

Project name since 2026-11-12: "Training Log Project" (training-log-project). It is a rule-based classification
program (Java core + Python wrapper), not an API. GitHub repository: https://github.com/bexteee/training-log-project
(renamed from training-log-api on 2026-11-12; the local folder may still be called `training-log-api`).

Semester CS course project (algorithms/data structures, performance analysis). Team: Vitor Sa, Owen Bartee
(Andy left the group in October 2026). Instructor: Marcus Birkenkrahe.
Java is the core language (data model, effort %, decision tree). Python is a wrapper around it (decided 2026-11-09,
confirmed with the instructor): it reads and cleans the CSV files (e.g. Google Forms exports), passes the data to Java,
and handles the output (agreement %, matplotlib graphs). Vitor writes the Python wrapper.

## Current plan (from the Sprint 2 literature review, 2026-11-01)

A Java application whose core is a rule-based classifier for sprint swimmers' training. AI is only an optional
add-on that explains scores and makes visuals. Scope is sprinters only.

- Research question: can a rule-based classifier using only set-level data (distance, intensity, rest interval)
  assign swim sets to energy-system categories as defined in the swimming-training literature?
- Base for the categories: the five training zones of Fernandes et al. (2024) (aerobic capacity 1-3, aerobic power,
  anaerobic power and capacity).
- The classifier is a hand-built decision tree (team decision, 2026-11-02): the team writes every split and threshold
  from the literature, each linked to a source. A learned tree is at most a later comparison.
- MVP (swimming only):
  1. a defined format for logging a set,
  2. rules that turn a logged set into a category,
  3. a way to run the rules on a collection of sets,
  4. a test set to check the output against.
- Measurement: agreement with a reference, i.e. the percentage of test sets where the classifier's category
  matches the reference category. Reference categories come from the literature (the coach does not label sets
  by energy system) and must be decided independently of the rules.
- Defined metric (built on top of the classifier, not part of the MVP): one Training Alignment Score combining
  workout-vs-plan match with lift-vs-swim alignment.
- Stretch goals: logging full workouts and weekly summaries; extending the classifier to lifting; the overall
  alignment score; optional AI explanations and visuals; checking the classifier against the coach's labels.
- Sprint 3 targets: the logging format in Java (classes for a set and a workout) with example sets; the first
  version of the classification rules with their sources, running on example sets; a test set with a reference
  category per set and where it came from; a first agreement percentage.

Sprint 2 sources (APA 7): Fernandes et al. (2024), Hellard et al. (2019), Okičić et al. (2007), Petrov et al. (2026),
Pollock et al. (2019), plus the coach interview (A. Makepeace, personal communication, October 30, 2026).

## Earlier plan (before Sprint 2), kept for reference

The original design checked logged training against a periodization plan: a `TrackComparator` interface
(`SwimTrackComparator`, `LiftTrackComparator`) comparing a `PlannedSession` with a `LoggedSession`, and a benchmark of
three strategies for pairing planned vs. logged sessions by date (nested loop O(n·m), sort + two-pointer O(n log n),
hashmap O(n)) with a doubling-hypothesis experiment. This is no longer the core and is not in the Sprint 2 document;
the comparison side now belongs to the stretch goals (alignment score, weekly summaries). Whether the pairing
benchmark returns as the algorithms/performance part of the project is not decided.

The bioflexible periodization book (Silva & Affonso, 2026, Editora Dialética) was considered but not used as a source.

## Rules for Claude (the TEAM's AI rule, stricter than the course requires — non-negotiable)

Claude does NOT write code for this project. This covers Java and Python source, tests, the benchmark harness,
the synthetic data generator and plotting scripts.

Claude MAY:
- Review code and flag bugs/logic errors, pointing to `file:line` and explaining *why* — the author writes the fix.
- Explain concepts, algorithms, complexity, and unfamiliar syntax/library behavior (generic examples only,
  never a drop-in solution for this project).
- Help design test cases as plain-language tables (input → expected output), not test code.
- Review benchmark methodology and results, and discuss design decisions (e.g. the compliance metric).

Claude must NOT:
- Edit or create `.java`, `.py` or `.ipynb` files (also enforced in `.claude/settings.json`), including via shell commands.
- Paste project-specific implementations in chat, even "just as a sketch".
- Run `/code-review --fix` or apply review findings itself.

Teaching exception (instructor, meeting 2026-11-24, reported by Vitor 2026-11-25): Claude may TEACH decision trees.
It may read the professor's handout and Colab notebook, simplify them, and make learning material with GENERIC
examples (toy datasets, not the project's swim data, features, labels or pipeline). The project's own tree code
(training, evaluation, agreement %) is still written by Vitor/Owen. Notebook files stay blocked for Claude in
`.claude/settings.json`: lessons are delivered as Markdown/HTML or chat, and the user makes any notebook themselves.

Every team member must be able to explain and reproduce any part of the codebase without AI (Sprint 4 spot-checks).
The files under `.claude/` are team tooling, not project code, and their creation is recorded in the AI-use log.

## AI-use log

Every prompt is recorded automatically in `docs/ai-log/prompts-<name>.md` (UserPromptSubmit hook).
This folder is gitignored: each member's log stays on their own machine and is never committed.
Use it to write the Sprint 3/4 disclosure entries: tool, purpose, what was used vs. changed/corrected.
When a session produces something the team adopts, remind the user to add a disclosure entry.

## Schedule

- Sprint 1 (proposal): graded. Instructor feedback 2026-10-12: define "how well you are training" as a computable
  number first; links are not references; no AI-use statement was given; label Title/Authors sections.
  Grading is stricter from Sprint 2 on.
- Sprint 2 (literature review): due 2026-10-30, finalized 2026-11-01; sprint review in class 2026-11-02
  (each member presents >= 1 source). It answers: what did we achieve, what did we plan but not achieve,
  what will we do in Sprint 3.
- Sprint 3: about one month; targets listed under "Current plan".
  Sprint 2 feedback (instructor email, read 2026-11-23): results sprint (Sprint 3) was DUE Fri 2026-11-20. Points:
  set up `thirdsprint/` in the project GDrive + link the whole GDrive (papers, data); MEASUREMENT TRAP: reference
  labels must be independent of the rules (rules from Fernandes -> labels from the coach labeling blind, or a second
  scheme such as Pollock et al. 2019), so labels citing Fernandes are circular; define "effort" as a loggable number
  and map it onto Fernandes's intensity domains; verify the Okicic et al. annotation (show the page); text must be in
  the team's own words, add WHEN Claude was used to the AI statement; course connection = symbol table (textbook 4.4)
  keyed by date or category + counting per category; still need a training plan from the coach. Presenting: start
  with the goal in plain words, talk instead of reading.
  Decided 2026-11-23 (Vitor + Owen, sprint review): reference labels come from Coach Makepeace, labeling the logged
  sets blind (no classifier output shown). The Fernandes-based A06 label in data/reference-labels.csv is replaced by
  the coach's label (labeled_by = coach). Sprint 3 submission planned for the night of 2026-11-23.
  Tree categories, DRAFT (Vitor + Owen, 2026-11-23): aerobic = effort 60-75%, hybrid = 76-90%, anaerobic = 92%+
  ("hybrid" replaces AEROBIC_POWER; labels file and coach request must use the same names). Open when building the
  tree: gaps (<60, 75-76, 90-92), a source for each cutoff, whether distance and rest also split.
  Sprint 3 document reviewed 2026-11-23 (Claude drafted revised wording for sections 1, 3, 5, 6, 7, 8c and the AI
  statement, disclosed in the statement). Decision to-do list lives in the report doc
  https://claude.ai/artifact/DvAtRgiECMzpoeX9uZRV2r ("Decision to-do list" section).
  Vitor's work order from 2026-11-24 on: (1) push/dive change (Set Log form question, ask swimmers, Python `start`
  column, Java SwimSet/BestKey/BestTimes + tests); (2) back to BestTimesReaderTest (file belongs in
  src/test/java/edu/augustana/traininglog/io/, helper with varargs rows, 7 planned cases); (3) decision tree, starting
  with the saved category questions (gaps, sources, mapping to Fernandes, extra splits, names).
  Work branch: `java-entry-point` (stacked on `python-wrapper`, whose PR to main is open).
  Status 2026-11-24: push/dive in Java done and committed: BestKey has start; new record ReferenceTime(time,
  startMatch); BestTimes.findReferenceTime(athlete, stroke, distance, setStart) -> Optional<ReferenceTime> via private
  helper lookUpKindAndStart (same start race -> test, then other start race -> test with startMatch false, else empty);
  BestTimesTest updated (52 tests passing). Plan for 2026-11-25 (trimmed for time, presentations start Nov 27):
  4 push/dive tests only (start beats kind; only other-start race; only other-start test; dive set falls back to push),
  fill in the swimmers' Start answers + re-export + re-clean, BestTimesReaderTest (cut to ~3 cases if short on time),
  then Vitor brings the professor's decision-tree guidance (meeting 2026-11-24). Risk: Owen's tree is the critical
  path to a first agreement % and is not in the repo yet; check in with him.
  Professor meeting 2026-11-24 (reported by Vitor 2026-11-25): build the decision tree in Python, possibly with
  scikit-learn (a learned tree, supervised learning). Not yet decided with Owen: replace the hand-built tree or run
  both and compare. Task type = classification (target = category), not regression. Concern: only ~25 logged sets,
  so a learned tree needs cross-validation (e.g. leave-one-out) and a majority-class baseline.
  Professor's follow-up email "Decision Tree" (2026-11-24, Owen cc'd): example handout
  https://github.com/birkenkrahe/cosc_3430_fall_26/blob/main/pdf/2a_diagnosis_decision_tree_handout.pdf , runnable Colab
  https://colab.research.google.com/drive/17dXYC4nexGJhyLkG1W7UFga9Nkq5D4Xs , DataCamp course "Machine Learning with
  Tree-Based Models in Python" (untried by him); Gini = probability two rows in a node have different classes, 0 at a
  pure leaf. He offered a private project presentation in finals week, best Wed 2026-12-09 1-3 pm (reply pending).
  Team's class presentation date: 2026-12-02.
  Status 2026-11-26: 4 push/dive fallback tests committed; BestTimesReaderTest done in src/test/java/.../io/ (helper
  writesBestTimes(String... rows) with @TempDir + 4 tests: one row, header only, several rows, missing file throws);
  60 tests passing. Next: swimmers' Start answers -> re-export/re-clean; then decision-tree lessons (generic) and the
  learned tree in Python.
  Sprint 3 feedback (instructor email, read 2026-11-27): late submission accepted without penalty; 2 points deducted
  (only the report was submitted, no link to all materials; "What you built" was a bare link to branch data-model and
  the README doesn't say what runs or how; AI statement lacks what the team verified itself; no reference list).
  Also: no title; Results was a done-list without a table -> add a table of collected data (sets and best times per
  swimmer, or sets per stroke) counted with the symbol table; all commits come from one account, so each member
  presents a part, including code. AI writing code is allowed by the course (report it, explain every line); the
  no-AI-code rule is the TEAM's own rule. Decision tree = stretch goal, not needed for the presentation; if done:
  written by the team as a few nested ifs on distance, effort and rest (no extra material needed).
  Presentation sprint: recorded 10-15 min online, OR a 30-min meeting Wed 2026-12-09 in FSC 372 (book on Canvas page
  "Optional: show me what you got on Wed Dec 9th"; both members must attend). Bring: AI use, research question, the
  program running, results. HARD DEADLINE for all materials (slides, code, recording if any), one accessible link:
  Fri 2026-12-04 11:59 pm, also for the Dec 9 meeting; missing or inaccessible = 0 points.
  Decided 2026-11-27 (Vitor): the team takes the Dec 9 meeting, with slides. Vitor presents the demo (he built the
  code); Owen takes the rest (report fixes, slides, etc.); what to say/show is planned later. Swimmers' Dive/Push
  answers expected 2026-11-28 (needed for the results table, since data/clean/sets.csv is empty until then).
  Vitor still wants the decision tree as a learning goal: generic lessons first, then the team writes it as nested
  ifs (instructor's advice), after the required items. Plan to Dec 4: (1) merge python-wrapper + java-entry-point
  into main, (2) README (what runs, how), (3) results table (sets and best times per swimmer, sets per stroke,
  counted with a symbol table), (4) one link to all materials (GDrive thirdsprint/), (5) report fixes (title,
  references, AI statement with what the team verified, "team rule"), (6) Owen commits his own part, (7) slides,
  (8) tree if time is left.
- Final presentations: 2026-11-27, 11-30, 12-02 or 12-04.

## Work split (decided 2026-10-29)

- Owen: categorization (the classification rules).
- Vitor: the data model (logging format for sets and workouts) and the comparison side (now stretch goals).
- Stack (2026-11-02): Java 21, Maven, JUnit 5, CSV input files; Vitor uses VS Code. Every file/change is committed
  to the team's GitHub repository.
- Classifier input fields (2026-11-02): distance, intensity, rest. Intensity = effort % calculated from the swimmer's
  race best (race best / actual time x 100).

## Vitor's task plan (made 2026-11-11, by priority)

Done on branch `data-model`: enums (StrokeType, BestKind, StartType), RaceBest, SwimSet, Workout (+ tests for the first
three; WorkoutTest in progress, helper `setAtPosition`).
1. Finish WorkoutTest (13 cases), fix pom (`maven.compiler.release` 21), commit + push.
2. Sync with Owen: status of his tree; handover contract (distance, average rest, effort % per set); clean CSV
   columns; performance experiment (e.g. hash lookup vs list search) yes/no.
3. Best-time lookup (HashMap, record key, Optional; race > test > none; duplicates -> faster) + set effort % + tests.
4. Open the pull request data-model -> main for Owen's review.
5. Example data: hand-written clean CSVs (10-20 sets, best times); test set with reference categories from the
   literature, decided independently of Owen's rules (with Owen).
6. Java entry point: read clean CSVs, build objects, run the tree, write results CSV; `mvn package` -> jar
   (needs Owen's tree).
7. Python wrapper: raw CSV -> clean CSV (time conversion), run the jar via subprocess, agreement % + first graph.
8. Later: Google Form; AI-use disclosure notes for Sprint 3.
Status 2026-11-12: tasks 1-3 done (36 tests, pushed). Repo renamed to training-log-project; local folder kept as
training-log-api. Progress summary + questions for Owen: `docs/My Work So Far - Vitor.docx` (renamed from the meeting agenda on
2026-11-16; repo access is open for Owen). Shared with Owen on 2026-11-16; answers expected 2026-11-17. Now on task 5 (best-time lookup + effort %).
Status 2026-11-16: BestKey + BestTimes (addBestTime keeps faster; findReferenceTime race -> test -> empty;
bestTimesCount) done, BestTimesTest with 11 tests; 47 tests total passing. Next: SwimSet.averageEffort(referenceTime).
Status 2026-11-17: SwimSet.averageEffort(referenceTime) done with 4 tests; 51 tests passing. Data model complete.
Owen has not answered "My Work So Far" yet; no classifier code from him in the repo.
Pull request data-model -> main opened 2026-11-17 (Owen as reviewer; not merged).
Plan 2026-11-18: build a SMALL decision tree first (fewer zones, add detail later); Python wrapper done by the end of
the weekend (Nov 22); Google Forms (best times + set log) sent to 6 swimmers by Friday Nov 20 to collect real data.
Forms published and emailed 2026-11-18: Best Times https://forms.gle/CmomCEc3xQV3R3qK9 , Set Log
https://forms.gle/c8gXcFFyoX9YgUU76 . Swimmers A01-A06 (name-to-ID list kept privately by Vitor and Owen, never in the
repo). Practices to log: Tue Nov 17, Fri Nov 20, Sat Nov 21 (race practices, comparable with season bests).
Status 2026-11-19: first responses checked (11 best times, 2 sets), no input fixes needed. The wrapper must handle: a
blank row, times as number or "m:ss.hh" text, send-off "m:ss" text, rep times "a, b, c", set number as 1.0.
Deadline for swimmers' responses: Saturday Nov 21 afternoon, then the first test run.
Status 2026-11-21: PR data-model -> main merged; working on branch `python-wrapper`. Data contract committed (headers
only, hand-written by Vitor): `data/clean/sets.csv` (swimmer_id, practice_date, set_number, stroke, rep_distance,
send_off_in_seconds, rep_times; one row per set, rep times separated by ";", stroke/kind/start as enum names, dates ISO)
and `data/clean/best_times.csv` (swimmer_id, stroke, distance, kind, start, best_time_in_seconds, date_swum).
Flow: raw form exports in `data/raw/` -> Python cleans -> `data/clean/` -> java -jar (paths as args) ->
`data/results/` -> Python joins with labels -> agreement %. Test-set labels: `data/reference-labels.csv`
(swimmer_id, practice_date, set_number, reference_zone, reason, source, confidence, labeled_by, date_labeled), zones
AEROBIC / AEROBIC_POWER / ANAEROBIC, labeled by Vitor from the literature before any tree code; 1 set labeled so far.
Status 2026-11-22: `wrapper/clean_data.py` (Vitor) has time_from_txt_to_float, times_from_txt_to_list,
datetime_from_str_to_iso_text (M/D/YYYY -> ISO), clean_row (best times) and write_each_clean_row (read raw, skip fully
blank rows, write clean). Best times cleaning works (13 rows -> data/clean/best_times.csv, committed). Empty values
raise ValueError (fail fast). A02's 100 fly times (TEST 53.5 on 2026-11-21, RACE 1:00.05) are both correct per Vitor;
race-first rule makes his 53.5 set ~112% effort (expected outlier).
Status 2026-11-22 (later): Set Log cleaning done (set_clean_row; write_each_set_log_clean_row, a second writer function
by choice). Equipment = Yes rows are skipped in Python with a printed "Skipped <id> <date> set <n>: equipment used"
message (contract unchanged). 16 raw sets -> 15 in data/clean/sets.csv (A03 equipment set skipped). Raw -> clean half of
the wrapper complete. Data issues to raise with swimmers: A06 2026-11-21 set 1 looks like a mixed set logged as one
(250 @0:00 with 3 different-length reps; Java rejects send-off 0); A05 2026-11-17 set 1 mixes easy 35s with two fast
50s; A05 has no 25 fly/back/breast bests (missing reference); A01 and A04 have no data.

## Data model decisions (2026-10-27)

Made for the earlier plan. The set-level fields still inform the MVP's logging format; the pairing and scoring
rules apply to the stretch goals (alignment score, weekly summaries). Full metric spec: `docs/compliance-metric.md`.

- Two levels: Session (date, practice number, type SWIM/LIFT) contains Sets; comparison happens at the set level
  (main sets only; warm-up/cool-down ignored).
- Sets within a paired session are matched by position; skipped sets are not modelled (swimmers almost never skip).
- Pairing key: date + practice number + type (practice 1, 2, ... = the planned practices of that day). The logged
  session states which planned practice it is for, so a missed practice made up later the same day still pairs.
  No make-up that day -> missed. A session moved to another day counts as missed + extra (rare).
  Practice number is counted per type (swim 1, swim 2, lift 1). A logged practice that is not in the plan counts as
  extra; input that breaks the daily rules below is rejected.
- At most 2 practices per day, and only these combinations: swim + lift, swim + swim, or swim only.
- One plan shared by the whole sprint group (same schedule); logs are per athlete.
- Swim sets log every rep's time in seconds. Achieved effort is calculated from times: race best time / actual time
  x 100 (race best = 100%; faster gives > 100%, no cap). Needs each athlete's race best per distance and stroke.
  This effort % is a candidate for the classifier's "intensity" input, since it is relative to each swimmer.
  Sets with equipment (fins, paddles...) are compared with goal times only, no effort %.
- Compliance metric (stretch goal; averaging the sets confirmed by Vitor and Owen 2026-11-01):
  - Swim set, no equipment: score = achieved effort / goal effort (reported as "93.8% (goal 95%)").
  - Swim set with equipment: score = goal time / actual time x 100.
  - Lift set: score = (sets x reps x load %) logged / (sets x reps x load %) planned.
  - Session score = average of its set scores. Missed session = 0%.
  - Two weekly scores: swim and lift, each the average of that week's session scores.
  - Extra sessions are not averaged; they are reported separately as a count.
  Compare times with a tolerance, never with ==.
- Lift load is stored as % of 1RM, planned and logged.

## Coach input

Coach Andrew Makepeace answered our questions (Vitor's file "Answers from Coach Andrew.txt", 2026-10-30). Used as
background on how periodization works: he plans weekly, adapts daily, does not write energy systems in workouts,
values effort over times in sprint sets, and plans lifting independently of swimming. His Q1 answer (season
structure: two peaks, ~16-week block, 3-4 week sub-blocks) is a garbled transcription; confirm before citing.
He agreed his plans can be used (anonymized); no example practice yet.

## Open decisions (as of 2026-11-01)

1. Maven project set up in `training-log/` (Java 21, JUnit 5.11.4; builds with `mvn test`). Packages agreed:
   `model` (Vitor), `classifier` (Owen), `io` (CSV reading). With the Python wrapper, the messy CSV reading moves to
   Python; still open: exactly how Python passes the data to Java (e.g. a clean CSV file + `java -jar` via subprocess)
   and what remains in the Java `io` package.
   Owen suggested perceived effort (RPE) and closeness to a goal time; the team kept effort % from race best as the
   only intensity measure (2026-11-02).
2. Intensity = effort % from race best (decided). Storage decided 2026-11-02 (option A): the CSV stores raw data
   (athlete, stroke, distance, rest, rep times) and effort % is calculated in code from a separate race-best file
   (athlete, distance, stroke, best time). A set's effort = average of rep efforts (decided 2026-11-04).
   Still open: how reference categories are chosen independently of the rules.
   Swim set table (being designed with Vitor, 2026-11-04): athlete = short ID (e.g. A01, anonymous); stroke = one of
   4 strokes (free, back, breast, fly; no IM); no equipment in the MVP (equipment = stretch goal / later change);
   pool = short course yards (SCY) only; rep distance = any positive value (sets like 10x15 @ 60 exist); rep times in
   seconds with hundredths; both the send-off interval and rest are stored.
   Rest used by the classifier = AVERAGE rest of the set, where each rep's rest = send-off interval - rep time.
   Why (to explain to Owen): rest differs rep by rep because slower reps leave less rest; the average is one number
   per set for the tree, stays close to the rest the coach intended with the interval, and is not distorted by a
   single slow or fast rep the way the shortest or longest rest would be.
   If a rep time is longer than the interval, that rep's rest counts as 0 (the set is not rejected).
   Each set stores its position in the workout (set 1, 2, ...). The athlete ID is stored on the workout (with the
   date), not on each set.
   Set effort % (classifier intensity) = AVERAGE of the rep efforts, each rep effort = best time / rep time x 100
   (not the effort of the average time). Why: consistent with average rest, every rep counts equally (the coach says
   each effort matters), simple to explain and test. Median is a possible later comparison.
   Shared design doc for Owen: `docs/Data Model - Swim Set.docx` (2026-11-04).
   Workout design (2026-11-10): a mixed set (e.g. 6x12.5 @30 + 100 easy + broken 100 + ..., swum twice) is split into
   parts, each part = one SwimSet (one distance, stroke, interval), logged in order; a repeated set continues the
   positions (second time through = next numbers). Easy swims between efforts are NOT logged (treated like
   warm-up/cool-down). Recovery practices ARE logged (their sets are easy swimming, low effort). A workout has at
   least one set. Set positions in a workout must have no gaps and no duplicates (1, 2, 3, ...).
   Rep distance (and best-time distance) changes from int to double so 12.5 yd can be logged.
   In code the SwimSet position field is currently named `round` (suggested rename to `position`: "round" now
   sounds like a repetition of a set).
   Race/test best table (2026-11-04): a swimmer can have both a race best and a test best for the same stroke and
   distance; effort % uses the race best when one exists, otherwise the test best. Each best records its start
   (dive or push) for transparency only, no adjustment (push vs dive is a stated limitation). Only the latest best is
   kept, with the date it was swum (replaced when the swimmer improves). A set with no matching best is skipped (not
   classified) and reported as "missing reference time".
   Key = athlete + stroke + distance + kind (RACE/TEST); start = DIVE/PUSH; date = when swum (not in the future).
   CHANGED 2026-11-23 (Vitor): push and dive bests are now distinguished. Each logged set records its start (new
   required Set Log question "Start: dive or push"; new `start` column in data/clean/sets.csv; SwimSet gets a
   StartType). BestKey adds start. Lookup for a set: same start first (race, then test); if none, fall back to the
   other start (race, then test) and flag the set `start_mismatch`; none at all -> "missing reference time".
   Agreement % is reported separately for matched vs mismatched sets. The 16 sets logged before the change get their
   start by asking each swimmer (no guessing). Why: practice reps are mostly from a push while race bests are from a
   dive, so comparing them underestimates effort, most on 25s/50s. Open: how to log backstroke race starts (in-water).
   Not yet implemented (BestTimes/BestKey/SwimSet and their tests change in a separate step).
   Duplicate rows for the same key: use the faster time. Limitation accepted for the MVP: workouts before a best's
   date are measured against the newer (faster) best; keeping dated history is a stretch goal (or fix manually).
   Short reps (e.g. 15 or 25 yd) have no race distance, so the race-best table also holds "test bests" (fastest
   practice time for that distance and stroke). Why (to explain to Owen, this is an addition to the project): effort %
   needs a reference time for the same distance and stroke; without test bests, sprint sets of 15s and 25s could not be
   classified, and scaling a 50 race best would be inaccurate (the first 25 of a 50 includes the start).
3. Whether the pairing benchmark / doubling experiment comes back as the algorithms-and-performance part of the
   project, and where.
4. Which matplotlib graphs to produce.
   Idea to brainstorm later (Vitor, 2026-11-04): a Google Form sent to the team's sprint group to collect each
   swimmer's sets and best times individually, exported to CSV as the project's data sets.
5. Every deliverable needs an explicit AI-use statement, including "none used". No AI was used on the Sprint 1
   proposal itself; Claude was used from 2026-09-28 for repo tooling and planning.
