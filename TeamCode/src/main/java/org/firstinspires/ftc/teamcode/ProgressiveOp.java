package org.firstinspires.ftc.teamcode;


import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotorEx;

@Autonomous (name="ProgressiveOp", group = "Autonomous")
public class ProgressiveOp extends AutoOp  {

    @Override
    public void runOpMode() throws InterruptedException {
        cam = new BuggleCam(telemetry, hardwareMap.appContext.getResources().getIdentifier(
                "tfodMonitorViewId", "id", hardwareMap.appContext.getPackageName()));
        driveTrain = new DriveTrain(
                hardwareMap.get(DcMotorEx.class, "backLeft"),
                hardwareMap.get(DcMotorEx.class, "backRight"),
                hardwareMap.get(DcMotorEx.class, "frontLeft"),
                hardwareMap.get(DcMotorEx.class, "frontRight")
        );
        driveTrain.resetEncoders();
        driveTrain.progressiveOp();

        waitForStart();

        //findGold();

        driveTrain.longitudinalDistance(2);
        //driveTrain.rotateDegrees(90);
    }
}
