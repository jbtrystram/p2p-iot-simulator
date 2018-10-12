package example.update.control;

import peersim.config.Configuration;
import peersim.core.CommonState;
import peersim.core.Control;

/**
 * Created by jibou on 10/10/18.
 */
public class TimeWatchController implements Control {

    final String param;

    public TimeWatchController(String prefix){
        param = Configuration.getString(prefix+"."+ "customSetting", null);
        // control.timewatch example.update.control.TimeWatchController
        // control.timewatch.customSetting whatever
        return ;
    }

    @Override




    public boolean execute() {
        System.out.println("Time is "+ CommonState.getIntTime());
        return false;
    }
}
