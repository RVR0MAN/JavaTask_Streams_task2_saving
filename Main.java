import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class Main {

    static int saveNumber = 1;
    static String userDirectory = "C://Users/Work/Desktop/RV";
    static String saveDirectory = userDirectory + "/Games/savegames";


    public static void main(String[] args) {

        GameProgress firstCheckPoint = new GameProgress(100, 10, 1, 123);
        GameProgress secondCheckPoint = new GameProgress(87, 8, 6, 267);
        GameProgress thirdCheckPoint = new GameProgress(45, 5, 9, 412);

        List saves = new LinkedList();
        saves.add(saveGame(saveDirectory, firstCheckPoint));
        saves.add(saveGame(saveDirectory, secondCheckPoint));
        saves.add(saveGame(saveDirectory, thirdCheckPoint));


        zipFiles(saveDirectory + "/zip_saves.zip", saves);


    }


    public static String saveGame(String directory, GameProgress gameProgress) {
        String save = directory + "/save" + saveNumber + ".dat";
        try (FileOutputStream fileStream = new FileOutputStream(save, false);
             ObjectOutputStream objectStream = new ObjectOutputStream(fileStream)) {
            objectStream.writeObject(gameProgress);

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        saveNumber++;
        return save;
    }


    public static void zipFiles(String directory, List saves) {
        saveNumber = 1;
        try {
            ZipOutputStream zout = new ZipOutputStream(new FileOutputStream(directory));

            for (int i = 0; i < saves.size(); i++) {
                FileInputStream fis = new FileInputStream((String) saves.get(i));
                ZipEntry entry = new ZipEntry("packed_save" + saveNumber + ".dat");
                zout.putNextEntry(entry);
                byte[] buffer = new byte[fis.available()];
                fis.read(buffer);
                zout.write(buffer);
                zout.closeEntry();
                fis.close();
                File cleaner = new File(saveDirectory + "/save" + saveNumber + ".dat");
                cleaner.delete();
                saveNumber++;
            }
            zout.close();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }


    }

}
