package example.update;

import peersim.core.Node;

/**
 * The type of a message. It contains a list of software packages
 * And the sender Node {@link peersim.core.Node}.
 */
public class NetworkMessage {

    final SoftwareJob announcedPackage;
    final Node sender;

    // Constructor
    public NetworkMessage(SoftwareJob announcedPackage, Node sender ) {
        this.announcedPackage = announcedPackage;
        this.sender = sender;
    }
}
