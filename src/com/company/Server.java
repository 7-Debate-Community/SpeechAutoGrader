package com.company;

import com.ibm.cloud.sdk.core.http.HttpMediaType;
import com.ibm.cloud.sdk.core.security.Authenticator;
import com.ibm.cloud.sdk.core.security.IamAuthenticator;
import com.ibm.watson.speech_to_text.v1.SpeechToText;
import com.ibm.watson.speech_to_text.v1.model.RecognizeOptions;
import com.ibm.watson.speech_to_text.v1.model.SpeechRecognitionAlternative;
import com.ibm.watson.speech_to_text.v1.model.SpeechRecognitionResult;
import com.ibm.watson.speech_to_text.v1.model.SpeechRecognitionResults;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Server {
    String apiKey;

    public Server(String setApiKey) {
        this.apiKey = setApiKey;
    }

    public double getScore(File audio) {
        SpeechRecognitionAlternative speechRecognitionAlternative = getAlternative(audio);
        if (speechRecognitionAlternative == null) {
            return 0;
        }
        List<String> source = getList("src\\com\\company\\Source", true);
        System.out.println(speechRecognitionAlternative.getTranscript());
        List<String> answer = getList(speechRecognitionAlternative.getTranscript(), false);
        if (source == null || answer == null) {
            return 0;
        }
        return getAccuracy(source, answer);
    }
    private SpeechRecognitionAlternative getAlternative(File audio)  {
        try {
            Authenticator authenticator = new IamAuthenticator(apiKey);
            SpeechToText service = new SpeechToText(authenticator);

            RecognizeOptions options = new RecognizeOptions.Builder()
                    .audio(audio)
                    .contentType(HttpMediaType.AUDIO_MP3)
                    .build();
            SpeechRecognitionResults speechRecognitionResults = service.recognize(options).execute().getResult();
            SpeechRecognitionResult speechRecognitionResult = speechRecognitionResults.getResults().get(0);
            return speechRecognitionResult.getAlternatives().get(0);
        } catch (FileNotFoundException fileNotFoundException) {
            System.out.println("file not found");
            return null;
        } catch (Exception e) {
            System.out.println(e.toString());
            return null;
        }
    }
    private static List<String> getList(String str, boolean isFilePath){
        try {
            List<String> list;
            if (isFilePath) {
                Scanner s = new Scanner(new File(str));
                list = new ArrayList<String>();
                while (s.hasNext()){
                    list.add(s.next());
                }
                s.close();
            } else {
                list = Arrays.asList(str.split(" "));
            }

            for (int index = 0; index < list.size(); index++) {
                list.set(index, list.get(index).replaceAll("[^a-zA-Z]", "").toLowerCase());
            }
            return list;
        } catch (FileNotFoundException fileNotFoundException) {
            System.out.println(fileNotFoundException.toString());
            return null;
        }

    }
    private static double getAccuracy(List<String> source, List<String> answer) {
        int length = source.size();
        for (String word : answer) {
            if (source.contains(word)) {
                source.remove(word);
            }
        }
        return 1 - (double) source.size() / length;
    }
}
