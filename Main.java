package lifedata;

import lifedata.analytics.LifeDataPlatform;
import lifedata.cli.LifeDataMenu;
public class Main {

    public static void main(String[] args) {
        LifeDataPlatform platform = new LifeDataPlatform();

        if (args != null && args.length > 0 && ("--demo".equalsIgnoreCase(args[0]) || "--auto".equalsIgnoreCase(args[0]))) {
            platform.runFullEndToEndDemo();
            return;
        }

        // Run full demonstration once, then open interactive console menu
        System.out.println("Initializing LifeData Platform...");
        platform.runFullEndToEndDemo();

        // Launch interactive menu
        LifeDataMenu menu = new LifeDataMenu(platform);
        menu.start();
    }
}
