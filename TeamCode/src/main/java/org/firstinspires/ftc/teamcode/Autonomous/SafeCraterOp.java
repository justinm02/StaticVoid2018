package org.firstinspires.ftc.teamcode.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name="Safe Crater Op", group = "Autonomous")
public class SafeCraterOp extends AutoOp {

    @Override
    public void runOpMode() {

        initialize();

        waitForStart();

        lowerBot();

        prospect();

        switch(cam.getGoldPosition()) {
            case RIGHT:
                driveTrain.longitudinalDistance(-12);
                driveTrain.rotatePreciseDegrees(45);
                driveTrain.longitudinalDistance(-24);
                break;
            case LEFT:
                driveTrain.longitudinalDistance(-12);
                driveTrain.rotatePreciseDegrees(-45);
                driveTrain.longitudinalDistance(-24);
                break;
            case CENTER:
            case NULL:
                driveTrain.longitudinalDistance(-36);
                break;
        }

    }

}
