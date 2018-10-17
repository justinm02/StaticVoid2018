package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpModeManager;
import com.qualcomm.robotcore.eventloop.opmode.OpModeRegistrar;

public class RegisterOpMode {

    @OpModeRegistrar
    public static void register(OpModeManager manager) {
        manager.register("TestTeleOP", DerpTeleOp.class);
    }
}
