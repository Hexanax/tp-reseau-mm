package server;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Database is the layer responsible for storing data in the server
 */
public class Database {
    /**
     * The data stored by the database
     */
    private Data data;

    private final String DATA_DIR = "data";

    /**
     * Creates a new empty database
     */
    public Database() {
        data = new Data();
        try {
            FileInputStream fi = new FileInputStream(new File(DATA_DIR));
            ObjectInputStream oi = new ObjectInputStream(fi);

            // Read objects
            data = (Data) oi.readObject();

            oi.close();
            fi.close();

        } catch (FileNotFoundException e) {
            System.out.println("Database not found, creating a new one");
            try {
                writeData(data);
            } catch (IOException ioException) {
                System.err.println("Error creating a new database file");
                ioException.printStackTrace();
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Saves the data, replaces the current data files if one already exists
     * @param data the new data
     */
    public void save(Data data) {

        try {
            writeData(data);

        } catch (FileNotFoundException e) {
            System.err.println("File not found");
        } catch (IOException e) {
            System.err.println("Error initializing stream");
        }

    }

    private void writeData(Data data) throws IOException {
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

