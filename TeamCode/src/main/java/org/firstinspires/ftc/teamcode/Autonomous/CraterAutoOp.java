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

        longitudinalDistance(6);

        rotateDegrees(-90); //turn towards minerals
        //Locates the gold mineral from one of the three given locations
        prospect();

        rotateDegrees(90);
        //Move bot based on where the Gold Mineral is, knocks it, and continues into friendly team's crater
        switch(cam.getGoldPosition()) {
            case LEFT:
                longitudinalDistance(5);
                rotateDegrees(-35);
                longitudinalDistance(24);
                longitudinalDistance(-12);
                rotatePreciseDegrees(-55);
                longitudinalDistance(54);
                break;
            case RIGHT:
                longitudinalDistance(5);
                rotateDegrees(45);
                longitudinalDistance(18);
                longitudinalDistance(-6);
                rotatePreciseDegrees(-135);
                longitudinalDistance(54);
                break;
            case CENTER:
            case NULL:
                longitudinalDistance(18);
                resetPosition();
                rotatePreciseDegrees(-80);
                longitudinalDistance(36);
                break;
        }
        rotateDegrees(125);
        strafe(26, .3, "left");
        strafe(4, .3, "right");
        longitudinalDistance(-40);
        dispenseMarker();
        longitudinalDistance(60, 0.5f);
        park();
    }
}
