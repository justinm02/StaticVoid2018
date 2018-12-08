package org.firstinspires.ftc.teamcode.Autonomous;

import com.qualcomm.ftccommon.SoundPlayer;
import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.ServoControllerEx;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.sun.tools.javac.tree.DCTree;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.teamcode.Robot.BuggleCam;
import org.firstinspires.ftc.teamcode.Robot.DriveTrain;
import org.firstinspires.ftc.teamcode.Robot.Intake;

import java.io.File;

@SuppressWarnings("Duplicates")
public abstract class AutoOp extends LinearOpMode {

    private DcMotorEx rearLeft, rearRight, frontLeft, frontRight;
    private DcMotorEx lift;
    private ElapsedTime runtime;
    private BNO055IMU imu;
    private DcMotorEx[] motors;

    private double desiredHeading;

    private static final double WHEEL_DIAMETER = 4;
    private static final double COUNTS_PER_INCH = (360/(WHEEL_DIAMETER * Math.PI)) * 1.01;

    protected Intake intake;
    protected BuggleCam cam;

    private File surprise = new File("/sdcard/FIRST/blocks/sounds/Africa by Toto.mp3");

    boolean rotatedLeft = false, rotatedRight = false;

    private final double LC = 1680/(Math.PI*3.65625);

    //Order of Operations:
    //
    //1) Lower Robot
    //2) Knock Gold Square
    //3) Place Marker
    //4) Lean on Crater

    public void initialize() {
        runtime = new ElapsedTime();

        //Instantiating the Motors
        rearLeft = hardwareMap.get(DcMotorEx.class, "backLeft");
        rearRight = hardwareMap.get(DcMotorEx.class, "backRight");
        frontLeft = hardwareMap.get(DcMotorEx.class, "frontLeft");
        frontRight = hardwareMap.get(DcMotorEx.class, "frontRight");
        motors = new DcMotorEx[] {this.rearLeft, this.rearRight, this.frontLeft, this.frontRight};

        intake = new Intake(hardwareMap.get(DcMotorEx.class, "lift"), hardwareMap.get(DcMotorEx.class, "noodles"),
                hardwareMap.get(DcMotorEx.class, "slide"),
                hardwareMap.get(DcMotorEx.class, "intakeLift"), hardwareMap.servo.get("basket"));
        intake.setTelemetry(this.telemetry);

        //Reverse the Right Motors
        rearRight.setDirection(DcMotorSimple.Direction.REVERSE);
        frontRight.setDirection(DcMotorSimple.Direction.REVERSE);


        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;

        imu = hardwareMap.get(BNO055IMU.class, "imu");
        imu.initialize(parameters);


        //Instantiates a Camera Object for use with Mineral Detection
        cam = new BuggleCam(telemetry, hardwareMap.appContext.getResources().getIdentifier(
                "tfodMonitorViewId", "id", hardwareMap.appContext.getPackageName()));

        //Set the encoder position back to 0
        resetEncoders();
        intake.resetEncoders();
        intake.lockIntake();
    }

    //Loop required for all Autonomous modes
    @Override
    public abstract void runOpMode();

    //Descend robot from Lander, move off the hook
    protected void lowerBot() {
        intake.resetEncoders();
        runtime.reset();
        intake.liftPosition(-3.3);
        //while(runtime.seconds() < 4 && opModeIsActive()) {}
        intake.resetEncoders();
        intake.lift(0);
    }

    protected void lowerBot(double power) {
        lift.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        lift.setPower(power);
        runtime.reset();
        while(lift.isBusy() && runtime.seconds() < 4 && opModeIsActive()) {}
        lift.setPower(0);
    }

    //Locates the gold mineral from one of the three given locations
    protected void prospect(){
        //While the Gold Position is undetermined, keep updating the camera
        cam.activateTFOD();

        runtime.reset();
        resetHeading();
        rotatedLeft = false;
        rotatedRight = false;

        while(cam.getGoldPosition() == BuggleCam.GOLD_POSITION.NULL && runtime.seconds() < 8 && opModeIsActive()) {
            cam.betterUpdate(telemetry);
            telemetry.update();
            if(runtime.seconds() > 3 && !rotatedLeft) {
                rotateDegrees(-10);
                rotatedLeft = true;
            }
            if(runtime.seconds() > 6 & !rotatedRight && cam.getGoldPosition() == BuggleCam.GOLD_POSITION.NULL) {
                rotateDegrees(20);
                rotatedRight = true;
            }

        }
        if(rotatedLeft && !rotatedRight)
            rotateDegrees(10);
        else if(rotatedLeft && rotatedRight)
            rotateDegrees(-10);
        cam.stopTFOD();

    }

