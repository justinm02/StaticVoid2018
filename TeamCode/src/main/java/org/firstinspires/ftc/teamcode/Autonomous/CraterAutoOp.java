package org.firstinspires.ftc.teamcode.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import org.firstinspires.ftc.teamcode.Autonomous.AutoOp;

@Autonomous(name = "CraterOp", group = "Autonomous")
public class CraterAutoOp extends AutoOp {

    @Override
    public void runOpMode() {
        initialize();
        waitForStart();

        //Descend robot from Lander, move off the hook
        lowerBot();

        //Locates the gold mineral from one of the three given locations
        prospect();

        //Move bot based on where the Gold Mineral is, knocks it, and continues into friendly team's crater
        switch(cam.getGoldPosition()) {
            case LEFT:
                driveTrain.longitudinalDistance(-12);
                driveTrain.rotateDegrees(-45);
                driveTrain.longitudinalDistance(-24);
                driveTrain.longitudinalDistance(12);
                driveTrain.rotatePreciseDegrees(135);
                driveTrain.longitudinalDistance(36);
                break;
            case RIGHT:
                driveTrain.longitudinalDistance(-12);
                driveTrain.rotateDegrees(45);
                driveTrain.longitudinalDistance(-24);
                driveTrain.longitudinalDistance(12);
                driveTrain.rotatePreciseDegrees(45);
                driveTrain.longitudinalDistance(48);
                break;
            case CENTER:
            case NULL:
                driveTrain.longitudinalDistance(-36);
                driveTrain.longitudinalDistance(12);
                driveTrain.rotatePreciseDegrees(90);
                driveTrain.rotatePreciseDegrees(48);
                break;
        }
        driveTrain.rotatePreciseDegrees(-45);
        driveTrain.longitudinalDistance(48);
        dispenseMarker();
        driveTrain.longitudinalDistance(-72);
    }
}
