package org.firstinspires.ftc.teamcode.Autonomous;


import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

@Autonomous(name = "MarkerOp", group = "Autonomous")
public class MarkerAutoOp extends AutoOp {

    @Override
    public void runOpMode() {
        initialize();

        waitForStart();

        /*while(opModeIsActive()) {
            telemetry.addData("Limit Switch", limitSwitch.getState());
            telemetry.update();
        }*/

        //Descend robot from Lander, move off the hook
        lowerBot();

        rotateDegrees(90);
        //Locates the gold mineral from one of the three given locations
        prospect();

        /*cam.activateTFOD();
        while(opModeIsActive()) {
            cam.betterUpdate(telemetry);
            telemetry.update();
        }
*/
        //Move bot based on where the Gold Mineral is, knocks it, and moves to above the rightmost mineral
        intake.lockIntake();
        switch(cam.getGoldPosition()) {
            case LEFT:
                longitudinalDistance(12);
                rotatePreciseDegrees(-45);
                longitudinalDistance(36);
                rotatePreciseDegrees(90);
                longitudinalDistance(24);
                dispenseMarker();
                rotatePreciseDegrees(3);
                longitudinalDistance(-60, 0.6f);
                park();
                break;
            case RIGHT:
                longitudinalDistance(12);
                rotatePreciseDegrees(45);
                longitudinalDistance(24);
                longitudinalDistance(-18);
                rotatePreciseDegrees(45);
                longitudinalDistance(-60);
                rotatePreciseDegrees(-50);
                longitudinalDistance(54);
                dispenseMarker();
                rotatePreciseDegrees(3);
                longitudinalDistance(-48, 0.6f);
                park();
                break;
            case CENTER:
            case NULL:
                longitudinalDistance(52);
                dispenseMarker();
                resetPosition();
                rotatePreciseDegrees(90);
                longitudinalDistance(-42);
                rotateDegrees(-40);
                longitudinalDistance(-24);
                park();
                break;
        }
    }
}
