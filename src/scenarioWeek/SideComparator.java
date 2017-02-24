package scenarioWeek;

import java.util.Comparator;

/**
 * Created by shaniceong on 23/02/2017.
 */
class SideComparator implements Comparator<Side>
{
    @Override
    public int compare(Side side1, Side side2)
    {
        if (side1.weight < side2.weight)
            return -1;
        if (side1.weight > side2.weight)
            return 1;
        return 0;
    }
}