package ru.sapteh;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class Main {
    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Input name: ");
        String path = reader.readLine();
        Path sourcePath = Paths.get(path );
        System.out.println("Input name archive: ");
        String zipArch = reader.readLine();
        Path zipPath = Paths.get(zipArch);
        System.out.println("Input name Directory: ");
        String dir = reader.readLine();
        Path dirPath = Paths.get(dir);
        if (!Files.isDirectory(sourcePath)){
            System.out.println("NO directory");
            System.exit(0);
        }

        //Проход по дереву файлов
        MyVisitClass myVisitClass = new MyVisitClass();
        Files.walkFileTree(sourcePath, myVisitClass);

        //Создание архива
        FileOutputStream zipArchive = new FileOutputStream(sourcePath + ".zip");
        ZipOutputStream zip = new ZipOutputStream(zipArchive);
        ZipEntry ze;

       // перенос папок и файлов в архив и вывод размеров
        for(File filePath : myVisitClass.getFileList()) {
            File file = new File(Pruning(path,filePath.getPath()));
            if(filePath.isDirectory()){
                ze = new ZipEntry(file + "/");
                zip.putNextEntry(ze);
                zip.closeEntry();
            } else if(filePath.isFile()){
                ze = new ZipEntry(file.toString());
                zip.putNextEntry(ze);
                Files.copy(filePath.toPath(),zip);
                zip.closeEntry();
                System.out.printf(" %-35s %-5d (%-4d) %.0f%%\n", file.getPath(),  ze.getSize(), ze.getCompressedSize(),
                        (100 - ((double)ze.getCompressedSize()/ ze.getSize()*100)));
            }
        }
//        FileInputStream fis = new FileInputStream(zipPath.toString());
//        ZipInputStream zipFis = new ZipInputStream(fis);
//
//        while (zipFis.available() > 0) {
//            ZipEntry nextEntry = zipFis.getNextEntry();
//            if (nextEntry == null) {
//                for (File filePath : myVisitClass.getFileList()) {
//                    File file = new File(Pruning(zipArch, filePath.getPath()));
//                    if (filePath.isDirectory()) {
//                        nextEntry = new ZipEntry(file + "/");
//                        zipFis.getNextEntry();
//                        zipFis.closeEntry();
//                    } else if (filePath.isFile()) {
//                        nextEntry = new ZipEntry(file.toString());
//                        zip.putNextEntry(nextEntry);
//                        Files.copy(zipPath,dirPath);
//                        zip.closeEntry();
//                        System.out.printf("%-35s %-5d (%-4d)\n", nextEntry, nextEntry.getSize(), nextEntry.getCompressedSize());
//
//                    }
//                }
//            }
//        }

        zip.close();




    }
    // Метод обрезки пути
    public static String Pruning(String path, String filePathPath) {
        Path sourcePath = Paths.get(path);
        Path fullPath = Paths.get(filePathPath);
        int stringSize = sourcePath.getParent().toString().length() + 1;
        String subPath = fullPath.toString().substring(stringSize);
        return subPath;
    }
}

