# SpeechAutoGrader

## Introduction ##

This project is used to produce auto graded score for given speech and corresponding text. It uses IBM speech to text API. Generally, it converts audio file into text transcript, and calculate accuracy based on similarity between produced transcript and original file. 

## Set up ##

- Add maven dependency `com.ibm.watson:speech-to-text:8.4.0`
- Add all jar file under `/speech2text/lib` to `project structure - library`
- add `source.txt` and `api.txt` under `/speech2text/src/com.comany`

## Usage ##

- put all audio files (must be **mp3** type) in a single directory and paste that path to `public static final String FOLDER_PATH = "";`

- Modify required audio file length

  ```java
  	// lower bound for audio length
      public static final int LOWER_THRESHOLD = 45;
      // upper bound for audio length
      public static final int UPPER_THRESHOLD = 60;
  ```