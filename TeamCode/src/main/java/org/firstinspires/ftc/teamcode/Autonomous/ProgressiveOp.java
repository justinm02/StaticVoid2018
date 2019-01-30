package org.firstinspires.ftc.teamcode.Autonomous;


import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import org.firstinspires.ftc.teamcode.Autonomous.AutoOp;
import org.firstinspires.ftc.teamcode.Robot.BuggleCam;

@Autonomous (name="ProgressiveOp", group = "Autonomous")
public class ProgressiveOp extends AutoOp {

    @Override
    public void runOpMode() {
        /*
        driveTrain = new DriveTrain(
                hardwareMap.get(DcMotorEx.class, "backLeft"),
                hardwareMap.get(DcMotorEx.class, "backRight"),
                hardwareMap.get(DcMotorEx.class, "frontLeft"),
                hardwareMap.get(DcMotorEx.class, "frontRight")
        );
        driveTrain.resetEncoders();
        driveTrain.reverseMotors();

        driveTrain.setTelemetry(this.telemetry);
        //intake = new Intake(hardwareMap.get(DcMotorEx.class, "lift"), null, null, null);

        */

        initialize();
        waitForStart();
        resetHeading();
        park();

        //prospect();

/*        while(opModeIsActive()) {
            telemetry.addData("Position", cam.getGoldPosition());
            telemetry.update();
        }*/
        /*longitudinalDistance(24);
        while(opModeIsActive()) {
            rotate((float) .2);
        }*/

        //longitudinalDistance(-100);
        //lowerBot();

        /*ElapsedTime runtime = new ElapsedTime();
        double time = runtime.milliseconds();

        while(runtime.milliseconds() < time + 10000) {}

        driveTrain.rotateDegrees(90);

        time = runtime.milliseconds();
        while(runtime.milliseconds() < time + 5000) {}*/
    }
}