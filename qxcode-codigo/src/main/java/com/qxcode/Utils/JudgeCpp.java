package com.qxcode.Utils;

import java.io.File;
import java.util.ArrayList;
import java.io.IOException;
import java.lang.InterruptedException;
import java.lang.ProcessBuilder;


public class JudgeCpp implements IJudge {
    private final File userFile;
    private final ArrayList<File> outputsExpecteds;
    private final ArrayList<File> outputsUser;
    private final ArrayList<File> inputs;
    private final ArrayList<File> diffs;

    //private ControllerQuestion controllerQuestion;

    private final String pathQuestion = "../../../../resources/com/qxcode/Arquivos/File/Question.cpp";
    private final String pathOutputUser = "../../../../resources/com/qxcode/Arquivos/OutputUser";
    private final String pathOutputExpected = "../../../../resources/com/qxcode/Arquivos/OutputExpecteds";
    private final String pathInput = "../../../../resources/com/qxcode/Arquivos/Inputs";
    private final String pathDiff = "../../../../resources/com/qxcode/Arquivos/Diffs";


    public JudgeCpp() {
        //  controllerQuestion.getExtension();
        userFile = new File(pathQuestion);
        outputsExpecteds = new ArrayList<File>();
        outputsUser = new ArrayList<File>();
        inputs = new ArrayList<File>();
        diffs = new ArrayList<File>();
        carregar(pathInput, inputs);
        carregar(pathOutputExpected, outputsExpecteds);
    }

    private void carregar(String path, ArrayList<File> list) {
        File file = new File(path);
        if (file.isDirectory() && file.exists()) {
            File[] files = file.listFiles();
            assert files != null;
            for (File file1 : files) {
                list.add(file1);
            }
        }
    }

    private boolean verifyIsNull(ArrayList<File> list) {
        for (File file : list) {
            if (file.length() == 0) {
                return true;
            }
        }
        return false;
    }

    public void compilar() {
        long tempoInicial = System.currentTimeMillis();
        long tempoFinal = 0;
        try {
            ProcessBuilder pbCompilacao = new ProcessBuilder("g++", userFile.getName(), "-o", "question");
            pbCompilacao.directory(userFile.getParentFile());
            pbCompilacao.redirectError(new File("error.txt"));
            File error = new File("./error.txt");
            Process process = pbCompilacao.start();
            process.waitFor();
            if (error.length() == 0) {
                for (int i = 0; i < inputs.size(); ++i) {
                    ProcessBuilder pbExecucao = new ProcessBuilder("./question");
                    pbExecucao.redirectInput(inputs.get(i));
                    pbExecucao.redirectOutput(new File(pathOutputUser, "userOut0" + (i + 1) + ".out"));
                    Process processExecucao = pbExecucao.start();
                    processExecucao.waitFor();
                }

            } else {
                System.out.println("Não compilou");
            }
            tempoFinal = System.currentTimeMillis();
            System.out.println("Executado em = " + (tempoFinal - tempoInicial) + " ms");
        } catch (IOException e) {
            System.out.println("Erro de I/O" + e.getMessage());
        } catch (InterruptedException e) {
            System.out.println("Erro de interrupção" + e.getMessage());
        }

    }

    public boolean verifyDiff() {
        carregar(pathOutputUser, outputsUser);
        for (int i = 0; i < outputsUser.size(); ++i) {
            String pathOutputUserTest = outputsUser.get(i).getAbsolutePath();
            String pathOutputExpectedTest = outputsExpecteds.get(i).getAbsolutePath();
            try {
                System.out.println("Comparando " + outputsExpecteds.get(i).getName() + " com " + outputsUser.get(i).getName());
                ProcessBuilder pbDiff = new ProcessBuilder("diff", pathOutputExpectedTest, pathOutputUserTest);
                pbDiff.redirectOutput(new File(pathDiff, "diff0" + (i + 1) + ".out"));
                Process pDiff = pbDiff.start();
                pDiff.waitFor();
            } catch (Exception e) {
                System.out.println(e);
            }
        }
        carregar(pathDiff, diffs);
        if (verifyIsNull(diffs)) {
            return false;
        }
        return true;
    }

    private void Destroy(String path) {
        File file = new File(path);
        if (file.isDirectory() && file.exists()) {
            File[] files = file.listFiles();
            assert files != null;
            for (File file1 : files) {
                file1.delete();
            }
        }
    }

    public  void destroyArquivos() {
        ArrayList<String> paths = new ArrayList<String>();
        paths.add(pathOutputUser);
        paths.add(pathDiff);
        paths.add(pathOutputExpected);
        paths.add(pathInput);

        for (String path : paths) {
            Destroy(path);
        }

        File error = new File("./error.txt");
        error.delete();

        File question = new File(pathQuestion);
        question.delete();
    }
}
