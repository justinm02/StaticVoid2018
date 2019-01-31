package org.firstinspires.ftc.teamcode.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import org.firstinspires.ftc.teamcode.Autonomous.AutoOp;

@Autonomous(name = "CraterCenter", group = "Autonomous")
public class CraterAutoCenter extends AutoOp {

    @Override
    public void runOpMode() {
        initialize();
        waitForStart();

        //Descend robot from Lander, move off the hook
        lowerBot();

        //Hits center mineral, moves into friendly team's crater
        longitudinalDistance(-26, 0.3);

    }
}