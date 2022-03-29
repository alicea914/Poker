
/**
 * Factory class to make Action objects
 *
 * @author alvaro
 * @version 3/27/2022
 */
public class ActionFactory extends Action
{
    public ActionFactory() {
        
    }
    
    public Action getAction(String actionString) {
        switch (actionString) {
            case "*":
                return new ExitAction();
            case "0":
                return new CallAction();
            default:
                return new Action();
        }
    }
}
