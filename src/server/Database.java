package server;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class Database {

    private Data data;

    private final String DATA_DIR = "data";
public Database() {
    try{
        FileInputStream fi = new FileInputStream(new File(DATA_DIR));
        ObjectInputStream oi = new ObjectInputStream(fi);

        // Read objects
        data = (Data) oi.readObject();

        oi.close();
        fi.close();
    } catch (IOException | ClassNotFoundException e) {
        e.printStackTrace();
    }
}

        public void save(Data data) {

            try {
                FileOutputStream f = new FileOutputStream(new File(DATA_DIR));
                ObjectOutputStream o = new ObjectOutputStream(f);

                // Write objects to file
                o.writeObject(data);

                o.close();
                f.close();

                FileInputStream fi = new FileInputStream(new File("myObjects.txt"));
                ObjectInputStream oi = new ObjectInputStream(fi);


            } catch (FileNotFoundException e) {
                System.out.println("File not found");
            } catch (IOException e) {
                System.out.println("Error initializing stream");
            }

        }

    public Data getData() {
        return data;
    }
}

