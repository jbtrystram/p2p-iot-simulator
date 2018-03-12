package example.update;

import org.apache.maven.artifact.versioning.ComparableVersion;

import java.time.LocalDateTime;

/**
 * This is the object recevied by the local manager.
 */
public class SoftwareJob {

    // QoS values
    static final int QOS_REMOVE = 1;
    static final int QOS_INSTALL_BEST_EFFORT = 10;
    static final int QOS_INSTALL_MANDATORY = 100;

    // Priorities values
    static final int PRIORITY_HIGH = 1;
    static final int PRIORITY_STANDARD = 2;
    static final int PRIORITY_LOW = 3;

    // Fields

    public final String Name;
    public final ComparableVersion version;
    public final LocalDateTime dateCreated ;
    public LocalDateTime dateExp;

    private int priority;
    int cost;
    int expectedQoS;
    int size;

    public int getPriority() {
        return priority;
    }

    public int getCost() {
        return cost;
    }

    public LocalDateTime getDateCreated() {
        return dateCreated;
    }

    public SoftwareJob(String name, String version, LocalDateTime dateExp, int priority, int expectedQoS, int size) {
        this.Name = name;
        this.version = new ComparableVersion(version);
        this.dateExp = dateExp;
        this.priority = priority;
        this.expectedQoS = expectedQoS;
        this.size = size;

        // cost is -1, as not yet computed
        this.cost = -1;
        this.dateCreated = LocalDateTime.now();
    }

    public boolean isDoable(int bandwitdh){
        LocalDateTime minimumDate =  LocalDateTime.now().plusSeconds(size/bandwitdh);

        if (minimumDate.isAfter(dateExp)) {
            return false;
        }else return true;
    }


    public boolean isExpired(){
        if (dateExp.isBefore(LocalDateTime.now())) {
            return true;
        }
        else return false;
    }
}