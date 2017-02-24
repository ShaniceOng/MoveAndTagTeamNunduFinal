package scenarioWeek;

import java.util.ArrayList;

public class Point {
    public double x;
    public double y;
    public ArrayList<Point> visible;
    public boolean isRobot = false;
    public boolean isAwake = false;
    public int cluster_number;
    public int index;

    public Point(double x, double y){
        this.x = x;
        this.y = y;
        this.visible = new ArrayList<Point>();
    }

    public void setCluster(int n) {
        this.cluster_number = n;
    }

    public int getCluster() {
        return this.cluster_number;
    }

    public boolean isEqual (Point other) {
        return ((this.x==other.x)&&(this.y==other.y));
    }

    public boolean isVisible (Point p) {
        return visible.contains(p);
    }

    public Point(double x, double y, boolean isRobot){
        this.x = x;
        this.y = y;
        this.visible = new ArrayList<Point>();
        this.isRobot = isRobot;
    }

    public void setVisible(ArrayList<Point> visible) {
        this.visible = visible;
    }

    public String toString(){
        return "(" + x + ", " + y + ")";
    }
}
