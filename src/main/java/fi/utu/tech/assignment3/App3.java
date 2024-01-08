package fi.utu.tech.assignment3;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

import fi.utu.tech.assignment3.SubmissionGenerator.Strategy;


public class App3 {

    public static void main(String[] args) {
        LinkedBlockingQueue<Submission> gradedSubmissions = new LinkedBlockingQueue<>(30);
        

        List<AutomaticGrader> autograders = new ArrayList<>();
        for (int i=0; i<50; i++) {
            var ungradedSubmissions = SubmissionGenerator.generateSubmissions(20, 2000, Strategy.RANDOM);
            autograders.add(new AutomaticGrader(ungradedSubmissions, gradedSubmissions));
        }

        List<StudyRecord> studyRegistry = new ArrayList<>();

        StudyRegistrar studyRegistrar = new StudyRegistrar(gradedSubmissions, studyRegistry, "DTEK0095");

        studyRegistrar.start();

        for (var grader : autograders) {
            grader.start();
        }

        for (var grader : autograders) {
            try {
                grader.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        studyRegistrar.interrupt();
    }
}
