import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class Chat {

    private Map<String, User> usersBase = new HashMap<>();

    public Chat() {
        try {
            BufferedReader reader = new BufferedReader(
                    new FileReader("src/Database.txt"));
            String name;
            while((name = reader.readLine()) != null)
            {
                usersBase.put(name, new User(name, reader.readLine()));
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Map<String, User> getUsersBase() {
        return usersBase;
    }

    public User getUser(String name) {
        return usersBase.get(name);
    }

    public boolean search(String name) {
        return usersBase.containsKey(name);
    }

    public String getAddress(String name) {
        return usersBase.get(name).getIp();
    }

    public void addUser(String name) {
        int count = usersBase.size() + 1;
        String ip;
        if(count < 256) {
            ip = "127.0.0." + count;
        } else if(count < 511) {
            ip = "127.0" + (count - 255) + ".0";
        } else {
            ip = "127.1.1.1";
            System.out.println("Ну хватит..");
        }
        try {
            BufferedWriter writer = new BufferedWriter(
                    new FileWriter("src/Database.txt", true));
            writer.write(name + "\n");
            writer.write(ip + "\n");
            writer.close();
            usersBase.put(name, new User(name, ip));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
