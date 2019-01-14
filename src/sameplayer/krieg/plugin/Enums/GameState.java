package sameplayer.krieg.plugin.Enums;

public enum GameState {

    WAITING_QUEUE, //Wartet auf Spieler
    WAITING_START, //Wartet auf Start
    WAITING_FIGHT, //Wartet auf Kampf
    WAITING_RESET_OBJECTIVE, //Wartet auf den MapRESET
    WAITING_REBOOT, //Wartet auf Reboot

    RUNNING_FIGHT, //Im Kampf

    REBOOTING,
    WON,
    SETUP;

}
