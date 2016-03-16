import java.util.Comparator;

/**
 * Created by Michał Martyniak on 16.03.2016.
 */
public class SizeComparator implements Comparator<DiskFile>{
    private Comparator comp;

    //nie ma z czym porownac
    public SizeComparator() {
        this.comp = null;
    }
    public SizeComparator(Comparator comp){
        this.comp = comp;
    }

    @Override//DiskFile objects are compared as normal Strings
    public int compare(DiskFile o1,DiskFile o2)
    {
        int result_of_comparison=0;

        //if different FileTypes
        if(o1.getKind() != o2.getKind())
        {
            //if first is a file, must be smaller(higher in console)
            // than a directory
            if(o1.getKind() == FileKind.REGULAR_FILE)
                return -1;
            else//o2 is regular file
                return 1;
        }
        //if types are the same, then compare its sizes

        if(o1.getSize() < o2.getSize())
            result_of_comparison = -1;
        else if(o1.getSize() == o2.getSize())
            result_of_comparison = 0;
        else//o1 greater than o2
            result_of_comparison = 1;

        return result_of_comparison;
    }
}
