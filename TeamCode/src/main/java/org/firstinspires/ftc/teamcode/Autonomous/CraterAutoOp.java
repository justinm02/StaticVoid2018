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
                longitudinalDistance(-8);
                rotateDegrees(-35);
                longitudinalDistance(-24);
                longitudinalDistance(12);
                rotatePreciseDegrees(-55);
                longitudinalDistance(-54);
                break;
            case RIGHT:
                longitudinalDistance(-12);
                rotateDegrees(45);
                longitudinalDistance(-24);
                longitudinalDistance(12);
                rotatePreciseDegrees(-135);
                longitudinalDistance(-54);
                break;
            case CENTER:
            case NULL:
                longitudinalDistance(-36);
                resetPosition();
                rotatePreciseDegrees(-90);
                longitudinalDistance(-54);
                break;
        }
        rotatePreciseDegrees(-40);
        longitudinalDistance(-32);
        dispenseMarker();
        rotateDegrees(-10);
        longitudinalDistance(56, 0.5f);
        park();
    }
}
