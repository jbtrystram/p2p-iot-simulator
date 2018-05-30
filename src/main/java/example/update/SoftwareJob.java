package example.update;

import org.apache.maven.artifact.versioning.ComparableVersion;

import java.security.MessageDigest;
import java.time.LocalDateTime;
import java.util.Arrays;


/**
 * This is the object recevied by the local manager.
 */
public class SoftwareJob {

    // QoS values
    public static final int QOS_REMOVE = 1;
    public static final int QOS_INSTALL_BEST_EFFORT = 10;
    public static final int QOS_INSTALL_MANDATORY = 100;

    // Priorities values
    public static final int PRIORITY_HIGH = 1;
    public static final int PRIORITY_STANDARD = 2;
    public static final int PRIORITY_LOW = 3;

    // Fields

    public final String name;
    public final ComparableVersion version;
    public final LocalDateTime dateCreated ;
    public LocalDateTime dateExp;

    private int priority;
    private int cost;
    int expectedQoS;
    int size;
    private final byte[] id;

    MessageDigest md = null;

    public int getPriority() {
        return priority;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost){ this.cost = cost;}

    public String getId(){ return name+" "+version;} //Arrays.toString(id); }

    public LocalDateTime getDateCreated() {
        return dateCreated;
    }

    public SoftwareJob(String name, String version, String dateExp, int priority, int expectedQoS, int size) {
        this.name = name;
        this.version = new ComparableVersion(version);
        this.dateExp = LocalDateTime.parse(dateExp);
        this.priority = priority;
        this.expectedQoS = expectedQoS;
        this.size = size;

        // cost is -1, as not yet computed
        this.cost = -1;
        this.dateCreated = LocalDateTime.now();


        try {
            md = MessageDigest.getInstance("SHA-256");
        }catch(Exception e) {
            System.err.println("exception in message digest creation : " + e.getMessage());
            System.exit(1);
        }
        this.id = md.digest( name.concat(version).getBytes());
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