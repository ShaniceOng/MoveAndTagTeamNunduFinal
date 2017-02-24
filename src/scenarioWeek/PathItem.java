package scenarioWeek;

/**
 * Created by shaniceong on 21/02/2017.
 */
public class PathItem {
    int robot;
    int destination;
    double totalDistance;
    int robotsAvailable = 2;
    double[] nearestArray;
    int len;
    double totalBefore;

    public PathItem(int robot, int destination, double totalDistance, double totalBefore) {
        this.robot = robot;
        this.destination = destination;
        this.totalDistance = totalDistance;
        this.totalBefore = totalBefore;
    }

    public PathItem(int robot, int destination, double totalDistance, double[] nearestArray, int len) {
        this.robot = robot;
        this.destination = destination;
        this.totalDistance = totalDistance;
        this.nearestArray = nearestArray;
        this.len = len;
    }

    public void updateArray (double[] nearestArray) {
        this.nearestArray = nearestArray;
    }

    public void useRobot () {
        this.robotsAvailable--;
    }
}
