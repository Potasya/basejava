import java.io.File;

/**
 * Created by Marisha on 25/02/2018.
 */
public class MainFile {
    public static void main(String[] args) {
        File root = new File("/Users/Marisha/basejava");
        listFileNames(root, "");
    }
    static void listFileNames(File file, String tab){
        System.out.println(tab + file.getName());
        for (File f : file.listFiles()){
            if (f.isDirectory() && !f.isHidden()){
                listFileNames(f, tab+"\t");
            } else {
                System.out.println(tab+"\t"+f.getName());
            }
        }
    }
}
