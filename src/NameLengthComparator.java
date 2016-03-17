import java.util.Comparator;

/**
 * Created by Michał Martyniak on 17.03.2016.
 */
public class NameLengthComparator implements Comparator<DiskFile> {
        private Comparator comp;

        //nie ma z czym porownac
        public NameLengthComparator() {
            this.comp = null;
        }
        public NameLengthComparator(Comparator comp){
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
            //COMPARING BY NAME LENGTH
            if(o1.getName().length() < o2.getName().length()) {
                result_of_comparison = -1;
            }else if(o1.getName().length() > o2.getName().length())
                result_of_comparison = 1;
            else {//if length of o1 is the same as o2
                result_of_comparison = (o1.getName()).compareTo(o2.getName());
            }


            return result_of_comparison;
        }
}
