package example.update.strategies;

import peersim.core.Protocol;

public class Storage implements Protocol {

    // ------------------------------------------------------------------------
    // Fields
    // ------------------------------------------------------------------------

    example.update.constraints.Storage disk;

    public Storage(String prefix){
        this.disk = new example.update.constraints.Storage();
    }

    @Override
    public Object clone() {
        return new Storage(null);
    }


    // dumb strategy so far.

    public boolean available(int size){
        return (size <= disk.getFreeSpace());
    }

        // TODO NetworkAgent should actually consume the space upon downoad
    public boolean consumeStorage(int size){
        if (available(size)) {
            disk.allocateSpace(size);
            return true;
        }else return false;
    }

    public boolean allocateStorage(int size){
        //TODO naive approach : reserve = consume
        return (consumeStorage(size));
    }

    // initializer method
    public void init(int size){
        disk.setTotalSpace(size);
    }

    // For observer Use
    public int getFreeSpace(){
        return disk.getFreeSpace();
    }

}
