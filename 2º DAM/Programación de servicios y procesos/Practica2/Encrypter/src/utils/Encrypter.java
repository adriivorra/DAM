package utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Stream;

public class Encrypter {
    private int key;

    public Encrypter(int key) {
        this.key = key;
    }

    public Encrypter() {

    }
    public void encryption (Path pathName) throws ExecutionException, InterruptedException, IOException {

        File directorio = new File(pathName.getParent() + "/encrypted");
        if (!directorio.exists())
            directorio.mkdirs();

        //encriptar
        ExecutorService executor = Executors.newWorkStealingPool();
        Callable<Stream<String>> changeCharacters = changeCharacters(String.valueOf(pathName));
        Future<Stream<String>> future = executor.submit(changeCharacters);

        //contarLetras
        Callable<Integer> characterCounter = characterCounter(String.valueOf(pathName));
        Future<Integer> future2 = executor.submit(characterCounter);
        executor.shutdown();

        File archivo = new File(pathName.getParent().toUri());
        future.get().forEach(linea ->{
            try {
                if (archivo.exists())
                    Files.writeString(pathName.getParent(),"\n" + linea , StandardOpenOption.APPEND);
                else
                    Files.writeString(pathName.getParent() , linea, StandardOpenOption.CREATE);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        Files.writeString(pathName.getParent(),"\n" + "Signature:" + future2.get() , StandardOpenOption.APPEND);


    }

    private static Callable<Stream<String>> changeCharacters(String file) {
        return () -> {
            try{
                Stream<String> stream = Files.lines(Paths.get(file)); {
                    return stream
                            .map(line -> {
                                String linea = "";
                                for (int i = 0 ; i < line.length() ; i++)
                                    linea += (char)(line.charAt(i)+3);
                                return linea;
                            });
                }} catch (IOException ex) {
                System.err.println("Errorreading" + file);
            }
            return null;
        };
    }

    private static Callable<Integer> characterCounter(String file) {
        return () -> {
            try (Stream<String> stream = Files.lines(Paths.get(file))) {
                return stream
                        .mapToInt(line -> {
                            int count = 0;
                            for (int i = 0 ; i < line.length(); i++)
                                if (line.charAt(i) >= 'a' &&line.charAt(i) <= 'e')
                                    count++;
                            return count;
                        })
                        .sum();
            } catch (IOException ex) {
                System.err.println("Errorreading" + file);
            }
            return 0;
        };
    }

    public Callable<Boolean> checkSignatures(Path pathname){
       return null;
    }




}
