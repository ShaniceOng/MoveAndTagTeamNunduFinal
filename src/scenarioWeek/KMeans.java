package scenarioWeek;

/**
 * This website http://www.dataonfocus.com/k-means-clustering-java-code/ was used when writing this code
 */

import java.util.List;
import java.util.ArrayList;
import java.util.Random;

public class KMeans {

    //Number of Clusters. This metric should be related to the number of points
    private int NUM_CLUSTERS = 3;
    //Number of Points
    private int NUM_POINTS = 15;
    //Min and Max X and Y
    private static double MIN_COORDINATE = 0;
    private static double MAX_COORDINATE = 10;

    private ArrayList<Point> points;
    public ArrayList<Cluster> clusters;

    double[][] floyd_table;

    public KMeans() {
        this.points = new ArrayList();
        this.clusters = new ArrayList();
    }

    private double distance(Point p1, Point p2){
        double dist = Math.sqrt((p2.x-p1.x)*(p2.x-p1.x) + (p2.y-p1.y)*(p2.y-p1.y));
        return dist;
    }

    public void doKMeans(int numClusters, int numPoints, ArrayList<Point> points) {
        NUM_CLUSTERS = numClusters;
        NUM_POINTS = numPoints;
        this.points = points;
        setMinAndMaxCoordinates();
        init();
        calculate();
    }

    public void setMinAndMaxCoordinates() {
        double min = Double.MAX_VALUE;
        double max = Double.MIN_VALUE;
        for (Point p : points) {
            if (p.x < min) min = p.x;
            else if (p.x > max) max = p.x;

            if (p.y < min) min = p.y;
            else if (p.y > max) max = p.y;
        }

        this.MAX_COORDINATE = max;
        this.MIN_COORDINATE = min;
    }

    private Point createRandomPoint(double min, double max) {
        Random r = new Random();
        double x = min + (max - min) * r.nextDouble();
        double y = min + (max - min) * r.nextDouble();
        return new Point(x,y);
    }

    private ArrayList createRandomPoints(double min, double max, int number) {
        ArrayList<Point> points = new ArrayList<>(number);
        for(int i = 0; i < number; i++) {
            points.add(createRandomPoint(min,max));
        }
        return points;
    }


    //Initializes the process
    public void init() {
        //Create Points
//        points = createRandomPoints(MIN_COORDINATE,MAX_COORDINATE,NUM_POINTS);

        //Create Clusters
        //Set Random Centroids
        for (int i = 0; i < NUM_CLUSTERS; i++) {
            Cluster cluster = new Cluster(i);
            Point centroid = createRandomPoint(MIN_COORDINATE,MAX_COORDINATE);
            cluster.setCentroid(centroid);
            clusters.add(cluster);
        }

        //Print Initial state
    }

    //The process to calculate the K Means, with iterating method.
    public void calculate() {
        boolean finish = false;
        int iteration = 0;

        // Add in new data, one at a time, recalculating centroids with each new one.
        while(!finish) {
            //Clear cluster state
            clearClusters();

            ArrayList<Point> lastCentroids = getCentroids();

            //Assign points to the closer cluster
            assignCluster();

            //Calculate new centroids.
            calculateCentroids();

            iteration++;

            ArrayList<Point> currentCentroids = getCentroids();

            //Calculates total distance between new and old Centroids
            double distance = 0;
            for(int i = 0; i < lastCentroids.size(); i++) {
                distance += distance(lastCentroids.get(i),currentCentroids.get(i));
            }

            if(distance == 0) {
                finish = true;
            }
        }
    }

    private void clearClusters() {
        for(Cluster cluster : clusters) {
            cluster.clear();
        }
    }

    private ArrayList<Point> getCentroids() {
        ArrayList<Point> centroids = new ArrayList<>(NUM_CLUSTERS);
        for(Cluster cluster : clusters) {
            Point aux = cluster.getCentroid();
            Point point = new Point(aux.x,aux.y);
            centroids.add(point);
        }
        return centroids;
    }

    private void assignCluster() {
        double max = Double.MAX_VALUE;
        double min = max;
        int cluster = 0;
        double distance = 0.0;

        for(Point point : points) {
            min = max;
            for(int i = 0; i < NUM_CLUSTERS; i++) {
                Cluster c = clusters.get(i);
                distance = distance(point, c.getCentroid());
                if(distance < min){
                    min = distance;
                    cluster = i;
                }
            }
            point.setCluster(cluster);
            clusters.get(cluster).addPoint(point);
        }
    }

    public ArrayList<Point> getPoints () {
        return this.points;
    }

    private void calculateCentroids() {
        for(Cluster cluster : clusters) {
            double sumX = 0;
            double sumY = 0;
            ArrayList<Point> list = cluster.getPoints();
            int n_points = list.size();

            for(Point point : list) {
                sumX += point.x;
                sumY += point.y;
            }

            Point centroid = cluster.getCentroid();
            if(n_points > 0) {
                double newX = sumX / n_points;
                double newY = sumY / n_points;
                centroid.x = newX;
                centroid.y = newY;
            }
        }
    }
}