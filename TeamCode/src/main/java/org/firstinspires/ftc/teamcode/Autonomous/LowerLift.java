package org.firstinspires.ftc.teamcode.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import org.firstinspires.ftc.teamcode.Autonomous.AutoOp;

@Autonomous(name = "LowerLift", group = "Autonomous")
public class LowerLift extends AutoOp {

    @Override
    public void runOpMode() {

        initialize();
        waitForStart();
        lowerBot(.2);

    }

}
