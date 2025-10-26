package util;


import java.io.*;
import java.util.ArrayList;
import java.util.List;


public class FileHandler {
public static <T> void saveList(File file, List<T> list) throws IOException {
try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
oos.writeObject(list);
}
}


@SuppressWarnings("unchecked")
public static <T> List<T> loadList(File file) throws IOException, ClassNotFoundException {
if (!file.exists()) return new ArrayList<>();
try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
return (List<T>) ois.readObject();
}
}
}