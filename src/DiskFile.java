import sun.java2d.pipe.SpanShapeRenderer;

import java.io.IOException;
import java.io.Serializable;
import java.nio.file.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by Michał Martyniak on 14.03.2016.
 */
public class DiskFile implements Serializable, Comparable{

    //privates
    private String name;
    private Path path;
    private long size;
    private String lastModifiedTime;
    private FileKind kind;
    private Set<DiskFile> childFiles;

    @Override
    //default comparator(USELESSSSSS, because its the same as SizeComparator
    //it is required that method takes Object, not DiskFIle class, Otherwise overriding fails
    public int compareTo(Object o) {
        int result_of_comparison=0;

        //if different FileTypes, Directory is always lesser(higher in listing)
        if(this.kind != ((DiskFile)o).kind)
        {
            if(this.getKind() == FileKind.REGULAR_FILE)
                return -1;
            else//o2 is regular file
                return 1;
        }

        //if types are the same, then compare its sizes

        if(this.size < ((DiskFile)o).size)
            result_of_comparison = -1;
        else if(this.size == ((DiskFile)o).size)
            result_of_comparison = 0;
        else//o1 greater than o2
            result_of_comparison = 1;

        return result_of_comparison;
    }

    public DiskFile(Path path, Comparator<DiskFile> choosenComparator) throws IOException {
        //name
        name = path.getFileName().toString();

        //path
        this.path = path;

        //lastModifiedTime formatted in proper format
        DateFormat df = new SimpleDateFormat("[dd.MM.yy hh:mm]");
        lastModifiedTime = df.format(Files.getLastModifiedTime(path).toMillis());

        //childFiles
        if(choosenComparator == null)
            childFiles = new TreeSet<>();
        else
            childFiles = new TreeSet<>(choosenComparator);

        try(DirectoryStream<Path> stream = Files.newDirectoryStream(path)){
            for (Path entry: stream) {
                childFiles.add(new DiskFile(entry,choosenComparator));    //build set of subfiles
            }
            stream.close();
        }catch(IOException | DirectoryIteratorException x){}

        //kind and size
        if(Files.isDirectory(path)){
            kind = FileKind.DIRECTORY;
            size = childFiles.size();
        }
        else{
            kind = FileKind.REGULAR_FILE;
            size = Files.size(path);
        }
    }

    private void listFiles(DiskFile diskFile, String level){

        if(diskFile == this){
            //first iteration display caller's Object attributes
            System.out.println(name + " "+ lastModifiedTime + " K " + "(" + size + ")");
        }

        String indent = "\t";
        for(DiskFile entry : diskFile.childFiles){

            //In order to make further println a lot shorter.
            String entryNameAndModified = level + entry.name + " "+ entry.lastModifiedTime;
            String entrySize = "(" + entry.size + ")";

            if(entry.kind == FileKind.REGULAR_FILE){
                System.out.println(entryNameAndModified + " P " + entrySize);
            }
            else if(entry.kind == FileKind.DIRECTORY){
                System.out.println(entryNameAndModified + " K " + entrySize);
                listFiles(entry,indent + level);        //go deeper to list childrens
            }
        }
    }


    //public
    public String getName() {
        return name;
    }

    public long getSize() {
        return size;
    }

    public String getLastModifiedTime(){
        return lastModifiedTime;
    }

    public FileKind getKind() {
        return kind;
    }

    public String toString(){
        return null;
    }

    public static void main(String[] args) throws IOException {
        Path path = FileSystems.getDefault().getPath(".");
        Comparator<DiskFile> sizeComparator = new SizeComparator();
        Comparator<DiskFile> nameLengthComparator = new NameLengthComparator();

        DiskFile diskFile = new DiskFile(path,null);

        diskFile.listFiles(diskFile,"\t");

    }

}
