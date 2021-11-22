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
    data = new Data();
    try{
        FileInputStream fi = new FileInputStream(new File(DATA_DIR));
        ObjectInputStream oi = new ObjectInputStream(fi);

        // Read objects
        data = (Data) oi.readObject();

        oi.close();
        fi.close();

    } catch (FileNotFoundException e){
        System.out.println("Database not found, creating a new one");
        try{
            writeData(data);
        } catch (IOException ioException) {
            System.err.println("Error creating a new database file");
            ioException.printStackTrace();
        }
    } catch (IOException | ClassNotFoundException e) {
        e.printStackTrace();
    }
}

        public void save(Data data) {

            try {
                writeData(data);

            } catch (FileNotFoundException e) {
                System.err.println("File not found");
            } catch (IOException e) {
                System.err.println("Error initializing stream");
            }

        }
    private void writeData(Data data) throws  IOException {
        FileOutputStream f = new FileOutputStream(new File(DATA_DIR));
        ObjectOutputStream o = new ObjectOutputStream(f);

        // Write objects to file
        o.writeObject(data);

        o.close();
        f.close();
    }
    public Data getData() {
        return data;
    }
}
