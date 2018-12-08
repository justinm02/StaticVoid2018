package org.firstinspires.ftc.teamcode.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name="Safe Crater Op", group = "Autonomous")
public class SafeCraterOp extends AutoOp {

    @Override
    public void runOpMode() {

        initialize();

        while(!isStarted()) {
            intake.lift(0);
            telemetry.addData("Started", isStarted());
            telemetry.addData("Is Active", opModeIsActive());
        }

        waitForStart();

        lowerBot();

        prospect();

        intake.lockIntake();
        switch(cam.getGoldPosition()) {
            case RIGHT:
                longitudinalDistance(-12);
                rotatePreciseDegrees(-90);
                longitudinalDistance(18);
                rotatePreciseDegrees(-90);
                longitudinalDistance(12);
                break;
            case LEFT:
                longitudinalDistance(-12);
                rotatePreciseDegrees(90);
                longitudinalDistance(18);
                rotatePreciseDegrees(90);
                longitudinalDistance(12);
                break;
            case CENTER:
            case NULL:
                longitudinalDistance(-30);
                break;
        }

    }

}
