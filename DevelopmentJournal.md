# March 2014 #

11 Mar 2014
  * Created title screen.

20 Mar 2014
  * Created stub classes for game objects.
  * Incorporated accelerometer tutorial code into project.
  * Instructions popup implemented.

24 Mar 2014
  * Implemented difficulty select screen, high score screen, and game screen as Fragments.
  * Game screen currently displays initial and current accelerometer values.

28 Mar 2014
  * Reorganized classes into packages.
  * Created `Foreground` class as `SurfaceView` extension and added to `GameFragment`.

# April 2014 #

2 Apr 2014
  * Added `Shot` and `Gun` sprites.
  * `Player` and `Gun` appear on stage and update in response to accelerometer values.

10 Apr 2014
  * Accelerometer controls store initial x and y values as zero when `GameFragment.resume()` is called.
  * High scores display properly.
  * All Java import declarations reorganized.
  * `Shot` and `Player` track hitboxes.
  * `Foreground` and `Background` classes renamed to `ForegroundView` and `BackgroundView`.
  * `Player` fires cannon when charge meter is full.
  * Tapping screen fires EMP, and `Shots` are removed when within EMP radius.
  * Tapping pause button pauses game.

14 Apr 2014
  * Created `Enemy` sprites.
  * Split existing `Player` sprite into ship and exhaust sprites.

20 Apr 2014
  * Added freeware sound effects taken from flashfix.com.
  * Sound effects play when `Gun`s fire, when EMP triggers, when `Player` fires laser, and when `Player` is hit.
  * Moved various event handling algorithms from `ForegroundView` to `Player`, `Gun`, `Enemy` classes.
  * `Enemy` and `Gun`s will move from bottom of screen to top when level starts.
  * Implemented several different firing patterns.

# May 2014 #
1 May 2014
  * Replaced `InstructionFragment` with `PreferencesFragment`.
  * `PreferencesFragment` uses `SharedPreferences` to track control style (tilt or touch) and difficulty level.
  * Removed `StartFragment`.
  * Streamlined level start countdown to remove if switch.
  * `Enemy` updates `Gun` positions while in motion.
  * Implemented touch controls. Currently ship will follow position of finger.
  * `Player` EMP activated with multi-touch screen tap.
  * Sound IDs retrieved when `GameFragment` loads, rather than when initializing `ForegroundView`.
  * Added init() methods to `Enemy`, `Player`, `Gun` classes.
  * Doubled `Player` and `Enemy` exhaust framerates.

14 May 2014
  * Fixed bug where `Gun` firing pattern 1 intermittently fired with no cooldown.
  * Getting a game over brings up `HiScoreFragment`. TODO:  adjust delay between game over and `HiScoreFragment`.
  * Implemented a `ScoreManager` to handle file IO.
  * Clearing a level brings up `ResultFragment`. TODO: adjust delay between level clear and `ResultFragment`.
  * Session scores now persist between levels.
  * Added explosion sprites.
  * Implemented `PreferencesFragment` to select difficulty, control style, and score resetting.

22 May 2014
  * All UI strings populated from resources.
  * `TableLayout` in `ResultsFragment` uses proper alignment.
  * Added accelerated timer option for debug.
  * Added delay before changing screens after stage clear or game over.
  * Removed superfluous enemy ID array from `GameFragment`.
  * `ScoreManager` uses two `ArrayLists` instead of `HashMap` to store scores.
  * `ResultsFragment` now goes to `TitleFragment` or `GameFragment`.
  * `GameFragment` increments level from `ResultsFragment`.
  * `Enemy` properly handles changing sprites and `Gun` placements between levels.
  * Hit detection uses `Player` center pixel instead of `Player` hitbox.
  * Removed superfluous `Player` hitbox and associated methods.
  * `Player` uses beam sound from `SoundPool` instead of custom `AudioTrack`.