    protected void dispenseMarker() {
        runtime.reset();
        while(runtime.seconds() < 1 && opModeIsActive())
            intake.moveIntake(-.1);
        intake.moveIntake(0);
        runtime.reset();
        while(runtime.seconds() < 2 && opModeIsActive())
            intake.outtake(.6);
        intake.outtake(0);
        runtime.reset();
        while(runtime.seconds() < 1.5)
            intake.moveIntake(.2);
        intake.moveIntake(0);
    }

    protected void playSurprise() {
        SoundPlayer.getInstance().startPlaying(hardwareMap.appContext, surprise);
    }

    public void resetEncoders() {
        for (DcMotorEx motor : motors) {
            motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        }
        for (DcMotorEx motor : motors) {
            motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        }
    }

    public void resetHeading() {
        desiredHeading = currentAngle();
    }

    private double currentAngle() {
        return imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES).firstAngle;
    }

    public void rotate(float power) {
        frontLeft.setPower(power);
        rearLeft.setPower(power);
        frontRight.setPower(-power);
        rearRight.setPower(-power);
    }

    public void rotatePreciseDegrees(double degrees) {
        rotatePreciseDegrees(degrees, 0.4f);
    }

    public void rotatePreciseDegrees(double degrees, float power) {
        desiredHeading -= degrees;
        if(desiredHeading <= -180)
            desiredHeading += 180;
        else if(desiredHeading >= 180)
            desiredHeading -= 180;

        for(DcMotorEx motor : motors) {
            motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }

        while((int) desiredHeading != (int) currentAngle() && opModeIsActive()) {
            telemetry.addData("Status", "Rotating (Precise) " + degrees + " Degrees");
            telemetry.addData("Current Angle", currentAngle());
            telemetry.addData("Desired Angle", desiredHeading);
            telemetry.update();
            if(Math.abs(desiredHeading - currentAngle()) <= 10) {
                if(desiredHeading < currentAngle()) {
                    rotate(power * .15f);
                } else {
                    rotate(-power * .15f);
                }
            } else {
                if(desiredHeading < currentAngle()) {
                    rotate(power * .3f);
                } else {
                    rotate(-power * .3f);
                }
            }
        }

        rotate(0);

        resetEncoders();
    }

    public void rotateDegrees(double degrees) {
        rotateDegrees(degrees, 0.4f);
    }

    public void rotateDegrees(double degrees, float power) {
        desiredHeading -= degrees;
        if(desiredHeading < -180)
            desiredHeading += 180;
        else if(desiredHeading > 180)
            desiredHeading += 180;

        for(DcMotorEx motor : motors) {
            motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }

        while((int) desiredHeading != (int) currentAngle() && opModeIsActive()) {
            telemetry.addData("Status", "Rotating (Coarse) " + degrees + " Degrees");
            telemetry.addData("Current Angle", currentAngle());
            telemetry.addData("Desired Angle", desiredHeading);
            telemetry.update();
            if(Math.abs(desiredHeading - currentAngle()) <= 3) {
                break;
            } else {
                if(desiredHeading < currentAngle()) {
                    rotate(power * .3f);
                } else {
                    rotate(-power * .3f);
                }
            }
        }

        rotate(0);

        resetEncoders();
    }

    public void longitudinalDistance(double inches) {
        longitudinalDistance(inches, 0.2);
    }

    public void longitudinalDistance(double inches, double power) {
        frontLeft.setTargetPosition((int) (inches * COUNTS_PER_INCH));
        frontRight.setTargetPosition((int) (inches * COUNTS_PER_INCH));
        rearLeft.setTargetPosition((int) (inches * COUNTS_PER_INCH));
        rearRight.setTargetPosition((int) (inches * COUNTS_PER_INCH));
        for (DcMotorEx motor : motors) {
            motor.setPower(power);
        }
        while(frontLeft.isBusy() && frontRight.isBusy() && rearLeft.isBusy() && rearRight.isBusy() && opModeIsActive()) {
            telemetry.addData("Status", "Moving");
            telemetry.addData("Desired Heading", desiredHeading);
            telemetry.addData("Current Heading", currentAngle());
            telemetry.update();
        }
        for (DcMotorEx motor : motors) {
            motor.setPower(0);
        }
        resetEncoders();
    }

}
